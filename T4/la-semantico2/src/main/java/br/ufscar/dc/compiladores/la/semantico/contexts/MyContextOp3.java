package br.ufscar.dc.compiladores.la.semantico.contexts;

public class MyContextOp3 extends MyContext {
    
    public enum Op3 {
        MOD
    }
    
    private Op3 op;
    
    public MyContextOp3(Op3 op) {
        this.op = op;
    }
    
    public Op3 getOp() {
        return this.op;
    }
}
