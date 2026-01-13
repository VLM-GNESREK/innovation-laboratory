package at.technikumwien.qds;

import at.technikumwien.qds.core.BombTesterInterferometer;
import at.technikumwien.qds.core.MachZehnderInterferometer;
import at.technikumwien.qds.core.MichelsonMorleyInterferometer;
import at.technikumwien.qds.model.Bomb;
import at.technikumwien.qds.model.Photon;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.OutputStream;
import java.io.PrintStream;

public class VisualizationApp extends Application
{
    private final PrintStream standardOut = System.out;

    public static void main(String[] args)
    {
        launch(args);
    }

    @Override
    public void start(Stage stage)
    {
        stage.setTitle("Quantum Discovery Simulator - Innovation Lab");

        TabPane tabPane = new TabPane();
        tabPane.setTabClosingPolicy(TabPane.TabClosingPolicy.UNAVAILABLE);

        Tab tabMZ = new Tab("Mach-Zehnder", createMachZehnderView());
        Tab tabBomb = new Tab("Bomb Tester", createBombTesterView());
        Tab tabMM = new Tab("Michelson-Morley", createMichelsonMorleyView());

        tabPane.getTabs().addAll(tabMZ, tabBomb, tabMM);

        Scene scene = new Scene(tabPane, 900, 600);
        stage.setScene(scene);
        stage.show();
    }

    /**
     * View for Mach-Zehnder Interferometer
     * Shows standard quantum interference (100% Path A).
     */
    private VBox createMachZehnderView()
    {
        VBox layout = new VBox(10);
        layout.setPadding(new Insets(15));

        Label title = new Label("Experiment 1: Mach-Zehnder Interferometer");
        title.setStyle("-fx-font-size: 18px; -fx-font-weight: bold;");

        Label desc = new Label("Demonstrates wave-particle duality. Constructive interference should result in 100% detection at Detector A.");
        desc.setWrapText(true);

        TextArea outputArea = createOutputArea();
        Button runBtn = new Button("Run 100 Photons");

        runBtn.setOnAction(e -> {
            redirectOutput(outputArea); // Send text to this box

            outputArea.clear();
            System.out.println("Initializing Mach-Zehnder...");

            MachZehnderInterferometer mz = new MachZehnderInterferometer();
            for (int i = 0; i < 100; i++) {
                mz.runExperiment(new Photon("P" + i));
            }
            mz.printStats();
        });

        layout.getChildren().addAll(title, desc, runBtn, outputArea);
        return layout;
    }

    /**
     * View for Bomb Tester
     */
    private VBox createBombTesterView()
    {
        VBox layout = new VBox(10);
        layout.setPadding(new Insets(15));

        Label title = new Label("Experiment 2: Elitzur-Vaidman Bomb Tester");
        title.setStyle("-fx-font-size: 18px; -fx-font-weight: bold;");

        Label desc = new Label("Can we detect a bomb without exploding it? 'Interaction-Free Measurement'.");

        TextArea outputArea = createOutputArea();

        // Options
        ComboBox<String> modeSelect = new ComboBox<>();
        modeSelect.getItems().addAll("10 Live Bombs", "10 Duds", "Mixed Batch (50/50)");
        modeSelect.getSelectionModel().selectFirst();

        Button runBtn = new Button("Test Bombs");

        runBtn.setOnAction(e ->
        {
            redirectOutput(outputArea);
            outputArea.clear();

            String mode = modeSelect.getValue();
            BombTesterInterferometer tester = new BombTesterInterferometer();

            System.out.println("Running Mode: " + mode);

            int count = 10;
            if (mode.contains("Mixed")) count = 50;

            for (int i = 0; i < count; i++)
            {
                boolean isLive = true;
                if (mode.contains("Duds")) isLive = false;
                if (mode.contains("Mixed")) isLive = Math.random() < 0.5;

                tester.runExperiment(new Bomb("Bomb-" + i, isLive));
            }
            tester.printStats();
        });

        layout.getChildren().addAll(title, desc, modeSelect, runBtn, outputArea);
        return layout;
    }

    /**
     * View for Michelson-Morley (Featuring Interactive Slider :>)
     */
    private VBox createMichelsonMorleyView()
    {
        VBox layout = new VBox(10);
        layout.setPadding(new Insets(15));

        Label title = new Label("Experiment 3: Michelson-Morley Interferometer");
        title.setStyle("-fx-font-size: 18px; -fx-font-weight: bold;");

        Label desc = new Label("Adjust the arm length difference to see Constructive vs Destructive interference.");

        // Slider Control
        Label sliderLabel = new Label("Arm Length Difference: 0 nm");
        Slider armSlider = new Slider(0, 600, 0); // 0 to 600nm
        armSlider.setShowTickLabels(true);
        armSlider.setShowTickMarks(true);
        armSlider.setMajorTickUnit(100);

        armSlider.valueProperty().addListener((obs, oldVal, newVal) ->
        {
            sliderLabel.setText(String.format("Arm Length Difference: %.1f nm", newVal.doubleValue()));
        });

        TextArea outputArea = createOutputArea();
        Button runBtn = new Button("Run Interferometer");

        runBtn.setOnAction(e ->
        {
            redirectOutput(outputArea);
            outputArea.clear();

            double dist = armSlider.getValue();
            System.out.println("Running with Arm Difference: " + String.format("%.2f", dist) + " nm");

            // Create experiment with slider value
            MichelsonMorleyInterferometer mm = new MichelsonMorleyInterferometer(dist);

            for (int i = 0; i < 1000; i++)
            {
                mm.runExperiment(new Photon("P" + i));
            }
            mm.printStats();

            // Educational hint
            if (Math.abs(dist - 275) < 20)
            {
                System.out.println("\n[Tip] Near 275nm (Lambda/2), you should see Destructive Interference!");
            }
        });

        layout.getChildren().addAll(title, desc, sliderLabel, armSlider, runBtn, outputArea);
        return layout;
    }

    // --- Helpers ---

    private TextArea createOutputArea()
    {
        TextArea area = new TextArea();
        area.setEditable(false);
        area.setPrefHeight(300);
        area.setStyle("-fx-font-family: 'Consolas', 'Monospace';");
        return area;
    }

    /**
     * Redirects System.out to the specified TextArea (So I don't have to refactor)
     */
    private void redirectOutput(TextArea targetArea)
    {
        System.setOut(new PrintStream(new OutputStream()
        {
            @Override
            public void write(int b)
            {
                javafx.application.Platform.runLater(() ->
                        targetArea.appendText(String.valueOf((char) b))
                );
            }
        }));
    }
}