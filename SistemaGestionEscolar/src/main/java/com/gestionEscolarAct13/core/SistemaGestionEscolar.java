package com.gestionEscolarAct13.core;

import com.gestionEscolarAct13.modelos.Estudiante;
import com.gestionEscolarAct13.modelos.RegistroSolicitud;
import com.gestionEscolarAct13.modelos.Pago;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;
import java.util.HashMap;
import java.util.InputMismatchException; // Importar para manejar errores de tipo de entrada
import java.util.Map;

public class SistemaGestionEscolar {

    private List<Estudiante> estudiantes;
    private List<RegistroSolicitud> solicitudesPendientes;
    private List<Pago> pagosRegistrados;
    private Scanner scanner;
    private Map<String, List<Estudiante>> vinculosPadreEstudiante;

    public SistemaGestionEscolar() {
        this.estudiantes = new ArrayList<>();
        this.solicitudesPendientes = new ArrayList<>();
        this.pagosRegistrados = new ArrayList<>();
        this.scanner = new Scanner(System.in);
        this.vinculosPadreEstudiante = new HashMap<>();

        inicializarDatosDePrueba();
    }

    private void inicializarDatosDePrueba() {
        Estudiante est1 = new Estudiante("E001", "Ana Garcia", "PENDIENTE");
        Estudiante est2 = new Estudiante("E002", "Luis Perez", "ACTIVO");
        Estudiante est3 = new Estudiante("E003", "Maria Lopez", "ACTIVO");
        Estudiante est4 = new Estudiante("E004", "Carlos Ruiz", "ACTIVO");

        est2.setSaldoPendiente(150.0);
        est3.setSaldoPendiente(200.0);
        est4.setSaldoPendiente(0.0);

        estudiantes.add(est1);
        estudiantes.add(est2);
        estudiantes.add(est3);
        estudiantes.add(est4);

        solicitudesPendientes.add(new RegistroSolicitud("S001", est1, "2025-07-28"));

        pagosRegistrados.add(new Pago("P001", est2, 100.0, "Tarjeta", "MENSUALIDAD"));
        pagosRegistrados.add(new Pago("P002", est3, 200.0, "Transferencia", "MATRICULA"));

        List<Estudiante> hijosJuan = new ArrayList<>();
        hijosJuan.add(est2);
        hijosJuan.add(est3);
        vinculosPadreEstudiante.put("JuanPerez", hijosJuan);

        List<Estudiante> hijosLaura = new ArrayList<>();
        hijosLaura.add(est1);
        vinculosPadreEstudiante.put("LauraGomez", hijosLaura);
    }

    // --- Caso de Uso: Aprobar Registro (MODIFICADO) ---
    public void aprobarRegistro() {
        System.out.println("\n--- Aprobar Solicitud de Registro ---");
        if (solicitudesPendientes.isEmpty()) {
            System.out.println("No hay solicitudes de registro pendientes.");
            return;
        }

        System.out.println("Solicitudes pendientes:");
        for (int i = 0; i < solicitudesPendientes.size(); i++) {
            System.out.println((i + 1) + ". " + solicitudesPendientes.get(i));
        }

        int opcion = -1;
        System.out.print("Ingrese el número de la solicitud a aprobar/rechazar (0 para cancelar): ");

        try {
            opcion = scanner.nextInt();
            scanner.nextLine();
        } catch (InputMismatchException e) {
            System.out.println("Error: Entrada inválida. Por favor, ingrese un número.");
            scanner.nextLine();
            return;
        }

        if (opcion == 0) {
            System.out.println("Operación cancelada.");
            return;
        }

        if (opcion > 0 && opcion <= solicitudesPendientes.size()) {
            RegistroSolicitud solicitud = solicitudesPendientes.get(opcion - 1);
            System.out.print("¿Desea 'APROBAR' o 'RECHAZAR' la solicitud " + solicitud.getIdSolicitud() + " de " + solicitud.getEstudiante().getNombre() + "? ");
            String decision = scanner.nextLine().toUpperCase();

            if (decision.equals("APROBAR")) {
                solicitud.setEstado("APROBADO");
                solicitud.getEstudiante().setEstadoRegistro("ACTIVO"); // Aquí se actualiza a ACTIVO
                solicitudesPendientes.remove(solicitud);
                System.out.println("Solicitud " + solicitud.getIdSolicitud() + " APROBADA. Estudiante " + solicitud.getEstudiante().getNombre() + " ahora está ACTIVO.");
                simularNotificacion("Registro de " + solicitud.getEstudiante().getNombre() + " aprobado.");
            } else if (decision.equals("RECHAZAR")) {
                solicitud.setEstado("RECHAZADA");
                solicitud.getEstudiante().setEstadoRegistro("INACTIVO"); // ¡NUEVO! Aquí se actualiza a INACTIVO
                solicitudesPendientes.remove(solicitud);
                System.out.println("Solicitud " + solicitud.getIdSolicitud() + " RECHAZADA. Estudiante " + solicitud.getEstudiante().getNombre() + " ahora está INACTIVO.");
                simularNotificacion("Registro de " + solicitud.getEstudiante().getNombre() + " rechazado.");
            } else {
                System.out.println("Opción no válida. La solicitud no fue procesada.");
            }
        } else {
            System.out.println("Error: Número de solicitud inválido. Por favor, ingrese un número de la lista.");
        }
    }

