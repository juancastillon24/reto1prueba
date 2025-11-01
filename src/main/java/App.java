import data.CsvDataService;
import data.DataService;
import user.FileUserService;
import user.UserService;

public class App {
    public static void main(String[] args) {

        /**
         * Se crean los servicios aportandole los CSV
         * y despues se inicializa el main
         */
        DataService ds = new CsvDataService("peliculas.csv");
        UserService us = new FileUserService("usuarios.csv");
        (new ui.Main(ds,us)).start();

    }
}
