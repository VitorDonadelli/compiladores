package br.ufscar.dc.compiladores.findinator.enums;

import java.util.Optional;
import org.antlr.v4.runtime.tree.TerminalNode;

// Mapeia texto para Enum
public enum ModificadorVerbo {
    MAIS_DE,
    MENOS_DE,
    EXATAMENTE;
    
    public static Optional<ModificadorVerbo> make(TerminalNode node) {
        if (node == null) {
            return Optional.empty();
        }
        
        String text = node.getText().toLowerCase();
        
        switch (text) {
            case "mais de":
                return Optional.of(MAIS_DE);
            case "menos de":
                return Optional.of(MENOS_DE);
            case "exatamente":
                return Optional.of(EXATAMENTE);
        }
        
        throw new RuntimeException("Invalid 'ModificadorVerbo' value");
    }
}
