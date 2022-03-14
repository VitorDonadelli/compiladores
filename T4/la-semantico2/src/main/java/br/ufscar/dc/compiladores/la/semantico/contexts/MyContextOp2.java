package br.ufscar.dc.compiladores.la.semantico.contexts;

public class MyContextOp2 extends MyContext {
    
    public enum Op2 {
        MUL, DIV
    }
    
    private Op2 op;
    
    public MyContextOp2(Op2 op) {
        this.op = op;
    }
    
    public Op2 getOp() {
        return this.op;
    }
}