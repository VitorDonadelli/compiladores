package br.ufscar.dc.compiladores.findinator.enums;

import org.antlr.v4.runtime.tree.TerminalNode;

public enum TimeUnit {
    MISSING,
    MINUTO,
    MINUTOS,
    HORA,
    HORAS,
    DIA,
    DIAS;
    
    public static TimeUnit make(TerminalNode node) {
        if (node == null) {
            return MISSING;
        }
        
        String text = node.getText().toLowerCase();
        
        switch (text) {
            case "minuto":
                return MINUTO;
            case "minutos":
                return MINUTOS;
            case "hora":
                return HORA;
            case "horas":
                return HORAS;
            case "dia":
                return DIA;
            case "dias":
                return DIAS;
        }
        
        throw new RuntimeException("Invalid 'TimeUnit' value");
    }
    
    public boolean isMissing() {
        return this == MISSING;
    }
}
