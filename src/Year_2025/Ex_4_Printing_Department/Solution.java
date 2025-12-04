package Year_2025.Ex_4_Printing_Department;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Solution {
    public static List<List<Character>> getInputData(String path) throws FileNotFoundException {
        File input = new File(path);
        Scanner myReader = new Scanner(input);

        var lines=new ArrayList<List<Character>>();

        while (myReader.hasNextLine()) {
            String line = myReader.nextLine();
            var paperRolls=new ArrayList<Character>();
            for (int i = 0; i < line.length(); i++) {
                paperRolls.add(line.charAt(i));
            }
            lines.add(paperRolls);
        }

        myReader.close();
        return lines;
    }



    static Part1Res solutionPart1(List<List<Character>> lines){
        int[][] dirs = {
                {-1, 0},//up
                {1, 0},//down
                {0, -1},//left
                {0, 1},//right
                {-1,-1},//up-left
                {-1,1},//up-right
                {1,-1},//down-left
                {1,1}//down-right
        };
        for (int r = 0; r < lines.size(); r++) {
            for (int c = 0; c < lines.get(0).size(); c++) {
                //check all 8 surroundings
                var rollsOfPaperAround=0;
                for (var dir:dirs){
                    var neighborR=r+dir[0];
                    var neighborC=c+dir[1];

                    if (neighborR<0 || neighborR >= lines.size() || neighborC<0 || neighborC>=lines.get(r).size())
                        continue;

                    if (lines.get(neighborR).get(neighborC)=='@' || lines.get(neighborR).get(neighborC)=='x')
                        rollsOfPaperAround++;
                }

                if (lines.get(r).get(c)=='@' && rollsOfPaperAround<4)
                    lines.get(r).set(c,'x');
            }
        }

        int res=0;
        for (int r = 0; r < lines.size(); r++) {
            for (int c = 0; c < lines.get(0).size(); c++) {
                if (lines.get(r).get(c)=='x')
                    res++;
            }
        }

//        prettyPrintMatrix(lines);
        return new Part1Res(res,lines);
    }

    record Part1Res(int res,List<List<Character>> lines){}

    static int solutionPart2(List<List<Character>> lines){
        // we can keep running the first part while adding and modifying the matrix
        var prevRes=-1;
        var res=0;

        while (res!=prevRes){
            var output=solutionPart1(lines);
            prevRes=res;
            res+=output.res;

            var cleanedOutput=output.lines;
            for (int r = 0; r < cleanedOutput.size(); r++) {
                for (int c = 0; c < cleanedOutput.get(0).size(); c++) {
                    if (cleanedOutput.get(r).get(c)=='x')
                        cleanedOutput.get(r).set(c,'.');
                }
            }
            lines=cleanedOutput;
        }
        return res;
    }

    static void prettyPrintMatrix(List<List<Character>> matrix) {
        for (List<Character> row : matrix) {
            for (char character : row) {
                System.out.print(character + " ");
            }
            System.out.println();
        }
    }
}
