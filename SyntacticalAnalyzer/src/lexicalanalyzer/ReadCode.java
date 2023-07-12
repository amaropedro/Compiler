/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package lexicalanalyzer;

import java.nio.file.Files;
import java.nio.file.Path;
import java.io.IOException;

/**
 *
 * @author Amaro
 */
public class ReadCode {
    public int index = 0;
    public String codeText;
    public boolean finished = false;

    public ReadCode(String file) throws IOException{
        Path filePath = Path.of(file);
        this.codeText = Files.readString(filePath);
    }
    
    public char nextChar() {
        if (index < codeText.length()) {
            char c = codeText.charAt(index);
            index++;
            return c;
        } else {
            finished = true;
            return '\000'; // Retorno para indicar EOF
        }
    }
}
