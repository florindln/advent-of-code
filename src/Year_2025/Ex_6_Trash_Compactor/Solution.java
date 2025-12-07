package Year_2025.Ex_6_Trash_Compactor;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

public class Solution {
    record IdRange(Long start, Long end) {}
    record InputData(List<List<Integer>> lines, List<String> operations, int colSize, int rowSize,
                     List<String> rawLines){}

    public static InputData getInputData(String path) throws FileNotFoundException {
        File input = new File(path);
        Scanner myReader = new Scanner(input);

        ArrayList<List<String>> lines=new ArrayList<>();

        var rawLines=new ArrayList<String>();
        while (myReader.hasNextLine()) {
            String line = myReader.nextLine();
            rawLines.add(line);
            var numbers= Arrays.stream(line.split(" ")).filter(val->!val.isEmpty()).toList();
            lines.add(numbers);
        }

        List<List<Integer>> numbersList=new ArrayList<>();

        for (int i = 0; i < lines.size()-1; i++) {
            numbersList=lines.subList(0,lines.size()-1).stream().map(list->list.stream().map(number->Integer.parseInt(number)).toList()).toList();
        }

        var operations=lines.get(lines.size()-1);

        myReader.close();

        return new InputData(numbersList,operations, numbersList.size(),operations.size(),rawLines);
    }

    static long solutionPart1(InputData inputData){
        var results=new ArrayList<Long>();
        for (int i = 0; i < inputData.rowSize; i++) {
            var operation=inputData.operations.get(i);

            var column=new ArrayList<Integer>();
            for (int j = 0; j < inputData.colSize; j++) {
                column.add(inputData.lines.get(j).get(i));
            }

            switch (operation){
                case "+"->{
                    results.add(column.stream().mapToLong(a->a).sum());
                }
                case "*"->{
                    results.add(column.stream().mapToLong(a->a).reduce(1,(a,b)->a*b));
                }
                default -> throw new IllegalArgumentException("invalid operator");
            }
        }
        var res=results.stream().mapToLong(a->a).sum();
        return res;
    }

    static long solutionPart2(InputData inputData){
        var rawLines=inputData.rawLines;
        var rowLength=rawLines.get(0).length();

        List<List<Integer>> lines=new ArrayList<>();
        var line=new ArrayList<Integer>();
        for (int rowPos = 0; rowPos < rowLength; rowPos++) {
            var newNum="";
            for (int colPos = 0; colPos < inputData.colSize; colPos++) {
                var currentDigit=rawLines.get(colPos).charAt(rowPos);
                newNum+=currentDigit;
            }
            if (newNum.trim().isEmpty()){
                lines.add(line);
                line=new ArrayList<>();
                continue;
            }

            line.add(Integer.parseInt(newNum.trim()));
        }
        lines.add(line);

        var results=new ArrayList<Long>();

        for (int i = 0; i < inputData.operations.size(); i++) {
            var operation=inputData.operations.get(i);
            switch (operation){
                case "+"->{
                    results.add(lines.get(i).stream().mapToLong(a->a).sum());
                }
                case "*"->{
                    results.add(lines.get(i).stream().mapToLong(a->a).reduce(1,(a,b)->a*b));
                }
                default -> throw new IllegalArgumentException("invalid operator");
            }
        }

        var res=results.stream().mapToLong(a->a).sum();
        return res;
    }

//    static long solutionPart2(InputData inputData){
//        var results=new ArrayList<Long>();
//        for (int i = inputData.rowSize-1; i >=0; i--) {
//            var operation=inputData.operations.get(i);
//
//            var column=new ArrayList<Integer>();
//            for (int j = 0; j < inputData.colSize; j++) {
//                column.add(inputData.lines.get(j).get(i));
//            }
//
//            var columnCephalopodMath = mapToCephalopodMath(column);
//
//            switch (operation){
//                case "+"->{
//                    results.add(columnCephalopodMath.stream().mapToLong(a->a).sum());
//                }
//                case "*"->{
//                    results.add(columnCephalopodMath.stream().mapToLong(a->a).reduce(1,(a,b)->a*b));
//                }
//                default -> throw new IllegalArgumentException("invalid operator");
//            }
//        }
//        var res=results.stream().mapToLong(a->a).sum();
//        return res;
//    }

    static List<Integer> mapToCephalopodMath(ArrayList<Integer> column) {
        var largestNum=column.stream().max((o1, o2) -> o1-o2).get();
        var largestDigitSize=largestNum.toString().length();

        var res=new ArrayList<Integer>();
        for (int i = 0; i < largestDigitSize; i++) {
            var newNum="";
            for (var num:column){
                if(i>=num.toString().length())
                    continue;

                var digit=num.toString().charAt(i);
                newNum+=digit;
            }
            res.add(Integer.parseInt(newNum));
        }

        return res;
    }
}
