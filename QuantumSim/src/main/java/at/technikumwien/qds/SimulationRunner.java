package at.technikumwien.qds;

import at.technikumwien.qds.core.Interferometer;
import at.technikumwien.qds.core.MachZehnderInterferometer;
import at.technikumwien.qds.model.Photon;

import java.util.concurrent.Executors;

public class SimulationRunner
{
    public static void main(String[] args)
    {
<<<<<<< HEAD

        System.out.println("Starting Quantum Discovery Simulator...");

        MachZehnderInterferometer setup = new MachZehnderInterferometer();

=======
        System.out.println("Starting Quantum Discovery Simulator...");
>>>>>>> 3a8f417fc65f55dba12156ae86749d49b32dc2a2
        try(var executor = Executors.newVirtualThreadPerTaskExecutor())
        {
            int experimentsToRun = 100;
            for(int i = 0; i < experimentsToRun; i++)
            {
                final int experimentId = i;

                executor.submit(() -> {
<<<<<<< HEAD
                    //Interferometer setup = new MachZehnderInterferometer(); <-nach ober verschoben
=======
                    Interferometer setup = new MachZehnderInterferometer();
>>>>>>> 3a8f417fc65f55dba12156ae86749d49b32dc2a2
                    Photon photon = new Photon("Photon-" + experimentId);

                    setup.runExperiment(photon);
                });
            }
        }

        System.out.println("All simulations complete.");
<<<<<<< HEAD

        setup.printStats();
=======
>>>>>>> 3a8f417fc65f55dba12156ae86749d49b32dc2a2
    }
}
