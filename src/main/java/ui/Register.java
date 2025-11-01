package ui;

import user.Usuario;

import javax.swing.*;
import java.awt.event.*;
import java.io.*;
import java.util.ArrayList;
import java.util.logging.Logger;

public class Register extends JDialog {
    private JPanel contentPane;
    private JButton buttonOK;
    private JButton buttonCancel;
    private JTextField txtEmail;
    private JPasswordField contraseña;
    private JPasswordField confirmacionContraseña;
    private static Integer lastId = -1;
    private static Logger logger = Logger.getLogger(Register.class.getName());


    public Register() {


        setContentPane(contentPane);
        setModal(true);
        getRootPane().setDefaultButton(buttonOK);
        pack();
        setLocationRelativeTo(null);

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

    private void onOK() {
        if (confirmacionContraseña.getText().equals(contraseña.getText())) {

            var salida = new ArrayList<Usuario>();

            logger.info("Abriendo archivo");
            try (BufferedReader br = new BufferedReader(new FileReader(new File("usuarios.csv")))) {
                var contenido = br.lines();

                contenido.forEach(line -> {
                    String[] lineArray = line.split(",");
                    if (lineArray.length < 3) {
                        logger.severe("Linea mal formada");
                    } else {
                        Usuario usuario = new Usuario();
                        usuario.setId_Usuario(Integer.parseInt(lineArray[0]));
                        usuario.setEmail(lineArray[1]);
                        usuario.setContraseña(lineArray[2]);
                        salida.add(usuario);
                    }
                });
                lastId = salida.size();
                logger.info("Actualizo tamaño: " + lastId);

            } catch (FileNotFoundException e) {
                throw new RuntimeException(e);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            try(var bfw = new BufferedWriter(new FileWriter(new File("usuarios.csv"),true))){
                lastId++;
                Usuario usuario = new Usuario();
                usuario.setId_Usuario(lastId);
                usuario.setEmail(txtEmail.getText());
                usuario.setContraseña(contraseña.getText());
                logger.info("Actualizo idUsuario: " + lastId);
                String nuevoUsuario = new StringBuilder()
                        .append(usuario.getId_Usuario()).append(",")
                        .append(usuario.getEmail()).append(",")
                        .append(usuario.getContraseña()).toString();
                logger.info("Nuevo usuario creado con exito: " + nuevoUsuario);
                bfw.write(nuevoUsuario);
                bfw.newLine();
                logger.info("Nuevo usuario guardado con exito");


            }catch(IOException e){
                throw new RuntimeException(e);

            }
            dispose();
        }
        else JOptionPane.showMessageDialog(null,"Las contraseñas no coinciden","",JOptionPane.WARNING_MESSAGE);
    }

    private void onCancel() {
        // add your code here if necessary
        dispose();
    }
}
