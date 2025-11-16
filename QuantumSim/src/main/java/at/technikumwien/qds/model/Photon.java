package at.technikumwien.qds.model;

public class Photon implements QuantumObject
{
    private String id;

    public Photon(String id)
    {
        this.id = id;
        System.out.println("Photon " + id + "created.");
    }
}
