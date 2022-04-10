package br.ufscar.dc.compiladores.findinator;

import br.ufscar.dc.compiladores.findinator.values.File;
import br.ufscar.dc.compiladores.findinator.values.Identificador;
import br.ufscar.dc.compiladores.findinator.values.Path;
import br.ufscar.dc.compiladores.findinator.values.NumeroValue;
import br.ufscar.dc.compiladores.findinator.enums.ModificadorNomePath;
import br.ufscar.dc.compiladores.findinator.enums.CaracteristicaEnum;
import br.ufscar.dc.compiladores.findinator.enums.Tipo;
import br.ufscar.dc.compiladores.findinator.enums.Conector;
import br.ufscar.dc.compiladores.findinator.enums.SizeUnit;
import br.ufscar.dc.compiladores.findinator.enums.ModificadorVerbo;
import br.ufscar.dc.compiladores.findinator.enums.VerboEnum;
import br.ufscar.dc.compiladores.findinator.enums.ModificadorTamanho;
import br.ufscar.dc.compiladores.findinator.enums.TimeUnit;
import br.ufscar.dc.compiladores.findinator.enums.Separador;
import br.ufscar.dc.compiladores.findinator.enums.LerOuEscrever;
import br.ufscar.dc.compiladores.findinator.enums.Comando;
import br.ufscar.dc.compiladores.findinator.contexts.MyContext;
import br.ufscar.dc.compiladores.findinator.contexts.Verbo;
import br.ufscar.dc.compiladores.findinator.contexts.Permissao;
import br.ufscar.dc.compiladores.findinator.contexts.Lugar;
import br.ufscar.dc.compiladores.findinator.contexts.NomePath;
import br.ufscar.dc.compiladores.findinator.contexts.MyTesteContext;
import br.ufscar.dc.compiladores.findinator.contexts.Usuario;
import br.ufscar.dc.compiladores.findinator.contexts.Caracteristica;
import br.ufscar.dc.compiladores.findinator.contexts.ArquivoDiretorio;
import br.ufscar.dc.compiladores.findinator.contexts.PathTail;
import br.ufscar.dc.compiladores.findinator.contexts.Find;
import br.ufscar.dc.compiladores.findinator.contexts.Teste;
import br.ufscar.dc.compiladores.findinator.contexts.Tamanho;
import br.ufscar.dc.compiladores.findinator.contexts.NomeTail;
import br.ufscar.dc.compiladores.findinator.contexts.Testes;
import br.ufscar.dc.compiladores.findinator.contexts.LerEscrever;
import br.ufscar.dc.compiladores.findinator.FindinatorParser.TesteContext;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import static java.util.stream.Collectors.toList;
import org.antlr.v4.runtime.tree.TerminalNode;

public class FindinatorSemantico extends FindinatorBaseVisitor<MyContext> {
    
    private final List<ErroSemantico> erros;

    FindinatorSemantico() {
        this.erros = new ArrayList();
    }
    
    @Override
    public Find visitFind(FindinatorParser.FindContext ctx) {
        
        if (ctx == null) { return null; }
        
        Comando comando = Comando.make(ctx.COMANDO());
        Lugar lugar = visitLugar(ctx.lugar());
        ArquivoDiretorio arquivoDiretorio = visitArquivo_diretorio(ctx.arquivo_diretorio());
        Testes testes = visitTestes(ctx.testes());
        
        Optional<Lugar> opLugar;
        
        if (comando.isMissing()) {
            this.addError("Erro: Find faltando 'Comando'");
            return null;
        }
        
        if (lugar == null) {
            opLugar = Optional.empty();
        } else {
            opLugar = Optional.of(lugar);
        }
        
        if (arquivoDiretorio == null) {
            this.addError("Erro: Find faltando 'ArquivoDiretorio'");
            return null;
        }
        
        if (testes == null) {
            this.addError("Erro: Find faltando 'Testes'");
            return null;
        }
        
        return new Find(
            comando,
            opLugar,
            arquivoDiretorio,
            testes
        );
    }

