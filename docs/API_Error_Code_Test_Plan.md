# Master Test Plan — API Error Code Validation

A reusable checklist for validating error responses on any REST API endpoint
(GET, POST, PUT, DELETE). Written while automating the Jobs API
(`interview-express-api-automation`), but the categories below are
transport/auth/validation-layer concerns — they apply to almost any endpoint.

---

## How to use this plan

1. Open the API doc for the endpoint you're testing.
2. Go category by category below. For each one, check: **does this doc even
   define this status code for this endpoint?** Not every code applies to
   every endpoint (e.g. 404 rarely applies to a POST that creates a new
   resource).
3. Write one test per applicable case.
4. Assert BOTH the HTTP status code AND the response body shape
   (`ErrorResponse` / `HTTPValidationError` POJO).
5. If actual behavior doesn't match the doc, that's a bug — log it, don't
   "fix" your test to hide it.

---

## 1. Authentication Failures — 401 Unauthorized

| Case | How to trigger | Expected |
|---|---|---|
| Missing key | Don't send `X-Api-Key` header at all | 401 |
| Wrong/invalid key | Send a syntactically valid but incorrect key | 401 |
| Empty string key | Send header with value `""` | 401 (checks for "empty ≠ missing" bugs) |
| Whitespace-only key (optional) | Send header with value `"   "` | 401 |

> Skip "null" as a separate case — in Java, not setting the header IS the
> "missing key" case, so it's already covered.

**Applies to:** GET, POST, PUT, DELETE — identical for all, since auth
usually happens before the request body/method is even processed.

---

## 2. Authorization Failures — 403 Forbidden

| Case | How to trigger | Expected |
|---|---|---|
| Valid key, insufficient role/permission | Use a valid key that lacks access to this specific action/resource | 403 |

> **Check first:** does this API even have role-based access control? If
> auth is binary (valid key = full access, invalid = 401), 403 may not be
> testable at all for this endpoint. Confirm in the doc before writing a
> test you can't actually trigger.

**Applies to:** more relevant for POST/PUT/DELETE (write actions often have
stricter permission checks than read-only GETs).

---

## 3. Resource Not Found — 404 Not Found

| Case | How to trigger | Expected |
|---|---|---|
| Non-existent ID (GET/PUT/DELETE by ID) | Call with a random/fake ID (e.g. UUID or `999999`) | 404 |
| Malformed ID format | Call with an ID that doesn't match expected format (e.g. `"abc"` instead of a UUID) | 404 or 400 — check doc for which |

> **Doesn't apply** to list endpoints with no path param (e.g. `GET /v1/jobs`)
> or to plain POST (creating a new resource) — check the doc to confirm 404
> is even a valid response for the endpoint you're testing.

**Applies to:** GET by ID, PUT by ID, DELETE by ID.

---

## 4. Malformed Request — 400 Bad Request

The request itself is broken at the transport/syntax level — the server
can't even properly parse or route it, regardless of what data it contains.

| Case | How to trigger | Expected |
|---|---|---|
| Malformed JSON body | Send broken/invalid JSON syntax (e.g. missing closing brace, trailing comma) | 400 |
| Wrong Content-Type header | Send JSON body but declare `Content-Type: text/plain` (or vice versa) | 400 |
| Invalid/garbage query param syntax | e.g. `page=abc` where the param expects a number — some APIs return 400 here instead of 422 (check the doc) | 400 |
| Invalid URL-encoded characters | Malformed encoding in query string | 400 |

> Rule of thumb: 400 = "I can't even understand this request." 422 = "I
> understood the request, but the data in it fails my rules."
> Some APIs blur this line — always confirm which one the doc actually
> documents for a given trigger before writing the assertion.

**Applies to:** GET (query param syntax), POST/PUT (body syntax, headers) —
essentially any endpoint that parses input.

---

## 5. Semantic Validation Failure — 422 Unprocessable Entity

