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
public class nodeExpressao {
    public nodeExpressaoSimples Es1;
    public String operador;
    public nodeExpressaoSimples Es2;
    public String tipo;
    //linha e col tbm
    
    public void visit (Visitor v){
     v.visitExpressao(this);
    }
}