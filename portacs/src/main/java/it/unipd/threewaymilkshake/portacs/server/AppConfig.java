package it.unipd.threewaymilkshake.portacs.server;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ResourceCondition;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

import it.unipd.threewaymilkshake.portacs.server.connection.ConnectionHandler;
import it.unipd.threewaymilkshake.portacs.server.engine.clients.ForkliftsList;
import it.unipd.threewaymilkshake.portacs.server.engine.clients.UsersList;
import it.unipd.threewaymilkshake.portacs.server.engine.map.PathFindingStrategy;
import it.unipd.threewaymilkshake.portacs.server.engine.map.StrategyBreadthFirst;
import it.unipd.threewaymilkshake.portacs.server.engine.map.WarehouseMap;
import it.unipd.threewaymilkshake.portacs.server.persistency.JsonForklift;
import it.unipd.threewaymilkshake.portacs.server.persistency.JsonMap;

@Configuration
public class AppConfig {
  //private final static String MAP_FILE="Map.json";
  //private final static String FORKLIFT_FILE="Forklift.json";

  @Bean
  //@Scope("singleton") //forse non serve perché Cardin dice che sono singleton di default 
                        //l'ho letto da varie parti, vedi https://www.javadevjournal.com/spring/spring-singleton-vs-singleton-pattern/
  public WarehouseMap warehouseMap(@Value("${server.database.json-map}") String mapFilePath){
    //Resource resource=new ClassPathResource(MAP_FILE);
    return new WarehouseMap(new JsonMap(mapFilePath));
  }

  @Bean
  public PathFindingStrategy pathFindingStrategy(){
    return new StrategyBreadthFirst();
  }

  @Bean
  public ConnectionHandler connectionHandler(){
    return new ConnectionHandler(usersList(), forkliftsList());
  }

  @Bean
  public UsersList usersList(){
    return new UsersList();
  }

  @Bean
  public ForkliftsList forkliftsList(){
    return new ForkliftsList(new JsonForklift(FORKLIFT_FILE));
  }
}
