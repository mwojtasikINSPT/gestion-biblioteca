package controllers;

public class ConsultasController {

    private final LectorController pacienteController;
    //private final CamaController camaController;
    private final PrestamoController reservaController;
    private final AsignacionController asignacionController;
    private final BibliotecarioController medicoController;

    public ConsultasController() {
        this.pacienteController = new LectorController();
        //this.camaController = new CamaController();
        this.reservaController = new PrestamoController();
        this.asignacionController = new AsignacionController();
        this.medicoController = new BibliotecarioController();
    }


}
