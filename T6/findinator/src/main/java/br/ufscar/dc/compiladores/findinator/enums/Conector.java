package br.ufscar.dc.compiladores.findinator.enums;

import org.antlr.v4.runtime.tree.TerminalNode;

// Mapeia texto para Enum
public enum Conector {
    MISSING,
    QUE,
    O_QUAL,
    COM,
    TAL_QUE,
    QUE_TEM,
    DE,
    OU;

    public static Conector make(TerminalNode node) {
        if (node == null) {
            return MISSING;
        }
        
        String text = node.getText().toLowerCase();
        
        switch (text) {
            case "que":
                return QUE;
            case "o qual":
                return O_QUAL;
            case "com":
                return COM;
            case "tal que":
                return TAL_QUE;
            case "que tem":
                return QUE_TEM;
            case "de":
                return DE;
            case "ou":
                return OU;
        }
        
        throw new RuntimeException("Invalid 'Conector' value");
    }
    
    public boolean isMissing() {
        return this == MISSING;
    }
}
