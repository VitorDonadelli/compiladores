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
        
        // Caso tenha a pasta
        if (!opLugar.isEmpty()) {
            cmd += opLugar.get().build();
        }
        
        // Adiciona no comando a parte de arquivo e diretorio
        cmd += arquivoDiretorio.build();
        
        // Adiciona no comando final um comando 
        switch (comando) {
            case MISSING:
                // Erro
                break;
            case ENCONTRE:
                // Somente o "find"
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
        
        // Adiciona no comando a parte de teste
        cmd += testes.build();
        
        return cmd;
    }
}
