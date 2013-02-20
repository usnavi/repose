/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rackspace.service.tracing;

import com.rackspace.tracing.util.TraceAnnotationImpl;
import java.util.List;


public interface GenericTrace {

   void annotateEvent(TraceAnnotation annotation);

   List<TraceAnnotation> getAnnotations();

   String getName();

   String getParentId();

   String getSpanId();

   String getTraceId();
   
}
