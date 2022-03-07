package br.ufscar.dc.compiladores.la.semantico.contexts;

public class MyContextTipoBasico extends MyContext {
    public String type;
    
    public MyContextTipoBasico(String type) {
        this.type = type;
    }
    
    public String getType() {
        return this.type;
    }
}
