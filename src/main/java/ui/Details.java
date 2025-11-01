package ui;

import context.ContextService;
import data.CsvDataService;
import data.DataService;
import data.Pelicula;

import javax.swing.*;
import java.awt.event.*;

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

        eliminarButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                dataService.delete();
                dispose();
            }
        });
    }

    private void onOK() {
        // add your code here
        dispose();
    }


}
