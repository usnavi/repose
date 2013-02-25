
package com.rackspace.papi.tracing;

import com.rackspace.service.tracing.TraceAnnotationType;
import com.rackspace.service.tracing.TraceBinaryAnnotation;
import com.rackspace.tracing.util.TraceBinaryAnnotationImpl;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.Charset;
import java.nio.charset.CharsetEncoder;
import java.util.ArrayList;
import java.util.List;
import javax.servlet.http.HttpServletRequest;


public final class BinaryAnnotationBuilder {
   
   private BinaryAnnotationBuilder(){
   }
   
   public static List<TraceBinaryAnnotation> buildAnnotationsFromRequest(HttpServletRequest request){
      
      
      List<TraceBinaryAnnotation> annotations = new ArrayList<TraceBinaryAnnotation>();
      
      //TODO: HTTP annotations
      
      //Rackspace Annotations
      if(request.getHeader("x-pp-user")!=null){
         annotations.add(new TraceBinaryAnnotationImpl("rax.username", getBufferFromString(request.getHeader("x-pp-user")), TraceAnnotationType.STRING));
      }
      
      if(request.getHeader("x-tenant-id")!=null){
         annotations.add(new TraceBinaryAnnotationImpl("rax.tenant_id", getBufferFromString(request.getHeader("x-tenant-id")), TraceAnnotationType.STRING));
      }
      
      annotations.add(new TraceBinaryAnnotationImpl("rax.hostname", getBufferFromString(request.getLocalAddr()), TraceAnnotationType.STRING));
      
      
      
      return annotations;
   }
   
   public static ByteBuffer getBufferFromString(String st){
      
      return ByteBuffer.wrap(st.getBytes());
   }
}
