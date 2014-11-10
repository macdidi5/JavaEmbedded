package fxthreaddemo;

import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.control.Slider;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import javafx.util.Duration;

public class FXThreadDemo extends Application {
    
    static int working = 0;
    
    @Override
    public void start(Stage primaryStage) {
        GridPane root = new GridPane();
        String image = FXThreadDemo.class.getResource("background.jpeg").toExternalForm();
        root.setStyle("-fx-background-image: url('" + image + "'); -fx-background-position: center center; -fx-background-repeat: stretch;");
        
        root.setHgap(10);
        root.setVgap(10);
        root.setPadding(new Insets(10, 10, 10, 10));
        
        final Slider slider = new Slider();
        slider.setMax(31);
        slider.setMin(0);
        root.add(slider, 0, 0, 4, 1);
        
        ImageView imageExit = new ImageView(
                new Image(FXThreadDemo.class.getResourceAsStream("exit.png")));
        
        imageExit.setOnMouseClicked(new EventHandler<MouseEvent>(){ 

            @Override
            public void handle(MouseEvent t) {
                System.exit(0);
            }
            
        });
        
        root.add(imageExit, 7, 0);
        
        final ProgressIndicator[] ps = new ProgressIndicator[32];
        final Timeline[] timelines = new Timeline[32];
        final KeyValue[] keyValues = new KeyValue[32];
        final KeyFrame[] keyFrames = new KeyFrame[32];
        
        for (int i = 0; i < ps.length; i++) {
            ps[i] = new ProgressIndicator();
            ps[i].setPrefSize(60, 60);
            ps[i].setProgress(0.0);
            ps[i].setStyle("-fxpercentage: hidden;");
            
            timelines[i] = new Timeline();
            timelines[i].setCycleCount(Timeline.INDEFINITE);
            timelines[i].setAutoReverse(true);
            
            keyValues[i] = new KeyValue(ps[i].progressProperty(), 1);
            keyFrames[i] = new KeyFrame(Duration.millis(5000), keyValues[i]);
            timelines[i].getKeyFrames().add(keyFrames[i]);
        }
        
        for (int i = 0; i < 8; i++) {
            root.add(ps[i], i, 1);
            root.add(ps[i + 8], i, 2);
            root.add(ps[i + 16], i, 3);
            root.add(ps[i + 24], i, 4);
        }
        
        Scene scene = new Scene(root, 580, 420);
        
        primaryStage.setTitle("Hello World!");
        primaryStage.setScene(scene);
        primaryStage.show();
        
        timelines[0].play();
        
        slider.setOnMouseReleased(new EventHandler<MouseEvent>() {

            @Override
            public void handle(MouseEvent event) {
                int newValue = (int)slider.getValue();
                
                if (newValue > working) {
                    for (int i = working + 1; i <= newValue; i++) {
                        timelines[i].play();
                    }
                }
                else if (working > newValue) {
                    for (int i = working; i > newValue; i--) {
                        timelines[i].stop();
                        ps[i].setProgress(0.0);
                    }
                }
                
                working = newValue;
            }
            
        });
        
    }

    public static void main(String[] args) {
        launch(args);
    }
    
}
