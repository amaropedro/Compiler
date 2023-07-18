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
    private Token previousTerminal;
    public int errorCount;

    public Parser(ReadCode code) {
        this.Lexical = new Scanner(code);
        this.previousTerminal = new Token((byte)26, "<eot>", 0, 0);
        this.currentTerminal = Lexical.scan();
        this.errorCount = 0;
    }

/*
    public nodeP parse(String arg){;
        nodeP p;
        System.out.println();
        System.out.println ("---> Iniciando analise sintatica");
        init(arg);
        p = parseP();
        return p;
    }
    
    nodeP parseP(){
        nodeP p = new nodeP();
        accept(Token.PROGRAM);
        accept(Token.IDENTIFIER);
        accept(Token.SEMICOLON);
        p.d=parseD();
        p.c=parseC();
        if (i!=n) error();
        return p;
   }

    nodeD parseD(){
        nodeD first, last, d;
        first = null;
        last = null;
        while (token=='#') {
        takeIt();
        d = new nodeD();
        d.name = parseI();
        d.next = null;
        if (first==null) first=d;
        else last.next=d;
        last=d;
        }
        return first;
   }
*/
  
    private void accept(Byte expectedTerminal){

        if(this.currentTerminal.kind == expectedTerminal){
            this.previousTerminal = this.currentTerminal;
            this.currentTerminal = this.Lexical.scan();
        }
        else{
            System.out.println("Erro na linha: " + this.previousTerminal.line + 
            " coluna: " + this.previousTerminal.col);
            System.out.println("Sintatico: Simbolo nao reconhecido. Esperado: " 
            + Token.toSpelling(expectedTerminal));
            this.errorCount++;
        }
    }
    
    private void acceptIt(){
        this.previousTerminal = this.currentTerminal;
        this.currentTerminal = this.Lexical.scan();
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
        parseTipoSimples();
    }
    
    private void  parseTipoSimples(){
        if( currentTerminal.kind == Token.INTEGER ||
            currentTerminal.kind == Token.REAL ||
            currentTerminal.kind == Token.BOOLEAN){
                acceptIt();
        }else{
            System.out.println("Erro na linha: " + this.previousTerminal.line + 
            " coluna: " + this.previousTerminal.col);
            System.out.println("Sintatico: Tipo nao reconhecido. Esperado: integer ou real ou boolean");
            this.errorCount++;
        }
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
                System.out.println("Erro na linha: " + this.previousTerminal.line + 
                " coluna: " + this.previousTerminal.col);
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
            System.out.println("Erro na linha: " + this.previousTerminal.line + 
            " coluna: " + this.previousTerminal.col);
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
                    System.out.println("Erro na linha: " + this.previousTerminal.line + 
                    " coluna: " + this.previousTerminal.col);
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
                System.out.println("Erro na linha: " + this.previousTerminal.line + 
                " coluna: " + this.previousTerminal.col);
                System.out.println("Sintatico: Comando nao reconhecido");
                this.errorCount++;
                break; //ERRO
        } 
    }
    
}
