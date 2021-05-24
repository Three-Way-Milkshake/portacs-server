/* (C) 2021 Three Way Milkshake - PORTACS - UniPd SWE*/
package it.unipd.threewaymilkshake.portacs;

import it.unipd.threewaymilkshake.portacs.server.AppConfig;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;

@SpringBootTest
@ContextConfiguration(classes = {AppConfig.class})
class PortacsApplicationTests {

  @Test
  void contextLoads() {}
}
