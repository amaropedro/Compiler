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
    
    public nodeProgram parse(){
        nodeProgram p;
        System.out.println();
        System.out.println ("---> Iniciando analise sintatica");
        p = parseProgram();
        return p;
    }
    
    private nodeProgram parseProgram(){
        nodeProgram p = new nodeProgram();
        accept(Token.PROGRAM);
        accept(Token.IDENTIFIER);
        accept(Token.SEMICOLON);
        p.c = parseCorpo();
        accept(Token.DOT);
        return p;
    }
    
    private nodeCorpo parseCorpo(){
        nodeCorpo c = new nodeCorpo();
        c.d = parseDeclaraVar();
        parseComandoComposto();
        return c;
    }
    
    private nodeDeclaraVar parseDeclaraVar(){
        nodeDeclaraVar firstD = null, lastD = null, firstNewTypeD, auxD;
        byte tipo;
        
        //(declaraVar ;)*
        while(currentTerminal.kind == Token.VAR){
            accept(Token.VAR);
            accept(Token.IDENTIFIER);
            
            auxD = new nodeDeclaraVar();
            auxD.name = previousTerminal.spelling;
            auxD.next = null;
            firstNewTypeD = auxD;
            
            if (firstD == null){
                firstD = auxD;
                lastD = auxD;
            }else{
                lastD.next = auxD;
            }
            
            //(, <id>)*
            while(currentTerminal.kind == Token.COMMA){
                acceptIt();
                accept(Token.IDENTIFIER);
                auxD = new nodeDeclaraVar();
                auxD.name = previousTerminal.spelling;
                auxD.next = null;
                lastD.next = auxD;
            }

            accept(Token.COLON);

            tipo = parseTipoSimples();
            auxD = firstNewTypeD;
            do{
                auxD.tipo = tipo;
                auxD = auxD.next;
            }while(auxD != null);
        
            accept(Token.SEMICOLON);
        }
        
        return firstD;
    }
    
    private byte parseTipoSimples(){
        if( currentTerminal.kind == Token.INTEGER ||
            currentTerminal.kind == Token.REAL ||
            currentTerminal.kind == Token.BOOLEAN){
                acceptIt();
                return currentTerminal.kind;
        }else{
            System.out.println("Erro na linha: " + this.previousTerminal.line + 
            " coluna: " + this.previousTerminal.col);
            System.out.println("Sintatico: Tipo nao reconhecido. Esperado: integer ou real ou boolean");
            this.errorCount++;
            return 27; //um numero fora do range
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
