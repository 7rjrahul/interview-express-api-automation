package com.interviewexpress.api.pojos;


import java.io.File;


public class CreateJobRequest
{
   private CreateJobPayload data;
   private File file;

   // 1. Default No-Arg Constructor
   public CreateJobRequest() {}

   // 2. Parameterized Constructor for requests WITH a file attachment
   public CreateJobRequest(CreateJobPayload data, File file)
   {
      this.data = data;
      this.file = file;
   }

   // 3. Parameterized Constructor for requests WITHOUT a file attachment
   public CreateJobRequest(CreateJobPayload data) {
      this.data = data;
      this.file = null;
   }

   // ==========================================
   // GETTERS AND SETTERS
   // ==========================================

   public CreateJobPayload getData() {
      return data;
   }

   public void setData(CreateJobPayload data) {
      this.data = data;
   }

   public File getFile() {
      return file;
   }

   public void setFile(File file) {
      this.file = file;
   }
}