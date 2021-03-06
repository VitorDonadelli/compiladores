package br.ufscar.dc.compiladores.findinator.contexts;

import br.ufscar.dc.compiladores.findinator.enums.CaracteristicaEnum;

public class Caracteristica extends MyTesteContext {
    private final CaracteristicaEnum caracteristica;
    
    public Caracteristica(CaracteristicaEnum caracteristica) {
        this.caracteristica = caracteristica;
    }
    
    // Constroi a parte de argumento referente a propriedade do arquivo/diretorio
    @Override
    public String build() {
        switch (caracteristica) {
            case VAZIO:
                return " -empty";
            case EXECUTAVEL:
                return " -executable";
        }
        
        throw new RuntimeException("Parâmetros inválidos para 'Caracteristica'");
    }
}
