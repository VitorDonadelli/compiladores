package br.ufscar.dc.compiladores.la.semantico.contexts;

public class MyContextOpLogico extends MyContext {
    public String type;
    
    public MyContextOpLogico(String type) {
        this.type = type;
    }
    
    public String getType() {
        return this.type;
    }
}
