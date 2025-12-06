package Year_2025.Ex_5_Cafeteria;


import org.junit.jupiter.api.Test;

import java.io.FileNotFoundException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertIterableEquals;


class SolutionTest {
    @Test
    void getInputData() throws FileNotFoundException {
        var dataTest= Solution.getInputData("src/Year_2025/Ex_5_Cafeteria/inputExample.txt");
        assertEquals(new Solution.IdRange(3L,5L),dataTest.freshRanges().get(0));
        assertEquals(1L,dataTest.availableIngredients().get(0));
    }

    @Test
    void solutionPart1_ExampleData() throws FileNotFoundException {
        var dataTest= Solution.getInputData("src/Year_2025/Ex_5_Cafeteria/inputExample.txt");
        var solution= Solution.solutionPart1(dataTest);
        assertEquals(3,solution);
    }

    @Test
    void solutionPart2_ExampleData() throws FileNotFoundException {
        var dataTest= Solution.getInputData("src/Year_2025/Ex_5_Cafeteria/inputExample.txt");
        var solution= Solution.solutionPart2(dataTest);
        assertEquals(14L,solution);
    }

    @Test
    void solutionPart1_RealData() throws FileNotFoundException {
        var dataTest= Solution.getInputData("src/Year_2025/Ex_5_Cafeteria/input.txt");
        var solution= Solution.solutionPart1(dataTest);
        assertEquals(733,solution);
    }

    @Test
    void solutionPart2_RealData() throws FileNotFoundException {
        var dataTest= Solution.getInputData("src/Year_2025/Ex_5_Cafeteria/input.txt");
        var solution= Solution.solutionPart2(dataTest);
        assertEquals(345821388687084L,solution);
    }
}