package br.ufscar.dc.compiladores.findinator.contexts;

import br.ufscar.dc.compiladores.findinator.enums.Conector;
import br.ufscar.dc.compiladores.findinator.values.NumeroValue;

public class Permissao extends MyTesteContext {
    private final Conector conector;
    private final NumeroValue numero;
    
    public Permissao(Conector conector, NumeroValue numero) {
        this.conector = conector;
        this.numero = numero;
    }
    
    // Monta flag da permissão
    @Override
    public String build() {
        return " -perm /" + numero.getValue();
    }
}
