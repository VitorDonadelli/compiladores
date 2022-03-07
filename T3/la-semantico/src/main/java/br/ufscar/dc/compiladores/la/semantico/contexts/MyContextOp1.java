package br.ufscar.dc.compiladores.la.semantico.contexts;

public class MyContextOp1 extends MyContext {
    private String type;
    
    public MyContextOp1(String type) {
        this.type = type;
    }
    
    public String getType() {
        return this.type;
    }
}
