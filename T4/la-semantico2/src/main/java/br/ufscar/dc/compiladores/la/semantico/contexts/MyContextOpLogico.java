package br.ufscar.dc.compiladores.la.semantico.contexts;

public class MyContextOpLogico extends MyContext {
    
    public enum OpLogico {
        E, OU
    }
    
    private OpLogico op;
    
    public MyContextOpLogico(OpLogico op) {
        this.op = op;
    }
    
    public OpLogico getOp() {
        return this.op;
    }
}
