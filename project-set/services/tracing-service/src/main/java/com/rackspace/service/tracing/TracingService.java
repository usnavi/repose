
package com.rackspace.service.tracing;

import com.rackspace.tracing.util.GenericTraceImpl;
import javax.security.auth.Destroyable;
import javax.servlet.http.HttpServletRequest;


public interface TracingService extends Destroyable {
   
   public void initialize(String scribeHost, int scribePort);
   
   public void logTraceEvent(GenericTrace genericTrace);

   public boolean isConnectionOpen();
   
   public void closeConnection();
   
   public void setCategory(String category);
}
