/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rackspace.service.tracing;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import javax.servlet.http.HttpServletRequest;

/*
 * 
 */
public class GenericTrace {

   private final String TRACE_ID_HEADER = "x-b3-traceid";
   private final String SPAN_ID_HEADER = "x-b3-spanid";
   private final String PARENT_SPAN_ID_HEADER = "x-b3-parentspanid";
   private final String DEFAULT_NAME = "Repose Name";
   String traceId;
   String spanId;
   String name;
   String parentId;
   private final Random rnd = new Random();
   private List<TraceAnnotation> annotations;

   public GenericTrace(String traceId, String spanId, String name, String parentId) {
      this(traceId, spanId);
      this.name = name;
      this.parentId = parentId;
      this.name = name;
   }

   public GenericTrace(String traceId, String spanId) {
      this.traceId = traceId;
      this.spanId = spanId;
      this.name = DEFAULT_NAME;
      annotations = new ArrayList<TraceAnnotation>();
   }
   
   

   public GenericTrace(HttpServletRequest request) {
      determineSpan(request);
      this.name = DEFAULT_NAME;
      this.spanId = genId();
      annotations = new ArrayList<TraceAnnotation>();
   }

   private void determineSpan(HttpServletRequest request) {


      this.traceId = request.getHeader(TRACE_ID_HEADER) != null ? request.getHeader(TRACE_ID_HEADER) : genId();
      this.parentId = request.getHeader(SPAN_ID_HEADER) != null ? request.getHeader(SPAN_ID_HEADER) : genId();
   }
   
   private String genId(){
      return String.valueOf(rnd.nextLong());
   }

   public void annotateEvent(TraceAnnotation annotation){
      this.annotations.add(annotation);
   }
   
   public List<TraceAnnotation> getAnnotations(){
      return this.annotations;
   }

   public String getTraceId() {
      return traceId;
   }

   public String getSpanId() {
      return spanId;
   }

   public String getName() {
      return name;
   }

   public String getParentId() {
      return parentId;
   }
   
   
}
