package br.ufscar.dc.compiladores.la.semantico.contexts;

public class MyContextVariavel extends MyContext {
    public String type;
    
    public MyContextVariavel(String type) {
        this.type = type;
    }
    
    public String getType() {
        return this.type;
    }
}
