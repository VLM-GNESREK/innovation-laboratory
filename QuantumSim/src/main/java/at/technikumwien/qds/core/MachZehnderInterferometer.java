package at.technikumwien.qds.core;

import at.technikumwien.qds.model.QuantumObject;

public class MachZehnderInterferometer implements Interferometer
{
    @Override
    public void runExperiment(QuantumObject input)
    {
        System.out.println("Running Mach-Zehnder simulation...");
    }
}
