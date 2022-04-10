package br.ufscar.dc.compiladores.findinator.values;

import org.antlr.v4.runtime.tree.TerminalNode;

public class NumeroValue {
    private Integer value;
    private boolean missing;
    
    public NumeroValue(Integer value) {
        this.value = value;
        this.missing = this.value == null;
    }
    
    public static NumeroValue make(TerminalNode token) {
        if (token == null) {
            return new NumeroValue(null);
        }
                
        return new NumeroValue(Integer.parseInt(token.getText()));
    }
    
    public boolean isMissing() {
        return missing;
    }
    
    public Integer getValue() {
        return value;
    }
}
