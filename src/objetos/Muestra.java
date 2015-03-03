package objetos;

import java.util.ArrayList;

public class Muestra {

	private final int NUM_MUESTRAS = 60;
	private double distanciaPorCont = 0;
	
	private ArrayList<Integer> listaContador;
	private ArrayList<Long> listaTiempos;
	
	/**
	 * @param distanciaCont Distancia por contador en centímetros
	 */
	public Muestra(double distanciaCont){
		listaContador = new ArrayList<Integer>();
		listaTiempos = new ArrayList<Long>();
		distanciaPorCont = distanciaCont;
	}
	
	public void anyadirMuestra(int contador, long tiempo){
		listaContador.add(contador);
		listaTiempos.add(tiempo);
		
		if(listaContador.size() > NUM_MUESTRAS){
			listaContador.remove(0);
			listaTiempos.remove(0);
		}
	}
	
	public double calcularVelocidad(){
		if(listaContador.size() < NUM_MUESTRAS){
			return 0.0;
		}
		
		int diferenciaCont = listaContador.get(NUM_MUESTRAS-1) - listaContador.get(0);
		double diferenciaDist = diferenciaCont*distanciaPorCont;
		diferenciaDist = diferenciaDist/100;
		
		long diferenciaTime = listaTiempos.get(NUM_MUESTRAS-1) - listaTiempos.get(0);
		double segundos = (diferenciaTime/1000.0);
		
		double mtsSegundo = diferenciaDist/segundos;
		
		return mtsSegundo*3.6;
	}
}
