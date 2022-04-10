package br.ufscar.dc.compiladores.findinator.enums;

import org.antlr.v4.runtime.tree.TerminalNode;

public enum Separador {
    MISSING,
    VIRGULA,
    E;
    
    public static Separador make(TerminalNode node) {
        if (node == null) {
            return MISSING;
        }
        
        String text = node.getText().toLowerCase();
        
        switch (text) {
            case ", ":
                return VIRGULA;
            case " e ":
                return E;
        }
        
        throw new RuntimeException("Invalid 'ModificadorTamanho' value");
    }
    
    public boolean isMissing() {
        return this == MISSING;
    }
}
