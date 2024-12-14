package Year_2024.Ex_8_Resonant_Collinearity;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

record Node(char symbol,boolean isAntinode,int row,int col){
    @Override
    public String toString(){
        if(isAntinode)
            return "#";
        return String.valueOf(symbol);
    }
}

interface ExtendStrategy{
    void extend(List<Node[]> matrix, Node node);
}

record NormalExtendStrategy() implements ExtendStrategy{
    @Override
    public void extend(List<Node[]> matrix, Node node) {
        for (int r = node.row(); r < matrix.size(); r++) {
            for (int c = 0; c < matrix.get(0).length; c++) {
                var curr=matrix.get(r)[c];
                if(curr.symbol()==node.symbol() && (r>node.row() || c>node.col())){
                    var rowDistance=Math.abs(node.row()-r);
                    var colDistance=Math.abs(node.col()-c);
                    Solution.addAntinodes(matrix,node,curr,rowDistance,colDistance);
                }
            }
        }
    }
}

record RecursiveExtendStrategy() implements ExtendStrategy{
    @Override
    public void extend(List<Node[]> matrix, Node node) {
        for (int r = node.row(); r < matrix.size(); r++) {
            for (int c = 0; c < matrix.get(0).length; c++) {
                var curr=matrix.get(r)[c];
                if(curr.symbol()==node.symbol() && (r>node.row() || c>node.col())){
                    //currents also become antinodes in this case
                    matrix.get(r)[c]=new Node(curr.symbol(),true,curr.row(),curr.col());
                    matrix.get(node.row())[node.col()]=new Node(node.symbol(),true,node.row(),node.col());

                    //recursively adding part
                    var initialRowDistance=Math.abs(node.row()-r);
                    var initialColDistance=Math.abs(node.col()-c);
                    var rowDistance=initialRowDistance;
                    var colDistance=initialColDistance;
                    while (rowDistance<matrix.size() || colDistance<matrix.get(0).length){
                        Solution.addAntinodes(matrix,node,curr,rowDistance,colDistance);
                        rowDistance+=initialRowDistance;
                        colDistance+=initialColDistance;
                    }
                }
            }
        }
    }
}

public class Solution {
    public static void main(String[] args) throws FileNotFoundException {
        File input = new File("src/Year_2024/Ex_8_Resonant_Collinearity/input.txt");
        Scanner myReader = new Scanner(input);

        List<Node[]> matrix1=new ArrayList<>();
        List<Node[]> matrix2=new ArrayList<>();
        var row=0;
        while (myReader.hasNextLine()) {
            String line = myReader.nextLine();
            var charArr=line.toCharArray();
            Node[] nodes1=new Node[charArr.length];
            Node[] nodes2=new Node[charArr.length];
            for (int i = 0; i <charArr.length; i++) {
                nodes1[i]=new Node(charArr[i],false,row,i);
                nodes2[i]=new Node(charArr[i],false,row,i);
            }
            matrix1.add(nodes1);
            matrix2.add(nodes2);
            row++;
        }
        myReader.close();

//        for (var arr:matrix1)
//            System.out.println(Arrays.toString(arr).replace(",",""));

        System.out.println("Result part 1: "+ getAntinodeSum(matrix1,new NormalExtendStrategy()));
        System.out.println("Result part 2: "+getAntinodeSum(matrix2,new RecursiveExtendStrategy()));

//        for (var arr:matrix2)
//            System.out.println(Arrays.toString(arr).replace(",",""));

    }

    private static int getAntinodeSum(List<Node[]> matrix,ExtendStrategy extendStrategy) {
        //to get the location of an antinode based on the location of 2 nodes we do the following:
        //get the distance between 2 nodes in rows and cols, and extend those 2 nodes with 2 more by adding/subtracting the distance
        //do this per symbol, from left to right, up to down

        var allSymbols = new HashSet<Character>();
        for (int r = 0; r < matrix.size(); r++) {
            for (int c = 0; c < matrix.get(0).length; c++) {
                var curr = matrix.get(r)[c];
                if (curr.symbol() != '.')
                    allSymbols.add(curr.symbol());
            }
        }

//        char symbol='0';
        for (var symbol : allSymbols){
            for (int r = 0; r < matrix.size(); r++) {
                for (int c = 0; c < matrix.get(0).length; c++) {
                    var curr = matrix.get(r)[c];
                    if (curr.symbol() == symbol) {
                        extendStrategy.extend(matrix, curr);
                    }
                }
            }
        }

        var res=0;
        for (int r = 0; r < matrix.size(); r++) {
            for (int c = 0; c < matrix.get(0).length; c++) {
                var curr = matrix.get(r)[c];
                if (curr.isAntinode())
                    res++;
            }
        }

        return res;
    }

    static void addAntinodes(List<Node[]> matrix, Node top, Node bot, int rowDistance, int colDistance) {
        var antinodeTopRow=top.row()-rowDistance;
        var antinodeTopCol=top.col()<bot.col()?top.col()-colDistance:top.col()+colDistance;
        var antinodeBotRow=bot.row()+rowDistance;
        var antinodeBotCol=top.col()<bot.col()?bot.col()+colDistance:bot.col()-colDistance;

        for (int r = 0; r < matrix.size(); r++) {
            for (int c = 0; c < matrix.get(0).length; c++) {
                if(r==antinodeTopRow && c==antinodeTopCol || r==antinodeBotRow && c==antinodeBotCol){
                    var existing= matrix.get(r)[c];
                    matrix.get(r)[c]=new Node(existing.symbol(),true,existing.row(),existing.col());
                }
            }
        }
    }
}
