package at.technikumwien.qds.core;

import at.technikumwien.qds.model.Photon;

public class Detector {

    private int detectionCount=0;   //misst ob photon ankommt und zaehlt mit
    private String name;
    private PathType Path;

    public enum PathType {
        PathA,
        PathB
    }

    public Detector(String name, PathType path){
        this.name=name;
        this.Path=path;
    }

    public Detector(String name){
        this(name, PathType.PathA);
    }

    public boolean detect(Photon photon){
        boolean detected;
        if (Path==PathType.PathA) {
            detected = photon.detectInPathA();
        } else {
            detected = photon.detectInPathB();

        }

        if(detected){
            detectionCount++;
            System.out.println(name + " detected in " + Path);
        }

        return detected;
    }

    public int getDetectionCount(){
        return detectionCount;
    }

    public String getName(){
        return name;
    }

    public void reset() {
        detectionCount = 0;
    }

    public PathType Path(){
        return Path;
    }

    public void printStats(){
        System.out.println( name + ": " + detectionCount);
    }

}
