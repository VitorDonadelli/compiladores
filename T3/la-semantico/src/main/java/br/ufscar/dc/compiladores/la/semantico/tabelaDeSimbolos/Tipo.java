package br.ufscar.dc.compiladores.la.semantico.tabelaDeSimbolos;

// Representação do tipo
public class Tipo {
    private String name;
    
    public Tipo(String name) {
        this.name = name;
    }
    
    public String getName() {
        return this.name;
    }
}
