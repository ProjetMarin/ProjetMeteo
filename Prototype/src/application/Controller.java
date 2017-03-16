package application;

import java.net.URL;
import java.util.ResourceBundle;

import javax.swing.JFileChooser;
import javax.swing.filechooser.FileNameExtensionFilter;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;

public class Controller {

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
	void quitter(ActionEvent event) {
		System.exit(0);
	}

	@FXML
	void changeToFrench(ActionEvent event) {
		Alert alert = new Alert(AlertType.INFORMATION);
		alert.setTitle("Changing language");
		alert.setHeaderText(null);
		alert.setContentText("The language system has been change to French");
		alert.showAndWait();
	}

	@FXML
	void openFile(ActionEvent event) {


		JFileChooser chooser = new JFileChooser();
		FileNameExtensionFilter filter = new FileNameExtensionFilter(
				"Grib & NetCDF Files", "grb", "nc");
		chooser.setFileFilter(filter);
		int returnVal = chooser.showOpenDialog(chooser);
		if(returnVal == JFileChooser.APPROVE_OPTION) {
			System.out.println("You chose to open this file: " +
					chooser.getSelectedFile().getName());
		}
	}


	@FXML
	void test(MouseEvent event) {

	}

	@FXML
	void initialize() {
		assert Ctrler != null : "fx:id=\"Ctrler\" was not injected: check your FXML file 'windSea.fxml'.";

	}

}
