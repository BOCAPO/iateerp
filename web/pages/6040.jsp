<%@taglib prefix="sql" uri="http://java.sun.com/jsp/jstl/sql"%>
<%@include file="head.jsp"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@taglib prefix="sql" uri="http://java.sun.com/jsp/jstl/sql"%>

<style type="text/css">
    table.fmt {border:none;width:0;padding:0px;margin-left:0em;margin-top:0em;}
    table.fmt tr td {border:none;background:none;padding:0px;margin:0em auto;}
</style>  

<script type="text/javascript" language="JavaScript" src="js/format.js"></script>

<script type="text/javascript" language="JavaScript">

    $(document).ready(function () {
            $('#tabelaMovimento tr:gt(0)').css('background', 'white');
            $('#tabelaCarne tr:gt(0)').css('background', 'white');
            $('#tabelaSocio tr:gt(0)').css('background', 'white');
            $('#tabelaCheque tr:gt(0)').css('background', 'white');
            $('#tabelaCheque tr:gt(0)').css('background', 'white');
            $('#tabelaOutrasFormas tr:gt(0)').css('background', 'white');

            $('#tabelaMovimento tr:gt(0)').hover(function() {
                    $(this).css('background','#f4f9fe');
            }, function() {
                    $(this).css('background','white');
            });
            $('#tabelaCarne tr:gt(0)').hover(function() {
                    $(this).css('background','#f4f9fe');
            }, function() {
                    $(this).css('background','white');
            });
            $('#tabelaSocio tr:gt(0)').hover(function() {
                    $(this).css('background','#f4f9fe');
            }, function() {
                    $(this).css('background','white');
            });
            $('#tabelaCheque tr:gt(0)').hover(function() {
                    $(this).css('background','#f4f9fe');
            }, function() {
                    $(this).css('background','white');
            });
            $('#tabelaOutrasFormas tr:gt(0)').hover(function() {
                    $(this).css('background','#f4f9fe');
            }, function() {
                    $(this).css('background','white');
            });
            
            $("#excluiCheque").live("click", excluiCheque);
            $("#excluiOutrasFormas").live("click", excluiOutrasFormas);
            
            $("#pesqSocio").hide();
            $("#pesqCarne").hide();
            $("#pesqFuncionario").hide();
            $("#pesqTaxaIndividual").hide();
            $("#pesqConviteEsportivo").hide();
            
            $("#campoObservacao").hide();
            $('#campoCheque').hide();
            $('#campoOutrasFormas').hide();
            
            $('#dtDeposito').mask('99/99/9999');
            
            $("#vrDevido").mouseup(function(e){e.preventDefault();});            
            $("#vrAcrescimo").mouseup(function(e){e.preventDefault();});            
            $("#vrDesconto").mouseup(function(e){e.preventDefault();});            
            $("#vrDinheiro").mouseup(function(e){e.preventDefault();});            
            $("#vrCheque").mouseup(function(e){e.preventDefault();});            
            $("#vrOutros").mouseup(function(e){e.preventDefault();});            
            $("#vrTotal").mouseup(function(e){e.preventDefault();});   
            
            $("#tabelaCheque tr:eq(1)").css('display', 'none');
            $("#tabelaOutrasFormas tr:eq(1)").css('display', 'none');
            
            $.format.locale({
                number: {
                groupingSeparator: '.',
                decimalSeparator: ','
                }
            });
            
            limpaGeral();
    });   
    
    
        
    function trocaTransacao(){
        if ($('#transacao').val()==$('#TranCarne').val()){
            $('#dtVencimento').val('');
        }else{
            $('#dtVencimento').val($('#dtAtual').val());
        }
        limpaGeral();
    }
    
    function limpaGeral(){
        
        $('#categoria').val('');
        $('#matricula').val('');
        $('#carteira').val('');
        $('#nome').val('');
        $('#vrDevido').val($.format.number( parseFloat($('#valTran'+$('#transacao').val()).val()), '#,##0.00'));
        
        //BLOQUEANDO O CAMPO DE VALOR DEVIDO PARA AS TRANSACOES DE CARNE E AQUELAS COM VALOR PADRAO
        if ($('#vrDevido').val()=="0,00" && $('#transacao').val()!=$('#TranCarne').val()){
            $('#vrDevido').removeAttr("readonly");
        }else{
            $('#vrDevido').attr("readonly", true);
        }
        
        $('#vrAcrescimo').val('0,00');
        $('#vrDesconto').val('0,00');
        $('#vrDinheiro').val('0,00');
        $('#vrCheque').val('0,00');
        $('#vrOutros').val('0,00');
        $('#vrTotal').val('0,00');
        
        $("textarea#observacao").val('');
        
        $('#tabelaMovimento').find('tbody').empty();
        
        $('#vrDevido').attr("checked", true);
        $('#operador').val('');
        $('#seqTaxaIndividual').val('');
        $('#nrConvite').val('');

    }
    
    function sair(){
        $('#app').val('6000');
        $('#acao').val('');
        document.forms[0].submit();
    }
    function cancelar(){
        $('#app').val('6040');
        $('#acao').val('');
        document.forms[0].submit();
    }
    
    function observacoes() {
        $("#campoObservacao").show();
    }        
    function cancelaObservacao() {
        $("textarea#observacao").val('');
        fechaObservacao();
    }        
    function fechaObservacao() {
        $("#campoObservacao").hide();
    }        
    function cancelaPesquisaSocio() {
        $("#pesqSocio").hide();
    }        
    function cancelaPesquisaCarne() {
        $("#pesqCarne").hide();
    }        
    function cancelaPesquisaFunc() {
        $("#pesqFuncionario").hide();
    }        
    function cancelaPesquisaTaxaIndividual() {
        $("#pesqTaxaIndividual").hide();
    }        
    function cancelaPesquisaConviteEsportivo() {
        $("#pesqConviteEsportivo").hide();
    }        

    function atualizaCheque(){
        $('#campoCheque').hide();
    }
    function atualizaOutrasFormas(){
        $('#campoOutrasFormas').hide();
    }
    function cheques(){
        
        if (parseFloat($('#vrCheque').val().replace('.', '').replace(',', '.')) == 0){
            alert('Informe o valor que será pago com Cheque!');
            return false;
        }
        
        if ($('#tabelaCheque tr').length==2){
            $('#banco').val('');
            $('#numCheque').val('');
            $('#serie').val('');
            $('#dtDeposito').val( $('#dtAtual').val() );
            $('#valorCheque').val( $('#vrCheque').val() );
        }
        
        $('#campoCheque').show();
    }
    function outrasFormas(){
        
        if (parseFloat($('#vrOutros').val().replace('.', '').replace(',', '.')) == 0){
            alert('Informe o valor que será pago com OutrasFormas de Pagamento!');
            return false;
        }
        
        if ($('#tabelaCheque tr').length==2){
            $('#valorOutrasFormas').val( $('#vrOutros').val() );
        }
        
        $('#campoOutrasFormas').show();
    }
    
    function incluiCheque(){
        //
        
        if ($('#banco').val()==''){
            alert('Informe o banco!');
            return false;
        }
        if ($('#numCheque').val()==''){
            alert('Informe o número do cheque!');
            return false;
        }
        if ($('#dtDeposito').val()==''){
            alert('Informe a Data de Depósito!');
            return false;
        }
        if ($('#valorCheque').val()==''){
            alert('Informe o valor do cheque!');
            return false;
        }
        if (parseFloat($('#valorCheque').val())==0){
            alert('Cheque com valor ZERO!');
            return false;
        }
        
        var msg="";
        $("#tabelaCheque tr:gt(1)").each(function(index){
            if(
            parseInt($(this).find('#tdBanco').html())==parseInt($('#banco').val()) &&
            parseInt($(this).find('#tdNumCheque').html())==parseInt($('#numCheque').val()) &&
            parseInt($(this).find('#tdSerie').html())==parseInt($('#serie').val())
            )
            {
                msg = 'Já existe este Cheque Cadastrado!';
            }
        });
        if(msg!=""){
            alert(msg);
            return false;
        }
        
        var vr1=0;
        $("#tabelaCheque tr:gt(1)").each(function(index){
            vr1 = vr1 + parseFloat($(this).find('#tdVrCheque').html().replace('.', '').replace(',', '.'));
        });
        vr1 = vr1 + parseFloat($('#valorCheque').val().replace('.', '').replace(',', '.'));
        
        if (vr1 > parseFloat($('#vrCheque').val().replace('.', '').replace(',', '.'))){
            alert('O somatório dos Valores em CHEQUE ultrapassa o valor informado');
            return false
        }
        
        var novoCampo = $("#tabelaCheque tr:last").clone();
        novoCampo.find("#tdBanco").html( $('#banco').val() );
        novoCampo.find("#tdNumCheque").html( $('#numCheque').val() );
        novoCampo.find("#tdSerie").html( $('#serie').val() );
        novoCampo.find("#tdDtDeposito").html( $('#dtDeposito').val() );
        novoCampo.find("#tdVrCheque").html( $.format.number(parseFloat($('#valorCheque').val().replace('.', '').replace(',', '.')), '#,##0.00') );
        novoCampo.css('display','');
        novoCampo.insertAfter("#tabelaCheque tr:last");
        
        $('#banco').val('');
        $('#numCheque').val('');
        $('#serie').val('');
        $('#dtDeposito').val('');
        $('#valorCheque').val('');
        
        if(vr1 == parseFloat($('#vrCheque').val().replace('.', '').replace(',', '.'))){
            $('#campoCheque').hide();
        }

    }
    
    function incluiOutrasFormas(){
        //
        
        if (parseFloat($('#valorOutrasFormas').val())==0){
            alert('Informe o valor!');
            return false;
        }
        
        var vr1=0;
        $("#tabelaOutrasFormas tr:gt(1)").each(function(index){
            vr1 = vr1 + parseFloat($(this).find('#tdVrForma').html().replace('.', '').replace(',', '.'));
        });
        vr1 = vr1 + parseFloat($('#valorOutrasFormas').val().replace('.', '').replace(',', '.'));
        
        if (vr1 > parseFloat($('#vrOutros').val().replace('.', '').replace(',', '.'))){
            alert('O somatório dos Valores ultrapassa o valor informado');
            return false
        }
        
        var novoCampo = $("#tabelaOutrasFormas tr:last").clone();
        novoCampo.find("#tdForma").html( $( "#formaPagamento option:selected" ).text());
        novoCampo.find("#tdIdForma").html( $( "#formaPagamento").val());
        novoCampo.find("#tdVrForma").html( $.format.number(parseFloat($('#valorOutrasFormas').val().replace('.', '').replace(',', '.')), '#,##0.00') );
        novoCampo.find("#tdDocForma").html( $( "#docOutrasFormas").val());
        novoCampo.css('display','');
        novoCampo.insertAfter("#tabelaOutrasFormas tr:last");
        
        $('#valorOutrasFormas').val('');
        $('#docOutrasFormas').val('');
        
        if(vr1 == parseFloat($('#vrOutros').val().replace('.', '').replace(',', '.'))){
            $('#campoOutrasFormas').hide();
        }

    }    
    
    function excluiCheque(){
        
        $(this).parent().parent().remove();
    }
    
    function excluiOutrasFormas(){
        
        $(this).parent().parent().remove();
    }

    function pesquisa() {
        if ($('#categoria').val()=="" && $('#matricula').val()=="" && $('#nome').val()=="" && $('#carteira').val()==""){
            alert('Informe corretamete o filtro para pesquisa');
            return false;
        }
        //só informou a categoria
        if ($('#nome').val()=="" && $('#carteira').val()=="" && ($('#categoria').val()!="" && $('#matricula').val()=="")){
            alert('Informe corretamete o filtro para pesquisa');
            return false;
        }
        //só informou a matricula
        if ($('#nome').val()=="" && $('#carteira').val()=="" && ($('#categoria').val()=="" && $('#matricula').val()!="")){
            alert('Informe corretamete o filtro para pesquisa');
            return false;
        }
        
        var vinculaTaxa = 'N';
        if ($('#cdTaxaTran'+$('#transacao').val()).val() > 0 ){
            if(confirm('Deseja vincular uma taxa individual a esta movimentação?')){
                vinculaTaxa = 'S';
            }
        }

        
        
        if ($('#transacao').val()==$('#TranCarne').val()){
            //pesquisar carne
            var tabela = $('#tabelaCarne').find('tbody').empty();
            $.ajax({url:'MovimentoCarneAjax', type:'GET',
                                data:{
                                tipo:1,
                                nome:$('#nome').val(),
                                matricula:$('#matricula').val(),
                                categoria:$('#categoria').val(),
                                carteira:$('#carteira').val()
                                }
            }).success(function(retorno){
                if (retorno.erro !== undefined){
                    alert(retorno.erro);
                }else {
                    $("#pesqCarne").show();

                    var linha="";
                    var campoHidden="";
                    var campoHidden2="";
                    $.each(retorno.jogador, function(i) {
                        campoHidden='<input type="hidden" id="campoSequencial"   value="'+this.sequencial+'"/>'
                        campoHidden2='<input type="hidden" id="campoPermitePag"  value="'+this.permite_pagamento+'"/>'
                        linha='<tr><td class="column1"><a href="#" id="tabNome" class="column1" onclick="selecionaCarne('+i+')">'+this.nome+'</a></td><td class="column1" align="center">'+this.carteira+' '+campoHidden+' '+campoHidden2+'</td><td class="column1" id="tabDtVenc" align="center">'+this.dtVencimento+'</td><td class="column1" align="right">'+this.valor+'</td><td class="column1" id="tabTitulo" align="center">'+this.titulo+'</td></tr>';
                        tabela.append(linha)
                    });
                }
            });
        }else if ($('#transacao').val()==$('#TranPreFunc').val()){
            //pesquisar funcionario
            var tabela = $('#tabelaFuncionario').find('tbody').empty();
            
            $.ajax({url:'MovimentoFuncionarioAjax', type:'GET',
                                data:{
                                nome:$('#nome').val(),
                                cdFuncionario:''
                                }
            }).success(function(retorno){
                if (retorno.erro !== undefined){
                    alert(retorno.erro);
                }else {
                    if (retorno.qtReg=='1'){ 
                        $.each(retorno.jogador, function(i) {
                            $('#nome').val(this.nome);
                            $('#matricula').val(this.idFuncionario);
                        });    
                    }else{
                        $("#pesqFuncionario").show();

                        var linha="";
                        var campoHidden="";
                        $.each(retorno.jogador, function(i) {
                            campoHidden='<input type="hidden" id="idFuncionario"   value="'+this.idFuncionario+'"/>'
                            linha='<tr><td class="column1"><a href="#" id="tabNome" class="column1" onclick="selecionaFuncionario('+i+')">'+this.nome+'</a></td><td class="column1" id="tabMatricula" align="center">'+this.matricula+' '+campoHidden+'</td><td class="column1">'+this.setor+'</td><td class="column1">'+this.cargo+'</td></tr>';
                            tabela.append(linha)
                        });
                    }
                    
                }
            });
        }else if ($('#transacao').val()==$('#cdTransConvEsp').val() || $('#transacao').val()==$('#cdTransConvEspMeio').val()){
            //pesquisar Convites Esportivo
            
            var tipo = "8C";
            if ($('#transacao').val()==$('#cdTransConvEspMeio').val()){
                tipo = "4C";
            }
            
            var tabela = $('#tabelaConviteEsportivo').find('tbody').empty();
            
            $.ajax({url:'MovimentoConviteEsportivoAjax', type:'GET',
                                data:{
                                tipo:1,
                                nome:$('#nome').val(),
                                matricula:$('#matricula').val(),
                                categoria:$('#categoria').val(),
                                carteira:$('#carteira').val(),
                                tipo:tipo
                                }
            }).success(function(retorno){
                if (retorno.erro !== undefined){
                    alert(retorno.erro);
                }else {
                    $("#pesqConviteEsportivo").show();

                    var linha="";
                    $.each(retorno.jogador, function(i) {
                        linha='<tr><td class="column1"><a href="#" id="tabConvidado" class="column1" onclick="selecionaConviteEsportivo('+i+')">'+this.convidado+'</a></td><td class="column1" id="tabSocio" align="left">'+this.socio+'</td><td class="column1">'+this.modalidade+'</td><td class="column1">'+this.validade+'</td><td  id="tabConvite" class="column1">'+this.convite+'</td></tr>';
                        tabela.append(linha)
                    });
                }
            });
        }else if ($('#cdTaxaTran'+$('#transacao').val()).val() > 0 && vinculaTaxa=='S' ){
            //pesquisar Taxas Individuais
            var tabela = $('#tabelaTaxaIndividual').find('tbody').empty();
            
            $.ajax({url:'MovimentoTaxaIndividualAjax', type:'GET',
                                data:{
                                tipo:1,
                                nome:$('#nome').val(),
                                matricula:$('#matricula').val(),
                                categoria:$('#categoria').val(),
                                carteira:$('#carteira').val(),
                                taxa:$('#cdTaxaTran'+$('#transacao').val()).val()
                                }
            }).success(function(retorno){
                
                if (retorno.erro !== undefined){
                    alert(retorno.erro);
                }else {
                    $("#pesqTaxaIndividual").show();
                    
                    var linha="";
                    var campoHidden2="";
                    var campoCheckBox = "";
                    $.each(retorno.jogador, function(i) {
                        campoHidden2='<input type="hidden" id="tituloChave"   value="'+this.tituloChave+'"/>'
                        campoCheckBox = '<td class="column1"><input type="checkbox" class="legendaCodigo MargemSuperior0" name="selTxInd" id="selTxInd" value="'+this.seqTaxa+'"></td>';
                        
                        linha='<tr>'+campoCheckBox+'<td class="column1" id="tabNome">'+this.nome+'</td><td class="column1" id="tabTaxa" align="left">'+this.taxa+'</td><td class="column1" id="tabValor" align="right">'+this.valor+'</td><td class="column1" align="center">'+this.dtGeracao+' '+campoHidden2+'</td><td class="column1" id="tabCobranca" align="center">'+this.cobranca+'</td></tr>';
                        tabela.append(linha)
                    });
                }
            });
        }else{
            //pesquisar socio
            var tabela = $('#tabelaSocio').find('tbody').empty();
            
            $.ajax({url:'MovimentoSocioAjax', type:'GET',
                                data:{
                                nome:$('#nome').val(),
                                matricula:$('#matricula').val(),
                                categoria:$('#categoria').val(),
                                carteira:$('#carteira').val()
                                }
            }).success(function(retorno){
                if (retorno.erro !== undefined){
                    alert(retorno.erro);
                }else {
                    
                    if (retorno.qtReg=='1'){ 
                        $.each(retorno.jogador, function(i) {
                            $('#nome').val(this.nome);
                            $('#categoria').val(this.categoria);
                            $('#matricula').val(this.matricula);
                            $('#carteira').val(this.carteira);
                        });    
                    }else{
                        $("#pesqSocio").show();

                        var linha="";
                        var campoHidden="";
                        var tpCadastro="";
                        var tpCategoria="";
                        $.each(retorno.jogador, function(i) {
                            if (this.dependente==0){
                                tpCadastro="TITULAR";
                            }else{
                                tpCadastro="DEPENDENTE";
                            }
                            if (this.tpCategoria=="SO"){
                                tpCategoria="SÓCIO";
                            }else{
                                tpCategoria="NÃO SÓCIO";
                            }

                            linha='<tr><td class="column1"><a href="#" id="tabNome" class="column1" onclick="selecionaSocio('+i+')">'+this.nome+'</a></td><td class="column1" id="tabTitulo" align="center">'+this.titulo+'</td><td class="column1">'+tpCadastro+'</td><td class="column1">'+this.descricao+'</td><td class="column1">'+tpCategoria+'</td><td id="tabCarteira" class="column1">'+this.carteira+'</td></tr>';
                            tabela.append(linha)
                        });
                    }
                    
                }
            });
        }
        return false;
    }
    
    function selecionaSocio(indice){
        $("#pesqSocio").hide();
        $("#tabelaSocio tr:eq("+(parseInt(indice)+1)+")").each(function(index){
            $('#nome').val($(this).find('#tabNome').html());
            $('#carteira').val($(this).find('#tabCarteira').html());
            $('#categoria').val($(this).find('#tabTitulo').html().substr(0, 2));
            $('#matricula').val($(this).find('#tabTitulo').html().substr(3, 4));
        });

    }
    
    function selecionaConviteEsportivo(indice){
        $("#pesqConviteEsportivo").hide();
        $("#tabelaConviteEsportivo tr:eq("+(parseInt(indice)+1)+")").each(function(index){
            $('#nome').val($(this).find('#tabConvidado').html());
            $('#nrConvite').val($(this).find('#tabConvite').html());
        });
    }
    
    function selecionaFuncionario(indice){
        $("#pesqFuncionario").hide();
        $("#tabelaFuncionario tr:eq("+(parseInt(indice)+1)+")").each(function(index){
            $('#nome').val($(this).find('#tabNome').html());
            $('#matricula').val($(this).find('#idFuncionario').val());
        });

    }
    
    function selecionaCarne(indice){
        
        $("#tabelaCarne tr:eq("+(parseInt(indice)+1)+")").each(function(index){
            if ($(this).find('#campoPermitePag').val()=='S') {
                alert('O Carne selecionado está bloqueado para pagamento!');
                return false;
            }
            
            var ret;
            $.ajax({url:'MovimentoAjax', type:'GET', async:false,
                                data:{
                                tipo:3,
                                categoria:$(this).find('#tabTitulo').html().substr(0, 2),
                                matricula:$(this).find('#tabTitulo').html().substr(3),
                                dtVenc:$(this).find('#tabDtVenc').html()
                                }
            }).success(function(retorno){
                ret=retorno;
            });
            
            
            var operador= '';
            if (ret > 1){
                if (confirm('Deseja acumular os meses anteriores em um único recibo?')){
                    operador= '<=';
                }else{
                    operador= '=';
                }
            }else{
                operador= '=';
            }

            $('#operador').val(operador);

            $.ajax({url:'MovimentoSocioAjax', type:'GET', async:false,
                                data:{
                                nome:'',
                                categoria:$(this).find('#tabTitulo').html().substr(0, 2),
                                matricula:$(this).find('#tabTitulo').html().substr(3),
                                carteira:''
                                }
            }).success(function(retorno){
                if (retorno.erro !== undefined){
                    alert(retorno.erro);
                }else {
                    $.each(retorno.jogador, function(i) {
                        $('#nome').val(this.nome);
                        $('#categoria').val(this.categoria);
                        $('#matricula').val(this.matricula);
                        $('#carteira').val(this.carteira);
                    });    
                }
            });


            $.ajax({url:'MovimentoCarneAjax', type:'GET', async:false,
                                data:{
                                tipo:2,
                                operador:operador,
                                categoria:$(this).find('#tabTitulo').html().substr(0, 2),
                                matricula:$(this).find('#tabTitulo').html().substr(3),
                                dtVenc:$(this).find('#tabDtVenc').html()
                                }
            }).success(function(retorno){
                ret=retorno;
            });
            
            if (ret.temProibido=='S'){
                alert('Existem carnês que não foram agrupados pois estão bloqueados para pagamento!');
            }

            var tabela = $('#tabelaMovimento').find('tbody').empty();

            $.each(ret.relacao, function(i) {
                linha='<tr><td class="column1" id=tdDetalhe align="left">'+this.detalhe+'</td><td class="column1" id=tdValor align="right">'+this.valor+'</td></tr>';
                tabela.append(linha)
            });

            $('#vrDevido').val(ret.vrDevido);
            $('#vrAcrescimo').val(ret.vrEncargos);
            $('#dtVencimento').val($(this).find('#tabDtVenc').html());
            
            atualizaValores('vrDevido');   
            
            $("#pesqCarne").hide();
        });
    }
    
    function selecionaTaxaIndividual(indice){
        var matricula = "";
        var categoria = "";
        
        $('input[name="selTxInd"]:checked').each(function(){
            categoria = $(this).parent().parent().find('#tituloChave').val().substr(0, 2);
            matricula = $(this).parent().parent().find('#tituloChave').val().substr(2);
        });
        
        
        if ($('input[name="selTxInd"]:checked').length==0){
            alert('Nenhuma Taxa foi selecionada.');
            return;
        }

        $.ajax({url:'MovimentoSocioAjax', type:'GET', async:false,
                            data:{
                            nome:'',
                            categoria:categoria,
                            matricula:matricula,
                            carteira:''
                            }
        }).success(function(retorno){
            if (retorno.erro !== undefined){
                alert(retorno.erro);
            }else {
                $.each(retorno.jogador, function(i) {
                    $('#nome').val(this.nome);
                    $('#categoria').val(this.categoria);
                    $('#matricula').val(this.matricula);
                    $('#carteira').val(this.carteira);
                });    
            }
        });
        
        var linha = '';
        var tabela = $('#tabelaMovimento').find('tbody').empty();
        var vrDevido = parseFloat(0);
        var vrDevidoTabela = parseFloat(0);
        var vrEncargos = parseFloat(0);
        var seqTaxaIndividual = 0;
        $('#seqTaxaIndividual').val('');
        
        $("#pesqTaxaIndividual").hide();
        $('input[name="selTxInd"]:checked').each(function(){

            linha='<tr><td class="column1" id=tdDetalhe align="left">'+$(this).parent().parent().find('#tabNome').html()+'</td><td class="column1" id=tdValor align="right"></td></tr>';
            tabela.append(linha)
            linha='<tr><td class="column1" id=tdDetalhe align="left">&nbsp;&nbsp;&nbsp;'+$(this).parent().parent().find('#tabTaxa').html()+'</td><td class="column1" id=tdValor align="right">'+$(this).parent().parent().find('#tabValor').html()+'</td></tr>';
            tabela.append(linha)
            seqTaxaIndividual = $(this).val();
            vrDevidoTabela = $(this).parent().parent().find('#tabValor').html().replace('.', '').replace(',', '.');
            
            $.ajax({url:'MovimentoTaxaIndividualAjax', type:'GET', async:false,
                                data:{  
                                tipo:2,
                                seqTaxaIndividual:seqTaxaIndividual
                                }
            }).success(function(retorno){
                if (retorno.temCarne == 'S'){
                    vrEncargos = parseFloat(vrEncargos) + parseFloat(retorno.vrEncargos.replace('.', '').replace(',', '.'));
                    vrDevido = parseFloat(vrDevido) + parseFloat(retorno.vrDevido.replace('.', '').replace(',', '.'));
                }else{
                    vrDevido = parseFloat(vrDevido) + parseFloat(vrDevidoTabela);
                }
            }); 
            
            $('#seqTaxaIndividual').val($('#seqTaxaIndividual').val()+$(this).val()+',');
        });

        $('#vrAcrescimo').val($.format.number(vrEncargos, '#,##0.00'));
        $('#vrDevido').val($.format.number(vrDevido, '#,##0.00'));

        atualizaValores('vrDevido');   
        //alert($('#seqTaxaIndividual').val());
    }
    
    function atualizaValores(campo){
        
        var vrDesconto = parseFloat($('#vrDesconto').val().replace('.', '').replace(',', '.'));
        var vrDinheiro = parseFloat($('#vrDinheiro').val().replace('.', '').replace(',', '.'));
        var vrCheque = parseFloat($('#vrCheque').val().replace('.', '').replace(',', '.'));
        var vrOutros = parseFloat($('#vrOutros').val().replace('.', '').replace(',', '.'));
        var vrDevido = parseFloat($('#vrDevido').val().replace('.', '').replace(',', '.'));
        var vrAcrescimo = parseFloat($('#vrAcrescimo').val().replace('.', '').replace(',', '.'));
        
        var vrTotal = vrDevido + vrAcrescimo - vrDesconto;
        
        if (campo == 'vrDinheiro'){
            if (vrCheque > vrTotal){
                vrCheque = vrTotal;
            }
            vrDinheiro = vrDevido + vrAcrescimo - vrDesconto - vrCheque - vrOutros;
        }else if (campo == 'vrCheque'){
            if (vrDinheiro > vrTotal){
                vrDinheiro = vrTotal;
            }
            vrCheque = vrDevido + vrAcrescimo - vrDesconto - vrDinheiro - vrOutros;
            
        }else if (campo == 'vrOutros'){
            if (vrOutros > vrTotal){
                vrOutros = vrTotal;
            }
            vrOutros = vrDevido + vrAcrescimo - vrDesconto - vrDinheiro - vrCheque;
        }   
        
        $('#vrDesconto').val($.format.number(vrDesconto, '#,##0.00'))
        $('#vrDinheiro').val($.format.number(vrDinheiro, '#,##0.00'));
        $('#vrCheque').val($.format.number(vrCheque, '#,##0.00'));
        $('#vrOutros').val($.format.number(vrOutros, '#,##0.00'));
        $('#vrDevido').val($.format.number(vrDevido, '#,##0.00'));
        $('#vrAcrescimo').val($.format.number(vrAcrescimo, '#,##0.00'));
        $('#vrTotal').val($.format.number(vrTotal, '#,##0.00'));
        
        $('#'+campo).select();
        
    }
    
    function escondeCampos(){
        $('#cmdCheques').hide();
        $('#cmdSair').hide();
        $('#cmdCancelar').hide();
        $('#cmdAtualizar').hide();
        $('#cmdObservacoes').hide();
    }
    
    function mostraCampos(){
        $('#cmdCheques').show();
        $('#cmdSair').show();
        $('#cmdCancelar').show();
        $('#cmdAtualizar').show();
        $('#cmdObservacoes').show();
    }
    
    function atualizar(){
        escondeCampos();
        
        var vrDesconto = parseFloat($('#vrDesconto').val().replace('.', '').replace(',', '.'));
        var vrDinheiro = parseFloat($('#vrDinheiro').val().replace('.', '').replace(',', '.'));
        var vrCheque = parseFloat($('#vrCheque').val().replace('.', '').replace(',', '.'));
        var vrOutros = parseFloat($('#vrOutros').val().replace('.', '').replace(',', '.'));
        var vrDevido = parseFloat($('#vrDevido').val().replace('.', '').replace(',', '.'));
        var vrAcrescimo = parseFloat($('#vrAcrescimo').val().replace('.', '').replace(',', '.'));
        
        var vrTotal = vrDevido + vrAcrescimo - vrDesconto;
        
        var vr1=0;
        var qtCheque=0;
        $("#tabelaCheque tr:gt(1)").each(function(index){
            qtCheque++;
            vr1 = vr1 + parseFloat($(this).find('#tdVrCheque').html().replace('.', '').replace(',', '.'));
        });
        
        if ($.format.number( vrCheque, '#,##0.00') != $.format.number( vr1, '#,##0.00')){
            alert('A Soma dos cheques não confere com o valor informado!');
            mostraCampos();
            return false;
        }
        
        var vr2=0;
        var qtOutros=0;
        $("#tabelaOutrasFormas tr:gt(1)").each(function(index){
            qtOutros++;
            vr2 = vr2 + parseFloat($(this).find('#tdVrForma').html().replace('.', '').replace(',', '.'));
        });
        
        if ($.format.number( vrOutros, '#,##0.00') != $.format.number( vr2, '#,##0.00')){
            alert('A Soma dos Outras Formas de pagamento s não confere com o valor informado!');
            mostraCampos();
            return false;
        }
		
        if ($.format.number(vrDinheiro + vrCheque + vrOutros, '#,##0.00') != $.format.number( vrTotal, '#,##0.00')){
            alert('A Soma dos valores não confere!');
            mostraCampos();
            return false;
        }

        if (vrDevido == 0){
            alert('Não é possível receber valor ZERO!');
            mostraCampos();
            return false;
        }
        
        if ($('#controleParcPre').attr('checked') == 'checked' && qtCheque>0){
            if ($('#categoria').val()=="" && $('#matricula').val()==""){
                alert('Foi selecionado o controle de parcelamento/pré-datado do cheque, porém não foi selecionado o sócio para o controle. Favor informar o sócio responsável pelo(s) cheque(s).');
                mostraCampos();
                return false;
            }
            
            $.ajax({url:'MovimentoSocioAjax', type:'GET', async:false,
                                data:{
                                nome:'',
                                matricula:$('#matricula').val(),
                                categoria:$('#categoria').val(),
                                carteira:''
                                }
            }).success(function(retorno){
                if (retorno.erro !== undefined){
                    alert(retorno.erro);
                }else {
                    $.each(retorno.jogador, function(i) {
                        $('#nome').val(this.nome);
                        $('#categoria').val(this.categoria);
                        $('#matricula').val(this.matricula);
                        $('#carteira').val(this.carteira);
                    });    
                }
            });
            
            if ($('#nome').val()==''){
                alert('Sócio não encontrado para controlar o parcelamento/pré-datado do(s) cheques!');
                mostraCampos();
                return false;
            }
            
            
            if (!confirm('Confirma o controle de parcelamento/pré-datado dos cheque para o título '+ $('#categoria').val() + '/' + $('#matricula').val() + ' - ' + $('#nome').val() + '?')){
                mostraCampos();
                return false;
            }
            
            
            var ret; 
            $.ajax({url:'MovimentoAjax', type:'GET', async:false,
                                data:{
                                tipo:1,
                                matricula:$('#matricula').val(),
                                categoria:$('#categoria').val(),
                                valor:vrCheque,
                                origem:'6040',
                                nome:$('#nome').val()
                                
                                }
            }).success(function(retorno){
                ret=retorno;
            });
            
            if (ret == 'PROBLEMA'){
                if ($('#app6187').val()=='true'){
                    if (!confirm('O valor do Cheque vai ultrapassar o limite máximo estipulado para Taxas Individuais e Cheques Pré-Datados. Deseja continuar com a transação?')){
                        mostraCampos();
                        return false;
                    }
                }else{
                    alert('O valor do Cheque vai ultrapassar o limite máximo estipulado para Taxas Individuais e Cheques Pré-Datados!');
                    mostraCampos();
                    return false;
                }
            }
            
            $.ajax({url:'MovimentoAjax', type:'GET', async:false,
                                data:{
                                tipo:2,
                                matricula:$('#matricula').val(),
                                categoria:$('#categoria').val()
                                }
            }).success(function(retorno){
                ret=retorno;
            });
            
            if (ret == 'JATEM'){
                if ($('#app6041').val()=='true'){
                    if (!confirm('Já existe um parcelamento ou cheque pré-datado que ainda não atingiu a data de depósito. Deseja continuar com outro parcelamento?')){
                        mostraCampos();
                        return false;
                    }
                }else{
                    alert('Já existe um parcelamento ou cheque pré-datado que ainda não atingiu a data de depósito, impossibilitando a criação de um novo parcelamento!');
                    mostraCampos();
                    return false;
                }
            }
            
            
        }
        
        if ($('#transacao').val()==$('#TranCarne').val()){
            if ($('#dtVencimento').val==''){
                alert('Necessário proceder a busca do carne pelo sistema.');
                mostraCampos();
                return false
            }
        }
        
        //SOCIO (PRE-PAGO)
        if ($('#transacao').val()==$('#TranPreSocio').val()){
            if ($('#categoria').val()=="" && $('#matricula').val()==""){
                alert('Foi selecionado o lançamento de crédito pré-pago, porém não foi selecionado o sócio. Favor informar o sócio que receberá o crédito.');
                mostraCampos();
                return false;
            }
            
            $.ajax({url:'MovimentoSocioAjax', type:'GET', async:false,
                                data:{
                                nome:'',
                                matricula:$('#matricula').val(),
                                categoria:$('#categoria').val(),
                                carteira:''
                                }
            }).success(function(retorno){
                if (retorno.erro !== undefined){
                    alert(retorno.erro);
                }else {
                    $.each(retorno.jogador, function(i) {
                        $('#nome').val(this.nome);
                        $('#categoria').val(this.categoria);
                        $('#matricula').val(this.matricula);
                        $('#carteira').val(this.carteira);
                    });    
                }
            });
            
            if ($('#nome').val()==''){
                alert('Sócio não encontrado para receber crédito pré-pago!');
                mostraCampos();
                return false;
            }
            
            
            if (!confirm('Confirma o lançamento de crédito pré-pago para o título '+ $('#categoria').val() + '/' + $('#matricula').val() + ' - ' + $('#nome').val() + '?')){
                mostraCampos();
                return false;
            }
            
        }
        //FIM SOCIO
        
        //FUNCIONARIO (PRE-PAGO)
        if ($('#transacao').val()==$('#TranPreFunc').val()){
            if ($('#nome').val()==""){
                alert('Foi selecionado o lançamento de crédito pré-pago, porém não foi selecionado o funcionário. Favor informar o funcionário que receberá o crédito.');
                mostraCampos();
                return false;
            }
            
            var erro = "";
            $.ajax({url:'MovimentoFuncionarioAjax', type:'GET', async:false,
                                data:{
                                nome:$('#nome').val(),
                                cdFuncionario:$('#matricula').val()
                                }
            }).success(function(retorno){
                if (retorno.erro !== undefined){
                    alert(retorno.erro);
                }else {
                    if (retorno.qtReg=='1'){ 
                        $.each(retorno.jogador, function(i) {
                            $('#nome').val(this.nome);
                            $('#matricula').val(this.idFuncionario);
                        });    
                    }else{
                        erro = "ERRO";
                    }
                }
            });

            if (erro!=""){
                alert('Funcionário não encontrado para receber crédito pré-pago!');
                mostraCampos();
                return false;
            }
        
            if (!confirm('Confirma o lançamento de crédito pré-pago para o Funcionario '+ $('#nome').val() + '?')){
                mostraCampos();
                return false;
            }
            
        }
        //FIM FUNCIONARIO
        
        if ($('#transacao').val()==$('#cdTransConvEsp').val()){
            if ($('#nrConvite').val()==''){
                alert('Foi selecionado o pagamento de Convite Esportivo, porém não foi selecionado o convite. Favor selecionar o convite que será pago.');
                mostraCampos();
                return false;
            }
        }
        
        // não eh carne nem carga de pre pago entao tem que preencher pelo menos o nome
        if ($('#transacao').val()!=$('#TranCarne').val() && $('#transacao').val()!=$('#TranPreSocio').val() && $('#transacao').val()!=$('#TranPreFunc').val()){
            if ($('#nome').val()==''){
                alert('O Nome deve ser informado!');
                mostraCampos();
                return false;
            }
        }
        
        
        var cheque = '';
        $("#tabelaCheque tr:gt(1)").each(function(index){
            cheque = cheque + $(this).find('#tdBanco').html() + '#';
            cheque = cheque + $(this).find('#tdNumCheque').html() + '#';
            cheque = cheque + $(this).find('#tdSerie').html() + '#';
            cheque = cheque + $(this).find('#tdDtDeposito').html() + '#';
            cheque = cheque + $(this).find('#tdVrCheque').html().replace('.', '').replace(',', '.') + ';';
        });
        $('#chq').val(cheque);
        
        var outrasFormas = '';
        $("#tabelaOutrasFormas tr:gt(1)").each(function(index){
            outrasFormas = outrasFormas + $(this).find('#tdIdForma').html() + '#';
            outrasFormas = outrasFormas + $(this).find('#tdVrForma').html().replace('.', '').replace(',', '.') + '#';
            outrasFormas = outrasFormas + $(this).find('#tdDocForma').html() + ' #;';
        });
        
        $('#formas').val(outrasFormas);
        
        var detCarne = '';
        var tdDetalhe = '';
        $("#tabelaMovimento tr:gt(0)").each(function(index){
            tdDetalhe = $(this).find('#tdDetalhe').html().replace(/&nbsp;/g, ' ').replace(/;/g, '');
            detCarne = detCarne + tdDetalhe + '#';
            
            detCarne = detCarne + $(this).find('#tdValor').html() + ' ;';
        });
        
        $('#detCarne').val(detCarne);
        
        //alert('vai submit');
        //mostraCampos();
        document.forms[0].submit();
        
    }
    
