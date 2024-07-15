package hy.util.http;

import java.net.InetAddress;
import java.net.UnknownHostException;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

@ConditionalOnProperty(name = "app.reactive.enabled", havingValue = "true", matchIfMissing = true)
@Component
public class ServiceUtil {
  private final String port;
  private String serviceAddress = null;

  public ServiceUtil(@Value("${server.port}") String port) {
    this.port = port;
  }

  public String getServiceAddress() {
    if (serviceAddress == null) {
      serviceAddress = findMyHostname() + "/" + findMyIpAddress() + ":" + port;
    }
    return serviceAddress;
  }

  private String findMyHostname() {
    try {
      return InetAddress.getLocalHost().getHostName();
    } catch (UnknownHostException e) {
      return "unknown host name";
    }
  }

  private String findMyIpAddress() {
    try {
      return InetAddress.getLocalHost().getHostAddress();
    } catch (UnknownHostException e) {
      return "unknown IP address";
    }
  }
}
