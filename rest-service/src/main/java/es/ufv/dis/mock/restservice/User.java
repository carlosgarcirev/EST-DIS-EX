package es.ufv.dis.mock.restservice;

import java.util.Arrays;

public class User {
    private String nombre;
    private String apellido;
    private int [] fecha;
    private String genero;
    private long uuid;

    public User(String nombre, String apellido, int[] fecha, String genero, long uuid) {
        this.nombre = nombre;
        this.apellido = apellido;
        this.fecha = fecha;
        this.genero = genero;
        this.uuid = uuid;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getApellido() {
        return apellido;
    }

    public void setApellido(String apellido) {
        this.apellido = apellido;
    }

    public int[] getFecha() {
        return fecha;
    }

    public void setFecha(int[] fecha) {
        this.fecha = fecha;
    }

    public String getGenero() {
        return genero;
    }

    public void setGenero(String genero) {
        this.genero = genero;
    }

    public long getUuid() {
        return uuid;
    }

    public void setUuid(long uuid) {
        this.uuid = uuid;
    }

    @Override
    public String toString() {
        return "User{" +
                "nombre='" + nombre + '\'' +
                ", apellido='" + apellido + '\'' +
                ", fecha=" + Arrays.toString(fecha) +
                ", genero='" + genero + '\'' +
                ", uuid=" + uuid +
                '}';
    }
}
