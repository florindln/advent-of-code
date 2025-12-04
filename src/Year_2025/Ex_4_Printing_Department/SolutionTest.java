package Year_2025.Ex_4_Printing_Department;


import org.junit.jupiter.api.Test;

import java.io.FileNotFoundException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertIterableEquals;


class SolutionTest {
    @Test
    void getInputData() throws FileNotFoundException {
        var dataTest= Solution.getInputData("src/Year_2025/Ex_4_Printing_Department/inputExample.txt");
        var first=dataTest.get(0);
        assertIterableEquals(List.of('.','.','@','@','.','@','@','@','@','.'),first);
    }

    @Test
    void solutionPart1_ExampleData() throws FileNotFoundException {
        var dataTest= Solution.getInputData("src/Year_2025/Ex_4_Printing_Department/inputExample.txt");
        var solution= Solution.solutionPart1(dataTest);
        assertEquals(13,solution.res());
    }

    @Test
    void solutionPart2_ExampleData() throws FileNotFoundException {
        var dataTest= Solution.getInputData("src/Year_2025/Ex_4_Printing_Department/inputExample.txt");
        var solution= Solution.solutionPart2(dataTest);
        assertEquals(43,solution);
    }

    @Test
    void solutionPart1_RealData() throws FileNotFoundException {
        var dataTest= Solution.getInputData("src/Year_2025/Ex_4_Printing_Department/input.txt");
        var solution= Solution.solutionPart1(dataTest);
        assertEquals(1376,solution.res());
    }

    @Test
    void solutionPart2_RealData() throws FileNotFoundException {
        var dataTest= Solution.getInputData("src/Year_2025/Ex_4_Printing_Department/input.txt");
        var solution= Solution.solutionPart2(dataTest);
        assertEquals(8587,solution);
    }
}