package br.ufscar.dc.compiladores.la.semantico.contexts;

public class MyContextOp2 extends MyContext {
    private String type;
    
    public MyContextOp2(String type) {
        this.type = type;
    }
    
    public String getType() {
        return this.type;
    }
}
