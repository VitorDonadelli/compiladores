package br.ufscar.dc.compiladores.la.semantico;

import br.ufscar.dc.compiladores.la.semantico.contexts.*;
import br.ufscar.dc.compiladores.la.semantico.tabelaDeSimbolos.*;

public class LaSemantico extends LaBaseVisitor<MyContext> {
    
    Escopos escopos = new Escopos();
    
    // Cria os tipos base
    Tipo literal = new Tipo("literal");
    Tipo inteiro = new Tipo("inteiro");
    Tipo real    = new Tipo("real");
    Tipo logico  = new Tipo("logico");
    Tipo unknown = new Tipo("");
    
    @Override
    public MyContext visitPrograma(LaParser.ProgramaContext ctx) {
       
        // Adiciona escopo global
        escopos.novo();
        
        // Adiciona os tipos base
        escopos.insereTipoGlobal("literal", literal);
        escopos.insereTipoGlobal("inteiro", inteiro);
        escopos.insereTipoGlobal("real",    real);
        escopos.insereTipoGlobal("logico",  logico);
        
        return visitChildren(ctx);
    }
    
    @Override public MyContext visitDeclaracao_local(LaParser.Declaracao_localContext ctx) {
        // Recupera os tokens
        var variavel = ctx.variavel();
        var identConstante = ctx.identConstante;
        var tipoBasico = ctx.tipo_basico();
        var valorConstante = ctx.valor_constante();
        var identTipo = ctx.identTipo;
        var tipo = ctx.tipo();
        
        if (variavel != null) {
            // Caso seja variavel
            return visitVariavel(variavel);
        } else if (identConstante != null && tipoBasico != null && valorConstante != null) {
            // Caso seja constante
            
            var nome = identConstante.getText();
            var tipoBasicoMyCtx = (MyContextTipoBasico)visitTipo_basico(tipoBasico);
            var valorConstanteMyCtx = (MyContextValorConstante)visitValor_constante(valorConstante);
            
            // Verifica se o nome existe no escopo
            if (escopos.acha(nome)) {
                LaSemanticoUtils.addErrIdentificadorJaDeclarado(ctx.start, nome);
            }
            
            // Verifica se os tipos são equivalentes
            if (!match(tipoBasicoMyCtx.getType(), valorConstanteMyCtx.getType())) {
                LaSemanticoUtils.addErrAtribuicaoNaoCompativel(valorConstante.start, nome);
            }
            
            // Insere no escopo local
            escopos.insereVariavelLocal(nome, new Variavel(valorConstanteMyCtx.getType()));
            
            return new MyContext();
        } else if (identTipo != null && tipo != null) {
            // Caso seja tipo
            
            var nome = identTipo.getText();
            // var tipoMyCtx = (MyContextTipo)visitTipo(tipo);
            
            // Verifica se o nome existe
            if (escopos.acha(nome)) {
                LaSemanticoUtils.addErrIdentificadorJaDeclarado(ctx.start, nome);
            }
            
            // Coloca o tipo no escopo local
            escopos.insereTipoLocal(nome, new Tipo(nome));
            
            return new MyContext();
        }
        
        return null;
    }

    @Override
    public MyContext visitVariavel(LaParser.VariavelContext ctx) {
        // Recupera os tokens
        var tipo = ctx.tipo();
        var nomeTipo = tipo.getText();
        
        Tipo tipo_ = unknown;
        var erroTipo = false;
        
        // Verifica se o tipo ja existe no escopo
        if (!escopos.acha(nomeTipo)) {
            erroTipo = true;
        } else {
            // Coloca o tipo no escopo local
            tipo_ = new Tipo(nomeTipo);
            escopos.insereTipoLocal(nomeTipo, tipo_);
        }
        
        for (var identificador : ctx.identificador()) {
            var nome = identificador.getText();
            
            // Verifica se o nome existe
            if (escopos.achaLocal(nome)) {
                LaSemanticoUtils.addErrIdentificadorJaDeclarado(identificador.start, nome);
            } else {
                // Coloca a variavel no escopo local
                escopos.insereVariavelLocal(nome, new Variavel(tipo_.getName()));
            }
        }
        
        // Adiciona erro de tipo
        if (erroTipo) {
            LaSemanticoUtils.addErrTipoNaoDeclarado(tipo.start, nomeTipo);
        }
        
        return visitChildren(ctx);
    }
    
