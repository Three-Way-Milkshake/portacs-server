/* (C) 2021 Three Way Milkshake - PORTACS - UniPd SWE*/
package it.unipd.threewaymilkshake.portacs.server;

import it.unipd.threewaymilkshake.portacs.server.engine.TasksSequencesList;
import it.unipd.threewaymilkshake.portacs.server.engine.clients.ForkliftsList;
import it.unipd.threewaymilkshake.portacs.server.engine.collision.CollisionDetection;
import it.unipd.threewaymilkshake.portacs.server.engine.collision.CollisionForklift;
import it.unipd.threewaymilkshake.portacs.server.engine.collision.CollisionPipeline;
import it.unipd.threewaymilkshake.portacs.server.engine.collision.DeadlockCheck;
import it.unipd.threewaymilkshake.portacs.server.engine.collision.HeadOnCollisions;
import it.unipd.threewaymilkshake.portacs.server.engine.collision.NearestToCollision;
import it.unipd.threewaymilkshake.portacs.server.engine.collision.NumberOfCollisions;
import it.unipd.threewaymilkshake.portacs.server.engine.map.PathFindingStrategy;
import it.unipd.threewaymilkshake.portacs.server.engine.map.StrategyBreadthFirst;
import it.unipd.threewaymilkshake.portacs.server.engine.map.WarehouseMap;
import it.unipd.threewaymilkshake.portacs.server.persistency.ForkliftDao;
import it.unipd.threewaymilkshake.portacs.server.persistency.JsonForklift;
import it.unipd.threewaymilkshake.portacs.server.persistency.JsonMap;
import it.unipd.threewaymilkshake.portacs.server.persistency.JsonUser;
import it.unipd.threewaymilkshake.portacs.server.persistency.MapDao;
import it.unipd.threewaymilkshake.portacs.server.persistency.UserDao;
import java.util.Deque;
import java.util.List;
import java.util.Set;
import java.util.concurrent.LinkedBlockingDeque;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@PropertySource("classpath:application.properties")
public class AppConfig {

  @Value("${server.database.json-users}")
  private String usersFilePath;

  @Value("${server.database.json-forklifts}")
  private String forkliftsFilePath;

  @Value("${server.database.json-map}")
  private String mapFilePath;

  @Bean("warehouseMap")
  public WarehouseMap warehouseMap() {
    return new WarehouseMap(jsonMap(), pathFindingStrategy());
  }

  @Bean("jsonMap")
  public MapDao jsonMap() {
    return new JsonMap(mapFilePath);
  }

  @Value("${server.database.json-map-test}")
  private String mapTestFilePath;

  @Bean("jsonMapTest")
  public JsonMap jsonMapTest() {
    return new JsonMap(mapTestFilePath);
  }

  @Bean("jsonUser")
  public UserDao jsonUser() {
    return new JsonUser(usersFilePath);
  }

  @Value("${server.database.json-users-test}")
  String usersTestFilePath;

  @Bean("jsonUserTest")
  public JsonUser jsonUserTest() {
    return new JsonUser(usersTestFilePath);
  }

  @Bean("jsonForklift")
  public ForkliftDao jsonForklift() {
    return new JsonForklift(forkliftsFilePath);
  }

  @Value("${server.database.json-forklifts-test}")
  String forkliftsTestFilePath;

  @Bean("jsonForkliftTest")
  public JsonForklift jsonForkliftTest() {
    return new JsonForklift(forkliftsTestFilePath);
  }

  @Bean
  public PathFindingStrategy pathFindingStrategy() {
    return new StrategyBreadthFirst();
  }

  @Bean("tasksSequencesListTest")
  public TasksSequencesList tasksSequencesListTest() {
    TasksSequencesList t = new TasksSequencesList();
    t.addTasksSequence(new LinkedBlockingDeque<>(List.of(29L, 3L, 26L, 13L, 10L)));
    t.addTasksSequence(new LinkedBlockingDeque<>(List.of(30L, 15L, 6L, 23L, 36L)));
    t.addTasksSequence(new LinkedBlockingDeque<>(List.of(31L, 32L, 24L, 23L, 4L)));
    t.addTasksSequence(new LinkedBlockingDeque<>(List.of(39L, 2L, 19L, 24L, 29L)));
    t.addTasksSequence(new LinkedBlockingDeque<>(List.of(10L, 12L, 17L, 36L, 30L)));

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

  @Bean
  public ForkliftsList forkliftsList() {
    return new ForkliftsList(
        jsonForklift(), warehouseMap(), tasksSequencesListTest(), exceptionalEvents());
  }

  @Bean
  public PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
  }

  @Bean
  public CollisionPipeline<List<CollisionForklift>, Set<CollisionForklift>> collisionPipeline() {
    return new CollisionPipeline<>(deadlockCheck())
        .addHandler(collisionDetection())
        .addHandler(headOnCollisions())
        //  .addHandler(numberOfCollisions())
        .addHandler(nearestToCollision());
  }

  @Bean
  public CollisionDetection collisionDetection() {
    return new CollisionDetection().setWarehouseMap(warehouseMap());
  }

  @Bean
  public DeadlockCheck deadlockCheck() {
    return new DeadlockCheck();
  }

  @Bean
  public NumberOfCollisions numberOfCollisions() {
    return new NumberOfCollisions();
  }

  @Bean
  public NearestToCollision nearestToCollision() {
    return new NearestToCollision();
  }

  @Bean
  public HeadOnCollisions headOnCollisions() {
    return new HeadOnCollisions();
  }

  @Bean
  public Deque<String> exceptionalEvents() {
    return new LinkedBlockingDeque<>();
  }
}
