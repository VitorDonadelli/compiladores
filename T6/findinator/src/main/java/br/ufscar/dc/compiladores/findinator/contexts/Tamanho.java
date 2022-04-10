package br.ufscar.dc.compiladores.findinator.contexts;

import br.ufscar.dc.compiladores.findinator.enums.ModificadorTamanho;
import br.ufscar.dc.compiladores.findinator.enums.SizeUnit;
import br.ufscar.dc.compiladores.findinator.values.NumeroValue;
import java.util.Optional;

public class Tamanho extends MyTesteContext {
    private final Optional<ModificadorTamanho> opModificadorTamanho;
    private final NumeroValue numero;
    private final SizeUnit sizeUnit;
    
    public Tamanho(Optional<ModificadorTamanho> opModificadorTamanho, NumeroValue numero, SizeUnit sizeUnit) {
        this.opModificadorTamanho = opModificadorTamanho;
        this.numero = numero;
        this.sizeUnit = sizeUnit;
    }
    
    @Override
    public String build() {
        String s = " -size ";
        
        if (!opModificadorTamanho.isEmpty()) {
            switch (opModificadorTamanho.get()) {
                case MAIOR_QUE:
                    s += "+";
                    break;
                case MENOR_QUE:
                    s += "-";
                    break;
                case EXATAMENTE:
                    // Nada
                    break;
            }
        }
        
        s += numero.getValue();
        
        switch (sizeUnit) {
            case B:
                s += "c";
                break;
            case KB:
                s += "k";
                break;
            case MB:
                s += "M";
                break;
            case GB:
                s += "G";
                break;
        }
        
        return s;
    }
}
