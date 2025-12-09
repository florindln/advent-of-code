package Year_2025.Ex_8_Playground;


import org.junit.jupiter.api.Test;

import java.io.FileNotFoundException;

import static org.junit.jupiter.api.Assertions.assertEquals;


class SolutionTest {
    @Test
    void getInputData() throws FileNotFoundException {
        var dataTest= Solution.getInputData("src/Year_2025/Ex_8_Playground/inputExample.txt");
        var first=dataTest.get(0);
        assertEquals(new Solution.JunctionBox(52,470,668),first);
    }

    @Test
    void solutionPart1_ExampleData() throws FileNotFoundException {
        var dataTest= Solution.getInputData("src/Year_2025/Ex_8_Playground/inputExample.txt");
        var solution= Solution.solutionPart1(dataTest,10);
        assertEquals(40,solution);
    }

    @Test
    void solutionPart2_ExampleData() throws FileNotFoundException {
        var dataTest= Solution.getInputData("src/Year_2025/Ex_8_Playground/inputExample.txt");
        var solution= Solution.solutionPart2(dataTest);
        assertEquals(25272L,solution);
    }

    @Test
    void solutionPart1_RealData() throws FileNotFoundException {
        var dataTest= Solution.getInputData("src/Year_2025/Ex_8_Playground/input.txt");
        var solution= Solution.solutionPart1(dataTest,1000);
        assertEquals(57970,solution);
    }

    @Test
    void solutionPart2_RealData() throws FileNotFoundException {
        var dataTest= Solution.getInputData("src/Year_2025/Ex_8_Playground/input.txt");
        var solution= Solution.solutionPart2(dataTest);
        assertEquals(8520040659L,solution);
    }
}