package br.ufscar.dc.compiladores.la.semantico.contexts;

import br.ufscar.dc.compiladores.la.semantico.tabelaDeSimbolos.Tipo;
import java.util.List;

public class MyContextVariavel extends MyContext {
    
    public List<String> identificadores;
    public Tipo tipo;
    
    public MyContextVariavel(List<String> identificadores, Tipo tipo) {
        this.identificadores = identificadores;
        this.tipo = tipo;
    }
    
    public Tipo getTipo() {
        return this.tipo;
    }
}
