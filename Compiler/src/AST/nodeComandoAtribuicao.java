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
public class nodeComandoAtribuicao extends nodeComando{
    public String name;
    public nodeExpressao e;
    public int line;
    public int col;

    public void visit (Visitor v){
     v.visitCmdAtribuicao(this);
    }
}