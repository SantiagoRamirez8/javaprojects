package com.dao;

import config.ConexionBD;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import model.LibrosCategoria;

public class LibrosCategoriasDAO {

    private Connection conexion;
    private ConexionBD conexionBD;

    public LibrosCategoriasDAO() {
        conexionBD = new ConexionBD();
        conexion = conexionBD.getConexion();
    }

    public void crearLibrosCategoria(LibrosCategoria libroscategoria) {
        String sql = "INSERT INTO Libros_Categorias (libro_id, categoria_id) VALUES (?, ?)";
        try (PreparedStatement statement = conexion.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS)) {
            statement.setInt(1, libroscategoria.getIdCategoria());
            statement.setInt(2, libroscategoria.getIdLibro());
            statement.executeUpdate();

            try (ResultSet generatedKeys = statement.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    LibrosCategoria.setId(generatedKeys.getInt(1));
                } else {
                    throw new SQLException("No se pudo obtener el ID del libro insertado.");
                }
            }
        } catch (SQLException e) {
            System.out.println("Error al guardar los datos: " + e.getMessage());
        }
    }

    public List<LibrosCategoria> leerLibrosCategoria() {
        List<LibrosCategoria> LibrosCategoria = new ArrayList<>();
        String sql = "SELECT * FROM LibrosCategoria";
        try (Statement statement = conexion.createStatement(); ResultSet resultSet = statement.executeQuery(sql)) {
            while (resultSet.next()) {
                int idcategoria = resultSet.getInt("idcategoria");
                int idlibro = resultSet.getInt("idlibro");
                LibrosCategoria libroscategoria = new LibrosCategoria(idcategoria, idlibro);
                LibrosCategoria.add(libroscategoria);
            }
        } catch (SQLException e) {
            System.out.println("Error al consultar los datos: " + e.getMessage());
        }
        return LibrosCategoria;
    }

    public void actualizarLibrosCategoria(LibrosCategoria libroscategoria) {
        String sql = "UPDATE libros SET idcategoria = ?, idlibro = ?";
        try (PreparedStatement statement = conexion.prepareStatement(sql)) {
            statement.setInt(1, libroscategoria.getIdCategoria());
            statement.setInt(2, libroscategoria.getIdLibro());
            statement.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Error al actualizar los datos: " + e.getMessage());
        }
    }

    public void eliminarLibrosCategoria(int id) {
        String sql = "DELETE FROM LibrosCategoria WHERE id = ?";
        try (PreparedStatement statement = conexion.prepareStatement(sql)) {
            statement.setInt(1, id);
            statement.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Error al eliminar los datos: " + e.getMessage());
        }
    }

    public boolean existeLibrosCategoria(int id) {
        String sql = "SELECT COUNT(*) AS count FROM librosCategoria WHERE id = ?";
        try (PreparedStatement statement = conexion.prepareStatement(sql)) {
            statement.setInt(1, id);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    int count = resultSet.getInt("count");
                    return count > 0;
                }
            }
        } catch (SQLException e) {
            System.out.println("Error al verificar la existencia del libro: " + e.getMessage());
        }
        return false;
    }

    public void cerrarConexion() {
        conexionBD.closeConexion(conexion);
    }
}
