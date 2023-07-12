package syntacticalanalyzer;


import lexicalanalyzer.Scanner;
import lexicalanalyzer.ReadCode;
import lexicalanalyzer.Token;

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

/**
 *
 * @author Amaro
 */
public class Parser {
    private ReadCode code;
    private Scanner Lexical;
    private byte currentTerminal;

    public Parser(ReadCode code) {
        this.code = code;
        this.Lexical = new Scanner(code);
    }
    
    private void accept(Byte expectedTerminal){

        if(currentTerminal == expectedTerminal){
            currentTerminal = this.Lexical.scan().kind;
        }
        else{
            System.out.println("syntactic erro");
        }
    }
    
    private void acceptIt(){
         currentTerminal = this.Lexical.scan().kind;
    }
    
    public void Parse(){
        parseProgram();
    }
    
    private void parseProgram(){
        accept(Token.PROGRAM);
        accept(Token.IDENTIFIER); //mudar para parseId();?
        accept(Token.SEMICOLON);
        parseCorpo();
        accept(Token.DOT);
    }
    
    private void parseCorpo(){
        //(declaraVar ;)*
        while(currentTerminal == Token.VAR){
            parseDeclaraVar();
            accept(Token.SEMICOLON);
        }
        parseComandoComposto();
    }
    
    private void parseDeclaraVar(){
        accept(Token.VAR);
        accept(Token.IDENTIFIER);//mudar para parseId();?
        //(, <id>)*
        while(currentTerminal == Token.COMMA){
            acceptIt();
            accept(Token.IDENTIFIER); //mudar para parseId();?
        }
        accept(Token.COLON);
        //parseTipoSimples();
    }
    
    private void parseComandoComposto(){
        accept(Token.BEGIN);
        //(comando ;)*
        while(currentTerminal == Token.IDENTIFIER || currentTerminal == Token.IF || 
                currentTerminal == Token.WHILE || currentTerminal == Token.BEGIN){
            parseComando();
            accept(Token.SEMICOLON);
        }
        accept(Token.END);
    }
    
    
    private void parseFator(){
        switch (currentTerminal){
            case Token.IDENTIFIER:
                acceptIt(); //mudar para parseId();
                break;
             case Token.BOOL_LIT:
                acceptIt();
                break;
            case Token.INT_LIT:
                acceptIt();//Mudar para parseInt-Lit?
                break;
            case Token.FLOAT_LIT:
                acceptIt();//Mudar para parseFloat-lit?
                break;
            case Token.LPAREN:
                acceptIt();
                parseExpressao();
                accept(Token.RPAREN);
                break;
            default:
            System.out.println("System.out.println(\"syntactic erro\");");
            break;
        }
    }
    
    private void parseTermo(){
        parseFator();
        while(currentTerminal =="*" || currentTerminal =="/" || currentTerminal =="and"){
            acceptIt();//Mudar para parseOp-Mul()?
            parseFator();
        }
    }
    
    private void parseExpressaoSimples(){
        parseTermo();
        while(currentTerminal=="+" || currentTerminal =="-" || currentTerminal =="or"){ //op-ad
            acceptIt();//Mudar para parseOp-Ad()?
            parseTermo();
        }
    }
    
    private void parseExpressao(){
        parseExpressaoSimples();
        if(currentTerminal=="<" || currentTerminal ==">" || currentTerminal=="="){//op-rel
            acceptIt();// Mudar para parseOp-Rel()?
            parseExpressaoSimples();
        }else if(currentTerminal =="do" || currentTerminal =="then" || currentTerminal ==")" || currentTerminal ==";"){
            //follow
        }else{
            //erro
        }
    }
    
    private void parseComando(){
        switch (currentTerminal){
            case "<identifier>": //atribuicao
                acceptIt();
                accept(":=");
                parseExpressao();
                break;
            case "if": //condicional
                acceptIt();
                parseExpressao();
                accept("then");
                parseComando();
                if(currentTerminal=="else"){
                    acceptIt();
                    parseComando();//Colocar else com erro?
                }else if(currentTerminal ==";"){
                    //follow
                }else{
                    //erro
                }
                break;
            case "while": //iterativo
                acceptIt();
                parseExpressao();
                accept("do");
                parseComando();
                break;
            case "begin": //comandocomposto
                parseComandoComposto();
                break;
            default:
                break; //ERRO
        } 
    }
    
}
