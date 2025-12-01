package Year_2025.Ex_1_Secret_Entrance;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Solution {
    public static void main(String[] args) throws FileNotFoundException {
    }
    enum Rotation { LEFT,RIGHT}
    record Turn(Rotation rotation, Integer amount) {    }
    public static List<Turn> getInputData(String path) throws FileNotFoundException {
        File input = new File(path);
        Scanner myReader = new Scanner(input);

        var list1 = new ArrayList<Turn>();

        while (myReader.hasNextLine()) {
            String line = myReader.nextLine();
            Rotation rotation = switch(line.charAt(0)){
                case 'L' -> Rotation.LEFT;
                case 'R' -> Rotation.RIGHT;
                default -> throw new IllegalArgumentException("Invalid rotation: " + line);
            };
            Integer amount=Integer.parseInt(line.substring(1));
            list1.add(new Turn(rotation,amount));
        }
        myReader.close();
        return list1;
    }

    static int solutionPart1(List<Turn> turns){
        int res=0;

        int start=50;

        //we can use the modulo %100 to get both the overflow past 100 and underflow below 0

        for (var turn:turns){
            switch (turn.rotation) {
                case LEFT -> {
                    start=(start-turn.amount)%100;
                }
                case RIGHT -> {
                    start=(start+turn.amount)%100;
                }
            }
            if(start==0)
                res++;
        }

        return res;
    }

    static int solutionPart2(List<Turn> turns){
        int res=0;
        int start=50;

        for (var turn:turns){
            var multiplier=turn.amount/100;
            res+=multiplier;
            var amount=turn.amount%100;

            switch (turn.rotation) {
                case LEFT -> {
                    var newStart=start-amount;
                    if (start!=0&&newStart<=0)
                        res++;
                    start=Math.floorMod(newStart,100);
                }
                case RIGHT -> {
                    var newStart=start+amount;
                    if (start!=0&&newStart>=100)
                        res++;
                    start=Math.floorMod(newStart,100);
                }
            }
        }

        return res;
    }
}
