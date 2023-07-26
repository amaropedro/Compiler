/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package compiler;
import java.util.ArrayList;

/**
 *
 * @author Amaro
 */
public final class ErrorPrinter {
    public static ErrorPrinter instance;
    private final ArrayList<String> Erros = new ArrayList<>();
    
    public static ErrorPrinter getInstance(){
        if(instance == null){
            instance = new ErrorPrinter();
        }
        return instance;
    }
    
    public void reportError(int line, int col, String type, String msg){
        String error = "\nErro na linha: "+ line + " coluna: " + col + "\n" +
            type + ": " + msg;
        //check if error is already on list
        this.Erros.add(error);
        System.out.println("entrou");
    }
    
    public void printErrors(){
        if(this.Erros.isEmpty()){
            System.out.println("Sucesso!");
        }else{
            System.out.println(this.Erros.size() + " Erros detectados:");
            System.out.println(this.Erros);
        }
    }
}
