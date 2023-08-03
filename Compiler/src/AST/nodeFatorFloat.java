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
public class nodeFatorFloat extends nodeFator{
    public String numReal;
    
    public void visit (Visitor v){
       v.visitFatorFloat(this);
    }
}