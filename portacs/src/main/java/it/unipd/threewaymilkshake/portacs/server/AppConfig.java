/* (C) 2021 Three Way Milkshake - PORTACS - UniPd SWE*/
package it.unipd.threewaymilkshake.portacs.server;

import it.unipd.threewaymilkshake.portacs.server.connection.Connection;
import it.unipd.threewaymilkshake.portacs.server.connection.ConnectionHandler;
import it.unipd.threewaymilkshake.portacs.server.engine.clients.ForkliftsList;
import it.unipd.threewaymilkshake.portacs.server.engine.clients.UsersList;
import it.unipd.threewaymilkshake.portacs.server.engine.map.PathFindingStrategy;
import it.unipd.threewaymilkshake.portacs.server.engine.map.StrategyBreadthFirst;
import it.unipd.threewaymilkshake.portacs.server.persistency.JsonForklift;
import it.unipd.threewaymilkshake.portacs.server.persistency.JsonMap;
import it.unipd.threewaymilkshake.portacs.server.persistency.JsonUser;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@PropertySource("classpath:application.properties")
public class AppConfig {
  // private final static String MAP_FILE="Map.json";
  private static final String FORKLIFT_FILE = "Forklift.json";

  @Value("${server.database.json-users}") 
  private String usersFilePath;

  @Value("${server.database.json-forklifts}") 
  private String forkliftsFilePath;

  /*@Bean("warehouseMap")
  //@Scope("singleton") //forse non serve perché Cardin dice che sono singleton di default
                        //l'ho letto da varie parti, vedi https://www.javadevjournal.com/spring/spring-singleton-vs-singleton-pattern/
  public WarehouseMap warehouseMap(PathFindingStrategy pathFindingStrategy){
    //Resource resource=new ClassPathResource(MAP_FILE);


    return new WarehouseMap(jsonMap, pathFindingStrategy());
  }*/

  @Bean("jsonMap")
  public JsonMap jsonMap(@Value("${server.database.json-map}") String mapFilePath) {
    return new JsonMap(mapFilePath);
  }

  @Bean("jsonMapTest")
  public JsonMap jsonMapTest(@Value("${server.database.json-map-test}") String mapFilePath) {
    return new JsonMap(mapFilePath);
  }

  @Bean("jsonUser")
  public JsonUser jsonUser(@Value("${server.database.json-users}") String usersFilePath) {
    return new JsonUser(usersFilePath);
  }

  @Bean("jsonUserTest")
  public JsonUser jsonUserTest(@Value("${server.database.json-users-test}") String usersFilePath) {
    return new JsonUser(usersFilePath);
  }

  // INIZIO aggiunti solo per non avere errori in quelli dopo, NON so se giusti
  @Bean
  public JsonUser jsonUser() {
    return new JsonUser(usersFilePath);
  }

  @Bean
  public JsonForklift jsonForklift() {
    return new JsonForklift(forkliftsFilePath);
  }
  // FINE aggiunta non certa

  @Bean
  public PathFindingStrategy pathFindingStrategy() {
    return new StrategyBreadthFirst();
  }

  /* @Bean
  public ConnectionHandler connectionHandler(){
    return new ConnectionHandler(usersList(), forkliftsList());
  } */

  /* @Bean
  public ConnectionHandler connectionHandler(@Value("${server.database.json-user}") String userFilePath){
    return new ConnectionHandler(usersList(userFilePath), forkliftsList());
  } */

  /* @Bean
  public UsersList usersList(){
    return new UsersList(jsonUser(), passwordEncoder());
  } */

  /* @Bean
  public ForkliftsList forkliftsList(){
    return new ForkliftsList(jsonForklift());
  } */

  @Bean
  public PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
  }
}
