package br.ufscar.dc.compiladores.la.semantico.contexts;

import br.ufscar.dc.compiladores.la.semantico.tabelaDeSimbolos.Tipo;

public class MyContextFatorLogico extends MyContext {
    
    private Tipo tipo;
    
    public MyContextFatorLogico(Tipo tipo) {
        this.tipo = tipo;
    }
    
    public Tipo getTipo() {
        return this.tipo;
    }
}
