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
        // String text = String.join(" ", args);
        // String text = "Encontre em ./pasta e /pasta os arquivos com nome teste.txt ou bola.txt ou cavalo.txt, do usuário vitor, que pode ser lido e que tem tamanho menor que 1 Mb";
        
        pw = new PrintWriter(args[1]);
        CharStream cs = CharStreams.fromFileName(args[0]);
        
        var f = new Findinator();
        
        // f.printTokens(text);
        //System.out.println(f.run(cs, pw));
        String retorno = f.run(cs, pw); 
        if (retorno != null ) {
            pw.write(retorno + '\n');
        }
        
        pw.close();
    }
    
    public String run(CharStream input, PrintWriter pw) {
        
        FindinatorLexer   lexer  = new FindinatorLexer(input);
        CommonTokenStream tokens = new CommonTokenStream(lexer);
        FindinatorParser  parser = new FindinatorParser(tokens);
        
        var arvore = new FindinatorSemantico();
        var find   = arvore.visitFind(parser.find());
        var erros  = arvore.getErros();
        
        for (var e : erros) {
            //System.out.println(e.getMsg());
            pw.write(e.getMsg() + '\n');
        }
        
        if (find == null) {
            //System.out.println("Não foi possível processar o texto!");
            pw.write("Não foi possível processar o texto!\n");
            return null;
        }
        
        return find.build();
    }
    
    public void printTokens(String input) {
        FindinatorLexer lexer = new FindinatorLexer(CharStreams.fromString(input));
                
        Token t;
        
        while ((t = lexer.nextToken()).getType() != Token.EOF) {
            System.out.println("'" + t.getText() + "'" + " " + FindinatorLexer.VOCABULARY.getSymbolicName(t.getType()));
        }
    }
}
