package ui;

import context.ContextService;
import data.CsvDataService;
import data.DataService;
import data.Pelicula;
import user.Usuario;

import javax.swing.*;
import java.awt.event.*;
import java.util.Optional;

public class Details extends JDialog {
    private JPanel contentPane;
    private JButton buttonOK;
    private JLabel labelTitulo;
    private JLabel labelAño;
    private JLabel labelDirector;
    private JLabel labelDescripcion;
    private JLabel labelGenero;
    private JLabel labelImagen;
    private JLabel labelIdUsuario;
    private JButton eliminarButton;
    private DataService dataService;


    public Details(JFrame parent) {

        this.dataService = new CsvDataService("peliculas.csv");
        setContentPane(contentPane);
        setModal(true);
        getRootPane().setDefaultButton(buttonOK);
        setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(parent);
        pack();

        Pelicula pelicula = (Pelicula) ContextService.getInstance().getItem("peliculaSeleccionada").get();

        setTitle(pelicula.getTitulo());

        labelTitulo.setText(pelicula.getTitulo());
        labelAño.setText(pelicula.getAño().toString());
        labelDirector.setText(pelicula.getDirector());
        labelDescripcion.setText(pelicula.getDescripcion());
        labelGenero.setText(pelicula.getGenero());
        labelImagen.setText(pelicula.getImagen());
        labelIdUsuario.setText(pelicula.getId_UsuarioPelicula().toString());


        buttonOK.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onOK();
            }
        });

        // call onCancel() on ESCAPE
        contentPane.registerKeyboardAction(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onOK();
            }
        }, KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);


        /**
         * Comprueba si el usuario esta logeado y si lo esta,
         * comprueba que su id coincida con la id de quien
         * creó la pelicula para poder borrarla
         * @return la lista de peliculas sin la que se ha eliminado
         */
        eliminarButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (ContextService.getInstance().getItem("usuarioActivo").isPresent()) {
                    Optional<Object> peliculaOpt = ContextService.getInstance().getItem("peliculaSeleccionada");
                    Pelicula peliculaEliminar = (Pelicula) peliculaOpt.get();
                    int usuarioPelicula = peliculaEliminar.getId_UsuarioPelicula();
                    Optional<Object> usuarioOpt = ContextService.getInstance().getItem("usuarioActivo");
                    Usuario usuario = (Usuario) usuarioOpt.get();
                    int idUsuario = usuario.getId_Usuario();
                    if (idUsuario == usuarioPelicula) {
                        dataService.delete();
                        dispose();
                    } else
                        JOptionPane.showMessageDialog(null, "La pelicula solo la puede eliminar el usuario que lo ha creado, la id del usuario que la creó es " + usuarioPelicula, "", JOptionPane.WARNING_MESSAGE);
                } else
                    JOptionPane.showMessageDialog(null, "Necesitas iniciar sesion para eliminar una pelicula", null, JOptionPane.WARNING_MESSAGE);
            }
        });
    }

    private void onOK() {
        // add your code here
        dispose();
    }


}