</script>  


<body class="internas">

    <script type="text/javascript" language="JavaScript" src="js/format.js"></script>
    
    <%@include file="menuCaixa.jsp"%>
        
    <div class="divisoria"></div>
    <div id="titulo-subnav">Movimento de Caixa</div>
    <div class="divisoria"></div>
    
    <form action="c" method="POST" onsubmit="return pesquisa()">
        <input type="hidden" name="app" id="app" value="6040">
        <input type="hidden" name="acao" id="acao" value="grava">
        <input type="hidden" name="dtAtual" id="dtAtual" value="${dtAtual}">
        <input type="hidden" name="app6187" id="app6187" value="${app6187}">
        <input type="hidden" name="app6041" id="app6041" value="${app6041}">
        <input type="hidden" name="chq" id="chq" value="">
        <input type="hidden" name="formas" id="formas" value="">
        <input type="hidden" name="detCarne" id="detCarne" value="">
        <input type="hidden" name="operador" id="operador" value="">
        <input type="hidden" name="seqTaxaIndividual" id="seqTaxaIndividual" value="">
        <input type="hidden" name="nrConvite" id="nrConvite" value="">
        
        <sql:query var="rs" dataSource="jdbc/iate">
            SELECT 
                CD_TRANSACAO_CONV_ESP,
                CD_TRANSACAO_CONV_ESP_MEIO
            FROM 
                TB_PARAMETRO_SISTEMA
        </sql:query>

        <c:forEach var="row" items="${rs.rows}">
             <input type="hidden" name="cdTransConvEsp" id="cdTransConvEsp" value="${row.CD_TRANSACAO_CONV_ESP}">
             <input type="hidden" name="cdTransConvEspMeio" id="cdTransConvEspMeio" value="${row.CD_TRANSACAO_CONV_ESP_MEIO}">
        </c:forEach>
        
        <div style="position:absolute; top:160px;left:5px;">    
            <fieldset class="field-set legendaFrame recuoPadrao" style="width:550px;height: 127px">
                <div style="position:absolute; top:12px;left:15px;">    
                    <p class="legendaCodigo MargemSuperior0">Transacao:</p>
                    <div class="selectheightnovo">
                     <select name="transacao" id="transacao" class="campoSemTamanho alturaPadrao" style="width:520px;" onchange="trocaTransacao()">
                         <c:forEach var="tr" items="${transacoes}">
                             <option value="${tr.id}" <c:if test='${tr.id == transacao}'>selected</c:if>>${tr.descricao}</option>
                         </c:forEach>
                     </select>
                     <c:forEach var="tr" items="${transacoes}">
                         <input type="hidden" name="valTran${tr.id}" id="valTran${tr.id}" value="${tr.valPadrao}">
                         <c:choose>
                            <c:when test='${tr.tipo == "R"}'>
                               <input type="hidden" name="TranCarne" id="TranCarne" value="${tr.id}">
                            </c:when>
                            <c:when test='${tr.tipo == "S"}'>
                               <input type="hidden" name="TranPreSocio" id="TranPreSocio" value="${tr.id}">
                            </c:when>
                            <c:when test='${tr.tipo == "F"}'>
                               <input type="hidden" name="TranPreFunc" id="TranPreFunc" value="${tr.id}">
                            </c:when>
                         </c:choose>                         
                         <c:if test='${tr.cdTaxa>0}'>
                            <input type="hidden" name="cdTaxaTran${tr.id}" id="cdTaxaTran${tr.id}" value="${tr.cdTaxa}">
                         </c:if>             
                               
                               
                     </c:forEach>
                    </div>        
                </div>  
                <div style="position:absolute; top:65px;left:15px;">    
                    <p class="legendaCodigo MargemSuperior0" >Categoria/Matrícula</p>
                    <input type="text" name="categoria" id="categoria" class="campoSemTamanho alturaPadrao" style="width:40px;" >
                </div>  
                <div style="position:absolute; top:84px;left:60px;">    
                    <input type="text" name="matricula" id="matricula" class="campoSemTamanho alturaPadrao larguraNumero" >
                </div>  
                <div style="position:absolute; top:65px;left:140px;">    
                    <p class="legendaCodigo MargemSuperior0" >Carteira</p>
                    <input type="text" name="carteira" id="carteira" class="campoSemTamanho alturaPadrao" style="width:80px;" >
                </div>  
                <div style="position:absolute; top:65px;left:240px;">    
                    <p class="legendaCodigo MargemSuperior0" >Nome</p>
                    <input type="text" name="nome" id="nome" class="campoSemTamanho alturaPadrao" style="width:255px;" >
                </div>  
                <div style="position:absolute; top:80px;left:517px;">    
                    <input class="botaobuscainclusao" type="submit" onclick="return pesquisa()" value="" title="Consultar" />
                </div>  
                
            </fieldset>
        </div>  
        <div style="position:absolute; top:160px;left:577px;">    
            <fieldset class="field-set legendaFrame recuoPadrao" style="width:170px;height:465px">
                <div style="position:absolute; top:15px;left:15px;">    
                    <p class="legendaCodigo MargemSuperior0" >Dt. Vencimento</p>
                    <input type="text" name="dtVencimento" id="dtVencimento" class="campoSemTamanhoNegrito alturaPadrao" align="center" readonly style="width:120px;text-align:center;" value="${dtAtual}" >
                </div>  
                <div style="position:absolute; top:70px;left:15px;">    
                    <p class="legendaCodigo MargemSuperior0" >Vr. Devido</p>
                    <input type="text" name="vrDevido" id="vrDevido" class="campoSemTamanho alturaPadrao" onfocus="atualizaValores('vrDevido')" style="width:120px;text-align:right;" >
                </div>  
                <div style="position:absolute; top:125px;left:15px;">    
                    <p class="legendaCodigo MargemSuperior0" >Vr. Acréscimo</p>
                    <input type="text" name="vrAcrescimo" id="vrAcrescimo" class="campoSemTamanho alturaPadrao" readonly style="width:120px;text-align:right;" >
                </div>  
                <div style="position:absolute;top:150px;left:20px;"/>+</div>    
                <div style="position:absolute; top:180px;left:15px;">    
                    <p class="legendaCodigo MargemSuperior0" >Vr. Desconto</p>
                    <input type="text" name="vrDesconto" id="vrDesconto" class="campoSemTamanho alturaPadrao" onfocus="atualizaValores('vrDesconto')" style="width:120px;text-align:right;" >
                </div>  
                <div style="position:absolute;top:202px;left:20px;"/>-</div>    
                
                <div style="border-bottom:1px solid black;position:absolute;top:232px;left:22px;width:160px;"/></div>    
                
                <div style="position:absolute; top:235px;left:15px;">    
                    <p class="legendaCodigo MargemSuperior0" >Vr. em Dinheiro</p>
                    <input type="text" name="vrDinheiro" id="vrDinheiro" class="campoSemTamanho alturaPadrao" onfocus="atualizaValores('vrDinheiro')" style="width:120px;text-align:right;" >
                </div>  
                <div style="position:absolute; top:290px;left:15px;">    
                    <p class="legendaCodigo MargemSuperior0" >Vr. em Cheque</p>
                    <input type="text" name="vrCheque" id="vrCheque" class="campoSemTamanho alturaPadrao" onfocus="atualizaValores('vrCheque')" style="width:120px;text-align:right;" >
                </div>  
                <div style="position:absolute;top:315px;left:20px;"/>+</div> 
    
                <div style="position:absolute; top:345px;left:15px;">    
                    <p class="legendaCodigo MargemSuperior0" >Vr. Outros</p>
                    <input type="text" name="vrOutros" id="vrOutros" class="campoSemTamanho alturaPadrao" onfocus="atualizaValores('vrOutros')" style="width:120px;text-align:right;" >
                </div>  
                <div style="position:absolute;top:370px;left:20px;"/>+</div>    
            
                <div style="position:absolute; top:360px;left:151px;">    
                    <input class="botaoNovoItem" type="button" onclick="outrasFormas()" value="" title="Outras Formas de Pagamento" />
                </div>  
    
                <div style="border-bottom:1px solid black;position:absolute;top:407px;left:22px;width:160px;"/></div>    
            
                <div style="position:absolute; top:410px;left:15px;">    
                    <p class="legendaCodigo MargemSuperior0" >Vr. Total Pagar</p>
                    <input type="text" name="vrTotal" id="vrTotal" class="campoSemTamanhoNegrito alturaPadrao" readonly style="width:120px;text-align:right;" >
                </div>  
                <div style="position:absolute;top:435px;left:20px;"/>=</div>    
                
            </fieldset>
        </div>  
           
        <div style="position:absolute; top:310px;left:5px;">    
            <fieldset class="field-set legendaFrame recuoPadrao" style="width:550px;height:315px">
                <div style="width:545px;overflow:auto;height: 240px">    
                    <table id="tabelaMovimento" style="background:#fff">
                       <thead>
                        <tr class="odd">
                            <th>Descrição</th>
                            <th>Valor</th>
                        </tr>
                       </thead>
                       <tbody>
                       </tbody>
                    </table>
                </div>  
                
            </fieldset>
        </div>  
        <div style="position:absolute; top:635px;left:20px;width:140px;overflow: hidden">
            <input type="button" class="botaocheques" name="cmdCheques" id="cmdCheques" style="margin-left: -10px;" onclick="cheques()" value=" " />
        </div>        
        <div style="position:absolute; top:635px;left:171px;width:140px;overflow: hidden">
            <input type="button" class="botaosair" name="cmdSair" id="cmdSair" style="margin-left: -10px;" onclick="sair()" value=" " />
        </div>        
        <div style="position:absolute; top:635px;left:322px;width:140px;overflow: hidden">
            <input type="button" class="botaocancelar" name="cmdCancelar" id="cmdCancelar" style="margin-left: -10px;margin-top:0px" onclick="cancelar()" value=" " />
        </div>        
        <div style="position:absolute; top:635px;left:473px;width:140px;overflow: hidden">
            <input type="button" class="botaoatualizar" name="cmdAtualizar" id="cmdAtualizar" style="margin-left: -10px;" onclick="atualizar()" value=" " />
        </div>        
        <div style="position:absolute; top:635px;left:624px;width:140px;overflow: hidden">
            <input type="button" class="botaoobservacoes" name="cmdObservacoes" id="cmdObservacoes" style="margin-left: -10px;" onclick="observacoes()" value=" " />
        </div>        

                
        <!--
            ************************************************************************
            ************************************************************************
            ************************************************************************
                                    CAMPO DE OBSERVACAO
            ************************************************************************
            ************************************************************************
            ************************************************************************
        -->

        <div id="campoObservacao" style="overflow:auto;left: 25%;top: 20%;position: fixed;width: 500px; height:400px;">
            <table style="background:#fff">
                <tr>
                  <td>
                    <div class="divisoria"></div>
                    <div id="titulo-subnav">Observações do Caixa</div>
                    <div class="divisoria"></div>

                    <table class="fmt" align="left" style="margin-left:0px;">
                        <tr>
                            <td colspan="2">
                               <textarea class="campoSemTamanho" id="observacao" rows="10" cols="80" name="observacao"></textarea><br>
                            </td>
                        </tr>
                        <tr>
                            <td>
                                <br>
                                <input style="margin-left:15px;" type="button" id="cmdatualizar" name="cmdPesquisa" class="botaoatualizar" onclick="fechaObservacao()" />
                            </td>
                            <td>
                                <br>
                                <input type="button" style="margin-top:-5px;" id="cmdcancelar" name="cmdPesquisa" class="botaocancelar" onclick="cancelaObservacao()" />
                            </td>
                        </tr>
                    </table>  
                    <br>
                </td>
              </tr>
            </table>
        </div>                
                
        
        <!--
            ************************************************************************
            ************************************************************************
            ************************************************************************
                               TABELA DE SELECAO DE SOCIO
            ************************************************************************
            ************************************************************************
            ************************************************************************
        -->
        <div id="pesqSocio" style="overflow:auto;left: 25%;top: 20%;position: fixed;width: 700px; height:400px;">
            <table style="background:#fff">
                <tr>
                  <td>
                    <div class="divisoria"></div>
                    <div id="titulo-subnav">Seleção de Sócio</div>
                    <div class="divisoria"></div>

                    <table id="tabelaSocio" align="left" style="margin-left:15px;">
                        <thead>
                        <tr class="odd">
                            <th scope="col" class="nome-lista">Nome</th>
                            <th scope="col" align="left">Título</th>
                            <th scope="col" align="left">Tp Cadastro</th>
                            <th scope="col" align="left">Categoria</th>
                            <th scope="col" align="left">Tp Categoria</th>
                            <th scope="col" align="left">Carteira</th>
                        </tr>	
                        </thead>
                        <tbody>
                        </tbody>
                    </table>
                    
                    <br>
                    <input style="margin-left:15px;" type="button" id="cmdcancelar" name="cmdPesquisa" class="botaocancelar" onclick="cancelaPesquisaSocio()" />
                </td>
              </tr>
            </table>
        </div>                
        
        
        <!--
            ************************************************************************
            ************************************************************************
            ************************************************************************
                                      TABELA DE FUNCIONARIO
            ************************************************************************
            ************************************************************************
            ************************************************************************
        -->
        <div id="pesqFuncionario" style="overflow:auto;left: 25%;top: 20%;position: fixed;width: 700px; height:400px;">
            <table style="background:#fff">
                <tr>
                  <td>
                    <div class="divisoria"></div>
                    <div id="titulo-subnav">Seleção de Funcionário</div>
                    <div class="divisoria"></div>

                    <table id="tabelaFuncionario" align="left" style="margin-left:15px;">
                        <thead>
                        <tr class="odd">
                            <th scope="col" class="nome-lista">Nome</th>
                            <th scope="col" align="left">Matrícula</th>
                            <th scope="col" align="left">Setor</th>
                            <th scope="col" align="left">Cargo</th>
                        </tr>	
                        </thead>
                        <tbody>
                        </tbody>
                    </table>  
                    <br>
                    <input style="margin-left:15px;" type="button" id="cmdcancelar" name="cmdPesquisa" class="botaocancelar" onclick="cancelaPesquisaFunc()" />
                </td>
              </tr>
            </table>
        </div>                

        
        <!--
            ************************************************************************
            ************************************************************************
            ************************************************************************
                               TABELA DE SELECAO DE CARNE
            ************************************************************************
            ************************************************************************
            ************************************************************************
         -->
        <div id="pesqCarne" style="overflow:auto;left: 25%;top: 20%;position: fixed;width: 700px; height:400px;">
            <table style="background:#fff">
                <tr>
                  <td>
                    <div class="divisoria"></div>
                    <div id="titulo-subnav">Seleção de Carnê</div>
                    <div class="divisoria"></div>

                    <table id="tabelaCarne" align="left" style="margin-left:15px;">
                        <thead>
                        <tr class="odd">
                            <th scope="col" class="nome-lista">Nome</th>
                            <th scope="col" align="center">Nr. Carteirinha</th>
                            <th scope="col" align="center">Data Venc.</th>
                            <th scope="col" align="right">Valor</th>
                            <th scope="col" align="center">Título</th>
                        </tr>	
                        </thead>
                        <tbody>
                        </tbody>
                    </table>  
                    <br>
                    <input style="margin-left:15px;" type="button" id="cmdcancelar" name="cmdPesquisa" class="botaocancelar" onclick="cancelaPesquisaCarne()" />
                </td>
              </tr>
            </table>
        </div>                

         
        <!--
            ************************************************************************
            ************************************************************************
            ************************************************************************
                                     TABELA DE CHEQUES
            ************************************************************************
            ************************************************************************
            ************************************************************************
        -->
        <div id="campoCheque" style="overflow:auto;left: 25%;top: 20%;position: fixed;width: 700px; height:400px;">
            <table style="background:#fff">
                <tr>
                  <td>
                    <div class="divisoria"></div>
                    <div id="titulo-subnav">Cheques</div>
                    <div class="divisoria"></div>

                    <table class="fmt" align="left" >
                        <tr>
                            <td>
                                <p class="legendaCodigo MargemSuperior0" >Banco</p>
                                <input type="text" id="banco" name="banco" class="campoSemTamanho alturaPadrao larguraNumero" value="">
                            </td>

                            <td>
                                <p class="legendaCodigo MargemSuperior0" >Nº Cheque</p>
                                <input type="text" id="numCheque" name="numCheque" class="campoSemTamanho alturaPadrao larguraNumero" value="">
                            </td>
                            <td>
                                <p class="legendaCodigo MargemSuperior0" >Serie</p>
                                <input type="text" id="serie" name="serie" class="campoSemTamanho alturaPadrao larguraNumeroPequeno" value="">
                            </td>
                            <td>
                                <p class="legendaCodigo MargemSuperior0" >Dt. Depósito</p>
                                <input type="text" id="dtDeposito" name="dtDeposito" class="campoSemTamanho alturaPadrao larguraData" value="">
                            </td>
                            <td>
                                <p class="legendaCodigo MargemSuperior0" >Valor</p>
                                <input type="text" id="valorCheque" name="valorCheque" class="campoSemTamanho alturaPadrao larguraNumero" value="">
                            </td>
                            <td >    
                                <a href="#" onclick="incluiCheque()"><img src="imagens/btn-incluir.png" style="margin-left:20px; margin-top:25px" width="100" height="25" /></a>
                            </td>
                        </tr>
                    </table>
                    <br><br><br>
                    <table id="tabelaCheque" align="left" style="margin-left:15px;">
                        <thead>
                        <tr class="odd">
                            <th scope="col" class="nome-lista">Banco</th>
                            <th scope="col" align="center">Nº Cheque</th>
                            <th scope="col" align="center">Serie</th>
                            <th scope="col" align="center">Dt. Depósito</th>
                            <th scope="col" align="center">Valor</th>
                            <th scope="col" align="center">Excluir</th>
                        </tr>	
                        </thead>
                        <tbody>
                            <tr>
                                <td id="tdBanco" class="column1"></td>
                                <td id="tdNumCheque" class="column1"></td>
                                <td id="tdSerie" class="column1"></td>
                                <td id="tdDtDeposito" class="column1" align="center"></td>
                                <td id="tdVrCheque" class="column1" align="right"></td>
                                <td class="column1" align="center"><a id="excluiCheque" href="javascript: void(0);'"><img src="imagens/icones/inclusao-titular-05.png" /></a></td>
                            </tr>
                        </tbody>
                    </table>  
                    <br><br><br><br>
                    <input type="checkbox" name="controleParcPre" id="controleParcPre" style="margin-left:15px;" checked value="true"> Controlar Parcalamento/Pré-Datado
                    <br><br>

                    <input style="margin-left:15px;" type="button" id="cmdAtualizar" name="cmdPesquisa" class="botaoatualizar" onclick="atualizaCheque()" />
                </td>
              </tr>
            </table>
        </div>

        
        <!--
            ************************************************************************
            ************************************************************************
            ************************************************************************
                             TABELA DE OUTRAS FORMAS DE PAGAMENTO
            ************************************************************************
            ************************************************************************
            ************************************************************************
        -->
        <div id="campoOutrasFormas" style="overflow:auto;left: 25%;top: 20%;position: fixed;width: 690px; height:400px;">
            <table style="background:#fff">
                <tr>
                  <td>
                    <div class="divisoria"></div>
                    <div id="titulo-subnav">Outras Formas de Pagamento</div>
                    <div class="divisoria"></div>

                    <table class="fmt" align="left" >
                        <tr>
                            <td>
                                <p class="legendaCodigo MargemSuperior0" >Forma de Pagamento</p>
                                <select name="formaPagamento" id="formaPagamento" class="campoSemTamanho alturaPadrao" style="width:250px;">
                                    <c:forEach var="of" items="${outrasFormas}">
                                        <option value="${of.id}">${of.descricao}</option>
                                    </c:forEach>
                                </select>
                            </td>

                            <td>
                                <p class="legendaCodigo MargemSuperior0" >Valor</p>
                                <input type="text" id="valorOutrasFormas" name="valorOutrasFormas" class="campoSemTamanho alturaPadrao larguraNumero" value="">
                            </td>
                            <td>
                                <p class="legendaCodigo MargemSuperior0" >Documento</p>
                                <input type="text" id="docOutrasFormas" name="docOutrasFormas" class="campoSemTamanho alturaPadrao" tyle="width:150px;" value="">
                            </td>
                            <td >    
                                <a href="#" onclick="incluiOutrasFormas()"><img src="imagens/btn-incluir.png" style="margin-left:20px; margin-top:25px" width="100" height="25" /></a>
                            </td>
                        </tr>
                    </table>
                    <br><br><br>
                    <table id="tabelaOutrasFormas" align="left" style="margin-left:15px;">
                        <thead>
                        <tr class="odd">
                            <th scope="col" class="nome-lista">Forma de Pagamento</th>
                            <th style="display:none;">Id</th>
                            <th scope="col" align="center">Valor</th>
                            <th scope="col" align="center">Documento</th>
                            <th scope="col" align="center">Excluir</th>
                        </tr>	
                        </thead>
                        <tbody>
                            <tr>
                                <td id="tdForma" class="column1"></td>
                                <td id="tdIdForma" style="display:none;"></td>
                                <td id="tdVrForma" class="column1" align="right"></td>
                                <td id="tdDocForma" class="column1" align="right"></td>
                                <td class="column1" align="center"><a id="excluiOutrasFormas" href="javascript: void(0);'"><img src="imagens/icones/inclusao-titular-05.png" /></a></td>
                            </tr>
                        </tbody>
                    </table>  
                    <br><br><br><br>

                    <input style="margin-left:15px;" type="button" id="cmdAtualizar" name="cmdPesquisa" class="botaoatualizar" onclick="atualizaOutrasFormas()" />
                </td>
              </tr>
            </table>
        </div>

        
        <!--
            ************************************************************************
            ************************************************************************
            ************************************************************************
                            TABELA DE SELECAO DE TAXA INDIVIDUAL
            ************************************************************************
            ************************************************************************
            ************************************************************************
         -->
        <div id="pesqTaxaIndividual" style="overflow:auto;left: 25%;top: 20%;position: fixed;width: 700px; height:400px;">
            <table style="background:#fff">
                <tr>
                  <td>
                    <div class="divisoria"></div>
                    <div id="titulo-subnav">Seleção de Taxa Individual</div>
                    <div class="divisoria"></div>

                    <table id="tabelaTaxaIndividual" align="left" style="margin-left:15px;">
                        <thead>
                        <tr class="odd">
                            <th scope="col" align="center">Sel.</th>
                            <th scope="col" class="nome-lista">Nome</th>
                            <th scope="col" align="left">Taxa</th>
                            <th scope="col" align="right">Valor</th>
                            <th scope="col" align="right">Dt. Geração</th>
                            <th scope="col" align="center">Cobrar em</th>
                        </tr>	
                        </thead>
                        <tbody>
                        </tbody>
                    </table>  
                    <br>
                    <table class="fmt" id="tabelaTaxaIndividual" align="left" style="margin-left:0px;">
                        <tr>
                            <td>
                                <input type="button" id="selecionar" name="selecionar" style="margin-top:5px" class="botaoSelecionar" onclick="selecionaTaxaIndividual()"  value=" " />
                            </td>
                            <td>
                                <input style="margin-left:15px;" type="button" id="cmdcancelar" name="cmdPesquisa" class="botaocancelar" onclick="cancelaPesquisaTaxaIndividual()" />
                            </td>
                        </tr>
                    </table>  
                </td>
              </tr>
              
            </table>
        </div>                
        
        <!--
            ************************************************************************
            ************************************************************************
            ************************************************************************
                            TABELA DE SELECAO DE CONVITE ESPORTIVO
            ************************************************************************
            ************************************************************************
            ************************************************************************
         -->
         
        <div id="pesqConviteEsportivo" style="overflow:auto;left: 25%;top: 20%;position: fixed;width: 700px; height:400px;">
            <table style="background:#fff">
                <tr>
                  <td>
                    <div class="divisoria"></div>
                    <div id="titulo-subnav">Seleção de Convite Esportivo</div>
                    <div class="divisoria"></div>

                    <table id="tabelaConviteEsportivo" align="left" style="margin-left:15px;">
                        <thead>
                        <tr class="odd">
                            <th scope="col" align="nome-lista">Convidado</th>
                            <th scope="col" class="left">Sócio</th>
                            <th scope="col" align="left">Modalidade</th>
                            <th scope="col" align="center">Validade</th>
                            <th scope="col" align="center">Nr. Convite</th>
                        </tr>	
                        </thead>
                        <tbody>
                        </tbody>
                    </table>  
                    <br>
                    <table class="fmt" id="tabelaTaxaIndividual" align="left" style="margin-left:0px;">
                        <tr>
                            <td>
                                <input style="margin-left:15px;" type="button" id="cmdcancelar" name="cmdPesquisa" class="botaocancelar" onclick="cancelaPesquisaConviteEsportivo()" />
                            </td>
                        </tr>
                    </table>  
                </td>
              </tr>
              
            </table>
        </div>                
                
    </form>

 </body>
</html>