    @Override
    public MyContext visitTipo_estendido(LaParser.Tipo_estendidoContext ctx) {
        // Recupera o token e visita recursivamente
        var tipoBasicoIdent = ctx.tipo_basico_ident();
        var tipoBasicoIdentMyCtx = (MyContextTipoBasicoIdent)visitTipo_basico_ident(tipoBasicoIdent);
        
        return new MyContextTipoExtendido(tipoBasicoIdentMyCtx.getType());
    }
    
    @Override
    public MyContext visitTipo_basico_ident(LaParser.Tipo_basico_identContext ctx) {
        // Recupera os tokens
        var tipoBasico = ctx.tipo_basico();
        var ident = ctx.IDENT();
        
        if (tipoBasico != null) {
            // Tipo basico
            var tipoBasicoMyCtx = (MyContextTipoBasico)visitTipo_basico(tipoBasico);
            
            return new MyContextTipoBasicoIdent(tipoBasicoMyCtx.getType());
        } else if (ident != null) {
            // Não é tipo basico
            var nome = ident.getText();
            
            return new MyContextTipoBasicoIdent(nome);
        }
        
        return null;
    }
    
    @Override 
    public MyContext visitRegistro(LaParser.RegistroContext ctx) {
        // Adiciona escopo do registro
        escopos.novo();
        
        // Visita as declarações do registro
        var ret = visitChildren(ctx);
        
        //Abandona escopo do registro
        escopos.abandonar();
        
        return ret;
    }
    
    @Override 
    public MyContext visitDeclaracao_global(LaParser.Declaracao_globalContext ctx) {
         // Recupera os tokens
        var ident = ctx.IDENT();
        var nome = ident.getText();
        
        if (escopos.achaLocal(nome)) {
            LaSemanticoUtils.addErrIdentificadorJaDeclarado(ctx.start, nome);
        } else {
            if (ctx.tipo_estendido() == null) {
                // Procedimento
                escopos.insereProcedimentoLocal(nome, new Procedimento());
            } else {
                // Função
                var tipoEstendido = ctx.tipo_estendido();
                var tipoEstendidoMyCtx = (MyContextTipoExtendido)visitTipo_estendido(tipoEstendido);
                
                escopos.insereFuncaoLocal(nome, new Funcao(tipoEstendidoMyCtx.getType()));
            }
        }
        
        // Cria escopo do procedimento/função  
        escopos.novo();
        
        // Visita o escopo do procedimento/função
        var ret = visitChildren(ctx);
        
        // Abandona escopo do procedimento/função
        escopos.abandonar();
        
        return ret;
    }
    
    @Override 
    public MyContext visitParametro(LaParser.ParametroContext ctx) {
         // Recupera os tokens
        var tipo_estendido = ctx.tipo_estendido();
        var nomeTipo = tipo_estendido.getText();
        
        Tipo tipo = unknown;
        var erroTipo = false;
        
        // Verifica se o tipo ja existe no escopo
        if (!escopos.acha(nomeTipo)) {
            erroTipo = true;
        } else {
            // Coloca o tipo no escopo local
            tipo = new Tipo(nomeTipo);
            escopos.insereTipoLocal(nomeTipo, tipo);
        }
        
        for (var identificador : ctx.identificador()) {
            var nome = identificador.getText();
            
            // Verifica se o nome existe
            if (escopos.achaLocal(nome)) {
                LaSemanticoUtils.addErrIdentificadorJaDeclarado(identificador.start, nome);
            } else {
                // Caso não, insere uma variavel no escopo local
                escopos.insereVariavelLocal(nome, new Variavel(tipo.getName()));
            }
        }
        
        // Adiciona erro de tipo
        if (erroTipo) {
            LaSemanticoUtils.addErrTipoNaoDeclarado(tipo_estendido.start, nomeTipo);
        }
        
        return visitChildren(ctx);
    }
    
