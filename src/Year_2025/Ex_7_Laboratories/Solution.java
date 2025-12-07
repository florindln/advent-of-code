package Year_2025.Ex_7_Laboratories;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Solution {

    public static List<String> getInputData(String path) throws FileNotFoundException {
        File input = new File(path);
        Scanner myReader = new Scanner(input);

        List<String> lines=new ArrayList<>();

        while (myReader.hasNextLine()) {
            String line = myReader.nextLine();
            lines.add(line);
        }

        myReader.close();

        return lines;
    }

    static int solutionPart1(List<String> lines){
        //go line by line, if in the above lene there is an 'S' or a '|' then propagate it to this line
        //when encountering a ^ make the adjacent spaces |
        var hits=0;
        for (int linePos = 1; linePos < lines.size(); linePos++) {
//            var line=lines.get(linePos);
//            var prevLine=lines.get(linePos-1);
            for (int charPos = 0; charPos < lines.get(linePos).length(); charPos++) {
                var aboveChar=lines.get(linePos-1).charAt(charPos);
                var currChar=lines.get(linePos).charAt(charPos);

                if (aboveChar=='S'||aboveChar=='|'){
                    if (currChar!='^'){
                        var newLine=new StringBuilder(lines.get(linePos));
                        newLine.setCharAt(charPos,'|');
                        lines.set(linePos,newLine.toString());
                    }
                    else {
                        var newLine=new StringBuilder(lines.get(linePos));
                        newLine.setCharAt(charPos-1,'|');
                        newLine.setCharAt(charPos+1,'|');
                        lines.set(linePos,newLine.toString());
                        hits++;
                    }
                }
            }
        }

        var beamsExisted=new boolean[lines.get(0).length()];
        for (int linePos = 1; linePos < lines.size(); linePos++) {
            for (int charPos = 0; charPos < lines.get(linePos).length(); charPos++) {
                if (lines.get(linePos).charAt(charPos)=='|')
                    beamsExisted[charPos]=true;
            }
        }

        return hits;
    }

    static long solutionPart2(List<String> lines){
        var memo=new long[lines.size()][lines.get(0).length()];
        var res=dfs(lines,1,memo);
        return res;
    }

    static long dfs(List<String> lines, int level, long[][] memo){
        if (level==lines.size()-1)
            return 1;


        for (int charPosIter = 0; charPosIter < lines.get(0).length(); charPosIter++) {

            if (lines.get(level-1).charAt(charPosIter)=='S' || lines.get(level-1).charAt(charPosIter)=='|'){
                if (lines.get(level).charAt(charPosIter)=='^'){
                    if (memo[level][charPosIter]!=0)
                        return memo[level][charPosIter];

                    //dfs left
                    var newLine=new StringBuilder(lines.get(level));
                    newLine.setCharAt(charPosIter-1,'|');
                    lines.set(level,newLine.toString());
                    var resLeft=dfs(lines,level+1,memo);

                    //dfs right, undo the left and apply right
                    newLine=new StringBuilder(lines.get(level));
                    newLine.setCharAt(charPosIter-1,'.');
                    newLine.setCharAt(charPosIter+1,'|');
                    lines.set(level,newLine.toString());
                    var resRight=dfs(lines,level+1,memo);

                    //undo right
                    newLine=new StringBuilder(lines.get(level));
                    newLine.setCharAt(charPosIter+1,'.');
                    lines.set(level,newLine.toString());

                    var res=resLeft+resRight;

                    memo[level][charPosIter]=res;
                    return res;
                }
                else {
                    var newLine=new StringBuilder(lines.get(level));
                    newLine.setCharAt(charPosIter,'|');
                    lines.set(level,newLine.toString());

                    var res=dfs(lines,level+1,memo);

                    //undo
                    newLine=new StringBuilder(lines.get(level));
                    newLine.setCharAt(charPosIter,'.');
                    lines.set(level,newLine.toString());

                    memo[level][charPosIter]=res;
                    return res;
                }
            }
        }
        return 0;
    }
}
