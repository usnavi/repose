/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rackspace.tracing.util;

import com.rackspace.service.tracing.TraceEndpoint;


public class TraceEndpointImpl implements TraceEndpoint {
   
   private String ip;
   private String port;
   private String name;

   public TraceEndpointImpl(String ip, String port, String name) {
      this.ip = ip;
      this.port = port;
      this.name = name;
   }

   public TraceEndpointImpl(String ip, String port) {
      this(ip,port,"");
   }
   
   public TraceEndpointImpl(){
      this("","","");
   }

   @Override
   public String getIp() {
      return ip;
   }

   @Override
   public String getPort() {
      return port;
   }

   @Override
   public String getName() {
      return name;
   }

   @Override
   public void setIp(String ip) {
      this.ip = ip;
   }

   @Override
   public void setPort(String port) {
      this.port = port;
   }

   @Override
   public void setName(String name) {
      this.name = name;
   }
   
   
   
   
}
