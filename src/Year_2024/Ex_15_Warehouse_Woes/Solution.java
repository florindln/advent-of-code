package Year_2024.Ex_15_Warehouse_Woes;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Solution {
    public static void main(String[] args) throws FileNotFoundException {
        File input = new File("src/Year_2024/Ex_15_Warehouse_Woes/input.txt");
        Scanner myReader = new Scanner(input);

        List<char[]> matrix=new ArrayList<>();
        var moves=new ArrayList<Character>();

        var beforeSpace=true;
        while (myReader.hasNextLine()) {
            String line = myReader.nextLine();
            if (line.isEmpty())
                beforeSpace=false;

            if(beforeSpace)
                matrix.add(line.toCharArray());
            else{
                for (char c : line.toCharArray()) {
                    moves.add(c);
                }
            }
        }
        myReader.close();

//        for (var arr:matrix)
//            System.out.println(arr);
//        System.out.println(moves);

        System.out.println("Result part 1: "+ getBoxGpsSum(matrix,moves));
//        System.out.println("Result part 2: "+ getHikingTrailsRatingSum(matrix));
    }

    record Position(int r,int c){}
    private static int getBoxGpsSum(List<char[]> matrix, List<Character> moves) {
        for (var move : moves) {
            moveRobot(matrix, move);

//            System.out.println("applying "+move);
//            for (var arr : matrix)
//                System.out.println(arr);
        }

        //now we calculate gps coords for each box using the formula 100*r+c
        var res = 0;
        for (int r = 0; r < matrix.size(); r++) {
            for (int c = 0; c < matrix.get(0).length; c++) {
                if (matrix.get(r)[c] == 'O')
                    res += 100 * r + c;
            }
        }

        return res;
    }

    private static void moveRobot(List<char[]> matrix, Character move) {
        Position robotPos=null;
        for (int r = 0; r < matrix.size(); r++) {
            for (int c = 0; c < matrix.get(0).length; c++) {
                if(matrix.get(r)[c]=='@')
                    robotPos=new Position(r,c);
            }
        }

        //logic for going up
        //if empty space above, just go
        //if there is a wall, don't go
        //if there is a box, apply the same steps, but for the box position first, then return to the previous steps
        applyMoveRec(matrix,move,robotPos,'@');
    }

    private static boolean applyMoveRec(List<char[]> matrix, Character move, Position pos, char objectToMove) {
        if(pos.r<0||pos.r>=matrix.size() || pos.c<0 || pos.c>=matrix.get(0).length || matrix.get(pos.r)[pos.c]=='#')
            return false;

        return switch (move) {
            case '^' -> move(matrix, move, pos, new Position(pos.r - 1, pos.c), objectToMove);
            case '>' -> move(matrix, move, pos, new Position(pos.r, pos.c + 1), objectToMove);
            case 'v' -> move(matrix, move, pos, new Position(pos.r + 1, pos.c), objectToMove);
            case '<' -> move(matrix, move, pos, new Position(pos.r, pos.c - 1), objectToMove);
            default -> throw new RuntimeException("Invalid move");
        };
    }

    private static boolean move(List<char[]> matrix, Character move, Position oldPos, Position newPos, char objectToMove) {
        //move into empty space
        if(matrix.get(newPos.r)[newPos.c]=='.'){
            matrix.get(newPos.r)[newPos.c]= objectToMove;
            matrix.get(oldPos.r)[oldPos.c]='.';
            return true;
        }
        //wall, no move
        if(matrix.get(newPos.r)[newPos.c]=='#'){
            return false;
        }
        //box
        if(matrix.get(newPos.r)[newPos.c]=='O'){
            if( applyMoveRec(matrix, move, new Position(newPos.r, newPos.c), 'O') ){
                matrix.get(newPos.r)[newPos.c]= objectToMove;
                matrix.get(oldPos.r)[oldPos.c]='.';
                return true;
            }
            else
                return false;
        }
        throw new RuntimeException("Unexpected object");
    }

}
