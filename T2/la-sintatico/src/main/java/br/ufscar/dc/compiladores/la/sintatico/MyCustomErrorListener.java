package br.ufscar.dc.compiladores.la.sintatico;

import java.io.PrintWriter;
import java.util.BitSet;
import org.antlr.v4.runtime.ANTLRErrorListener; // cuidado para importar a versão 4
import org.antlr.v4.runtime.Parser;
import org.antlr.v4.runtime.RecognitionException;
import org.antlr.v4.runtime.Recognizer;
import org.antlr.v4.runtime.Token; // Vamos também precisar de Token
import org.antlr.v4.runtime.atn.ATNConfigSet;
import org.antlr.v4.runtime.dfa.DFA;
// Outros imports vão ser necessários aqui. O NetBeans ou IntelliJ fazem isso automaticamente

public class MyCustomErrorListener implements ANTLRErrorListener {
    PrintWriter pw;
    public MyCustomErrorListener(PrintWriter pw){
        this.pw = pw;
    }
    @Override
    public void	reportAmbiguity(Parser recognizer, DFA dfa, int startIndex, int stopIndex, boolean exact, BitSet ambigAlts, ATNConfigSet configs) {
        // Não será necessário para o T2, pode deixar vazio
    }
    
    @Override
    public void reportAttemptingFullContext(Parser recognizer, DFA dfa, int startIndex, int stopIndex, BitSet conflictingAlts, ATNConfigSet configs) {
        // Não será necessário para o T2, pode deixar vazio
    }

    @Override
    public void reportContextSensitivity(Parser recognizer, DFA dfa, int startIndex, int stopIndex, int prediction, ATNConfigSet configs) {
        // Não será necessário para o T2, pode deixar vazio
    }

    @Override
    public void	syntaxError(Recognizer<?,?> recognizer, Object offendingSymbol, int line, int charPositionInLine, String msg, RecognitionException e) {
        // Aqui vamos colocar o tratamento de erro customizado
        Token t = (Token) offendingSymbol;
        
        String s = new String(LaLexer.VOCABULARY.getDisplayName(t.getType()));
        
        if (s.equals("COMENTARIO_NAO_FECHADO")) {
            pw.write("Linha " + line + ": comentario nao fechado\n");
        }       

        else if (s.equals("CADEIA_NAO_FECHADA")) {
            pw.write("Linha " + line + ": cadeia literal nao fechada\n");
        } 

        else if (s.equals("SIMBOLO_NAO_IDENTIFICADO")) {
            pw.write("Linha " + line + ": " + t.getText() + " - simbolo nao identificado\n");
        }
        
        else if(t.getType() == Token.EOF){
            pw.write("Linha " + line + ": erro sintatico proximo a EOF\n");
        }
        
        else {
            pw.write("Linha " + line + ": erro sintatico proximo a " + t.getText() + "\n");
        }
        
        pw.write("Fim da compilacao\n");
     
        throw new RuntimeException();
    }
}