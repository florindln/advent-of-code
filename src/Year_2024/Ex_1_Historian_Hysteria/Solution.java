package Year_2024.Ex_1_historian_hysteria;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;

public class Solution {
    public static void main(String[] args) throws FileNotFoundException {
        File input = new File("src/Year_2024/Ex_1_historian_hysteria/input.txt");
        Scanner myReader = new Scanner(input);

        var list1 = new ArrayList<Integer>();
        var list2 = new ArrayList<Integer>();

        while (myReader.hasNextLine()) {
            String line = myReader.nextLine();
            String[] parts = line.split("\\s+");
            list1.add(Integer.parseInt(parts[0]));
            list2.add(Integer.parseInt(parts[1]));
        }
        myReader.close();

        System.out.println("List 1: " + list1);
        System.out.println("List 2: " + list2);

        System.out.println("Result part 1: "+getDistance(list1,list2));
        System.out.println("Result part 2: "+getSimilarity(list1,list2));
    }

    private static int getSimilarity(List<Integer> list1, List<Integer> list2) {
        var occ = new HashMap<Integer, Integer>();
        for (var num : list2) {
            occ.put(num, occ.getOrDefault(num, 0) + 1);
        }

        var res = 0;
        for (var num : list1) {
            res += num * occ.getOrDefault(num, 0);
        }

        return res;
    }

    private static int getDistance(List<Integer> list1, List<Integer> list2) {
        var sorted1=list1.stream().sorted().toList();
        var sorted2=list2.stream().sorted().toList();

        var distance=0;
        for (int i = 0; i < sorted1.size(); i++) {
            distance+=Math.abs(sorted1.get(i)-sorted2.get(i));
        }

        return distance;
    }
}
