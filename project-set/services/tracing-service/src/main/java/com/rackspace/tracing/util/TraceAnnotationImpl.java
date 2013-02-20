/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rackspace.tracing.util;

import com.rackspace.service.tracing.TraceAnnotation;
import com.rackspace.service.tracing.TraceEndpoint;


public class TraceAnnotationImpl implements TraceAnnotation {
   
   private long timeStamp;
   private String value;
   private TraceEndpointImpl endpoint;
   private String duration;

   public TraceAnnotationImpl(long timeStamp, String value) {
      this.timeStamp = timeStamp;
      this.value = value;
      this.endpoint = new TraceEndpointImpl();
   }
   
   public TraceAnnotationImpl(String value){
      this.value = value;
      this.timeStamp = System.currentTimeMillis();
      this.endpoint = new TraceEndpointImpl();
   }


   @Override
   public void setDuration(String duration) {
      this.duration = duration;
   }

   @Override
   public long getTimeStamp() {
      return timeStamp;
   }

   @Override
   public String getValue() {
      return value;
   }


   @Override
   public String getDuration() {
      return duration;
   }
   
   @Override
   public boolean isEndpointSet(){
      return endpoint.getIp().isEmpty()|| endpoint.getIp().isEmpty() ? false : true;
   }
   
   @Override
   public TraceEndpoint getEndpoint(){
      return endpoint;
   }
   
   
   
   
   
}
