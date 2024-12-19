package Year_2024.Ex_17_Chronospatial_Computer;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;
import java.util.stream.Collectors;

//record ChronoCalc(int A,int B,int C,List<Integer> instructions){}
class ChronoComputer {
    public long A;
    public long B;
    public long C;
    public List<Integer> instructions;
    public ChronoComputer(long A, long B, long C, List<Integer> instructions) {
        this.A = A;
        this.B = B;
        this.C = C;
        this.instructions = instructions;
    }
    List<Integer> applyInstructions() {
        var output = new ArrayList<Integer>();
        for (int i = 0; i < instructions.size(); i += 2) {
            var opCode = instructions.get(i);
            var operand = instructions.get(i + 1);
            var comboOperand = getComboOperand(operand);
            var literalOperand = getLiteralOperand(operand);
            switch (opCode) {
                case 0 -> {
                    var numerator = A;
                    var denominator = Math.pow(2, comboOperand);
                    var result = numerator / denominator;
                    A = (long) result;
                }
                case 1 -> {
                    B = B ^ literalOperand;
                }
                case 2 -> {
                    B = comboOperand % 8;
                }
                case 3 -> {
                    if (A == 0)
                        continue;
                    i = literalOperand;
                    i -= 2;
                }
                case 4 -> {
                    B = B ^ C;
                }
                case 5 -> {
                    output.add((int)(comboOperand % 8));
                }
                case 6 -> {
                    var numerator = A;
                    var denominator = Math.pow(2, comboOperand);
                    var result = numerator / denominator;
                    B = (long) result;
                }
                case 7 -> {
                    var numerator = A;
                    var denominator = Math.pow(2, comboOperand);
                    var result = numerator / denominator;
                    C = (long) result;
                }
            }
        }
        return output;
    }
    int getLiteralOperand(int operand){
        return operand;
    }
    long getComboOperand(int operand){
       return switch (operand){
           case 0,1,2,3->operand;
           case 4->A;
           case 5->B;
           case 6->C;
           default -> throw new RuntimeException("Unsupported combo operand");
       };
    }

}

public class Solution {
    public static void main(String[] args) throws FileNotFoundException {
        File input = new File("src/Year_2024/Ex_17_Chronospatial_Computer/input.txt");
        Scanner myReader = new Scanner(input);


        long A=0L;
        long B=0L;
        long C=0L;
        var instructions=new ArrayList<Integer>();
        while (myReader.hasNextLine()) {
            String line = myReader.nextLine();
            if (line.isEmpty())
                continue;
            var split = line.split(":");
            var beforeTwoDots = split[split.length - 2];
            if (beforeTwoDots.contains("A"))
                A = Long.parseLong(split[split.length - 1].trim());
            else if (beforeTwoDots.contains("B"))
                B = Long.parseLong(split[split.length - 1].trim());
            else if (beforeTwoDots.contains("C"))
                C = Long.parseLong(split[split.length - 1].trim());
            else {
                var nums = split[split.length - 1].trim();
                var numsArr = nums.split(",");
                for (var num : numsArr) {
                    instructions.add(Integer.parseInt(num));
                }
            }
        }
        myReader.close();

        var chronoComputer=new ChronoComputer(A,B,C,instructions);

        var res1=chronoComputer.applyInstructions();
        System.out.println("Result part 1: "+ res1.stream().map(String::valueOf).collect(Collectors.joining(",")));

//        set the current value to 0. Then for i in {0, 1, 2, 3, 4, 5, 6, 7} set the current value (register A) to `current + i`. If the first output of the program equals the last instruction of the program, then multiply the current value by 8. Now again, for each i in {0, 1, 2, 3, 4, 5, 6, 7} set the current value (register A) to `current + i`. If the first output of the program equals the second-last instruction, then multiply the current value by 8 and continue in a similar fashion, until you found the solution.
        var validA=new ArrayList<Long>();
        Queue<Long> possibleA=new LinkedList<>();
        possibleA.add(0L);
        var foundA=false;

        while (!possibleA.isEmpty()&&!foundA) {
            var currentPossibleA = possibleA.poll();
            for (int i = 0; i < 8; i++) {
                var newA = currentPossibleA + i;

                var chronoComputer2 = new ChronoComputer(newA, B, C, instructions);
                var res2 = chronoComputer2.applyInstructions();

                if (res2.equals(instructions)){
                    validA.add(newA);
                }

                if(res2.size()>instructions.size())
                    break;

                var matchFound=true;
                for (int res2Index = 0; res2Index < res2.size() ; res2Index++) {
                    if(res2.get(res2.size()-1-res2Index)!=instructions.get(instructions.size()-1-res2Index)){
                        matchFound=false;
                        break;
                    }
                }

                if (matchFound) {
                    possibleA.add(newA*8);
                }
            }
        }

//        System.out.println(validA);
        System.out.println("Result pairt 2: "+Collections.min(validA));
    }
  }
