package Year_2025.Ex_1_Secret_Entrance;


import org.junit.jupiter.api.Test;

import java.io.FileNotFoundException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;


class SolutionTest {
    @Test
    void getInputData() throws FileNotFoundException {
        var dataTest= Solution.getInputData("src/Year_2025/Ex_1_Secret_Entrance/inputTest.txt");
        assertEquals(Solution.Rotation.LEFT,dataTest.get(0).rotation());
        assertEquals(68,dataTest.get(0).amount());
    }

    @Test
    void solutionPart1_TestData() throws FileNotFoundException {
        var dataTest= Solution.getInputData("src/Year_2025/Ex_1_Secret_Entrance/inputTest.txt");
        var solution=Solution.solutionPart1(dataTest);
        assertEquals(3,solution);
    }

    @Test
    void solutionPart2_TestData() throws FileNotFoundException {
        var dataTest= Solution.getInputData("src/Year_2025/Ex_1_Secret_Entrance/inputTest.txt");
        var solution=Solution.solutionPart2(dataTest);
        assertEquals(6,solution);
    }

    @Test
    void solutionPart2_multiplier() throws FileNotFoundException {
        var solution=Solution.solutionPart2(List.of(new Solution.Turn(Solution.Rotation.RIGHT,1000)));
        assertEquals(10,solution);

        solution=Solution.solutionPart2(List.of(new Solution.Turn(Solution.Rotation.LEFT,1000)));
        assertEquals(10,solution);

        solution=Solution.solutionPart2(List.of(new Solution.Turn(Solution.Rotation.RIGHT,1050)));
        assertEquals(11,solution);

        solution=Solution.solutionPart2(List.of(new Solution.Turn(Solution.Rotation.LEFT,20),
                new Solution.Turn(Solution.Rotation.LEFT,1050)
                ));
        assertEquals(11,solution);
    }

    @Test
    void solutionPart1_RealData() throws FileNotFoundException {
        var dataTest= Solution.getInputData("src/Year_2025/Ex_1_Secret_Entrance/input.txt");
        var solution=Solution.solutionPart1(dataTest);
        assertEquals(1021,solution);
    }

    @Test
    void solutionPart2_RealData() throws FileNotFoundException {
        var data= Solution.getInputData("src/Year_2025/Ex_1_Secret_Entrance/input.txt");
        var solution=Solution.solutionPart2(data);
        assertEquals(5933,solution);
    }
}