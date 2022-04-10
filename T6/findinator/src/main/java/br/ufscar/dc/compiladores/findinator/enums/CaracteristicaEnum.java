package br.ufscar.dc.compiladores.findinator.enums;

import org.antlr.v4.runtime.tree.TerminalNode;

public enum CaracteristicaEnum {
    MISSING,
    VAZIO,
    EXECUTAVEL;
    
    public static CaracteristicaEnum make(TerminalNode node) {
        if (node == null) {
            return MISSING;
        }
        
        String text = node.getText().toLowerCase();
        
        switch (text) {
            case "vazio":
                return VAZIO;
            case "executavel":
                return EXECUTAVEL;
        }
        
        throw new RuntimeException("Invalid 'CaracteristicaEnum' value");
    }
    
    public boolean isMissing() {
        return this == MISSING;
    }
}
