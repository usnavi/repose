/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rackspace.papi.tracing;

import com.rackspace.papi.service.ServiceRegistry;
import com.rackspace.papi.service.context.ServiceContext;
import com.rackspace.papi.service.headers.request.RequestHeaderService;
import com.rackspace.service.tracing.TracingService;
import com.rackspace.tracing.impl.ThriftTracingServiceImpl;
import javax.servlet.ServletContextEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

@Component("tracingServiceContext")
public class TracingServiceContext implements ServiceContext<TracingService> {

   public static final String SERVICE_NAME = "repose:services/thrift-tracing";
   private TracingService service;
   private final ServiceRegistry registry;
  
   @Autowired
   public TracingServiceContext(@Qualifier("serviceRegistry") ServiceRegistry registry, @Qualifier("tracingService") TracingService tracingService){
      this.service = tracingService;
      this.registry = registry;
   }

   public void register() {
        if (registry != null) {
            registry.addService(this);
        }
    }
   @Override
   public String getServiceName() {
      return SERVICE_NAME;
   }

   @Override
   public TracingService getService() {
      return service;
   }

   @Override
   public void contextInitialized(ServletContextEvent sce) {
      
      service.initialize("host", 1463);
      register();
   }

   @Override
   public void contextDestroyed(ServletContextEvent sce) {
      throw new UnsupportedOperationException("Not supported yet.");
   }
}
