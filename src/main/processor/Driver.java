package src.main.processor;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

public class Driver {
    // Input format: text file
    public static void main(String[] args) throws IOException {
        String pathIn = new File("input-processor/in.txt").getAbsolutePath(); // Input file
        String pathOut = new File("output-processor/out.txt").getAbsolutePath(); // Output file

        String file = readFile(pathIn);

        Processor processor = new Processor(file, pathOut);
        processor.process();
        System.out.println("Complete");
    }

    static String readFile(String pathIn) throws IOException {
        BufferedReader br = new BufferedReader(new FileReader(pathIn));
        try {
            StringBuilder sb = new StringBuilder();
            String line = br.readLine();
    
            while (line != null) {
                sb.append(line);
                line = br.readLine();
            }
            return sb.toString().replaceAll("\\s+","");
        } finally {
            br.close();
        }
    }
}
