package br.ufscar.dc.compiladores.findinator.enums;

import org.antlr.v4.runtime.tree.TerminalNode;

public enum Comando {
    MISSING,
    ENCONTRE,
    DEBUG,
    DELETE,
    LISTE;
    
    public static Comando make(TerminalNode node) {
        if (node == null) {
            return MISSING;
        }
        
        String text = node.getText().toLowerCase();
        
        switch (text) {
            case "encontre":
                return ENCONTRE;
            case "debug":
                return DEBUG;
            case "delete":
                return DELETE;
            case "liste":
                return LISTE;
        }
        
        throw new RuntimeException("Invalid 'Comando' value");
    }
    
    public boolean isMissing() {
        return this == MISSING;
    }
}
