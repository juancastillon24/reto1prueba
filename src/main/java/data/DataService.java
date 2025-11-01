package data;

import java.util.List;
import java.util.Optional;

public interface DataService {

    public List<Pelicula> findAll();
    Optional<Pelicula> save(Pelicula pelicula);
    public List<Pelicula> delete() ;

}
