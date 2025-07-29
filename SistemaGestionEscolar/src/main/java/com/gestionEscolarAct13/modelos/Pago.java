package com.gestionEscolarAct13.modelos;

import java.time.LocalDate;

public class Pago {
    private String idPago;
    private Estudiante estudiante; // Referencia al estudiante que realiza el pago
    private double monto;
    private LocalDate fechaPago;
    private String metodoPago;
    private String estado; // Ej: "COMPLETADO", "PENDIENTE", "REEMBOLSADO"
    private String tipoTransaccion; // Nuevo: ej. "MATRICULA", "MENSUALIDAD", "OTRO"

    public Pago(String idPago, Estudiante estudiante, double monto, String metodoPago, String tipoTransaccion) { // Constructor modificado
        this.idPago = idPago;
        this.estudiante = estudiante;
        this.monto = monto;
        this.fechaPago = LocalDate.now(); // Fecha actual del pago
        this.metodoPago = metodoPago;
        this.estado = "COMPLETADO"; // Estado inicial del pago al registrarlo
        this.tipoTransaccion = tipoTransaccion; // Asignar el nuevo atributo
    }

    // Getters
    public String getIdPago() { return idPago; }
    public Estudiante getEstudiante() { return estudiante; }
    public double getMonto() { return monto; }
    public LocalDate getFechaPago() { return fechaPago; }
    public String getMetodoPago() { return metodoPago; }
    public String getEstado() { return estado; }
    public String getTipoTransaccion() { return tipoTransaccion; } // Nuevo Getter

    // Setter (si el estado del pago pudiera cambiar, ej. a "reembolsado")
    public void setEstado(String estado) {
        this.estado = estado;
    }

    @Override
    public String toString() {
        return "Pago [ID=" + idPago + ", Estudiante=" + estudiante.getNombre() + ", Monto=" + monto + ", Fecha=" + fechaPago + ", Metodo=" + metodoPago + ", Tipo=" + tipoTransaccion + ", Estado=" + estado + "]";
    }
}
