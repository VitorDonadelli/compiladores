package br.ufscar.dc.compiladores.findinator.values;

import org.antlr.v4.runtime.tree.TerminalNode;

// Wrapper para valor arbitrario de nome de Identificador
public class Identificador {
    private final String value;
    private final boolean missing;
    
    public Identificador(String value) {
        this.value = value;
        this.missing = this.value == null;
    }
    
    public static Identificador make(TerminalNode token) {
        if (token == null) {
            return new Identificador(null);
        }
        
        return new Identificador(token.getText());
    }
    
    public boolean isMissing() {
        return missing;
    }
    
    public String getValue() {
        return value;
    }
}
