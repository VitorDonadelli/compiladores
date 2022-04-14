package br.ufscar.dc.compiladores.findinator.contexts;

public class Teste extends MyContext {
    private final MyTesteContext teste;
    
    public Teste(MyTesteContext teste) {
        this.teste = teste;
    }
    
    // Constroi o respectivo teste
    public String build() {
        return teste.build();
    }
}
