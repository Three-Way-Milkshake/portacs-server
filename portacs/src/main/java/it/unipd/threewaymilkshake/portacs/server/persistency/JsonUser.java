package it.unipd.threewaymilkshake.portacs.server.persistency;

import java.util.List;

import com.google.gson.Gson;

import org.springframework.boot.autoconfigure.gson.GsonAutoConfiguration;

import it.unipd.threewaymilkshake.portacs.server.engine.clients.User;


public class JsonUser implements UserDao{
  
  private String filePath;

@Override
public void updateUsers(List<User> u) {
    // TODO Auto-generated method stub
    
}

@Override
public List<User> readUsers() {
    // TODO Auto-generated method stub
    return null;
}

  
}