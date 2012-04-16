package com.rackspace.papi.components.versioning.domain;

import com.rackspace.papi.commons.util.StringUriUtilities;
import static com.rackspace.papi.commons.util.StringUriUtilities.indexOfUriFragment;
import com.rackspace.papi.commons.util.StringUtilities;
import com.rackspace.papi.commons.util.string.JCharSequenceFactory;
import com.rackspace.papi.components.versioning.config.ServiceVersionMapping;
import com.rackspace.papi.components.versioning.util.http.HttpRequestInfo;
import com.rackspace.papi.components.versioning.util.http.UniformResourceInfo;

public class VersionedRequest {

   private final HttpRequestInfo requestInfo;
   private final ServiceVersionMapping mapping;
   private final String clientAddressedHost;

   public VersionedRequest(HttpRequestInfo requestInfo, ServiceVersionMapping mapping) {
      this.requestInfo = requestInfo;
      this.mapping = mapping;
      this.clientAddressedHost = requestInfo.getHost();
   }

   public ServiceVersionMapping getMapping() {
      return mapping;
   }

   public HttpRequestInfo getRequestInfo() {
      return requestInfo;
   }

   public String getHost() {
      return clientAddressedHost;
   }

   public boolean isRequestForRoot() {
      return StringUriUtilities.formatUri(requestInfo.getUri()).isEmpty();
   }

   public boolean requestBelongsToVersionMapping() {
      final String requestedUri = StringUriUtilities.formatUri(requestInfo.getUri());
      final String versionUri = StringUriUtilities.formatUri(mapping.getId());

      return indexOfUriFragment(JCharSequenceFactory.jchars(requestedUri), versionUri) == 0;
   }

   public boolean requestMatchesVersionMapping() {
      final String requestedUri = StringUriUtilities.formatUri(requestInfo.getUri());

      return requestedUri.equals(StringUriUtilities.formatUri(mapping.getId()));
   }

   public String asExternalURL() {
      return requestInfo.getUrl();
   }

   public String asInternalURL() {
      return StringUtilities.join(requestInfo.getScheme() + "://", clientAddressedHost, asInternalURI());
   }

   public String asInternalURI() {
      return removeVersionPrefix(requestInfo, mapping.getId());
   }

   private String removeVersionPrefix(UniformResourceInfo requestInfo, String prefix) {
      if (requestInfo.getUri().charAt(0) != '/') {
         throw new IllegalArgumentException("Request URI must be a URI with a root reference - i.e. the URI must start with '/'");
      }

      final String uri = StringUriUtilities.formatUri(requestInfo.getUri());
      final String formattedOriginal = StringUriUtilities.formatUri(prefix);

      if (uri.startsWith(formattedOriginal + "/")) {
         return uri.substring(formattedOriginal.length());
      }
      
      return uri;
   }
}
