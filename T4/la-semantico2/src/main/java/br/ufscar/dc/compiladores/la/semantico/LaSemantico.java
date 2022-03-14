package br.ufscar.dc.compiladores.la.semantico;

import br.ufscar.dc.compiladores.la.semantico.contexts.*;
import br.ufscar.dc.compiladores.la.semantico.contexts.MyContextOp1.Op1;
import br.ufscar.dc.compiladores.la.semantico.contexts.MyContextOp2.Op2;
import br.ufscar.dc.compiladores.la.semantico.contexts.MyContextOp3.Op3;
import br.ufscar.dc.compiladores.la.semantico.contexts.MyContextOpLogico.OpLogico;
import br.ufscar.dc.compiladores.la.semantico.contexts.MyContextOpRelacional.OpRelacional;
import br.ufscar.dc.compiladores.la.semantico.contexts.MyContextOpUnario.OpUnario;
import br.ufscar.dc.compiladores.la.semantico.tabelaDeSimbolos.*;
import br.ufscar.dc.compiladores.la.semantico.tabelaDeSimbolos.Tipo.TipoBase;
import static java.util.stream.Collectors.toList;
import org.antlr.v4.runtime.Token;

public class LaSemantico extends LaBaseVisitor<MyContext> {
    
    Escopos escopos = new Escopos();
    
    // Cria os tipos base
    Tipo LITERAL = new Tipo(TipoBase.LITERAL, "literal");
    Tipo INTEIRO = new Tipo(TipoBase.INTEIRO, "inteiro");
    Tipo REAL    = new Tipo(TipoBase.REAL, "real");
    Tipo LOGICO  = new Tipo(TipoBase.LOGICO, "logico");
    
    @Override
    public MyContext visitPrograma(LaParser.ProgramaContext ctx) {
       
        // Adiciona escopo global
        escopos.novo();
        
        // Adiciona os tipos base
        escopos.insereTipoGlobal("literal", LITERAL);
        escopos.insereTipoGlobal("inteiro", INTEIRO);
        escopos.insereTipoGlobal("real",    REAL);
        escopos.insereTipoGlobal("logico",  LOGICO);
        
        return visitChildren(ctx);
    }
    
    @Override
    public MyContext visitDeclaracao_local(LaParser.Declaracao_localContext ctx) {
        // Recupera os tokens
        var variavel       = ctx.variavel();
        var identConstante = ctx.identConstante;
        var tipoBasico     = ctx.tipo_basico();
        var valorConstante = ctx.valor_constante();
        var identTipo      = ctx.identTipo;
        var tipo           = ctx.tipo();
        
        if (variavel != null) {
            // Caso seja variavel
            
            return visitVariavel(variavel);
        } else if (identConstante != null && tipoBasico != null && valorConstante != null) {
            // Caso seja constante
            
            var nomeIdent         = identConstante.getText();
            var ctxTipoBasico     = visitTipo_basico(tipoBasico);
            var ctxValorConstante = visitValor_constante(valorConstante);
            
            // Verifica se o nome existe no escopo
            if (escopos.acha(nomeIdent)) {
                LaSemanticoUtils.addErrIdentificadorJaDeclarado(ctx.start, nomeIdent);
            } else {
                // Insere no escopo local
                escopos.insereVariavelLocal(nomeIdent, new Variavel(ctxTipoBasico.getTipo()));
            }
            
            // Verifica se os tipos são equivalentes
            if (!match(ctxTipoBasico.getTipo(), ctxValorConstante.getTipo())) {
                LaSemanticoUtils.addErrAtribuicaoNaoCompativel(valorConstante.start, nomeIdent);
            }
            
            return new MyContext();
        } else if (identTipo != null && tipo != null) {
            // Caso seja tipo
            
            var nomeIdent = identTipo.getText();
            var ctxTipo = visitTipo(tipo);
            
            // Verifica se o nome existe
            if (escopos.acha(nomeIdent)) {
                LaSemanticoUtils.addErrIdentificadorJaDeclarado(ctx.start, nomeIdent);
            } else {
                // Coloca o tipo no escopo local
                escopos.insereTipoLocal(nomeIdent, ctxTipo.getTipo());
            }
            
            return new MyContext();
        }
        
        return null;
    }

