package data;

import context.ContextService;

import javax.swing.*;
import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.logging.Logger;


public class CsvDataService implements DataService {

    private String archivo;

    private static Integer lastId = -1;

    private static Logger logger = Logger.getLogger(CsvDataService.class.getName());


    public CsvDataService(String csvFile) {
        archivo = csvFile;
    }

    @Override
    public List<Pelicula> findAll() {
        var salida = new ArrayList<Pelicula>();

        logger.info("Abriendo archivo");
        try (BufferedReader br = new BufferedReader(new FileReader(new File(archivo)))) {
            var contenido = br.lines();

            contenido.forEach(line -> {
                String[] lineArray = line.split(",");
                if (lineArray.length < 7) {
                    logger.severe("Linea mal formada");
                } else {
                    Pelicula pelicula = new Pelicula();
                    pelicula.setId(Integer.parseInt(lineArray[0]));
                    pelicula.setTitulo(lineArray[1]);
                    pelicula.setAño(Integer.parseInt(lineArray[2]));
                    pelicula.setDirector(lineArray[3]);
                    pelicula.setDescripcion(lineArray[4]);
                    pelicula.setGenero(lineArray[5]);
                    pelicula.setImagen(lineArray[6]);
                    pelicula.setId_UsuarioPelicula(Integer.parseInt(lineArray[7]));
                    salida.add(pelicula);
                }
            });
            lastId = salida.size();
            logger.info("Actualizo tamaño: " + lastId);

        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return salida;
    }

    @Override
    public Optional<Pelicula> save(Pelicula pelicula) {
        logger.info("Abriendo el archivo para escribir");
        try (var bfw = new BufferedWriter(new FileWriter(new File(archivo), true))) {

            lastId++;
            pelicula.setId(lastId);
            logger.info("Actualizando id: " + lastId);

            String salida = new StringBuilder()
                    .append(pelicula.getId()).append(",")
                    .append(pelicula.getTitulo()).append(",")
                    .append(pelicula.getAño()).append(",")
                    .append(pelicula.getDirector()).append(",")
                    .append(pelicula.getDescripcion()).append(",")
                    .append(pelicula.getGenero()).append(",")
                    .append(pelicula.getImagen()).append(",")
                    .append(pelicula.getId_UsuarioPelicula()).toString();
            logger.info("Nueva pelicula creada");
            bfw.write(salida);
            bfw.newLine();
            logger.info("Pelicula guardada");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return Optional.of(pelicula);
    }

    @Override
    public List<Pelicula> delete() {

        var salida = new ArrayList<Pelicula>();
        try {
            Optional<Object> peliculaOpt = ContextService.getInstance().getItem("peliculaSeleccionada");
            Pelicula peliculaEliminar = (Pelicula) peliculaOpt.get();
            int idEliminar = peliculaEliminar.getId();

            try (BufferedReader br = new BufferedReader(new FileReader(new File(archivo)))) {
                var contenido = br.lines();

                contenido.forEach(line -> {
                    String[] lineArray = line.split(",");
                    if (lineArray.length < 7) {
                        logger.severe("Linea mal formada");
                    } else {
                        Pelicula pelicula = new Pelicula();
                        pelicula.setId(Integer.parseInt(lineArray[0]));
                        pelicula.setTitulo(lineArray[1]);
                        pelicula.setAño(Integer.parseInt(lineArray[2]));
                        pelicula.setDirector(lineArray[3]);
                        pelicula.setDescripcion(lineArray[4]);
                        pelicula.setGenero(lineArray[5]);
                        pelicula.setImagen(lineArray[6]);
                        pelicula.setId_UsuarioPelicula(Integer.parseInt(lineArray[7]));

                        if (idEliminar == pelicula.getId()) {

                        } else salida.add(pelicula);
                    }
                });
                lastId = salida.size();
                logger.info("Actualizo tamaño: " + lastId);

            } catch (FileNotFoundException e) {
                throw new RuntimeException(e);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            logger.info("Abriendo el archivo para eliminar la pelicula");
            try (var bfw = new BufferedWriter(new FileWriter(new File(archivo), false))) {

                for (Pelicula pelicula : salida) {
                    String linea = new StringBuilder()
                            .append(pelicula.getId()).append(",")
                            .append(pelicula.getTitulo()).append(",")
                            .append(pelicula.getAño()).append(",")
                            .append(pelicula.getDirector()).append(",")
                            .append(pelicula.getDescripcion()).append(",")
                            .append(pelicula.getGenero()).append(",")
                            .append(pelicula.getImagen()).append(",")
                            .append(pelicula.getId_UsuarioPelicula())
                            .toString();

                    bfw.write(linea);
                    bfw.newLine();
                }

                logger.info("Pelicula eliminada correctamente");

            } catch (IOException e) {
                throw new RuntimeException();
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "No hay pelicula seleccionada", "", JOptionPane.WARNING_MESSAGE);
        }

        return salida;
    }

}
