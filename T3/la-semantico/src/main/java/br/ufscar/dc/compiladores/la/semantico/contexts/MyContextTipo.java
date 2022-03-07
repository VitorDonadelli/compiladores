package br.ufscar.dc.compiladores.la.semantico.contexts;

public class MyContextTipo extends MyContext {
    private String type;
    
    public MyContextTipo(String type) {
        this.type = type;
    }
    
    public String getType() {
        return this.type;
    }
}
