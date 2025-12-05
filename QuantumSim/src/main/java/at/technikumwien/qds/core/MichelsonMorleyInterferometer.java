package at.technikumwien.qds.core;

import at.technikumwien.qds.model.QuantumObject;
import at.technikumwien.qds.model.Photon;

public class MichelsonMorleyInterferometer implements Interferometer
{
    private int detectStatConstructive = 0;  // konstruktive Interferenz
    private int detectStatDestructive = 0;   // destruktive Interferenz
    private int totalStat = 0;
    private Detector mainDetector;
    private double armLengthDifference;  // Gangunterschied der Arme in nm

    public MichelsonMorleyInterferometer() {
        this(0.0);
    }

    public MichelsonMorleyInterferometer(double armLengthDifference) {
        this.mainDetector = new Detector("Main Detector", Detector.PathType.PathA);
        this.armLengthDifference = armLengthDifference;
    }

    @Override
    public void runExperiment(QuantumObject input) {
        if (input instanceof Photon photon) {
            System.out.println("Running Michelson-Morley simulation...");

            // 1. Beam Splitter teilt das Photon
            photon.BeamSplitter();

            // 2. Berechne Phasenverschiebung durch Armlängenunterschied
            // Wellenlänge von sichtbarem Licht ~550 nm
            double wavelength = 550.0;  // nm
            double phaseDifference = (2 * Math.PI * armLengthDifference) / wavelength;

            // 3. Photonen werden an Spiegeln reflektiert und treffen wieder aufeinander
            // 4. Interferenz am zweiten Beam Splitter
            boolean constructiveInterference = isConstructiveInterference(phaseDifference);

            if (constructiveInterference) {
                // Konstruktive Interferenz -> Photon wird detektiert
                detectStatConstructive++;
                System.out.println("Constructive interference - photon detected");
            } else {
                // Destruktive Interferenz -> Photon wird nicht detektiert (ausgelöscht)
                detectStatDestructive++;
                System.out.println("Destructive interference - no detection");
            }

            totalStat++;
        }
    }


     // Bestimmt ob konstruktive oder destruktive Interferenz auftritt
     //Konstruktiv: Phasendifferenz ist Vielfaches von 2π
     //Destruktiv: Phasendifferenz ist ungerades Vielfaches von π

    private boolean isConstructiveInterference(double phaseDifference) {
        // Normalisiere Phase auf [0, 2π]
        double normalizedPhase = phaseDifference % (2 * Math.PI);
        if (normalizedPhase < 0) normalizedPhase += 2 * Math.PI;

        // Wahrscheinlichkeit für konstruktive Interferenz basierend auf cos²(φ/2)
        double probability = Math.pow(Math.cos(normalizedPhase / 2), 2);

        return Math.random() < probability;
    }

    public void setArmLengthDifference(double difference) {
        this.armLengthDifference = difference;
    }

    public double getArmLengthDifference() {
        return armLengthDifference;
    }

    public void printStats() {
        System.out.println("\n=== Michelson-Morley Statistics ===");
        System.out.println("Arm length difference: " + armLengthDifference + " nm");
        System.out.println("Constructive interference: " + detectStatConstructive);
        System.out.println("Destructive interference: " + detectStatDestructive);
        System.out.println("Total: " + totalStat);

        if (totalStat > 0) {
            double visibility = (double)(detectStatConstructive - detectStatDestructive) / totalStat;
            System.out.println("Visibility: " + String.format("%.2f", visibility));
        }
    }

    public void resetStats() {
        detectStatConstructive = 0;
        detectStatDestructive = 0;
        totalStat = 0;
        mainDetector.reset();
    }
    }