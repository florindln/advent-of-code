package Year_2025.Ex_3_Lobby;


import org.junit.jupiter.api.Test;

import java.io.FileNotFoundException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;


class SolutionTest {
    @Test
    void getInputData() throws FileNotFoundException {
        var dataTest= Solution.getInputData("src/Year_2025/Ex_3_Lobby/inputExample.txt");
        var first=dataTest.get(0);
        assertIterableEquals(List.of(9,8,7,6,5,4,3,2,1,1,1,1,1,1,1),first);
    }

    @Test
    void solutionPart1_ExampleData() throws FileNotFoundException {
        var dataTest= Solution.getInputData("src/Year_2025/Ex_3_Lobby/inputExample.txt");
        var solution= Solution.solutionPart1(dataTest);
        assertEquals(357,solution);
    }

    @Test
    void solutionPart2_ExampleData() throws FileNotFoundException {
        var dataTest= Solution.getInputData("src/Year_2025/Ex_3_Lobby/inputExample.txt");
        var solution= Solution.solutionPart2(dataTest,12);
        assertEquals(3121910778619L,solution);
    }


    @Test
    void solutionPart1_RealData() throws FileNotFoundException {
        var dataTest= Solution.getInputData("src/Year_2025/Ex_3_Lobby/input.txt");
        var solution= Solution.solutionPart1(dataTest);
        assertEquals(16993,solution);
    }

    @Test
    void solutionPart2_RealData() throws FileNotFoundException {
        var dataTest= Solution.getInputData("src/Year_2025/Ex_3_Lobby/input.txt");
        var solution= Solution.solutionPart2(dataTest,12);
        assertEquals(168617068915447L,solution);
    }
}