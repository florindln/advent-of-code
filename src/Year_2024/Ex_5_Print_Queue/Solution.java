package Year_2024.Ex_5_Print_Queue;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

public class Solution {
    public static void main(String[] args) throws FileNotFoundException {
        File input = new File("src/Year_2024/Ex_5_Print_Queue/input.txt");
        Scanner myReader = new Scanner(input);

        Map<Integer,List<Integer>> orderMap=new HashMap<>();
        List<List<Integer>> updates=new ArrayList<>();

        while (myReader.hasNextLine()) {
            String line = myReader.nextLine();
            if(line.contains("|")){
                var parts=line.split("\\|");
                var key=Integer.parseInt(parts[0]);
                if(!orderMap.containsKey(key)){
                    orderMap.put(key,new ArrayList<>());
                }
                orderMap.get(key).add(Integer.parseInt(parts[1]));
            }else{
                if (line.isEmpty())
                    continue;

                var parts=line.split(",");

                updates.add(Arrays.stream(parts).map(Integer::parseInt).toList());

            }
        }
        myReader.close();

//        System.out.println(orderMap);
//        System.out.println(updates);

        System.out.println("Result part 1: "+ getSum(orderMap,updates));
        System.out.println("Result part 2: "+getSumWithCorrections(orderMap,updates));
    }

    private static int getSumWithCorrections(Map<Integer, List<Integer>> orderMap, List<List<Integer>> updates) {
        var res = 0;
        for (var update : updates) {
            var fixedUpdate = new ArrayList<>(update);
            if(isUpdateCorrect(orderMap,update))
                continue;

            fixUpdate(orderMap,fixedUpdate);
//          get the middle
            var numToAdd = fixedUpdate.get(fixedUpdate.size() / 2);
            res += numToAdd;
        }
        return res;
    }

    private static void fixUpdate(Map<Integer, List<Integer>> orderMap, ArrayList<Integer> update) {
//        System.out.println("before update = " + update);
        if(isUpdateCorrect(orderMap,update))
            return;

        update.sort((a,b)->{
            if(orderMap.containsKey(a)){
                if(orderMap.get(a).contains(b))
                    return -1;
                else
                    return 0;
            }
            else
                return 0;
        });

//        System.out.println("after update = " + update);
//        if(!isUpdateCorrect(orderMap,update))
//            throw new RuntimeException("should be correct");
    }


    private static int getSum(Map<Integer, List<Integer>> orderMap, List<List<Integer>> updates) {
        var res=0;
        for (var update:updates){
            if(isUpdateCorrect(orderMap,update)){
//                get the middle
                var numToAdd=update.get(update.size()/2);
                res+=numToAdd;
            }
        }
        return res;
    }

    private static boolean isUpdateCorrect(Map<Integer, List<Integer>> orderMap, List<Integer> update) {
        for (int i = 0; i < update.size(); i++) {
            var curr = update.get(i);
            if (orderMap.containsKey(curr)) {
                var mustBeAfterList = orderMap.get(curr);
                for (var mustBeAfter : mustBeAfterList) {
                    if (update.contains(mustBeAfter) && i >= update.indexOf(mustBeAfter))
                        return false;
                }
            }
        }
        return true;
    }

}
