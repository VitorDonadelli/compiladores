package br.ufscar.dc.compiladores.la.semantico.tabelaDeSimbolos;

// Representação da variavel
public class Variavel {
    private Tipo tipo;
    
    public Variavel(Tipo tipo) {
        this.tipo = tipo;
    }
    
    public Tipo getTipo() {
        return this.tipo;
    }
}
