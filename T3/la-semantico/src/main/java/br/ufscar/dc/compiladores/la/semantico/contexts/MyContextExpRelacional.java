package br.ufscar.dc.compiladores.la.semantico.contexts;

public class MyContextExpRelacional extends MyContext {
    private String type;
    
    public MyContextExpRelacional(String type) {
        this.type = type;
    }
    
    public String getType() {
        return this.type;
    }
}
