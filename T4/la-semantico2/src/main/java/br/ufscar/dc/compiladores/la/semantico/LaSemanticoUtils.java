package br.ufscar.dc.compiladores.la.semantico;

import java.util.ArrayList;
import java.util.List;
import org.antlr.v4.runtime.Token;

public class LaSemanticoUtils {
   
    public static List<String> errosSemanticos = new ArrayList<>();
   
    // Metodo generico para adicionar os erros
    public static void adicionarErroSemantico(Token t, String mensagem) {
        int linha = t.getLine();
        
        errosSemanticos.add(String.format("Linha %d: %s", linha, mensagem));
    }
    
    // Mensagem de erro de tipo não declarado 
    public static void addErrTipoNaoDeclarado(Token t, String targetName) {
        adicionarErroSemantico(t, "tipo " + targetName + " nao declarado");
    }
    
    // Mensagem de erro de atribuição não compativel
    public static void addErrAtribuicaoNaoCompativel(Token t, String targetName) {
        adicionarErroSemantico(t, "atribuicao nao compativel para " + targetName);
    }
    
    // Mensagem de erro de identificador ja declarado anteriormente
    public static void addErrIdentificadorJaDeclarado(Token t, String targetName) {
        adicionarErroSemantico(t, "identificador " + targetName + " ja declarado anteriormente");
    }
    
    // Mensagem de erro de identificador não declarado
    public static void addErrIdentificadorNaoDeclarado(Token t, String targetName) {
        adicionarErroSemantico(t, "identificador " + targetName + " nao declarado");
    }
    
    // get dos erros
    public static List<String> getErrors() {
        return errosSemanticos;
    }
}