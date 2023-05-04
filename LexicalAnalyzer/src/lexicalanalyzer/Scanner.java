/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package lexicalanalyzer;

/**
 *
 * @author Amaro
 */
public class Scanner {
    private char currentChar; //= first source char
    private byte currentKind;
    private StringBuffer currentSpelling;
    public int line = 0;
    public int col = 0;
    
    private void take (char expectedChar){
        if (currentChar == expectedChar){
            currentSpelling.append(currentChar);
            //currentChar = next char
            line++;
        }
        else{
            //erro
        }
    }
    
    private void takeIt(){
        currentSpelling.append(currentChar);
        //currentChar = next char
        line++;
    }
    
    private boolean isDigit (char c){
        return Character.isDigit(c);
    }
    
    private boolean isLetter(char c){
        return Character.isLetter(c);
    }
    
    private boolean isGraphic (char c){
        return isLetter(currentChar) || isDigit(currentChar)
                || c == '+' || c == '-' || c == '*'
                || c == '/' || c == '<' || c == '>'
                || c == '=' || c == '\\';
    }
    
    private byte scanToken(){
        if(isLetter(currentChar)){
            //Letter
            takeIt();                                        
            while (isLetter(currentChar) || isDigit(currentChar))
                //(Letter|Digit)*
                takeIt();                                    
            return Token.IDENTIFIER;
        }
        if(isDigit(currentChar)){
            //Digit
            takeIt();                                       
            while (isDigit(currentChar))
                //Digit*
                takeIt();                                   
            return Token.INTLITERAL;
        }
        if(    currentChar == '+' || currentChar == '-' || currentChar == '*'
            || currentChar == '/' || currentChar == '<' || currentChar == '>'
            || currentChar == '=' || currentChar == '\\'){
            //+|-|*|/|<|>|=|\
            takeIt();                                       
            return Token.OPERATOR;
        }
        if(currentChar == ';'){
            //;
            takeIt();                                       
            return Token.SEMICOLON;
        }
        if(currentChar == ':'){
            //:
            takeIt();
            if(currentChar == '='){
                //:=
                takeIt();
                return Token.BECOMES;
            }else
                return Token.COLON;
        }
        if(currentChar == '~'){
            //~
            takeIt();
            return Token.IS;
        }
        if(currentChar == '('){
            //(
            takeIt();
            return Token.LPAREN;
        }
        if(currentChar == ')'){
            //)
            takeIt();
            return Token.RPAREN;
        }
        if(currentChar == '\000'){
            //EOT
            takeIt();
            return Token.EOT;
        }
        System.out.println("LEXICAL ERROR!");
        return 21;
    }
    
    private void scanSeparator (){
        switch (currentChar){
            case '!':{
                //!
                takeIt();
                while ( isGraphic(currentChar))
                    //Graphic*
                    takeIt();
                //eol
                take('\n');
                col++;
                line = 0;
            }
            break;
            case ' ':
                //space
                takeIt();
                break;
            case '\n':
                //eol
                takeIt();
                break;
        }
    }
    
    public Token scan () {
        //Separator*
        while(currentChar == '!' || currentChar == ' ' || currentChar == '\n')
            scanSeparator();
        currentSpelling = new StringBuffer("");
        //Token
        currentKind = scanToken();
        return new Token (currentKind, currentSpelling.toString(), line, col);
    }
}