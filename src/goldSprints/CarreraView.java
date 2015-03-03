package goldSprints;

import interfazSerie.ArduinoApp;
import interfazSerie.ProcesoArduino;

import java.text.DecimalFormat;

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.animation.TimelineBuilder;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBoxBuilder;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import javafx.util.Duration;
import objetos.Configuracion;
import objetos.Muestra;

public class CarreraView extends Pane {
	@SuppressWarnings("unused")
	private Stage primaryStage;
	
	private final Image BICI1 = new Image(this.getClass().getResourceAsStream("/bikeAzul.png"));
	private final Image BICI2 = new Image(this.getClass().getResourceAsStream("/bikeRoja.png"));
	
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
	private Timeline animacionPrincipal;
	
	public CarreraView(Stage primaryStage, Configuracion conf){
		this.primaryStage = primaryStage;
		this.conf = conf;
		
		primaryStage.setOnCloseRequest(new EventHandler<WindowEvent>() {
			@Override
			public void handle(WindowEvent arg0) {
				if(arduino != null){
					arduino.exit();
				}
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
		
		reloj = new Reloj(Color.RED, Color.BLACK);
		reloj.setLayoutX(180);
		reloj.setLayoutY(200);
		reloj.refreshClocks(0);
		getChildren().add(reloj);
		
		animacionPrincipal = TimelineBuilder.create()
		   .cycleCount(Animation.INDEFINITE)
		   .keyFrames(oneFrame)
		   .build();
		
		animacionPrincipal.play();
        
		try{
			arduino = new ArduinoApp(conf);
		}
		catch(RuntimeException ex){
			errorInicandoArduino();
		}
	}
	
	private void dibujarVelocidad(GraphicsContext gc){
		gc.setFill(Color.BLUE);
		gc.fillRect(20, 180, 30, velocidades[0]*1.6);
		
		gc.setFill(Color.RED);
		gc.fillRect(60, 180, 30, 160);
		
		Font font = new Font("Serif", 12);
	    gc.setFont(font);
	    
		gc.setFill(Color.WHITE);
		gc.fillText(formatonum.format(velocidades[0]), 20, 360);
		//gc.fillText(velocidades[1]+"", 60, 360);
		gc.fillText("Km/h", 100, 360);
	}
	
	private void dibujarReloj(GraphicsContext gc){
		
	}

	public void iniciar() {
		animacionPrincipal.pause();
		reloj.setVisible(false);
		
		Timeline cuentaAtras = AnimacionCuenta.crearAnimacion(canvas);
		cuentaAtras.play();
		
		cuentaAtras.setOnFinished(new EventHandler<ActionEvent>() {
			
			@Override
			public void handle(ActionEvent arg0) {
				reloj.setVisible(true);
				animacionPrincipal.play();
				procesoArd = new ProcesoArduino(conf, arduino);
				tiempoIni = System.currentTimeMillis();
				procesoArd.start();
			}
		});
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
	
	/**
	 * Cancela el inicio de la aplicación cuando no se ha podido establecer la conexión con el
	 * dispositivo arduino. Avisa antes al usuario con un popUp.
	 * 
	 */
	private void errorInicandoArduino(){
		Stage dialogStage = new Stage();
		dialogStage.initModality(Modality.WINDOW_MODAL);
		
		Button btnSalir = new Button("Salir");
		btnSalir.setOnMouseClicked(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent arg0) {
				System.exit(0);
			}
		});
		
		dialogStage.setScene(new Scene(VBoxBuilder.create().
		    children(new Text("No se ha podido establecer la conexión con el dispositivo Arduino.\nPulse el botón para salir"), 
		    		btnSalir).
		    alignment(Pos.CENTER).padding(new Insets(5)).build()));
		dialogStage.showAndWait();
	}
}
