package br.ufscar.dc.compiladores.la.semantico.contexts;

import br.ufscar.dc.compiladores.la.semantico.tabelaDeSimbolos.Tipo;

public class MyContextTipoEstendido extends MyContext {
    
    public Tipo tipo;
    public boolean ehPonteiro;
    
    public MyContextTipoEstendido(Tipo tipo) {
        this.tipo = tipo;
    }
    
    public Tipo getTipo() {
        return this.tipo;
    }
    
    public boolean getEhPonteiro() {
        return this.ehPonteiro;
    }
}
