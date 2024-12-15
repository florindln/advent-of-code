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

        List<char[]> matrix1=new ArrayList<>();
        List<char[]> matrix2=new ArrayList<>();
        var moves=new ArrayList<Character>();

        var beforeSpace=true;
        while (myReader.hasNextLine()) {
            String line = myReader.nextLine();
            if (line.isEmpty())
                beforeSpace=false;

            if(beforeSpace){
                matrix1.add(line.toCharArray());
                char[] matrix2Line=new char[line.length()*2];
                var matrix2LineIndex=0;
                for (int i = 0; i < line.length(); i++) {
                    var c=line.charAt(i);
                    switch (c){
                        case '#'->{
                            matrix2Line[matrix2LineIndex++]='#';
                            matrix2Line[matrix2LineIndex++]='#';
                        }
                        case 'O'->{
                            matrix2Line[matrix2LineIndex++]='[';
                            matrix2Line[matrix2LineIndex++]=']';
                        }
                        case '.'->{
                            matrix2Line[matrix2LineIndex++]='.';
                            matrix2Line[matrix2LineIndex++]='.';
                        }
                        case '@'->{
                            matrix2Line[matrix2LineIndex++]='@';
                            matrix2Line[matrix2LineIndex++]='.';
                        }
                        default -> throw new RuntimeException("Invalid character in input");
                    }
                }
                matrix2.add(matrix2Line);
            }
            else{
                for (char c : line.toCharArray()) {
                    moves.add(c);
                }
            }
        }
        myReader.close();

