package Year_2024.Ex_4_Ceres_Search;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;

public class Solution {
    public static void main(String[] args) throws FileNotFoundException {
        File input = new File("src/Year_2024/Ex_4_Ceres_Search/input.txt");
        Scanner myReader = new Scanner(input);

        ArrayList<char[]> matrix=new ArrayList<>();

        while (myReader.hasNextLine()) {
            String line = myReader.nextLine();
            matrix.add(line.toCharArray());
        }
        myReader.close();

//        for (var arr:matrix)
//            System.out.println(arr);

        var res1 = getRes1(matrix);
        var res2 = getRes2(matrix);

        System.out.println("Result part 1: "+ res1);
        System.out.println("Result part 2: "+res2);
    }

    private static int getRes2(ArrayList<char[]> matrix) {
        var res2=0;
        //to look through for an MAS in the form of an X we try to find the center A and then look at the diagonals for M/S and the opposite letter on the other side

        for (int r = 1; r < matrix.size()-1; r++) {
            for (int c = 1; c < matrix.get(0).length-1; c++) {
                if(matrix.get(r)[c]=='A'){
                    if (canMakeX(matrix,r,c)){
                        res2+=1;
                    }
                }
            }
        }
        return res2;
    }

    private static boolean canMakeX(ArrayList<char[]> matrix, int r, int c) {
        boolean topLeftM = matrix.get(r - 1)[c - 1] == 'M';
        boolean topRightM = matrix.get(r - 1)[c + 1] == 'M';
        boolean bottomLeftM = matrix.get(r + 1)[c - 1] == 'M';
        boolean bottomRightM = matrix.get(r+1)[c+1]=='M';

        boolean bottomRightS = matrix.get(r + 1)[c + 1] == 'S';
        boolean bottomLeftS = matrix.get(r + 1)[c - 1] == 'S';
        boolean topRightS = matrix.get(r - 1)[c + 1] == 'S';
        boolean topLeftS=matrix.get(r-1)[c-1]=='S';

        if(topLeftM&&bottomLeftM)
            if(bottomRightS&&topRightS)
                return true;

        if (topLeftM && topRightM)
            if (bottomRightS && bottomLeftS)
                return true;

        if(topRightM&&bottomRightM)
            if(topLeftS && bottomLeftS)
                return true;

        if(bottomRightM&&bottomLeftM)
            if(topLeftS&&topRightS)
                return true;

        return false;
    }

    private static int getRes1(ArrayList<char[]> matrix) {
        var directions=new ArrayList<Integer[]>();
        for(int r=-1;r<=1;r++)
            for(int c=-1;c<=1;c++)
                if(!(r==0&&c==0))
                    directions.add(new Integer[]{r, c});

        var res=0;

        for (int r = 0; r < matrix.size(); r++) {
            for (int c = 0; c < matrix.get(0).length; c++) {
                for(var direction:directions){
                    res+=rec(matrix,r,c,new ArrayList<>(),direction);

                }
            }
        }
        return res;
    }

    private static int rec(ArrayList<char[]> matrix, int r, int c, List<Character> curr, Integer[] direction) {
        if(curr.size()>4)
            return 0;

        if(curr.stream().map(String::valueOf).collect(Collectors.joining()).equals("XMAS"))
            return 1;

        if(r<0 || r>=matrix.size() || c<0 || c>=matrix.get(0).length)
            return 0;

        var currChar=matrix.get(r)[c];

        var res=0;
        curr.add(currChar);
        res+=rec(matrix,r+direction[0],c+direction[1],curr, direction);
        curr.removeLast();

        return res;
    }


}