The request is syntactically valid (parses fine), but the actual data
violates business/schema rules. Very common in FastAPI-style APIs — this is
exactly what your `HTTPValidationError` / `ValidationErrorDetail` POJOs are
built for.

| Case | How to trigger | Expected |
|---|---|---|
| Missing required field (POST/PUT body) | Omit a mandatory field from the JSON payload | 422 |
| Wrong data type in body | e.g. send `minExperienceYears: "five"` instead of a number | 422 |
| Invalid enum value | e.g. `status=notarealstatus`, `department=DoesNotExist` | 422 — or check if it silently returns unfiltered results instead (real bug pattern already found with `department` on this API) |
| Value out of allowed range | e.g. `pageSize=50000` when max allowed is 100 | 422 |
| Invalid query param value | e.g. `page=-1` (negative, semantically invalid even though syntactically a valid number) | 422 |

> This is the category most likely to reveal real bugs — silently accepting
> bad input instead of rejecting it is a very common defect. Always check:
> does the API reject it, or does it quietly ignore the bad value and
> proceed anyway?

**Applies to:** GET (query params), POST/PUT (request body + query params)
most heavily — POST/PUT will need far more test cases here since request
bodies have many fields to validate.

---

## 6. Server Errors — 500 Internal Server Error

| Case | How to trigger | Expected |
|---|---|---|
| Extreme/edge input that might crash server logic | e.g. extremely long strings, special characters, huge pageSize values | Should still be handled gracefully (400/422), NOT 500 |

> You're not trying to intentionally crash the server — you're checking that
> edge-case input is handled gracefully rather than causing an unhandled
> exception. If you get a 500, that's always a bug worth reporting.

**Applies to:** all methods, but especially POST/PUT with complex bodies.

---

## Standard assertion pattern (use for every case above)

```java
@Test(description = "401: Invalid API key")
public void testInvalidApiKey() {
    Response response = jobsClient.getAllJobsWithCustomAuth("aih_invalid_key_123");

    // 1. Assert status code
    Assert.assertEquals(response.getStatusCode(), 401, "Expected 401 for invalid API key");

    // 2. Deserialize body into POJO
    ErrorResponse errorResponse = response.as(ErrorResponse.class);

    // 3. Assert body shape/content
    Assert.assertNotNull(errorResponse.getError(), "Error field should not be null");
    Assert.assertNotNull(errorResponse.getMessage(), "Message field should not be null");

    // 4. Print for debugging/CI logs
    System.out.println("---- Full Error Response Body ----");
    response.prettyPrint();
}
```

For 422 validation errors specifically, use `HTTPValidationError` /
`ValidationErrorDetail` POJOs instead of `ErrorResponse`, since FastAPI-style
422 bodies have a different shape (`detail` array with `loc`, `msg`, `type`).

---

## Execution order (recommended)

1. ✅ 401 — missing key (done)
2. 401 — wrong key
3. 401 — empty key
4. 403 — only if role-based access exists for this endpoint
5. 404 — non-existent ID
6. 400 — malformed request (broken JSON, wrong Content-Type)
7. 422 — semantic validation failures (invalid enum, missing field, out-of-range)
8. 500 — edge-case/extreme input (lower priority, do last)

---

## Notes on reusability across GET/POST/PUT/DELETE

- **401/403** — nearly identical tests across all methods; only the client
  method changes (which endpoint you're hitting), the auth logic is the same.
- **404** — only relevant where a resource ID is involved (GET/PUT/DELETE by
  ID); not applicable to list GETs or plain POST (create).
- **400** — mostly about malformed syntax; fewer test cases needed overall,
  but relevant to any endpoint that parses a body or complex query string.
- **422** — this is where POST/PUT need the most dedicated test cases,
  since they have request bodies with many fields to validate, vs GET which
  mostly only has query params.
- **500** — same graceful-handling philosophy everywhere.

Bottom line: build this once per API client method, and copy/adapt the
pattern for each new endpoint you automate.
