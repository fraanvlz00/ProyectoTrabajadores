package Dominio;

public class Isapre {
    private String nombre;
    private String plan;

    // Constructor
    public Isapre(String nombre, String plan) {
        this.nombre = nombre;
        this.plan = plan;
    }

    // Getters y Setters
    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getPlan() {
        return plan;
    }

    public void setPlan(String plan) {
        this.plan = plan;
    }

    @Override
    public String toString() {
        return "Nombre: " + nombre + ", Plan: " + plan;
    }
}

