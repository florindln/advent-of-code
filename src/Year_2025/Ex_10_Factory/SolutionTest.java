package Year_2025.Ex_10_Factory;


import org.junit.jupiter.api.Test;

import java.io.FileNotFoundException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertIterableEquals;


class SolutionTest {
    @Test
    void getInputData() throws FileNotFoundException {
        var dataTest= Solution.getInputData("src/Year_2025/Ex_10_Factory/inputExample.txt");
        var first=dataTest.inputRows().get(0);
        assertIterableEquals(List.of(false,true,true,false),first.lights());
        assertEquals(3,first.buttons().get(0).toggledLights().get(0));
        assertEquals(1,first.buttons().get(1).toggledLights().get(0));
        assertEquals(3,first.buttons().get(1).toggledLights().get(1));
        assertIterableEquals(List.of(3,5,4,7),first.joltages());
    }

    @Test
    void solutionPart1_ExampleData() throws FileNotFoundException {
        var dataTest= Solution.getInputData("src/Year_2025/Ex_10_Factory/inputExample.txt");
        var solution= Solution.solutionPart1(dataTest);

//        System.out.println(dataTest.inputRows().get(0).buttons());

        assertEquals(7,solution);
    }

//    @Test
//    void solutionPart2_ExampleData() throws FileNotFoundException {
//        var dataTest= Solution.getInputData("src/Year_2025/Ex_9_Movie_Theater/inputExample.txt");
//        var solution= Solution.solutionPart2(dataTest);
//        assertEquals(24L,solution);
//    }

    @Test
    void solutionPart1_RealData() throws FileNotFoundException {
        var dataTest= Solution.getInputData("src/Year_2025/Ex_10_Factory/input.txt");
        var solution= Solution.solutionPart1(dataTest);
        assertEquals(2L,solution);
    }

//    @Test
//    void solutionPart2_RealData() throws FileNotFoundException {
//        var dataTest= Solution.getInputData("src/Year_2025/Ex_9_Movie_Theater/input.txt");
//        var solution= Solution.solutionPart2(dataTest);
//        assertEquals(1450414119L,solution);
//    }
}