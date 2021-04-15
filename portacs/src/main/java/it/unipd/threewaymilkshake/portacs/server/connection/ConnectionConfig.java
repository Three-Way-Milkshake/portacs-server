package it.unipd.threewaymilkshake.portacs.server.connection;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ConnectionConfig {
  @Bean
  public ConnectionHandler connectionHandler(){
    return new ConnectionHandler();
  }
}
