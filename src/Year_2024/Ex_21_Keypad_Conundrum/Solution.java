package Year_2024.Ex_21_Keypad_Conundrum;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

record Position(int r,int c){}
record Pair<T,R>(T left,R right){}
record Triple<T,R,I>(T left,R mid,I right){}
record Node(int r,int c,char symbol){}
record Numpad(){
    static char[][] numpadMatrix = {
            {'7', '8', '9'},
            {'4', '5', '6'},
            {'1', '2', '3'},
            {'x', '0', 'A'}
    };
}
record Dirpad() {
    static char[][] dirpadMatrix = {
            {'x', '^', 'A'},
            {'<', 'v', '>'},
    };
}

public class Solution {
    //the direction order is important becasue our holes are in the bottom left and top left, so we should prefer to go lastly left and right first
    public static final int[][] DIRS= {{0, 1},{-1, 0},{1, 0},{0, -1}};
    public static char dirToMove(int[] dir){
        if(dir[0]==-1 && dir[1]==0)
            return '^';
        if(dir[0]==1 && dir[1]==0)
            return 'v';
        if(dir[0]==0 && dir[1]==-1)
            return '<';
        if(dir[0]==0 && dir[1]==1)
            return '>';

        throw new IllegalStateException("Unexpected value: " + Arrays.toString(dir));
    }

    public static void main(String[] args) throws FileNotFoundException {
        File input = new File("src/Year_2024/Ex_21_Keypad_Conundrum/input_test.txt");
        Scanner myReader = new Scanner(input);

        List<String> codes=new ArrayList<>();

        while (myReader.hasNextLine()) {
            String line = myReader.nextLine();
            codes.add(line);
        }
        myReader.close();

//        System.out.println(getMoves('A','1',Numpad.numpadMatrix));
//        System.out.println(getMoves('A','9',Numpad.numpadMatrix));
//        System.out.println(getMoves('1','6',Numpad.numpadMatrix));
//        System.out.println(getMoves('0','1',Numpad.numpadMatrix));
//        System.out.println(getMoves('0','7',Numpad.numpadMatrix));
//        System.out.println();
//        System.out.println(getMoves('A','v',Dirpad.dirpadMatrix));
//        System.out.println(getMoves('A','<',Dirpad.dirpadMatrix));
//        System.out.println(getMoves('<','^',Dirpad.dirpadMatrix));
//        System.out.println(getMoves('^','<',Dirpad.dirpadMatrix));

//        System.out.println(codes);

        var testNeeded="029A";
        var test1= getMovesByMatrix(testNeeded,Numpad.numpadMatrix);
        if(test1.length()!=12)
            throw new RuntimeException("test1 is incorrect , len should be 12, but is: "+test1.length());

        var test2= getMovesByMatrix(test1,Dirpad.dirpadMatrix);
        if(test2.length()!=28 )
            throw new RuntimeException("test2 is incorrect, len should be 28, but is "+test2.length());

        var test3=getMovesByMatrix(test2,Dirpad.dirpadMatrix);
        if(test3.length()!=68)
            throw new RuntimeException("test3 is incorrect, len should be 68, but is "+test3.length());

        var res1=getComplexityOfAllCodes(codes);
        System.out.println("Result part 1: "+ res1);


    }

    private static long getComplexityOfAllCodes(List<String> codes) {
        var res=0L;

        for (var code:codes){
            var moves=runAllMovesForACode(code);
            var builder=new StringBuilder();
            for (var ch:code.toCharArray()){
                if(!Character.isDigit(ch))
                    continue;
                builder.append(ch);
            }
            var numericPart=Integer.parseInt(builder.toString());
            var moveLen=moves.length();
            long complexity= (long) moveLen *numericPart;
            res+=complexity;
        }

        return res;
    }

    private static String runAllMovesForACode(String needed) {
        var res1= getMovesByMatrix(needed,Numpad.numpadMatrix);
        var res2= getMovesByMatrix(res1,Dirpad.dirpadMatrix);
        var res3=getMovesByMatrix(res2,Dirpad.dirpadMatrix);
        return res3;
    }

    private static String getMovesByMatrix(String needed, char[][] matrix) {
        var res=new StringBuilder();
        //need to append an A at the beginning since we always start there
        var newNeeded="A"+needed;
        var neededArr= newNeeded.toCharArray();
        for (int i = 0; i < neededArr.length-1; i++) {
            var curr=neededArr[i];
            var next=neededArr[i+1];
            var moves=getMoves(curr,next,matrix);
            moves.add('A');
            for (var move:moves)
                res.append(move);
        }
        return res.toString();
    }


