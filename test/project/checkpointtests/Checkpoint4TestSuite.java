package project.checkpointtests;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class Checkpoint4TestSuite {

    @Test
    public void testFileWritten() throws Exception {
        Path inputPath = Paths.get(ManualTestingFramework.INPUT);
        Files.deleteIfExists(inputPath);
        
        // Checkpoint 4 Note: If you chose a different input file format (ex: csv), you're
        // allowed to update this line to create differently-formatted input data
        Files.write(inputPath, "1,2,3".getBytes());
        
        ManualTestingFramework.main(new String[] {});
        
        Path outputPath = Paths.get(ManualTestingFramework.OUTPUT);
        List<String> allLines = Files.readAllLines(outputPath);
        Assertions.assertTrue(allLines.size() == 1, 
                "Make sure that the output is all written to one comma-separated line");
        String[] results = allLines.get(0).split(",");
        Assertions.assertTrue(results.length == 3, "Make sure exactly one output is created for each input");
    }
}