/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rackspace.tracing.util;

import com.rackspace.service.tracing.GenericTrace;
import com.rackspace.service.tracing.TraceAnnotation;
import com.twitter.zipkin.gen.Annotation;
import com.twitter.zipkin.gen.BinaryAnnotation;
import com.twitter.zipkin.gen.Endpoint;
import com.twitter.zipkin.gen.Span;
import com.twitter.zipkin.gen.zipkinCoreConstants;
import java.util.ArrayList;
import java.util.List;


public final class SpanGenerator {
   
   private SpanGenerator(){
   }
   
   
   public static Span generateSpanFromTrace(GenericTrace genericTrace){
      
      List<Annotation> annotations = new ArrayList<Annotation>();
      
      for(TraceAnnotation tn : genericTrace.getAnnotations()){
         Annotation ann = new Annotation(tn.getTimeStamp(), zipkinCoreConstants.CLIENT_RECV);
         if(tn.isEndpointSet()){
            ann.setHost(new Endpoint(Integer.parseInt(tn.getEndpoint().getIp()), Short.parseShort(tn.getEndpoint().getPort()), tn.getEndpoint().getName()));
            
         }
         annotations.add(ann);
      }
      
      List<BinaryAnnotation> binaryAnnotations = new ArrayList<BinaryAnnotation>();
      
      return new com.twitter.zipkin.gen.Span(Long.parseLong(genericTrace.getTraceId()), genericTrace.getName(), Long.parseLong(genericTrace.getSpanId()), annotations, binaryAnnotations);
      
   }
   
   
}