    @Override
    public MyContext visitVariavel(LaParser.VariavelContext ctx) {
        // Recupera os tokens
        var identificador = ctx.identificador();
        var tipo = ctx.tipo();
        
        var ctxTipo = visitTipo(tipo);
        
        System.out.println(ctxTipo.getTipo().getName());
        
        var nomeTipo = ctxTipo.getTipo().getName();
        
        // Verifica se o tipo ja existe no escopo
        if (!nomeTipo.isBlank() && !escopos.acha(nomeTipo)) {
            LaSemanticoUtils.addErrTipoNaoDeclarado(tipo.start, nomeTipo);
        } else {
            // Coloca o tipo no escopo local
            escopos.insereTipoLocal(nomeTipo, ctxTipo.getTipo());
        }
        
        for (var ident : identificador) {
            var ctxIdent = visitIdentificador(ident);
            
            var nomeIdent = ctxIdent.getName();
            
            // Verifica se o nome existe
            if (escopos.achaLocal(nomeIdent)) {
                LaSemanticoUtils.addErrIdentificadorJaDeclarado(ident.start, nomeIdent);
            } else {
                // Coloca a variavel no escopo local
                escopos.insereVariavelLocal(nomeIdent, new Variavel(ctxTipo.getTipo()));
            }
        }
        
        return new MyContext();
    }
    
    @Override
    public MyContextIdentificador visitIdentificador(LaParser.IdentificadorContext ctx) {
        var ident = ctx.ident1.getText();
        var otherIdents = ctx.ident2.stream().map(Token::getText).collect(toList());
        var ctxDimensao = visitDimensao(ctx.dimensao());
                
        return new MyContextIdentificador(ident, otherIdents, ctxDimensao);
    }
    
    @Override
    public MyContextDimensao visitDimensao(LaParser.DimensaoContext ctx) {
        // TODO
        return new MyContextDimensao();
    }
    
    @Override
    public MyContextTipo visitTipo(LaParser.TipoContext ctx) {
        var registro = ctx.registro();
        var tipoEstendido = ctx.tipo_estendido();
        
        if (registro != null) {
            return new MyContextTipo(visitRegistro(registro).getTipo());
        } else if (tipoEstendido != null) {
            return new MyContextTipo(visitTipo_estendido(tipoEstendido).getTipo());
        }
        
        throw new RuntimeException("Error no visitTipo");
    }
    
    @Override
    public MyContextTipoEstendido visitTipo_estendido(LaParser.Tipo_estendidoContext ctx) {
        // Recupera o token e visita recursivamente
        var ponteiro = ctx.ponteiro;
        var tipoBasicoIdent = ctx.tipo_basico_ident();
        var tipoBasicoIdentMyCtx = visitTipo_basico_ident(tipoBasicoIdent);
        
        var tipo = tipoBasicoIdentMyCtx.getTipo();
        
        // Verifica se eh ponteiro
        if (ponteiro != null) {
            tipo.setEhPonteiro();
        }
        
        return new MyContextTipoEstendido(tipo);
    }
    
    @Override
    public MyContextValorConstante visitValor_constante(LaParser.Valor_constanteContext ctx) {
        var cadeia = ctx.CADEIA();
        var numInt = ctx.NUM_INT();
        var numReal = ctx.NUM_REAL();
        var booleanLiteral = ctx.getText();
        
        if (cadeia != null) {
            return new MyContextValorConstante(LITERAL);
        } else if (numInt != null) {
            return new MyContextValorConstante(INTEIRO);
        } else if (numReal != null) {
            return new MyContextValorConstante(REAL);
        } else if (booleanLiteral != null) {
            return new MyContextValorConstante(LOGICO);
        }
       
        throw new RuntimeException("Error in MyContextValorConstante");
    }
    
