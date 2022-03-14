package br.ufscar.dc.compiladores.la.semantico.contexts;

public class MyContextOpUnario extends MyContext {
    
    public enum OpUnario {
        MINUS
    }
    
    private OpUnario op;
    
    public MyContextOpUnario(OpUnario op) {
        this.op = op;
    }
    
    public OpUnario getOp() {
        return this.op;
    }
}
