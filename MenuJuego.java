package com.proyecto;

import javax.swing.JOptionPane;

public class MenuJuego {
    static Juego juego; // Es la clase gestor del juego

    // Constructor, inicializa el gestor del juego
    public MenuJuego(Juego juego) {
        MenuJuego.juego = juego;
    }

    // CLI para registrar un nuevo jugador
    // luego de registrar al jugador muestra todos los jugadores registrados
    public static void registrarJugador() {
        String nombre = JOptionPane.showInputDialog("Nombre del jugador:");
        while (nombre == null || nombre.trim().isEmpty()) {
            JOptionPane.showMessageDialog(null, "El nombre no puede estar vacío. Inténtelo de nuevo.", "Error",
                    JOptionPane.ERROR_MESSAGE);
            nombre = JOptionPane.showInputDialog("Nombre del jugador:");
        }

        String teclaA = JOptionPane.showInputDialog("Tecla de aceptación:");
        while (teclaA == null || teclaA.trim().isEmpty()) {
            JOptionPane.showMessageDialog(null, "La tecla de aceptación no puede estar vacía. Inténtelo de nuevo.",
                    "Error", JOptionPane.ERROR_MESSAGE);
            teclaA = JOptionPane.showInputDialog("Tecla de aceptación:");
        }

        String teclaR = JOptionPane.showInputDialog("Tecla de rechazo:");
        while (teclaR == null || teclaR.trim().isEmpty()) {
            JOptionPane.showMessageDialog(null, "La tecla de rechazo no puede estar vacía. Inténtelo de nuevo.",
                    "Error", JOptionPane.ERROR_MESSAGE);
            teclaR = JOptionPane.showInputDialog("Tecla de rechazo:");
        }

        if (MenuJuego.juego.agregarJugador(new Jugador(nombre, teclaA, teclaR))) {
            JOptionPane.showMessageDialog(null, "Jugador registrado con éxito!!");
        } else {
            JOptionPane.showMessageDialog(null, "No se pudo registrar el jugador", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public static void editarJugador(int op) {
        if (op < MenuJuego.juego.jugadores.size() && op >= 0) {
            String nombre = JOptionPane.showInputDialog("Nombre del jugador:");
            String teclaA = JOptionPane.showInputDialog("Tecla de aceptación:");
            String teclaR = JOptionPane.showInputDialog("Tecla de rechazo:");
            MenuJuego.juego.jugadores.get(op).nombre = nombre;
            MenuJuego.juego.jugadores.get(op).teclaAceptación = teclaA;
            MenuJuego.juego.jugadores.get(op).teclaRechazo = teclaR;
            JOptionPane.showMessageDialog(null, "Jugador actualizado con éxito!!");
            MenuJuego.juego.imprimirJugadores();
        } else {
            JOptionPane.showMessageDialog(null, "No se puede editar jugador fuera de rango", "Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    private static void eliminarJugador(int op) {
        if (op < MenuJuego.juego.jugadores.size() && op >= 0) {
            MenuJuego.juego.jugadores.remove(op);
            JOptionPane.showMessageDialog(null, "Jugador eliminado con éxito!!");
        } else {
            JOptionPane.showMessageDialog(null, "No se puede eliminar jugador fuera de rango", "Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    // Menu principal
    // opción 1 lleva al menú de registro de jugadores
    // opción 2 inicia el juego
    public static void menuPrincipal() {
        int opcion = 0;
        do {
            String input = JOptionPane.showInputDialog(
                    "**************************************************\n" +
                            "JUEGO DE LA FRASE INTERMINABLE\n" +
                            "**************************************************\n" +
                            "1.- Jugadores\n" +
                            "2.- Iniciar nuevo juego\n" +
                            "3.- Salir\n" +
                            "Por favor, seleccione una opción (1-3):");
            try {
                opcion = Integer.parseInt(input);
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(null, "¡Cuidado! Solo puedes insertar números.", "Error",
                        JOptionPane.ERROR_MESSAGE);
                continue; // Volver a pedir la opción sin realizar ninguna acción
            }
            switch (opcion) {
                case 1:
                    menuRegistro();
                    break;
                case 2:
                    verificarJugadores();
                    break;
                case 3:
                    JOptionPane.showMessageDialog(null, "Hasta pronto!!");
                    break;
                default:
                    JOptionPane.showMessageDialog(null, "Opción no válida. Por favor, seleccione una opción (1-3).",
                            "Error",
                            JOptionPane.ERROR_MESSAGE);
                    break;
            }
        } while (opcion != 3);
    }

    // Método para verificar si hay dos o más jugadores para iniciar el juego
    public static void verificarJugadores() {
        if (MenuJuego.juego.jugadores.size() > 1) {
            MenuJuego.juego.iniciarJuego();
        } else {
            JOptionPane.showMessageDialog(null, "No hay suficiente cantidad de jugadores para comenzar el juego",
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    // Menu de registro de jugadores
    // opción 1 para registrar un nuevo jugador
    // opción 2 para ver los jugadores que se encuentran registrados
    public static void menuRegistro() {
        int opcion = 0;
        do {
            String input = JOptionPane.showInputDialog(
                    "*******************************************\n" +
                            "MENU REGISTRO JUGADORES\n" +
                            "*******************************************\n" +
                            "1.- Registrar jugador\n" +
                            "2.- Jugadores registrados\n" +
                            "3.- Editar jugador\n" +
                            "4.- Eliminar jugador\n" +
                            "5.- Salir\n" +
                            "Por favor, seleccione una opción (1-5):");
            try {
                opcion = Integer.parseInt(input);
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(null, "¡Cuidado! Solo puedes insertar números.", "Error",
                        JOptionPane.ERROR_MESSAGE);
                continue; // Volver a pedir la opción sin realizar ninguna acción
            }
            switch (opcion) {
                case 1:
                    registrarJugador();
                    break;
                case 2:
                    if (MenuJuego.juego.jugadores.isEmpty()) {
                        JOptionPane.showMessageDialog(null, "No hay jugadores registrados.", "Información",
                                JOptionPane.INFORMATION_MESSAGE);
                    } else {
                        MenuJuego.juego.imprimirJugadores();
                    }
                    break;
                case 3:
                    if (MenuJuego.juego.jugadores.isEmpty()) {
                        JOptionPane.showMessageDialog(null, "No hay jugadores registrados.", "Información",
                                JOptionPane.INFORMATION_MESSAGE);
                    } else {
                        MenuJuego.juego.imprimirJugadores();
                        input = JOptionPane.showInputDialog(
                                "Seleccione jugador a editar (1-" + MenuJuego.juego.jugadores.size() + "):");
                        try {
                            opcion = Integer.parseInt(input);
                        } catch (NumberFormatException e) {
                            JOptionPane.showMessageDialog(null, "¡Cuidado! Solo puedes insertar números.", "Error",
                                    JOptionPane.ERROR_MESSAGE);
                            continue; // Volver a pedir la opción sin realizar ninguna acción
                        }
                        editarJugador(opcion - 1);
                    }
                    break;
                case 4:
                    if (MenuJuego.juego.jugadores.isEmpty()) {
                        JOptionPane.showMessageDialog(null, "No hay jugadores registrados.", "Información",
                                JOptionPane.INFORMATION_MESSAGE);
                    } else {
                        MenuJuego.juego.imprimirJugadores();
                        input = JOptionPane.showInputDialog(
                                "Seleccione jugador a eliminar (1-" + MenuJuego.juego.jugadores.size() + "):");
                        try {
                            opcion = Integer.parseInt(input);
                        } catch (NumberFormatException e) {
                            JOptionPane.showMessageDialog(null, "¡Cuidado! Solo puedes insertar números.", "Error",
                                    JOptionPane.ERROR_MESSAGE);
                            continue; // Volver a pedir la opción sin realizar ninguna acción
                        }
                        eliminarJugador(opcion - 1);
                    }
                    break;
                case 5:
                    return; // Salir del menú de registro
                default:
                    JOptionPane.showMessageDialog(null, "Opción no válida. Por favor, seleccione una opción (1-5).",
                            "Error",
                            JOptionPane.ERROR_MESSAGE);
                    break;
            }
        } while (opcion != 5);
    }
}