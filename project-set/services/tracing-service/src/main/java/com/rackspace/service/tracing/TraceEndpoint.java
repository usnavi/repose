/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rackspace.service.tracing;


public interface TraceEndpoint {

   String getIp();

   String getName();

   String getPort();

   void setIp(String ip);

   void setName(String name);

   void setPort(String port);
   
}
