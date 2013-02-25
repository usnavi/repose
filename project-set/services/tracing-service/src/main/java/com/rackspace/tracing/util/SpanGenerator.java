/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rackspace.tracing.util;

import com.rackspace.service.tracing.GenericTrace;
import com.rackspace.service.tracing.TraceAnnotation;
import com.rackspace.service.tracing.TraceBinaryAnnotation;
import com.twitter.zipkin.gen.Annotation;
import com.twitter.zipkin.gen.AnnotationType;
import com.twitter.zipkin.gen.BinaryAnnotation;
import com.twitter.zipkin.gen.Endpoint;
import com.twitter.zipkin.gen.Span;
import java.util.ArrayList;
import java.util.List;


public final class SpanGenerator {
   
   private SpanGenerator(){
   }
   
   
   public static Span generateSpanFromTrace(GenericTrace genericTrace){
      
      List<Annotation> annotations = new ArrayList<Annotation>();
      
      for(TraceAnnotation tn : genericTrace.getAnnotations()){
         Annotation ann = new Annotation(tn.getTimeStamp(), tn.getValue());
         if(tn.isEndpointSet()){
            ann.setHost(new Endpoint(Integer.parseInt(tn.getEndpoint().getIp()), Short.parseShort(tn.getEndpoint().getPort()), tn.getEndpoint().getName()));
            
         }
         annotations.add(ann);
      }
      
      List<BinaryAnnotation> binaryAnnotations = new ArrayList<BinaryAnnotation>();
      for(TraceBinaryAnnotation bn: genericTrace.getBinaryAnnotations()){
         
         BinaryAnnotation bnn = new BinaryAnnotation(bn.getKey(), bn.getValue(), AnnotationType.STRING); //Only supporting string binary annotations for now
         if(bn.isEndpointSet()){
            bnn.setHost(new Endpoint(Integer.parseInt(bn.getEndpoint().getIp()), Short.parseShort(bn.getEndpoint().getPort()), bn.getEndpoint().getName()));
         }
         binaryAnnotations.add(bnn);
      }
      
      return new com.twitter.zipkin.gen.Span(Long.parseLong(genericTrace.getTraceId()), genericTrace.getName(), Long.parseLong(genericTrace.getSpanId()), annotations, binaryAnnotations);
      
   }
   
   
}
