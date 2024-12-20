package Year_2024.Ex_20_Race_Condition;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

record Position(int r,int c){}
record Pair<T,R>(T left,R right){}
public class Solution {
    public static void main(String[] args) throws FileNotFoundException {
        File input = new File("src/Year_2024/Ex_20_Race_Condition/input.txt");
        Scanner myReader = new Scanner(input);

        List<char[]> matrix=new ArrayList<>();

        while (myReader.hasNextLine()) {
            String line = myReader.nextLine();
            matrix.add(line.toCharArray());
        }
        myReader.close();

//        for (var arr:matrix)
//            System.out.println(arr);


        List<Pair<Position,Integer>> basePath=new ArrayList<>();
        int res1 = getPicoSecBFS(matrix,basePath);
        var savings = getAllSavings(matrix, res1,new ArrayList<>());
        var savingsTestOther =getSavingsWithMaxDistance(matrix,res1,basePath,2);
        if(!savings.stream().sorted().toList().equals(savingsTestOther.stream().sorted().toList()))
            throw new RuntimeException("savings using both methods should be equal");

        System.out.println("Result part 1: "+ savings.stream().filter(s->s>=100).count());
//        System.out.println(savings.stream().sorted().toList());

        //for part 2 we can't just randomly recurse up to 20 times, checking each time if replacing a wall with space works, because we would have trillions
        //but we can iterate over the base path with 2 for loops, and for each calculate the manhattan distance. if it's smaller than 20, we can take the amount of elements skipped as saved distance
        var savings2=getSavingsWithMaxDistance(matrix,res1,basePath,20);
        System.out.println("Result part 2: "+ savings2.stream().filter(s->s>=100).count());
//        System.out.println(savings2.stream().sorted().toList());

//        boolean containsAll = new HashSet<>(savings2).containsAll(savings);
//        System.out.println("savings2 contains all elements of savings: " + containsAll);
    }

    static List<Integer> getSavingsWithMaxDistance(List<char[]> matrix,int res1,List<Pair<Position,Integer>> basePath,int maxCheat){
        var savings=new ArrayList<Integer>();
        for (int i = 0; i < basePath.size(); i++) {
            for (int j = i; j < basePath.size(); j++) {
                var iPath=basePath.get(i);
                var jPath=basePath.get(j);
                var manhattanDistance=calculateManhattanDistance(iPath.left().c(),iPath.left().r(),jPath.left().c(),jPath.left().r());
                if (manhattanDistance<=maxCheat){
                    var skipped=j-i-manhattanDistance;
                    if (skipped>0)
                        savings.add(skipped);
                }
            }
        }
        return savings;
    }

    static int calculateManhattanDistance(int startX, int startY, int endX, int endY) {
        return Math.abs(startX - endX) + Math.abs(startY - endY);
    }

    private static List<Integer> getAllSavings(List<char[]> matrix, int res1,List<Pair<Position,Integer>> basePath) {
        var savings=new ArrayList<Integer>();
        //simulate a cheat at every possible wall
        for (int r = 0; r < matrix.size(); r++) {
            for (int c = 0; c < matrix.get(0).length; c++) {
                if(matrix.get(r)[c]=='#'){
                    matrix.get(r)[c]='.';
                    int saving = res1 - getPicoSecBFS(matrix,basePath);
                    if(saving>0)
                        savings.add(saving);
                    matrix.get(r)[c]='#';
                }
            }
        }
//        System.out.println(savings);
        return savings;
    }

    static int getPicoSecBFS(List<char[]> originalMatrix,List<Pair<Position,Integer>> basePath){
        int[][] dirs = {{-1, 0},{1, 0},{0, -1},{0, 1}};
        var rowLen=originalMatrix.size();
        var colLen=originalMatrix.get(0).length;

        //the pair is the node position and the distance form the start
        Queue<Pair<Position,Integer>> q=new LinkedList<>();

        //queue the start
        for (int r = 0; r < rowLen; r++) {
            for (int c = 0; c < colLen; c++) {
                if(originalMatrix.get(r)[c]=='S')
                    q.add(new Pair<>(new Position(r,c),0));
            }
        }

        var matrix=deepCopyMatrix(originalMatrix);

        while (!q.isEmpty()){
            var curr=q.poll();
            var dist=curr.right();
            var r=curr.left().r();
            var c=curr.left().c();

            basePath.add(curr);

            if(matrix.get(r)[c]=='E')
                return dist;

            matrix.get(r)[c]='O';

            for(var dir:dirs){
                var newR=r+dir[0];
                var newC=c+dir[1];
                if (isValid(matrix,newR,newC)){
//                    matrix.get(newR)[newC]='O';
                    q.add(new Pair<>(new Position(newR,newC),dist+1));
                }
            }
        }
//        for (var arr:matrix)
//            System.out.println(arr);

        throw new RuntimeException("Ending should be reached");
    }

    static boolean isValid(List<char[]> matrix,int r,int c){
        if(r<0||r>=matrix.size() || c<0||c>=matrix.get(0).length)
            return false;

        var curr=matrix.get(r)[c];
        if(curr=='O' || curr=='#')
            return false;

        return true;
    }


    static List<char[]> deepCopyMatrix(List<char[]> original) {
        List<char[]> copy = new ArrayList<>();
        for (char[] row : original) {
            char[] newRow = new char[row.length];
            System.arraycopy(row, 0, newRow, 0, row.length);
            copy.add(newRow);
        }
        return copy;
    }
}
