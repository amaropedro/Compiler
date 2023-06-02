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
        String file = "D:\\Folders\\Univasf\\2022.2\\Compiladores\\Compiler\\code.txt";
        ReadCode code = new ReadCode(file);
        Scanner Lexical = new Scanner(code);
        //Atenção! se for colocado um espaço no final do arquivo, ele pega o token errado, pode haver outros problemas come espaço
        //se colocar um comentario ou um enter ou um espaço no inicio também da problema
        System.out.println(code.codeText);
        
        while(!code.finished){
            Token token = Lexical.scan();
            System.out.println(token.toString());
        }
    }
    
}
