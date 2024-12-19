package Year_2024.Ex_19_Linen_Layout;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

public class Solution {
    public static void main(String[] args) throws FileNotFoundException {
        File input = new File("src/Year_2024/Ex_19_Linen_Layout/input.txt");
        Scanner myReader = new Scanner(input);

        List<String> availablePatterns=new ArrayList<>();
        List<String> neededCombinations=new ArrayList<>();

        var passedEmptyLine=false;
        while (myReader.hasNextLine()) {
            String line = myReader.nextLine();
            if (line.isEmpty()) {
                passedEmptyLine = true;
                continue;
            }
            if (!passedEmptyLine) {
                var patterns = Arrays.stream(line.split(",")).map(String::trim).toList();
                availablePatterns.addAll(patterns);
            } else {
                neededCombinations.add(line);
            }
        }
        myReader.close();

//        System.out.println(availablePatterns);
//        System.out.println(neededCombinations);

        System.out.println("Result part 1: "+ getPossibleDesignsNum(availablePatterns,neededCombinations));

        Map<String, Long> combinationToPatternsMap=new HashMap<>();
        System.out.println("Result part 2: "+ getPossibleDesignsNum2(availablePatterns,neededCombinations,combinationToPatternsMap));
//        System.out.println(combinationToPatternsMap);
    }

    private static long getPossibleDesignsNum2(List<String> availablePatterns, List<String> neededCombinations, Map<String, Long> patternToPossibleCombinationsMap) {
        for (var combination:neededCombinations){
            checkCombinationPossible2(availablePatterns, combination,patternToPossibleCombinationsMap);
        }

        var res=0L;
        for (var val:neededCombinations){
            res+=patternToPossibleCombinationsMap.get(val);
        }
        return res;
    }

    private static long checkCombinationPossible2(List<String> availablePatterns, String remainingCombination, Map<String, Long> patternToPossibleCombinationsMap) {
        if (remainingCombination.isEmpty()){
            return 1;
        }

        if(patternToPossibleCombinationsMap.containsKey(remainingCombination)){
            return patternToPossibleCombinationsMap.get(remainingCombination);
        }

        var res=0L;
        for (var pattern:availablePatterns){
            if(remainingCombination.startsWith(pattern)){
                res+= checkCombinationPossible2(availablePatterns, remainingCombination.substring(pattern.length()),patternToPossibleCombinationsMap);
            }
        }

        patternToPossibleCombinationsMap.put(remainingCombination,res);

        return res;
    }



    private static long getPossibleDesignsNum(List<String> availablePatterns, List<String> neededCombinations) {
        var res=0L;
        for (var combination:neededCombinations){
            var combinationPossible=checkCombinationPossible(availablePatterns,combination);
            if (combinationPossible)
                res++;
        }

        return res;
    }

    private static boolean checkCombinationPossible(List<String> availablePatterns, String combination) {
        if (combination.isEmpty())
            return true;

        for (var pattern:availablePatterns){
            if(combination.startsWith(pattern)){
                var checkPos= checkCombinationPossible(availablePatterns,combination.substring(pattern.length()));
                if (checkPos)
                    return true;
            }
        }

        return false;
    }
}
