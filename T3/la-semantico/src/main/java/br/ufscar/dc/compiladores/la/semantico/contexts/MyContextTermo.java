package br.ufscar.dc.compiladores.la.semantico.contexts;

public class MyContextTermo extends MyContext {
    public String type;
    
    public MyContextTermo(String type) {
        this.type = type;
    }
    
    public String getType() {
        return this.type;
    }
}
