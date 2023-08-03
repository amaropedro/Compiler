/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package compiler;
import syntacticalanalyzer.Parser;
import AST.nodePrograma;

/**
 *
 * @author Amaro
 */
import java.io.IOException;
import lexicalanalyzer.ReadCode;
public class Compiler{
    public static void main(String args[]) throws IOException{
        nodePrograma p;
        String file = "D:\\Folders\\Univasf\\2022.2\\Compiladores\\Compiler\\code.txt";
        ReadCode code = new ReadCode(file);
        ErrorPrinter E = ErrorPrinter.getInstance();
        
        Parser Syntactical = new Parser(code);
        
        Printer printer = new Printer();
        
        Checker checker = new Checker();
        
        Coder coder = new Coder();
        
        p = Syntactical.parse();
        
        printer.print(p);
        checker.check(p);
        E.printErrors();
        if(E.numErrors == 0)
            coder.code(p);
    }
}