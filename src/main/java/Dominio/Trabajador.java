package Dominio;

public class Trabajador {
    private String nombre;
    private String apellido;
    private String rut;
    private Isapre isapre;
    private AFP afp;

    // Constructor
    public Trabajador(String nombre, String apellido, String rut) {
        this.nombre = nombre;
        this.apellido = apellido;
        this.rut = rut;
    }

    // Getters y Setters
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

    public String getRut() {
        return rut;
    }

    public void setRut(String rut) {
        this.rut = rut;
    }

    public Isapre getIsapre() {
        return isapre;
    }

    public void setIsapre(Isapre isapre) {
        this.isapre = isapre;
    }

    public AFP getAfp() {
        return afp;
    }

    public void setAfp(AFP afp) {
        this.afp = afp;
    }

    @Override
    public String toString() {
        return "Trabajador{" +
                "nombre='" + nombre + '\'' +
                ", apellido='" + apellido + '\'' +
                ", rut='" + rut + '\'' +
                ", isapre=" + (isapre != null ? isapre : "No asignado") +
                ", afp=" + (afp != null ? afp : "No asignado") +
                '}';
    }
}
