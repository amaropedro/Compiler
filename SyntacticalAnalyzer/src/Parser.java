/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

/**
 *
 * @author Amaro
 */
public class Parser {
    private String currentTerminal;
    //private token ou terminalSymbol currentTerminal
    
    private void accept(String currentTerminal /*(token ou terminalSymbol currentTerminal*/){
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
        accept("program");
        accept("<identifier>");
        accept(";");
        parseCorpo();
        accept(".");
    }
    
    private void parseCorpo(){
        //(declaraVar ;)*
        while(currentTerminal == "var"){
            parseDeclaraVar();
            accept(";");
        }
        parseComandoComposto();
    }
    
    //eu sou juan hahaha
    
    private void parseDeclaraVar(){
        accept("var");
        accept("<identifier>");
        //(, <id>)*
        while(currentTerminal == ","){
            acceptIt();
            accept("<identifier>");
        }
        accept(":");
        parseTipoSimples();
    }
    
    private void parseComandoComposto(){
        accept("begin");
        //(comando ;)*
        while(currentTerminal == "<identifier>" || currentTerminal == "if" || 
                currentTerminal =="while" || currentTerminal == "begin"){
            parseComando();
            accept(";");
        }
        accept("end");
    }
    
    private void parseComando(){
        switch (currentTerminal){
            case "<identifier>":
                    acceptIt();
                    accept(":=");
                    parseExpressao();
                    break;
            case "if":
                //
                break;
            case "while":
                //
                break;
            case "begin":
                parseComandoComposto();
                break;
            default:
                break;
        }
                
        
    }
    
}
