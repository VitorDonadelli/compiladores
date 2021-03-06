package br.ufscar.dc.compiladores.findinator.contexts;

import br.ufscar.dc.compiladores.findinator.enums.LerOuEscrever;

public class LerEscrever extends MyTesteContext {
    private final LerOuEscrever lerOuEscrever;
    
    public LerEscrever(LerOuEscrever lerOuEscrever) {
        this.lerOuEscrever = lerOuEscrever;
    }
    
    // Constroi a parte de argumento referente a propriedade do arquivo/diretorio
    @Override
    public String build() {
        switch (lerOuEscrever) {
            case LIDO:
                return " -readable";
            case ESCRITO:
                return " -writable";
        }
        
        throw new RuntimeException("Parâmetros inválidos para 'LerEscrever'");
    }
}
