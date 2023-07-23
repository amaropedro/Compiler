/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package compiler;

import AST.*;

/**
 *
 * @author Amaro
 */
public class Printer implements Visitor{
    
    public void print(nodePrograma p){
        System.out.println ("---> Iniciando analise sintatica");
        p.visit(this);
        System.out.println("\n----Fim da analise sintatica----");
    }
    
    @Override
    public void visitPrograma (nodePrograma p){
        if(p!=null){
            if(p.c!=null)
                p.c.visit(this);
        }
    };
    
    @Override
    public void visitCorpo (nodeCorpo c){
        if(c != null){
            if(c.d != null)
                c.d.visit(this);
            if(c.CMD != null )
                c.CMD.visit(this);
        }
    };
    
    @Override
    public void visitDeclara (nodeDeclaraVar d){
        if(d != null){
            System.out.println("var " + d.name + ": " + d.tipo);
            if(d.next != null){
                d.next.visit(this);
            }
        }
    };
    
    @Override
    public void visitCmdComp (nodeComandoComposto CMD){
        if(CMD != null){
            if(CMD.cmd != null){
                CMD.cmd.visit(this);
                if(CMD.next != null){
                    CMD.next.visit(this);
                }
            }
        }
    };
    
    @Override
    public void visitCmd (nodeComando cmd){
        if(cmd != null)
            cmd.visit(this);
    };
    
    @Override
    public void visitCmdAtribuicao (nodeComandoAtribuicao cmdAtribuicao){
        if(cmdAtribuicao != null){
            System.out.print("\n"+cmdAtribuicao.name +" := ");
            if(cmdAtribuicao.e != null)
                cmdAtribuicao.e.visit(this);
        }
    };
    
    @Override
    public void visitCmdCond (nodeComandoCond cmdCond){
        if(cmdCond != null){
            if(cmdCond.e != null)
                cmdCond.e.visit(this);
            if(cmdCond.c1 != null)
                cmdCond.c1.visit(this);
            if(cmdCond.c2 != null)
                cmdCond.c2.visit(this);
        }
    };
    
    @Override
    public void visitCmdIt (nodeComandoIterativo cmdIt){
        if(cmdIt != null){
            if(cmdIt.e != null)
                cmdIt.e.visit(this);
            if(cmdIt.c != null)
                cmdIt.c.visit(this);
        }
    };
    
    @Override
    public void visitExpressao (nodeExpressao e){
        if(e != null){
            if(e.Es1 != null)
                e.Es1.visit(this);
            if(e.operador != null)
                System.out.print(" "+e.operador+" ");
            if(e.Es2 != null)
                e.Es2.visit(this);
        }
    };
    
    @Override
    public void visitExpressaoSimples (nodeExpressaoSimples Es){
        if(Es != null){
            if(Es.T != null)
                Es.T.visit(this);
            if(Es.EsOp != null)
                Es.EsOp.visit(this);
        }
    };
    
    @Override
    public void visitExoressaoSimplesComOP (nodeExpressaoSimplesComOp EsOp){
        if(EsOp != null){
            if(EsOp.T != null)
                EsOp.T.visit(this);
            System.out.print(" "+EsOp.operador+" ");
            if(EsOp.next != null)
                EsOp.next.visit(this);
        }
    };
    
    @Override
    public void visitFator (nodeFator f){
        if(f != null)
            f.visit(this);
    };
    
    @Override
    public void visitFatorBool (nodeFatorBool fBool){
        if(fBool != null)
            fBool.visit(this);
    };
    
    @Override
    public void visitFatorComOp (nodeFatorComOp fOp){
        if(fOp != null){
            if(fOp.f != null)
                fOp.f.visit(this);
            System.out.print(" "+fOp.operador+" ");
            if(fOp.next != null)
                fOp.next.visit(this);
        }
    };
    
    @Override
    public void visitFatorExp (nodeFatorExp fExp){
        if(fExp != null)
            fExp.E.visit(this);
    };
    
    @Override
    public void visitFatorFloat (nodeFatorFloat fFloat){
        if(fFloat != null)
            System.out.print(fFloat.numReal+" ");
    };
    
    @Override
    public void visitFatorId (nodeFatorId fId){
        if(fId != null)
            System.out.print(fId.name+" ");
    };
    
    @Override
    public void visitFatorInt (nodeFatorInt fInt){
        if(fInt != null)
            System.out.print(fInt.num+" ");
    };
    
    @Override
    public void visitTermo (nodeTermo t){
        if(t != null){
            if(t.f != null)
                t.f.visit(this);
            if(t.fOp != null)
                t.fOp.visit(this);
        }
    };
}
