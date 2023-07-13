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
        currentTerminal = Lexical.scan().kind;
    }
    
    private void accept(Byte expectedTerminal){

        if(currentTerminal == expectedTerminal){
            currentTerminal = this.Lexical.scan().kind;
        }
        else{
            System.out.println("Erro simbolo nao reconhecido. Experado: " + expectedTerminal);
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
                System.out.println("Erro: fator nao reconhecido");
            break;
        }
    }
    
    private void parseTermo(){
        parseFator();
        while(currentTerminal == Token.OP_MUL){
            acceptIt();//Mudar para parseOp-Mul()?
            parseFator();
        }
    }
    
    private void parseExpressaoSimples(){
        parseTermo();
        while(currentTerminal == Token.OP_ADD){ //op-ad
            acceptIt();//Mudar para parseOp-Ad()?
            parseTermo();
        }
    }
    
    private void parseExpressao(){
        parseExpressaoSimples();
        if(currentTerminal== Token.OP_REL){//op-rel
            acceptIt();// Mudar para parseOp-Rel()?
            parseExpressaoSimples();
        }else if(currentTerminal == Token.DO || currentTerminal == Token.THEN || currentTerminal == Token.RPAREN || currentTerminal == Token.SEMICOLON){
            //follow
        }else{
            System.out.println("Erro: expressao nao reconhecida.");
        }
    }
    
    private void parseComando(){
        switch (currentTerminal){
            case Token.IDENTIFIER: //atribuicao
                acceptIt();
                accept(Token.BECOMES);
                parseExpressao();
                break;
            case Token.IF: //condicional
                acceptIt();
                parseExpressao();
                accept(Token.THEN);
                parseComando();
                if(currentTerminal == Token.ELSE){
                    acceptIt();
                    parseComando();//Colocar else com erro?
                }else if(currentTerminal == Token.SEMICOLON){
                    //follow
                }else{
                    System.out.println("Erro: comando condicional nao reconhecido. Experado: 'ELSE' ou ';'");
                }
                break;
            case Token.WHILE: //iterativo
                acceptIt();
                parseExpressao();
                accept(Token.DO);
                parseComando();
                break;
            case Token.BEGIN: //comandocomposto
                parseComandoComposto();
                break;
            default:
                System.out.println("Erro: comando nao reconhecido");
                break; //ERRO
        } 
    }
    
}
