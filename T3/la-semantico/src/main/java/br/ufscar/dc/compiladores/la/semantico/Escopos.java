package br.ufscar.dc.compiladores.la.semantico;

import br.ufscar.dc.compiladores.la.semantico.tabelaDeSimbolos.*;
import java.util.Iterator;
import java.util.LinkedList;

public class Escopos {

    // Cria uma pilha para tabela de simbolos
    private LinkedList<TabelaDeSimbolos> pilhaEscopos;

    public Escopos() {
        pilhaEscopos = new LinkedList<>();
    }

    // Cria novo escopo
    public void novo() {
        pilhaEscopos.push(new TabelaDeSimbolos());
    }

    // Abandona o ultimo escopo
    public void abandonar() {
        pilhaEscopos.pop();
    }
    
    // Retorna o ultimo escopo
    public TabelaDeSimbolos atual() {
        return pilhaEscopos.peek();
    }
    
    // Retorna o primeiro escopo
    public TabelaDeSimbolos global() {
        if (pilhaEscopos.isEmpty()) {
            return null;
        }
        
        return pilhaEscopos.get(0);
    }
    
    // Procura um identificador em todos os escopos
    public boolean acha(String s) {
        Iterator<TabelaDeSimbolos> itr = pilhaEscopos.descendingIterator();
        
        while(itr.hasNext()){
            if (itr.next().acha(s)) {
                return true;
            }
        }
        
        return false;
    }
    
    // Procura um identificador no escopo local
    public boolean achaLocal(String s) {
        return this.atual().acha(s);
    }
    
    // Procura um identificador no escopo global
    public boolean achaGlobal(String s) {
        return this.global().acha(s);
    }
    
    // Procura uma variavel em todos os escopos
    public boolean achaVariavel(String s) {
        Iterator<TabelaDeSimbolos> itr = pilhaEscopos.descendingIterator();
        
        while(itr.hasNext()){
            if (itr.next().achaVariavel(s)) {
                return true;
            }
        }
        
        return false;
    }
    
    // Procura um procedimento em todos os escopos
    public boolean achaProcedimento(String s) {
        Iterator<TabelaDeSimbolos> itr = pilhaEscopos.descendingIterator();
        
        while(itr.hasNext()){
            if (itr.next().achaProcedimento(s)) {
                return true;
            }
        }
        
        return false;
    }
    
    // Procura uma função em todos os escopos
    public boolean achaFuncao(String s) {
        Iterator<TabelaDeSimbolos> itr = pilhaEscopos.descendingIterator();
        
        while(itr.hasNext()){
            if (itr.next().achaFuncao(s)) {
                return true;
            }
        }
        
        return false;
    }
    
    // Procura um tipo em todos os escopos
    public boolean achaTipo(String s) {
        Iterator<TabelaDeSimbolos> itr = pilhaEscopos.descendingIterator();
        
        while(itr.hasNext()){
            if (itr.next().achaTipo(s)) {
                return true;
            }
        }
        
        return false;
    }
    
    // Procura uma variavel no escopo local
    public boolean achaVariavelLocal(String s) {
        return this.atual().achaVariavel(s);
    }
    
    // Procura um procedimento no escopo local
    public boolean achaProcedimentoLocal(String s) {
        return this.atual().achaProcedimento(s);
    }
    
    // Procura uma função no escopo local
    public boolean achaFuncaoLocal(String s) {
        return this.atual().achaFuncao(s);
    }
    
    // Procura um tipo no escopo local
    public boolean achaTipoLocal(String s) {
        return this.atual().achaTipo(s);
    }
    
    // Procura uma variavel no escopo global
    public boolean achaVariavelGlobal(String s) {
        var global = this.global();
        
        if (global == null) {
            return false;
        }
        
        return global.achaVariavel(s);
    }
    
    // Procura um procedimento no escopo global
    public boolean achaProcedimentoGlobal(String s) {
        var global = this.global();
        
        if (global == null) {
            return false;
        }
        
        return global.achaProcedimento(s);
    }
    
    // Procura uma função no escopo global
    public boolean achaFuncaoGlobal(String s) {
        var global = this.global();
        
        if (global == null) {
            return false;
        }
        
        return global.achaFuncao(s);
    }
    
    // Procura um tipo no escopo global
    public boolean achaTipoGlobal(String s) {
        var global = this.global();
        
        if (global == null) {
            return false;
        }
        
        return global.achaTipo(s);
    }
    
    // Insere uma variavel no escopo local
    public void insereVariavelLocal(String s, Variavel v) {
        this.atual().insereVariavel(s, v);
    }
    
    // Insere um procedimento no escopo local
    public void insereProcedimentoLocal(String s, Procedimento p) {
        this.atual().insereProcedimento(s, p);
    }
    
    // Insere uma função no escopo local
    public void insereFuncaoLocal(String s, Funcao f) {
        this.atual().insereFuncao(s, f);
    }
    
    // Insere um tipo no escopo local
    public void insereTipoLocal(String s, Tipo t) {
        this.atual().insereTipo(s, t);
    }
    
    // Insere uma variavel no escopo global
    public void insereVariavelGlobal(String s, Variavel v) {
        this.global().insereVariavel(s, v);
    }
    
    // Insere um procedimento no escopo global
    public void insereProcedimentoGlobal(String s, Procedimento p) {
        this.global().insereProcedimento(s, p);
    }
    
    // Insere uma função no escopo global
    public void insereFuncaoGlobal(String s, Funcao f) {
        this.global().insereFuncao(s, f);
    }
    
    // Insere um tipo no escopo global
    public void insereTipoGlobal(String s, Tipo t) {
        this.global().insereTipo(s, t);
    }
    
    // Retorna uma variavel
    public Variavel pegaVariavel(String s) {
        Iterator<TabelaDeSimbolos> itr = pilhaEscopos.descendingIterator();
        
        while(itr.hasNext()){
            var o = itr.next();
            
            if (o.achaVariavel(s)) {
                return o.pegaVariavel(s);
            }
        }
        
        return null;
    }
    
    // Retorna uma função
    public Funcao pegaFuncao(String s) {
        Iterator<TabelaDeSimbolos> itr = pilhaEscopos.descendingIterator();
        
        while(itr.hasNext()){
            var o = itr.next();
            
            if (o.achaFuncao(s)) {
                return o.pegaFuncao(s);
            }
        }
        
        return null;
    }
    
    // Retorna um procedimento
    public Procedimento pegaProcedimento(String s) {
        Iterator<TabelaDeSimbolos> itr = pilhaEscopos.descendingIterator();
        
        while(itr.hasNext()){
            var o = itr.next();
            
            if (o.achaProcedimento(s)) {
                return o.pegaProcedimento(s);
            }
        }
        
        return null;
    }
}
