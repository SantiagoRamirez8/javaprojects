package controller;

import com.dao.PrestamosDAO;
import com.dao.UsuariosDAO;
import model.Usuarios;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import views.PrestamoView;
import views.UsuariosView;

public class UsuarioController {

    private UsuariosView view;
    private UsuariosDAO dao;
    

    public UsuarioController(UsuariosView view, UsuariosDAO dao) {
        this.view = view;
        this.dao = dao;
        this.view.btnCrear.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                crearUsuario();
            }
        });
        this.view.btnMostrar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                mostrarUsuarios();
            }
        });
        this.view.btnActualizar.addActionListener(new ActionListener() {
            @Override

            public void actionPerformed(ActionEvent e) {
                mostrarUsuarios();
                if (view.txtId.getText().isEmpty()
                        || view.txtNombre.getText().isEmpty()
                        || view.txtEmail.getText().isEmpty()
                        || view.txtTelefono.getText().isEmpty()) {
                    JOptionPane.showMessageDialog(view, "Por favor, seleccione un usuario de la tabla para actualizar.");
                } else {
                    actualizarUsuario();
                }

            }
        }
        );

        this.view.btnEliminar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                mostrarUsuarios();
                if (view.txtId.getText().isEmpty()
                        || view.txtNombre.getText().isEmpty()
                        || view.txtEmail.getText().isEmpty()
                        || view.txtTelefono.getText().isEmpty()) {
                    JOptionPane.showMessageDialog(view, "Por favor, seleccione un usuario de la tabla para eliminar.");
                } else {
                    int response = JOptionPane.showConfirmDialog(
                            view,
                            "¿Está seguro de que desea eliminar este usuario?",
                            "Confirmar eliminación",
                            JOptionPane.YES_NO_OPTION,
                            JOptionPane.QUESTION_MESSAGE
                    );
                    if (response == JOptionPane.YES_OPTION) {
                        eliminarUsuario();
                    }
                }
            }
        }
        );

        this.view.tablaUsuarios.getSelectionModel()
                .addListSelectionListener(new ListSelectionListener() {
                    @Override
                    public void valueChanged(ListSelectionEvent e
                    ) {
                        if (!e.getValueIsAdjusting() && view.tablaUsuarios.getSelectedRow() != -1) {
                            mostrarDatosSeleccionados();
                        }
                    }
                }
                        
                );
        
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

    private void crearUsuario() {
        String nombre = view.txtNombre.getText();
        String email = view.txtEmail.getText();
        String telefono = view.txtTelefono.getText();

        Usuarios usuario = new Usuarios(nombre, email, telefono);
        dao.crearUsuarios(usuario);

        JOptionPane.showMessageDialog(view, "Usuario creado exitosamente.");
        limpiarCampos();
    }

    private void mostrarUsuarios() {
        List<Usuarios> listaUsuarios = dao.leerUsuarios();
        DefaultTableModel model = (DefaultTableModel) view.tablaUsuarios.getModel();
        model.setRowCount(0);

        for (Usuarios usuario : listaUsuarios) {
            model.addRow(new Object[]{usuario.getId(), usuario.getNombre(), usuario.getEmail(), usuario.getTelefono()});
        }
    }

    private void actualizarUsuario() {
        int id = Integer.parseInt(view.txtId.getText());
        String nombre = view.txtNombre.getText();
        String email = view.txtEmail.getText();
        String telefono = view.txtTelefono.getText();

        Usuarios usuario = new Usuarios(nombre, email, telefono);
        dao.actualizarUsuarios(usuario);

        JOptionPane.showMessageDialog(view, "Usuario actualizado exitosamente.");
        limpiarCampos();
        mostrarUsuarios(); // Actualizar la tabla después de actualizar el usuario
    }

    private void eliminarUsuario() {
        int id = Integer.parseInt(view.txtId.getText());
        dao.eliminarUsuario(id);

        JOptionPane.showMessageDialog(view, "Usuario eliminado exitosamente.");
        limpiarCampos();
        mostrarUsuarios(); // Actualizar la tabla después de eliminar el usuario
    }

    private void limpiarCampos() {
        view.txtId.setText("");
        view.txtNombre.setText("");
        view.txtEmail.setText("");
        view.txtTelefono.setText("");
    }

    private void mostrarDatosSeleccionados() {
        int row = view.tablaUsuarios.getSelectedRow();
        view.txtId.setText(view.tablaUsuarios.getValueAt(row, 0).toString());
        view.txtNombre.setText(view.tablaUsuarios.getValueAt(row, 1).toString());
        view.txtEmail.setText(view.tablaUsuarios.getValueAt(row, 2).toString());
        view.txtTelefono.setText(view.tablaUsuarios.getValueAt(row, 3).toString());
    }
}