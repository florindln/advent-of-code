package Year_2024.Ex_22_Monkey_Market;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

public class Solution {
    public static void main(String[] args) throws FileNotFoundException {
        File input = new File("src/Year_2024/Ex_22_Monkey_Market/input.txt");
        Scanner myReader = new Scanner(input);

        List<Long> nums=new ArrayList<>();

        while (myReader.hasNextLine()) {
            String line = myReader.nextLine();
            nums.add(Long.parseLong(line));
        }
        myReader.close();

//        for (int i = 1; i < 11; i++) {
//            System.out.println(calcSecretNum(123,i));
//        }

        System.out.println("Result part 1: "+ calcSecretNumSum(nums));

    }

    private static long calcSecretNumSum(List<Long> nums) {
        var res=0L;
        for (var num:nums){
            var afterIterations=calcSecretNum(num,2000);
            res+=afterIterations;
        }
        return res;
    }

    private static long calcSecretNum(long num, int iterations) {
        var res=num;
        for (int i = 0; i < iterations; i++) {
            var afterIteration=applyOneIteration(res);
            res=afterIteration;
        }
        return res;
    }

    private static long applyOneIteration(long secret) {
        var res=secret*64;
        secret=mix(res,secret);
        secret=prune(secret);

        res=secret/32;
        secret=mix(res,secret);
        secret=prune(secret);

        res=secret*2048;
        secret=mix(res,secret);
        secret=prune(secret);

        return secret;
    }

    private static long prune(long num) {
        return num%16777216;
    }

    private static long mix(long val, long prevSecret) {
        return val^prevSecret;
    }


}
