
package com.rackspace.service.tracing;

import javax.security.auth.Destroyable;
import javax.servlet.http.HttpServletRequest;


public interface TracingService extends Destroyable {
   
   public void initialize(String scribeHost, int scribePort);
   
   public void logTraceEvent(GenericTrace genericTrace);

   public boolean isConnectionOpen();
   
   public void closeConnection();
   
   public void setCategory(String category);
}
