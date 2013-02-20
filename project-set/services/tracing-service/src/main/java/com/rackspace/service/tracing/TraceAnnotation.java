/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rackspace.service.tracing;

import com.rackspace.tracing.util.TraceEndpointImpl;

public interface TraceAnnotation {

   String getDuration();

   TraceEndpoint getEndpoint();

   long getTimeStamp();

   String getValue();

   boolean isEndpointSet();

   void setDuration(String duration);
   
}
