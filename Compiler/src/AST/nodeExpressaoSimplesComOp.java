/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package AST;

import compiler.Visitor;

/**
 *
 * @author Amaro
 */
public class nodeExpressaoSimplesComOp {
    public nodeTermo T;
    public String operador;
    public nodeExpressaoSimplesComOp next;
    public int line;
    public int col;
    
    public void visit (Visitor v){
     v.visitExoressaoSimplesComOP(this);
    }
}