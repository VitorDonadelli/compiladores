package br.ufscar.dc.compiladores.la.semantico.tabelaDeSimbolos;

// Representação da variavel
public class Variavel {
    private String type;
    
    public Variavel(String type) {
        this.type = type;
    }
    
    public String getType() {
        return this.type;
    }
}
