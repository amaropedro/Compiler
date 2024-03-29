package syntacticalanalyzer;

import AST.*;
import compiler.ErrorPrinter;
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
    private final ErrorPrinter Error = ErrorPrinter.getInstance();

    public Parser(ReadCode code) {
        this.Lexical = new Scanner(code);
        this.previousTerminal = new Token((byte)26, "<eot>", 0, 0);
        this.currentTerminal = Lexical.scan();
    }
  
    private void accept(Byte expectedTerminal){

        if(this.currentTerminal.kind == expectedTerminal){
            this.previousTerminal = this.currentTerminal;
            this.currentTerminal = this.Lexical.scan();
        }
        else{
            Error.reportError(this.previousTerminal.line, this.previousTerminal.col,
                    "Sintatico",
                    "Simbolo nao reconhecido. Esperado: " + Token.toSpelling(expectedTerminal));
        }
    }
    
    private void acceptIt(){
        this.previousTerminal = this.currentTerminal;
        this.currentTerminal = this.Lexical.scan();
    }
    
    public nodePrograma parse(){
        nodePrograma p;
        p = parseProgram();
        return p;
    }
    
    private nodePrograma parseProgram(){
        nodePrograma p = new nodePrograma();
        accept(Token.PROGRAM);
        accept(Token.IDENTIFIER);
        p.progamName = previousTerminal.spelling;
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
        String tipo;
        
        //(declaraVar ;)*
        while(currentTerminal.kind == Token.VAR){
            accept(Token.VAR);
            accept(Token.IDENTIFIER);
            
            auxD = new nodeDeclaraVar();
            auxD.name = previousTerminal.spelling;
            auxD.line = previousTerminal.line;
            auxD.col = previousTerminal.col;
            auxD.next = null;
            firstNewTypeD = auxD;
            
            if (firstD == null){
                firstD = auxD;
            }else{
                lastD.next = auxD;
            }
            
            lastD = auxD;
            
            //(, <id>)*
            while(currentTerminal.kind == Token.COMMA){
                acceptIt();
                accept(Token.IDENTIFIER);
                auxD = new nodeDeclaraVar();
                auxD.name = previousTerminal.spelling;
                auxD.line = previousTerminal.line;
                auxD.col = previousTerminal.col;
                auxD.next = null;
                lastD.next = auxD;
                lastD = auxD;
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
    
    private String parseTipoSimples(){
        switch(currentTerminal.kind){
            case Token.INTEGER:
                acceptIt();
                return "int";
            case Token.REAL:
                acceptIt();
                return "real";
            case Token.BOOLEAN:
                acceptIt();
                return "bool";
            default:
                Error.reportError(this.previousTerminal.line, this.previousTerminal.col,
                    "Sintatico",
                    "Tipo nao reconhecido. Esperado: integer ou real ou boolean");
                return "missing";
        }
    }
    
    private nodeComandoComposto parseComandoComposto(){
        nodeComandoComposto CMD = new nodeComandoComposto();
        nodeComando cmd, firstcmd = null, lastcmd = null;
        
        accept(Token.BEGIN);
        
        //(comando ;)*
        while(currentTerminal.kind== Token.IDENTIFIER || currentTerminal.kind== Token.IF || 
                currentTerminal.kind== Token.WHILE || currentTerminal.kind== Token.BEGIN){
            
            cmd = new nodeComando();
            
            cmd.cmd = parseComando();
            cmd.next = null;
            
            if(firstcmd == null){
                firstcmd = cmd;
            }else{
                lastcmd.next = cmd;
            }
            
            lastcmd = cmd;
            accept(Token.SEMICOLON);
        }
        accept(Token.END);
        
        CMD.firstcmd = firstcmd;
        
        return CMD;
    }
    
    private nodeFator parseFator(){
        switch (currentTerminal.kind){
            case Token.IDENTIFIER:
                nodeFatorId Fid = new nodeFatorId();
                Fid.name = currentTerminal.spelling;
                Fid.line = currentTerminal.line;
                Fid.col = currentTerminal.col;
                acceptIt();
                return Fid;
             case Token.BOOL_LIT:
                nodeFatorBool Fbool = new nodeFatorBool();
                Fbool.bool = currentTerminal.spelling;
                Fbool.line = currentTerminal.line;
                Fbool.col = currentTerminal.col;
                acceptIt();
                return Fbool;
            case Token.INT_LIT:
                nodeFatorInt Fint = new nodeFatorInt();
                Fint.num = currentTerminal.spelling;
                Fint.line = currentTerminal.line;
                Fint.col = currentTerminal.col;
                acceptIt();
                return Fint;
            case Token.FLOAT_LIT:
                nodeFatorFloat Ffloat = new nodeFatorFloat();
                Ffloat.numReal = currentTerminal.spelling;
                Ffloat.line = currentTerminal.line;
                Ffloat.col = currentTerminal.col;
                acceptIt();
                return Ffloat;
            case Token.LPAREN:
                nodeFatorExp Fexp = new nodeFatorExp();
                acceptIt();
                Fexp.E = parseExpressao();
                Fexp.line = currentTerminal.line;
                Fexp.col = currentTerminal.col;
                accept(Token.RPAREN);
                return Fexp;
            default:
                Error.reportError(this.previousTerminal.line, this.previousTerminal.col,
                    "Sintatico",
                    "Fator nao reconhecido");
                return null;
        }
    }
    
    private nodeTermo parseTermo(){
        nodeTermo T = new nodeTermo();
        nodeFatorComOp FOp, firstFOp = null, lastFOp = null;
        
        T.f = parseFator();
        if (T.f != null){
            T.line = T.f.line;
            T.col = T.f.col;
        }
        
        while(currentTerminal.kind== Token.OP_MUL){
            FOp = new nodeFatorComOp();
            FOp.next = null;
            FOp.operador = currentTerminal.spelling;
            acceptIt();
            FOp.f = parseFator();
            FOp.line = previousTerminal.line;
            FOp.col = previousTerminal.col;
            
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
        if(Es.T != null){
            Es.line = Es.T.line;
            Es.col = Es.T.col;
        }
        
        while(currentTerminal.kind == Token.OP_ADD){ //op-ad
            EsOp = new nodeExpressaoSimplesComOp();
            EsOp.next = null;
            EsOp.operador = currentTerminal.spelling;
            
            acceptIt();//consome OP_ADD
            
            EsOp.T = parseTermo();
            EsOp.line = previousTerminal.line;
            EsOp.col = previousTerminal.col;
            
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
        if(E.Es1 != null){
            E.line = E.Es1.line;
            E.col = E.Es1.col;
        }
        E.Es2 = null;
        if(currentTerminal.kind == Token.OP_REL){//op-rel
            E.operador = currentTerminal.spelling;
            acceptIt();
            E.Es2 = parseExpressaoSimples();
        }else if(currentTerminal.kind== Token.DO || //follow
                currentTerminal.kind== Token.THEN ||
                currentTerminal.kind== Token.RPAREN ||
                currentTerminal.kind== Token.SEMICOLON ||
                currentTerminal.kind == Token.ELSE){
            
        }else{
            Error.reportError(this.previousTerminal.line, this.previousTerminal.col,
                    "Sintatico",
                    "Expressao nao reconhecida.");
        }
        
        return E;
    }
    
    private nodeComando parseComando(){
        switch (currentTerminal.kind){
            case Token.IDENTIFIER: //atribuicao
                nodeComandoAtribuicao cmdId = new nodeComandoAtribuicao();
                cmdId.name = currentTerminal.spelling;
                cmdId.line = currentTerminal.line;
                cmdId.col = currentTerminal.col;
                acceptIt();
                
                accept(Token.BECOMES);
                
                cmdId.e = parseExpressao();
                return cmdId;
            case Token.IF: //condicional
                nodeComandoCond cmdCond = new nodeComandoCond();
                acceptIt();
                cmdCond.e = parseExpressao();
                
                accept(Token.THEN);
                cmdCond.c1 = parseComando();
                cmdCond.c2 = null;
                
                if(currentTerminal.kind== Token.ELSE){
                    acceptIt();
                    cmdCond.c2 = parseComando();
                }else if(currentTerminal.kind== Token.SEMICOLON){
                    //follow
                }else{
                    Error.reportError(this.previousTerminal.line, this.previousTerminal.col,
                    "Sintatico",
                    "Comando condicional nao reconhecido. Esperado: 'ELSE' ou ';'");
                }
                return cmdCond;
            case Token.WHILE: //iterativo
                nodeComandoIterativo cmdIt = new nodeComandoIterativo();
                acceptIt();
                
                cmdIt.e = parseExpressao();
                
                accept(Token.DO);
                cmdIt.c = parseComando();
                return cmdIt;
            case Token.BEGIN: //comandocomposto
                return parseComandoComposto();
            default:
                Error.reportError(this.previousTerminal.line, this.previousTerminal.col,
                    "Sintatico",
                    "Comando nao reconhecido.");
                return null;
        } 
    }
    
}
