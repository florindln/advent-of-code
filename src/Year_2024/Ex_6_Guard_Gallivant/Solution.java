package Year_2024.Ex_6_Guard_Gallivant;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;


enum Direction{
    UP,
    RIGHT,
    DOWN,
    LEFT
}

public class Solution {
    public static void main(String[] args) throws FileNotFoundException {
        File input = new File("src/Year_2024/Ex_6_Guard_Gallivant/input.txt");
        Scanner myReader = new Scanner(input);

        List<char[]> matrix=new ArrayList<>();

        while (myReader.hasNextLine()) {
            String line = myReader.nextLine();
            matrix.add(line.toCharArray());
        }
        myReader.close();

//        for (var arr:matrix)
//            System.out.println(arr);


        System.out.println("Result part 1: "+ getPatrolSum(matrix));
        System.out.println("Result part 2: "+ getInfiniteLoopPossibilities(matrix));
    }

    private static int getInfiniteLoopPossibilities(List<char[]> matrix) {
        var guardR=0;
        var guardC=0;
        for (int r = 0; r < matrix.size(); r++) {
            for (int c = 0; c < matrix.get(0).length; c++) {
                if(matrix.get(r)[c]=='^'){
                    guardR=r;
                    guardC=c;
                }
            }
        }

        //we try to place an obstacle at every possible existing position(besides the starting one guard) and check if it will create an infinite loop
        var res=0;
        for (int r = 0; r < matrix.size(); r++) {
            for (int c = 0; c < matrix.get(0).length; c++) {
                var curr=matrix.get(r)[c];
                if(curr=='^' || curr=='#')
                    continue;
                matrix.get(r)[c]='#';
                if(isInfiniteLoop(matrix,guardR,guardC,Direction.UP))
                    res+=1;
                matrix.get(r)[c]=curr;
            }
        }
        return res;
    }

    private static boolean isInfiniteLoop(List<char[]> matrix, int startR, int startC, Direction startDirection) {
        Set<String> visited = new HashSet<>();
        int r = startR, c = startC;
        Direction direction = startDirection;

        while (true) {
            if (r < 0 || r >= matrix.size() || c < 0 || c >= matrix.get(0).length) {
                return false;
            }

            String state = r + " " + c + " " + direction;
            if (matrix.get(r)[c] != '#' && visited.contains(state)) {
                return true;
            }
            visited.add(state);

            if (matrix.get(r)[c] == '#') {
                //go back so we are not on the #
                switch (direction) {
                    case UP -> r++;
                    case RIGHT -> c--;
                    case DOWN -> r--;
                    case LEFT -> c++;
                }
                direction = turnRight(direction);
            } else {
                switch (direction) {
                    case UP -> r--;
                    case RIGHT -> c++;
                    case DOWN -> r++;
                    case LEFT -> c--;
                }
            }
        }
    }

    private static Direction turnRight(Direction dir) {
        return switch (dir) {
            case UP -> Direction.RIGHT;
            case RIGHT -> Direction.DOWN;
            case DOWN -> Direction.LEFT;
            case LEFT -> Direction.UP;
        };
    }

    //might be correct but gives stackoverflow due to being too deep
//    private static boolean isInfiniteLoop(List<char[]> matrix, int r, int c, Direction direction,List<String> state) {
//        if(r<0 || r>=matrix.size() || c<0 || c>=matrix.get(0).length)
//            return false;
//
//        var currentState=r+" "+c+" "+direction;
//        if(state.contains(currentState))
//            return true;
//
//        state.add(currentState);
//
//        switch (direction) {
//            case UP -> {
//                if(matrix.get(r)[c]=='#'){
//                    if ( isInfiniteLoop(matrix,r+1,c,Direction.RIGHT,state) ) return true;
//                }
//                else
//                    if ( isInfiniteLoop(matrix,r-1,c,Direction.UP,state) ) return true;
//            }
//            case RIGHT -> {
//                if(matrix.get(r)[c]=='#'){
//                    if ( isInfiniteLoop(matrix,r,c-1,Direction.DOWN,state) ) return true;
//                }
//                else
//                    if ( isInfiniteLoop(matrix,r,c+1,Direction.RIGHT,state) ) return true;
//            }
//            case DOWN -> {
//                if(matrix.get(r)[c]=='#'){
//                    if ( isInfiniteLoop(matrix,r-1,c,Direction.LEFT,state) ) return true;
//                }
//                else
//                    if ( isInfiniteLoop(matrix,r+1,c,Direction.DOWN,state) ) return true;
//            }
//            case LEFT -> {
//                if(matrix.get(r)[c]=='#'){
//                    if ( isInfiniteLoop(matrix,r,c+1,Direction.UP,state) ) return true;
//                }
//                else
//                    if ( isInfiniteLoop(matrix,r,c-1,Direction.LEFT,state) ) return true;
//            }
//            default -> throw new RuntimeException("Shouldn't reach here");
//        }
//        return false;
//    }


    private static int getPatrolSum(List<char[]> matrixOriginal) {
        //deep copy to not mess up the original
        var matrix=new ArrayList<char[]>();
        for (var charArr:matrixOriginal){
            var newCharArr=new char[charArr.length];
            for (int i = 0; i < newCharArr.length; i++) {
                newCharArr[i]=charArr[i];
            }
            matrix.add(newCharArr);
        }


        for (int r = 0; r < matrix.size(); r++) {
            for (int c = 0; c < matrix.get(0).length; c++) {
                if(matrix.get(r)[c]=='^'){
                    runPath(matrix,r,c,Direction.UP);
                }
            }
        }
        var res=0;
        for (int r = 0; r < matrix.size(); r++) {
            for (int c = 0; c < matrix.get(0).length; c++) {
                if(matrix.get(r)[c]=='X'){
                    res++;
                }
            }
        }
        return res;
    }

    private static void runPath(List<char[]> matrix, int r, int c, Direction direction) {
        if(r<0 || r>=matrix.size() || c<0 || c>=matrix.get(0).length)
            return ;

        if(matrix.get(r)[c]!='#')
            matrix.get(r)[c]='X';

        switch (direction) {
            case UP -> {
                if(matrix.get(r)[c]=='#')
                    runPath(matrix,r+1,c,Direction.RIGHT);
                else
                    runPath(matrix,r-1,c,Direction.UP);
            }
            case RIGHT -> {
                if(matrix.get(r)[c]=='#')
                    runPath(matrix,r,c-1,Direction.DOWN);
                else
                    runPath(matrix,r,c+1,Direction.RIGHT);
            }
            case DOWN -> {
                if(matrix.get(r)[c]=='#')
                    runPath(matrix,r-1,c,Direction.LEFT);
                else
                    runPath(matrix,r+1,c,Direction.DOWN);

            }
            case LEFT -> {
                if(matrix.get(r)[c]=='#')
                    runPath(matrix,r,c+1,Direction.UP);
                else
                    runPath(matrix,r,c-1,Direction.LEFT);
            }
            default -> throw new RuntimeException("Shouldn't reach here");
        }
    }
}
