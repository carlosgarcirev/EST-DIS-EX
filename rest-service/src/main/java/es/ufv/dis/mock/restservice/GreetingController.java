package es.ufv.dis.mock.restservice;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicLong;

@RestController
public class GreetingController {

    @GetMapping("/users")
    public ArrayList<User> users() {
        JsonReader reader = new JsonReader();
        ArrayList<User> userList = reader.readJsonFile("./src/main/resources/cp-national-datafile.json");
        return userList;
    }
    @GetMapping("/users/{nombre}")
    public ResponseEntity<User> GetByNombre(@PathVariable String nombre) {
        DataHandling dataHandling = new DataHandling();
        User foundUser = dataHandling.getUserInfo(nombre);
        return new ResponseEntity<>(foundUser, HttpStatus.OK);
    }
    @PostMapping(path = "/users",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<User> create(@RequestBody User newUser) {
        DataHandling dataHandling = new DataHandling();
        ArrayList<User> usersList = dataHandling.addUser(newUser);
        User user = usersList.get(usersList.size()-1);
        return new ResponseEntity<>(user, HttpStatus.CREATED);
    }

}
