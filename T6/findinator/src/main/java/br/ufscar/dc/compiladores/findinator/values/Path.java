package br.ufscar.dc.compiladores.findinator.values;

import org.antlr.v4.runtime.tree.TerminalNode;

// Wrapper para valor arbitrario de Path
public class Path {
    private final String value;
    private boolean missing;
    
    public Path(String value) {
        this.value = value;
        this.missing = this.value == null;
    }
    
    public static Path make(TerminalNode token) {
        if (token == null) {
            return new Path(null);
        }
        
        return new Path(token.getText());
    }
    
    public boolean isMissing() {
        return missing;
    }
    
    public String getValue() {
        return value;
    }
}