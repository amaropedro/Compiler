package syntacticalanalyzer;

import AST.nodeExpressaoSimples;
import AST.nodeProgram;
import AST.nodeComando;
import AST.nodeExpressaoSimplesComOp;
import AST.nodeDeclaraVar;
import AST.nodeTermo;
import AST.nodeCorpo;
import AST.nodeComandoAtribuicao;
import AST.nodeComandoComposto;
import AST.nodeExpressao;
import AST.nodeFator;
import AST.nodeFatorId;
import AST.nodeFatorInt;
import AST.nodeFatorFloat;
import AST.nodeFatorBool;
import AST.nodeFatorExp;
import AST.nodeFatorComOp;
import lexicalanalyzer.Scanner;
import lexicalanalyzer.ReadCode;
import lexicalanalyzer.Token;

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
        c.CMD = parseComandoComposto();
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
    
    private nodeComandoComposto parseComandoComposto(){
        nodeComandoComposto CMD, firstCMD = null, lastCMD = null;
        
        accept(Token.BEGIN);
        
        //(comando ;)*
        while(currentTerminal.kind== Token.IDENTIFIER || currentTerminal.kind== Token.IF || 
                currentTerminal.kind== Token.WHILE || currentTerminal.kind== Token.BEGIN){
            
            CMD = new nodeComandoComposto();
            
            CMD.cmd = parseComando();
            CMD.next = null;
            
            if(firstCMD == null){
                firstCMD = CMD;
            }else{
                lastCMD.next = CMD;
            }
            
            lastCMD = CMD;
            accept(Token.SEMICOLON);
        }
        accept(Token.END);
        
        return firstCMD;
    }
    
    private nodeFator parseFator(){
        switch (currentTerminal.kind){
            case Token.IDENTIFIER:
                nodeFatorId Fid = new nodeFatorId();
                Fid.name = currentTerminal.spelling;
                acceptIt();
                return Fid;
             case Token.BOOL_LIT:
                nodeFatorBool Fbool = new nodeFatorBool();
                Fbool.bool = currentTerminal.spelling;
                acceptIt();
                return Fbool;
            case Token.INT_LIT:
                nodeFatorInt Fint = new nodeFatorInt();
                Fint.num = currentTerminal.spelling;
                acceptIt();
                return Fint;
            case Token.FLOAT_LIT:
                nodeFatorFloat Ffloat = new nodeFatorFloat();
                Ffloat.numReal = currentTerminal.spelling;
                acceptIt();
                return Ffloat;
            case Token.LPAREN:
                nodeFatorExp Fexp = new nodeFatorExp();
                acceptIt();
                Fexp.E = parseExpressao();
                accept(Token.RPAREN);
                return Fexp;
            default:
                System.out.println("Erro na linha: " + this.previousTerminal.line + 
                " coluna: " + this.previousTerminal.col);
                System.out.println("Sintatico: Fator nao reconhecido");
                this.errorCount++;
                return null;
        }
    }
    
    private nodeTermo parseTermo(){
        nodeTermo T = new nodeTermo();
        nodeFatorComOp FOp, firstFOp = null, lastFOp = null;
        
        T.f = parseFator();
        while(currentTerminal.kind== Token.OP_MUL){
            FOp = new nodeFatorComOp();
            FOp.next = null;
            FOp.operador = currentTerminal.spelling;
            acceptIt();//Mudar para parseOp-Mul()?
            FOp.f = parseFator();
            
            if(firstFOp == null){
                firstFOp = FOp;
            }else{
                lastFOp.next = FOp;
            }
            
            lastFOp = FOp;
        }
        T.fOp = firstFOp;
        
        return T;
    }
    
    private nodeExpressaoSimples parseExpressaoSimples(){
        nodeExpressaoSimples Es = new nodeExpressaoSimples();
        nodeExpressaoSimplesComOp EsOp, firstEsOp = null, lastEsOp = null;
        
        Es.T = parseTermo();
        while(currentTerminal.kind == Token.OP_ADD){ //op-ad
            EsOp = new nodeExpressaoSimplesComOp();
            EsOp.next = null;
            EsOp.operador = currentTerminal.spelling;
            
            acceptIt();//consome OP_ADD
            
            EsOp.T = parseTermo();
            
            if(firstEsOp == null){
                firstEsOp = EsOp;
            }else{
                lastEsOp.next = EsOp;
            }
            
            lastEsOp = EsOp;
        }
        
        Es.EsOp = firstEsOp;
        
        return Es;
    }
    
    private nodeExpressao parseExpressao(){
        nodeExpressao E = new nodeExpressao();
        
        E.Es1 = parseExpressaoSimples();
        E.Es2 = null;
        if(currentTerminal.kind == Token.OP_REL){//op-rel
            acceptIt();// Mudar para parseOp-Rel()?
            parseExpressaoSimples();
        }else if(currentTerminal.kind== Token.DO || //follow
                currentTerminal.kind== Token.THEN ||
                currentTerminal.kind== Token.RPAREN ||
                currentTerminal.kind== Token.SEMICOLON){
            
        }else{
            System.out.println("Erro na linha: " + this.previousTerminal.line + 
            " coluna: " + this.previousTerminal.col);
            System.out.println("Sintatico: Expressao nao reconhecida.");
            this.errorCount++;
        }
        
        return E;
    }
    
    private nodeComando parseComando(){
        switch (currentTerminal.kind){
            case Token.IDENTIFIER: //atribuicao
                nodeComandoAtribuicao cmd = new nodeComandoAtribuicao();
                cmd.name = currentTerminal.spelling;
                acceptIt();
                
                accept(Token.BECOMES);
                
                cmd.E = parseExpressao();
                return cmd;
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
                return null;
            case Token.WHILE: //iterativo
                acceptIt();
                parseExpressao();
                accept(Token.DO);
                parseComando();
                return null;
            case Token.BEGIN: //comandocomposto
                parseComandoComposto();
                return null;
            default:
                System.out.println("Erro na linha: " + this.previousTerminal.line + 
                " coluna: " + this.previousTerminal.col);
                System.out.println("Sintatico: Comando nao reconhecido");
                this.errorCount++;
                return null;
        } 
    }
    
}
