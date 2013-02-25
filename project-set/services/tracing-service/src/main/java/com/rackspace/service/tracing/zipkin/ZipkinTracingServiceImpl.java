/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rackspace.service.tracing.zipkin;

import com.rackspace.service.tracing.GenericTrace;
import com.rackspace.service.tracing.TracingService;
import com.rackspace.tracing.util.SpanGenerator;
import com.twitter.util.Base64StringEncoder$;
import com.twitter.zipkin.gen.Endpoint;
import com.twitter.zipkin.gen.ResultCode;
import com.twitter.zipkin.gen.Span;
import com.twitter.zipkin.gen.ZipkinCollector;
import java.io.ByteArrayOutputStream;
import java.net.Socket;
import java.nio.charset.Charset;
import java.nio.charset.CharsetEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import javax.security.auth.DestroyFailedException;

import org.apache.thrift.protocol.TBinaryProtocol;
import org.apache.thrift.transport.TSocket;
import org.apache.thrift.transport.TFramedTransport;
import org.apache.thrift.transport.TTransportException;

import org.apache.thrift.TException;
import org.apache.thrift.protocol.TProtocol;
import org.apache.thrift.protocol.TProtocolFactory;
import org.apache.thrift.transport.TIOStreamTransport;
import org.slf4j.Logger;
import org.springframework.stereotype.Component;

@Component("tracingService")
public class ZipkinTracingServiceImpl implements TracingService {

   private String scribeHost;
   private int scribePort;
   private static final String SCRIBE_CATEGORY = "Repose";
   private List<com.twitter.zipkin.gen.LogEntry> logEntries;
   private TFramedTransport transport;
   private ZipkinCollector.Client client;
   public Charset charset = Charset.forName("UTF-8");
   public CharsetEncoder encoder = charset.newEncoder();
   private static final Logger LOG = org.slf4j.LoggerFactory.getLogger(ZipkinTracingServiceImpl.class);
   private String category;
   private TProtocolFactory protocol;
   private final Random rnd = new Random();

   public ZipkinTracingServiceImpl() {
   }

   @Override
   public void initialize(String scribeHost, int scribePort) {
      this.scribeHost = scribeHost;
      this.scribePort = scribePort;
      this.category = SCRIBE_CATEGORY;
      this.protocol = new TBinaryProtocol.Factory();
      if (isConnectionOpen()) {
         transport.close();
      }
      connect();
   }

   private void configureClient() {
      try {

         logEntries = new ArrayList<com.twitter.zipkin.gen.LogEntry>(1);
         TSocket sock = new TSocket(new Socket(scribeHost, scribePort));
         transport = new TFramedTransport(sock);
         TBinaryProtocol tbprotocol = new TBinaryProtocol(transport, false, false);

         //client = new Client(protocol, protocol);

         client = new ZipkinCollector.Client(tbprotocol);

      } catch (TTransportException e) {
         LOG.error("Unable to create socket for configured scribe host: " + scribeHost, e);
      } catch (Exception e) {
         LOG.error("Unable to connect to scribe instance: " + scribeHost + ":" + scribePort, e.getMessage());
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
   public void logTraceEvent(GenericTrace trace) {

      if (isConnectionOpen()) {
         connect();
         
         ResultCode code;
         try {            
            String message = encodeSpan(SpanGenerator.generateSpanFromTrace(trace));
            com.twitter.zipkin.gen.LogEntry entry = new com.twitter.zipkin.gen.LogEntry(category, message);
            logEntries.add(entry);

            code = client.Log(logEntries);
         } catch (TTransportException e) {
            LOG.warn("Unable to open transport to scribe host: " + scribeHost + ":" + scribePort);
            transport.close();
         } catch (Exception e) {
            LOG.warn("Error in sending trace request", e);
         } finally {
            logEntries.clear();
         }
      }
   }

   @Override
   public void closeConnection() {
      transport.close();
   }

   @Override
   public void destroy() throws DestroyFailedException {
      if (isConnectionOpen()) {
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

   private Endpoint createEndpoint() {
      return new Endpoint(0x1234, Short.valueOf("1643"), scribeHost);
   }

   private String encodeSpan(Span thriftSpan) throws TException {
      return Base64StringEncoder$.MODULE$.encode(spanToBytes(thriftSpan));
   }

   private byte[] spanToBytes(Span thriftSpan) throws TException {
      final ByteArrayOutputStream buf = new ByteArrayOutputStream();
      final TProtocol proto = protocol.getProtocol(new TIOStreamTransport(buf));
      thriftSpan.write(proto);
      return buf.toByteArray();
   }
}
