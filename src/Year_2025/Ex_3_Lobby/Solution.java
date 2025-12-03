package Year_2025.Ex_3_Lobby;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Solution {
    public static List<List<Integer>> getInputData(String path) throws FileNotFoundException {
        File input = new File(path);
        Scanner myReader = new Scanner(input);

        var voltages=new ArrayList<List<Integer>>();

        while (myReader.hasNextLine()) {
            String line = myReader.nextLine();
            var lineVoltages=new ArrayList<Integer>();
            for (int i = 0; i < line.length(); i++) {
                lineVoltages.add(line.charAt(i)-'0');
            }
            voltages.add(lineVoltages);
        }

        myReader.close();
        return voltages;
    }

    static long solutionPart1(List<List<Integer>> lines){
        var highestPerLine=new ArrayList<Integer>();
        for (var line:lines){
            // use 2 loops one to get the highest digit (doesnt check the last voltage) and the other loop takes the highest num after the first one
            var max=-1;
            var maxPos=-1;
            for (int i = 0; i < line.size()-1; i++) {
                if(line.get(i)>max){
                    max=line.get(i);
                    maxPos=i;
                }
            }
            var secondMax=-1;
            for (int i = maxPos+1; i < line.size(); i++) {
                if (line.get(i)>secondMax){
                    secondMax=line.get(i);
                }
            }
            var concatenated=Integer.parseInt(String.valueOf(max)+ secondMax);
            highestPerLine.add(concatenated);
        }

        long res=highestPerLine.stream().reduce(0,Integer::sum);
        return res;
    }

    static long solutionPart2(List<List<Integer>> lines,int depth){
        //we can use the same idea from part 1 but instead of hardcoding twice using a function that can be called multiple times
        var highestPerLine=new ArrayList<Long>();
        for (var line:lines){
            // use X loops first to get the highest digit, second for second highest (doesnt check the last X-1 voltage)
            var max=-1;
            var maxPos=-1;
            var tempLineRes=new ArrayList<String>();
            for (int currDepth = depth-1; currDepth >= 0; currDepth--) {
                for (int j = maxPos+1; j < line.size()-currDepth; j++) {
                    if(line.get(j)>max){
                        max=line.get(j);
                        maxPos=j;
                    }
                }
                tempLineRes.add(String.valueOf(max));
                max=0;
            }
            var concatenated=Long.parseLong(tempLineRes.stream().reduce("",String::concat));
            highestPerLine.add(concatenated);
        }

        long res=highestPerLine.stream().reduce(0L,Long::sum);
        return res;
    }
}
