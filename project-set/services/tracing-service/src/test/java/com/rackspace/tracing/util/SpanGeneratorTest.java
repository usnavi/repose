/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rackspace.tracing.util;

import com.rackspace.service.tracing.GenericTrace;
import com.rackspace.service.tracing.TraceAnnotation;
import com.rackspace.service.tracing.TraceEndpoint;
import com.twitter.zipkin.gen.Span;
import javax.servlet.http.HttpServletRequest;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.experimental.runners.Enclosed;
import org.junit.runner.RunWith;
import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
@RunWith(Enclosed.class)
public class SpanGeneratorTest {

   public static class WhenGeneratingZipkinSpans {

      private GenericTrace trace;
      private TraceAnnotation annotation;
      private TraceEndpoint endpoint;
      HttpServletRequest request;
      @Before
      public void setUp() {
         
         request = mock(HttpServletRequest.class);
         when(request.getHeader(TracingHeaders.TRACE_ID_HEADER.toString())).thenReturn("1235488");
         when(request.getHeader(TracingHeaders.SPAN_ID_HEADER.toString())).thenReturn("321654");
         
         
         trace = new GenericTraceImpl(request);
      }

      /**
       * Test of generateSpanFromTrace method, of class SpanGenerator.
       */
      @Test
      public void testGenerateSpanFromTrace() {
         annotation = new TraceAnnotationImpl("Repose Test");
         
         trace.annotateEvent(annotation);
         com.twitter.zipkin.gen.Span span = SpanGenerator.generateSpanFromTrace(trace);
         
         assertEquals(span.getTrace_id(), Long.parseLong("1235488"));
      }

      {
      }
   }
}
