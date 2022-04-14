package br.ufscar.dc.compiladores.findinator.values;

import org.antlr.v4.runtime.tree.TerminalNode;

// Wrapper para valor arbitrario de nome de Arquivo
public class File {
    private final String value;
    private final boolean missing;
    
    public File(String value) {
        this.value = value;
        this.missing = this.value == null;
    }
    
    public static File make(TerminalNode token) {
        if (token == null) {
            return new File(null);
        }
        
        return new File(token.getText());
    }
    
    public boolean isMissing() {
        return missing;
    }
    
    public String getValue() {
        return value;
    }
}