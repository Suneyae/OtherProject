 package jnlp.sample.servlet;
 
 public class ErrorResponseException
   extends Exception
 {
   private DownloadResponse _downloadResponse;
   
   public ErrorResponseException(DownloadResponse downloadResponse)
   {
     this._downloadResponse = downloadResponse;
   }
   
   public DownloadResponse getDownloadResponse()
   {
     return this._downloadResponse;
   }
   
   public String toString()
   {
     return this._downloadResponse.toString();
   }
 }