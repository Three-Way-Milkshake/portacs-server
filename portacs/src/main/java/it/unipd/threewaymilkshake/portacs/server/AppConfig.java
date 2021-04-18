package it.unipd.threewaymilkshake.portacs.server;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ResourceCondition;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import it.unipd.threewaymilkshake.portacs.server.connection.ConnectionHandler;
import it.unipd.threewaymilkshake.portacs.server.engine.clients.ForkliftsList;
import it.unipd.threewaymilkshake.portacs.server.engine.clients.UsersList;
import it.unipd.threewaymilkshake.portacs.server.engine.map.PathFindingStrategy;
import it.unipd.threewaymilkshake.portacs.server.engine.map.StrategyBreadthFirst;
import it.unipd.threewaymilkshake.portacs.server.engine.map.WarehouseMap;
import it.unipd.threewaymilkshake.portacs.server.persistency.JsonForklift;
import it.unipd.threewaymilkshake.portacs.server.persistency.JsonMap;
import it.unipd.threewaymilkshake.portacs.server.persistency.JsonUser;

@Configuration
public class AppConfig {
  // private final static String MAP_FILE="Map.json";
  private final static String FORKLIFT_FILE="Forklift.json";

  @Bean
  //@Scope("singleton") //forse non serve perché Cardin dice che sono singleton di default 
                        //l'ho letto da varie parti, vedi https://www.javadevjournal.com/spring/spring-singleton-vs-singleton-pattern/
  public WarehouseMap warehouseMap(@Value("${server.database.json-map}") String mapFilePath){
    //Resource resource=new ClassPathResource(MAP_FILE);
    return new WarehouseMap(new JsonMap(mapFilePath), pathFindingStrategy());
  }

  @Bean("warehouseMapTest")
  public WarehouseMap warehouseMapTest(@Value("${server.database.json-map.test}") String mapFilePath){
    return new WarehouseMap(new JsonMap(mapFilePath), pathFindingStrategy());
  }

  @Bean
  public PathFindingStrategy pathFindingStrategy(){
    return new StrategyBreadthFirst();
  }

  @Bean
  public ConnectionHandler connectionHandler(@Value("${server.database.json-user}") String userFilePath){
    return new ConnectionHandler(usersList(userFilePath), forkliftsList());
  }

  @Bean
  public UsersList usersList(@Value("${server.database.json-user}") String userFilePath){
    return new UsersList(new JsonUser(userFilePath), passwordEncoder());
  }

  @Bean
  public ForkliftsList forkliftsList(){
    return new ForkliftsList(new JsonForklift(FORKLIFT_FILE));
  }

  @Bean
  public PasswordEncoder passwordEncoder(){
    return new BCryptPasswordEncoder();
  }
}
