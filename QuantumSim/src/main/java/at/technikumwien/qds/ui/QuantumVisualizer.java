package at.technikumwien.qds.ui;

import javafx.animation.AnimationTimer;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;

public class QuantumVisualizer extends Canvas
{

    private final GraphicsContext gc;
    private long startTime = 0;
    private boolean animating = false;

    // Experiment State
    private String currentExperiment = "MZ";
    private String lastResultPath = "A";
    private boolean bombExploded = false;
    private boolean bombLive = false;

    // Layout Coordinates
    private final double PADDING = 50;
    private final double BOX_SIZE = 300;
    private final double SOURCE_X = 50;
    private final double SOURCE_Y = 350; // Bottom Left
    private final double BS1_X = 150;
    private final double BS1_Y = 350;
    private final double MIRROR_TOP_X = 150;
    private final double MIRROR_TOP_Y = 50;
    private final double MIRROR_BOT_X = 450;
    private final double MIRROR_BOT_Y = 350; // This is where the Bomb sits
    private final double BS2_X = 450;
    private final double BS2_Y = 50;

    private final double DET_A_X = 450; // Top exit
    private final double DET_A_Y = -30;
    private final double DET_B_X = 530; // Right exit
    private final double DET_B_Y = 50;

    public QuantumVisualizer(double width, double height)
    {
        super(width, height);
        this.gc = getGraphicsContext2D();
        drawSchematic();
    }

    /**
     * Resets and starts the animation for a specific run.
     */
    public void playAnimation(String experimentType, String resultPath, boolean exploded, boolean isLiveBomb)
    {
        this.currentExperiment = experimentType;
        this.lastResultPath = resultPath;
        this.bombExploded = exploded;
        this.bombLive = isLiveBomb;
        this.animating = true;
        this.startTime = System.nanoTime();

        AnimationTimer timer = new AnimationTimer()
        {
            @Override
            public void handle(long now)
            {
                if (!animating)
                {
                    this.stop();
                    return;
                }
                updateAnimation(now);
            }
        };
        timer.start();
    }

    private void updateAnimation(long now)
    {
        double elapsedSeconds = (now - startTime) / 1_000_000_000.0;
        double speed = 200; // pixels per second
        double dist = elapsedSeconds * speed;

        drawSchematic();

        // Sorce to BeamSplitter
        if (dist < 100)
        {
            drawPhoton(SOURCE_X + dist, SOURCE_Y, 1.0);
        }
        // SuperPosition
        else if (dist < 100 + BOX_SIZE)
        {
            double localDist = dist - 100;

            drawPhoton(BS1_X, BS1_Y - localDist, 0.5); // Moving Up

            // Bottom Path
            drawPhoton(BS1_X + localDist, BS1_Y, 0.5); // Moving Right towards Bomb/Mirror

            if (currentExperiment.equals("BOMB") && bombExploded && localDist >= (MIRROR_BOT_X - BS1_X))
            {
                // Boom!
                drawExplosion(MIRROR_BOT_X, MIRROR_BOT_Y);
                animating = false; // Stop animation
            }
        }
        // Recombination
        else if (dist < 100 + BOX_SIZE * 2)
        {
            double localDist = dist - (100 + BOX_SIZE);

            if (!bombExploded)
            {
                // Top Mirror -> BS2
                drawPhoton(MIRROR_TOP_X + localDist, MIRROR_TOP_Y, 0.5);

                // Bottom Mirror/Bomb -> BS2
                drawPhoton(MIRROR_BOT_X, MIRROR_BOT_Y - localDist, 0.5);
            }
        }
        // Final Output
        else if (dist < 100 + BOX_SIZE * 2 + 100)
        {
            double localDist = dist - (100 + BOX_SIZE * 2);

            if (!bombExploded)
            {
                if (lastResultPath.equals("A"))  // collapsed
                {
                    drawPhoton(BS2_X, BS2_Y - localDist, 1.0); // Up to Det A
                }
                else
                {
                    drawPhoton(BS2_X + localDist, BS2_Y, 1.0); // Right to Det B
                }
            }
        }
        else
        {
            animating = false; // End
        }
    }

