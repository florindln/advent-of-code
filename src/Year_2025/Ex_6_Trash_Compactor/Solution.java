package Year_2025.Ex_6_Trash_Compactor;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

public class Solution {
    record IdRange(Long start, Long end) {}
    record InputData(List<List<Integer>> lines, List<String> operations,int colSize,int rowSize){}

    public static InputData getInputData(String path) throws FileNotFoundException {
        File input = new File(path);
        Scanner myReader = new Scanner(input);

        ArrayList<List<String>> lines=new ArrayList<>();

        while (myReader.hasNextLine()) {
            String line = myReader.nextLine();

            var numbers= Arrays.stream(line.split(" ")).filter(val->!val.isEmpty()).toList();
            lines.add(numbers);
        }

        List<List<Integer>> numbersList=new ArrayList<>();

        for (int i = 0; i < lines.size()-1; i++) {
            numbersList=lines.subList(0,lines.size()-1).stream().map(list->list.stream().map(number->Integer.parseInt(number)).toList()).toList();
        }

        var operations=lines.get(lines.size()-1);

        myReader.close();
        return new InputData(numbersList,operations, numbersList.size(),operations.size());
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

    static void solutionPart2(InputData inputData){

    }
}
