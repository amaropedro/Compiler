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
    private Scanner Lexical;
    private Token currentTerminal;
    public int errorCount;

    public Parser(ReadCode code) {
        this.Lexical = new Scanner(code);
        this.currentTerminal = Lexical.scan();
        this.errorCount = 0;
    }
    
    private void accept(Byte expectedTerminal){

        if(this.currentTerminal.kind == expectedTerminal){
            this.currentTerminal.kind = this.Lexical.scan().kind;
        }
        else{
            System.out.println("Erro na linha: " + this.currentTerminal.line + 
            " coluna: " + this.currentTerminal.col);
            System.out.println("Sintatico: Simbolo nao reconhecido. Esperado: " 
            + Token.toSpelling(expectedTerminal));
            this.errorCount++;
        }
    }
    
    private void acceptIt(){
        this.currentTerminal.kind = this.Lexical.scan().kind;
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
        while(currentTerminal.kind == Token.VAR){
            parseDeclaraVar();
            accept(Token.SEMICOLON);
        }
        parseComandoComposto();
    }
    
    private void parseDeclaraVar(){
        accept(Token.VAR);
        accept(Token.IDENTIFIER);//mudar para parseId();?
        //(, <id>)*
        while(currentTerminal.kind == Token.COMMA){
            acceptIt();
            accept(Token.IDENTIFIER); //mudar para parseId();?
        }
        accept(Token.COLON);
        //parseTipoSimples();
    }
    
    private void parseComandoComposto(){
        accept(Token.BEGIN);
        //(comando ;)*
        while(currentTerminal.kind== Token.IDENTIFIER || currentTerminal.kind== Token.IF || 
                currentTerminal.kind== Token.WHILE || currentTerminal.kind== Token.BEGIN){
            parseComando();
            accept(Token.SEMICOLON);
        }
        accept(Token.END);
    }
    
    
    private void parseFator(){
        switch (currentTerminal.kind){
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
                System.out.println("Erro na linha: " + this.currentTerminal.line + 
                " coluna: " + this.currentTerminal.col);
                System.out.println("Sintatico: Fator nao reconhecido");
                this.errorCount++;
            break;
        }
    }
    
    private void parseTermo(){
        parseFator();
        while(currentTerminal.kind== Token.OP_MUL){
            acceptIt();//Mudar para parseOp-Mul()?
            parseFator();
        }
    }
    
    private void parseExpressaoSimples(){
        parseTermo();
        while(currentTerminal.kind== Token.OP_ADD){ //op-ad
            acceptIt();//Mudar para parseOp-Ad()?
            parseTermo();
        }
    }
    
    private void parseExpressao(){
        parseExpressaoSimples();
        if(currentTerminal.kind == Token.OP_REL){//op-rel
            acceptIt();// Mudar para parseOp-Rel()?
            parseExpressaoSimples();
        }else if(currentTerminal.kind== Token.DO || currentTerminal.kind== Token.THEN || currentTerminal.kind== Token.RPAREN || currentTerminal.kind== Token.SEMICOLON){
            //follow
        }else{
            System.out.println("Erro na linha: " + this.currentTerminal.line + 
            " coluna: " + this.currentTerminal.col);
            System.out.println("Sintatico: Expressao nao reconhecida.");
            this.errorCount++;
        }
    }
    
    private void parseComando(){
        switch (currentTerminal.kind){
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
                if(currentTerminal.kind== Token.ELSE){
                    acceptIt();
                    parseComando();//Colocar else com erro?
                }else if(currentTerminal.kind== Token.SEMICOLON){
                    //follow
                }else{
                    System.out.println("Erro na linha: " + this.currentTerminal.line + 
                    " coluna: " + this.currentTerminal.col);
                    System.out.println("Sintatico: Comando condicional nao reconhecido. Esperado: 'ELSE' ou ';'");
                    this.errorCount++;
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
                System.out.println("Erro na linha: " + this.currentTerminal.line + 
                " coluna: " + this.currentTerminal.col);
                System.out.println("Sintatico: Comando nao reconhecido");
                this.errorCount++;
                break; //ERRO
        } 
    }
    
}
