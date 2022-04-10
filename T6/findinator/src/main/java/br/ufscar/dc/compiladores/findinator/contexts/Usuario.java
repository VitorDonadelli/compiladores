package br.ufscar.dc.compiladores.findinator.contexts;

import br.ufscar.dc.compiladores.findinator.values.Identificador;

public class Usuario extends MyTesteContext {
    private final Identificador identificador;
    
    public Usuario(Identificador identificador) {
        this.identificador = identificador;
    }
    
    @Override
    public String build() {
        if (identificador == null) {
            System.out.println("null");
        }
        
        return " -user " + identificador.getValue();
    }
}
