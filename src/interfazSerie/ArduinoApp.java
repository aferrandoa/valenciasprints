package interfazSerie;

import objetos.Configuracion;
import processing.core.PApplet;
import cc.arduino.Arduino;

public class ArduinoApp extends PApplet {

	private static final long serialVersionUID = 1L;
	
	private Configuracion conf;
	
	private Arduino arduino;
	private int ledPin = 13;
	private int VCC = 12;
	private int INOPT = 10;
	
	public ArduinoApp(Configuracion conf) {
		this.conf = conf;
		setup();
	}
	
	public void setup() {
		arduino = new Arduino(this, "/dev/ttyACM0", 57600);
		
		arduino.pinMode(ledPin, Arduino.OUTPUT);
	    
	    arduino.pinMode(VCC, Arduino.OUTPUT);
	    arduino.digitalWrite(VCC, Arduino.HIGH);
	    
	    arduino.pinMode(INOPT, Arduino.INPUT);
	}

	public int[] leerValores(){
		int[] retorno = new int[2];
		
		retorno[0] = arduino.digitalRead(INOPT);
		retorno[1] = arduino.digitalRead(INOPT);
		
		return retorno;
	}
}
