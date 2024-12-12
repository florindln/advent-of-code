package Year_2024.Ex_3_Mull_It_Over;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Optional;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Solution {
    public static void main(String[] args) throws FileNotFoundException {
        File input = new File("src/Year_2024/Ex_3_Mull_It_Over/input.txt");
        Scanner myReader = new Scanner(input);

        StringBuilder jumbled = new StringBuilder();

        while (myReader.hasNextLine()) {
            String line = myReader.nextLine();
            jumbled.append(line);

        }
        myReader.close();

        System.out.println("Result part 1: "+ getCalc(jumbled.toString()));
        System.out.println("Result part 2: "+getCalcDoDont(jumbled.toString()));
    }

    private static int getCalcDoDont(String jumbledString) {
        var res=0;
        //for this we do a similar thing as before but we search to the left of a substring like mul(1,1) for dont() or do(). if we find it we skip that part
        String pattern = "mul\\(\\d+,\\d+\\)";
        Pattern compiledPattern = Pattern.compile(pattern);
        Matcher matcher = compiledPattern.matcher(jumbledString);

        while (matcher.find()) {
//            var sub=jumbledString.substring(matcher.start(),matcher.end());
//            System.out.println(sub);

            var skip=false;
            //keep going left of the index until we find do or dont
            for (int i = matcher.start(); i >=0 ; i--) {
                var substringLeft=jumbledString.substring(i,matcher.start());
                if (substringLeft.contains("don't()")){
                    skip=true;
                    break;
                } else if (substringLeft.contains("do()")) {
                    break;
                }
            }
            if(skip)
                continue;

            //in the correct form like mul(6,11135)
            var found= matcher.group();

            var numbersWithComma=found.substring(4,found.length()-1);
            var numsArr=numbersWithComma.split(",");
            var num1=Integer.parseInt(numsArr[0]);
            var num2=Integer.parseInt(numsArr[1]);

            var multiplication=num1*num2;

            res+=multiplication;
        }

        return res;
    }


        private static int getCalc(String jumbledString) {
        var res=0;

        String pattern = "mul\\(\\d+,\\d+\\)";
        Pattern compiledPattern = Pattern.compile(pattern);
        Matcher matcher = compiledPattern.matcher(jumbledString);

        while (matcher.find()) {
            //in the correct form like mul(6,11135)
            var found= matcher.group();

            var numbersWithComma=found.substring(4,found.length()-1);
//            System.out.println(numbersWithComma);
            var numsArr=numbersWithComma.split(",");
            var num1=Integer.parseInt(numsArr[0]);
            var num2=Integer.parseInt(numsArr[1]);

            var multiplication=num1*num2;

            res+=multiplication;
        }

        return res;
    }

}
