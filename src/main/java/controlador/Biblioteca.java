package controlador;

import com.dao.PrestamosDAO;
import controller.PrestamoController;
import javax.swing.*;
import views.PrestamoView;

public class Biblioteca {

    public static void main(String[] args) {
           SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                PrestamoView view = new PrestamoView();
                PrestamosDAO dao = new PrestamosDAO();
                PrestamoController controller = new PrestamoController(view, dao);
                
                JFrame frame = new JFrame("Gestor de Usuarios");
                frame.setContentPane(view);
                frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                frame.pack();
                frame.setVisible(true);
            }
        });
    }
}
