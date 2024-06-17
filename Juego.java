package com.proyecto;

import java.util.LinkedList;
import java.util.Random;
import javax.swing.JOptionPane;

public class Juego {
    public LinkedList<Jugador> jugadores; // Lista de los jugadores registrados
    public LinkedList<Jugador> jugadoresSobrantes; // Jugadores que están jugando y que se van eliminando
    public LinkedList<String> frase; // Lista que almacena las palabras de la frase

    public Juego() {
        this.jugadores = new LinkedList<>();
        this.frase = new LinkedList<>();
    }

    // Agrega un jugador a la lista de jugadores registrados
    public boolean agregarJugador(Jugador jugador) {
        return this.jugadores.add(jugador);
    }

    // Cambia el orden de los jugadores a un orden aleatorio
    public void elegirOrden() {
        int n = this.jugadores.size();
        Random random = new Random();
        for (int i = 0; i < n; i++) {
            int change = i + random.nextInt(n - i);
            cambiar(i, change);
        }
        this.jugadoresSobrantes = new LinkedList<>(this.jugadores);
    }

    // Se utiliza en el método `elegirOrden`, intercambia las posiciones de los
    // jugadores
    public void cambiar(int i, int change) {
        Jugador helper = this.jugadores.get(i);
        this.jugadores.set(i, this.jugadores.get(change));
        this.jugadores.set(change, helper);
    }

