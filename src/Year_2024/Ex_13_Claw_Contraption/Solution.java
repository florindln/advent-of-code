package Year_2024.Ex_13_Claw_Contraption;

import java.io.File;
import java.io.FileNotFoundException;
import java.math.BigInteger;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

//record ClawMachine(Coords buttonA,Coords buttonB, Coords prize, Coords current){}
class Coords {
    public int x;
    public int y;
    public Coords(int x, int y) {
        this.x = x;
        this.y = y;
    }
    @Override
    public String toString() {
        return "("+x+','+y+")";
    }
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Coords coords = (Coords) obj;
        return x == coords.x && y == coords.y;
    }

    @Override
    public int hashCode() {
        return 31 * x + y;
    }
}

record BigCoords(long x,long y){}

class ClawMachine {
    public Coords buttonA;
    public Coords buttonB;
    public Coords prize;
    public Coords current;

    public ClawMachine(Coords buttonA, Coords buttonB, Coords prize, Coords current) {
        this.buttonA = buttonA;
        this.buttonB = buttonB;
        this.prize = prize;
        this.current = current;
    }

    @Override
    public String toString() {
        return '\n' + "ClawMachine{" + "buttonA=" + buttonA + ", buttonB=" + buttonB + ", prize=" + prize + ", current=" + current + '}';
    }

    static int reachEndingUsingFewestTokens(Coords buttonA, Coords buttonB, Coords current, Coords prize, int buttonAUsages, int buttonBUsages, Map<Coords,Integer> memo) {
        if (current.x == prize.x && current.y == prize.y)
            return 0;
        if (current.x > prize.x || current.y > prize.y)
            return Integer.MAX_VALUE;

        if(memo.containsKey(current))
            return memo.get(current);

        var useButtonAVal = reachEndingUsingFewestTokens(buttonA, buttonB, new Coords(current.x + buttonA.x, current.y + buttonA.y), prize, buttonAUsages + 1, buttonBUsages,memo);
        var useButtonBVal = reachEndingUsingFewestTokens(buttonA, buttonB, new Coords(current.x + buttonB.x, current.y + buttonB.y), prize, buttonAUsages, buttonBUsages + 1,memo);

        var res=Integer.MAX_VALUE;
        if (useButtonAVal == Integer.MAX_VALUE && useButtonBVal == Integer.MAX_VALUE)
            res= Integer.MAX_VALUE;
        else if (useButtonAVal == Integer.MAX_VALUE)
            res= 1 + useButtonBVal;
        else if (useButtonBVal == Integer.MAX_VALUE)
            res= 3 + useButtonAVal;
        else
            res= Math.min(3 + useButtonAVal, 1 + useButtonBVal);

        memo.put(current,res);
        return res;
    }
}

record Triple<A,B,C>(A buttonA,B buttonB,C prize){}

public class Solution {
    public static void main(String[] args) throws FileNotFoundException {
        File input = new File("src/Year_2024/Ex_13_Claw_Contraption/input.txt");
        Scanner myReader = new Scanner(input);

        List<ClawMachine> clawMachines = new ArrayList<>();

        List<Triple<Coords,Coords,BigCoords>> clawMachines2 = new ArrayList<>();
        while (myReader.hasNextLine()) {
            String lineButtonA = myReader.nextLine();
            if (lineButtonA.isEmpty())
                continue;
            String lineButtonB = myReader.nextLine();
            String linePrize = myReader.nextLine();


            Pattern patternButtonA = Pattern.compile("Button A: X([+-]?\\d+), Y([+-]?\\d+)");
            Pattern patternButtonB = Pattern.compile("Button B: X([+-]?\\d+), Y([+-]?\\d+)");
            Pattern patternPrize = Pattern.compile("Prize: X=(\\d+), Y=(\\d+)");

            Matcher matcherButtonA = patternButtonA.matcher(lineButtonA);
            Matcher matcherButtonB = patternButtonB.matcher(lineButtonB);
            Matcher matcherPrize = patternPrize.matcher(linePrize);

            Coords buttonA = null;
            if (matcherButtonA.find()) {
                int x = Integer.parseInt(matcherButtonA.group(1));
                int y = Integer.parseInt(matcherButtonA.group(2));
                buttonA = new Coords(x, y);
            }
            Coords buttonB = null;
            if (matcherButtonB.find()) {
                int x = Integer.parseInt(matcherButtonB.group(1));
                int y = Integer.parseInt(matcherButtonB.group(2));
                buttonB = new Coords(x, y);
            }
            Coords prize = null;
            BigCoords prize2 = null;
            if (matcherPrize.find()) {
                int x = Integer.parseInt(matcherPrize.group(1));
                int y = Integer.parseInt(matcherPrize.group(2));
                prize = new Coords(x, y);
                prize2 = new BigCoords(
                        10000000000000L
                                +x,
                        10000000000000L
                                +y);
            }
            clawMachines.add(new ClawMachine(buttonA,buttonB,prize,new Coords(0,0)));
            clawMachines2.add(new Triple<>(buttonA,buttonB,prize2));
        }

        myReader.close();

//        System.out.println(clawMachines);

        System.out.println("Result part 1: "+ getAllClawMachinesResult(clawMachines));
        System.out.println("Result part 2: "+ getClawmachinesResultUsingMath(clawMachines2));
    }

