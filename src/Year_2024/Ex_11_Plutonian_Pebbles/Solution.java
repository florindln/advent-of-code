package Year_2024.Ex_11_Plutonian_Pebbles;

import java.io.File;
import java.io.FileNotFoundException;
import java.math.BigInteger;
import java.util.*;


public class Solution {
    public static void main(String[] args) throws FileNotFoundException {
        File input = new File("src/Year_2024/Ex_11_Plutonian_Pebbles/input.txt");
        Scanner myReader = new Scanner(input);

        var nums = new ArrayList<BigInteger>();
        while (myReader.hasNextLine()) {
            String line = myReader.nextLine();

            Arrays.stream(line.split(" ")).map(BigInteger::new).forEach(nums::add);
        }
        myReader.close();

//        System.out.println(nums);

        System.out.println("Result part 1: "+ getStonesSum(nums,25));
        System.out.println("Result part 2: "+getStonesSumDfs(nums,75));
    }

    private static BigInteger getStonesSumDfs(ArrayList<BigInteger> nums, int blinks) {
        var res=BigInteger.ZERO;
        for (var num:nums)
            res= res.add( stoneDfs(num,blinks,new HashMap<>()) );

        return res;
    }

    record Pair(BigInteger num,int blink){}
    private static BigInteger stoneDfs(BigInteger num, int blinks, Map<Pair,BigInteger> memo) {
        if(blinks==0)
            return BigInteger.ONE;

        var pair=new Pair(num,blinks);
        if(memo.containsKey(pair))
            return memo.get(pair);

        var newStones=blink(List.of(num));

        var res=BigInteger.ZERO;
        for (var stone:newStones)
            res=res.add( stoneDfs(stone,blinks-1,memo) );

        memo.put(pair,res);
        return res;
    }


    private static int getStonesSum(ArrayList<BigInteger> nums, int blinks) {
        var newNums=new ArrayList<BigInteger>(nums);

        for (int i = 0; i < blinks; i++) {
            newNums=blink(newNums);
//            System.out.println(newNums);
        }

        return newNums.size();
    }

    private static ArrayList<BigInteger> blink(List<BigInteger> nums) {
        var res= new ArrayList<BigInteger>();

        for (var num:nums){
            if(num.equals(BigInteger.ZERO)){
                res.add(BigInteger.ONE);
            } else if (num.toString().length()%2==0) {
                var leftSide=num.toString().substring(0,num.toString().length()/2);
                var rightSide=num.toString().substring(num.toString().length()/2);
                res.add(new BigInteger(leftSide));
                res.add(new BigInteger(rightSide));
            } else {
                res.add(num.multiply(BigInteger.valueOf(2024)));
            }
        }
        return res;
    }


}
