package at.technikumwien.qds.model;

public class Photon implements QuantumObject {

    //probabilityPathA/B entfernt, im code nicht verwendet
    private final String id;
    private String actualPath = "A";

    public Photon(String id) {
        this.id = id;
        System.out.println(id + " created.");
    }

    public void BeamSplitter() {
        actualPath = Math.random() < 0.5 ? "A" : "B";
    }

    public boolean detectInPathA() {
        return "A".equals(actualPath);
    }

    public boolean detectInPathB() {
        return "B".equals(actualPath);
    }
}
