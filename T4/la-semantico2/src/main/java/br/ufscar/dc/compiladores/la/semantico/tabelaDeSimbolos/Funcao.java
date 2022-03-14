package br.ufscar.dc.compiladores.la.semantico.tabelaDeSimbolos;

// Representação da função
public class Funcao {
    private Tipo tipo;
    
    public Funcao(Tipo tipo) {
        this.tipo = tipo;
    }
    
    public Tipo getTipo() {
        return this.tipo;
    }
}
