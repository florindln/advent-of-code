package Year_2025.Ex_2_Gift_Shop;


import org.junit.jupiter.api.Test;

import java.io.FileNotFoundException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;


class SolutionTest {
    @Test
    void getInputData() throws FileNotFoundException {
        var dataTest= Solution.getInputData("src/Year_2025/Ex_2_Gift_Shop/inputExample.txt");
        var first=dataTest.get(0);
        assertEquals("11",first.start());
        assertEquals("22",first.end());
    }

    @Test
    void solutionPart1_ExampleData() throws FileNotFoundException {
        var dataTest= Solution.getInputData("src/Year_2025/Ex_2_Gift_Shop/inputExample.txt");
        var solution=Solution.solutionPart1(dataTest);
        assertEquals(1227775554L,solution);
    }

    @Test
    void getAllPossibleRepeatedSequences(){
        var sequences=Solution.getAllPossibleRepeatedSequences(123123);
        var expected=List.of("1", "12", "123", "1231", "12312", "2", "23", "231", "2312", "3", "31", "312", "1", "12", "2");
        assertIterableEquals(expected,sequences);
    }

    @Test
    void isValid(){
        var sequences=List.of("1", "12", "123", "1231", "12312", "2", "23", "231", "2312", "3", "31", "312", "1", "12", "2");

        for (var seq:sequences){
            if(seq.equals("123"))
                continue;

            assertTrue(Solution.isValid(123123, seq));
        }
        assertFalse(Solution.isValid(123123, "123"));
    }

    @Test
    void solutionPart2_sequences() throws FileNotFoundException {
        List<Solution.IdRange> dataTest= List.of(new Solution.IdRange("2121212118","2121212124"));
        var solution=Solution.solutionPart2(dataTest);
        assertEquals(2121212121L,solution);
    }

    @Test
    void solutionPart2_ExampleData() throws FileNotFoundException {
        var dataTest= Solution.getInputData("src/Year_2025/Ex_2_Gift_Shop/inputExample.txt");
        var solution=Solution.solutionPart2(dataTest);
        assertEquals(4174379265L,solution);
    }


    @Test
    void solutionPart1_RealData() throws FileNotFoundException {
        var dataTest= Solution.getInputData("src/Year_2025/Ex_2_Gift_Shop/input.txt");
        var solution=Solution.solutionPart1(dataTest);
        assertEquals(19386344315L,solution);
    }

    @Test
    void solutionPart2_RealData() throws FileNotFoundException {
        var dataTest= Solution.getInputData("src/Year_2025/Ex_2_Gift_Shop/input.txt");
        var solution=Solution.solutionPart2(dataTest);
        assertEquals(34421651192L,solution);
    }
}