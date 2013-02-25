
package com.rackspace.service.tracing;

import java.nio.ByteBuffer;


public interface TraceBinaryAnnotation {
   
   
   String getKey();
   
   ByteBuffer getValue();
   
   boolean isEndpointSet();
   
   void setEndpoint(TraceEndpoint endpoint);
   
   TraceAnnotationType getType();
   
   TraceEndpoint getEndpoint();
   
}
