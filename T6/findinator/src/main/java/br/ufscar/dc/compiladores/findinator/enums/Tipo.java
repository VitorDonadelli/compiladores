package br.ufscar.dc.compiladores.findinator.enums;

import org.antlr.v4.runtime.tree.TerminalNode;

public enum Tipo {
    MISSING,
    ARQUIVOS,
    DIRETORIOS;
    
    public static Tipo make(TerminalNode node) {
        if (node == null) {
            return MISSING;
        }
        
        String text = node.getText().toLowerCase();
        
        switch (text) {
            case "arquivos":
                return ARQUIVOS;
            case "diretorios":
                return DIRETORIOS;
        }
        
        throw new RuntimeException("Invalid 'Tipo' value");
    }
    
    public boolean isMissing() {
        return this == MISSING;
    }
}
