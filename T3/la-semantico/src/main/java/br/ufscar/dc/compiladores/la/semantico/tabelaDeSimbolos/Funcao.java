package br.ufscar.dc.compiladores.la.semantico.tabelaDeSimbolos;

// Representação da função
public class Funcao {
    private String type;
    
    public Funcao(String type) {
        this.type = type;
    }
    
    public String getType() {
        return this.type;
    }
}
