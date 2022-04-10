package br.ufscar.dc.compiladores.findinator.enums;

import org.antlr.v4.runtime.tree.TerminalNode;

public enum SizeUnit {
    MISSING,
    B,
    KB,
    MB,
    GB;
    
    public static SizeUnit make(TerminalNode node) {
        if (node == null) {
            return MISSING;
        }
        
        String text = node.getText().toLowerCase();
        
        switch (text) {
            case "b":
                return B;
            case "kb":
                return KB;
            case "mb":
                return MB;
            case "gb":
                return GB;
        }
        
        throw new RuntimeException("Invalid 'SizeUnit' value");
    }
    
    public boolean isMissing() {
        return this == MISSING;
    }
}