/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rackspace.papi.tracing;

import com.rackspace.papi.commons.config.manager.UpdateListener;
import com.rackspace.papi.container.config.ContainerConfiguration;
import com.rackspace.papi.container.config.TracingConfiguration;
import com.rackspace.papi.service.ServiceRegistry;
import com.rackspace.papi.service.config.ConfigurationService;
import com.rackspace.papi.service.context.ServiceContext;
import com.rackspace.papi.service.deploy.ContainerConfigurationListener;
import com.rackspace.papi.service.headers.request.RequestHeaderService;
import com.rackspace.service.tracing.TracingService;
import com.rackspace.service.tracing.zipkin.ZipkinTracingServiceImpl;
import java.net.URL;
import javax.servlet.ServletContextEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

@Component("tracingServiceContext")
public class TracingServiceContext implements ServiceContext<TracingService> {

   public static final String SERVICE_NAME = "repose:services/thrift-tracing";
   private TracingService service;
   private final ServiceRegistry registry;
   private final ContainerConfigurationListener containerConfigurationListener;
   private final ConfigurationService configurationManager;

   @Autowired
   public TracingServiceContext(@Qualifier("serviceRegistry") ServiceRegistry registry, @Qualifier("tracingService") TracingService tracingService,
           @Qualifier("configurationManager") ConfigurationService configurationManager) {
      this.service = tracingService;
      this.registry = registry;
      this.containerConfigurationListener = new ContainerConfigurationListener();
      this.configurationManager = configurationManager;
   }

   public void register() {
      if (registry != null) {
         registry.addService(this);
      }
   }

   private class ContainerConfigurationListener implements UpdateListener<ContainerConfiguration> {

      private boolean isInitialized = false;

      @Override
      public void configurationUpdated(ContainerConfiguration configurationObject) {

         TracingConfiguration cfg = configurationObject.getDeploymentConfig().getTracingConfiguration();

         if (cfg != null) {
            service.initialize(cfg.getHostname(), cfg.getPort());
            service.setCategory(cfg.getCategory());
         } else if (service.isConnectionOpen()){
            service.closeConnection();
         }

         isInitialized = true;
      }

      @Override
      public boolean isInitialized() {
         return this.isInitialized;
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
      URL containerXsdURL = getClass().getResource("/META-INF/schema/container/container-configuration.xsd");
      configurationManager.subscribeTo("container.cfg.xml", containerXsdURL, containerConfigurationListener, ContainerConfiguration.class);
      register();
   }

   @Override
   public void contextDestroyed(ServletContextEvent sce) {
      if(service.isConnectionOpen()){
         service.closeConnection();
      }
      configurationManager.unsubscribeFrom("container.cfg.xml", containerConfigurationListener);
   }
}
