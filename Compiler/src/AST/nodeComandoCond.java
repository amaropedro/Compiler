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
public class nodeComandoCond extends nodeComando{
    public nodeExpressao e;
    public nodeComando c1;
    public nodeComando c2;
    
    public void visit (Visitor v){
     v.visitCmdCond(this);
    }
}
