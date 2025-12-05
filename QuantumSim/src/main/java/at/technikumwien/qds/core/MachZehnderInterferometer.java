package at.technikumwien.qds.core;

import at.technikumwien.qds.model.QuantumObject;
<<<<<<< HEAD
import at.technikumwien.qds.model.Photon;

public class MachZehnderInterferometer implements Interferometer
{
    private int detectStatA=0;
    private int detectStatB=0;
    private int TotalStat=0;
    private Detector detectorA;
    private Detector detectorB;

    public MachZehnderInterferometer(){
        this.detectorA=  new Detector("Detector A", Detector.PathType.PathA);
        this.detectorB = new Detector("Detector B", Detector.PathType.PathB);
    }

    @Override
    public void runExperiment(QuantumObject input)
    {
        if (input instanceof Photon photon) {
            System.out.println("Running Mach-Zehnder simulation...");

            photon.BeamSplitter();

            boolean detectedA = detectorA.detect(photon);
            boolean detectedB = detectorB.detect(photon);

            if (detectedA) detectStatA++;
            if (detectedB) detectStatB++;

            TotalStat++;

        }
    }

    public void printStats(){
        System.out.println("\nStatistic");
        System.out.println("Path A: " + detectStatA);
        System.out.println("Path B: " + detectStatB);
        System.out.println("Total: " + TotalStat);
        detectorA.printStats();
        detectorB.printStats();
    }

    public void resetStats() {
        detectStatA=0;
        detectStatB=0;
        TotalStat=0;
        detectorA.reset();
        detectorB.reset();
=======

public class MachZehnderInterferometer implements Interferometer
{
    @Override
    public void runExperiment(QuantumObject input)
    {
        System.out.println("Running Mach-Zehnder simulation...");
>>>>>>> 3a8f417fc65f55dba12156ae86749d49b32dc2a2
    }
}
