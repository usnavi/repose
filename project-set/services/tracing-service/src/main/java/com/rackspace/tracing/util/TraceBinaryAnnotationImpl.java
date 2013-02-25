/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rackspace.tracing.util;

import com.rackspace.service.tracing.TraceAnnotationType;
import com.rackspace.service.tracing.TraceBinaryAnnotation;
import com.rackspace.service.tracing.TraceEndpoint;
import java.nio.ByteBuffer;


public class TraceBinaryAnnotationImpl implements TraceBinaryAnnotation{

   private String key;
   private ByteBuffer value;
   private TraceAnnotationType type;
   private TraceEndpoint endpoint;
   private boolean endpointSet;
   
   public TraceBinaryAnnotationImpl(String key, ByteBuffer value, TraceAnnotationType type) {
      this.key = key;
      this.value = value;
      this.type = type;
      this.endpointSet = false;
   }

   public TraceBinaryAnnotationImpl(String key, ByteBuffer value, TraceAnnotationType type, TraceEndpoint endpoint) {
      this.key = key;
      this.value = value;
      this.type = type;
      this.endpoint = endpoint;
      this.endpointSet = true;
   }
   
   
   @Override
   public String getKey() {
      return key;
   }

   @Override
   public ByteBuffer getValue() {
      return value;
   }

   @Override
   public boolean isEndpointSet() {
      return endpointSet;
   }

   @Override
   public void setEndpoint(TraceEndpoint endpoint) {
      this.endpoint = endpoint;
      this.endpointSet = true;
      
      
   }

   @Override
   public TraceAnnotationType getType() {
      return type;
   }
   
   @Override
   public TraceEndpoint getEndpoint(){
      return endpoint;
   }
   
}
