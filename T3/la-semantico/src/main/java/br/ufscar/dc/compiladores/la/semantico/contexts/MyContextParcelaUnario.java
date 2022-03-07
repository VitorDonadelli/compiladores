package br.ufscar.dc.compiladores.la.semantico.contexts;

public class MyContextParcelaUnario extends MyContext {
    public String type;
    
    public MyContextParcelaUnario(String type) {
        this.type = type;
    }
    
    public String getType() {
        return this.type;
    }
}
