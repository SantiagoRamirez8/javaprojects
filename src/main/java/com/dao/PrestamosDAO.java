package com.dao;

import config.ConexionBD;
import model.Prestamo;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PrestamosDAO {

    private Connection conexion;
    private ConexionBD conexionBD;

    public PrestamosDAO() {
        conexionBD = new ConexionBD();
        conexion = conexionBD.getConexion();
    }

    // Método para crear un nuevo préstamo
    public void crearPrestamo(Prestamo prestamo) {
        String sql = "INSERT INTO prestamos (libro_id, usuario_id, fecha_prestamo, fecha_devolucion) VALUES (?, ?, ?, ?)";
        try (PreparedStatement statement = conexion.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS)) {
            statement.setInt(1, prestamo.getIdLibro());
            statement.setInt(2, prestamo.getIdUsuario());
            statement.setDate(3, Date.valueOf(prestamo.getFechaPrestamo()));
            if (prestamo.getFechaDevolucion() != null) {
                statement.setDate(4, Date.valueOf(prestamo.getFechaDevolucion()));
            } else {
                statement.setNull(4, Types.DATE);
            }
            statement.executeUpdate();

            try (ResultSet generatedKeys = statement.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    prestamo.setIdPrestamo(generatedKeys.getInt(1));
                } else {
                    throw new SQLException("No se pudo obtener el ID del préstamo insertado.");
                }
            }
        } catch (SQLException e) {
            System.out.println("Error al guardar los datos: " + e.getMessage());
        }
    }

    // Método para leer todos los préstamos
    public List<Prestamo> MostrarPrestamos() {
        List<Prestamo> prestamos = new ArrayList<>();
        String sql = "SELECT * FROM prestamos";
        try (Statement statement = conexion.createStatement(); ResultSet resultSet = statement.executeQuery(sql)) {
            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                int libroId = resultSet.getInt("libro_id");
                int usuarioId = resultSet.getInt("usuario_id");
                String fechaPrestamo = resultSet.getString("fecha_prestamo");
                String fechaDevolucion = resultSet.getString("fecha_devolucion");
                Prestamo prestamo = new Prestamo(id, libroId, usuarioId, fechaPrestamo, fechaDevolucion );
                prestamos.add(prestamo);
            }
        } catch (SQLException e) {
            System.out.println("Error al consultar los datos: " + e.getMessage());
        }
        return prestamos;
    }

    // Método para actualizar un préstamo
    public void actualizarPrestamo(Prestamo prestamo) {
        String sql = "UPDATE prestamos SET libro_id = ?, usuario_id = ?, fecha_prestamo = ?, fecha_devolucion = ? WHERE id = ?";
        try (PreparedStatement statement = conexion.prepareStatement(sql)) {
            statement.setInt(1, prestamo.getIdLibro());
            statement.setInt(2, prestamo.getIdUsuario());
            statement.setDate(3, Date.valueOf(prestamo.getFechaPrestamo()));
            if (prestamo.getFechaDevolucion() != null) {
                statement.setDate(4, Date.valueOf(prestamo.getFechaDevolucion()));
            } else {
                statement.setNull(4, Types.DATE);
            }
            statement.setInt(5, prestamo.getIdPrestamo());
            statement.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Error al actualizar los datos: " + e.getMessage());
        }
    }

    // Método para eliminar un préstamo
    public void eliminarPrestamo(int id) {
        String sql = "DELETE FROM prestamos WHERE id = ?";
        try (PreparedStatement statement = conexion.prepareStatement(sql)) {
            statement.setInt(1, id);
            statement.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Error al eliminar los datos: " + e.getMessage());
        }
    }

    // Método para cerrar la conexión a la base de datos
    public void cerrarConexion() {
        conexionBD.closeConexion(conexion);
    }
}