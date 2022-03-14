package br.ufscar.dc.compiladores.la.semantico.contexts;

public class MyContextOpRelacional extends MyContext {
    
    public enum OpRelacional {
        EQ, NE, BE, LE, BT, LT
    }
    
    private OpRelacional op;
    
    public MyContextOpRelacional(OpRelacional op) {
        this.op = op;
    }
    
    public OpRelacional getOp() {
        return this.op;
    }
}
