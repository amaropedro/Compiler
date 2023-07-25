/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package compiler;

import AST.*;
import contextanalyzer.*;

/**
 *
 * @author Amaro
 */
public class Checker implements Visitor{
    private int indentAmount = 0;
    private final SimbolTable ST = new SimbolTable();

    
    public void check(nodePrograma p){
        System.out.println ("\n---> Iniciando analise de contexto");
        p.visit(this);
        ST.printST();
        System.out.println("\n----Fim da analise de contexto----");
    }
    
    private void indent(){
        System.out.print("\n");
        for(int i=0; i < indentAmount; i++){
            System.out.print("  ");
        }
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
            if(c.d != null){
                System.out.println("Declaracoes:");
                c.d.visit(this);
                System.out.println("---------");
                System.out.println("Corpo:");
            }
                
            if(c.CMD != null )
                c.CMD.visit(this);
        }
    };
    
    @Override
    public void visitDeclara (nodeDeclaraVar d){
        if(d != null){
            ST.addToST(d);
            System.out.println("var " + d.name + ": " + d.tipo);
            if(d.next != null){
                d.next.visit(this);
            }
        }
    };
    
    @Override
    public void visitCmdComp (nodeComandoComposto CMD){
        if(CMD != null){
            System.out.println("BEGIN");
            if(CMD.firstcmd != null){
                CMD.firstcmd.visit(this);
            }
            indent();
            System.out.print("END\n");
        }
    };
    
    @Override
    public void visitCmd (nodeComando cmd){
        if(cmd.cmd != null){
            cmd.cmd.visit(this);
            System.out.print("\n");
            if(cmd.next != null){
                cmd.next.visit(this);
            }
        } 
    };
    
    @Override
    public void visitCmdAtribuicao (nodeComandoAtribuicao cmdAtribuicao){
        indentAmount++;
        if(cmdAtribuicao != null){
            indent();
            System.out.print(cmdAtribuicao.name +" := ");
            if(!ST.IsDeclared(cmdAtribuicao.name))
                System.out.println("erro contextual: variavel '"
                        + cmdAtribuicao.name +"' nao declarada.");
            
            if(cmdAtribuicao.e != null){
                cmdAtribuicao.e.visit(this);
                
                VariableList vl = ST.GetSTByName(cmdAtribuicao.name);
                String tipo = cmdAtribuicao.e.tipo;
                if( vl!= null &&
                    !ST.IsOfType(cmdAtribuicao.name, tipo)){
                    System.out.println("erro contextual: tipo incompativel. " +
                    "esperado: '"+vl.d.tipo + "' recebeu: '"+tipo+"'");
                }
            }    
        }
        indentAmount--;
    };
    
    @Override
    public void visitCmdCond (nodeComandoCond cmdCond){
        indentAmount++;
        if(cmdCond != null){
            indent();
            System.out.print("if (");
            if(cmdCond.e != null)
                cmdCond.e.visit(this);
            System.out.print(") then ");
            if(cmdCond.c1 != null)
                cmdCond.c1.visit(this);
            if(cmdCond.c2 != null){
                indent();
                System.out.print("else ");
                cmdCond.c2.visit(this);
            }
        }
        indentAmount--;
    };
    
    @Override
    public void visitCmdIt (nodeComandoIterativo cmdIt){
        indentAmount++;
        if(cmdIt != null){
            indent();
            System.out.print("while (");
            if(cmdIt.e != null)
                cmdIt.e.visit(this);
            System.out.print(") do ");
            if(cmdIt.c != null)
                cmdIt.c.visit(this);
        }
        indentAmount--;
    };
    
    @Override
    public void visitExpressao (nodeExpressao e){
        String tipo1 = null, tipo2 = null;
        if(e != null){
            if(e.Es1 != null){
                e.Es1.visit(this);
                tipo1 = e.Es1.tipo;
                e.tipo = tipo1;
            }
            if(e.Es2 != null){
                e.Es2.visit(this);
                tipo2 = e.Es2.tipo;
            }
                
            if(e.operador != null)
                System.out.print(" "+e.operador+" ");
            
            if(tipo1 != null && tipo2 != null)
                if(!tipo1.equals(tipo2)){
                    System.out.println("erro contextual: operacoes entre "
                            +tipo1+" e "+tipo2+" nao sao compativeis");
                }
        }
    };
    
    @Override
    public void visitExpressaoSimples (nodeExpressaoSimples Es){
        String tipo1 = null, tipo2 = null;
        if(Es != null){
            if(Es.T != null){
                Es.T.visit(this);
                tipo1 = Es.T.tipo;
                Es.tipo = tipo1;
            }
                
            if(Es.EsOp != null){
                Es.EsOp.visit(this);
                tipo2 = Es.EsOp.T.tipo;
            }
            
            if(tipo1 != null && tipo2 != null)
                if(!tipo1.equals(tipo2)){
                    System.out.println("erro contextual: operacoes entre "
                            +tipo1+" e "+tipo2+" nao sao compativeis");
                }
        }
    };
    
    @Override
    public void visitExoressaoSimplesComOP (nodeExpressaoSimplesComOp EsOp){
        if(EsOp != null){
            if(EsOp.T != null){
                EsOp.T.visit(this); 
            }
            if(EsOp.next != null)
                EsOp.next.visit(this);
            if(EsOp.operador != null)
                System.out.print(EsOp.operador+" ");
        }
    };
    
    @Override
    public void visitFator (nodeFator f){
        if(f != null)
            f.visit(this);
    };
    
    @Override
    public void visitFatorBool (nodeFatorBool fBool){
        if(fBool != null){
            fBool.tipo = "bool";
            fBool.visit(this);   
        }
    };
    
    @Override
    public void visitFatorComOp (nodeFatorComOp fOp){
        if(fOp != null){
            if(fOp.f != null)
                fOp.f.visit(this);
            if(fOp.next != null)
                fOp.next.visit(this);
            if(fOp.operador != null)
                System.out.print(fOp.operador+" ");
        }
    };
    
    @Override
    public void visitFatorExp (nodeFatorExp fExp){
        if(fExp != null){
            System.out.print("(");
            fExp.E.visit(this);
            fExp.tipo = fExp.E.tipo;
            System.out.print(")");
        }
    };
    
    @Override
    public void visitFatorFloat (nodeFatorFloat fFloat){
        if(fFloat != null){
            fFloat.tipo = "real";
            System.out.print("|"+fFloat.numReal+"| ");
        }
    };
    
    @Override
    public void visitFatorId (nodeFatorId fId){
        if(fId != null){
            fId.tipo = ST.GetSTByName(fId.name).d.tipo;
            System.out.print("|"+fId.name+"| ");
        }      
    };
    
    @Override
    public void visitFatorInt (nodeFatorInt fInt){
        if(fInt != null){
            fInt.tipo = "int";
            System.out.print("|"+fInt.num+"| ");
        }
    };
    
    @Override
    public void visitTermo (nodeTermo t){
        String tipo1 = null, tipo2 = null;
        if(t != null){
            if(t.f != null){
                t.f.visit(this);
                tipo1 = t.f.tipo;
                t.tipo = tipo1;
            }
                
            if(t.fOp != null){
                t.fOp.visit(this);
                tipo2 = t.fOp.f.tipo;
            }      
            
            if(tipo1 != null && tipo2 != null)
                if(!tipo1.equals(tipo2)){
                    System.out.println("erro contextual: operacoes entre "
                            +tipo1+" e "+tipo2+" nao sao compativeis");
                }
        }
    };
}