    static List<Character> getMoves(char from, char to,char[][] matrix){
        var rowLen=matrix.length;
        var colLen=matrix[0].length;
        //current node,previous node, the direction we took
        Queue<Triple<Node,Character,Character>> q=new LinkedList<>();
        var fullList=new ArrayList<Triple<Node,Character,Character>>();

        for (int r = 0; r <rowLen ; r++) {
            for (int c = 0; c < colLen; c++) {
                var curr=matrix[r][c];
                if(curr==from){
                    var initial=new Triple<>(new Node(r,c,curr),'k','k');
                    q.add(initial);
                    fullList.add(initial);
                }
            }
        }

        var visited=new boolean[rowLen][colLen];
        Triple<Node, Character, Character> lastNode=null;
        while (!q.isEmpty()){
            var curr=q.poll();
            var r=curr.left().r();
            var c=curr.left().c();

            if(curr.left().symbol()==to){
                lastNode=curr;
                break;
            }

            //todo here instead of going from the beginning dir, we need to attempt to go in the same dir then loop over the dirs
            int[][] dirsInOrder=getDirsInOrder(curr.right(),Solution.DIRS);
            for (var dir:dirsInOrder){
                var dirSymbol=Solution.dirToMove(dir);
                var newR=r+dir[0];
                var newC=c+dir[1];
                if(!Solution.isValid(matrix,newR,newC) || visited[newR][newC] )
                    continue;

                var next=new Triple<>(new Node(newR,newC,matrix[newR][newC]),curr.left().symbol(),dirSymbol);
                visited[newR][newC]=true;
                q.add(next);
                fullList.add(next);
            }
        }

        var path=new ArrayList<Character>();
        while(true){
            path.add(lastNode.right());

            if(lastNode.mid()=='k'){
                break;
            }

            Triple<Node, Character, Character> finalLastNode = lastNode;
            lastNode=fullList.stream().filter(elem->elem.left().symbol()== finalLastNode.mid()).findFirst().get();
        }
        path.removeLast();
        return path.reversed();
    }

    private static int[][] getDirsInOrder(Character prevDir, int[][] dirs) {
        int[] foundDir=null;
        for (var dir:dirs){
            var dirToMove=Solution.dirToMove(dir);
            if(dirToMove==prevDir)
                foundDir=dir;
        }

        if (foundDir==null)
            return dirs;

        int[][] res=new int[dirs.length][dirs[0].length];
        res[0]=foundDir;
        var count=1;
        for (var dir:dirs){
            if(dir[0]==foundDir[0] && dir[1]==foundDir[1])
                continue;
            res[count++]=dir;
        }
        return res;
    }

//    static int getPicoSecBFS(List<char[]> originalMatrix,List<Pair<Position,Integer>> basePath){
//        var rowLen=originalMatrix.size();
//        var colLen=originalMatrix.get(0).length;
//
//        //the pair is the node position and the distance form the start
//        Queue<Pair<Position,Integer>> q=new LinkedList<>();
//
//        //queue the start
//        for (int r = 0; r < rowLen; r++) {
//            for (int c = 0; c < colLen; c++) {
//                if(originalMatrix.get(r)[c]=='S')
//                    q.add(new Pair<>(new Position(r,c),0));
//            }
//        }
//
//        var matrix=deepCopyMatrix(originalMatrix);
//
//        while (!q.isEmpty()){
//            var curr=q.poll();
//            var dist=curr.right();
//            var r=curr.left().r();
//            var c=curr.left().c();
//
//            basePath.add(curr);
//
//            if(matrix.get(r)[c]=='E')
//                return dist;
//
//            matrix.get(r)[c]='O';
//
//            for(var dir:Solution.DIRS){
//                var newR=r+dir[0];
//                var newC=c+dir[1];
//                if (isValid(matrix,newR,newC)){
////                    matrix.get(newR)[newC]='O';
//                    q.add(new Pair<>(new Position(newR,newC),dist+1));
//                }
//            }
//        }
////        for (var arr:matrix)
////            System.out.println(arr);
//
//        throw new RuntimeException("Ending should be reached");
//    }

    static boolean isValid(char[][] matrix,int r,int c){
        if(r<0||r>=matrix.length || c<0||c>=matrix[0].length)
            return false;

        var curr=matrix[r][c];
        if(curr=='x')
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
