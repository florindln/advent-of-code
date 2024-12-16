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

public class Solution {
    public static void main(String[] args) throws FileNotFoundException {
        File input = new File("src/Year_2024/Ex_13_Claw_Contraption/input.txt");
        Scanner myReader = new Scanner(input);

        List<ClawMachine> clawMachines = new ArrayList<>();
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
            if (matcherPrize.find()) {
                int x = Integer.parseInt(matcherPrize.group(1));
                int y = Integer.parseInt(matcherPrize.group(2));
                prize = new Coords(x, y);
            }
            clawMachines.add(new ClawMachine(buttonA,buttonB,prize,new Coords(0,0)));
        }

        myReader.close();

//        System.out.println(clawMachines);

        System.out.println("Result part 1: "+ getAllClawMachinesResult(clawMachines));
//        System.out.println("Result part 2: "+ second);
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