//        for (var arr:matrix2)
//            System.out.println(arr);
//        System.out.println(moves);

        System.out.println("Result part 1: "+ getBoxGpsSum(matrix1,moves));
        System.out.println("Result part 2: "+ getBoxGpsSumBigBox(matrix2,moves));
    }

    private static int getBoxGpsSumBigBox(List<char[]> matrix2, List<Character> moves) {
        for (var move : moves) {
            moveRobotBigBox(matrix2, move);

//            System.out.println("applying " + move);
//            for (var arr : matrix2)
//                System.out.println(arr);
        }

        //now we calculate gps coords for each box using the formula 100*r+c, keeping in mind the closest edge
        var res = 0;
        for (int r = 0; r < matrix2.size(); r++) {
            for (int c = 0; c < matrix2.get(0).length; c++) {
                var curr = matrix2.get(r)[c];
                if (curr == ']')
                    continue;
                if (curr == '[') {
                    res += 100 * r + c;
                }
            }
        }

        return res;
    }

    private static void moveRobotBigBox(List<char[]> matrix, Character move) {
        Position robotPos=null;
        for (int r = 0; r < matrix.size(); r++) {
            for (int c = 0; c < matrix.get(0).length; c++) {
                if(matrix.get(r)[c]=='@')
                    robotPos=new Position(r,c);
            }
        }

        //logic for going left/right is the same as part 1 since only the width increased
        //for going up/down we need to take into account widths of 2 for boxes
        var canMove=applyMoveRecBigBox(matrix,move,robotPos,'@',false);
        if (canMove){
            applyMoveRecBigBox(matrix,move,robotPos,'@',true);
        }

    }

    private static boolean applyMoveRecBigBox(List<char[]> matrix, Character move, Position currPos, char objectToMove,boolean moveImmediately) {
        if(currPos.r<0||currPos.r>=matrix.size() || currPos.c<0 || currPos.c>=matrix.get(0).length || matrix.get(currPos.r)[currPos.c]=='#')
            return false;

        return switch (move) {
            case '^' -> {
                if(objectToMove=='['){
                    var newPosLeft=new Position(currPos.r - 1, currPos.c);
                    var newPosRight=new Position(currPos.r - 1, currPos.c+1);
                    var currPosRight=new Position(currPos.r,currPos.c+1);
                    yield moveBigBoxUpDown(matrix, move, currPos,currPosRight, newPosLeft,newPosRight, objectToMove,']',moveImmediately);
                }
                else if (objectToMove==']'){
                    var newPosLeft=new Position(currPos.r - 1, currPos.c-1);
                    var newPosRight=new Position(currPos.r - 1, currPos.c);
                    var currPosLeft=new Position(currPos.r,currPos.c-1);
                    yield moveBigBoxUpDown(matrix, move,currPosLeft, currPos, newPosLeft,newPosRight, '[',objectToMove,moveImmediately);
                }
                else{
                    yield moveBigBox(matrix, move, currPos,new Position(currPos.r - 1, currPos.c), objectToMove,moveImmediately);
                }
            }
            case '>' -> moveBigBox(matrix, move, currPos, new Position(currPos.r, currPos.c + 1), objectToMove,moveImmediately);
            case 'v' -> {
                if(objectToMove=='['){
                    var newPosLeft=new Position(currPos.r + 1, currPos.c);
                    var newPosRight=new Position(currPos.r + 1, currPos.c+1);
                    var currPosRight=new Position(currPos.r,currPos.c+1);
                    yield moveBigBoxUpDown(matrix, move, currPos,currPosRight, newPosLeft,newPosRight, objectToMove,']',moveImmediately);
                }
                else if (objectToMove==']'){
                    var newPosLeft=new Position(currPos.r + 1, currPos.c-1);
                    var newPosRight=new Position(currPos.r + 1, currPos.c);
                    var currPosLeft=new Position(currPos.r,currPos.c-1);
                    yield moveBigBoxUpDown(matrix, move,currPosLeft, currPos, newPosLeft,newPosRight, '[',objectToMove,moveImmediately);
                }
                else{
                    yield moveBigBox(matrix, move, currPos, new Position(currPos.r + 1, currPos.c), objectToMove,moveImmediately);
                }
            }
            case '<' -> moveBigBox(matrix, move, currPos, new Position(currPos.r, currPos.c - 1), objectToMove,moveImmediately);
            default -> throw new RuntimeException("Invalid move");
        };
    }

    private static boolean moveBigBoxUpDown(List<char[]> matrix, Character move, Position currPosLeft,Position currPosRight, Position newPosLeft, Position newPosRight, char objectToMoveLeft, char objectToMoveRight,boolean moveImmediately) {
        //move into empty space
        char newObjLeft = matrix.get(newPosLeft.r)[newPosLeft.c];
        char newObjRight = matrix.get(newPosRight.r)[newPosRight.c];

        char currObjLeft=matrix.get(currPosLeft.r)[currPosLeft.c];
        char currObjRight=matrix.get(currPosRight.r)[currPosRight.c];

        if(newObjLeft =='.' && newObjRight=='.'){
            if (moveImmediately)
                modifyMatrix(matrix, currPosLeft, currPosRight, newPosLeft, newPosRight, objectToMoveLeft, objectToMoveRight);
            return true;
        }
        //wall, no move
        if(newObjLeft=='#' || newObjRight=='#'){
            return false;
        }
        //boxes stacked perfectly on top of each other
        // []
        // []
        if(newObjLeft=='['&&newObjRight==']') {
            if (
                    applyMoveRecBigBox(matrix, move, new Position(newPosLeft.r, newPosLeft.c), objectToMoveLeft,moveImmediately)
//                    && applyMoveRecBigBox(matrix, move, new Position(newPosRight.r, newPosRight.c), objectToMoveRight)
            ) {
                if (moveImmediately)
                    modifyMatrix(matrix, currPosLeft, currPosRight, newPosLeft, newPosRight, objectToMoveLeft, objectToMoveRight);
                return true;
            } else
                return false;
        }
        //boxes stacked like this
        // []
        //  []
        var leftCanMove = true;
        var rightCanMove = true;

        if (newObjLeft == ']') {
            leftCanMove = applyMoveRecBigBox(matrix, move, new Position(newPosLeft.r, newPosLeft.c - 1), objectToMoveLeft,moveImmediately);
        }

        if (newObjRight == '[') {
            rightCanMove = applyMoveRecBigBox(matrix, move, new Position(newPosRight.r, newPosRight.c + 1), objectToMoveRight,moveImmediately);
        }

        if (leftCanMove && rightCanMove) {
            if (moveImmediately)
                modifyMatrix(matrix, currPosLeft, currPosRight, newPosLeft, newPosRight, objectToMoveLeft, objectToMoveRight);
            return true;
        }

        return false;
    }

    private static void modifyMatrix(List<char[]> matrix, Position currPosLeft, Position currPosRight, Position newPosLeft, Position newPosRight, char objectToMoveLeft, char objectToMoveRight) {
        matrix.get(newPosLeft.r)[newPosLeft.c] = objectToMoveLeft;
        matrix.get(newPosRight.r)[newPosRight.c] = objectToMoveRight;
        matrix.get(currPosLeft.r)[currPosLeft.c] = '.';
        matrix.get(currPosRight.r)[currPosRight.c] = '.';
    }

    private static boolean moveBigBox(List<char[]> matrix, Character move, Position oldPos, Position newPos, char objectToMove,boolean moveImmediately) {
        //move into empty space
        char newObj = matrix.get(newPos.r)[newPos.c];
        if(newObj =='.'){
            if(moveImmediately) {
                matrix.get(newPos.r)[newPos.c] = objectToMove;
                matrix.get(oldPos.r)[oldPos.c] = '.';
            }
            return true;
        }
        //wall, no move
        if(newObj=='#'){
            return false;
        }
        //box
        if(newObj=='['||newObj==']'){
            if( applyMoveRecBigBox(matrix, move, new Position(newPos.r, newPos.c), newObj,moveImmediately) ) {
                if (moveImmediately) {
                    matrix.get(newPos.r)[newPos.c] = objectToMove;
                    matrix.get(oldPos.r)[oldPos.c] = '.';
                }
                return true;
            }
            else
                return false;
        }
        throw new RuntimeException("Unexpected object big box");
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
