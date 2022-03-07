package br.ufscar.dc.compiladores.la.semantico.contexts;

public class MyContextFatorLogico extends MyContext {
    private String type;
    
    public MyContextFatorLogico(String type) {
        this.type = type;
    }
    
    public String getType() {
        return this.type;
    }
}
