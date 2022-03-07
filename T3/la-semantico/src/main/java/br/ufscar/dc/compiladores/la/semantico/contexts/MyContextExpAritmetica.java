package br.ufscar.dc.compiladores.la.semantico.contexts;

public class MyContextExpAritmetica extends MyContext {
    private String type;
    
    public MyContextExpAritmetica(String type) {
        this.type = type;
    }
    
    public String getType() {
        return this.type;
    }
}
