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
public class nodeDeclaraVar { //Declarações de variaveis
    public String name; //Nome e tipo da variavel
    public String tipo;
    public nodeDeclaraVar next;  //Proxima variavel
    
    public void visit (Visitor v){
     v.visitDeclara(this);
    }
}