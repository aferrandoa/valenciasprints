package goldSprints;

import java.util.Calendar;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.Parent;
import javafx.scene.effect.Glow;
import javafx.scene.effect.InnerShadow;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.util.Duration;

public class Reloj extends Parent {
    private Calendar calendar = Calendar.getInstance();
    private Digito[] digits;
    private Timeline delayTimeline, secondTimeline;

    public Reloj(Color onColor, Color offColor) {
        // create effect for on LEDs
        Glow onEffect = new Glow(1.7f);
        onEffect.setInput(new InnerShadow());
        // create effect for on dot LEDs
        /*
        Glow onDotEffect = new Glow(1.7f);
        onDotEffect.setInput(new InnerShadow(5,Color.BLACK));
        */
        // create effect for off LEDs
        InnerShadow offEffect = new InnerShadow();
        // create digits
        digits = new Digito[7];
        for (int i = 0; i < 6; i++) {
            Digito digit = new Digito(onColor, offColor, onEffect, offEffect);
            digit.setLayoutX(i * 80 + ((i + 1) % 2) * 20);
            digits[i] = digit;
            getChildren().add(digit);
        }
        // create dots
        Group dots = new Group(
                new Circle(80 + 54 + 20, 44, 6, onColor),
                new Circle(80 + 54 + 17, 64, 6, onColor),
                new Circle((80 * 3) + 54 + 20, 44, 6, onColor),
                new Circle((80 * 3) + 54 + 17, 64, 6, onColor));
        //dots.setEffect(onDotEffect);
        getChildren().add(dots);
        // update digits to current time and start timer to update every second
        refreshClocks(System.currentTimeMillis());
    }

    public void refreshClocks(long timeMilis) {
    	calendar.setTimeInMillis(timeMilis);
        int hours = calendar.get(Calendar.MINUTE);
        int minutes = calendar.get(Calendar.SECOND);
        int seconds = calendar.get(Calendar.MILLISECOND);
        digits[0].showNumber(hours / 10);
        digits[1].showNumber(hours % 10);
        digits[2].showNumber(minutes / 10);
        digits[3].showNumber(minutes % 10);
        digits[4].showNumber(seconds / 10);
        digits[5].showNumber(seconds % 10);
    }

    public void play() {
        // wait till start of next second then start a timeline to call refreshClocks() every second
        delayTimeline = new Timeline();
        delayTimeline.getKeyFrames().add(
                new KeyFrame(new Duration(1000 - (System.currentTimeMillis() % 1000)), new EventHandler<ActionEvent>() {
                    @Override public void handle(ActionEvent event) {
                        if (secondTimeline != null) {
                            secondTimeline.stop();
                        }
                        secondTimeline = new Timeline();
                        secondTimeline.setCycleCount(Timeline.INDEFINITE);
                        secondTimeline.getKeyFrames().add(
                                new KeyFrame(Duration.seconds(1), new EventHandler<ActionEvent>() {
                                    @Override public void handle(ActionEvent event) {
                                        //refreshClocks();
                                    }
                                }));
                        secondTimeline.play();
                    }
                })
        );
        delayTimeline.play();
    }

    public void stop(){
        delayTimeline.stop();
        if (secondTimeline != null) {
            secondTimeline.stop();
        }
    }
}
