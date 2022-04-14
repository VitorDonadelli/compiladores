package br.ufscar.dc.compiladores.findinator;

import java.io.IOException;
import java.io.PrintWriter;
import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.Token;

public class Findinator {

    public static PrintWriter pw;
    
    public static void main(String[] args) throws IOException {
        
        // Cria printWriter pro arquivo de saida
        pw = new PrintWriter(args[1]);
        CharStream cs = CharStreams.fromFileName(args[0]);
        
        // Instancia e roda o compilador
        var f = new Findinator(); 
        String retorno = f.run(cs, pw); 
        if (retorno != null ) {
            pw.write(retorno + '\n');
        }
        
        pw.close();
    }
    
    public String run(CharStream input, PrintWriter pw) {
        
        // Construção do lexer, parser e arvore semantica
        FindinatorLexer   lexer  = new FindinatorLexer(input);
        CommonTokenStream tokens = new CommonTokenStream(lexer);
        FindinatorParser  parser = new FindinatorParser(tokens);
        
        var arvore = new FindinatorSemantico();
        var find   = arvore.visitFind(parser.find());
        var erros  = arvore.getErros();
        
        // Imprime os erros da stack
        for (var e : erros) {
            pw.write(e.getMsg() + '\n');
        }
        
        // Caso de erro
        if (find == null) {
            pw.write("Não foi possível processar o texto!\n");
            return null;
        }
        
        // Roda o gerador de código
        return find.build();
    }
    
    // Debug (não ta chamando :D)
    public void printTokens(String input) {
        FindinatorLexer lexer = new FindinatorLexer(CharStreams.fromString(input));
                
        Token t;
        
        while ((t = lexer.nextToken()).getType() != Token.EOF) {
            System.out.println("'" + t.getText() + "'" + " " + FindinatorLexer.VOCABULARY.getSymbolicName(t.getType()));
        }
    }
}
