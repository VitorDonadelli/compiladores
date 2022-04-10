package br.ufscar.dc.compiladores.findinator.contexts;

import br.ufscar.dc.compiladores.findinator.enums.Tipo;
import java.util.Optional;

public class ArquivoDiretorio extends MyContext {
    private final Tipo tipo;
    private final Optional<Tipo> opTipoExtra;
    
    public ArquivoDiretorio(Tipo tipo, Optional<Tipo> opTipoExtra) {
        this.tipo = tipo;
        this.opTipoExtra = opTipoExtra;
    }
    
    public String build() {
        String s = " -type ";
        
        switch (tipo) {
            case ARQUIVOS:
                s += "f";
                break;
            case DIRETORIOS:
                s += "s";
                break;
        }
        
        if (!opTipoExtra.isEmpty()) {
            switch (opTipoExtra.get()) {
                case ARQUIVOS:
                    s += "f";
                    break;
                case DIRETORIOS:
                    s += "d";
                    break;
            }
        }
        
        return s;
    }
}
