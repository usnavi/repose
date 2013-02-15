/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rackspace.tracing.impl;

import com.rackspace.service.tracing.TracingService;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.Charset;
import java.nio.charset.CharsetEncoder;
import java.util.ArrayList;
import java.util.List;
import javax.security.auth.DestroyFailedException;
import javax.servlet.http.HttpServletRequest;

import org.apache.thrift.protocol.TBinaryProtocol;
import org.apache.thrift.transport.TSocket;
import org.apache.thrift.transport.TFramedTransport;
import org.apache.thrift.transport.TTransportException;

import org.apache.flume.source.scribe.Scribe.Client;
import org.apache.flume.source.scribe.LogEntry;
import org.slf4j.Logger;
import org.springframework.stereotype.Component;

@Component("tracingService")
public class ThriftTracingServiceImpl implements TracingService {

   private String scribeHost;
   private int scribePort;
   private static final String SCRIBE_CATEGORY = "Repose";
   private List<LogEntry> logEntries;
   private TFramedTransport transport;
   private Client client;
   public Charset charset = Charset.forName("UTF-8");
   public CharsetEncoder encoder = charset.newEncoder();
   private static final Logger LOG = org.slf4j.LoggerFactory.getLogger(ThriftTracingServiceImpl.class);
   private String category;
   
   
   public ThriftTracingServiceImpl(){}
   
   
   @Override
   public void initialize(String scribeHost, int scribePort){
      this.scribeHost = scribeHost;
      this.scribePort = scribePort;
      this.category = SCRIBE_CATEGORY;
      
      if(isConnectionOpen()){
         transport.close();
      }
      connect();
   }

   private void configureClient() {
      try {

         logEntries = new ArrayList<LogEntry>(1);
         TSocket sock = new TSocket(new Socket(scribeHost, scribePort));
         transport = new TFramedTransport(sock);
         TBinaryProtocol protocol = new TBinaryProtocol(transport, false, false);
         client = new Client(protocol, protocol);

      } catch (TTransportException e) {
         LOG.error("Unable to create socket for configured scribe host: " + scribeHost, e);
      } catch (Exception e) {
         LOG.error("Unable to connect to scribe instance: " + scribeHost + ":" + scribePort, e);
      }
   }

   private void connect() {
      if (transport != null && transport.isOpen()) {
         return;
      }

      if (transport != null && transport.isOpen() == false) {
         transport.close();

      }
      configureClient();
   }

   @Override
   public boolean isConnectionOpen() {
      return transport != null && transport.isOpen();
   }

   @Override
   public void logTraceEvent(HttpServletRequest request) {

      if (isConnectionOpen()) {
         connect();

         try {
            //String message = String.format("%s %s %s", hostname, layout.format(event), stackTrace.toString());
            String message = String.format("%s %s %s", scribeHost, "Message", request.getPathInfo());
            LogEntry entry = new LogEntry(category, encodeMessage(message));
            //LogEntry entry = new LogEntry(scribeHost, null);
            logEntries.add(entry);
            client.Log(logEntries);
         } catch (TTransportException e) {
            LOG.warn("Unable to open transport to scribe host: " + scribeHost + ":" + scribePort);
            transport.close();
         } catch (Exception e) {
            System.err.println(e);
         } finally {
            logEntries.clear();
         }


      }
   }

   private ByteBuffer encodeMessage(String message) {
      try {
         return encoder.encode(CharBuffer.wrap(message));
      } catch (Exception e) {
         LOG.error("Unable to encode messge for tracing: " + message, e);
      }
      return null;
   }
   
   @Override
   public void closeConnection(){
      transport.close();
   }

   @Override
   public void destroy() throws DestroyFailedException {
      if(isConnectionOpen()){
         transport.close();
      }
   }

   @Override
   public boolean isDestroyed() {
      return !isConnectionOpen();
   }

   @Override
   public void setCategory(String category) {
      this.category = category;
   }
}
