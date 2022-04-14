package br.ufscar.dc.compiladores.findinator.contexts;

import br.ufscar.dc.compiladores.findinator.enums.ModificadorNomePath;
import br.ufscar.dc.compiladores.findinator.values.File;
import br.ufscar.dc.compiladores.findinator.values.Identificador;
import br.ufscar.dc.compiladores.findinator.values.Path;
import java.util.List;
import java.util.Optional;

public class PathTail extends MyContext {
    private final List<Optional<ModificadorNomePath>> opModificadoresNomePath;
    private final List<Path> paths;

    public PathTail(List<Optional<ModificadorNomePath>> opModificadoresNomePath, List<Path> paths) {
        this.opModificadoresNomePath = opModificadoresNomePath;
        this.paths = paths;
    }
    
    public String build() {
        String r = "";
        
        int s = opModificadoresNomePath.size();
        
        for (int i = 0; i < s; i++) {
            String cmd = "";
            String target = "";
            
            Optional<ModificadorNomePath> opModificadorNomePath = opModificadoresNomePath.get(i);
            Path path = paths.get(i);

            // Atribui a flag e o valor
            if (!path.isMissing()) {
                cmd = "-path";
                target = path.getValue();
            } else {
                throw new RuntimeException("Parâmetros inválidos para 'NomePath'");
            }

            // Monta o comando de acordo com o modificador usado
            if (opModificadorNomePath.isEmpty()) {
                r += " -o " + String.format("%s \"%s\"", cmd, target);
            } else {
                switch (opModificadorNomePath.get()) {
                    case QUE_COMECA_COM:
                        r += " -o " + String.format("%s \"%s*\"", cmd, target);
                    case QUE_TERMINA_COM:
                        r += " -o " + String.format("%s \"*%s\"", cmd, target);
                }
            }
        }
        
        if (r.isEmpty()) {
            throw new RuntimeException("Parâmetros inválidos para 'NomeTail'");
        }
        
        return r;
    }
    
    public List<Optional<ModificadorNomePath>> getOpModificadoresNomePath() {
        return opModificadoresNomePath;
    }
    
    public List<Path> getPaths() {
        return paths;
    }
}