    @Override
    public MyContextTipoBasico visitTipo_basico_ident(LaParser.Tipo_basico_identContext ctx) {
        // Recupera os tokens
        var tipoBasico = ctx.tipo_basico();
        var ident = ctx.IDENT();
        
        if (tipoBasico != null) {
            // Tipo basico
            var ctxTipoBasico = visitTipo_basico(tipoBasico);
            
            return new MyContextTipoBasico(ctxTipoBasico.getTipo());
        } else if (ident != null) {
            // Não é tipo basico
            var nomeTipo = ident.getText();
            
            Tipo tipo = escopos.pegaTipo(nomeTipo);
            
            return new MyContextTipoBasico(tipo);
        }
        
        throw new RuntimeException("Error in visitTipo_basico_ident");
    }
    
    @Override 
    public MyContextRegistro visitRegistro(LaParser.RegistroContext ctx) {
        // Adiciona escopo do registro
        escopos.novo();
        
        // Visita as declarações do registro
        visitChildren(ctx);
        
        // Recupera o escopo do registro
        var escopoRegistro = escopos.atual();
        
        //Abandona escopo do registro
        escopos.abandonar();
        
        var tipoRegistro = new Tipo(TipoBase.REGISTRO, "");
        tipoRegistro.setEscopo(escopoRegistro);
        
        // Cria tipo registro abstrato (sem nome)
        return new MyContextRegistro(tipoRegistro);
    }
    
    @Override 
    public MyContext visitDeclaracao_global(LaParser.Declaracao_globalContext ctx) {
        // Recupera os tokens
        var ident = ctx.IDENT();
        var nome = ident.getText();
        
        if (!escopos.achaLocal(nome)) {
            LaSemanticoUtils.addErrIdentificadorJaDeclarado(ctx.start, nome);
        }
        
        if (ctx.tipo_estendido() == null) {
            // Procedimento
            escopos.insereProcedimentoLocal(nome, new Procedimento());

            // Cria escopo do procedimento/função  
            escopos.novo();

            // Visita os parâmetros
            visitParametros(ctx.parametros());
            
            // Visita as declarações locais
            for (var d : ctx.declaracao_local()) {
                visitDeclaracao_local(d);
            }
            
            // Visita os comandos
            for (var c : ctx.cmd()) {
                visitCmd(c);
            }

            // Abandona escopo do procedimento/função
            escopos.abandonar();

            return new MyContext();
        } else {
            // Função
            var tipoEstendido = ctx.tipo_estendido();
            var tipoEstendidoMyCtx = visitTipo_estendido(tipoEstendido);

            // verificar se o tipo esta no escopo

            // Cria escopo do procedimento/função  
            escopos.novo();

            // Visita os parâmetros
            visitParametros(ctx.parametros());
            
            // Visita as declarações locais
            for (var d : ctx.declaracao_local()) {
                visitDeclaracao_local(d);
            }
            
            // Visita os comandos
            for (var c : ctx.cmd()) {
                visitCmd(c);
            }

            // Abandona escopo do procedimento/função
            escopos.abandonar();

            escopos.insereFuncaoLocal(nome, new Funcao(tipoEstendidoMyCtx.getTipo()));
            
            return new MyContext();
        }
    }
    
    @Override 
    public MyContext visitParametro(LaParser.ParametroContext ctx) {
        // Recupera os tokens
        var identificadores = ctx.identificador();
        var tipoEstendido = ctx.tipo_estendido();
        
        if (identificadores == null || tipoEstendido == null) {
            throw new RuntimeException("Error in visitParametro");
        }
        
        var ctxIdentificadores = identificadores.stream().map((ident) -> visitIdentificador(ident)).collect(toList());
        var ctxTipoEstendido = visitTipo_estendido(tipoEstendido);

        var nomeTipo = ctxTipoEstendido.getTipo().getName();
        
        // Verifica se o tipo já existe no escopo
        if (!escopos.acha(nomeTipo)) {
            LaSemanticoUtils.addErrTipoNaoDeclarado(tipoEstendido.start, nomeTipo);
        } else {
            // Coloca o tipo no escopo local
            escopos.insereTipoLocal(nomeTipo, ctxTipoEstendido.getTipo());
        }
        
        for (var ctxIdent : ctxIdentificadores) {
            var nomeIdent = ctxIdent.getName();
            
            // Verifica se o nome existe
            if (escopos.achaLocal(nomeIdent)) {
                LaSemanticoUtils.addErrIdentificadorJaDeclarado(identificadores.get(0).start, nomeIdent);
            } else {
                // Caso não, insere uma variavel no escopo local
                escopos.insereVariavelLocal(nomeIdent, new Variavel(ctxTipoEstendido.getTipo()));
            }
        }
        
        return new MyContext();
    }
    
