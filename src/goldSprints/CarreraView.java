package goldSprints;

import java.text.DecimalFormat;

import interfazSerie.ArduinoApp;
import interfazSerie.ProcesoArduino;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.TimelineBuilder;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import javafx.util.Duration;
import objetos.Configuracion;
import objetos.Muestra;

public class CarreraView extends Pane {
	@SuppressWarnings("unused")
	private Stage primaryStage;
	
	private final Image BICI1 = new Image(this.getClass().getResourceAsStream("/bike.png"));
	private final Image BICI2 = new Image(this.getClass().getResourceAsStream("/bike.png"));
	
	private Canvas canvas;
	private Reloj reloj;
	
	private Configuracion conf;
	private double avanceNoAjus = 0;
	private double avance = 0;
	private int[] posBicis;
	private double[] velocidades;
	private Muestra[] muestras;
	private final int POSINI = 20;
	private long tiempoIni = 0;
	DecimalFormat formatonum = new DecimalFormat("###.##");
	
	private ProcesoArduino procesoArd;
	final Duration oneFrameAmt = Duration.millis(1000/60);
	
	private ArduinoApp arduino;
	
	public CarreraView(Stage primaryStage, Configuracion conf){
		this.primaryStage = primaryStage;
		this.conf = conf;
		
		primaryStage.setOnCloseRequest(new EventHandler<WindowEvent>() {
			@Override
			public void handle(WindowEvent arg0) {
				arduino.exit();
			}
		} );
		
		canvas = new Canvas(800, 600);
		getChildren().addAll(canvas);
		
		//INICIALIZACIÓN PARÁMETROS
		double factorAdv = canvas.getWidth()/(conf.getDistancia()*100);
		avanceNoAjus = 2*Math.PI*conf.getRadioRueda();
		avance = avanceNoAjus*factorAdv;
		
		posBicis = new int[conf.getCorredores()];
		velocidades = new double[conf.getCorredores()];
		muestras = new Muestra[conf.getCorredores()];
				
		for(int i = 0; i < conf.getCorredores(); i++){
			posBicis[i] = POSINI;
			muestras[i] = new Muestra(avanceNoAjus);
		}
		
		reloj = new Reloj(Color.RED, Color.WHITE);
		reloj.setLayoutX(180);
		reloj.setLayoutY(200);
		reloj.refreshClocks(0);
		getChildren().add(reloj);
		
		TimelineBuilder.create()
		   .cycleCount(Animation.INDEFINITE)
		   .keyFrames(oneFrame)
		   .build()
		   .play();
        
		arduino = new ArduinoApp(conf);
	}
	
	private void dibujarVelocidad(GraphicsContext gc){
		gc.setFill(Color.BLUE);
		gc.fillRect(20, 180, 30, velocidades[0]*1.6);
		
		gc.setFill(Color.RED);
		gc.fillRect(60, 180, 30, 160);
		
		gc.setFill(Color.BLACK);
		gc.fillText(formatonum.format(velocidades[0]), 20, 360);
		//gc.fillText(velocidades[1]+"", 60, 360);
		gc.fillText("Km/h", 100, 360);
	}
	
	private void dibujarReloj(GraphicsContext gc){
		
	}

	public void iniciar() {
		procesoArd = new ProcesoArduino(conf, arduino);
		tiempoIni = System.currentTimeMillis();
		procesoArd.start();
	}
	
	public void parar() {
		procesoArd.parar();
	}
	
	final KeyFrame oneFrame = new KeyFrame(oneFrameAmt,
	   new EventHandler<ActionEvent>() {
		
	   @Override
	   public void handle(ActionEvent event) {
		    GraphicsContext gc = canvas.getGraphicsContext2D();
		    gc.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());
		    
		    if(procesoArd != null && procesoArd.isAlive()){
		    	actualizarDistancias();
		    	actualizarVelocidades();
		    }
		    
		    gc.drawImage(BICI1, posBicis[0], 20);
			
			gc.setStroke(Color.BLUE);
	        gc.setLineWidth(5);
	        gc.strokeLine(20, 70, 700, 70);
	        
	        gc.drawImage(BICI2, posBicis[1], 90);
			
			gc.setStroke(Color.RED);
	        gc.setLineWidth(5);
	        gc.strokeLine(20, 140, 700, 140);
	        
	        dibujarVelocidad(gc);
	        dibujarReloj(gc);
	   }
	});
	
	private void actualizarDistancias(){
		reloj.refreshClocks(System.currentTimeMillis()-tiempoIni);
		int[] contadores = procesoArd.obtenerContadores();
		
		for(int i = 0; i < conf.getCorredores(); i++){
			muestras[i].anyadirMuestra(contadores[i], System.currentTimeMillis());
			posBicis[i] = (int) ((contadores[i]*avance)+20);
		}
	}
	
	private void actualizarVelocidades(){
		for(int i = 0; i < conf.getCorredores(); i++){
			velocidades[i] = muestras[i].calcularVelocidad();
		}
	}
}
