package br.ufscar.dc.compiladores.findinator.contexts;

import br.ufscar.dc.compiladores.findinator.enums.ModificadorNomePath;
import br.ufscar.dc.compiladores.findinator.values.File;
import br.ufscar.dc.compiladores.findinator.values.Identificador;
import java.util.List;
import java.util.Optional;

public class NomeTail extends MyContext {
    private final List<Optional<ModificadorNomePath>> opModificadoresNomePath;
    private final List<Identificador> identificadores;
    private final List<File> files;

    public NomeTail(List<Optional<ModificadorNomePath>> opModificadoresNomePath, List<Identificador> identificadores, List<File> files) {
        this.opModificadoresNomePath = opModificadoresNomePath;
        this.identificadores = identificadores;
        this.files = files;
    }
    
    public String build() {
        String r = "";
        
        int s = opModificadoresNomePath.size();
        
        for (int i = 0; i < s; i++) {
            String cmd = "";
            String target = "";
            
            Optional<ModificadorNomePath> opModificadorNomePath = opModificadoresNomePath.get(i);
            Identificador identificador = identificadores.get(i);
            File file = files.get(i);

            if (!identificador.isMissing()) {
                cmd = "-name";
                target = identificador.getValue();
            } else if (!file.isMissing()) {
                cmd = "-name";
                target = file.getValue();
            } else {
                throw new RuntimeException("Parâmetros inválidos para 'NomePath'");
            }

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
    
    public List<Identificador> getIdentificadores() {
        return identificadores;
    }
    
    public List<File> getFiles() {
        return files;
    }
}
