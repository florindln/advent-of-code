package Year_2024.Ex_16_Reindeer_Maze;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

enum Direction{
    UP,
    RIGHT,
    DOWN,
    LEFT
}

record Position(int r,int c){}


public class Solution {
    int globalMinScore;

    public static void main(String[] args) throws FileNotFoundException {
        File input = new File("src/Year_2024/Ex_16_Reindeer_Maze/input.txt");
        Scanner myReader = new Scanner(input);

        ArrayList<char[]> matrix=new ArrayList<>();

        while (myReader.hasNextLine()) {
            String line = myReader.nextLine();
            matrix.add(line.toCharArray());
        }
        myReader.close();

//        for (var arr:matrix)
//            System.out.println(arr);

        Solution solution=new Solution();

        System.out.println("Result part 1: "+ solution.getLowestScoreThroughMaze(matrix));
//        System.out.println("Result part 2: "+res2);
    }

    private int getLowestScoreThroughMaze(List<char[]> matrix) {
        for (int r = 0; r < matrix.size(); r++) {
            for (int c = 0; c < matrix.get(0).length; c++) {
                if(matrix.get(r)[c]=='S'){
                    var visited=new boolean[matrix.size()][matrix.get(0).length];
                    globalMinScore = Integer.MAX_VALUE; // Initialize global score
                    var optimalPath=new ArrayList<Direction>();
                    var res= recurseMaze(matrix,r,c,0,Direction.RIGHT,visited,new ArrayList<>(),optimalPath,new HashMap<>());

                    System.out.println(optimalPath);

                    return res;
                }
            }
        }
        return 0;
    }

    private int recurseMaze(List<char[]> matrix, int r, int c, int score, Direction prevDir, boolean[][] visited, List<Direction> path, List<Direction> optimalPath, Map<String,Integer> memo) {
        if(matrix.get(r)[c]=='#' || visited[r][c]|| score>globalMinScore)
            return Integer.MAX_VALUE;

        if(matrix.get(r)[c]=='E'){
            if (score < globalMinScore) {
                globalMinScore = score;
                optimalPath.clear();
                optimalPath.addAll(path);
            }
            return score;
        }

        var state=r+" "+c+" "+prevDir+" "+score;
        if(memo.containsKey(state))
            return memo.get(state);

        visited[r][c]=true;

        var res=Integer.MAX_VALUE;
        for (var dir:Direction.values()){
            var oppositeOfPrevious= getOppositeDir(prevDir);
            var newPos= getNewPosition(dir,r,c);
            path.add(dir);
            if(dir.equals(prevDir)){
                res=Math.min(res,recurseMaze(matrix,newPos.r(),newPos.c(),score+1,dir, visited,path,optimalPath,memo));
            }
            else if (dir.equals(oppositeOfPrevious)) {
                res=Math.min(res,recurseMaze(matrix,newPos.r(),newPos.c(),score+2001,dir, visited,path,optimalPath,memo));
            }
            else {
                res=Math.min(res,recurseMaze(matrix,newPos.r(),newPos.c(),score+1001,dir, visited,path,optimalPath,memo));
            }
            path.removeLast();
        }

        visited[r][c]=false;

        memo.put(state,res);
        return res;
    }

    private static Position getNewPosition(Direction dir, int r, int c) {
        return switch (dir) {
            case UP -> new Position(r-1,c);
            case RIGHT -> new Position(r,c+1);
            case DOWN -> new Position(r+1,c);
            case LEFT -> new Position(r,c-1);
            default -> throw new RuntimeException("Direction doesnt exist");
        };
    }

    private static Direction getOppositeDir(Direction dir) {
        return switch (dir) {
            case UP -> Direction.DOWN;
            case RIGHT -> Direction.LEFT;
            case DOWN -> Direction.UP;
            case LEFT -> Direction.RIGHT;
            default -> throw new RuntimeException("Direction doesnt exist");
        };
    }


}
