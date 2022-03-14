package br.ufscar.dc.compiladores.la.semantico.tabelaDeSimbolos;

// Representação do tipo
public class Tipo {
    
    public enum TipoBase {
        LITERAL,
        INTEIRO,
        REAL,
        LOGICO,
        REGISTRO,
        UNKNOWN
    }
    
    private TipoBase tipoBase;
    private boolean ehPonteiro;
    private TabelaDeSimbolos escopo;
    private String name;
    
    public Tipo() {
        this.tipoBase = TipoBase.UNKNOWN;
        this.name = "";
    }
    
    public Tipo(TipoBase tipoBase, String name) {
        this.tipoBase = tipoBase;
        this.name = name;
    }
    
    public Tipo(Tipo tipo) {
        this.tipoBase = tipo.tipoBase;
        this.ehPonteiro = tipo.ehPonteiro;
        this.escopo = tipo.escopo;
        this.name = tipo.name;
    }
    
    public boolean getEhPonteiro() {
        return this.ehPonteiro;
    }
    
    public void setEhPonteiro() {
        this.ehPonteiro = true;
    }
    
    public TabelaDeSimbolos getEscopo() {
        return this.escopo;
    }
    
    public void setEscopo(TabelaDeSimbolos escopo) {
        this.escopo = escopo;
    }
    
    public String getName() {
        if (this.name == null) {
            throw new RuntimeException("Tipo null name");
        }
        
        return this.name;
    }
    
    public void setName(String name) {
        this.name = name;
    }
    
    public TipoBase getTipoBase() {
        return this.tipoBase;
    }
}
