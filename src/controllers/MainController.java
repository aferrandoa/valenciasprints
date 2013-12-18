package controllers;

import goldSprints.CarreraView;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import objetos.Configuracion;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Tooltip;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class MainController implements Initializable {
	private Stage primaryStage;
	private Configuracion conf;
	private CarreraView carrera;
	
	private final Image ICON_CONF = new Image(this.getClass().getResourceAsStream("/settings.png"));
	private final Image ICON_START = new Image(this.getClass().getResourceAsStream("/startLight.png"));
	private final Image ICON_STOP = new Image(this.getClass().getResourceAsStream("/stopRace.png"));
	
	@FXML private HBox contenedor;
	@FXML private Button btnConf;
	@FXML private Button btnStart;
	@FXML private Button btnStop;
	
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		ImageView imagenConf = new ImageView(ICON_CONF);
		btnConf.setGraphic(imagenConf);
		
		btnConf.setTooltip(new Tooltip("Configuración"));
		
		ImageView imagenStart = new ImageView(ICON_START);
		btnStart.setGraphic(imagenStart);
		
		btnStart.setTooltip(new Tooltip("Iniciar"));
		
		ImageView imagenStop = new ImageView(ICON_STOP);
		btnStop.setGraphic(imagenStop);
		
		btnStop.setTooltip(new Tooltip("Parar"));
	}
	
	public void setConf(Configuracion conf) {
		this.conf = conf;
	}

	public void setPrimaryStage(Stage primaryStage) {
		this.primaryStage = primaryStage;
	}
	
	public void configurar(){
		carrera = new CarreraView(primaryStage, conf);
		contenedor.getChildren().add(carrera);
	}
	
	@FXML
    protected void clickConfiguracion(ActionEvent event) throws IOException{
		
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/configuracion.fxml"));
        Parent root = (Parent) loader.load();
        
        ConfiguracionController controlador = loader.getController();
        controlador.setConfiguracion(conf);
        
        Stage dialogStage = new Stage();
        dialogStage.setTitle("Configuración");
        dialogStage.initModality(Modality.WINDOW_MODAL);
        dialogStage.initOwner(primaryStage);
        Scene scene = new Scene(root);
        dialogStage.setScene(scene);

        dialogStage.showAndWait();
    }
	
	@FXML
	protected void clickStart(ActionEvent event) throws IOException{
		btnStart.setDisable(true);
		btnStop.setDisable(false);
		carrera.iniciar();
	}
	
	@FXML
	protected void clickStop(ActionEvent event) throws IOException{
		btnStart.setDisable(false);
		btnStop.setDisable(true);
		carrera.parar();
	}
}