    @Override
    public Lugar visitLugar(FindinatorParser.LugarContext ctx) {
        
        if (ctx == null) { return null; }
        
        List<TerminalNode> tokensSeparador = ctx.SEPARADOR();

        int s = tokensSeparador.size();
        
        for (int i = 0; i < s; i++) {
            Separador separador = Separador.make(tokensSeparador.get(i));
            
            if (i == s - 1) {
                if (separador != Separador.E) {
                    this.addError("Erro: Lugar com 'Separador' errado, devia ser 'e'");
                    return null;
                }
            } else {
                if (separador != Separador.VIRGULA) {
                    this.addError("Erro: Lugar com 'Separador' errado, devia ser ','");
                    return null;
                }
            }
        }

        List<TerminalNode> tokensPath = ctx.PATH();
        
        int p = tokensPath.size();
        
        if (s != (p - 1)) {
            this.addError("Erro: Lugar faltando 'Separador'");
            return null;
        }

        List<Path> paths = tokensPath.stream()
            .map((path) -> Path.make(path))
            .filter((op) -> !op.isMissing())
            .collect(toList());
        
        return new Lugar(
            paths
        );
    }

    @Override
    public ArquivoDiretorio visitArquivo_diretorio(FindinatorParser.Arquivo_diretorioContext ctx) {
        
        if (ctx == null) { return null; }
        
        List<Tipo> tipos = ctx.TIPO().stream().map((token) -> Tipo.make(token)).collect(toList());
        
        int t = tipos.size();
        
        Tipo tipo = null;
        Tipo tipoExtra = null;
 
        if (t == 0) {
            this.addError("Erro: ArquivoDiretorio faltando 'Tipo'");
            return null;
        }
        
        if (t >= 1) {
            tipo = tipos.get(0);
        }
        
        if (t >= 2) {
            tipoExtra = tipos.get(1);
        }
        
        if (tipo == null || tipo.isMissing()) {
            this.addError("Erro: ArquivoDiretorio faltando 'Tipo'");
            return null;
        }
        
        Optional<Tipo> opTipoExtra;
        
        if (tipoExtra == null) {
            opTipoExtra = Optional.empty();
        } else {
            opTipoExtra = Optional.of(tipoExtra);
            
            if (tipo == opTipoExtra.get()) {
                this.addError("Erro: ArquivoDiretorio com 'Tipo' repetido");
                return null;
            }
        }
        
        return new ArquivoDiretorio(
            tipo,
            opTipoExtra
        );
    }

    @Override
    public Testes visitTestes(FindinatorParser.TestesContext ctx) {
        
        if (ctx == null) { return null; }
        
        List<Teste> testes = new ArrayList();

        List<TerminalNode> nodesSeparador = ctx.SEPARADOR();
        List<TesteContext> contextsTeste = ctx.teste();
        
        int s = nodesSeparador.size();
        int t = contextsTeste.size();
        
        // Primeiro teste
        if (t > 0) {
            testes.add(visitTeste(contextsTeste.get(0)));
        } else {
            this.addError("Erro: Testes com zero 'Teste'");
            return null;
        }
        
        for (int i = 0, j = 1; i < s && j < t; i++, j++) {
            Separador separador = Separador.make(nodesSeparador.get(i));
            Teste teste = visitTeste(contextsTeste.get(j));
            
            if (separador.isMissing()) {
                this.addError("Erro: Testes faltando 'Separador'");
                return null;
            } else {
                if (i == (s - 1)) {
                    if (separador != Separador.E) {
                        this.addError("Erro: Testes com 'Separador' errado");
                        return null;
                    }
                } else {
                    if (separador != Separador.VIRGULA) {
                        this.addError("Erro: Testes com 'Separador' errado");
                        return null;
                    }
                }
            }
            
            if (teste == null) {
                this.addError("Erro: Testes faltando 'Teste'");
                return null;
            } else {
                testes.add(teste);
            }
        }
        
        if (testes.isEmpty()) {
            this.addError("Erro: Testes com zero 'Teste'");
            return null;
        }
        
        return new Testes(testes);
    }

    @Override
    public Teste visitTeste(FindinatorParser.TesteContext ctx) {
        
        if (ctx == null) { return null; }
        
        MyTesteContext child = (MyTesteContext) visitChildren(ctx);
        
        if (child == null) {
            this.addError("Erro: Teste sem 'Teste'");
            return null;
        }
        
        return new Teste(child);
    }

