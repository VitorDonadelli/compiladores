package br.ufscar.dc.compiladores.findinator.enums;

import org.antlr.v4.runtime.tree.TerminalNode;

// Mapeia texto para Enum
public enum VerboEnum {
    MISSING,
    ACESSADO,
    ALTERADO;
    
    public static VerboEnum make(TerminalNode node) {
        if (node == null) {
            return MISSING;
        }
        
        String text = node.getText().toLowerCase();
        
        switch (text) {
            case "acessado":
                return ACESSADO;
            case "alterado":
                return ALTERADO;
        }
        
        throw new RuntimeException("Invalid 'VerboEnum' value");
    }
    
    public boolean isMissing() {
        return this == MISSING;
    }
}
