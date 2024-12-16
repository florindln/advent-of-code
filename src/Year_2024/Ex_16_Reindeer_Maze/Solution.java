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

record Pair<T,R>(T left,R right){}

public class Solution {
    int globalMinScore=Integer.MAX_VALUE;

    List<Pair<boolean[][],Integer>> allVisited=new ArrayList<>();

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

        var res2Visited=new boolean[matrix.size()][matrix.get(0).length];
        for (var pair:solution.allVisited){
            if(pair.right()==solution.globalMinScore){
                //matrice is one of the optimal paths, overlap the true values with our result
                for (int i = 0; i < pair.left().length; i++) {
                    for (int j = 0; j < pair.left()[0].length; j++) {
                        if(pair.left()[i][j])
                            res2Visited[i][j]=true;
                    }
                }
            }

        }
        System.out.println("Result part 2: "+(getVisitedCells(res2Visited)+1));

//        System.out.println(printVisited(res2Visited));
//        solution.allVisited.forEach(a-> System.out.println(printVisited(a.left())));
    }

    public static String printVisited(boolean[][] visited){
        var res="";
        for (int i = 0; i < visited.length; i++) {
            for (int j = 0; j < visited[0].length; j++) {
                res+=visited[i][j]?"X":".";
            }
            res+="\n";
        }
        return res;
    }

    private int getLowestScoreThroughMaze(List<char[]> matrix) {
        for (int r = 0; r < matrix.size(); r++) {
            for (int c = 0; c < matrix.get(0).length; c++) {
                if(matrix.get(r)[c]=='S'){
                    var visited=new boolean[matrix.size()][matrix.get(0).length];
                    var optimalPath=new ArrayList<Direction>();
                    var res= recurseMaze(matrix,r,c,0,Direction.RIGHT,visited,new ArrayList<>(),optimalPath,new HashMap<>());

//                    System.out.println("size optimal end "+optimalPath.size()+" "+optimalPath);

                    return res;
                }
            }
        }
        return 0;
    }

    private int recurseMaze(List<char[]> matrix, int r, int c, int score, Direction prevDir, boolean[][] visited, List<Direction> path, List<Direction> optimalPath, Map<String,Integer> memo) {
//        var visString=printVisited(visited);
        if(matrix.get(r)[c]=='#' || visited[r][c]|| score>globalMinScore)
            return Integer.MAX_VALUE;

        if(matrix.get(r)[c]=='E'){
            globalMinScore = score;
            optimalPath.clear();
            optimalPath.addAll(path);

            //need a deep copy of visited
            var newVisited=new boolean[visited.length][visited[0].length];
            for (int i = 0; i < visited.length; i++) {
                for (int j = 0; j < visited[0].length; j++) {
                    newVisited[i][j]=visited[i][j];
                }
            }
            allVisited.add(new Pair<>(newVisited,score));
            return score;
        }

        var state=r+" "+c+" "+prevDir+" "+score;
        //additional check for max value otherwise we will memoize the optimal path and return early even though there could be multiple optimal paths
        if(memo.containsKey(state) &&memo.get(state)==Integer.MAX_VALUE)
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

    private static int getVisitedCells(boolean[][] visited) {
        var numVisited=0;
        for (int i = 0; i < visited.length; i++) {
            for (int j = 0; j < visited[0].length; j++) {
                if(visited[i][j])
                    numVisited++;
            }
        }
        return numVisited;
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
