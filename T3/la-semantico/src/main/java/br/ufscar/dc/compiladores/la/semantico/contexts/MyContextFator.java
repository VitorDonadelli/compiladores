package br.ufscar.dc.compiladores.la.semantico.contexts;

public class MyContextFator extends MyContext {
    private String type;
    
    public MyContextFator(String type) {
        this.type = type;
    }
    
    public String getType() {
        return this.type;
    }
}
