package Year_2024.Ex_17_Chronospatial_Computer;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;
import java.util.stream.Collectors;

//record ChronoCalc(int A,int B,int C,List<Integer> instructions){}
class ChronoComputer {
    public int A;
    public int B;
    public int C;
    public List<Integer> instructions;
    public ChronoComputer(int A, int B, int C, List<Integer> instructions) {
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
                    A = (int) result;
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
                    output.add(comboOperand % 8);
                }
                case 6 -> {
                    var numerator = A;
                    var denominator = Math.pow(2, comboOperand);
                    var result = numerator / denominator;
                    B = (int) result;
                }
                case 7 -> {
                    var numerator = A;
                    var denominator = Math.pow(2, comboOperand);
                    var result = numerator / denominator;
                    C = (int) result;
                }
            }
        }
        return output;
    }
    int getLiteralOperand(int operand){
        return operand;
    }
    int getComboOperand(int operand){
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


        int A=0;
        int B=0;
        int C=0;
        var instructions=new ArrayList<Integer>();
        while (myReader.hasNextLine()) {
            String line = myReader.nextLine();
            if (line.isEmpty())
                continue;
            var split = line.split(":");
            var beforeTwoDots = split[split.length - 2];
            if (beforeTwoDots.contains("A"))
                A = Integer.parseInt(split[split.length - 1].trim());
            else if (beforeTwoDots.contains("B"))
                B = Integer.parseInt(split[split.length - 1].trim());
            else if (beforeTwoDots.contains("C"))
                C = Integer.parseInt(split[split.length - 1].trim());
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

//        System.out.println(chronoCalc);

        var res1=chronoComputer.applyInstructions();

        System.out.println("Result part 1: "+ res1.stream().map(String::valueOf).collect(Collectors.joining(",")));

//        System.out.println("Result part 2: "+(getVisitedCells(res2Visited)+1));

    }

  }
