/* (C) 2021 Three Way Milkshake - PORTACS - UniPd SWE*/
package it.unipd.threewaymilkshake.portacs.server;

import it.unipd.threewaymilkshake.portacs.server.engine.TasksSequencesList;
import it.unipd.threewaymilkshake.portacs.server.engine.clients.ForkliftsList;
import it.unipd.threewaymilkshake.portacs.server.engine.clients.UsersList;
import it.unipd.threewaymilkshake.portacs.server.engine.collision.Action;
import it.unipd.threewaymilkshake.portacs.server.engine.collision.CollisionDetector;
import it.unipd.threewaymilkshake.portacs.server.engine.collision.CollisionPipeline;
import it.unipd.threewaymilkshake.portacs.server.engine.collision.CollisionSolver;
import it.unipd.threewaymilkshake.portacs.server.engine.map.PathFindingStrategy;
import it.unipd.threewaymilkshake.portacs.server.engine.map.StrategyBreadthFirst;
import it.unipd.threewaymilkshake.portacs.server.engine.map.WarehouseMap;
import it.unipd.threewaymilkshake.portacs.server.persistency.ForkliftDao;
import it.unipd.threewaymilkshake.portacs.server.persistency.JsonForklift;
import it.unipd.threewaymilkshake.portacs.server.persistency.JsonMap;
import it.unipd.threewaymilkshake.portacs.server.persistency.JsonUser;
import it.unipd.threewaymilkshake.portacs.server.persistency.MapDao;
import it.unipd.threewaymilkshake.portacs.server.persistency.UserDao;

import static org.mockito.Mockito.mock;

import java.util.Deque;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.LinkedBlockingDeque;

import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.annotation.Scope;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@PropertySource("classpath:application.properties")
public class AppConfig {
  // private final static String MAP_FILE="Map.json";
  //private static final String FORKLIFT_FILE = "Forklift.json";

  @Value("${server.database.json-users}")
  private String usersFilePath;

  @Value("${server.database.json-forklifts}")
  private String forkliftsFilePath;

  @Value("${server.database.json-map}") 
  private String mapFilePath;

  @Bean("warehouseMap")
  public WarehouseMap warehouseMap(){
    return new WarehouseMap(jsonMap(), pathFindingStrategy());
  }

  /* @Bean("jsonMap")
  public JsonMap jsonMap(@Value("${server.database.json-map}") String mapFilePath) {
    return new JsonMap(mapFilePath);
  } */

  @Bean("jsonMap")
  public MapDao jsonMap() {
    return new JsonMap(mapFilePath);
  }

  @Bean("jsonMapTest")
  public JsonMap jsonMapTest(@Value("${server.database.json-map-test}") String mapFilePath) {
    return new JsonMap(mapFilePath);
  }

  /* @Bean("jsonUser")
  public JsonUser jsonUser(@Value("${server.database.json-users}") String usersFilePath) {
    return new JsonUser(usersFilePath);
  } */

  @Bean("jsonUser")
  public UserDao jsonUser() {
    return new JsonUser(usersFilePath);
  }

  @Bean("jsonUserTest")
  public JsonUser jsonUserTest(@Value("${server.database.json-users-test}") String usersFilePath) {
    return new JsonUser(usersFilePath);
  }

  /* @Bean("jsonForklift")
  public JsonForklift jsonForklift(
      @Value("${server.database.json-forklifts}") String forkliftFilePath) {
    return new JsonForklift(forkliftFilePath);
  } */

  @Bean("jsonForklift")
  public ForkliftDao jsonForklift() {
    return new JsonForklift(forkliftsFilePath);
  }

  @Bean("jsonForkliftTest")
  public JsonForklift jsonForkliftTest(
      @Value("${server.database.json-forklifts-test}") String forkliftFilePath) {
    return new JsonForklift(forkliftFilePath);
  }

  @Bean
  public PathFindingStrategy pathFindingStrategy() {
    return new StrategyBreadthFirst();
  }

  @Bean("tasksSequencesListTest") // TODO: for testing only
  // @Scope("prototype")
  public TasksSequencesList tasksSequencesListTest() {
    TasksSequencesList t = new TasksSequencesList();
    t.addTasksSequence(new LinkedBlockingDeque<>(List.of(1L, 2L)));
    t.addTasksSequence(new LinkedBlockingDeque<>(List.of(3L, 4L)));
    t.addTasksSequence(new LinkedBlockingDeque<>(List.of(5L, 6L)));
    t.addTasksSequence(new LinkedBlockingDeque<>(List.of(7L, 8L)));
    return t;
  }

  @Bean("tasksSequencesList")
  public TasksSequencesList tasksSequencesList() {
    return new TasksSequencesList();
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
    // return new UsersList(userDaoMock(), passwordEncoder());
  } */

  //TODO: remove -> BIG code smell (imported mockito here and changed gradle)
  @Bean
  public UserDao userDaoMock(){
    UserDao u=mock(UserDao.class);
    return u;
  }

  @Bean
  public ForkliftsList forkliftsList(){
    return new ForkliftsList(jsonForklift(), warehouseMap(), tasksSequencesListTest(), exceptionalEvents()); //TODO set real one
    // return new ForkliftsList(forkliftDaoMock());
  }

  //TODO: remove -> BIG code smell (imported mockito here and changed gradle)
  @Bean
  public ForkliftDao forkliftDaoMock(){
    ForkliftDao f=mock(ForkliftDao.class);
    return f;
  }

  @Bean
  public PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
  }

  @Bean
  public CollisionPipeline<ForkliftsList,Map<String, Action>> collisionPipeline(){
    return new CollisionPipeline<>(collisionDetector())
      .addHandler(collisionSolver());
  }

  @Bean
  public CollisionDetector collisionDetector(){
    return new CollisionDetector().setWarehouseMap(warehouseMap());
  }

  @Bean
  public CollisionSolver collisionSolver(){
    return new CollisionSolver().setForkliftsList(forkliftsList());
  }

  @Bean
  public Deque<String> exceptionalEvents(){
    return new LinkedBlockingDeque<>();
  }
}
