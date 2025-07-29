package com.gestionEscolarAct13.modelos;

public class RegistroSolicitud {
    private String idSolicitud;
    private Estudiante estudiante; // Referencia al estudiante que solicita
    private String fechaSolicitud;
    private String estado; // Ej: "PENDIENTE", "APROBADO", "RECHAZADO"

    public RegistroSolicitud(String idSolicitud, Estudiante estudiante, String fechaSolicitud) {
        this.idSolicitud = idSolicitud;
        this.estudiante = estudiante;
        this.fechaSolicitud = fechaSolicitud;
        this.estado = "PENDIENTE"; // Estado inicial
    }

    // Getters
    public String getIdSolicitud() { return idSolicitud; }
    public Estudiante getEstudiante() { return estudiante; }
    public String getFechaSolicitud() { return fechaSolicitud; }
    public String getEstado() { return estado; }

    // Setter
    public void setEstado(String estado) {
        this.estado = estado;
    }

    @Override
    public String toString() {
        return "Solicitud [ID=" + idSolicitud + ", Estudiante=" + estudiante.getNombre() + ", Fecha=" + fechaSolicitud + ", Estado=" + estado + "]";
    }
}
