package Year_2025.Ex_11_Reactor;


import org.junit.jupiter.api.Test;

import java.io.FileNotFoundException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertIterableEquals;


class SolutionTest {
    @Test
    void getInputData() throws FileNotFoundException {
        var dataTest= Solution.getInputData("src/Year_2025/Ex_11_Reactor/inputExample.txt");
        var first=dataTest.get("aaa");
        assertEquals(List.of("you","hhh"),first);
    }

    @Test
    void solutionPart1_ExampleData() throws FileNotFoundException {
        var dataTest= Solution.getInputData("src/Year_2025/Ex_11_Reactor/inputExample.txt");
        var solution= Solution.solutionPart1(dataTest);
        assertEquals(5,solution);
    }

    @Test
    void solutionPart2_ExampleData() throws FileNotFoundException {
        var dataTest= Solution.getInputData("src/Year_2025/Ex_11_Reactor/inputExample2.txt");
        var solution= Solution.solutionPart2(dataTest);
        assertEquals(2L,solution);
    }

    @Test
    void solutionPart1_RealData() throws FileNotFoundException {
        var dataTest= Solution.getInputData("src/Year_2025/Ex_11_Reactor/input.txt");
        var solution= Solution.solutionPart1(dataTest);
        assertEquals(571L,solution);
    }

    @Test
    void solutionPart2_RealData() throws FileNotFoundException {
        var dataTest= Solution.getInputData("src/Year_2025/Ex_11_Reactor/input.txt");
        var solution= Solution.solutionPart2(dataTest);
        assertEquals(511378159390560L,solution);
    }
}