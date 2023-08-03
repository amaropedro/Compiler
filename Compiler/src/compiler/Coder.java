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
public class Coder implements Visitor{
    private String jumpAdr = null;
    private int currentAdr = 0;
    
    private void printCode(String msg){
        
        if(this.jumpAdr!= null)
            System.out.println(this.jumpAdr+" | "+msg);
        else
            System.out.println("      | "+msg);
        
        this.jumpAdr = null;
    }
    
    private String getAdrLabel(){
        this.currentAdr++;
        String adr;
        
        if(this.currentAdr > 99)
            adr = "E_"+this.currentAdr;
        else if(this.currentAdr > 9)
            adr = "E_0"+this.currentAdr;
        else
            adr = "E_00"+this.currentAdr;
        
        return adr;
    }
    
    public void code(nodePrograma p){
        System.out.println ("\n---> Iniciando geracao de codigo");
        p.visit(this);
        System.out.println("\n----Fim da geracao de codigo----");
    }
      
    @Override
    public void visitPrograma (nodePrograma p){
        if(p!=null){
            System.out.println("run "+p.progamName);
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
            if(d.next != null){
                printCode("Declara "+d.name+" com Size "+d.tipo);
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
            if(cmdAtribuicao.e != null){
                cmdAtribuicao.e.visit(this);
                printCode("STORE "+cmdAtribuicao.name);
            }    
        }
        
    };
    
    @Override
    public void visitCmdCond (nodeComandoCond cmdCond){
        String elseAdr = getAdrLabel(), endAdr = getAdrLabel();
        if(cmdCond != null){
            if(cmdCond.e != null){
                cmdCond.e.visit(this);
                printCode("JUMPIF(0) "+elseAdr);
            }
            if(cmdCond.c1 != null)
                cmdCond.c1.visit(this);
            printCode("JUMP "+endAdr);
            if(cmdCond.c2 != null){
                this.jumpAdr =elseAdr;
                printCode("");
                cmdCond.c2.visit(this);
                this.jumpAdr = endAdr;
                printCode("");
            }
        }
        
    };
    
    @Override
    public void visitCmdIt (nodeComandoIterativo cmdIt){
        String startAdr = getAdrLabel(), endAdr = getAdrLabel();
        if(cmdIt != null){
            this.jumpAdr = startAdr;
            printCode("");
            if(cmdIt.e != null){
                cmdIt.e.visit(this);
            }
            printCode("JUMPIF(0)"+endAdr);
            if(cmdIt.c != null)
                cmdIt.c.visit(this);
            printCode("JUMP "+startAdr);
            this.jumpAdr = endAdr;
            printCode("");
        }
        
    };
    
    @Override
    public void visitExpressao (nodeExpressao e){
        if(e != null){
            if(e.Es1 != null){
                e.Es1.visit(this);
            }
            if(e.Es2 != null){
                e.Es2.visit(this);
            }
            if(e.operador != null)
                printCode("CALL "+e.operador);
        }
    };
    
    @Override
    public void visitExpressaoSimples (nodeExpressaoSimples Es){
        if(Es != null){
            if(Es.T != null){
                Es.T.visit(this);
            }
                
            if(Es.EsOp != null){
                Es.EsOp.visit(this);
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
            if(EsOp.operador != null){
                String op="";
                switch (EsOp.operador){
                    case "-":
                        op = "sub";
                        break;
                    case "+":
                        op = "add";
                        break;
                    case "/":
                        op = "div";
                        break;
                    case "*":
                        op = "mult";
                        break;
                    case ">":
                        op = "gt";
                        break;
                    case "<":
                        op = "lt";
                        break;
                    case ">=":
                        op = "get";
                        break;
                    case "<=":
                        op = "let";
                        break;
                    case "<>":
                        op = "dif";
                    default:
                        break;
                }
                printCode("CALL "+op);
            }
        }
    };
    
    @Override
    public void visitFator (nodeFator f){
        //
    };
    
    @Override
    public void visitFatorBool (nodeFatorBool fBool){
        if(fBool != null){
            printCode("LOAD bool " +fBool.bool);
        }
    };
    
    @Override
    public void visitFatorComOp (nodeFatorComOp fOp){
        if(fOp != null){
            if(fOp.f != null)
                fOp.f.visit(this);
            if(fOp.next != null)
                fOp.next.visit(this);
            if(fOp.operador != null){
                printCode("CALL "+fOp.operador);
            }
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
            printCode("LOADL "+fFloat.numReal);
        }
    };
    
    @Override
    public void visitFatorId (nodeFatorId fId){
        if(fId != null){
            printCode("LOAD "+fId.name);
        }      
    };
    
    @Override
    public void visitFatorInt (nodeFatorInt fInt){
        if(fInt != null){
            printCode("LOADL " +fInt.num);
        }
    };
    
    @Override
    public void visitTermo (nodeTermo t){
        if(t != null){
            if(t.f != null){
                t.f.visit(this);
            }
                
            if(t.fOp != null){
                t.fOp.visit(this);
            }      
        }
    };
}
