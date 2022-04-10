package br.ufscar.dc.compiladores.findinator.enums;

import java.util.Optional;
import org.antlr.v4.runtime.tree.TerminalNode;

public enum ModificadorNomePath {
    QUE_COMECA_COM,
    QUE_TERMINA_COM;
    
    public static Optional<ModificadorNomePath> make(TerminalNode node) {
        if (node == null) {
            return Optional.empty();
        }
        
        String text = node.getText().toLowerCase();
        
        switch (text) {
            case "que começa com":
                return Optional.of(QUE_COMECA_COM);
            case "que termina com":
                return Optional.of(QUE_TERMINA_COM);
        }
        
        throw new RuntimeException("Invalid 'ModificadorNomePath' value");
    }
}
