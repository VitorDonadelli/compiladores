package br.ufscar.dc.compiladores.la.semantico.contexts;

public class MyContextTipoBasicoIdent extends MyContext {
    public String type;
    
    public MyContextTipoBasicoIdent(String type) {
        this.type = type;
    }
    
    public String getType() {
        return this.type;
    }
    
}