    // --- Caso de Uso: Registrar Pago (se mantiene igual, ya está robusto) ---
    public void registrarPago() {
        System.out.println("\n--- Registrar Nuevo Pago ---");
        System.out.print("Ingrese el ID del estudiante: ");
        String idEstudiante = scanner.nextLine();

        Estudiante estudianteEncontrado = null;
        for (Estudiante e : estudiantes) {
            if (e.getId().equals(idEstudiante)) {
                estudianteEncontrado = e;
                break;
            }
        }

        if (estudianteEncontrado == null) {
            System.out.println("Error: Estudiante con ID " + idEstudiante + " no encontrado.");
            return;
        }

        if (estudianteEncontrado.getEstadoRegistro().equals("PENDIENTE")) {
            System.out.println("Error: No se puede registrar pagos para el estudiante " + estudianteEncontrado.getNombre() + ".");
            System.out.println("Su estado de registro es 'PENDIENTE'. Debe ser 'ACTIVO' para realizar pagos.");
            return;
        }

        System.out.println("Estudiante seleccionado: " + estudianteEncontrado.getNombre() + " (Estado: " + estudianteEncontrado.getEstadoRegistro() + ", Saldo pendiente: " + estudianteEncontrado.getSaldoPendiente() + ")");

        if (estudianteEncontrado.getSaldoPendiente() <= 0) {
            System.out.println("Error: El estudiante " + estudianteEncontrado.getNombre() + " no tiene saldo pendiente o mensualidades a pagar.");
            return;
        }

        System.out.print("Ingrese el monto del pago: ");
        double monto = -1; // Inicializamos con un valor inválido
        try {
            monto = scanner.nextDouble();
            scanner.nextLine();
        } catch (InputMismatchException e) {
            System.out.println("Error: Monto inválido. Por favor, ingrese un número.");
            scanner.nextLine(); // Limpiar el buffer
            return;
        }

        if (monto <= 0) {
            System.out.println("Error: El monto del pago debe ser positivo.");
            return;
        }

        if (monto > estudianteEncontrado.getSaldoPendiente()) {
            System.out.printf("Advertencia: El monto del pago (%.2f) excede el saldo pendiente (%.2f).%n", monto, estudianteEncontrado.getSaldoPendiente());
            System.out.print("¿Desea continuar con el pago de este monto? (S/N): ");
            String confirmacion = scanner.nextLine().toUpperCase();
            if (!confirmacion.equals("S")) {
                System.out.println("Operación de pago cancelada.");
                return;
            }
        }

        System.out.print("Ingrese el método de pago (Ej. Efectivo, Tarjeta, Transferencia): ");
        String metodo = scanner.nextLine();

        System.out.print("Ingrese el tipo de transacción (Ej. MATRICULA, MENSUALIDAD, OTRO): ");
        String tipoTransaccion = scanner.nextLine().toUpperCase();

        String usuarioPadreAsociado = obtenerUsuarioPadreDeEstudiante(estudianteEncontrado.getId());
        if (usuarioPadreAsociado == null) {
            System.out.println("Advertencia: No se encontró un padre asociado para el estudiante " + estudianteEncontrado.getNombre() + ".");
        } else {
            System.out.println("Estudiante asociado al padre/tutor: " + usuarioPadreAsociado);
        }

        System.out.println("-> Iniciando procesamiento de transacción con Pasarela de Pagos...");
        boolean transaccionExitosa = simularProcesarTransaccion(monto, metodo);

        if (!transaccionExitosa) {
            System.out.println("Error: La transacción fue rechazada por la Pasarela de Pagos.");
            simularNotificacion("Fallo en el pago de " + estudianteEncontrado.getNombre() + " por " + monto);
            return;
        }
        System.out.println("<- Transacción procesada exitosamente.");

        String nuevoIdPago = "P" + (pagosRegistrados.size() + 1);
        Pago nuevoPago = new Pago(nuevoIdPago, estudianteEncontrado, monto, metodo, tipoTransaccion);
        pagosRegistrados.add(nuevoPago);

        estudianteEncontrado.setSaldoPendiente(estudianteEncontrado.getSaldoPendiente() - monto);

        System.out.println("Pago registrado exitosamente:\n" + nuevoPago);
        System.out.println("Nuevo saldo pendiente para " + estudianteEncontrado.getNombre() + ": " + estudianteEncontrado.getSaldoPendiente());

        System.out.println("-> Generando factura en Sistema de Facturación...");
        simularGenerarFactura(nuevoPago);
        System.out.println("<- Factura generada y enviada al estudiante/padre.");

        System.out.println("-> Registrando cambios y enviando notificaciones...");
        simularRegistrarCambios(nuevoPago);
        simularNotificacion("Nuevo pago de " + estudianteEncontrado.getNombre() + " por " + monto + " registrado.");
        if (usuarioPadreAsociado != null) {
            simularNotificacion("Notificación enviada al padre " + usuarioPadreAsociado + " sobre el pago de " + estudianteEncontrado.getNombre());
        }
        System.out.println("<- Cambios registrados y notificaciones enviadas.");
    }

