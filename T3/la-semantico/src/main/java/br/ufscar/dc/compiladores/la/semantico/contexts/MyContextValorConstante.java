package br.ufscar.dc.compiladores.la.semantico.contexts;

public class MyContextValorConstante extends MyContext {
    public String type;
    
    public MyContextValorConstante(String type) {
        this.type = type;
    }
    
    public String getType() {
        return this.type;
    }
}
