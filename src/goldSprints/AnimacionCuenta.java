package goldSprints;

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.animation.TimelineBuilder;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.text.Font;
import javafx.util.Duration;

public class AnimacionCuenta{
	public static final Duration oneFrameAmt = Duration.millis(1000);
	
	public static Timeline crearAnimacion(final Canvas canvas){
		
		final KeyFrame oneFrame = new KeyFrame(oneFrameAmt,
				new EventHandler<ActionEvent>() {
					
					int valor = 3;
				
					@Override
					public void handle(ActionEvent event) {				
						GraphicsContext gc = canvas.getGraphicsContext2D();
					    gc.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());
					    
					    Font font = new Font("Serif", 96);
					    gc.setFont(font);
					    
					    gc.fillText(""+valor, (canvas.getWidth()/2)-96, (canvas.getHeight()/2)-96);
					    
					    valor-=1;
					}
				});
		
		Timeline retorno = TimelineBuilder.create()
				   .cycleCount(4)
				   .keyFrames(oneFrame)
				   .build();
		
		return retorno;
	}
}
