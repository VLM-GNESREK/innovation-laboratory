package at.technikumwien.qds.core;

import at.technikumwien.qds.model.Photon;

public class Detector {

    private int detectionCount;
    private final String name;
    private final PathType path;

    public enum PathType {
        PathA,
        PathB
    }

    public Detector(String name, PathType path) {
        this.name = name;
        this.path = path;
    }

    public Detector(String name) {
        this(name, PathType.PathA);
    }

    public boolean detect(Photon photon) {
        boolean detected = (path == PathType.PathA)
                ? photon.detectInPathA()
                : photon.detectInPathB();

        if (detected) {
            detectionCount++;
            System.out.println(name + " detected in " + path);
        }

        return detected;
    }

    public void reset() {
        detectionCount = 0;
    }

    public void printStats() {
        System.out.println(name + ": " + detectionCount);
    }
}
