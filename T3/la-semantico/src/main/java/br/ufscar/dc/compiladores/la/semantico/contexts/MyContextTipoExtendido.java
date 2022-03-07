package br.ufscar.dc.compiladores.la.semantico.contexts;

public class MyContextTipoExtendido extends MyContext {
    public String type;
    
    public MyContextTipoExtendido(String type) {
        this.type = type;
    }
    
    public String getType() {
        return this.type;
    }
}
