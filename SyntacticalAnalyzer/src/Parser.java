/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

/**
 *
 * @author Amaro
 */
public class Parser {
    //private token ou terminalSymbol currentTerminal
    
    private void accept( /*(token ou terminalSymbol currentTerminal*/){
        /*
        if(currentTerminal == expectedTerminal)
            currentTerminal = next terminal
        else
            syntactic erro
        */
    }
    
    private void acceptIt(){
        //currentTerminal = next terminal
    }
    
    private void parseProgram(){
        accept('program');
        parseID();
        accept(';');
        parseCorpo();
        accept('.');
    }
    
    private void parseID(){
        
    }
    
    private void parseCorpo(){
    
    }
    
}
