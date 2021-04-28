package it.unipd.threewaymilkshake.portacs.server.persistency;

import org.skyscreamer.jsonassert.JSONAssert;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.stream.*;

import javax.annotation.Resource;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.Arrays;
import java.util.Collection;

import org.assertj.core.api.Assert;
import org.json.JSONException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.internal.matchers.apachecommons.ReflectionEquals;
import org.mockito.internal.matchers.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import it.unipd.threewaymilkshake.portacs.server.AppConfig;
import it.unipd.threewaymilkshake.portacs.server.engine.SimplePoint;
import it.unipd.threewaymilkshake.portacs.server.engine.clients.Admin;
import it.unipd.threewaymilkshake.portacs.server.engine.clients.Manager;
import it.unipd.threewaymilkshake.portacs.server.engine.clients.User;


@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {AppConfig.class})
//@EnableAutoConfiguration
public class JsonUserTest {

    @Autowired
    @Qualifier("jsonUserTest")
    private JsonUser jsonUser;

    private List<User> usersList;
    
    @BeforeEach
    public void setUp() {
        usersList = new LinkedList<User>();
        User user1 = new Admin("first.user","First","User","abcd");
        User user2 = new Manager("second.user","Second","User","efgh"); 
        usersList.add(user1);
        usersList.add(user2);
        
    }

    @Test
    @DisplayName("Tests if updating the set of users in the JSON persistency works")
    public void updateUserTest() throws JSONException {
        jsonUser.updateUsers(usersList); 
        
        Scanner created;
        Scanner compare;
        try {
            created = new Scanner(new File(jsonUser.getFilePath()),"UTF-8");
            String createdContent = created.useDelimiter("\\Z").next();
            compare = new Scanner(new File("src/test/java/it/unipd/threewaymilkshake/portacs/server/database/userComparisonTest.json"),"UTF-8");
            String comparedContent = compare.useDelimiter("\\Z").next();           
            //assertEquals(createdContent,comparedContent);
            JSONAssert.assertEquals(createdContent, comparedContent, false);
        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
         
    }
    
    @Test 
    @DisplayName("Tests if updating the reading of users in the JSON persistency works")
    public void readUserTest() {
        List<User> readUsers = jsonUser.readUsers();
        
        /*System.out.println("*****************************************************************");
        IntStream.range(0, usersList.size()).forEach(i->{
            System.out.println(readUsers.get(i).getId());
        });
        System.out.println("*****************************************************************");
        /*IntStream.range(0, usersList.size()).forEach(i->{
            assertTrue(new ReflectionEquals(usersList.get(i)).matches(readUsers.get(i)));
            //assertEquals(usersList.get(i), readUsers.get(i));
        }); */
    }





}
