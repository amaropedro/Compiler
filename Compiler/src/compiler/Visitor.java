/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package compiler;
import AST.*;

/**
 *
 * @author Amaro
 */
public interface Visitor {
    public void visitPrograma (nodePrograma p);
    public void visitCorpo (nodeCorpo c);
    public void visitDeclara (nodeDeclaraVar d);
    public void visitCmdComp (nodeComandoComposto CMD);
    public void visitCmd (nodeComando cmd);
    public void visitCmdAtribuicao (nodeComandoAtribuicao cmdAtribuicao);
    public void visitCmdCond (nodeComandoCond cmdCond);
    public void visitCmdIt (nodeComandoIterativo cmdIt);
    public void visitExpressao (nodeExpressao e);
    public void visitExpressaoSimples (nodeExpressaoSimples Es);
    public void visitExoressaoSimplesComOP (nodeExpressaoSimplesComOp EsOp);
    public void visitFator (nodeFator f);
    public void visitFatorBool (nodeFatorBool fBool);
    public void visitFatorComOp (nodeFatorComOp fOp);
    public void visitFatorExp (nodeFatorExp fExp);
    public void visitFatorFloat (nodeFatorFloat fFloat);
    public void visitFatorId (nodeFatorId fId);
    public void visitFatorInt (nodeFatorInt fInt);
    public void visitTermo (nodeTermo t);
}