    @Override 
    public MyContext visitCmdLeia(LaParser.CmdLeiaContext ctx) {
        // Recupera os tokens
        var identificadores = ctx.identificador();
        
        // Recupera o token e verifica se existe no escopo local
        for (var identificador : identificadores) {
            var ctxIdentificador = visitIdentificador(identificador);
            
            var nomeIdent = ctxIdentificador.getName();
            
            if (!escopos.achaLocal(nomeIdent)) {
                LaSemanticoUtils.addErrIdentificadorNaoDeclarado(identificador.start, nomeIdent);
            }
        }
        
        return visitChildren(ctx);
    }
    
    @Override
    public MyContext visitCmdEscreva(LaParser.CmdEscrevaContext ctx) {
        // Recupera o token e visita expressao recursivamente
        var expressoes = ctx.expressao();
        
        for (var exp : expressoes) {
            visitExpressao(exp);
        }
        
        return new MyContext();
    }
    
    @Override 
    public MyContext visitCmdAtribuicao(LaParser.CmdAtribuicaoContext ctx) {
        // Recupera os tokens
        var ponteiro = ctx.ponteiro;
        var identificador = ctx.identificador();
        var expressao = ctx.expressao();
        
        var ctxIdentificador = visitIdentificador(identificador);
        var ctxExpressao = visitExpressao(expressao);
        
        var nomeIdent = ctxIdentificador.getName();
        
        //  Verifica se o nome existe no escopo
        if (!escopos.achaLocal(nomeIdent)) {
            LaSemanticoUtils.addErrIdentificadorNaoDeclarado(identificador.start, nomeIdent);
        }
        
        var variavel = escopos.pegaVariavel(nomeIdent);
        
        var tipoTemp = new Tipo(variavel.getTipo());
        
        if (ponteiro != null) {
            tipoTemp.setEhPonteiro();
        }
                
        // Verifica se os tipos são equivalentes
        if (!match(tipoTemp, ctxExpressao.getTipo())) {
            LaSemanticoUtils.addErrAtribuicaoNaoCompativel(identificador.start, ctxIdentificador.getNameFullName());
        }
        
        return visitChildren(ctx);
    }
    
    @Override
    public MyContext visitCmdChamada(LaParser.CmdChamadaContext ctx) {
        // Recupera o token
        var nomeIdent = ctx.IDENT().getText();
        
        // Verifical se nome já existe no escopo local
        if (!escopos.achaLocal(nomeIdent)) {
            LaSemanticoUtils.addErrIdentificadorNaoDeclarado(ctx.start, nomeIdent);
        }
        
        var expressao = ctx.expressao();
        
        for (var exp : expressao) {
            visitExpressao(exp);
        }
        
        return new MyContext();
    }
    
    @Override
    public MyContextExpressao visitExpressao(LaParser.ExpressaoContext ctx) {
        // Recupera o token 
        var termosLogicos = ctx.termo_logico();
        var opsLogicos = ctx.op_logico_1();
        
        if (opsLogicos == null || opsLogicos.isEmpty()) {
            // Não tem operador
            var termoLogico = termosLogicos.get(0);
            
            var ctxTermoLogico = visitTermo_logico(termoLogico);
            
            return new MyContextExpressao(ctxTermoLogico.getTipo());
        } else {
            // Visita os termos
            for (var termoLogico : termosLogicos) {
                visitTermo_logico(termoLogico);
            }
            
            return new MyContextExpressao(LOGICO);
        }
    }
    
