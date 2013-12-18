package objetos;

import java.io.IOException;
import java.util.Properties;

public class Configuracion {

	private String puerto;
	
	private boolean tipoTiempo;
	
	private long tiempo = 0;
	
	private long distancia = 1000;
	
	private int corredores;
	
	private double radioRueda = 0;
	
	public Configuracion() {
		super();
		Properties prop = new Properties(); 
		try {
			prop.load(this.getClass().getClassLoader().getResourceAsStream("configuracion.properties"));
			this.puerto = prop.getProperty("puerto");
			this.corredores = new Integer(prop.getProperty("corredores"));
			this.radioRueda = new Double(prop.getProperty("radioRueda"));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public String getPuerto() {
		return puerto;
	}

	public void setPuerto(String puerto) {
		this.puerto = puerto;
	}

	public boolean isTipoTiempo() {
		return tipoTiempo;
	}

	public void setTipoTiempo(boolean tipoTiempo) {
		this.tipoTiempo = tipoTiempo;
	}

	public long getTiempo() {
		return tiempo;
	}

	public void setTiempo(long tiempo) {
		this.tiempo = tiempo;
	}

	public long getDistancia() {
		return distancia;
	}

	public void setDistancia(long distancia) {
		this.distancia = distancia;
	}

	public int getCorredores() {
		return corredores;
	}

	public void setCorredores(int corredores) {
		this.corredores = corredores;
	}

	public double getRadioRueda() {
		return radioRueda;
	}

	public void setRadioRueda(double radioRueda) {
		this.radioRueda = radioRueda;
	}
	
	
}
