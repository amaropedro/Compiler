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
    private final ErrorPrinter E = ErrorPrinter.getInstance();

    
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
            if(ST.GetEntryByName(d.name) == null)
                ST.addToST(d);
            else{
                E.reportError(d.line, d.col, "Contextual", "variavel '" 
                        + d.name + "' ja declarada no escopo.");
            }
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
                E.reportError(cmdAtribuicao.line, cmdAtribuicao.col,
                        "Contextual", "variavel '"+cmdAtribuicao.name+"' nao declarada." ); 
            }
            
            if(cmdAtribuicao.e != null){
                cmdAtribuicao.e.visit(this);
                VariableList VL = ST.GetEntryByName(cmdAtribuicao.name);
                String tipo = cmdAtribuicao.e.tipo;
                if(VL!=null && tipo != null){
                    if(!ST.IsOfType(cmdAtribuicao.name, tipo)){
                        E.reportError(cmdAtribuicao.line, cmdAtribuicao.col,
                        "Contextual", "tipo incompativel." 
                        + " Esperado: '"+VL.d.tipo + "' recebeu: '"+tipo+"'");
                    }
                }
                
            }    
        }
        
    };
    
    @Override
    public void visitCmdCond (nodeComandoCond cmdCond){
        
        if(cmdCond != null){
            if(cmdCond.e != null){
                cmdCond.e.visit(this);
                if(!"bool".equals(cmdCond.e.tipo)){
                        E.reportError(cmdCond.e.line, cmdCond.e.col,
                        "Contextual", "tipo incompativel. Comando condicional "
                                + "esperava tipo bool e recebeu '" +cmdCond.e.tipo+"'"); 
                    }
            }
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
            if(cmdIt.e != null){
                cmdIt.e.visit(this);
                if(!"bool".equals(cmdIt.e.tipo)){
                        E.reportError(cmdIt.e.line, cmdIt.e.col,
                        "Contextual", "tipo incompativel. Comando iterativo "
                                + "esperava tipo bool e recebeu '" +cmdIt.e.tipo+"'"); 
                    }
            }
            if(cmdIt.c != null)
                cmdIt.c.visit(this);
        }
        
    };
    
    @Override
    public void visitExpressao (nodeExpressao e){
        String tipo1 = null, tipo2 = null, operador = null;
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
                
            if(e.operador != null){
                operador = e.operador;
            }
            
            if(tipo1 != null && tipo2 != null){
                if(!tipo1.equals(tipo2)){
                    e.tipo = "undefined";
                    E.reportError(e.line, e.col,
                        "Contextual", "operacoes entre '"+tipo1+"' e '"
                                +tipo2+"' nao sao compativeis");
                }
                
                if(">".equals(operador)||">=".equals(operador) ||
                        "<".equals(operador)||"<=".equals(operador)
                        || "<>".equals(operador) || "=".equals(operador))
                    e.tipo = "bool";
                
                if("and".equals(operador) || "or".equals(operador)){
                    if(!"bool".equals(tipo1)){
                        E.reportError(e.Es1.line, e.Es1.col, "Contextual", "operador '"
                            +operador + "' nao eh compativel com tipo '"+tipo1+"'");
                    }
                    if(!"bool".equals(tipo2)){
                        E.reportError(e.Es2.line, e.Es2.col, "Contextual", "operador '"
                            +operador + "' nao eh compativel com tipo '"+tipo2+"'");
                    }
                }else if (!"<>".equals(operador) && !"=".equals(operador)){
                    if("bool".equals(tipo1)){
                        E.reportError(e.Es1.line, e.Es1.col, "Contextual", "operador '"
                            +operador + "' nao eh compativel com tipo '"+tipo1+"'");
                    }
                    if("bool".equals(tipo2)){
                        E.reportError(e.Es2.line, e.Es2.col, "Contextual", "operador '"
                            +operador + "' nao eh compativel com tipo '"+tipo2+"'");
                    }
                }
            }
        }
    };
    
    @Override
    public void visitExpressaoSimples (nodeExpressaoSimples Es){
        String tipo1 = null, tipo2 = null, operador = null;
        if(Es != null){
            if(Es.T != null){
                Es.T.visit(this);
                tipo1 = Es.T.tipo;
                Es.tipo = tipo1;
            }
                
            if(Es.EsOp != null){
                Es.EsOp.visit(this);
                tipo2 = Es.EsOp.T.tipo;
                operador = Es.EsOp.operador;
            }
            
            if(tipo1 != null && tipo2 != null){
                if(!tipo1.equals(tipo2)){
                    Es.tipo = "undefined";
                    E.reportError(Es.line, Es.col,
                        "Contextual", "operacoes entre '"+tipo1+"' e '"
                                +tipo2+"' nao sao compativeis");
                }
                
                if(">".equals(operador)||">=".equals(operador) ||
                        "<".equals(operador)||"<=".equals(operador)
                        || "<>".equals(operador) || "=".equals(operador))
                    Es.tipo = "bool";
                
                if("and".equals(operador) || "or".equals(operador)){
                    if(!"bool".equals(tipo1)){
                        E.reportError(Es.line, Es.col, "Contextual", "operador '"
                            +operador + "' nao eh compativel com tipo '"+tipo1+"'");
                    }
                    if(!"bool".equals(tipo2)){
                        E.reportError(Es.EsOp.line, Es.EsOp.col, "Contextual", "operador '"
                            +operador + "' nao eh compativel com tipo '"+tipo2+"'");
                    }
                }else if (!"<>".equals(operador) && !"=".equals(operador)){
                    if("bool".equals(tipo1)){
                        E.reportError(Es.line, Es.col, "Contextual", "operador '"
                            +operador + "' nao eh compativel com tipo '"+tipo1+"'");
                    }
                    if("bool".equals(tipo2)){
                        E.reportError(Es.EsOp.line, Es.EsOp.col, "Contextual", "operador '"
                            +operador + "' nao eh compativel com tipo '"+tipo2+"'");
                    }
                }
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
        //
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
            VariableList VR = ST.GetEntryByName(fId.name);
            if(VR!=null){
                fId.tipo = VR.d.tipo;
            }else{
                E.reportError(fId.line, fId.col, "Contextual", "variavel '"+fId.name+"' nao declarada");
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
        String tipo1 = null, tipo2 = null, operador = null;
        if(t != null){
            if(t.f != null){
                t.f.visit(this);
                tipo1 = t.f.tipo;
                t.tipo = tipo1;
            }
                
            if(t.fOp != null){
                t.fOp.visit(this);
                tipo2 = t.fOp.f.tipo;
                operador = t.fOp.operador;
            }      
            
            if(tipo1 != null && tipo2 != null){
                if(!tipo1.equals(tipo2)){
                    t.tipo = "undefined";
                    E.reportError(t.line, t.col, "Contextual", "operacoes entre "
                            +tipo1+" e "+tipo2+" nao sao compativeis");
                }
                
                if(">".equals(operador)||">=".equals(operador) ||
                        "<".equals(operador)||"<=".equals(operador)
                        || "<>".equals(operador) || "=".equals(operador))
                    t.tipo = "bool";
                
                if("and".equals(operador) || "or".equals(operador)){
                    if(!"bool".equals(tipo1)){
                        E.reportError(t.line, t.col, "Contextual", "operador '"
                            +operador + "' nao eh compativel com tipo '"+tipo1+"'");
                    }
                    if(!"bool".equals(tipo2)){
                        E.reportError(t.fOp.line, t.fOp.col, "Contextual", "operador '"
                            +operador + "' nao eh compativel com tipo '"+tipo2+"'");
                    }
                }else if (!"<>".equals(operador) && !"=".equals(operador)){
                    if("bool".equals(tipo1)){
                        E.reportError(t.line, t.col, "Contextual", "operador '"
                            +operador + "' nao eh compativel com tipo '"+tipo1+"'");
                    }
                    if("bool".equals(tipo2)){
                        E.reportError(t.fOp.line, t.fOp.col, "Contextual", "operador '"
                            +operador + "' nao eh compativel com tipo '"+tipo2+"'");
                    }
                }
            }
        }
    };
}
