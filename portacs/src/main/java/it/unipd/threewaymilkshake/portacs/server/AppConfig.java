package it.unipd.threewaymilkshake.portacs.server;

import org.springframework.boot.autoconfigure.condition.ResourceCondition;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

import it.unipd.threewaymilkshake.portacs.server.engine.map.WarehouseMap;
import it.unipd.threewaymilkshake.portacs.server.persistency.JsonMap;

@Configuration
public class AppConfig {
  private final static String MAP_FILE="Map.json";

  @Bean
  @Scope("singleton")
  public WarehouseMap mapSingleton(){
    Resource resource=new ClassPathResource(MAP_FILE);
    return new WarehouseMap(new JsonMap(resource.getFile()));
  }
}
