package model.entities;

import utils.Color;

public class User {
   String id;
   String name;
   String password;

   public User(String id, String name, String password) {
      this.id = id;
      this.name = name;
      this.password = password;
   }

   public String getId() {
      return id;
   }
   public void setId(String id) {
      this.id = id;
   }

   public String getName() {
      return name;
   }
   public void setName(String name) {
      this.name = name;
   }

   public String getPassword() {
      return password;
   }
   public void setPassword(String password) {
      this.password = password;
   }

   @Override
   public String toString() {
      return String.format(Color.infoMessage("User Details:\n" +
         "  ID      : %s\n" +
         "  Name    : %s\n" +
         "  Password: %s"),
         id, name, password
      );
   }

   public String toMessage() {
      return String.format("%s-%s-%s", id, name, password);
   }
}