    @Override
    public Verbo visitVerbo(FindinatorParser.VerboContext ctx) {
        
        if (ctx == null) { return null; }
        
        Conector conector = Conector.make(ctx.CONECTOR());
        VerboEnum verboEnum = VerboEnum.make(ctx.VERBO());
        Optional<ModificadorVerbo> opModificadorVerbo = ModificadorVerbo.make(ctx.MODIFICADOR_VERBO());
        NumeroValue numeroValue = NumeroValue.make(ctx.NUM());
        TimeUnit timeUnit = TimeUnit.make(ctx.TIME_UNIT());
        
        if (conector.isMissing()) {
            this.addError("Erro: Verbo sem 'Conector'");
            return null;
        } else if ((conector == Conector.QUE) && (conector == Conector.O_QUAL)) {
            this.addError("Erro: Verbo com 'Conector' errado");
            return null;
        }
        
        if (verboEnum.isMissing()) {
            this.addError("Erro: Verbo sem 'VerboEnum'");
            return null;
        }

        if (numeroValue.isMissing()) {
            this.addError("Erro: Verbo sem 'NumeroValue'");
            return null;
        }
        
        if (timeUnit.isMissing()) {
            this.addError("Erro: Verbo sem 'TimeUnit'");
            return null;
        }
        
        return new Verbo(
            verboEnum,
            opModificadorVerbo,
            numeroValue,
            timeUnit
        );
    }

    @Override
    public Caracteristica visitCaracteristica(FindinatorParser.CaracteristicaContext ctx) {
        
        if (ctx == null) { return null; }
        
        CaracteristicaEnum caracteristicaEnum = CaracteristicaEnum.make(ctx.CARACTERISTICA());
        
        if (caracteristicaEnum.isMissing()) {
            this.addError("Erro: Caracteristica sem 'CaracteristicaEnum'");
            return null;
        }
        
        return new Caracteristica(
            caracteristicaEnum
        );
    }
    
    @Override
    public NomeTail visitNome_tail(FindinatorParser.Nome_tailContext ctx) {
        
        if (ctx == null) { return null; }
        
        List<Optional<ModificadorNomePath>> opModificadoresNomePath = new ArrayList();
        List<Identificador> identificadores = new ArrayList();
        List<File> files = new ArrayList();
        
        Optional<ModificadorNomePath> opModificadorNomePath = ModificadorNomePath.make(ctx.MODIFICADOR_NOME_PATH());
        Identificador identificador = Identificador.make(ctx.IDENT());
        File file = File.make(ctx.FILE());
        FindinatorParser.Nome_tailContext tailCtx = ctx.nome_tail();

        if (identificador.isMissing() && file.isMissing()) {
            this.addError("Erro: NomeTail sem 'Identificador' ou 'File'");
            return null;
        }
        
        opModificadoresNomePath.add(opModificadorNomePath);
        identificadores.add(identificador);
        files.add(file);

        if (tailCtx != null) {
            NomeTail tail = visitNome_tail(tailCtx);
            
            opModificadoresNomePath.addAll(tail.getOpModificadoresNomePath());
            identificadores.addAll(tail.getIdentificadores());
            files.addAll(tail.getFiles());
        }
        
        return new NomeTail(
            opModificadoresNomePath,
            identificadores,
            files
        );
    }

    @Override
    public PathTail visitPath_tail(FindinatorParser.Path_tailContext ctx) {
        
        if (ctx == null) { return null; }
        
        List<Optional<ModificadorNomePath>> opModificadoresNomePath = new ArrayList();
        List<Path> paths = new ArrayList();
        
        Optional<ModificadorNomePath> opModificadorNomePath = ModificadorNomePath.make(ctx.MODIFICADOR_NOME_PATH());
        Path path = Path.make(ctx.PATH());
        FindinatorParser.Path_tailContext tailCtx = ctx.path_tail();

        if (path.isMissing()) {
            this.addError("Erro: PathTail sem 'Identificador' ou 'File'");
            return null;
        }
        
        opModificadoresNomePath.add(opModificadorNomePath);
        paths.add(path);

        if (tailCtx != null) {
            PathTail tail = visitPath_tail(tailCtx);
            
            opModificadoresNomePath.addAll(tail.getOpModificadoresNomePath());
            paths.addAll(tail.getPaths());
        }
        
        return new PathTail(
            opModificadoresNomePath,
            paths
        );
    }
        
    @Override
    public NomePath visitNome_path(FindinatorParser.Nome_pathContext ctx) {
        
        if (ctx == null) { return null; }
                
        Conector conector = Conector.make(ctx.CONECTOR());
        Optional<ModificadorNomePath> opModificadorNomePath = ModificadorNomePath.make(ctx.MODIFICADOR_NOME_PATH());
        Identificador identificador = Identificador.make(ctx.IDENT());
        File file = File.make(ctx.FILE());
        Path path = Path.make(ctx.PATH());
        
        if (conector.isMissing()) {
            this.addError("Erro: NomePath sem 'Conector'");
            return null;
        }
        
        if (identificador.isMissing() && file.isMissing() && path.isMissing()) {
            this.addError("Erro: NomePath sem 'Identificador', 'File' ou 'Path'");
            return null;
        }
        
        NomeTail nomeTail = visitNome_tail(ctx.nome_tail());        
        PathTail pathTail = visitPath_tail(ctx.path_tail());
        
        return new NomePath(
            opModificadorNomePath,
            identificador,
            file,
            path,
            nomeTail,
            pathTail
        );
    }

