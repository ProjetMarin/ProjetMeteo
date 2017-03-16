package application;

import java.io.FileInputStream;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

public final class Main extends Application {

	@Override
	public void start(final Stage primaryStage){
		
		
	     try {
	            // Localisation du fichier FXML.
	            URL url = getClass().getResource("gg.fxml");
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
	        primaryStage.setTitle("Test2 FXML");
	        primaryStage.show();
		 


	}

	public static void main(String[] args) {
		launch(args);
	}
}
