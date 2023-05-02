/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package lexicalanalyzer;

/**
 *
 * @author Amaro
 */
public class Token {
    public byte kind;
    public String spelling;
    public int line;
    public int col;
    
    public Token (byte kind, String spelling){
        this.kind = kind;
        this.spelling = spelling;
        
        if (kind == IDENTIFIER){
            for( int k = BEGIN; k <= WHILE; k++)
                if(spelling.equals(spellings[k])){
                    this.kind = k;
                    break;
                }              
        }
    }
    
    public final static byte
            IDENTIFIER = 0, INTLITERAL = 1, OPERATOR = 2,
            BEGIN = 3, CONST = 4, DO = 5, ELSE = 6, END =7,
            IF = 8, IN = 9, LET = 10, THEN = 11, VAR = 12,
            WHILE = 13, SEMICOLON = 14, COLON = 15, BECOMES = 16,
            IS = 17, LPAREN = 18, RPAREN = 19, EOT = 20;
    
    private final static String[] spellings = {
        "<identifier>", "<integer-literal>", "<operator>",
        "begin", "const", "do", "else", "end","if", "in", 
        "let", "then", "var", "while", ";", ":", ":=", "~",
        "(", ")", "<eot>"};
    }
}
