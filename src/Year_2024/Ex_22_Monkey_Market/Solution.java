package Year_2024.Ex_22_Monkey_Market;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

public class Solution {
    public static void main(String[] args) throws FileNotFoundException {
        File input = new File("src/Year_2024/Ex_22_Monkey_Market/input.txt");
        Scanner myReader = new Scanner(input);

        List<Long> secrets=new ArrayList<>();

        while (myReader.hasNextLine()) {
            String line = myReader.nextLine();
            secrets.add(Long.parseLong(line));
        }
        myReader.close();

//        for (int i = 1; i < 11; i++) {
//            System.out.println(calcSecretNum(123,i));
//        }

        System.out.println("Result part 1: "+ calcSecretNumSum(secrets));
        System.out.println("Result part 2: "+ getMaxScore(secrets));
    }

    static long getMaxScore(List<Long> secrets){
        var changesForAllPrices=getChangesForAllSecrets(secrets);

        //map for performance
        Map<Long,Map<List<Long>,Long>> secretToChangesToPriceMap=new HashMap<>();
        for (var secret:secrets){
            var changesToPriceMap = getChangesToPriceMap(secret, 2000);
            secretToChangesToPriceMap.put(secret,changesToPriceMap);
        }

//        var testCounter=1;
        var res=0L;
        for (var change:changesForAllPrices) {
//            if(testCounter++%1000==0){
//                System.out.println("progress: "+testCounter+"/"+changesForAllPrices.size());
//            }
            var localRes=0L;
            for (var secret : secrets) {
                var changesToPriceMap = secretToChangesToPriceMap.get(secret);
                var currPrice=changesToPriceMap.getOrDefault(change,0L);
                localRes+=currPrice;
            }
            res=Math.max(res,localRes);
        }
        return res;
    }
    private static Set<List<Long>> getChangesForAllSecrets(List<Long> secrets) {
        Set<List<Long>> res=new HashSet<>();
        for (var secret:secrets){
            var changesToPriceMap=getChangesToPriceMap(secret,2000);
            res.addAll(changesToPriceMap.keySet());
        }
        return res;
    }
    static Map<List<Long>,Long> getChangesToPriceMap(long secret,int iterations){
        Map<List<Long>,Long> changeToPriceMap=new HashMap<>();
        List<Long> changes=new ArrayList<>();
        var prevPrice=getPrice(secret);
        for (int i = 1; i <= iterations; i++) {
            secret=applyOneIteration(secret);
            var nextPrice=getPrice(secret);
            var change=getChange(prevPrice,nextPrice);
            changes.add(change);
            if(changes.size()==4){
                changeToPriceMap.putIfAbsent(new ArrayList<>(changes),nextPrice);
                changes.removeFirst();
            }
            prevPrice=nextPrice;
        }
        return changeToPriceMap;
    }

    private static long getChange(long price, long nextSecretPrice) {
        return nextSecretPrice-price;
    }
    static long getPrice(long secret){
        return secret%10;
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
