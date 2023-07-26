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
public class nodeTermo {
    public nodeFator f;
    public nodeFatorComOp fOp;
    public String tipo;
    public int line;
    public int col;
    
    public void visit (Visitor v){
        v.visitTermo(this);
    }
}