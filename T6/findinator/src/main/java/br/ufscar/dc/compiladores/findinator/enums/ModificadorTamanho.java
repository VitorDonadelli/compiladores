package br.ufscar.dc.compiladores.findinator.enums;

import java.util.Optional;
import org.antlr.v4.runtime.tree.TerminalNode;

public enum ModificadorTamanho {
    MAIOR_QUE,
    MENOR_QUE,
    EXATAMENTE;
    
    public static Optional<ModificadorTamanho> make(TerminalNode node) {
        if (node == null) {
            return Optional.empty();
        }
        
        String text = node.getText().toLowerCase();
        
        switch (text) {
            case "maior que":
                return Optional.of(MAIOR_QUE);
            case "menor que":
                return Optional.of(MENOR_QUE);
            case "exatamente":
                return Optional.of(EXATAMENTE);
        }
        
        throw new RuntimeException("Invalid 'ModificadorTamanho' value");
    }
}