    private void drawPhoton(double x, double y, double opacity)
    {
        gc.setFill(Color.YELLOW.deriveColor(0, 1, 1, opacity));
        gc.fillOval(x - 5, y - 5, 10, 10);
        // Glow effect
        gc.setStroke(Color.ORANGE.deriveColor(0, 1, 1, opacity * 0.5));
        gc.strokeOval(x - 8, y - 8, 16, 16);
    }

    private void drawExplosion(double x, double y)
    {
        gc.setFill(Color.RED);
        gc.fillOval(x - 20, y - 20, 40, 40);
        gc.setStroke(Color.YELLOW);
        gc.setLineWidth(3);
        gc.strokeLine(x-30, y-30, x+30, y+30);
        gc.strokeLine(x+30, y-30, x-30, y+30);
        gc.setFill(Color.WHITE);
        gc.fillText("BOOM!", x - 15, y - 25);
    }

    private void drawSchematic()
    {
        gc.clearRect(0, 0, getWidth(), getHeight());

        gc.setStroke(Color.LIGHTGRAY);
        gc.setLineWidth(2);

        // Grid
        gc.strokeLine(SOURCE_X, SOURCE_Y, MIRROR_BOT_X, MIRROR_BOT_Y); // Bottom horiz
        gc.strokeLine(BS1_X, BS1_Y, MIRROR_TOP_X, MIRROR_TOP_Y);       // Left vert
        gc.strokeLine(MIRROR_TOP_X, MIRROR_TOP_Y, BS2_X, BS2_Y);       // Top horiz
        gc.strokeLine(MIRROR_BOT_X, MIRROR_BOT_Y, BS2_X, BS2_Y);       // Right vert

        // Output lines
        gc.strokeLine(BS2_X, BS2_Y, BS2_X, BS2_Y - 50); // To Det A
        gc.strokeLine(BS2_X, BS2_Y, BS2_X + 50, BS2_Y); // To Det B

        // Components
        drawBlock(BS1_X, BS1_Y, Color.CYAN, "BS1"); // Beam Splitter 1
        drawBlock(BS2_X, BS2_Y, Color.CYAN, "BS2"); // Beam Splitter 2
        drawBlock(MIRROR_TOP_X, MIRROR_TOP_Y, Color.SILVER, "Mirror");

        // Bottom Right Element (Mirror or Bomb)
        if (currentExperiment.equals("BOMB"))
        {
            if (bombLive)
            {
                drawBlock(MIRROR_BOT_X, MIRROR_BOT_Y, Color.BLACK, "BOMB");
            }
            else
            {
                drawBlock(MIRROR_BOT_X, MIRROR_BOT_Y, Color.LIGHTGREY, "DUD");
            }
        }
        else
        {
            drawBlock(MIRROR_BOT_X, MIRROR_BOT_Y, Color.SILVER, "Mirror");
        }

        // Detectors
        drawDetector(BS2_X, BS2_Y - 60, "Det A (Safe)");
        drawDetector(BS2_X + 60, BS2_Y, "Det B (Dark)");

        // Source
        gc.setFill(Color.LIME);
        gc.fillOval(SOURCE_X - 10, SOURCE_Y - 10, 20, 20);
        gc.setFill(Color.WHITE);
        gc.fillText("Source", SOURCE_X - 20, SOURCE_Y + 30);
    }

    private void drawBlock(double x, double y, Color color, String label)
    {
        gc.setFill(color);
        gc.fillRect(x - 15, y - 15, 30, 30);
        gc.setStroke(Color.WHITE);
        gc.strokeRect(x - 15, y - 15, 30, 30);
        gc.setFill(Color.WHITE);
        gc.setFont(new Font("Arial", 10));
        gc.fillText(label, x - 10, y + 25);
    }

    private void drawDetector(double x, double y, String label)
    {
        gc.setFill(Color.DARKORANGE);
        gc.fillArc(x - 15, y - 15, 30, 30, 0, 180, javafx.scene.shape.ArcType.ROUND);
        gc.fillText(label, x - 20, y - 20);
    }
}