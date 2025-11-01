package ui;

import context.ContextService;
import data.DataService;
import data.Pelicula;
import user.Usuario;
import javax.swing.*;
import java.awt.event.*;
import java.util.Optional;

public class CreateForm extends JDialog {
    private JPanel contentPane;
    private JButton buttonOK;
    private JButton buttonCancel;
    private JTextField textTitulo;
    private JTextField textDirector;
    private JTextField textDescripcion;
    private JTextField textGenero;
    private JTextField textImagen;
    private JSpinner spinnerAño;

    private DataService dataService;
    JFrame parent;

    public CreateForm(DataService ds) {

        dataService = ds;

        setContentPane(contentPane);
        setModal(true);
        getRootPane().setDefaultButton(buttonOK);
        setLocationRelativeTo(parent);
        pack();

        spinnerAño.setModel(new SpinnerNumberModel(1980, 1950, 2025, 1));

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
        Pelicula pelicula = new Pelicula();

        pelicula.setTitulo(textTitulo.getText());
        pelicula.setAño((Integer)spinnerAño.getValue());
        pelicula.setDirector(textDirector.getText());
        pelicula.setDescripcion(textDescripcion.getText());
        pelicula.setGenero(textGenero.getText());
        pelicula.setImagen(textImagen.getText());
        Optional<Object> usuarioOpt = ContextService.getInstance().getItem("usuarioActivo");
        if (usuarioOpt.isPresent()) {
            Usuario usuario = (Usuario) usuarioOpt.get();
            pelicula.setId_UsuarioPelicula(usuario.getId_Usuario());
        }

        if (dataService.save(pelicula).isEmpty()){
            JOptionPane.showMessageDialog(this, "Error al guardar","",JOptionPane.WARNING_MESSAGE);
        }else  dispose();
    }

    private void onCancel() {
        // add your code here if necessary
        dispose();
    }


}
