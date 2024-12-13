package Year_2024.Ex_7_Bridge_Repair;

import java.io.File;
import java.io.FileNotFoundException;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;

enum Operator{
    ADD,
    MULTIPLY,
    CONCAT
}

public class Solution {
    public static void main(String[] args) throws FileNotFoundException {
        File input = new File("src/Year_2024/Ex_7_Bridge_Repair/input.txt");
        Scanner myReader = new Scanner(input);

        List<BigInteger> mathResults=new ArrayList<>();
        List<List<BigInteger>> nums=new ArrayList<>();

        while (myReader.hasNextLine()) {
            String line = myReader.nextLine();
            var split=line.split(":");
            mathResults.add(BigInteger.valueOf(Long.parseLong(split[0])));
            var numsStrings=split[1].trim().split(" ");
            nums.add(Arrays.stream(numsStrings).map(Long::parseLong).map(BigInteger::valueOf).toList());
        }
        myReader.close();

//        System.out.println(mathResults);
//        System.out.println(nums);

        System.out.println("Result part 1: "+ getCorrectMathResultsSum(mathResults,nums));
        System.out.println("Result part 2: "+getCorrectMathResultsSumWithConcat(mathResults,nums));
    }

    private static BigInteger getCorrectMathResultsSumWithConcat(List<BigInteger> mathResults, List<List<BigInteger>> nums) {
        BigInteger res=BigInteger.ZERO;
        for (int i = 0; i < mathResults.size(); i++) {
            if(canMakeResultWithConcat(mathResults.get(i),nums.get(i),1,nums.get(i).get(0))){
                var goodNum=mathResults.get(i);
                res=res.add(goodNum);
            }
        }
        return res;
    }

    private static boolean canMakeResultWithConcat(BigInteger mathResult, List<BigInteger> nums, int index, BigInteger res) {
        if (mathResult.equals(res) && index == nums.size())
            return true;

        if (index >= nums.size())
            return false;

        var curr=nums.get(index);
        for (var operator : Operator.values()) {
            switch (operator) {
                case ADD -> {
                    if (canMakeResultWithConcat(mathResult, nums, index + 1, res.add(curr)))
                        return true;
                }
                case MULTIPLY -> {
                    if (canMakeResultWithConcat(mathResult, nums, index + 1, res.multiply(curr)))
                        return true;
                }

                case CONCAT -> {
                    if (canMakeResultWithConcat(mathResult, nums, index + 1, new BigInteger(res.toString()+ curr.toString())))
                        return true;
                }
            }
        }

        return false;
    }

    private static BigInteger getCorrectMathResultsSum(List<BigInteger> mathResults, List<List<BigInteger>> nums) {
        BigInteger res=BigInteger.ZERO;
        for (int i = 0; i < mathResults.size(); i++) {
            if(canMakeResult(mathResults.get(i),nums.get(i),1,nums.get(i).get(0))){
                var goodNum=mathResults.get(i);
                res=res.add(goodNum);
            }
        }
        return res;
    }

    private static boolean canMakeResult(BigInteger mathResult, List<BigInteger> nums, int index, BigInteger res) {
        if (mathResult.equals(res) && index == nums.size())
            return true;

        if (index >= nums.size())
            return false;

        for (var operator : Operator.values()) {
            switch (operator) {
                case ADD -> {
                    if (canMakeResult(mathResult, nums, index + 1, res.add(nums.get(index))))
                        return true;
                }
                case MULTIPLY -> {
                    if (canMakeResult(mathResult, nums, index + 1, res.multiply(nums.get(index))))
                        return true;
                }
            }
        }

        return false;
    }
}
