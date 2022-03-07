package br.ufscar.dc.compiladores.la.semantico.contexts;

public class MyContextOpRelacional extends MyContext {
    public String type;
    
    public MyContextOpRelacional(String type) {
        this.type = type;
    }
    
    public String getType() {
        return this.type;
    }
}
