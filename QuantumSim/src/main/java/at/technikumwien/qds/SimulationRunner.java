package at.technikumwien.qds;

import at.technikumwien.qds.core.Interferometer;
import at.technikumwien.qds.core.MachZehnderInterferometer;
import at.technikumwien.qds.model.Photon;

import java.util.concurrent.Executors;

public class SimulationRunner
{
    public static void main(String[] args)
    {
        System.out.println("Starting Quantum Discovery Simulator...");
        try(var executor = Executors.newVirtualThreadPerTaskExecutor())
        {
            int experimentsToRun = 100;
            for(int i = 0; i < experimentsToRun; i++)
            {
                final int experimentId = i;

                executor.submit(() -> {
                    Interferometer setup = new MachZehnderInterferometer();
                    Photon photon = new Photon("Photon-" + experimentId);

                    setup.runExperiment(photon);
                });
            }
        }

        System.out.println("All simulations complete.");
    }
}