    // --- Caso de Uso: Generar Reporte de Ingresos (se mantiene igual) ---
    public void generarReporteIngresos() {
        System.out.println("\n--- Generar Reporte de Ingresos ---");
        if (pagosRegistrados.isEmpty()) {
            System.out.println("No hay pagos registrados para generar un reporte.");
            return;
        }

        System.out.println("\n--- Reporte de Ingresos del Sistema ---");
        System.out.println("=====================================");

        double totalIngresos = 0;
        pagosRegistrados.stream()
                .collect(Collectors.groupingBy(Pago::getTipoTransaccion,
                        Collectors.summingDouble(Pago::getMonto)))
                .forEach((tipo, monto) -> System.out.printf("  Total por %s: %.2f%n", tipo, monto));

        System.out.println("-------------------------------------");

        for (Pago pago : pagosRegistrados) {
            totalIngresos += pago.getMonto();
            System.out.println(pago);
        }
        System.out.println("=====================================");
        System.out.printf("Total de Ingresos General: %.2f%n", totalIngresos);
        System.out.println("=====================================");
    }

    // --- Métodos de Simulación para Sistemas Externos (se mantienen iguales) ---
    private boolean simularProcesarTransaccion(double monto, String metodo) {
        if (monto > 500 && metodo.equals("Tarjeta")) {
            System.out.println("   [Pasarela de Pagos] Transacción de tarjeta de alto monto rechazada.");
            return false;
        }
        System.out.printf("   [Pasarela de Pagos] Transacción de %.2f %s aprobada.%n", monto, metodo);
        return true;
    }

    private void simularGenerarFactura(Pago pago) {
        System.out.println("   [Sistema de Facturación] Factura F-" + pago.getIdPago() + " generada para " + pago.getEstudiante().getNombre());
    }

    private void simularRegistrarCambios(Pago pago) {
        System.out.println("   [Sistema de Auditoría] Cambios de pago " + pago.getIdPago() + " registrados.");
    }

    private void simularNotificacion(String mensaje) {
        System.out.println("   [Sistema de Notificaciones] Mensaje enviado: " + mensaje);
    }

    private String obtenerUsuarioPadreDeEstudiante(String idEstudiante) {
        for (Map.Entry<String, List<Estudiante>> entry : vinculosPadreEstudiante.entrySet()) {
            for (Estudiante est : entry.getValue()) {
                if (est.getId().equals(idEstudiante)) {
                    return entry.getKey();
                }
            }
        }
        return null;
    }

    // --- Método Principal (main) para ejecutar la aplicación (se mantiene igual) ---
    public static void main(String[] args) {
        SistemaGestionEscolar sistema = new SistemaGestionEscolar();
        Scanner mainScanner = new Scanner(System.in);
        boolean salir = false;

        while (!salir) {
            System.out.println("\n--- Menú Principal del Administrador ---");
            System.out.println("1. Aprobar Registro");
            System.out.println("2. Registrar Pago");
            System.out.println("3. Generar Reporte de Ingresos");
            System.out.println("4. Ver Estudiantes");
            System.out.println("5. Ver Solicitudes Pendientes");
            System.out.println("6. Ver Pagos Registrados");
            System.out.println("0. Salir");
            System.out.print("Seleccione una opción: ");

            if (mainScanner.hasNextInt()) {
                int opcion = mainScanner.nextInt();
                mainScanner.nextLine();

                switch (opcion) {
                    case 1:
                        sistema.aprobarRegistro();
                        break;
                    case 2:
                        sistema.registrarPago();
                        break;
                    case 3:
                        sistema.generarReporteIngresos();
                        break;
                    case 4:
                        System.out.println("\n--- Lista de Estudiantes ---");
                        if (sistema.estudiantes.isEmpty()) {
                            System.out.println("No hay estudiantes registrados.");
                        } else {
                            sistema.estudiantes.forEach(System.out::println);
                        }
                        break;
                    case 5:
                        System.out.println("\n--- Solicitudes de Registro Pendientes ---");
                        if (sistema.solicitudesPendientes.isEmpty()) {
                            System.out.println("No hay solicitudes pendientes.");
                        } else {
                            sistema.solicitudesPendientes.forEach(System.out::println);
                        }
                        break;
                    case 6:
                        System.out.println("\n--- Pagos Registrados ---");
                        if (sistema.pagosRegistrados.isEmpty()) {
                            System.out.println("No hay pagos registrados.");
                        } else {
                            sistema.pagosRegistrados.forEach(System.out::println);
                        }
                        break;
                    case 0:
                        salir = true;
                        System.out.println("Saliendo del sistema. ¡Hasta luego!");
                        break;
                    default:
                        System.out.println("Opción no válida. Intente de nuevo.");
                }
            } else {
                System.out.println("Entrada inválida. Por favor, ingrese un número.");
                mainScanner.nextLine();
            }
        }
        mainScanner.close();
        sistema.scanner.close();
    }
}
