package at.technikumwien.qds.model;

public class Photon implements QuantumObject
{
    private String id;
<<<<<<< HEAD
    private double probabilityPathA;
    private double probabilityPathB;
    private String actualPath;
=======
>>>>>>> 3a8f417fc65f55dba12156ae86749d49b32dc2a2

    public Photon(String id)
    {
        this.id = id;
<<<<<<< HEAD
        this.probabilityPathA = 1.0; //licht wird gestrahlt, startet auf path A
        this.probabilityPathB= 0.0;
        this.actualPath="A";
        System.out.println( id + " created.");
    }

    public void BeamSplitter(){
        this.probabilityPathA = 0.5; //nach dem splitter wird es aufgeteilt
        this.probabilityPathB = 0.5;

        if  (Math.random() < 0.5){ //photon entscheidet sich fuer einen Path
            actualPath="A";
        } else {
            actualPath="B";
        }

    }

    public boolean detectInPathA(){
        return actualPath.equals("A");
    }
    public boolean detectInPathB(){
        return actualPath.equals("B");
    }

    public double getProbPathA(){ return probabilityPathA; }
    public double getProbPathB(){ return probabilityPathB; }


=======
        System.out.println("Photon " + id + "created.");
    }
>>>>>>> 3a8f417fc65f55dba12156ae86749d49b32dc2a2
}
