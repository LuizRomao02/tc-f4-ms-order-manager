package com.java.fiap.ordermanager.domain.config;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.java.fiap.ordermanager.config.IpExtractor;
import jakarta.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

class IpExtractorTest {

  @Mock private HttpServletRequest request;

  @InjectMocks private IpExtractor ipExtractor;

  @BeforeEach
  void setUp() {
    MockitoAnnotations.openMocks(this);
  }

  @Test
  void testGetClientIp_ShouldReturnIpFromXForwardedFor() {
    String expectedIp = "192.168.1.1";
    when(request.getHeader("X-Forwarded-For")).thenReturn(expectedIp);

    String clientIp = ipExtractor.getClientIp(request);

    assertEquals(expectedIp, clientIp);
  }

  @Test
  void testGetClientIp_ShouldReturnIpFromProxyClientIp_WhenXForwardedForIsEmpty() {
    String expectedIp = "192.168.1.2";
    when(request.getHeader("X-Forwarded-For")).thenReturn(null);
    when(request.getHeader("Proxy-Client-IP")).thenReturn(expectedIp);

    String clientIp = ipExtractor.getClientIp(request);

    assertEquals(expectedIp, clientIp);
  }

  @Test
  void testGetClientIp_ShouldReturnIpFromWLProxyClientIp_WhenOtherHeadersAreEmpty() {
    String expectedIp = "192.168.1.3";
    when(request.getHeader("X-Forwarded-For")).thenReturn(null);
    when(request.getHeader("Proxy-Client-IP")).thenReturn(null);
    when(request.getHeader("WL-Proxy-Client-IP")).thenReturn(expectedIp);

    String clientIp = ipExtractor.getClientIp(request);

    assertEquals(expectedIp, clientIp);
  }

  @Test
  void testGetClientIp_ShouldReturnIpFromRemoteAddr_WhenAllHeadersAreEmpty() {
    String expectedIp = "192.168.1.4";
    when(request.getHeader("X-Forwarded-For")).thenReturn(null);
    when(request.getHeader("Proxy-Client-IP")).thenReturn(null);
    when(request.getHeader("WL-Proxy-Client-IP")).thenReturn(null);
    when(request.getHeader("HTTP_CLIENT_IP")).thenReturn(null);
    when(request.getHeader("HTTP_X_FORWARDED_FOR")).thenReturn(null);
    when(request.getRemoteAddr()).thenReturn(expectedIp);

    String clientIp = ipExtractor.getClientIp(request);

    assertEquals(expectedIp, clientIp);
  }

  @Test
  void testGetClientIp_ShouldReturnFirstIpFromCommaSeparatedList() {
    String expectedIp = "192.168.1.5";
    String forwardedFor = "192.168.1.5, 192.168.1.6";
    when(request.getHeader("X-Forwarded-For")).thenReturn(forwardedFor);

    String clientIp = ipExtractor.getClientIp(request);

    assertEquals(expectedIp, clientIp);
  }

  @Test
  void testGetClientIp_ShouldReturnNull_WhenNoIpFound() {
    when(request.getHeader("X-Forwarded-For")).thenReturn(null);
    when(request.getHeader("Proxy-Client-IP")).thenReturn(null);
    when(request.getHeader("WL-Proxy-Client-IP")).thenReturn(null);
    when(request.getHeader("HTTP_CLIENT_IP")).thenReturn(null);
    when(request.getHeader("HTTP_X_FORWARDED_FOR")).thenReturn(null);
    when(request.getRemoteAddr()).thenReturn(null);

    String clientIp = ipExtractor.getClientIp(request);

    assertNull(clientIp);
  }
}