    @Override
    public MyContextTermoLogico visitTermo_logico(LaParser.Termo_logicoContext ctx) {
        // Recupera o token 
        var fatoresLogicos = ctx.fator_logico();
        var opslogicos = ctx.op_logico_2();
        
        if (opslogicos == null || opslogicos.isEmpty()) {
            // Não tem operador
            var fatorLogico = fatoresLogicos.get(0);
            
            var ctxFatorLogico = visitFator_logico(fatorLogico);
            
            return new MyContextTermoLogico(ctxFatorLogico.getTipo());
        } else {
            // Visita os fatores
            for (var fatorLogico : fatoresLogicos) {
                visitFator_logico(fatorLogico);
            }

            return new MyContextTermoLogico(LOGICO);
        }
    }
    
    @Override
    public MyContextFatorLogico visitFator_logico(LaParser.Fator_logicoContext ctx) {
        // Recupera os tokens
        var parcelaLogica = ctx.parcela_logica();
        
        var ctxParcelaLogica = visitParcela_logica(parcelaLogica);
        
        return new MyContextFatorLogico(ctxParcelaLogica.getTipo());
    }
    
    @Override
    public MyContextParcelaLogica visitParcela_logica(LaParser.Parcela_logicaContext ctx) {
        // Recupera o token
        var expRelacional = ctx.exp_relacional();
        
        if (expRelacional == null) {
            // Não tem expressão
            
            return new MyContextParcelaLogica(LOGICO);
        } else {
            // Visita expressão relacional
            var ctxExpRelacional = visitExp_relacional(expRelacional);

            return new MyContextParcelaLogica(ctxExpRelacional.getTipo());
        }
    }
    
    @Override
    public MyContextExpRelacional visitExp_relacional(LaParser.Exp_relacionalContext ctx) {
        // Recupera o token
        var expsAritmeticas = ctx.exp_aritmetica();
        var opsRelacionais = ctx.op_relacional();
        
        if (opsRelacionais == null || opsRelacionais.isEmpty()) {
            // Não tem operador
            var expAritmetica = expsAritmeticas.get(0);
            
            var ctxExpAritmetica = visitExp_aritmetica(expAritmetica);
            
            return new MyContextExpRelacional(ctxExpAritmetica.getTipo());
        } else {
            // Visita as expressoes aritmeticas
            for (var expAritmetica : expsAritmeticas) {
                visitExp_aritmetica(expAritmetica);
            }

            return new MyContextExpRelacional(LOGICO);
        }
    }

    @Override
    public MyContextExpAritmetica visitExp_aritmetica(LaParser.Exp_aritmeticaContext ctx) {
        // Recupera o token
        var termos = ctx.termo();
        var ops = ctx.op1();
        
        if (ops == null || ops.isEmpty()) {
            var termo = termos.get(0);
            
            var ctxTermo = visitTermo(termo);
            
            return new MyContextExpAritmetica(ctxTermo.getTipo());
        } else {
            MyContextTermo ctxTermo = null;
            
            // Visita os termos
            for (var termo : termos) {
                ctxTermo = visitTermo(termo);
            }

            assert(ctxTermo != null);

            return new MyContextExpAritmetica(ctxTermo.getTipo());
        }
    }
    
    @Override
    public MyContextTermo visitTermo(LaParser.TermoContext ctx) {
        // Recupera o token
        var fatores = ctx.fator();
        var ops = ctx.op2();
        
        if (ops == null || ops.isEmpty()) {
            var fator = fatores.get(0);
            
            var ctxFator = visitFator(fator);
            
            return new MyContextTermo(ctxFator.getTipo());
        } else {
            MyContextFator ctxFator = null;
                
            // Visita os fatores
            for (var fator : fatores) {
                ctxFator = visitFator(fator);
            }    

            assert(ctxFator != null);

            return new MyContextTermo(ctxFator.getTipo());
        }
    }
    
