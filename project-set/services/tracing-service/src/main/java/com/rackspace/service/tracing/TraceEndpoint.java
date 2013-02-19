/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rackspace.service.tracing;


public class TraceEndpoint {
   
   private String ip;
   private String port;
   private String name;

   public TraceEndpoint(String ip, String port, String name) {
      this.ip = ip;
      this.port = port;
      this.name = name;
   }

   public TraceEndpoint(String ip, String port) {
      this(ip,port,"");
   }
   
   public TraceEndpoint(){
      this("","","");
   }

   public String getIp() {
      return ip;
   }

   public String getPort() {
      return port;
   }

   public String getName() {
      return name;
   }

   public void setIp(String ip) {
      this.ip = ip;
   }

   public void setPort(String port) {
      this.port = port;
   }

   public void setName(String name) {
      this.name = name;
   }
   
   
   
   
}
