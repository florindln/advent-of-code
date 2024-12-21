package Year_2024.Ex_21_Keypad_Conundrum;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

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
    public static final int[][] DIRS= {{0, 1},{-1, 0},{1, 0},{0, -1}};
    public static final int[][][] DIRS_PERMUTATIONS={
            {{0, 1}, {-1, 0}, {1, 0}, {0, -1}},
            {{0, 1}, {-1, 0}, {0, -1}, {1, 0}},
            {{0, 1}, {1, 0}, {-1, 0}, {0, -1}},
            {{0, 1}, {1, 0}, {0, -1}, {-1, 0}},
            {{0, 1}, {0, -1}, {-1, 0}, {1, 0}},
            {{0, 1}, {0, -1}, {1, 0}, {-1, 0}},
            {{-1, 0}, {0, 1}, {1, 0}, {0, -1}},
            {{-1, 0}, {0, 1}, {0, -1}, {1, 0}},
            {{-1, 0}, {1, 0}, {0, 1}, {0, -1}},
            {{-1, 0}, {1, 0}, {0, -1}, {0, 1}},
            {{-1, 0}, {0, -1}, {0, 1}, {1, 0}},
            {{-1, 0}, {0, -1}, {1, 0}, {0, 1}},
            {{1, 0}, {0, 1}, {-1, 0}, {0, -1}},
            {{1, 0}, {0, 1}, {0, -1}, {-1, 0}},
            {{1, 0}, {-1, 0}, {0, 1}, {0, -1}},
            {{1, 0}, {-1, 0}, {0, -1}, {0, 1}},
            {{1, 0}, {0, -1}, {0, 1}, {-1, 0}},
            {{1, 0}, {0, -1}, {-1, 0}, {0, 1}},
            {{0, -1}, {0, 1}, {-1, 0}, {1, 0}},
            {{0, -1}, {0, 1}, {1, 0}, {-1, 0}},
            {{0, -1}, {-1, 0}, {0, 1}, {1, 0}},
            {{0, -1}, {-1, 0}, {1, 0}, {0, 1}},
            {{0, -1}, {1, 0}, {0, 1}, {-1, 0}},
            {{0, -1}, {1, 0}, {-1, 0}, {0, 1}},
    };
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
        File input = new File("src/Year_2024/Ex_21_Keypad_Conundrum/input.txt");
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
        var test1= getPossibleMovesByMatrix(testNeeded,Numpad.numpadMatrix);
        var minTest1=test1.stream().min(Comparator.comparingInt(String::length)).orElse("");
        if(minTest1.length()!=12)
            throw new RuntimeException("test1 is incorrect , len should be 12, but is: "+minTest1.length());

//        var test2= getPossibleMovesByMatrix(test1,Dirpad.dirpadMatrix);
//        if(test2.length()!=28 )
//            throw new RuntimeException("test2 is incorrect, len should be 28, but is "+test2.length());
//
//        var test3=getPossibleMovesByMatrix(test2,Dirpad.dirpadMatrix);
//        if(test3.length()!=68)
//            throw new RuntimeException("test3 is incorrect, len should be 68, but is "+test3.length());

//        var t=runAllMovesForACode("379A");
//        var q=runAllMovesForACode("029A");

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
        var run1= getPossibleMovesByMatrix(needed,Numpad.numpadMatrix);

        var run2=new ArrayList<String>();
        for(var tempRes:run1){
            var temp=getPossibleMovesByMatrix(tempRes,Dirpad.dirpadMatrix);
            run2.addAll(temp);
        }

        var run3=new ArrayList<String>();
        for(var tempRes:run2){
            var temp=getPossibleMovesByMatrix(tempRes,Dirpad.dirpadMatrix);
            run3.addAll(temp);
        }
        var res=run3.stream().min(Comparator.comparingInt(String::length)).orElse("");

        return res;
    }

    private static List<String> getPossibleMovesByMatrix(String needed, char[][] matrix) {
        var possibilitiesFromOneStepToAnother = new ArrayList<Set<String>>();
        //need to append an A at the beginning since we always start there
        var newNeeded = "A" + needed;
        var neededArr = newNeeded.toCharArray();
        for (int i = 0; i < neededArr.length - 1; i++) {
            var curr = neededArr[i];
            var next = neededArr[i + 1];

            var allWaysFromCurrToNext=new HashSet<String>();
            for (var dirs : Solution.DIRS_PERMUTATIONS) {
                var moves = getMoves(curr, next, matrix, dirs);
                var currentString = new StringBuilder();
                moves.add('A');
                for (var move : moves)
                    currentString.append(move);

                allWaysFromCurrToNext.add(currentString.toString());
            }
            possibilitiesFromOneStepToAnother.add(allWaysFromCurrToNext);
        }

        var results = concatenateAll(possibilitiesFromOneStepToAnother, 0, "");

        return results;
    }

    static List<String> concatenateAll(List<Set<String>> sets, int index, String current) {
        List<String> results = new ArrayList<>();
        if (index == sets.size()) {
            results.add(current);
            return results;
        }

        for (var str : sets.get(index)) {
            results.addAll(concatenateAll(sets, index + 1, current + str));
        }

        return results;
    }


    static List<Character> getMoves(char from, char to,char[][] matrix,int[][] dirs){
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

            int[][] dirsInOrder=getDirsInOrder(curr.right(),dirs);
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

    static boolean isValid(char[][] matrix,int r,int c){
        if(r<0||r>=matrix.length || c<0||c>=matrix[0].length)
            return false;

        var curr=matrix[r][c];
        if(curr=='x')
            return false;

        return true;
    }
}
