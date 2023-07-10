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
        accept("<identifier>"); //mudar para parseId();?
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
    
    private void parseDeclaraVar(){
        accept("var");
        accept("<identifier>");//mudar para parseId();?
        //(, <id>)*
        while(currentTerminal == ","){
            acceptIt();
            accept("<identifier>"); //mudar para parseId();?
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
    
    private void parseFator(){
        switch (currentTerminal){
            case "<identifier>":
                acceptIt(); //mudar para parseId();
                break;
            case "<literal>":
                parseLiteral();
                break;
            case "(":
                acceptIt();
                parseExpressao();
                accept(")");
                break;
        }
    }
    
    private void parseTermo(){
        parseFator();
        while(currentTerminal =="*" || currentTerminal =="/" || currentTerminal =="and"){
            parseOp-Mul()//Ou só aceita?
            parseFator();
        }
    }
    
    private void parseExpressaoSimples(){
        parseTermo();
        while(currentTerminal=="+" || currentTerminal =="-" || currentTerminal =="or"){ //op-ad
            parseOp-Ad(); //Ou só aceita?
            parseTermo();
        }
    }
    
    private void parseExpressao(){
        parseExpressaoSimples();
        if(currentTerminal=="<" || currentTerminal ==">" || currentTerminal=="="){//op-rel
            parseOp-Rel();//Ou só aceita?
            parseExpressaoSimples();
        }else if(currentTerminal =="do" || currentTerminal =="then" || currentTerminal ==")" || currentTerminal ==";"){
            //follow
        }else{
            //erro
        }
    }
    
    private void parseComando(){
        switch (currentTerminal){
            case "<identifier>": //atribuicao
                acceptIt();
                accept(":=");
                parseExpressao();
                break;
            case "if": //condicional
                acceptIt();
                parseExpressao();
                accept("then");
                parseComando();
                if(currentTerminal=="else"){
                    acceptIt();
                    parseComando();//Colocar else com erro?
                }else if(currentTerminal ==";"){
                    //follow
                }else{
                    //erro
                }
                break;
            case "while": //iterativo
                acceptIt();
                parseExpressao();
                accept("do");
                parseComando();
                break;
            case "begin": //comandocomposto
                parseComandoComposto();
                break;
            default:
                break; //ERRO
        } 
    }
    
}
