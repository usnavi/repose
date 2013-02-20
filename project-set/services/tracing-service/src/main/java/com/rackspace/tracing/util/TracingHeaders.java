/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rackspace.tracing.util;


public enum TracingHeaders {
   
   TRACE_ID_HEADER("X-B3-TraceId"),
   SPAN_ID_HEADER("X-B3-SpanId"),
   PARENT_SPAN_ID_HEADER("X-B3-ParentSpanId");
   
   String header;
   
   TracingHeaders(String header){
      this.header = header.toLowerCase();
   }
   
   public String toString(){
      return header;
   }
   
}
