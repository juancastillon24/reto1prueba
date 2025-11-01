package user;

import lombok.Data;

@Data
public class Usuario {
    private Integer id_Usuario;
    private String email;
    private String contraseña;
}
