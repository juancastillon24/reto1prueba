package user;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Optional;

public class FileUserService implements UserService {
    private String archivo;

    public FileUserService(String userFile) {
        archivo = userFile;
    }

    /**
     *
     * @param email
     * @param contraseña
     * comprobacion de que las credenciales son correctas
     * @return hacer el login bien o mal depende de si esta bien
     */
   @Override
   public Optional<Usuario> validate(String email, String contraseña) {
       try (BufferedReader br = new BufferedReader(new FileReader(archivo))) {
           String linea;

           while ((linea = br.readLine()) != null) {

               String[] partes = linea.split(",");

               if (partes.length >= 3) {
                   String idUsuarioCsv = partes[0].trim();
                   String emailCsv = partes[1].trim();
                   String contraseñaCsv = partes[2].trim();

                   if (emailCsv.equals(email) && contraseñaCsv.equals(contraseña)) {
                       Usuario usuario = new Usuario();
                       usuario.setId_Usuario(Integer.parseInt(idUsuarioCsv));
                       usuario.setEmail(emailCsv);
                       usuario.setContraseña(contraseñaCsv);

                       return Optional.of(usuario);
                   }
               }
           }

       } catch (IOException e) {
           throw new RuntimeException("Error leyendo el archivo CSV", e);
       }

       return Optional.empty();
   }

}
