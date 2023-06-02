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
    
    public Token (byte kind, String spelling, int line, int col){
        this.kind = kind;
        this.spelling = spelling;
        this.line = line;
        this.col = col;
        
        if (kind == IDENTIFIER){
            for( int k = BEGIN; k <= END; k++)
                if(spelling.equals(spellings[k])){
                    this.kind = (byte) k;
                    break;
                }
            if(spelling.equals("and")){
                this.kind = OP_ADD;
            }
            if(spelling.equals("or")){
                this.kind = OP_MUL;
            }
            if(spelling.equals("true") 
                || spelling.equals("false")){
                this.kind = BOOL_LIT;
            }
        }
    }
    
    public final static byte
            IDENTIFIER = 0, OP_ADD = 1, OP_MUL = 2, OP_REL = 3,
            BEGIN = 4, DO = 5, ELSE = 6, IF = 7,  THEN = 8, VAR = 9, 
            WHILE = 10, END = 11, 
            SEMICOLON = 12, COLON = 13, BECOMES = 14,
            LPAREN = 15, RPAREN = 16, 
            COMMA = 17, DOT = 18, 
            INT_LIT = 19, FLOAT_LIT = 20, BOOL_LIT = 21,
            EOT = 22;
    
    private final static String[] spellings = {
        "<identifier>", "<op_add>", "<op_mul>", "<op_rel>",
        "begin", "do", "else", "if", "then", "var", "while", "end", 
        ";", ":", ":=", 
        "(", 
        ")", 
        ",", ".", 
        "<int-lit>", "<float-lit>", "<bool-lit>",
        "<eot>"};
    
    @Override
    public String toString() {
        return "Token{" + "kind= " + kind + ", spelling= " + spelling + 
                ", line= " + line + ", col= " + col + '}';
    }
}