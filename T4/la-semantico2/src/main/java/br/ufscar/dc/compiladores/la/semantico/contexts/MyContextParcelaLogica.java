package br.ufscar.dc.compiladores.la.semantico.contexts;

import br.ufscar.dc.compiladores.la.semantico.tabelaDeSimbolos.Tipo;

public class MyContextParcelaLogica extends MyContext {
    
    public Tipo tipo;
    
    public MyContextParcelaLogica(Tipo tipo) {
        this.tipo = tipo;
    }
    
    public Tipo getTipo() {
        return this.tipo;
    }
}
