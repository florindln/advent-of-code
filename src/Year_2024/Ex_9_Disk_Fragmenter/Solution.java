package Year_2024.Ex_9_Disk_Fragmenter;

import java.io.File;
import java.io.FileNotFoundException;
import java.math.BigInteger;
import java.util.*;


public class Solution {
    public static void main(String[] args) throws FileNotFoundException {
        File input = new File("src/Year_2024/Ex_9_Disk_Fragmenter/input.txt");
        Scanner myReader = new Scanner(input);

        int[] diskLine = null;
        while (myReader.hasNextLine()) {
            String line = myReader.nextLine();
            int[] intArray = new int[line.length()];

            for (int i = 0; i < line.length(); i++) {
                intArray[i] = Character.getNumericValue(line.charAt(i));
            }
            diskLine=intArray;
        }
        myReader.close();

//        System.out.println(Arrays.toString(diskLine));

        System.out.println("Result part 1: "+ getCheckSum(diskLine));
        System.out.println("Result part 2: "+getCheckSumFullBlocks(diskLine));
    }

    private static BigInteger getCheckSumFullBlocks(int[] diskLine) {
        var blockLine = getBlockLine(diskLine);

        var rBegin = blockLine.size() - 1;
        var rEnd = blockLine.size() - 1;

        while (rBegin >= 1) {
            //rBegin and rEnd catch an interval with only numbers, beginning inclusive, end exclusive
            while (rEnd >= 0 && blockLine.get(rEnd).equals("."))
                rEnd--;

            rBegin = rEnd;
            while (rBegin >= 0 && blockLine.get(rBegin).equals(blockLine.get(rEnd)))
                rBegin--;

            //fix beginning and end for the right side
            rBegin++;
            rEnd++;

            // now we attempt to fit the right side into every possible empty slot starting from the left side
            var lBegin = 0;
            var lEnd = 0;

            var originalRBegin = rBegin;

            while (lEnd < rBegin) {
                //lBegin and lEnd catch a .... interval, beginning inclusive, end exclusive
                while (lBegin < rBegin && !blockLine.get(lBegin).equals("."))
                    lBegin++;

                lEnd = lBegin;
                while (lEnd < rBegin && blockLine.get(lEnd).equals("."))
                    lEnd++;

                if (rEnd - rBegin <= lEnd - lBegin) {
                    while (rBegin < rEnd) {
                        var left = blockLine.get(lBegin);
                        blockLine.set(lBegin, blockLine.get(rBegin));
                        blockLine.set(rBegin, left);
                        rBegin++;
                        lBegin++;
//                    System.out.println(blockLine);
                    }
                    break;
                }
                lBegin = lEnd;
            }
            rEnd = originalRBegin - 1;
        }
        return getCheckSumFromCorrectBlockLine(blockLine);
    }

    private static BigInteger getCheckSum(int[] diskLine) {
        var blockLine=getBlockLine(diskLine);

        var l=0;
        var r=blockLine.size()-1;

        while (l<r){
            while (!blockLine.get(l).equals("."))
                l++;
            while (blockLine.get(r).equals("."))
                r--;

            if(l>=r)
                continue;

            var left=blockLine.get(l);
            blockLine.set(l,blockLine.get(r));
            blockLine.set(r,left);
        }
//        System.out.println(blockLine);

        return getCheckSumFromCorrectBlockLine(blockLine);
    }

    private static BigInteger getCheckSumFromCorrectBlockLine(List<String> blockLine) {
        BigInteger res=BigInteger.ZERO;
        var counter=0L;
        for (var val: blockLine){
            if(val.equals(".")){
                counter++;
                continue;
            }

            res=res.add(BigInteger.valueOf(counter*Long.parseLong(val)));
            counter++;
        }

        return res;
    }

    private static List<String> getBlockLine(int[] diskLine) {
        var res=new ArrayList<String>();

        int counter=0;
        for (int i = 0; i < diskLine.length; i++) {
            if(i%2!=0){
                for (int numVal = 0; numVal < diskLine[i]; numVal++)
                    res.add(".");
                continue;
            }
            for (int numVal = 0; numVal < diskLine[i]; numVal++)
                res.add(String.valueOf(counter));

            counter++;
        }
        return res;
    }

}
