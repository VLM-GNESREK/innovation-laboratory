package at.technikumwien.qds;

import at.technikumwien.qds.core.BombTesterInterferometer;
import at.technikumwien.qds.model.Bomb;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.PrintStream;

public class BombTesterFX extends Application {

    @Override
    public void start(Stage stage) {

        TextArea output = new TextArea();
        output.setEditable(false);
        output.setWrapText(true);

        // System.out in die TextArea umleiten
        System.setOut(new PrintStream(new java.io.OutputStream() {
            @Override
            public void write(int b) {
                output.appendText(String.valueOf((char) b));
            }
        }));

        Button runButton = new Button("Simulation starten");
        Button clearButton = new Button("Ausgabe löschen");

        runButton.setOnAction(e -> {
            BombTesterInterferometer tester = new BombTesterInterferometer();
            for (int i = 0; i < 10; i++) {
                tester.runExperiment(new Bomb("Bomb-" + i, Math.random() < 0.5));
            }
            tester.printStats();
        });

        clearButton.setOnAction(e -> output.clear());

        VBox root = new VBox(10, runButton, clearButton, output);
        root.setStyle("-fx-padding: 10;");

        stage.setTitle("Elitzur-Vaidman Bomb Tester");
        stage.setScene(new Scene(root, 700, 500));
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
