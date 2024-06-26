package controller;

import com.dao.LibroDAO;
import com.dao.PrestamosDAO;
import model.Libro;
import views.LibroView;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import views.PrestamoView;

public class LibroController {

    private LibroView view;
    private LibroDAO dao;

    public LibroController(LibroView view, LibroDAO dao) {
        this.view = view;
        this.dao = dao;
        
        this.view.btnCrear.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                crearLibro();
            }
        });
        this.view.btnMostrar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                mostrarLibros();
            }
        });
        this.view.btnActualizar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (view.txtid.getText().isEmpty()) {
                    JOptionPane.showMessageDialog(view, "Por favor, seleccione un libro de la tabla para actualizar.");
                } else {
                    actualizarLibro();
                }
            }
        });
        this.view.btnEliminar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (view.txtid.getText().isEmpty()) {
                    JOptionPane.showMessageDialog(view, "Por favor, seleccione un libro de la tabla para eliminar.");
                } else {
                    int response = JOptionPane.showConfirmDialog(
                            view,
                            "¿Está seguro de que desea eliminar este libro?",
                            "Confirmar eliminación",
                            JOptionPane.YES_NO_OPTION,
                            JOptionPane.QUESTION_MESSAGE
                    );
                    if (response == JOptionPane.YES_OPTION) {
                        eliminarLibro();
                    }
                }
            }
        });
        this.view.tablaLibro.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                if (!e.getValueIsAdjusting() && view.tablaLibro.getSelectedRow() != -1) {
                    mostrarDatosSeleccionados();
                }
            }
        });

        mostrarLibros();
        
        this.view.btnVolver.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFrame currentFrame = (JFrame) SwingUtilities.getWindowAncestor(view);
                if (currentFrame != null) {
                    currentFrame.dispose();
                }
                SwingUtilities.invokeLater(new Runnable() {
                    public void run() {
                        PrestamoView view = new PrestamoView();
                        PrestamosDAO dao = new PrestamosDAO();
                        PrestamoController controller = new PrestamoController(view, dao);

                        JFrame frame = new JFrame("Gestor de Usuarios");
                        frame.setContentPane(view);
                        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
                        frame.pack();
                        frame.setVisible(true);

                    }
                });
            }
        });
    }

    private void crearLibro() {
        String titulo = view.txtTitulo.getText();
        String autor = view.txtAutor.getText();
        String editorial = view.txtEditorial.getText();
        int anioPublicacion = Integer.parseInt(view.txtAñoPublicacion.getText());
        String isbn = view.txtIsbn.getText();

        Libro libro = new Libro(titulo, autor, editorial, anioPublicacion, isbn);
        dao.crearLibro(libro);

        JOptionPane.showMessageDialog(view, "Libro creado exitosamente.");
        limpiarCampos();
        mostrarLibros();
    }

    private void mostrarLibros() {
        List<Libro> listaLibros = dao.leerLibros();
        DefaultTableModel model = (DefaultTableModel) view.tablaLibro.getModel();
        model.setRowCount(0);

        for (Libro libro : listaLibros) {
            model.addRow(new Object[]{libro.getId(), libro.getTitulo(), libro.getAutor(), libro.getEditorial(), libro.getAnio_publicacion(), libro.getIsbn()});
        }
    }

    private void actualizarLibro() {
        int id = Integer.parseInt(view.txtid.getText());
        String titulo = view.txtTitulo.getText();
        String autor = view.txtAutor.getText();
        String editorial = view.txtEditorial.getText();
        int anioPublicacion = Integer.parseInt(view.txtAñoPublicacion.getText());
        String isbn = view.txtIsbn.getText();

        Libro libro = new Libro(titulo, autor, editorial, id, isbn);
        dao.actualizarLibro(libro);
        JOptionPane.showMessageDialog(view, "Libro actualizado exitosamente.");
        limpiarCampos();
        mostrarLibros();
    }

    private void eliminarLibro() {
        int id = Integer.parseInt(view.txtid.getText());
        dao.eliminarLibro(id);

        JOptionPane.showMessageDialog(view, "Libro eliminado exitosamente.");
        limpiarCampos();
        mostrarLibros();
    }

    private void limpiarCampos() {
        view.txtid.setText("");
        view.txtTitulo.setText("");
        view.txtAutor.setText("");
        view.txtEditorial.setText("");
        view.txtAñoPublicacion.setText("");
        view.txtIsbn.setText("");
    }

    private void mostrarDatosSeleccionados() {
        int row = view.tablaLibro.getSelectedRow();
        view.txtid.setText(view.tablaLibro.getValueAt(row, 0).toString());
        view.txtTitulo.setText(view.tablaLibro.getValueAt(row, 1).toString());
        view.txtAutor.setText(view.tablaLibro.getValueAt(row, 2).toString());
        view.txtEditorial.setText(view.tablaLibro.getValueAt(row, 3).toString());
        view.txtAñoPublicacion.setText(view.tablaLibro.getValueAt(row, 4).toString());
        view.txtIsbn.setText(view.tablaLibro.getValueAt(row, 5).toString());
    }
}
