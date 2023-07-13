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
    private ReadCode code;
    private StringBuffer currentSpelling;
    private int line = 0;
    private int col = 0;

    public Scanner(ReadCode code) {
        this.code = code;
        this.currentChar = code.nextChar(); 
    }
    
    private void take (char expectedChar){
        if (currentChar == expectedChar){
            currentSpelling.append(currentChar);
            currentChar = code.nextChar();
            line = line +1;
        }
        else{
            System.out.println("erro!");
        }
    }
    
    private void takeIt(){
        currentSpelling.append(currentChar);
        currentChar = code.nextChar();
        line = line +1;
    }
    
    private boolean isDigit (char c){
        return Character.isDigit(c);
    }
    
    private boolean isLetter(char c){
        return Character.isLetter(c);
    }
    
    private boolean isGraphic (char c){
        return isLetter(c) || isDigit(c)
                || c == '+' || c == '-' || c == '*'
                || c == '/' || c == '<' || c == '>'
                || c == '=';
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
            takeIt();
            
            //float num.num or num. type
            if(currentChar == '.'){
                takeIt();
                while (isDigit(currentChar))
                    //Digit*
                    takeIt();                                   
                return Token.FLOAT_LIT;
            }
            //Digit
            else{
                while (isDigit(currentChar))
                    //Digit*
                    takeIt();                                   
                return Token.INT_LIT;
            }
        }
        if(currentChar == '+' || currentChar == '-'){
            //+|-
            takeIt();                                       
            return Token.OP_ADD;
        }
        if(currentChar == '*' || currentChar == '/' ){
            takeIt();
            return Token.OP_MUL;
        }
        if(currentChar == '='){
            takeIt();
            return Token.OP_REL;
        }
        if(currentChar == '<'){
            takeIt();
            if(currentChar == '>'){
                    //<>
                    takeIt();
                    return Token.OP_REL;
            }
            else if(currentChar == '='){
                    //<=
                    takeIt();
                    return Token.OP_REL;
            }
            else
                //<
                return Token.OP_REL;
        }
        if(currentChar == '>'){
            takeIt();
            if(currentChar == '='){
                    //>=
                    takeIt();
                    return Token.OP_REL;
            }
            else
                //>
                return Token.OP_REL;
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
        if(currentChar == '.'){
            takeIt();
            //float .num type
            if(isDigit(currentChar)){
                while (isDigit(currentChar))
                    //Digit*
                    takeIt();                                   
                return Token.FLOAT_LIT;
            }
            else
                //.
                return Token.DOT;
        }
        if(currentChar == ','){
            //~
            takeIt();
            return Token.COMMA;
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
            case '!':
                //!
                takeIt();
                while ( isGraphic(currentChar))
                    //Graphic*
                    takeIt();
                //eol
                take('\r'); //olhar isso
                col = col+1;
                line = 0;
            break;
            case ' ':
                //space
                takeIt();
                break;
            case '\n':
                //eol
                col = col+1;
                line = 0;
                takeIt();
                break;
            case '\r': //new line no windows é '\r\n' -_-
                col = col+1;
                line = 0;
                takeIt();
                takeIt();
        }
    }
    
    public Token scan () {
        //Separator*
        while(currentChar == '!' || currentChar == ' ' || currentChar == '\n' || currentChar == '\r')
            scanSeparator();
        currentSpelling = new StringBuffer("");
        //Token
        currentKind = scanToken();
        return new Token (currentKind, currentSpelling.toString(), line, col);
    }
}
