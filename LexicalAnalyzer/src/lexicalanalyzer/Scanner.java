/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package lexicalanalyzer;

/**
 *
 * @author Amaro
 */
public class Scanner {
    private char currentChar; //= first source char
    private byte currentKind;
    private StringBuffer currentSpelling;
    
    private void take (char expectedChar){
        if (currentChar == expectedChar){
            currentSpelling.append(currentChar);
            //currentChar = next char
        }
        else{
            //erro
        }
    }
    
    private void take
}
