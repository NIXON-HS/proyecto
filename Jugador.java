package com.proyecto;

public class Jugador {
    String nombre;
    String teclaAceptación;
    String teclaRechazo;
    double puntuacion;
    double tiempo;

    public Jugador(String nombre, String teclaAceptación, String teclaRechazo) {
        this.nombre = nombre;
        this.teclaAceptación = teclaAceptación;
        this.teclaRechazo = teclaRechazo;
        this.puntuacion = 0;
        this.tiempo = 0;
    }

    @Override
    public String toString() {
        return "Nombre: " + nombre + ", Tecla de Aceptación: " + teclaAceptación + ", Tecla de Rechazo: " + teclaRechazo;
    }

    public void sumarTiempo(double tiempo) {
        this.tiempo += tiempo;
    }

    public void puntuar(double puntos) {
        this.puntuacion += puntos;
    }
}