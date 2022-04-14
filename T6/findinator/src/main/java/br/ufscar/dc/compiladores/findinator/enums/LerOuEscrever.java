package br.ufscar.dc.compiladores.findinator.enums;

import org.antlr.v4.runtime.tree.TerminalNode;

// Mapeia texto para Enum
public enum LerOuEscrever {
    MISSING,
    LIDO,
    ESCRITO;
    
    public static LerOuEscrever make(TerminalNode node) {
        if (node == null) {
            return MISSING;
        }
        
        String text = node.getText().toLowerCase();
        
        switch (text) {
            case "lido":
                return LIDO;
            case "escrito":
                return ESCRITO;
        }
        
        throw new RuntimeException("Invalid 'LerOuEscrever' value");
    }
    
    public boolean isMissing() {
        return this == MISSING;
    }
}
