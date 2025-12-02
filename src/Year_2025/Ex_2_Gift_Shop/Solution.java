package Year_2025.Ex_2_Gift_Shop;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Solution {
    public static void main(String[] args) throws FileNotFoundException {
    }
    record IdRange(String start, String end) {    }
    public static List<IdRange> getInputData(String path) throws FileNotFoundException {
        File input = new File(path);
        Scanner myReader = new Scanner(input);

        var res = new ArrayList<IdRange>();
        //we know there's only one line
        String line = myReader.nextLine();
        var ranges=line.split(",");
        for (var range:ranges){
            var parts=range.split("-");
//            var begin=Long.parseLong(parts[0]);
//            var end=Long.parseLong(parts[1]);
            res.add(new IdRange(parts[0],parts[1]));
        }
        myReader.close();
        return res;
    }

    static long solutionPart1(List<IdRange> turns){
        //sequence repeated twice check
        //leading zero check
        long res=0;

        for (var turn:turns){
            var begin=Long.parseLong(turn.start());
            var end=Long.parseLong(turn.end());

            for (long i = begin; i <= end; i++) {
                //sequence repeated twice check
                var iString=String.valueOf(i);
                var len=iString.length();
                //odd length cant be invalid
                if(len%2!=0)
                    continue;

                var stringBegin= iString.substring(0,len/2);
                var stringEnd=iString.substring(len/2);

                if (stringBegin.equals(stringEnd)){
//                    System.out.println("found invalid: "+i);
                    res+=i;
                }
            }
        }

        return res;
    }

    static long solutionPart2(List<IdRange> turns){
        long res=0;
        for (var turn:turns) {
            var begin = Long.parseLong(turn.start());
            var end = Long.parseLong(turn.end());

            for (long i = begin; i <= end; i++) {
                //sequence repeated twice or more check
                List<String> sequences= getAllPossibleRepeatedSequences(i);
                for (var seq:sequences){
                    boolean isValid= isValid(i,seq);
                    if (!isValid){
                        res+=i;
                        break;
                    }
                }
            }
        }
        return res;
    }

    static boolean isValid(long i, String seq) {
        var iString=String.valueOf(i);

        //try to multiply sequence until the sized match and then check the actual values
        StringBuilder builder=new StringBuilder();
        while (builder.length()<iString.length()){
            builder.append(seq);
        }
        var built=builder.toString();

        if (built.length()==iString.length()){
            if (built.equals(iString))
                return false;
        }

        return true;
    }

    static List<String> getAllPossibleRepeatedSequences(long i) {
        var res=new ArrayList<String>();
        var iString=String.valueOf(i);

        for (int j = 0; j < iString.length(); j++) {
            for (int k = j+1; k < iString.length(); k++) {
                res.add(iString.substring(j,k));
            }
        }

        return res;
    }
}
