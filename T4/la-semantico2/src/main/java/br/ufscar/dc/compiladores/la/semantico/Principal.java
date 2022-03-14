package br.ufscar.dc.compiladores.la.semantico;

import br.ufscar.dc.compiladores.la.semantico.LaParser.ProgramaContext;
import java.io.IOException;
import java.io.PrintWriter;
import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;

public class Principal {
    
    public static PrintWriter pw;

    public static void main(String args[]) throws IOException {
        
        pw = new PrintWriter(args[1]);
        
        // Construção do lexer 
        CharStream cs = CharStreams.fromFileName(args[0]);
        LaLexer lexer = new LaLexer(cs);
        CommonTokenStream tokens = new CommonTokenStream(lexer);
        // Construção do parser
        LaParser parser = new LaParser(tokens);
        ProgramaContext arvore = parser.programa();
        // Construção da arvore semantica
        LaSemantico las = new LaSemantico();
        las.visitPrograma(arvore);
        // Tratamento e print de erros
        LaSemanticoUtils.getErrors().forEach((erro) -> pw.write(erro + '\n'));
        
        pw.write("Fim da compilacao\n");
        pw.close();
    }
}