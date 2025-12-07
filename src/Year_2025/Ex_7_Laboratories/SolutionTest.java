package Year_2025.Ex_7_Laboratories;


import org.junit.jupiter.api.Test;

import java.io.FileNotFoundException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;


class SolutionTest {
    @Test
    void solutionPart1_ExampleData() throws FileNotFoundException {
        var dataTest= Solution.getInputData("src/Year_2025/Ex_7_Laboratories/inputExample.txt");
        var solution= Solution.solutionPart1(dataTest);
        assertEquals(21,solution);
    }

    @Test
    void solutionPart2_ExampleData() throws FileNotFoundException {
        var dataTest= Solution.getInputData("src/Year_2025/Ex_7_Laboratories/inputExample.txt");
        var solution= Solution.solutionPart2(dataTest);
        assertEquals(40,solution);
    }

    @Test
    void solutionPart1_RealData() throws FileNotFoundException {
        var dataTest= Solution.getInputData("src/Year_2025/Ex_7_Laboratories/input.txt");
        var solution= Solution.solutionPart1(dataTest);
        assertEquals(1518,solution);
    }

    @Test
    void solutionPart2_RealData() throws FileNotFoundException {
        var dataTest= Solution.getInputData("src/Year_2025/Ex_7_Laboratories/input.txt");
        var solution= Solution.solutionPart2(dataTest);
        assertEquals(25489586715621L,solution);
    }
}