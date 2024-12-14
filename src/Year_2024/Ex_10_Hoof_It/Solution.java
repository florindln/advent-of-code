package Year_2024.Ex_10_Hoof_It;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

public class Solution {
    public static void main(String[] args) throws FileNotFoundException {
        File input = new File("src/Year_2024/Ex_10_Hoof_It/input.txt");
        Scanner myReader = new Scanner(input);

        ArrayList<int[]> matrix=new ArrayList<>();

        while (myReader.hasNextLine()) {
            String line = myReader.nextLine();
            int[] intArray = new int[line.length()];

            for (int i = 0; i < line.length(); i++) {
                intArray[i] = Character.getNumericValue(line.charAt(i));
            }
            matrix.add(intArray);
        }
        myReader.close();

//        for (var arr:matrix)
//            System.out.println(arr);


        System.out.println("Result part 1: "+ getHikingTrailsSum(matrix));
        System.out.println("Result part 2: "+ getHikingTrailsRatingSum(matrix));
    }

    private static int getHikingTrailsRatingSum(ArrayList<int[]> matrix) {
        var res = 0;
        for (int r = 0; r < matrix.size(); r++) {
            for (int c = 0; c < matrix.get(0).length; c++) {
                if (matrix.get(r)[c] == 0) {
                    res += recurseTrailsRating(matrix, r, c, -1);
                }
            }
        }
        return res;
    }

    private static int recurseTrailsRating(ArrayList<int[]> matrix, int r, int c, int prevElevation) {
        if (r < 0 || r >= matrix.size() || c < 0 || c >= matrix.get(0).length || matrix.get(r)[c] - prevElevation != 1)
            return 0;
        var currElevation=matrix.get(r)[c];
        if (currElevation==9)
            return 1;

        var res= recurseTrailsRating(matrix,r-1,c,currElevation)+
                recurseTrailsRating(matrix,r+1,c,currElevation)+
                recurseTrailsRating(matrix,r,c-1,currElevation)+
                recurseTrailsRating(matrix,r,c+1,currElevation);

        return res;
    }

    private static int getHikingTrailsSum(ArrayList<int[]> matrix) {
        var res = 0;
        for (int r = 0; r < matrix.size(); r++) {
            for (int c = 0; c < matrix.get(0).length; c++) {
                if (matrix.get(r)[c] == 0) {
                    res += recurseTrails(matrix, r, c, -1,new boolean[matrix.size()][matrix.get(0).length]);
                }
            }
        }
        return res;
    }

    //memo so we only count unique destinations
    private static int recurseTrails(ArrayList<int[]> matrix, int r, int c, int prevElevation,boolean[][] visitedDestination) {
        if (r < 0 || r >= matrix.size() || c < 0 || c >= matrix.get(0).length || matrix.get(r)[c] - prevElevation != 1)
            return 0;
        var currElevation=matrix.get(r)[c];

        if (currElevation==9) {
            if (visitedDestination[r][c])
                return 0;

            visitedDestination[r][c] = true;
            return 1;
        }

        var res=recurseTrails(matrix,r-1,c,currElevation,visitedDestination)+
                recurseTrails(matrix,r+1,c,currElevation,visitedDestination)+
                recurseTrails(matrix,r,c-1,currElevation,visitedDestination)+
                recurseTrails(matrix,r,c+1,currElevation,visitedDestination);

        return res;
    }

}
