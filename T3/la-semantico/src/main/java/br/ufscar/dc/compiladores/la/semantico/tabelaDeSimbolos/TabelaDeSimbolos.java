package br.ufscar.dc.compiladores.la.semantico.tabelaDeSimbolos;

import java.util.HashMap;

public class TabelaDeSimbolos {
    
    // Criando um hashmap para cada tipo de entidade
    private HashMap<String, Variavel>     variaveis     = new HashMap();
    private HashMap<String, Procedimento> procedimentos = new HashMap();
    private HashMap<String, Funcao>       funcoes       = new HashMap();
    private HashMap<String, Tipo>         tipos         = new HashMap();

    // Verifica se o identificar existe no escopo
    public boolean acha(String s) {
        if (variaveis.containsKey(s)) {
            return true;
        }
        
        if (procedimentos.containsKey(s)) {
            return true;
        }
        
        if (funcoes.containsKey(s)) {
            return true;
        }
        
        if (tipos.containsKey(s)) {
            return true;
        }
        
        return false;
    }
    
    // Verifica se a variavel existe no escopo
    public boolean achaVariavel(String s) {
        if (variaveis.containsKey(s)) {
            return true;
        }
        
        return false;
    }
    
    // Verifica se o procedimento existe no escopo
    public boolean achaProcedimento(String s) {
        if (tipos.containsKey(s)) {
            return true;
        }
        
        return false;
    }
    
    // Verifica se a função existe no escopo
    public boolean achaFuncao(String s) {
        if (funcoes.containsKey(s)) {
            return true;
        }
        
        return false;
    }
    
    // Verifica se o tipo existe no escopo
    public boolean achaTipo(String s) {
        if (tipos.containsKey(s)) {
            return true;
        }
        
        return false;
    }
    
    // Insere a variavel no escopo
    public void insereVariavel(String s, Variavel v) {
        variaveis.put(s, v);
    }
    
    // Insere o procedimento no escopo
    public void insereProcedimento(String s, Procedimento p) {
        procedimentos.put(s, p);
    }
    
    // Insere a função no escopo
    public void insereFuncao(String s, Funcao f) {
        funcoes.put(s, f);
    }
    
    // Insere o tipo no escopo
    public void insereTipo(String s, Tipo t) {
        tipos.put(s, t);
    }
    
    // Recupera a variavel no escopo
    public Variavel pegaVariavel(String s) {
        if (variaveis.containsKey(s)) {
            return variaveis.get(s);
        }
        
        return null;
    }
    
    // Recupera a função no escopo
    public Funcao pegaFuncao(String s) {
        if (funcoes.containsKey(s)) {
            return funcoes.get(s);
        }
        
        return null;
    }
    
    // Recupera o procedimento no escopo
    public Procedimento pegaProcedimento(String s) {
        if (procedimentos.containsKey(s)) {
            return procedimentos.get(s);
        }
        
        return null;
    }
}
