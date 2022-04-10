package br.ufscar.dc.compiladores.findinator.contexts;

import br.ufscar.dc.compiladores.findinator.enums.ModificadorVerbo;
import br.ufscar.dc.compiladores.findinator.enums.TimeUnit;
import br.ufscar.dc.compiladores.findinator.enums.VerboEnum;
import br.ufscar.dc.compiladores.findinator.values.NumeroValue;
import java.util.Optional;

public class Verbo extends MyTesteContext {
    private final VerboEnum verbo;
    private final Optional<ModificadorVerbo> opModificadorVerbo;
    private final NumeroValue numero;
    private final TimeUnit timeUnit;
    
    public Verbo(VerboEnum verbo, Optional<ModificadorVerbo> opModificadorVerbo, NumeroValue numero, TimeUnit timeUnit) {
        this.verbo = verbo;
        this.opModificadorVerbo = opModificadorVerbo;
        this.numero = numero;
        this.timeUnit = timeUnit;
    }
    
    @Override
    public String build() {
        int value = numero.getValue();
        
        String mod = "";
        
        if (!opModificadorVerbo.isEmpty()) {
            switch (opModificadorVerbo.get()) {
                case MAIS_DE:
                    mod = "+";
                    break;
                case MENOS_DE:
                    mod = "-";
                    break;
                case EXATAMENTE:
                    // Nada
                    break;
            }
        }
        
        switch (verbo) {
            case ACESSADO:
                switch (timeUnit) {
                    case MINUTO:
                    case MINUTOS:
                        return String.format("-amin %s%d", mod, value);
                    case HORA:
                    case HORAS:
                        value *= 60;
                        return String.format("-amin %s%d", mod, value);
                    case DIA:
                    case DIAS:
                        return String.format("-atime %s%d", mod, value);
                }
                break;
            case ALTERADO:
                switch (timeUnit) {
                    case MINUTO:
                    case MINUTOS:
                        return String.format("(-cmin %s%d -o -mmin %s%d)", mod, value, mod, value);
                    case HORA:
                    case HORAS:
                        value *= 60;
                        return String.format("(-cmin %s%d -o -mmin %s%d)", mod, value, mod, value);
                    case DIA:
                    case DIAS:
                        return String.format("(-ctime %s%d -o -mtime %s%d)", mod, value, mod, value);
                }
        }
        
        throw new RuntimeException("Parâmetros inválidos para 'Verbo'");
    }
}