    @Override
    public Permissao visitPermissao(FindinatorParser.PermissaoContext ctx) {
        
        if (ctx == null) { return null; }
        
        Conector conector = Conector.make(ctx.CONECTOR());
        NumeroValue numeroValue = NumeroValue.make(ctx.NUM());
        
        if (conector.isMissing()) {
            this.addError("Erro: Permissao sem 'Conector'");
            return null;
        } else if (conector != Conector.COM) {
            this.addError("Erro: Permissao com 'Conector' errado");
            return null;
        }
        
        if (numeroValue.isMissing()) {
            this.addError("Erro: Permissao sem 'NumeroValue'");
            return null;
        } else {
            int value = numeroValue.getValue();
            
            if (0 < value) {
                int u = value % 10;
                value /= 10;
                int d = value % 10;
                value /= 10;
                int c = value % 10;
                
                if (!(1 <= u && u <= 7 && 1 <= d && d <= 7 && 1 <= c && c <= 7)) {
                    this.addError("Erro: Permissao com 'NumeroValue' inválido");
                    return null;
                }
            } else {
                this.addError("Erro: Permissao com 'NumeroValue' inválido");
                return null;
            }
        }
        
        return new Permissao(
            conector,
            numeroValue
        );
    }

    @Override
    public LerEscrever visitLer_escrever(FindinatorParser.Ler_escreverContext ctx) {
        
        if (ctx == null) { return null; }
        
        Conector conector = Conector.make(ctx.CONECTOR());
        LerOuEscrever lerEscrever = LerOuEscrever.make(ctx.LER_ESCREVER());
        
        if (conector.isMissing()) {
            this.addError("Erro: LerEscrever sem 'Conector'");
            return null;
        } else if ((conector != Conector.QUE) && (conector != Conector.O_QUAL) && (conector != Conector.TAL_QUE)) {
            this.addError("Erro: LerEscrever com 'Conector' errado");
            return null;
        }
        
        if (lerEscrever.isMissing()) {
            this.addError("Erro: LerEscrever sem 'LerOuEscrever'");
            return null;
        }
        
        return new LerEscrever(
            lerEscrever
        );
    }

    @Override
    public Usuario visitUsuario(FindinatorParser.UsuarioContext ctx) {
        
        if (ctx == null) { return null; }
        
        Identificador identificador = Identificador.make(ctx.IDENT());
        
        if (identificador.isMissing()) {
            this.addError("Erro: Usuario sem 'Identificador'");
            return null;
        }
        
        return new Usuario(
            identificador
        );
    }

    @Override
    public Tamanho visitTamanho(FindinatorParser.TamanhoContext ctx) {
        
        if (ctx == null) { return null; }
        
        Conector conector = Conector.make(ctx.CONECTOR());
        Optional<ModificadorTamanho> opModificadorTamanho = ModificadorTamanho.make(ctx.MODIFICADOR_TAMANHO());
        NumeroValue numero = NumeroValue.make(ctx.NUM());
        SizeUnit sizeUnit = SizeUnit.make(ctx.SIZE_UNIT());
        
        if (conector.isMissing()) {
            this.addError("Erro: Tamanho sem 'Conector'");
            return null;
        } else if (conector != Conector.QUE_TEM && conector != Conector.DE) {
            this.addError("Erro: Tamanho com 'Conector' errado");
            return null;
        }
        
        if (numero.isMissing()) {
            this.addError("Erro: Tamanho sem 'NumeroValue'");
            return null;
        }
        
        if (sizeUnit.isMissing()) {
            this.addError("Erro: Tamanho sem 'SizeUnit'");
            return null;
        }
        
        return new Tamanho(
            opModificadorTamanho,
            numero,
            sizeUnit
        );
    }
    
    private void addError(String msg) {
        this.erros.add(new ErroSemantico(msg));
    }

    public List<ErroSemantico> getErros() {
        return this.erros;
    }
}

class ErroSemantico {
    private final String msg;
    
    ErroSemantico(String msg) {
        this.msg = msg;
    }
    
    public String getMsg() {
        return msg;
    }
}