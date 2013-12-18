package controllers;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Control;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.stage.Stage;
import objetos.Configuracion;
import cc.arduino.Arduino;

public class ConfiguracionController implements Initializable{
	@FXML private ComboBox<String> cboPuertos;
	
	@FXML private ToggleGroup radioTipo;
	
	@FXML private TextField txtTiempo;
	
	@FXML private TextField txtDistancia;
	
	private Configuracion conf;

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		String[] listaPuertos = Arduino.list();
		cboPuertos.getItems().addAll(listaPuertos);
	}
	
	@FXML
	protected void aceptarClick(ActionEvent event){
		conf.setDistancia(new Long(txtDistancia.getText()));
		conf.setTiempo(new Long(txtTiempo.getText()));
		conf.setPuerto(cboPuertos.getValue());
		
		cerrarPopUp(event.getSource());
	}
	
	@FXML
	protected void cancelarClick(ActionEvent event){
		cerrarPopUp(event.getSource());
	}
	
	private void cerrarPopUp(Object disparador){
		Control control = (Control) disparador;
		Stage stage = (Stage)control.getScene().getWindow();
	    stage.close();
	}
	
	public void setConfiguracion(Configuracion conf){
		this.conf = conf;
		txtTiempo.setText(this.conf.getTiempo()+"");
		txtDistancia.setText(this.conf.getDistancia()+"");
		cboPuertos.setValue(this.conf.getPuerto());
	}
}
