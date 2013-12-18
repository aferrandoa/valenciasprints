package interfazSerie;

import objetos.Configuracion;

public class ProcesoArduino extends Thread {
	
	private ArduinoApp arduino;
	private boolean activo = true;
	private Configuracion conf;
	
	private int[] contadores;
	private int corredores;
	
	public ProcesoArduino(Configuracion conf, ArduinoApp arduino) {
		super();
		this.conf = conf;
		corredores = conf.getCorredores();
		contadores = new int[corredores];
		this.arduino = arduino;		
	}
	
	public void run() {
		int[] valoresAnt = new int[corredores];
		int[] valoresAct = new int[corredores];
		
		while(activo){
			valoresAct = arduino.leerValores();
			
			for(int i = 0; i < corredores; i++){
				contadores[i] += obtenerIncremento(valoresAnt[i], valoresAct[i]);
			}
			
			valoresAnt = valoresAct.clone();
		}
	}
	
	public void parar(){
		activo = false;
	}
	
	private int obtenerIncremento(int valorAnt, int valorAct){
		int res = 0;
		
		if(valorAct != valorAnt && valorAct == 1){
			res = 1;
		}
		
		return res;
	}
	
	public int[] obtenerContadores(){
		return contadores;
	}
}
