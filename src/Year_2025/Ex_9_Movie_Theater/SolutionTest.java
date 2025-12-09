package Year_2025.Ex_9_Movie_Theater;


import org.junit.jupiter.api.Test;

import java.io.FileNotFoundException;

import static org.junit.jupiter.api.Assertions.assertEquals;


class SolutionTest {
    @Test
    void getInputData() throws FileNotFoundException {
        var dataTest= Solution.getInputData("src/Year_2025/Ex_9_Movie_Theater/inputExample.txt");
        var first=dataTest.get(0);
        assertEquals(new Solution.TileCoord(1,7),first);
    }

    @Test
    void solutionPart1_ExampleData() throws FileNotFoundException {
        var dataTest= Solution.getInputData("src/Year_2025/Ex_9_Movie_Theater/inputExample.txt");
        var solution= Solution.solutionPart1(dataTest);
        assertEquals(50,solution);
    }

    @Test
    void solutionPart2_ExampleData() throws FileNotFoundException {
        var dataTest= Solution.getInputData("src/Year_2025/Ex_9_Movie_Theater/inputExample.txt");
        var solution= Solution.solutionPart2(dataTest);
        assertEquals(24L,solution);
    }

    @Test
    void solutionPart1_RealData() throws FileNotFoundException {
        var dataTest= Solution.getInputData("src/Year_2025/Ex_9_Movie_Theater/input.txt");
        var solution= Solution.solutionPart1(dataTest);
        assertEquals(4782151432L,solution);
    }

    @Test
    void solutionPart2_RealData() throws FileNotFoundException {
        var dataTest= Solution.getInputData("src/Year_2025/Ex_9_Movie_Theater/input.txt");
        var solution= Solution.solutionPart2(dataTest);
        assertEquals(1450414119L,solution);
    }
}