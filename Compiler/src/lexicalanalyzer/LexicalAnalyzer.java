/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package lexicalanalyzer;

import java.io.IOException;

/**
 *
 * @author Amaro
 */
public class LexicalAnalyzer {

    public static void main(String[] args) throws IOException{
        String file = "path/to/your/file.txt";
        ReadCode code = new ReadCode(file);
        Scanner Lexical = new Scanner(code);
        System.out.println(code.codeText);
        
        while(!code.finished){
            Token token = Lexical.scan();
            System.out.println(token.toString());
        }
    }
    
}
