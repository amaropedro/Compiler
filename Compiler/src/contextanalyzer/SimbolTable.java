/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package contextanalyzer;

/**
 *
 * @author Amaro
 */
public class SimbolTable {
    private VariableList ST, firstST = null, lastST = null;
    
    public void addToST(VariableList newST){
        if(firstST == null){
            firstST = newST;
        }else{
            lastST.next = newST;
        }
        lastST = newST;
    }
    
    public void printST(){
        ST = firstST;
        System.out.println("\n------------------");
        System.out.println("TABELA DE SIMBOLOS");
        System.out.println("------------------");
        while(ST != null){
            System.out.println(ST.d.name +" | "+ST.d.tipo);
            ST = ST.next;
        }
    }
    
    public VariableList GetSTByName(String name){
        ST = firstST;
        while(ST != null){
            if(name.equals(ST.d.name))
                return ST;
            ST = ST.next;
        }
        return null;
    }
    
    public boolean IsDeclared(String name){
        return !(GetSTByName(name) == null);
    }
    
    public boolean IsOfType(String name, String type){
        ST = GetSTByName(name);
        return type.equals(ST.d.tipo);
    }
}
