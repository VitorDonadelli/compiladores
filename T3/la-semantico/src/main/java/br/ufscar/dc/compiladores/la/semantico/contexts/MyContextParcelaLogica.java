package br.ufscar.dc.compiladores.la.semantico.contexts;

public class MyContextParcelaLogica extends MyContext {
    public String type;
    
    public MyContextParcelaLogica(String type) {
        this.type = type;
    }
    
    public String getType() {
        return this.type;
    }
}
