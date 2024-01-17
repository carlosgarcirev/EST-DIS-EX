package es.ufv.dis.mock.restservice;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.Reader;
import java.io.Writer;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;

public class JsonReader {
    public ArrayList<User> readJsonFile(String fichero) {
        try {
            Gson gson = new Gson(); //crea un objeto Gson
            //lee el fichero que le pasemos y lo carga en un reader
            Reader reader = Files.newBufferedReader(Paths.get(fichero));
            // convierte el array JSON a un arraylist de users
            ArrayList<User> users = new Gson().fromJson(reader, new TypeToken<ArrayList<User>>() {
            }.getType());
            users.forEach(System.out::println);// imprime los users
            reader.close();// close reader
            return users;

        } catch (Exception ex) {
            ex.printStackTrace();
            return new ArrayList<>(); //si no ha leido nada, devuelve un array vacio
        }

    }
    public boolean writeJsonFile(String fichero, ArrayList<User> users){
        try{
            // lee el fichero que le pasemos y lo abre en modo escritura
            Writer writer = Files.newBufferedWriter(Paths.get(fichero));
            // convierte el arrayListe de users a un array JSON, y lo escribe en el fichero de forma ordenada y bonita
            writer.write(new Gson().toJson(users));
            writer.close();// close reader
            return true;
        }catch (Exception ex){
            ex.printStackTrace();
            return false; //Si no ha leido nada, devuelve un array vacio
        }
    }
}

