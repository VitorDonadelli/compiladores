package br.ufscar.dc.compiladores.findinator.contexts;

import br.ufscar.dc.compiladores.findinator.enums.Comando;
import java.util.Optional;

public class Find extends MyContext {
    private final Comando comando;
    private final Optional<Lugar> opLugar;
    private final ArquivoDiretorio arquivoDiretorio;
    private final Testes testes;
    
    public Find(Comando comando, Optional<Lugar> opLugar, ArquivoDiretorio arquivoDiretorio, Testes testes) {
        this.comando = comando;
        this.opLugar = opLugar;
        this.arquivoDiretorio = arquivoDiretorio;
        this.testes = testes;
    }
    
    public String build() {
        String cmd = "find";
        
        //
        
        if (!opLugar.isEmpty()) {
            cmd += opLugar.get().build();
        }
        
        //
        
        cmd += arquivoDiretorio.build();
        
        //
        
        switch (comando) {
            case MISSING:
                // Erro
                break;
            case ENCONTRE:
                // Nada
                break;
            case DEBUG:
                cmd += " -print";
                break;
            case DELETE:
                cmd += " -delete";
                break;
            case LISTE:
                cmd += " -ls";
                break;
        }
        
        //
        
        cmd += testes.build();
        
        return cmd;
    }
}
