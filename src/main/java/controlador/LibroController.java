package controlador;


import modelo.LibroDAO;

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

/**
 *
 * @author SENA
 */
public class LibroController {
    private LibroView vista;
    private LibroDAO modelo;

    public LibroController(LibroView vista, LibroDAO modelo) {
        this.vista = vista;
        this.modelo = modelo;
        // Inicializar y añadir oyentes a los botones
    }

    // Métodos para manejar eventos de los botones (crear, leer, actualizar, eliminar)
}

