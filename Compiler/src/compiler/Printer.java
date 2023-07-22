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
    
    @Override
    public void visitPrograma (nodePrograma p){
        System.out.println ("---> Iniciando analise sintatica");
        p.visit(this);
    };
    @Override
    public void visitCorpo (nodeCorpo c){
        
    };
    @Override
    public void visitDeclara (nodeDeclaraVar d){
        
    };
    @Override
    public void visitCmdComp (nodeComandoComposto CMD){
        
    };
    @Override
    public void visitCmd (nodeComando cmd){
        
    };
    @Override
    public void visitCmdAtribuicao (nodeComandoAtribuicao cmdAtribuicao){
        
    };
    @Override
    public void visitCmdCond (nodeComandoCond cmdCond){
        
    };
    @Override
    public void visitCmdIt (nodeComandoIterativo cmdIt){
        
    };
    @Override
    public void visitExpressao (nodeExpressao e){
        
    };
    @Override
    public void visitExpressaoSimples (nodeExpressaoSimples Es){
        
    };
    @Override
    public void visitExoressaoSimplesComOP (nodeExpressaoSimplesComOp EsOp){
        
    };
    @Override
    public void visitFator (nodeFator f){
        
    };
    @Override
    public void visitFatorBool (nodeFatorBool fBool){
        
    };
    @Override
    public void visitFatorComOp (nodeFatorComOp fOp){
        
    };
    @Override
    public void visitFatorExp (nodeFatorExp fExp){
        
    };
    @Override
    public void visitFatorFloat (nodeFatorFloat fFloat){
        
    };
    @Override
    public void visitFatorId (nodeFatorId fId){
        
    };
    @Override
    public void visitFatorInt (nodeFatorInt fInt){
        
    };
    @Override
    public void visitTermo (nodeTermo t){
        
    };
}