    // Imprime todos los jugadores registrados
    public void imprimirJugadores() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < jugadores.size(); i++) {
            Jugador jugador = jugadores.get(i);
            sb.append((i + 1) + ". Nombre: " + jugador.nombre + ", Tecla de Aceptación: " + jugador.teclaAceptación
                    + ", Tecla de Rechazo: " + jugador.teclaRechazo + "\n");
        }
        JOptionPane.showMessageDialog(null, sb.toString(), "Jugadores Registrados", JOptionPane.INFORMATION_MESSAGE);
    }

    // Inicializa la lista frase para guardar las palabras, todavía está en
    // construcción
    public void iniciarJuego() {
        this.frase = new LinkedList<>();
        LinkedList<Jugador> eliminados = new LinkedList<>();
        JOptionPane.showMessageDialog(null, "El juego ha comenzado!", "Inicio del Juego",
                JOptionPane.INFORMATION_MESSAGE);
        elegirOrden(); // Ensure players are in a random order
        StringBuilder mensaje = new StringBuilder("Orden de los jugadores:\n");
        for (int i = 0; i < jugadores.size(); i++) {
            Jugador jugador = jugadores.get(i);
            mensaje.append((i + 1) + ". Nombre: " + jugador.nombre + ", Tecla de Aceptación: " + jugador.teclaAceptación
                    + ", Tecla de Rechazo: " + jugador.teclaRechazo + "\n");
        }
        JOptionPane.showMessageDialog(null, mensaje.toString(), "Orden de los Jugadores",
                JOptionPane.INFORMATION_MESSAGE);

        // Start the game loop
        while (jugadoresSobrantes.size() > 1) {
            for (int i = 0; i < jugadoresSobrantes.size(); i++) {
                Jugador jugador = jugadoresSobrantes.get(i);
                JOptionPane.showMessageDialog(null, "Es el turno de " + jugador.nombre, "Turno del Jugador",
                        JOptionPane.INFORMATION_MESSAGE);
                JOptionPane.showMessageDialog(null, "Frase actual: " + String.join(" ", frase), "Frase Actual",
                        JOptionPane.INFORMATION_MESSAGE);

                long startTime = System.currentTimeMillis(); // Start timing
                String input = JOptionPane.showInputDialog("Ingrese la frase:");
                long endTime = System.currentTimeMillis(); // End timing

                double timeTaken = (endTime - startTime) / 1000.0; // Time in seconds
                jugador.sumarTiempo(timeTaken);

                String[] palabras = input.split(" ");
                boolean error = comprobarError(palabras, i);

                // Calificación por otros jugadores
                for (int j = 0; j < jugadoresSobrantes.size(); j++) {
                    if (j != i) {
                        Jugador calificador = jugadoresSobrantes.get(j);
                        String decision;
                        do {
                            decision = JOptionPane.showInputDialog(
                                    calificador.nombre + " está calificando la frase de " + jugador.nombre
                                            + "\nTecla de aceptación: " + calificador.teclaAceptación
                                            + ", Tecla de rechazo: " + calificador.teclaRechazo
                                            + "\n¿La frase es correcta? (aceptar/rechazar): ");
                            if (!decision.equalsIgnoreCase(calificador.teclaAceptación)
                                    && !decision.equalsIgnoreCase(calificador.teclaRechazo)) {
                                JOptionPane.showMessageDialog(null,
                                        "Entrada inválida. Por favor, ingrese solo la tecla de aceptación o rechazo.",
                                        "Advertencia", JOptionPane.WARNING_MESSAGE);
                            }
                        } while (!decision.equalsIgnoreCase(calificador.teclaAceptación)
                                && !decision.equalsIgnoreCase(calificador.teclaRechazo));
                        boolean calificacion = decision.equalsIgnoreCase(calificador.teclaAceptación);
                        calificar(j, error, calificacion);
                    }
                }

                if (error) {
                    JOptionPane.showMessageDialog(null,
                            "La frase fue rechazada. " + jugador.nombre + " ha sido eliminado.", "Frase Rechazada",
                            JOptionPane.INFORMATION_MESSAGE);
                    eliminados.add(jugadoresSobrantes.remove(i));
                    i--; // Adjust index after removal
                } else {
                    JOptionPane.showMessageDialog(null, "La frase fue aceptada.", "Frase Aceptada",
                            JOptionPane.INFORMATION_MESSAGE);
                }

                // Mostrar puntajes y tiempos actuales
                StringBuilder sb = new StringBuilder("Puntajes y tiempos actuales:\n");
                jugadoresSobrantes.sort((j1, j2) -> Double.compare(j2.puntuacion, j1.puntuacion)); // Ordenar por
                                                                                                   // puntaje
                                                                                                   // descendente
                for (Jugador j : jugadoresSobrantes) {
                    sb.append(j.nombre + " - Puntaje: " + j.puntuacion + ", Tiempo: " + j.tiempo + "\n");
                }
                JOptionPane.showMessageDialog(null, sb.toString(), "Puntajes y Tiempos",
                        JOptionPane.INFORMATION_MESSAGE);
            }

            // Eliminar jugador con menor puntaje
            eliminarJugadorConMenorPuntaje();

            // Mostrar estadísticas finales y ganador
            JOptionPane.showMessageDialog(null, "El juego ha terminado!", "Fin del Juego",
                    JOptionPane.INFORMATION_MESSAGE);
            StringBuilder sb = new StringBuilder("Resultados finales:\n");
            eliminados.addAll(jugadoresSobrantes); // Añadir los jugadores sobrantes a la lista de eliminados
            eliminados.sort((j1, j2) -> Double.compare(j2.puntuacion, j1.puntuacion)); // Ordenar por puntaje
                                                                                       // descendente
            for (Jugador j : eliminados) {
                sb.append(j.nombre + " - Puntaje: " + j.puntuacion + ", Tiempo: " + j.tiempo + "\n");
            }
            JOptionPane.showMessageDialog(null, sb.toString(), "Resultados Finales", JOptionPane.INFORMATION_MESSAGE);

            // Mostrar el ganador
            if (!eliminados.isEmpty()) {
                Jugador ganador = eliminados.get(0);
                JOptionPane.showMessageDialog(null,
                        "El ganador es: " + ganador.nombre + " con un puntaje de " + ganador.puntuacion,
                        "Ganador", JOptionPane.INFORMATION_MESSAGE);
            }
            // Regresar al menú principal
            MenuJuego.menuPrincipal();
        }
    }

    private void eliminarJugadorConMenorPuntaje() {
        if (this.jugadoresSobrantes.isEmpty())
            return;

        double menorPuntaje = Double.MAX_VALUE;
        int id = -1;
        for (int i = 0; i < this.jugadoresSobrantes.size(); i++) {
            if (this.jugadoresSobrantes.get(i).puntuacion < menorPuntaje) {
                menorPuntaje = this.jugadoresSobrantes.get(i).puntuacion;
                id = i;
            }
        }

        if (id != -1) {
            JOptionPane.showMessageDialog(null, "Se eliminó al jugador: " + this.jugadoresSobrantes.get(id).nombre
                    + " por tener el menor puntaje...", "Jugador Eliminado", JOptionPane.INFORMATION_MESSAGE);
            this.jugadoresSobrantes.remove(id);
        }
    }

    // Método dado de baja
    public void equivocacion(String[] frase, String mensaje1, String mensaje2) {
        String ultimaPalabra = frase[frase.length - 1];
        int equivocaciones = 0;
        for (int i = 0; i < this.frase.size(); i++) {
            if (!frase[i].equalsIgnoreCase(this.frase.get(i))) {
                equivocaciones++;
            }
        }
        if (equivocaciones > 0) {
            JOptionPane.showMessageDialog(null, mensaje1, "Error", JOptionPane.ERROR_MESSAGE);
        } else {
            JOptionPane.showMessageDialog(null, mensaje2, "Correcto", JOptionPane.INFORMATION_MESSAGE);
            if (frase.length - 1 == this.frase.size()) {
                this.frase.add(ultimaPalabra);
            }
        }
    }

    // Un jugador en la posición i califica con true o false la frase de otro
    // jugador
    public void calificar(int i, boolean error, boolean calificacion) {
        if (error) {
            if (calificacion) {
                this.jugadoresSobrantes.get(i).puntuar(-0.5);
            } else {
                this.jugadoresSobrantes.get(i).puntuar(0.5);
            }
        } else {
            if (calificacion) {
                this.jugadoresSobrantes.get(i).puntuar(-0.5);
            } else {
                this.jugadoresSobrantes.get(i).puntuar(0.5);
            }
        }
    }

    // Comprueba si hay errores en la frase escrita por un jugador
    public boolean comprobarError(String[] frase, int jugador) {
        if ((frase.length) - this.frase.size() > 1) {
            return true;
        } else if ((frase.length) < this.frase.size()) {
            return true;
        } else if ((frase.length) == this.frase.size()) {
            return true;
        } else {
            if (this.frase.isEmpty()) {
                String ultimaPalabra = frase[frase.length - 1];
                this.frase.add(ultimaPalabra);
                this.jugadoresSobrantes.get(jugador).puntuar(1);
                return false;
            } else {
                int equivocaciones = 0;
                for (int i = 0; i < this.frase.size(); i++) {
                    if (!frase[i].equalsIgnoreCase(this.frase.get(i))) {
                        equivocaciones++;
                    }
                }
                if (equivocaciones > 0) {
                    return true;
                } else {
                    this.frase.add(frase[frase.length - 1]);
                    this.jugadoresSobrantes.get(jugador).puntuar(1);
                    return false;
                }
            }
        }
    }
}