package ui;

import context.ContextService;
import data.DataService;
import data.Pelicula;
import user.UserService;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.ArrayList;

public class Main extends javax.swing.JFrame {
    private JPanel panel1;
    private JTable table1;

    private DataService dataservice;
    private UserService userservice;

    private ArrayList<Pelicula> peliculas = new ArrayList<>();

    /* Es necesario que este accesible para poder modificarlo */
    private JMenuItem menuItemAñadir;
    private JMenuItem menuItemDisconnect;

    public Main(DataService ds, UserService us) {
        dataservice = ds;
        userservice = us;

        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setTitle("Peliculas");
        this.setResizable(false);
        this.setSize(800, 600);
        this.setLocationRelativeTo(null);
        this.setContentPane(panel1);

        /* Añadir menu de forma programática */
        JMenuBar menuBar = PrepareMenuBar();
        panel1.add(menuBar, BorderLayout.NORTH);


        /* Configuración y carga de la tabla */
        var modelo = new DefaultTableModel();
        modelo.addColumn("ID");
        modelo.addColumn("Nombre");
        modelo.addColumn("Descripcion");
        table1.setModel(modelo);

        loadDataTable();

        table1.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        table1.getSelectionModel().addListSelectionListener((e) -> {
                    if (!e.getValueIsAdjusting() && table1.getSelectedRow() >= 0) {
                        System.out.println("seleccionado: " + table1.getSelectedRow());
                        Pelicula pelicula = peliculas.get(table1.getSelectedRow());

                        // AppContext.peliculaSeleccionada = pelicula;
                        ContextService.getInstance().addItem("peliculaSeleccionada", pelicula);

                        (new Details(this)).setVisible(true);
                        loadDataTable();
                    }
                }
        );
    }

    private void loadDataTable() {
        DefaultTableModel modelo = (DefaultTableModel) table1.getModel();
        peliculas = (ArrayList<Pelicula>) dataservice.findAll();
        modelo.setRowCount(0);
        peliculas.forEach( (j)->{
            var fila =  new Object[]{ j.getId(), j.getTitulo(),j.getDescripcion() };
            modelo.addRow(fila);
        });
    }

    private JMenuBar PrepareMenuBar() {
        JMenuBar menuBar = new JMenuBar();
        JMenu jMenuInicio = new JMenu("Inicio");
        JMenuItem menuItemLogin = new JMenuItem("Login");
        // es un atributo de la clase, no hay que hacer nwe
        menuItemAñadir = new JMenuItem("Añadir");
        menuItemAñadir.setEnabled(false);
        menuItemDisconnect = new JMenuItem("Cerrar Sesión");
        menuItemDisconnect.setEnabled(false);
        JMenuItem menuItemRegistrarse = new JMenuItem("Registrarse");
        JMenuItem menuItemSalir = new JMenuItem("Salir");

        menuBar.add(jMenuInicio);
        jMenuInicio.add(menuItemLogin);
        jMenuInicio.addSeparator();
        jMenuInicio.add(menuItemAñadir);
        jMenuInicio.addSeparator();
        jMenuInicio.add(menuItemDisconnect);
        jMenuInicio.addSeparator();
        jMenuInicio.add(menuItemRegistrarse);
        jMenuInicio.addSeparator();
        jMenuInicio.add(menuItemSalir);

        /* Eventos del menú */

        menuItemLogin.addActionListener(e -> {

            (new Login(this, userservice)).setVisible(true);

            // Como es modal, se espera la ejecución hasta que se cierre

            ContextService.getInstance().getItem("usuarioActivo").ifPresent( (_)->{
                menuItemAñadir.setEnabled(true);
            });
            ContextService.getInstance().getItem("usuarioActivo").ifPresent( (_)->{
                menuItemDisconnect.setEnabled(true);
            });
            ContextService.getInstance().getItem("usuarioActivo").ifPresent( (_)->{
                menuItemLogin.setEnabled(false);
            });
            ContextService.getInstance().getItem("usuarioActivo").ifPresent( (_)->{
                menuItemRegistrarse.setEnabled(false);
            });
        });

        menuItemRegistrarse.addActionListener(e -> {
            (new Register()).setVisible(true);
        });

        menuItemSalir.addActionListener(e -> { System.exit(0); });
        menuItemAñadir.addActionListener(e -> {
            // Añadir nueva pelicula
            (new CreateForm(dataservice)).setVisible(true);
            loadDataTable();
        });

        menuItemDisconnect.addActionListener(e -> {
            ContextService.getInstance().removeItem("usuarioActivo");
            if(ContextService.getInstance().getItem("usuarioActivo").isEmpty()){
                menuItemLogin.setEnabled(true);
                menuItemAñadir.setEnabled(false);
                menuItemDisconnect.setEnabled(false);
                menuItemRegistrarse.setEnabled(true);
            }
        });
        return menuBar;
    }

    public void start(){
        this.setVisible(true);
    }
}
