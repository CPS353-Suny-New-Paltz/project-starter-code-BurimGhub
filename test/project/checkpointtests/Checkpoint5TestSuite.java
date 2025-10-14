package project.checkpointtests;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class Checkpoint5TestSuite {

    // Verify that exceptions do not propagate back to the user as exceptions
    @Test
    public void testFileWritten() throws Exception {
        Path inputPath = Paths.get(ManualTestingFramework.INPUT);
        Files.deleteIfExists(inputPath);
        
        ManualTestingFramework.main(new String[] {});
    }
}
