package br.ufscar.dc.compiladores.la.semantico.contexts;

import java.util.List;

public class MyContextIdentificador extends MyContext {
    
    private String ident;
    private List<String> otherIdents;
    private MyContextDimensao ctxDimensao;
            
    public MyContextIdentificador(String ident, List<String> otherIdents, MyContextDimensao ctxDimensao) {
        this.ident = ident;
        this.otherIdents = otherIdents;
        this.ctxDimensao = ctxDimensao;
    }
    
    public String getName() {
        return this.ident;
    }
    
    public String getNameFullName() {
        String name = "";
        
        name += this.ident;
        
        for (var i : this.otherIdents) {
            name += "." + i;
        }
        
        return name;
    }
}
