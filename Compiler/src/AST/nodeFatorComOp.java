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
public class nodeFatorComOp {
    public nodeFator f;
    public String operador;
    public nodeFatorComOp next;
    public String tipo;
    public int line;
    public int col;
    
    public void visit (Visitor v){
     v.visitFatorComOp(this);
    }
}