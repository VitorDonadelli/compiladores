package br.ufscar.dc.compiladores.la.semantico.contexts;

public class MyContextOp1 extends MyContext {
    
    public enum Op1 {
        SUM, SUB
    }
    
    private Op1 op;
    
    public MyContextOp1(Op1 op) {
        this.op = op;
    }
    
    public Op1 getOp() {
        return this.op;
    }
}
