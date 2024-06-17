package com.proyecto;

public class App {
    public static void main(String[] args) {
        // Aplicacion llama al menu principal y recibe de parametro un GestorJuego
        MenuJuego menu = new MenuJuego(new Juego());
        menu.menuPrincipal();
    }
}