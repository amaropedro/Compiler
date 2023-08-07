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
                this.kind = OP_MUL;
            }
            if(spelling.equals("or")){
                this.kind = OP_ADD;
            }
            if(spelling.equals("true") 
                || spelling.equals("false")){
                this.kind = BOOL_LIT;
            }
        }
    }
    
    public final static byte
            IDENTIFIER = 0, OP_ADD = 1, OP_MUL = 2, OP_REL = 3,
            BEGIN = 4, PROGRAM = 5, DO = 6, ELSE = 7, IF = 8,  THEN = 9, VAR = 10,
            WHILE = 11, INTEGER = 12, REAL = 13, BOOLEAN = 14, END = 15, 
            SEMICOLON = 16, COLON = 17, BECOMES = 18,
            LPAREN = 19, RPAREN = 20, 
            COMMA = 21, DOT = 22, 
            INT_LIT = 23, FLOAT_LIT = 24, BOOL_LIT = 25, 
            EOT = 26;
    
    private final static String[] spellings = {
        "<identifier>", "<op_add>", "<op_mul>", "<op_rel>",
        "begin", "program", "do", "else", "if", "then", "var", "while",
        "integer", "real", "boolean", "end", 
        ";", ":", ":=", 
        "(", 
        ")", 
        ",", ".", 
        "<int-lit>", "<float-lit>", "<bool-lit>",
        "<eot>"};
    
    public static String toSpelling(byte kind){
        return spellings[kind];
    }
    
    @Override
    public String toString() {
        return "Token{" + "kind= " + kind + ", spelling= " + spelling + 
                ", line= " + line + ", col= " + col + '}';
    }
}