/* (C) 2021 Three Way Milkshake - PORTACS - UniPd SWE*/
package it.unipd.threewaymilkshake.portacs.server;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.stereotype.Component;

@SpringBootApplication
@EnableScheduling
public class PortacsApplication {

  public static void main(String[] args) {
    SpringApplication.run(PortacsApplication.class, args);
  }
}

@Component
class PortacsRunner implements ApplicationRunner {

  private static final Logger logger = LoggerFactory.getLogger(PortacsRunner.class);

  @Override
  public void run(ApplicationArguments args) throws Exception {
    logger.info("PORTACS UniPd Three Way Milkshake");

    // new Thread(new Engine()).start();
  }
}
