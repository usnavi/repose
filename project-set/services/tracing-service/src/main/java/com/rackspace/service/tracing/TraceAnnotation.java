/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rackspace.service.tracing;


public class TraceAnnotation {
   
   private long timeStamp;
   private String value;
   private TraceEndpoint endpoint;
   private String duration;

   public TraceAnnotation(long timeStamp, String value) {
      this.timeStamp = timeStamp;
      this.value = value;
      this.endpoint = new TraceEndpoint();
   }
   
   public TraceAnnotation(String value){
      this.value = value;
      this.timeStamp = System.currentTimeMillis();
      this.endpoint = new TraceEndpoint();
   }


   public void setDuration(String duration) {
      this.duration = duration;
   }

   public long getTimeStamp() {
      return timeStamp;
   }

   public String getValue() {
      return value;
   }


   public String getDuration() {
      return duration;
   }
   
   public boolean isEndpointSet(){
      return endpoint.getIp().isEmpty()|| endpoint.getIp().isEmpty() ? false : true;
   }
   
   public TraceEndpoint getEndpoint(){
      return endpoint;
   }
   
   
   
   
   
}
