package application;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;

public class Ctrler {

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private VBox Ctrler;

    @FXML
    void ConfigurationAvance(ActionEvent event) {
    	System.out.println("Lancement fenetre avancé");
    }

    @FXML
    void test(MouseEvent event) {

    }

    @FXML
    void initialize() {
        assert Ctrler != null : "fx:id=\"Ctrler\" was not injected: check your FXML file 'gg.fxml'.";

    }
}