    @Override 
    public MyContext visitCmdLeia(LaParser.CmdLeiaContext ctx) {
        // Recupera o token e verifica se existe no escopo local
        for (var identificador : ctx.identificador()) {
            var nome = identificador.getText();
            
            if (!escopos.achaLocal(nome)) {
                LaSemanticoUtils.addErrIdentificadorNaoDeclarado(identificador.start, nome);
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
        var identificador = ctx.identificador();
        var nome = identificador.getText();
        
        //  Verifica se o nome existe no escopo
        if (!escopos.achaLocal(nome)) {
            LaSemanticoUtils.addErrIdentificadorNaoDeclarado(identificador.start, nome);
        }
        
        // Recupera a variavel do escopo
        var variavel = escopos.pegaVariavel(nome);
        
        var exp = ctx.expressao();
        var expMyCtx = (MyContextExpressao)visitExpressao(exp);
        
        // Verifica se os tipos são equivalentes
        if (!match(variavel.getType(), expMyCtx.getType())) {
            LaSemanticoUtils.addErrAtribuicaoNaoCompativel(identificador.start, nome);
        }
        
        return visitChildren(ctx);
    }
    
    @Override public MyContext visitCmdChamada(LaParser.CmdChamadaContext ctx) {
        // Recupera o token
        var nome = ctx.IDENT().getText();
        
        // Verifical se nome ja existe no escopo local
        if (!escopos.achaLocal(nome)) {
            LaSemanticoUtils.addErrIdentificadorNaoDeclarado(ctx.start, nome);
        }
        
        return visitChildren(ctx);
    }
    
    @Override
    public MyContext visitExpressao(LaParser.ExpressaoContext ctx) {
        // Recupera o token 
        var ops = ctx.op_logico_1();
        
        if (ops == null || ops.isEmpty()) {
            // Não tem operador
            var termo = ctx.termo_logico(0);
            var termoMyCtx = (MyContextTermoLogico)visitTermo_logico(termo);
            
            return new MyContextExpressao(termoMyCtx.getType());
        }

        var termos = ctx.termo_logico();
        
        // Visita os termos recursivamente
        for (var termo : termos) {
            visitTermo_logico(termo);
        }
        
        return new MyContextExpressao(logico.getName());
    }
    
    @Override
    public MyContext visitTermo_logico(LaParser.Termo_logicoContext ctx) {
        // Recupera o token 
        var ops = ctx.op_logico_2();
        
        if (ops == null || ops.isEmpty()) {
            // Não tem operador
            var fator = ctx.fator_logico(0);
            var fatorMyCtx = (MyContextFatorLogico)visitFator_logico(fator);
            
            return new MyContextTermoLogico(fatorMyCtx.getType());
        }
        
        var fatores = ctx.fator_logico();
        
        // Visita os fatores recursivamente
        for (var fator : fatores) {
            visitFator_logico(fator);
        }
        
        return new MyContextTermoLogico(logico.getName());
    }
    
    @Override
    public MyContext visitFator_logico(LaParser.Fator_logicoContext ctx) {
        // Recupera os tokens
        var parcela = ctx.parcela_logica();
        var parcelaMyCtx = (MyContextParcelaLogica)visitParcela_logica(parcela);
        
        // Colocar logica para o não
        
        return new MyContextFatorLogico(parcelaMyCtx.getType());
    }
    
    @Override
    public MyContext visitParcela_logica(LaParser.Parcela_logicaContext ctx) {
        // Recupera o token
        var exp = ctx.exp_relacional();
        
        if (exp == null) {
            // Não tem expressão
            return new MyContextParcelaLogica(logico.getName());
        }
        
        // Visita expressão relacional
        var expRelacionalMyCtx = (MyContextExpRelacional)visitExp_relacional(exp);
        
        return new MyContextParcelaLogica(expRelacionalMyCtx.getType());
    }
    
    @Override
    public MyContext visitExp_relacional(LaParser.Exp_relacionalContext ctx) {
        // Recupera o token
        var ops = ctx.op_relacional();
        
        if (ops == null || ops.isEmpty()) {
            // Não tem operador
            var expAri = ctx.exp_aritmetica(0);
            var expAriMyCtx = (MyContextExpAritmetica)visitExp_aritmetica(expAri);
            
            return new MyContextExpRelacional(expAriMyCtx.getType());
        }
        
        var expsAri = ctx.exp_aritmetica();
        
        // Visita as expressoes aritmeticas recursivamente
        for (var expAri : expsAri) {
            visitExp_aritmetica(expAri);
        }
        
        return new MyContextExpRelacional(logico.getName());
    }

    @Override
    public MyContext visitExp_aritmetica(LaParser.Exp_aritmeticaContext ctx) {
        // Recupera o token
        var termos = ctx.termo();
        
        MyContextTermo termoMyCtx = null;
        
        // Visita os termos recursivamente
        for (var termo : termos) {
            termoMyCtx = (MyContextTermo)visitTermo(termo);
        }

        return new MyContextExpAritmetica(termoMyCtx.getType());
    }
    
    @Override
    public MyContext visitTermo(LaParser.TermoContext ctx) {
        // Recupera o token
        var fatores = ctx.fator();
        MyContextFator fatorMyCtx = null;
                
        // Visita os fatores recursivamente
        for (var fator : fatores) {
            fatorMyCtx = (MyContextFator)visitFator(fator);
        }    

        return new MyContextTermo(fatorMyCtx.getType());
    }
    
    @Override
    public MyContext visitFator(LaParser.FatorContext ctx) {
        // Recupera o token
        var parcelas = ctx.parcela();
        MyContextParcela parcelaMyCtx = null;
                
        // Visita as parcelas recursivamente
        for (var parcela : parcelas) {
            parcelaMyCtx = (MyContextParcela)visitParcela(parcela);
        }

        return new MyContextFator(parcelaMyCtx.getType());
    }
    
    @Override
    public MyContext visitParcela(LaParser.ParcelaContext ctx) {
        // Recupera os tokens
        var parcelaUnario = ctx.parcela_unario();
        var parcelaNaoUnario = ctx.parcela_nao_unario();
        
        if (parcelaUnario != null) {
            // Parcela unaria
            var parcelaUnarioMyCtx = (MyContextParcelaUnario)visitParcela_unario(parcelaUnario);
            return new MyContextParcela(parcelaUnarioMyCtx.getType());
        } else {
            // Parcela não unaria
            var parcelaNaoUnarioMyCtx = (MyContextParcelaNaoUnario)visitParcela_nao_unario(parcelaNaoUnario);
            return new MyContextParcela(parcelaNaoUnarioMyCtx.getType());
        }
    }
    
    @Override
    public MyContext visitParcela_unario(LaParser.Parcela_unarioContext ctx) {
        // Recupera os tokens
        var identificador = ctx.identificador();
        var ident = ctx.IDENT();
        var numInt = ctx.NUM_INT();
        var numReal = ctx.NUM_REAL();
        var parcelaUnarioExp = ctx.parcelaUnarioExp;
        
        if (identificador != null) {
            // Variavel
            var nome = identificador.getText();
        
            // Verifica se o nome existe no escopo
            if (!escopos.achaLocal(nome)) {
                LaSemanticoUtils.addErrIdentificadorNaoDeclarado(identificador.start, nome);
                
                return new MyContextParcelaUnario(unknown.getName());
            }
            
            var variavel = escopos.pegaVariavel(nome);
            
            return new MyContextParcelaUnario(variavel.getType());
        } else if (ident != null) {
            // Função
            var nome = ident.getText();
        
            // Verifica se a função existe no escopo
            if (!escopos.achaFuncaoLocal(nome)) {
                LaSemanticoUtils.addErrIdentificadorNaoDeclarado(ctx.start, nome);
                
                return new MyContextParcelaUnario(unknown.getName());
            }
            
            var funcao = escopos.pegaFuncao(nome);
            
            if (funcao != null) {
                return new MyContextParcelaUnario(funcao.getType());
            }
            
            return new MyContextParcelaUnario(unknown.getName());
        } else if (numInt != null) {
            // Inteiro
            return new MyContextParcelaUnario(inteiro.getName());
        } else if (numReal != null) {
            // Real
            return new MyContextParcelaUnario(real.getName());
        } else if (parcelaUnarioExp != null) {
            // Visita a expressao recursivamente
            var parcelaUnarioExpMyCtx = (MyContextExpressao)visitExpressao(parcelaUnarioExp);
            return new MyContextParcelaUnario(parcelaUnarioExpMyCtx.getType());
        }
        
        return null;
    }
    
    @Override
    public MyContext visitParcela_nao_unario(LaParser.Parcela_nao_unarioContext ctx) {
        // Recupera os tokens
        var identificador = ctx.identificador();
        var cadeia = ctx.CADEIA();
        
        if (identificador != null) {
            // Variavel
            var nome = identificador.getText();
        
            // Verifica se o nome existe no escopo local
            if (!escopos.achaLocal(nome)) {
                LaSemanticoUtils.addErrIdentificadorNaoDeclarado(identificador.start, nome);
                
                return new MyContextParcelaUnario(unknown.getName());
            }
            
            var variavel = escopos.pegaVariavel(nome);
            
            return new MyContextParcelaNaoUnario(variavel.getType());
        } else if (cadeia != null) {
            // String
            return new MyContextParcelaNaoUnario(literal.getName());
        }
        
        return null;
    }
    
    // Sem filhos
    // Retornam um MyContext que lhes representam
    
    @Override
    public MyContext visitTipo_basico(LaParser.Tipo_basicoContext ctx) {
        return new MyContextTipoBasico(ctx.getText());
    }
    
    @Override
    public MyContext visitOp_relacional(LaParser.Op_relacionalContext ctx) {
        return new MyContextOpRelacional(ctx.getText());
    }

    @Override
    public MyContext visitOp1(LaParser.Op1Context ctx) {
        return new MyContextOp1(ctx.getText());
    }
    
    @Override
    public MyContext visitOp2(LaParser.Op2Context ctx) {
        return new MyContextOp2(ctx.getText());
    }
    
    @Override
    public MyContext visitOp3(LaParser.Op3Context ctx) {
        return new MyContextOp3();
    }
    
    @Override
    public MyContext visitOp_logico_1(LaParser.Op_logico_1Context ctx) {
        return new MyContextOpLogico("ou");
    }
    
    @Override
    public MyContext visitOp_logico_2(LaParser.Op_logico_2Context ctx) {
        return new MyContextOpLogico("e");
    }
    
    @Override
    public MyContext visitOp_unario(LaParser.Op_unarioContext ctx) {
        return new MyContextOpUnario();
    }
    
    // Comparação de tipos
    // Para conseguir atribuir REAL <- INTEIRO e INTEIRO <- REAL
    private boolean match(String t1, String t2) {
        if (t1.equals("real") && (t2.equals("inteiro"))) {
            return true;
        } else if (t1.equals("inteiro") && (t2.equals("real"))) {
            return true;
        }
        
        return t1.equals(t2);
    }
}