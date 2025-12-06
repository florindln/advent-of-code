package Year_2025.Ex_5_Cafeteria;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

public class Solution {
    record IdRange(Long start, Long end) {}
    record InputData(List<IdRange> freshRanges, List<Long> availableIngredients){}
    public static InputData getInputData(String path) throws FileNotFoundException {
        File input = new File(path);
        Scanner myReader = new Scanner(input);

        var freshRanges=new ArrayList<IdRange>();
        var availableIngredients=new ArrayList<Long>();

        var reachedAvailable=false;
        while (myReader.hasNextLine()) {
            String line = myReader.nextLine();
            if(line.isEmpty()){
                reachedAvailable=true;
                continue;
            }
            if (!reachedAvailable){
                var parts=line.split("-");
                var range=new IdRange(Long.parseLong(parts[0]),Long.parseLong(parts[1]));
                freshRanges.add(range);
            }
            else {
                availableIngredients.add(Long.parseLong(line));
            }
        }

        myReader.close();
        return new InputData(freshRanges,availableIngredients);
    }

    static int solutionPart1(InputData inputData){
        //naive approach OOM
//        Set<Long> allFreshRanges=new HashSet<>();
//        for (var range:inputData.freshRanges){
//            for (long i = range.start; i <= range.end; i++) {
//                allFreshRanges.add(i);
//            }
//        }
//        int res=0;
//        for (var id:inputData.availableIngredients){
//            if (allFreshRanges.contains(id))
//                res++;
//        }
//        return res;

        //go through the available and then compare against every known fresh

        Set<Long> freshSet=new HashSet<>();
        for (var id:inputData.availableIngredients){
            for (var freshRange:inputData.freshRanges){
                if (freshRange.start<=id && id <= freshRange.end){
//                    System.out.println(id+" is fresh");
                    freshSet.add(id);
                }
            }
        }

        return freshSet.size();
    }


    static long solutionPart2(InputData inputData){
        //merge the values and then add the sum of the merged fresh ranges
        var intervalsSorted=inputData.freshRanges;
        intervalsSorted.sort(Comparator.comparing(idRange -> idRange.start));

        for (int i = 0; i < intervalsSorted.size()-1; i++) {
            var first=intervalsSorted.get(i);
            var second=intervalsSorted.get(i+1);
            //no overlap
            if(first.end<second.start)
                continue;
            //overlap
            else {
                var lowest=first.start;
                var highest=Math.max(first.end, second.end);
                intervalsSorted.set(i,new IdRange(lowest,highest));
                intervalsSorted.remove(i+1);
                i--;
            }
        }

        long res=0;
        for (var interval:intervalsSorted){
            res+=interval.end-interval.start+1;
        }
        return res;
    }


}
