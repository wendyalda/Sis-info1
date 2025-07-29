package com.gestionEscolarAct13.modelos;



public class Estudiante {
    private String id;
    private String nombre;
    private String estadoRegistro; // Ej: "PENDIENTE", "ACTIVO", "INACTIVO"
    private double saldoPendiente; // Para pagos

    public Estudiante(String id, String nombre, String estadoRegistro) {
        this.id = id;
        this.nombre = nombre;
        this.estadoRegistro = estadoRegistro;
        this.saldoPendiente = 0.0; // Inicialmente sin saldo pendiente
    }

    // Getters
    public String getId() { return id; }
    public String getNombre() { return nombre; }
    public String getEstadoRegistro() { return estadoRegistro; }
    public double getSaldoPendiente() { return saldoPendiente; }

    // Setters (para actualizar estados o saldo)
    public void setEstadoRegistro(String estadoRegistro) {
        this.estadoRegistro = estadoRegistro;
    }

    public void setSaldoPendiente(double saldoPendiente) {
        this.saldoPendiente = saldoPendiente;
    }

    @Override
    public String toString() {
        return "Estudiante [ID=" + id + ", Nombre=" + nombre + ", Estado=" + estadoRegistro + ", Saldo Pendiente=" + saldoPendiente + "]";
    }
}
