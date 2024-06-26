package controller;

import com.dao.CategoriaDAO;
import com.dao.LibroDAO;
import com.dao.PrestamosDAO;
import com.dao.UsuariosDAO;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;
import javax.swing.table.DefaultTableModel;
import model.Prestamo;
import views.CategoriaView;
import views.LibroView;
import views.PrestamoView;
import views.UsuariosView;

public class PrestamoController {

    private final PrestamoView view;
    private final PrestamosDAO dao;

    public PrestamoController(PrestamoView view, PrestamosDAO dao) {
        this.view = view;
        this.dao = dao;

        this.view.btnCrear.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                crearPrestamo();
            }
        });

        this.view.btnMostrar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                cargarPrestamos();
            }
        });

        this.view.btnActualizar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                actualizarPrestamo();
            }
        });

        this.view.btnEliminar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                eliminarPrestamo();
            }
        });
        this.view.btnCategorias.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFrame currentFrame = (JFrame) SwingUtilities.getWindowAncestor(view);
                if (currentFrame != null) {
                    currentFrame.dispose();
                }
                SwingUtilities.invokeLater(new Runnable() {
                    public void run() {
                        CategoriaView view = new CategoriaView();
                        CategoriaDAO dao = new CategoriaDAO();
                        CategoriaController controller = new CategoriaController(view, dao);

                        JFrame frame = new JFrame("Gestor de Usuarios");
                        frame.setContentPane(view);
                        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
                        frame.pack();
                        frame.setVisible(true);

                    }
                });
            }
        });
        this.view.btnLibros.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFrame currentFrame = (JFrame) SwingUtilities.getWindowAncestor(view);
                if (currentFrame != null) {
                    currentFrame.dispose();
                }
                SwingUtilities.invokeLater(new Runnable() {
                    public void run() {
                        LibroView view = new LibroView();
                        LibroDAO dao = new LibroDAO();
                        LibroController controller = new LibroController(view, dao);

                        JFrame frame = new JFrame("Gestor de Usuarios");
                        frame.setContentPane(view);
                        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
                        frame.pack();
                        frame.setVisible(true);

                    }
                });
            }
        });
        this.view.btnUsuarios.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFrame currentFrame = (JFrame) SwingUtilities.getWindowAncestor(view);
                if (currentFrame != null) {
                    currentFrame.dispose();
                }
                SwingUtilities.invokeLater(new Runnable() {
                    public void run() {
                        UsuariosView view = new UsuariosView();
                        UsuariosDAO dao = new UsuariosDAO();
                        UsuarioController controller = new UsuarioController(view, dao);

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

    private void cargarPrestamos() {
        List<Prestamo> prestamos = dao.MostrarPrestamos();
        DefaultTableModel model = (DefaultTableModel) view.tablaPrestamo.getModel();
        model.setRowCount(0);
        for (Prestamo prestamo : prestamos) {
            model.addRow(new Object[]{prestamo.getIdPrestamo(), prestamo.getIdLibro(), prestamo.getIdUsuario(), prestamo.getFechaPrestamo(), prestamo.getFechaDevolucion()});
        }
    }

    private void crearPrestamo() {
        try {
            Prestamo prestamo = new Prestamo();
            prestamo.setIdLibro(Integer.parseInt(view.txtIdLibro.getText()));
            prestamo.setIdUsuario(Integer.parseInt(view.txtIdUsuario.getText()));
            prestamo.setFechaPrestamo(view.txtFechaPrestamo.getText());
            prestamo.setFechaDevolucion(view.txtFechaDevolucion.getText());
            dao.crearPrestamo(prestamo);
            cargarPrestamos();
            JOptionPane.showMessageDialog(null, "Prestamo creado correctamente");
            limpiarCampos();
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(null, "Los campos de Id Libro e Id Usuario deben ser números enteros");
        }
    }

    private void actualizarPrestamo() {
        try {
            int filaSeleccionada = view.tablaPrestamo.getSelectedRow();
            if (filaSeleccionada != -1) {
                Prestamo prestamo = new Prestamo();
                prestamo.setIdPrestamo(Integer.parseInt(view.txtIdPrestamos.getText()));
                prestamo.setIdLibro(Integer.parseInt(view.txtIdLibro.getText()));
                prestamo.setIdUsuario(Integer.parseInt(view.txtIdUsuario.getText()));
                prestamo.setFechaPrestamo(view.txtFechaPrestamo.getText());
                prestamo.setFechaDevolucion(view.txtFechaDevolucion.getText());
                dao.actualizarPrestamo(prestamo);
                cargarPrestamos();
                JOptionPane.showMessageDialog(null, "Prestamo actualizado correctamente");
                limpiarCampos();
            } else {
                JOptionPane.showMessageDialog(null, "Selecciona un prestamo de la tabla para actualizar");
            }
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(null, "Los campos de Id Libro e Id Usuario deben ser números enteros");
        }
    }

    private void eliminarPrestamo() {
        int filaSeleccionada = view.tablaPrestamo.getSelectedRow();
        if (filaSeleccionada != -1) {
            int idPrestamo = (int) view.tablaPrestamo.getValueAt(filaSeleccionada, 0);
            dao.eliminarPrestamo(idPrestamo);
            cargarPrestamos();
            JOptionPane.showMessageDialog(null, "Prestamo eliminado correctamente");
            limpiarCampos();
        } else {
            JOptionPane.showMessageDialog(null, "Selecciona un prestamo de la tabla para eliminar");
        }
    }

    private void limpiarCampos() {
        view.txtIdPrestamos.setText("");
        view.txtIdLibro.setText("");
        view.txtIdUsuario.setText("");
        view.txtFechaPrestamo.setText("");
        view.txtFechaDevolucion.setText("");
    }
}