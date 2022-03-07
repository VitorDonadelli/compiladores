package br.ufscar.dc.compiladores.la.semantico.contexts;

public class MyContextExpressao extends MyContext {
    private String type;
    
    public MyContextExpressao(String type) {
        this.type = type;
    }
    
    public String getType() {
        return this.type;
    }
}
