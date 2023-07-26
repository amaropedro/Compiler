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
    private final SimbolTable ST = new SimbolTable();

    
    public void check(nodePrograma p){
        System.out.println ("\n---> Iniciando analise de contexto");
        p.visit(this);
        ST.printST();
        System.out.println("\n----Fim da analise de contexto----");
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
                c.d.visit(this);
            }
                
            if(c.CMD != null )
                c.CMD.visit(this);
        }
    };
    
    @Override
    public void visitDeclara (nodeDeclaraVar d){
        if(d != null){
            ST.addToST(d);
            if(d.next != null){
                d.next.visit(this);
            }
        }
    };
    
    @Override
    public void visitCmdComp (nodeComandoComposto CMD){
        if(CMD != null){
            if(CMD.firstcmd != null){
                CMD.firstcmd.visit(this);
            }
        }
    };
    
    @Override
    public void visitCmd (nodeComando cmd){
        if(cmd.cmd != null){
            cmd.cmd.visit(this);
            if(cmd.next != null){
                cmd.next.visit(this);
            }
        } 
    };
    
    @Override
    public void visitCmdAtribuicao (nodeComandoAtribuicao cmdAtribuicao){
        
        if(cmdAtribuicao != null){
            if(!ST.IsDeclared(cmdAtribuicao.name)){
                System.out.println("Erro na linha: "
                        +cmdAtribuicao.line+" coluna: "+cmdAtribuicao.col);
                System.out.println("contextual: variavel '"
                        + cmdAtribuicao.name +"' nao declarada.");   
            }
            
            if(cmdAtribuicao.e != null){
                cmdAtribuicao.e.visit(this);
                VariableList VL = ST.GetSTByName(cmdAtribuicao.name);
                String tipo = cmdAtribuicao.e.tipo;
                if(VL!=null && tipo != null){
                    if(!ST.IsOfType(cmdAtribuicao.name, tipo)){
                        System.out.println("Erro na linha: "
                            +cmdAtribuicao.line+" coluna: "+cmdAtribuicao.col);
                        System.out.println("erro contextual: tipo incompativel." 
                        + " Esperado: '"+VL.d.tipo + "' recebeu: '"+tipo+"'");
                    }
                }
                
            }    
        }
        
    };
    
    @Override
    public void visitCmdCond (nodeComandoCond cmdCond){
        
        if(cmdCond != null){
            if(cmdCond.e != null)
                cmdCond.e.visit(this);
            if(cmdCond.c1 != null)
                cmdCond.c1.visit(this);
            if(cmdCond.c2 != null){
                cmdCond.c2.visit(this);
            }
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
                
            if(e.operador != null){}
            
            if(tipo1 != null && tipo2 != null)
                if(!tipo1.equals(tipo2)){
                    System.out.println("Erro na linha: "
                        +e.line+" coluna: "+e.col);
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
                    System.out.println("Erro na linha: "
                        +Es.line+" coluna: "+Es.col);
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
            if(EsOp.operador != null){}
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
        }
    };
    
    @Override
    public void visitFatorComOp (nodeFatorComOp fOp){
        if(fOp != null){
            if(fOp.f != null)
                fOp.f.visit(this);
            if(fOp.next != null)
                fOp.next.visit(this);
            if(fOp.operador != null){}
        }
    };
    
    @Override
    public void visitFatorExp (nodeFatorExp fExp){
        if(fExp != null){
            fExp.E.visit(this);
            fExp.tipo = fExp.E.tipo;
        }
    };
    
    @Override
    public void visitFatorFloat (nodeFatorFloat fFloat){
        if(fFloat != null){
            fFloat.tipo = "real";
        }
    };
    
    @Override
    public void visitFatorId (nodeFatorId fId){
        if(fId != null){
            VariableList VR = ST.GetSTByName(fId.name);
            if(VR!=null){
                fId.tipo = VR.d.tipo;
            }else{
                System.out.println("Erro na linha: "
                    +fId.line+" coluna: "+fId.col);
                System.out.println("Contexto: variavel nao declarada");
            }     
        }      
    };
    
    @Override
    public void visitFatorInt (nodeFatorInt fInt){
        if(fInt != null){
            fInt.tipo = "int";
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
                    System.out.println("Erro na linha: "
                        +t.line+" coluna: "+t.col);
                    System.out.println("erro contextual: operacoes entre "
                            +tipo1+" e "+tipo2+" nao sao compativeis");
                }
        }
    };
}