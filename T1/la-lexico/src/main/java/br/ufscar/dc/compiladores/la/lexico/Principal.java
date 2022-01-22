package br.ufscar.dc.compiladores.la.lexico;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.Token;

public class Principal {
    
    public static void main(String[] args) throws IOException {
        
        FileWriter fw = new FileWriter(new File(args[1]));

        CharStream cs = CharStreams.fromFileName(args[0]);
        LaLexer lex = new LaLexer(cs);
        
        Token t = null;
                
        while ((t = lex.nextToken()).getType() != Token.EOF) {
            String s = LaLexer.VOCABULARY.getSymbolicName(t.getType());
            // Exemplo de Retorno = <'algoritmo','algoritmo'>
            //if (s.equals("PALAVRA_CHAVE") || s.equals("DECLARACAO") || s.equals("OPERADOR_LOGICO") || s.equals("VALOR_BOOLEANO") || s.equals("OPERADOR_COMPARACAO") || s.equals("OPERADOR_ARITMETICO") || s.equals("CARACTERES")) {
            if (s.equals("TOKEN")) {
                fw.write("<'" + t.getText() + "','" + t.getText() + "'>\n");
            } 
            // Exemplo de Retorno = <'idade',IDENT>
            else if (s.equals("NUM_INT") || s.equals("NUM_REAL") || s.equals("CADEIA") || s.equals("IDENT")) {
                fw.write("<'" + t.getText() + "'," + LaLexer.VOCABULARY.getSymbolicName(t.getType()) + ">\n");
            } 
  
            // Verificação de Erros
            else if (s.equals("COMENTARIO_NAO_FECHADO")) {
                fw.write("Linha " + t.getLine() + ": comentario nao fechado\n");
                break;    
            }                
            else if (s.equals("CADEIA_NAO_FECHADA")) {
                fw.write("Linha " + t.getLine() + ": cadeia literal nao fechada\n");
                break;
            } 
            else if (s.equals("SIMBOLO_NAO_IDENTIFICADO")) {
                fw.write("Linha " + t.getLine() + ": " + t.getText() + " - simbolo nao identificado\n");
                break;
            }
        }                   
        fw.close();
    }
}