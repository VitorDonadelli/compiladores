package br.ufscar.dc.compiladores.la.lexico;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.Token;

public class Principal {
    
    public static void main(String[] args) throws IOException {
        
        // Inicia o FileWriter pra escrever no arquivo passado como entrada
        FileWriter fw = new FileWriter(new File(args[1]));

        // Construção do lexer 
        CharStream cs = CharStreams.fromFileName(args[0]);
        LaLexer lex = new LaLexer(cs);
        
        // Criação do Token
        Token t = null;
                
        while ((t = lex.nextToken()).getType() != Token.EOF) {
            String s = LaLexer.VOCABULARY.getSymbolicName(t.getType());
                    
            // Caso seja um TOKEN genérico criado na gramatica, ou seja
            //if (s.equals("PALAVRA_CHAVE") || s.equals("DECLARACAO") || s.equals("OPERADOR_LOGICO") ||
            //    s.equals("VALOR_BOOLEANO") || s.equals("OPERADOR_COMPARACAO") || s.equals("OPERADOR_ARITMETICO") || s.equals("CARACTERES"))
            if (s.equals("TOKEN")) {
                // Retornaremos no seguinte exemplo: <'algoritmo','algoritmo'>
                fw.write("<'" + t.getText() + "','" + t.getText() + "'>\n");
            } 
            
            // Caso seja um numero inteiro, numero real, cadeia ou um identificador
            else if (s.equals("NUM_INT") || s.equals("NUM_REAL") || s.equals("CADEIA") || s.equals("IDENT")) {
                 // Retornaremos no seguinte exemplo: <'nome',IDENT>
                fw.write("<'" + t.getText() + "'," + LaLexer.VOCABULARY.getSymbolicName(t.getType()) + ">\n");
            } 
  
            // Verificação de Erros
            // Comentário não fechado na mesma linha
            else if (s.equals("COMENTARIO_NAO_FECHADO")) {
                // Retorno: Linha X: comentario nao fechado
                fw.write("Linha " + t.getLine() + ": comentario nao fechado\n");
                break;    
            }       
            
            // Cadeia não fechada na mesma linha
            else if (s.equals("CADEIA_NAO_FECHADA")) {
                // Retorno: Linha X: cadeia literal nao fechada
                fw.write("Linha " + t.getLine() + ": cadeia literal nao fechada\n");
                break;
            } 
            
            // Simbolo que não encaixa nas definições da gramática
            else if (s.equals("SIMBOLO_NAO_IDENTIFICADO")) {
                // Retorno: Linha X: SIMBOLO - simbolo nao identificado
                fw.write("Linha " + t.getLine() + ": " + t.getText() + " - simbolo nao identificado\n");
                break;
            }
        }
        // Fecha o arquivo de escrita
        fw.close();
    }
}