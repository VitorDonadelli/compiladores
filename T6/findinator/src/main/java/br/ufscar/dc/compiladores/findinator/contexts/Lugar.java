package br.ufscar.dc.compiladores.findinator.contexts;

import br.ufscar.dc.compiladores.findinator.values.Path;
import java.util.List;

public class Lugar extends MyContext {
    private final List<Path> paths;
    
    public Lugar(List<Path> paths) {
        this.paths = paths;
    }
    
    public String build() {
        String s = "";
        
        for (var p : paths) {
            s += " " + p.getValue();
        }
        
        return s;
    }
}
