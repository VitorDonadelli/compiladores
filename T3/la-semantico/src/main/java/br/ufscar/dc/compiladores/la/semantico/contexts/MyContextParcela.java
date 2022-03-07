package br.ufscar.dc.compiladores.la.semantico.contexts;

public class MyContextParcela extends MyContext {
    public String type;
    
    public MyContextParcela(String type) {
        this.type = type;
    }
    
    public String getType() {
        return this.type;
    }
}
