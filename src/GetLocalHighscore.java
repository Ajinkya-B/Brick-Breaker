
/**
 * ICS4U0 Computer Science, Grade 12
 *
 * modified     20201110
 * date         20201109
 * @filename	GetLocalHighscore.java
 * @author      Group 2 (Ajinkya, Abdul Hadi jehanzeb)
 * @version     1.0
 */


import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;


public class GetLocalHighscore {
    
    int localHighscore;
    
    
    // Read file method
    public void readHighscore(){
        try{
            
            // create a scanner object that reads from "Highscore.txt"
            Scanner scanner = new Scanner(new File(".\\Highscore.txt"));
            
            
            while (scanner.hasNextLine()) {
                this.localHighscore = Integer.parseInt(scanner.nextLine());
            }
            
        }catch (FileNotFoundException e) {
            System.out.println("File Not Founnd!");
        }
    }
    
    
    // referance: https://stackoverflow.com/questions/29878237/java-how-to-clear-a-text-file-without-deleting-it/29878333#29878333
    public void writeHighscore(int newHighscore) throws IOException{
            
        try (FileWriter writer = new FileWriter(".\\Highscore.txt", false)) {
            try (PrintWriter innerWriter = new PrintWriter(writer, false)) {
                innerWriter.flush();// removes everything from the txt file
                writer.write(Integer.toString(newHighscore));// updates/writes the new highscre
                writer.close();
                this.localHighscore = newHighscore;
            }            
        }
    }
}
