package br.ufscar.dc.compiladores.la.semantico.contexts;

public class MyContextTermoLogico extends MyContext {
    private String type;
    
    public MyContextTermoLogico(String type) {
        this.type = type;
    }
    
    public String getType() {
        return this.type;
    }
}
