/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package syntacticalanalyzer;
import java.io.IOException;
import lexicalanalyzer.ReadCode;

/**
 *
 * @author Amaro
 */
public class SyntacticalAnalyzer {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws IOException{
        String file = "D:\\Folders\\Univasf\\2022.2\\Compiladores\\Compiler\\code.txt";
        ReadCode code = new ReadCode(file);
        Parser Syntactical = new Parser(code);
        
        System.out.println("Analizando programa:");
        System.out.println(code.codeText);
        System.out.println("-------");
        Syntactical.parse();
    }
    
}
