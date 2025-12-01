package at.technikumwien.qds;

import at.technikumwien.qds.core.BombTesterInterferometer;
import at.technikumwien.qds.model.Bomb;
import at.technikumwien.qds.model.QuantumObject;

//Demo für Elitzur-Vaidman Bomb Tester

public class BombTestSimulation {

    public static void main(String[] args) {
        System.out.println("=".repeat(60));
        System.out.println("ELITZUR-VAIDMAN QUANTUM BOMB TESTER");
        System.out.println("=".repeat(60));
        System.out.println("Can we detect if a bomb is live WITHOUT triggering it?");
        System.out.println("Classical physics: IMPOSSIBLE");
        System.out.println("Quantum mechanics: POSSIBLE!\n");

        BombTesterInterferometer tester = new BombTesterInterferometer();

        // Test 1: nur DUD-Bombe
        System.out.println("\n" + "=".repeat(60));
        System.out.println("TEST 1: Testing 10 DUD bombs");
        System.out.println("=".repeat(60));

        for (int i = 0; i < 10; i++) {
            Bomb dud = new Bomb("DUD-" + i, false);
            tester.runExperiment((QuantumObject) dud);
        }

        tester.printStats();
        tester.resetStats();

        // Test 2: Nur LIVE-Bomben
        System.out.println("\n\n" + "=".repeat(60));
        System.out.println("TEST 2: Testing 20 LIVE bombs");
        System.out.println("=".repeat(60));

        for (int i = 0; i < 20; i++) {
            Bomb live = new Bomb("LIVE-" + i, true);
            tester.runExperiment((QuantumObject) live);
        }

        tester.printStats();
        tester.resetStats();

        // Test 3: gemischte Bomben
        System.out.println("\n\n" + "=".repeat(60));
        System.out.println("TEST 3: Testing 100 mixed bombs (50% live, 50% dud)");
        System.out.println("=".repeat(60));

        for (int i = 0; i < 100; i++) {
            boolean isLive = Math.random() < 0.5;
            Bomb bomb = new Bomb("Bomb-" + i, isLive);
            tester.runExperiment((QuantumObject) bomb);
        }

        tester.printStats();

        // Statistik
        System.out.println("\n\n" + "=".repeat(60));
        System.out.println("THEORETICAL EXPECTATION:");
        System.out.println("=".repeat(60));
        System.out.println("For each LIVE bomb:");
        System.out.println("  - 50% chance: Explodes");
        System.out.println("  - 25% chance: Inconclusive (Detector C)");
        System.out.println("  - 25% chance: Identified safely (Detector D) ← THE MAGIC!");
        System.out.println("\nFor each DUD:");
        System.out.println("  - 100% chance: Identified as DUD");
        System.out.println("\nOverall: ~12.5% of all bombs can be identified as live");
        System.out.println("Without triggering them");
        System.out.println("=".repeat(60));
    }
}