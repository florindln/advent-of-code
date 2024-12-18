package Year_2024.Ex_18_RAM_Run;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;
public class Solution {
    int globalLen=Integer.MAX_VALUE;

    public static void main(String[] args) throws FileNotFoundException {
        char[][] matrix = getMatrix(1024);

//        for (var arr:matrix)
//            System.out.println(arr);

        var sol=new Solution();
        System.out.println("Result part 1: "+ sol.getLowestScoreThroughMaze(matrix));

        for (int i = 1; i < 3450-1024; i++) {
            var sol2=new Solution();
            var currentBytes=i+1024;
            char[][] matrix2 = getMatrix(currentBytes);
            System.out.println("Result part 2: "+currentBytes+" "+sol2.getLowestScoreThroughMaze(matrix2));
            //Result part 2: 2958 492
            //Result part 2: 2959 2147483647
            //so the 2959 byte cuts off our algorithm, we need to look in the input file for the solution
        }
    }

    private static char[][] getMatrix(int maxByteCount) throws FileNotFoundException {
        File input = new File("src/Year_2024/Ex_18_RAM_Run/input.txt");
        Scanner myReader = new Scanner(input);

        var rowSize=71;
        var colSize=71;

        char[][] matrix=new char[rowSize][colSize];
        for (int r = 0; r < rowSize; r++) {
            for (int c = 0; c < colSize; c++) {
                matrix[r][c]='.';
            }
        }

        var count=0;
        while (myReader.hasNextLine()) {
            String line = myReader.nextLine();
            var chars=line.split(",");
            //careful about x,y and r,c
            matrix[Integer.parseInt(chars[1])][Integer.parseInt(chars[0])]='#';
            if(++count==maxByteCount)
                break;
        }
        myReader.close();
        return matrix;
    }

    private int getLowestScoreThroughMaze(char[][] matrix) {
//        for (int r = 0; r < matrix.length; r++) {
//            for (int c = 0; c < matrix[0].length; c++) {
//                if(matrix[r][c]=='S'){
                     recurseMaze(matrix,0,0,new Integer[matrix.length][matrix[0].length],0);

//                    return res;
//                }
//            }
//        }
        return globalLen;
    }

    private void recurseMaze(char[][] matrix, int r, int c, Integer[][] memo, int currentLen) {
        if (r < 0 || r >= matrix.length || c < 0 || c >= matrix[0].length || matrix[r][c] == '#' || matrix[r][c] == 'O' || currentLen > globalLen)
            return;

        if (r == matrix.length - 1 && c == matrix[0].length - 1) {
//            System.out.println("\ncurrent len: "+currentLen);
//            for (var arr:matrix)
//                System.out.println(arr);
            globalLen = Math.min(globalLen, currentLen);
            return;
        }

        if (memo[r][c] != null && currentLen >= memo[r][c])
            return;

        var curr = matrix[r][c];
        matrix[r][c] = 'O';

        memo[r][c] = currentLen;

        recurseMaze(matrix, r - 1, c, memo, currentLen + 1);
        recurseMaze(matrix, r + 1, c, memo, currentLen + 1);
        recurseMaze(matrix, r, c - 1, memo, currentLen + 1);
        recurseMaze(matrix, r, c + 1, memo, currentLen + 1);

        matrix[r][c] = curr;
    }

}
