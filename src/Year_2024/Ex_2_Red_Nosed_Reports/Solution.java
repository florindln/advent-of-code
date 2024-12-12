package Year_2024.Ex_2_Red_Nosed_Reports;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

public class Solution {
    public static void main(String[] args) throws FileNotFoundException {
        File input = new File("src/Year_2024/Ex_2_Red_Nosed_Reports/input.txt");
        Scanner myReader = new Scanner(input);

        var levels = new ArrayList<List<Integer>>();

        while (myReader.hasNextLine()) {
            String line = myReader.nextLine();
            String[] parts = line.split(" ");
//            System.out.println(Arrays.toString(parts));
            levels.add(Arrays.stream(parts).map(Integer::parseInt).toList());
        }
        myReader.close();

        System.out.println("Levels: " + levels);

        System.out.println("Result part 1: "+ getSafeLevelsNumber(levels));
        System.out.println("Result part 2: "+getSafeLevelsDampener(levels));
    }

    private static int getSafeLevelsNumber(ArrayList<List<Integer>> levels) {
        var res=0;

        for (var level:levels){
            if(isLevelSafe(level))
                res+=1;
        }

        return res;
    }

    private static int getSafeLevelsDampener(ArrayList<List<Integer>> levels) {
        var res=0;

        for (var level:levels){
            if(isLevelSafeDampener(level))
                res+=1;
        }

        return res;
    }

    private static boolean isLevelSafeDampener(List<Integer> level) {
        if(isLevelSafe(level))
            return true;

        // try to remove numbers number at a time from a level to check if it becomes safe
        for (int i = 0; i < level.size(); i++) {
            var newlevel=new ArrayList<>(level);
            newlevel.remove(i);
            if(isLevelSafe(newlevel))
                return true;
        }
        return false;
    }

    private static boolean isLevelSafe(List<Integer> level) {
        var safeDifference=true;
        var safeAsc=true;
        var safeDesc=true;

        //check difference
        for (int i = 0; i < level.size()-1; i++) {
            if(Math.abs(level.get(i)- level.get(i+1))<=3){
                continue;
            }
            safeDifference=false;
        }
        //check increasing
        for (int i = 0; i < level.size()-1; i++) {
            if(level.get(i)< level.get(i+1)){
                continue;
            }
            safeAsc=false;
        }
        //check decreasing
        for (int i = 0; i < level.size()-1; i++) {
            if(level.get(i)> level.get(i+1)){
                continue;
            }
            safeDesc=false;
        }
        if(safeDifference && (safeAsc || safeDesc)){
//                System.out.println("Level safe "+level);
            return true;
        }
        return false;
    }

}
