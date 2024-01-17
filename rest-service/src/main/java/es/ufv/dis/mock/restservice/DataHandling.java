package es.ufv.dis.mock.restservice;



import java.util.ArrayList;

public class DataHandling {
    ArrayList<User> addUser (User newUser){
        JsonReader reader = new JsonReader();
        ArrayList<User> userList = reader.readJsonFile("./src/main/resources/cp-national-datafile.json");
        userList.add(newUser);
        reader.writeJsonFile("./src/main/resources/cp-national-datafile.json", userList);
        return userList;
    }
    User getUserInfo (String name){
        User foundUser = null;
        JsonReader reader = new JsonReader();

        ArrayList<User> usersList = reader.readJsonFile("./src/main/resources/cp-national-datafile.json");
        for (User user : usersList){
            if (user.getNombre().equalsIgnoreCase(name)){
                foundUser = user;
            }
        }



        return foundUser;


    }
}