    @Override
    public MyContextFator visitFator(LaParser.FatorContext ctx) {
        // Recupera o token
        var parcelas = ctx.parcela();
        var ops = ctx.op3();
        
        if (ops == null || ops.isEmpty()) {
            var parcela = parcelas.get(0);
            
            var ctxParcela = visitParcela(parcela);
            
            return new MyContextFator(ctxParcela.getTipo());
        } else {
            MyContextParcela ctxParcela = null;
                
            // Visita as parcelas recursivamente
            for (var parcela : parcelas) {
                ctxParcela = visitParcela(parcela);
            }

            assert(ctxParcela != null);

            return new MyContextFator(ctxParcela.getTipo());
        }
    }
    
    @Override
    public MyContextParcela visitParcela(LaParser.ParcelaContext ctx) {
        // Recupera os tokens
        // var opUnario = ctx.op_unario();
        var parcelaUnario = ctx.parcela_unario();
        var parcelaNaoUnario = ctx.parcela_nao_unario();
        
        if (parcelaUnario != null) {
            // Parcela unaria
            
            var ctxParcelaUnario = visitParcela_unario(parcelaUnario);
            
            return new MyContextParcela(ctxParcelaUnario.getTipo());
        } else {
            // Parcela não unaria
            var ctxParcelaNaoUnario = visitParcela_nao_unario(parcelaNaoUnario);
            
            return new MyContextParcela(ctxParcelaNaoUnario.getTipo());
        }
    }
    
    @Override
    public MyContextParcelaUnario visitParcela_unario(LaParser.Parcela_unarioContext ctx) {
        // Recupera os tokens
        var identificador = ctx.identificador();
        var ident = ctx.IDENT();
        var expressoes = ctx.expressao();
        var numInt = ctx.NUM_INT();
        var numReal = ctx.NUM_REAL();
        var parcelaUnarioExp = ctx.parcelaUnarioExp;
        
        if (identificador != null) {
            // Variavel
            var ctxIdentificador = visitIdentificador(identificador);
            
            var nomeIdent = ctxIdentificador.getName();
            
            // Verifica se o nome existe no escopo
            if (!escopos.achaLocal(nomeIdent)) {
                LaSemanticoUtils.addErrIdentificadorNaoDeclarado(identificador.start, nomeIdent);
                
                return new MyContextParcelaUnario(new Tipo());
            } else {
                var variavel = escopos.pegaVariavel(nomeIdent);
                
                return new MyContextParcelaUnario(variavel.getTipo());
            }
        } else if (ident != null) {
            // Função
            var nomeFuncao = ident.getText();
        
            for (var expressao : expressoes) {
                visitExpressao(expressao);
            }
            
            // Verifica se a função existe no escopo
            if (!escopos.achaFuncaoLocal(nomeFuncao)) {
                LaSemanticoUtils.addErrIdentificadorNaoDeclarado(ctx.start, nomeFuncao);
                
                return new MyContextParcelaUnario(new Tipo());
            } else {
                var funcao = escopos.pegaFuncao(nomeFuncao);
            
                if (funcao != null) {
                    // Função
                    
                    return new MyContextParcelaUnario(funcao.getTipo());
                } else {
                    // Procedimento
                    
                    return new MyContextParcelaUnario(new Tipo());
                }
            }
        } else if (numInt != null) {
            // Inteiro
            
            return new MyContextParcelaUnario(INTEIRO);
        } else if (numReal != null) {
            // Real
            
            return new MyContextParcelaUnario(REAL);
        } else if (parcelaUnarioExp != null) {
            // Visita a expressao
            
            var ctxParcelaUnarioExp = visitExpressao(parcelaUnarioExp);
            
            return new MyContextParcelaUnario(ctxParcelaUnarioExp.getTipo());
        }
        
        return null;
    }
    