    private static double getClawmachinesResultUsingMath(List<Triple<Coords, Coords, BigCoords>> clawMachines) {
        double res=0;
        for(var clawMachine: clawMachines){
            var sol=solveClawMachineMath(clawMachine);
            res+=sol;
        }
        return res;
    }

    static private double solveClawMachineMath(Triple<Coords, Coords, BigCoords> clawMachine) {
        //coefficients of the equations for example 1
        //equation 1: 94 * A + 22 * B = 8400
        //equation 2: 34 * A + 67 * B = 5400
        // A = (8400 - 22 * B) / 94
        // this gives us everything in terms of B
        // 34 * ((8400 - 22 * B) / 94) + 67 * B = 5400
        var buttonA=clawMachine.buttonA();
        var buttonB=clawMachine.buttonB();
        var prize=clawMachine.prize();

        //generalized
        //equation 1: A.x * aPresses + B.x * bPresses = C.x
        //equation 2: A.y * aPresses + B.y * bPresses = C.y
        // aPresses = (C.x - B.x * bPresses) / A.x
        // this gives us everything in terms of bPresses
        // A.y * ((C.x - B.x * bPresses) / A.x) + B.y * bPresses = C.y
        //

        var bPresses=solveB(clawMachine);
        var aPresses = (prize.x() - (long) buttonB.x * bPresses) / buttonA.x;

//        if the result is not an integer, then the answer is not valid
        if(!isWithinPointOneOfNearestLong(aPresses)  || !isWithinPointOneOfNearestLong(bPresses) )
            return 0;

        return 3*aPresses+bPresses;
    }

    public static boolean isWithinPointOneOfNearestLong(double value) {
        long nearestLong = Math.round(value);
        return Math.abs(value - nearestLong) <= 0.01;
    }

    private static double solveB(Triple<Coords, Coords, BigCoords> clawMachine) {
        var buttonA = clawMachine.buttonA();
        var buttonB = clawMachine.buttonB();
        var prize = clawMachine.prize();

        // Extract coefficients
        double aX = buttonA.x;
        double aY = buttonA.y;
        double bX = buttonB.x;
        double bY = buttonB.y;
        double cX = prize.x();
        double cY = prize.y();

        // Generalized substitution for bPresses:
        // A.y * ((C.x - B.x * bPresses) / A.x) + B.y * bPresses = C.y
        // Simplify:
        // (A.y / A.x) * (C.x - B.x * bPresses) + B.y * bPresses = C.y
        // Let k1 = A.y / A.x, k2 = B.x, k3 = B.y, k4 = C.x, and k5 = C.y
        double k1 = aY / aX;
        double k2 = bX;
        double k3 = bY;
        double k4 = cX;
        double k5 = cY;

        // Solve for bPresses:
        // bPresses * (-k1 * k2 + k3) = k5 - k1 * k4
        return (k5 - k1 * k4) / (-k1 * k2 + k3);
    }

    private static int getAllClawMachinesResult(List<ClawMachine> clawMachines) {
        var res=0;

        for (var clawMachine:clawMachines){
            var bestScore=ClawMachine.reachEndingUsingFewestTokens(clawMachine.buttonA, clawMachine.buttonB,clawMachine.current,clawMachine.prize,0,0,new LinkedHashMap<>());
            if(bestScore==Integer.MAX_VALUE)
                continue;
            res+=bestScore;
        }
        return res;
    }
}
