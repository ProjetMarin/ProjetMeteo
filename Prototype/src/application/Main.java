package application;

import java.io.IOException;
import java.net.URL;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public final class Main extends Application {

	@Override
	public void start(final Stage primaryStage){

		try {
			// Localisation du fichier FXML.
			URL url = getClass().getResource("windSea.fxml");
			// Création du loader.
			FXMLLoader fxmlLoader = new FXMLLoader(url);
			System.out.println("fmxloader"+url);
			// Chargement du FXML.
			VBox root = (VBox) fxmlLoader.load();
			// Accès au contrôleur.


			// Création de la scène.
			Scene scene = new Scene(root, 1275, 700);
			primaryStage.setScene(scene);
		} catch (IOException ex) {
			System.err.println("Erreur au chargement: " +ex);
			ex.printStackTrace();
		}
		primaryStage.setTitle("Dub Wind Sea");
		primaryStage.show();

	}

	public static void main(String[] args) {
		launch(args);

	}
}
