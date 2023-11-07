import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.DynamicTest.dynamicTest;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

import org.junit.jupiter.api.Assumptions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.DynamicTest;
import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestFactory;
import org.junit.jupiter.api.io.TempDir;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

class StupidClassTest {
	
	StupidClass stupidClass;
	
	@BeforeEach
	void setUp() {
		stupidClass = new StupidClass();
	}
	
	@Test
	@DisplayName("Semplice moltiplicazione che dovrebbe funzionare")
	void testMultiply() {
		assertEquals(20, stupidClass.multiply(4, 5), "Espressione regolare dovrebbe funzionare");
		//fail("Not yet implemented");
	}
	
	@RepeatedTest(5)                                    
    @DisplayName("Ensure correct handling of zero")
    void testMultiplyWithZero() {
        assertEquals(0, stupidClass.multiply(0, 5), "Multiple with zero should be zero");
        assertEquals(0, stupidClass.multiply(5, 0), "Multiple with zero should be zero");
    }
	
	@RepeatedTest(5)
    @DisplayName("Ensure correct handling of zero")
    void testMultiplyWithZero1() {
        Assumptions.assumeFalse(System.getProperty("os.name").contains("Linux"));

        assertEquals(stupidClass.multiply(0,5), 0, "Multiple with zero should be zero");
        assertEquals(stupidClass.multiply(5,0), 0, "Multiple with zero should be zero");
    }
	
	@TestFactory
    Stream<DynamicTest> testDifferentMultiplyOperations() {
        StupidClass tester = new StupidClass();
        int[][] data = new int[][] { { 1, 2, 2 }, { 5, 3, 15 }, { 121, 4, 484 } };
        return Arrays.stream(data).map(entry -> {
            int m1 = entry[0];
            int m2 = entry[1];
            int expected = entry[2];
            return dynamicTest(m1 + " * " + m2 + " = " + expected, () -> {
                assertEquals(expected, tester.multiply(m1, m2));
            });
        });
    }
	
	
	
	
	public static int[][] data() {
        return new int[][] { { 1 , 2, 2 }, { 5, 3, 15 }, { 121, 4, 484 } };
    }

    @ParameterizedTest
    @MethodSource(value =  "data")
    void testWithStringParameter(int[] data) {
    	StupidClass tester = new StupidClass();
        int m1 = data[0];
        int m2 = data[1];
        int expected = data[2];
        assertEquals(expected, tester.multiply(m1, m2));
    }
    
    
    
    @Test
    @DisplayName("Ensure that two temporary directories with same files names and content have same hash")
    void hashTwoDynamicDirectoryWhichHaveSameContent(@TempDir Path tempDir, @TempDir Path tempDir2) throws IOException {

        Path file1 = tempDir.resolve("myfile.txt");
        List<String> input = Arrays.asList("input1", "input2", "input3");
        Files.write(file1, input);
        assertTrue(Files.exists(file1), "File should exist");
        Path file2 = tempDir2.resolve("myfile.txt");
        Files.write(file2, input);
        assertTrue(Files.exists(file2), "File should exist");

    }
    
    
    @Test
	void testDivideisThrown() {
    	StupidClass tester = new StupidClass();
        assertThrows(IllegalArgumentException.class, () -> tester.divide(1000, 5));
	}
    
    @Test
    void testDivide() {
    	StupidClass tester = new StupidClass();
        assertEquals(2, tester.divide(10, 5), "10 x 5 must be 50");
    }
    

}