    @Override
    public MyContextParcelaNaoUnario visitParcela_nao_unario(LaParser.Parcela_nao_unarioContext ctx) {
        // Recupera os tokens
        var endereco = ctx.endereco;
        var identificador = ctx.identificador();
        var cadeia = ctx.CADEIA();
        
        // Verifica se é cadeia ou identificador
        if (cadeia != null) {
            // Cadeia
            
            return new MyContextParcelaNaoUnario(LITERAL);
        } else if (identificador != null) {
            // Identificador
            
            var ctxIdentificador = visitIdentificador(identificador);
            
            var nomeIdent = ctxIdentificador.getName();
            
            // Verifica se o identificador esta no escopo
            if (!escopos.achaVariavel(nomeIdent)) {
                LaSemanticoUtils.addErrIdentificadorNaoDeclarado(identificador.start, nomeIdent);
            }
            
            var variavel = escopos.pegaVariavel(nomeIdent);
            
            var tipo = variavel.getTipo();
            
            // Verifica se é endereço
            if (endereco != null) {
                // É
                
                tipo.setEhPonteiro();
                
                return new MyContextParcelaNaoUnario(tipo);
            } else {
                // Não é
                
                return new MyContextParcelaNaoUnario(tipo);
            }
        }
        
        throw new RuntimeException("Error in MyContextParcelaNaoUnario");
    }
    
    // Sem filhos
    // Retornam um MyContext que lhes representam
    
    @Override
    public MyContextTipoBasico visitTipo_basico(LaParser.Tipo_basicoContext ctx) {
        Tipo tipo = null;
        
        switch (ctx.getText()) {
            case "literal":
                tipo = LITERAL;
                break;
            case "inteiro":
                tipo = INTEIRO;
                break;
            case "real":
                tipo = REAL;
                break;
            case "logico":
                tipo = LOGICO;
                break;
        }
        
        assert(tipo != null);
    
        return new MyContextTipoBasico(tipo);
    }
    
    @Override
    public MyContextOpUnario visitOp_unario(LaParser.Op_unarioContext ctx) {
        return new MyContextOpUnario(OpUnario.MINUS);
    }
    
    @Override
    public MyContextOp1 visitOp1(LaParser.Op1Context ctx) {
        Op1 op = null;
        
        switch (ctx.getText()) {
            case "*":
                op = Op1.SUM;
                break;
            case "/":
                op = Op1.SUB;
                break;
        }
        
        assert(op != null);
        
        return new MyContextOp1(op);
    }
    
    @Override
    public MyContextOp2 visitOp2(LaParser.Op2Context ctx) {
        Op2 op = null;
        
        switch (ctx.getText()) {
            case "*":
                op = Op2.MUL;
                break;
            case "/":
                op = Op2.DIV;
                break;
        }
        
        assert(op != null);
        
        return new MyContextOp2(op);
    }
    
    @Override
    public MyContextOp3 visitOp3(LaParser.Op3Context ctx) {
        return new MyContextOp3(Op3.MOD);
    }
    
    @Override
    public MyContextOpRelacional visitOp_relacional(LaParser.Op_relacionalContext ctx) {
        OpRelacional op = null;
        
        switch (ctx.getText()) {
            case "=":
                op = OpRelacional.EQ;
                break;
            case "<>":
                op = OpRelacional.NE;
                break;
            case ">=":
                op = OpRelacional.BE;
                break;
            case "<=":
                op = OpRelacional.LE;
                break;
            case ">":
                op = OpRelacional.BT;
                break;
            case "<":
                op = OpRelacional.LT;
                break;
        }
        
        assert(op != null);
        
        return new MyContextOpRelacional(op);
    }
    
    @Override
    public MyContextOpLogico visitOp_logico_1(LaParser.Op_logico_1Context ctx) {
        return new MyContextOpLogico(OpLogico.OU);
    }
    
    @Override
    public MyContextOpLogico visitOp_logico_2(LaParser.Op_logico_2Context ctx) {
        return new MyContextOpLogico(OpLogico.E);
    }
    
    // Comparação de tipos
    // Para conseguir atribuir REAL <- INTEIRO e INTEIRO <- REAL
    private boolean match(Tipo t1, Tipo t2) {
        if (t1.getEhPonteiro() != t2.getEhPonteiro()) {
            return false;
        }
        
        if ((t1.getTipoBase() == TipoBase.REAL) && (t2.getTipoBase() == TipoBase.INTEIRO)) {
            return true;
        } else if ((t2.getTipoBase() == TipoBase.REAL) && (t1.getTipoBase() == TipoBase.INTEIRO)) {
            return true;
        }
        
        return t1.getTipoBase() == t2.getTipoBase();
    }
}