package ui;

import context.ContextService;
import user.UserService;
import user.Usuario;

import javax.swing.*;
import java.awt.event.*;

public class Login extends JDialog {
    private JPanel contentPane;
    private JButton buttonOK;
    private JButton buttonCancel;
    private JTextField txtEmail;
    private JPasswordField txtContraseña;

    UserService userService;
    JFrame parent;

    public Login(JFrame parent, UserService us) {

        // para poder usar makeLogin
        this.parent = parent;

        //  Inyección de dependencia
        userService = us;

        setContentPane(contentPane);
        setModal(true);
        getRootPane().setDefaultButton(buttonOK);
        pack();
        setLocationRelativeTo(parent);

        buttonOK.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onOK();
            }
        });

        buttonCancel.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onCancel();
            }
        });

        // call onCancel() when cross is clicked
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                onCancel();
            }
        });

        // call onCancel() on ESCAPE
        contentPane.registerKeyboardAction(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onCancel();
            }
        }, KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);
    }

    /**
     * Comprueba las credenciales a la hora de iniciar sesion
     * para poder acceder a mas funcionalidades
     * @return sesion iniciada
     */
    private void onOK() {
        userService.validate(txtEmail.getText(), txtContraseña.getText()).ifPresentOrElse(
                (Usuario user) -> {
                    //AppContext.usuarioActivo = user;
                    ContextService.getInstance().addItem("usuarioActivo", user);
                    dispose();
                },
                () -> {
                    JOptionPane.showMessageDialog(parent, "Usuario no existe");
                });
    }

    private void onCancel() {
        // add your code here if necessary
        dispose();
    }
}
