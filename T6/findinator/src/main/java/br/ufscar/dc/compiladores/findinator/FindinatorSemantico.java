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

    // Inicia array de erros
    FindinatorSemantico() {
        this.erros = new ArrayList();
    }
    
    @Override
    public Find visitFind(FindinatorParser.FindContext ctx) {
        
        if (ctx == null) { return null; }
        
        // Recupera as partes que formam o find
        Comando comando = Comando.make(ctx.COMANDO());
        Lugar lugar = visitLugar(ctx.lugar());
        ArquivoDiretorio arquivoDiretorio = visitArquivo_diretorio(ctx.arquivo_diretorio());
        Testes testes = visitTestes(ctx.testes());
        
        Optional<Lugar> opLugar;
        
        // Verifica se o comando está faltando 
        if (comando.isMissing()) {
            this.addError("Erro: Find faltando 'Comando'");
            return null;
        }
        
        // Verifica se foi ou não passado uma pasta
        if (lugar == null) {
            opLugar = Optional.empty();
        } else {
            opLugar = Optional.of(lugar);
        }
        
        // Verifica se o token de o que buscar está faltando
        if (arquivoDiretorio == null) {
            this.addError("Erro: Find faltando 'ArquivoDiretorio'");
            return null;
        }
        
        // Verifica se há testes
        if (testes == null) {
            this.addError("Erro: Find faltando 'Testes'");
            return null;
        }
        
        // Constroi a classe que representa o contexto 
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
        
        // Recupera os separadores
        List<TerminalNode> tokensSeparador = ctx.SEPARADOR();

        int s = tokensSeparador.size();
        
        // Valida os separadores
        for (int i = 0; i < s; i++) {
            Separador separador = Separador.make(tokensSeparador.get(i));
            
            // Se o separador for o ultimo deve ser um separador do tipo "e"
            // Caso contrario deve ser um separador do tipo virgula "," 
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

        // Recupera lista de paths
        List<TerminalNode> tokensPath = ctx.PATH();
        
        int p = tokensPath.size();
        
        // Valida quantidade de separadores por path 
        if (s != (p - 1)) {
            this.addError("Erro: Lugar faltando 'Separador'");
            return null;
        }

        // Converte uma lista de TerminalNode para Lista do tipo Path
        // filtrando os que estão missing
        List<Path> paths = tokensPath.stream()
            .map((path) -> Path.make(path))
            .filter((op) -> !op.isMissing())
            .collect(toList());
        
        // Constroi a classe que representa o contexto
        return new Lugar(
            paths
        );
    }

    @Override
    public ArquivoDiretorio visitArquivo_diretorio(FindinatorParser.Arquivo_diretorioContext ctx) {
        
        if (ctx == null) { return null; }
        
        // Converte para uma lista de Tipos
        List<Tipo> tipos = ctx.TIPO().stream().map((token) -> Tipo.make(token)).collect(toList());
        
        int t = tipos.size();
        
        Tipo tipo = null;
        Tipo tipoExtra = null;
 
        // Faltando token de tipo (arquivos ou diretorios)
        if (t == 0) {
            this.addError("Erro: ArquivoDiretorio faltando 'Tipo'");
            return null;
        }
        
        // Pega o primeiro tipo caso tenha
        if (t >= 1) {
            tipo = tipos.get(0);
        }
        
        // Pega o segundo tipo caso tenha
        if (t >= 2) {
            tipoExtra = tipos.get(1);
        }
        
        // Verifica se o primeiro tipo (que é obrigatório) existe
        if (tipo == null || tipo.isMissing()) {
            this.addError("Erro: ArquivoDiretorio faltando 'Tipo'");
            return null;
        }
        
        // Mapeia o tipoExtra para Optional
        Optional<Tipo> opTipoExtra;
        
        if (tipoExtra == null) {
            opTipoExtra = Optional.empty();
        } else {
            opTipoExtra = Optional.of(tipoExtra);
            
            // Falha caso os dois tipos forem iguais
            // Exemplo de entrada: "Encontre os arquivos ou arquivos" -> erro
            if (tipo == opTipoExtra.get()) {
                this.addError("Erro: ArquivoDiretorio com 'Tipo' repetido");
                return null;
            }
        }
        
        // Constroi a classe que representa o contexto
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
        
        // Pega o primeiro teste
        if (t > 0) {
            testes.add(visitTeste(contextsTeste.get(0)));
        } else {
            // Caso não tenha testes
            this.addError("Erro: Testes com zero 'Teste'");
            return null;
        }
        
        for (int i = 0, j = 1; i < s && j < t; i++, j++) {
            // Recupera cada separador/teste
            Separador separador = Separador.make(nodesSeparador.get(i));
            Teste teste = visitTeste(contextsTeste.get(j));
            
            // Caso não tenha separador
            if (separador.isMissing()) {
                this.addError("Erro: Testes faltando 'Separador'");
                return null;
            } else {
                // Se o separador for o ultimo deve ser um separador do tipo "e"
                // Caso contrario deve ser um separador do tipo virgula "," 
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
            
            // Valida se teste existe
            if (teste == null) {
                this.addError("Erro: Testes faltando 'Teste'");
                return null;
            } else {
                testes.add(teste);
            }
        }
        
        // Caso não tenha teste
        if (testes.isEmpty()) {
            this.addError("Erro: Testes com zero 'Teste'");
            return null;
        }
        
        // Constroi a classe que representa o contexto
        return new Testes(testes);
    }

    // Visita o child pra não precisar visitar todos os testes possiveis
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
        
        // Recupera os constituintes de um Verbo
        Conector conector = Conector.make(ctx.CONECTOR());
        VerboEnum verboEnum = VerboEnum.make(ctx.VERBO());
        Optional<ModificadorVerbo> opModificadorVerbo = ModificadorVerbo.make(ctx.MODIFICADOR_VERBO());
        NumeroValue numeroValue = NumeroValue.make(ctx.NUM());
        TimeUnit timeUnit = TimeUnit.make(ctx.TIME_UNIT());
        
        // Caso esteja faltando o conector
        if (conector.isMissing()) {
            this.addError("Erro: Verbo sem 'Conector'");
            return null;
        } else if ((conector != Conector.QUE) && (conector != Conector.O_QUAL)) {
            // Valida o conector
            this.addError("Erro: Verbo com 'Conector' errado");
            return null;
        }
        
        // Caso não tenha o token VERBO do Verbo
        if (verboEnum.isMissing()) {
            this.addError("Erro: Verbo sem 'VerboEnum'");
            return null;
        }

        // Caso não tenha Value
        if (numeroValue.isMissing()) {
            this.addError("Erro: Verbo sem 'NumeroValue'");
            return null;
        }
        
        // Caso não tenha TimeUnit
        if (timeUnit.isMissing()) {
            this.addError("Erro: Verbo sem 'TimeUnit'");
            return null;
        }
        
        // Constroi a classe que representa o contexto
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
        
        // Caso esteja faltando Caracteristica
        if (caracteristicaEnum.isMissing()) {
            this.addError("Erro: Caracteristica sem 'CaracteristicaEnum'");
            return null;
        }
        
        // Constroi a classe que representa o contexto
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

        // Caso esteja faltando identificador e arquivo
        if (identificador.isMissing() && file.isMissing()) {
            this.addError("Erro: NomeTail sem 'Identificador' ou 'File'");
            return null;
        }
        
        // Coloca o item desse contexto na lista
        opModificadoresNomePath.add(opModificadorNomePath);
        identificadores.add(identificador);
        files.add(file);

        // Le recursivamente
        if (tailCtx != null) {
            NomeTail tail = visitNome_tail(tailCtx);
            
            opModificadoresNomePath.addAll(tail.getOpModificadoresNomePath());
            identificadores.addAll(tail.getIdentificadores());
            files.addAll(tail.getFiles());
        }
        
        // Constroi a classe que representa o contexto
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

        // Caso esteja faltando a path 
        if (path.isMissing()) {
            this.addError("Erro: PathTail sem 'Identificador' ou 'File'");
            return null;
        }
        
        // Coloca o item desse contexto na lista
        opModificadoresNomePath.add(opModificadorNomePath);
        paths.add(path);

        // Le recursivamente
        if (tailCtx != null) {
            PathTail tail = visitPath_tail(tailCtx);
            
            opModificadoresNomePath.addAll(tail.getOpModificadoresNomePath());
            paths.addAll(tail.getPaths());
        }
        
        // Constroi a classe que representa o contexto
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
        
        // Caso esteja faltando o conector
        if (conector.isMissing()) {
            this.addError("Erro: NomePath sem 'Conector'");
            return null;
        }
        
        // Caso esteja faltando identificador e arquivo e path
        if (identificador.isMissing() && file.isMissing() && path.isMissing()) {
            this.addError("Erro: NomePath sem 'Identificador', 'File' ou 'Path'");
            return null;
        }
        
        // Le a cauda
        NomeTail nomeTail = visitNome_tail(ctx.nome_tail());        
        PathTail pathTail = visitPath_tail(ctx.path_tail());
        
        // Constroi a classe que representa o contexto
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
        
        // Caso esteja faltando o conector
        if (conector.isMissing()) {
            this.addError("Erro: Permissao sem 'Conector'");
            return null;
        } else if (conector != Conector.COM) {
            // Caso tipo de conector esteja errado, conector esperado: "com"
            this.addError("Erro: Permissao com 'Conector' errado");
            return null;
        }
        
        // Caso esteja faltando o numero da permissão
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
                
                // Valida o valor da permissao, tres numeros de 0 a 7 ex: 555
                if (!(1 <= u && u <= 7 && 1 <= d && d <= 7 && 1 <= c && c <= 7)) {
                    this.addError("Erro: Permissao com 'NumeroValue' inválido");
                    return null;
                }
            } else {
                // value menor ou igual a 0
                this.addError("Erro: Permissao com 'NumeroValue' inválido");
                return null;
            }
        }
        
        // Constroi a classe que representa o contexto
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
        
        // Caso esteja faltando o conector
        if (conector.isMissing()) {
            this.addError("Erro: LerEscrever sem 'Conector'");
            return null;
        } else if ((conector != Conector.QUE) && (conector != Conector.O_QUAL) && (conector != Conector.TAL_QUE)) {
            // Caso tipo de conector esteja errado, conector esperado: "que" ou "o qual" ou "tal que"
            this.addError("Erro: LerEscrever com 'Conector' errado");
            return null;
        }
        
        // Caso esteja faltando o token de pode ser "lido" ou "escrito"
        if (lerEscrever.isMissing()) {
            this.addError("Erro: LerEscrever sem 'LerOuEscrever'");
            return null;
        }
        
        // Constroi a classe que representa o contexto
        return new LerEscrever(
            lerEscrever
        );
    }

    @Override
    public Usuario visitUsuario(FindinatorParser.UsuarioContext ctx) {
        
        if (ctx == null) { return null; }
        
        Identificador identificador = Identificador.make(ctx.IDENT());
        
        // Caso esteja faltando o identificador
        if (identificador.isMissing()) {
            this.addError("Erro: Usuario sem 'Identificador'");
            return null;
        }
        
        // Constroi a classe que representa o contexto
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
        
        // Caso esteja faltando o conector
        if (conector.isMissing()) {
            this.addError("Erro: Tamanho sem 'Conector'");
            return null;
        } else if (conector != Conector.QUE_TEM && conector != Conector.DE) {
            // Caso tipo de conector esteja errado, conector esperado: "que tem" ou "de"
            this.addError("Erro: Tamanho com 'Conector' errado");
            return null;
        }
        
        // Caso esteja faltando o tamanho
        if (numero.isMissing()) {
            this.addError("Erro: Tamanho sem 'NumeroValue'");
            return null;
        }
        
        // Caso esteja faltando a unidade (b, Kb, Mb, Gb)
        if (sizeUnit.isMissing()) {
            this.addError("Erro: Tamanho sem 'SizeUnit'");
            return null;
        }
        
        // Constroi a classe que representa o contexto
        return new Tamanho(
            opModificadorTamanho,
            numero,
            sizeUnit
        );
    }

    // Insere um erro no array de erros
    private void addError(String msg) {
        this.erros.add(new ErroSemantico(msg));
    }

    // Retorna os erros
    public List<ErroSemantico> getErros() {
        return this.erros;
    }
}

// Seria usado em caso de precisar de mais descrições do erro (ex: posição do erro)
class ErroSemantico {
    private final String msg;
    
    ErroSemantico(String msg) {
        this.msg = msg;
    }
    
    public String getMsg() {
        return msg;
    }
}