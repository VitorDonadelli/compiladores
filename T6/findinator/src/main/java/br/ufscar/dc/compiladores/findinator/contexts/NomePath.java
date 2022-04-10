package br.ufscar.dc.compiladores.findinator.contexts;

import br.ufscar.dc.compiladores.findinator.enums.ModificadorNomePath;
import br.ufscar.dc.compiladores.findinator.values.File;
import br.ufscar.dc.compiladores.findinator.values.Identificador;
import br.ufscar.dc.compiladores.findinator.values.Path;
import java.util.List;
import java.util.Optional;

public class NomePath extends MyTesteContext {
    private final Optional<ModificadorNomePath> opModificadorNomePath;
    private final Identificador identificador;
    private final File file;
    private final Path path;
    private final NomeTail nomeTail;
    private final PathTail pathTail;
    
    public NomePath(Optional<ModificadorNomePath> opModificadoresNomePath, Identificador identificador, File file, Path path, NomeTail nomeTail, PathTail pathTail) {
        this.opModificadorNomePath = opModificadoresNomePath;
        this.identificador = identificador;
        this.file = file;
        this.path = path;
        this.nomeTail = nomeTail;
        this.pathTail = pathTail;
    }
    
    @Override
    public String build() {

        String r = "";

        String cmd = "";
        String target = "";

        if (!identificador.isMissing()) {
            cmd = " -name";
            target = identificador.getValue();
        } else if (!file.isMissing()) {
            cmd = " -name";
            target = file.getValue();
        } else if (!path.isMissing()) {
            cmd = " -path";
            target = path.getValue();
        } else {
            throw new RuntimeException("Parâmetros inválidos para 'NomePath'");
        }

        if (opModificadorNomePath.isEmpty()) {
            r += " \\(" + String.format("%s \"%s\"", cmd, target);
        } else {
            switch (opModificadorNomePath.get()) {
                case QUE_COMECA_COM:
                    r += " \\(" + String.format("%s \"%s*\"", cmd, target);
                case QUE_TERMINA_COM:
                    r += " \\(" + String.format("%s \"*%s\"", cmd, target);
            }
        }
        
        if (nomeTail != null) {
            r += nomeTail.build() + " \\)";
        } else if (pathTail != null) {
            r += pathTail.build() + " \\)";
        } else {
            r += " \\)";
        }
        
        if (r.isEmpty()) {
            throw new RuntimeException("Parâmetros inválidos para 'NomePath'");
        }
        
        return r;
    }
}