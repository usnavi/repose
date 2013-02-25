/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rackspace.service.tracing;

import com.rackspace.tracing.util.TraceAnnotationImpl;
import java.util.List;


public interface GenericTrace {

   void annotateEvent(TraceAnnotation annotation);
   
   void annotateEvent(TraceBinaryAnnotation annotation);

   List<TraceAnnotation> getAnnotations();
   
   List<TraceBinaryAnnotation> getBinaryAnnotations();

   String getName();

   String getParentId();

   String getSpanId();

   String getTraceId();
   
}
