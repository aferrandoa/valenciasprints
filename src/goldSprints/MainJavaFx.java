package goldSprints;

import java.io.IOException;

import objetos.Configuracion;

import controllers.MainController;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
 
public class MainJavaFx extends Application {
	
	private Configuracion conf = new Configuracion();
	
    public static void main(String[] args) {
        launch(args);
    }
    
    @Override
    public void start(Stage primaryStage) throws IOException {
    	FXMLLoader loader = new FXMLLoader(getClass().getResource("/vistaPrincipal.fxml"));
    	Parent root = (Parent) loader.load();
        MainController controlador = loader.getController();
        controlador.setPrimaryStage(primaryStage);
        controlador.setConf(conf);
        controlador.configurar();
    	
        Scene scene = new Scene(root, 800, 600);
        scene.getStylesheets().addAll(getClass().getResource("/styles.css").toExternalForm());
    
        primaryStage.setTitle("VLCSprints");
        primaryStage.setScene(scene);
        primaryStage.show();
    }
}