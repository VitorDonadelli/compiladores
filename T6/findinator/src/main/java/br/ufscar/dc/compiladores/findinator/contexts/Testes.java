package br.ufscar.dc.compiladores.findinator.contexts;

import java.util.List;

public class Testes extends MyContext {
    private final List<Teste> testes;
    
    public Testes(List<Teste> testes) {
        this.testes = testes;
    }
    
    public String build() {
        String s = "";
        
        for (var t : testes) {
            s += t.build();
        }
        
        return s;
    }
}
