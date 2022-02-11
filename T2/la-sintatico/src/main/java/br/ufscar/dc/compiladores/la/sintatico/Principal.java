package br.ufscar.dc.compiladores.la.sintatico;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;

public class Principal{
    public static void main(String args[]) throws IOException {
        try(PrintWriter pw = new PrintWriter(new File(args[1]))) { 
            
            CharStream cs = CharStreams.fromFileName(args[0]);
            LaLexer lexer = new LaLexer(cs);
            
            CommonTokenStream tokens = new CommonTokenStream(lexer);
            LaParser parser = new LaParser(tokens);
            
            parser.removeErrorListeners();
            MyCustomErrorListener mcel = new MyCustomErrorListener(pw);
            parser.addErrorListener(mcel);
            
            try{
               parser.programa();                  
            } catch(Exception e) {}

        }
    }
}



