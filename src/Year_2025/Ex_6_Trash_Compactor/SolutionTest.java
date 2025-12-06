package Year_2025.Ex_6_Trash_Compactor;


import org.junit.jupiter.api.Test;

import java.io.FileNotFoundException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;


class SolutionTest {
    @Test
    void getInputData() throws FileNotFoundException {
        var dataTest= Solution.getInputData("src/Year_2025/Ex_6_Trash_Compactor/inputExample.txt");
        assertEquals(List.of(123,328,51,64 ),dataTest.lines().get(0));
        assertEquals(List.of("*","+","*","+"),dataTest.operations());
    }

    @Test
    void solutionPart1_ExampleData() throws FileNotFoundException {
        var dataTest= Solution.getInputData("src/Year_2025/Ex_6_Trash_Compactor/inputExample.txt");
        var solution= Solution.solutionPart1(dataTest);
        assertEquals(4277556L,solution);
    }

    @Test
    void solutionPart2_ExampleData() throws FileNotFoundException {
        var dataTest= Solution.getInputData("src/Year_2025/Ex_5_Cafeteria/inputExample.txt");
//        var solution= Solution.solutionPart2(dataTest);
//        assertEquals(14L,solution);
    }

    @Test
    void solutionPart1_RealData() throws FileNotFoundException {
        var dataTest= Solution.getInputData("src/Year_2025/Ex_6_Trash_Compactor/input.txt");
        var solution= Solution.solutionPart1(dataTest);
        assertEquals(3968933219902L,solution);
    }

    @Test
    void solutionPart2_RealData() throws FileNotFoundException {
        var dataTest= Solution.getInputData("src/Year_2025/Ex_5_Cafeteria/input.txt");
//        var solution= Solution.solutionPart2(dataTest);
//        assertEquals(345821388687084L,solution);
    }
}