/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rackspace.tracing.util;

import com.rackspace.service.tracing.GenericTrace;
import com.rackspace.service.tracing.TraceAnnotation;
import com.rackspace.tracing.util.TracingHeaders;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import javax.servlet.http.HttpServletRequest;

/*
 * 
 */
public class GenericTraceImpl implements GenericTrace {

   private final String DEFAULT_NAME = "Repose Name";
   String traceId;
   String spanId;
   String name;
   String parentId;
   private final Random rnd = new Random();
   private List<TraceAnnotation> annotations;

   public GenericTraceImpl(String traceId, String spanId, String name, String parentId) {
      this(traceId, spanId);
      this.name = name;
      this.parentId = parentId;
      this.name = name;
   }

   public GenericTraceImpl(String traceId, String spanId) {
      this.traceId = traceId;
      this.spanId = spanId;
      this.name = DEFAULT_NAME;
      annotations = new ArrayList<TraceAnnotation>();
   }
   
   

   public GenericTraceImpl(HttpServletRequest request) {
      determineSpan(request);
      this.name = DEFAULT_NAME;
      this.spanId = genId();
      annotations = new ArrayList<TraceAnnotation>();
   }

   private void determineSpan(HttpServletRequest request) {


      this.traceId = request.getHeader(TracingHeaders.TRACE_ID_HEADER.toString()) != null ? request.getHeader(TracingHeaders.TRACE_ID_HEADER.toString()) : genId();
      this.parentId = request.getHeader(TracingHeaders.SPAN_ID_HEADER.toString()) != null ? request.getHeader(TracingHeaders.SPAN_ID_HEADER.toString()) : genId();
   }
   
   private String genId(){
      return String.valueOf(rnd.nextLong());
   }

   @Override
   public void annotateEvent(TraceAnnotation annotation){
      this.annotations.add(annotation);
   }
   
   @Override
   public List<TraceAnnotation> getAnnotations(){
      return this.annotations;
   }

   @Override
   public String getTraceId() {
      return traceId;
   }

   @Override
   public String getSpanId() {
      return spanId;
   }

   @Override
   public String getName() {
      return name;
   }

   @Override
   public String getParentId() {
      return parentId;
   }
   
   
}
