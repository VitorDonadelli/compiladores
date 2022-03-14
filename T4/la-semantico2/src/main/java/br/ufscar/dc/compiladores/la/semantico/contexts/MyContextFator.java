package br.ufscar.dc.compiladores.la.semantico.contexts;

import br.ufscar.dc.compiladores.la.semantico.tabelaDeSimbolos.Tipo;

public class MyContextFator extends MyContext {
    
    private Tipo tipo;
    
    public MyContextFator(Tipo tipo) {
        this.tipo = tipo;
    }
    
    public Tipo getTipo() {
        return this.tipo;
    }
}
