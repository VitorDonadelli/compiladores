package br.ufscar.dc.compiladores.la.semantico.contexts;

public class MyContextParcelaNaoUnario extends MyContext {
    public String type;
    
    public MyContextParcelaNaoUnario(String type) {
        this.type = type;
    }
    
    public String getType() {
        return this.type;
    }
}
