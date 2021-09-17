<%@taglib prefix="sql" uri="http://java.sun.com/jsp/jstl/sql"%>
<%@include file="head.jsp"%>

<style type="text/css">
    table.fmt {border:none;width:0;padding:0px;margin-left:0em;margin-top:0em;}
    table.fmt tr td {border:none;background:none;padding:0px;margin:0em auto;}
    
    .tituloCC{
            font-family: "Arial";
            font-size: 24px;
            font-weight: bold;
            color: red;
    }    
</style>  

<script type="text/javascript" language="JavaScript" src="js/format.js"></script>

<script type="text/javascript" language="JavaScript">
    $(document).ready(function () {
            $('#tabelaMovimento tr:gt(0)').css('background', 'white');
            $('#tabelaCarne tr:gt(0)').css('background', 'white');
            $('#tabelaSocio tr:gt(0)').css('background', 'white');
            $('#tabelaCheque tr:gt(0)').css('background', 'white');
            

            $('#tabelaMovimento tr:gt(0)').hover(function() {
                    $(this).css('background','#f4f9fe');
            }, function() {
                    $(this).css('background','white');
            })
            $('#tabelaSocio tr:gt(0)').hover(function() {
                    $(this).css('background','#f4f9fe');
            }, function() {
                    $(this).css('background','white');
            })
            $('#tabelaCheque tr:gt(0)').hover(function() {
                    $(this).css('background','#f4f9fe');
            }, function() {
                    $(this).css('background','white');
            })
            
            $("#excluiCheque").live("click", excluiCheque);
            $("#excluiMovimento").live("click", excluiMovimento);
            
            
            $("#pesqSocio").hide();
            $("#pesqCarne").hide();
            $("#campoObservacao").hide();
            $('#campoCheque').hide();
            $('#divNovaPessoa').hide();
            
            $('#dtDeposito').mask('99/99/9999');
            
            $("#vrDevido").mouseup(function(e){e.preventDefault();});            
            $("#vrDebito").mouseup(function(e){e.preventDefault();});            
            $("#vrDesconto").mouseup(function(e){e.preventDefault();});            
            $("#vrDinheiro").mouseup(function(e){e.preventDefault();});            
            $("#vrCheque").mouseup(function(e){e.preventDefault();});            
            $("#vrTotal").mouseup(function(e){e.preventDefault();});   
            
            $("#tabelaCheque tr:eq(1)").css('display', 'none');
            
            $.format.locale({
                number: {
                groupingSeparator: '.',
                decimalSeparator: ','
                }
            });
            
            limpaGeral();
    });   
    
    
    function pesquisa(){
        $('#tabelaSocio').find('tbody').empty();
        $('#nomeSocio').val('');
        $('#matriculaSocio').val('');
        $('#categoriaSocio').val('');
        
        $("#pesqSocio").show();
        $("#categoriaSocio").focus();
    }
    
    function carregaSocio(){
        
        //pesquisar socio
        var tabela = $('#tabelaSocio').find('tbody').empty();

        $.ajax({url:'TaxaIndividualSocioAjax', type:'GET',
                            data:{
                            nome:$('#nomeSocio').val(),
                            matricula:$('#matriculaSocio').val(),
                            categoria:$('#categoriaSocio').val(),
                            carteira:''
                            }
        }).success(function(retorno){
            if (retorno.erro !== undefined){
                alert(retorno.erro);
            }else {

                var linha="";
                var tpCadastro="";
                var tpCategoria="";
                var foto="";
                var autorizacao="";
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
                    foto='<img src="f?tb=6&mat='+this.matricula+'&seq='+this.dependente+'&cat='+this.categoria+'" class="recuoPadrao MargemSuperiorPadrao" width="80" height="100">';
                    autorizacao = "";
                    if (parseInt(this.dependente)>0){
                        autorizacao= '<a  href="javascript:autorizaDependente(' +this.matricula+','+this.categoria+','+this.dependente+ ')"><img src="imagens/icones/inclusao-titular-17.png"/></a>'
                    }
                    
                    
                    linha='<tr><td class="column1"><a href="#" id="tabNome" class="column1" onclick="selecionaSocio('+i+')">'+this.nome+'</a></td><td class="column1" id="tabTitulo" align="center">'+this.titulo+'</td><td class="column1">'+tpCadastro+'</td><td class="column1">'+this.descricao+'</td><td class="column1">'+tpCategoria+'</td><td id="tabCarteira" class="column1">'+this.carteira+'</td><td align="center">'+autorizacao+'</td><td>'+foto+'</td></tr>';
                    tabela.append(linha)
                });

            }
        });
    }
    
    function autorizaDependente(matricula, categoria, dependente){
        window.open('c?app=1740&acao=showForm&matricula='+matricula+'&idCategoria='+categoria+'&seqDependente='+dependente+'&alerta=0','page','toolbar=no,location=no,status=no,menubar=no,scrollbars=no,resizable=no,width=600,height=600'); 
    }
    
    function selecionaSocio(indice){
        $("#pesqSocio").hide();
        $("#tabelaSocio tr:eq("+(parseInt(indice)+1)+")").each(function(index){
            var item = '<option selected value=0>'+'#'+$(this).find('#tabTitulo').html() + ' - ' + ($(this).find('#tabNome').html())+'</option>';
            $("#pendencia").append(item);
        });
        limpaGeral();

    }
    
    
    function cancelaPesquisaSocio() {
        $("#pesqSocio").hide();
    }        
        
    function limpaGeral(){
        
        $('#vrDesconto').val('0,00');
        $('#vrDinheiro').val('0,00');
        $('#vrCheque').val('0,00');
        $('#vrDebito').val('0,00');
        $('#vrTotal').val('0,00');
        $('#vrDevido').val('0,00');
        
        $("textarea#observacao").val('');
        
        $('#tabelaMovimento').find('tbody').empty();
    }
    
    function sair(){
        $('#app').val('6000');
        $('#acao').val('');
        document.forms[0].submit();
    }
    function cancelar(){
        $('#app').val('6160');
        $('#acao').val('showForm');
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
    function atualizaCheque(){
        $('#campoCheque').hide();
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
    function incluiCheque(){
        
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
    function excluiCheque(){
        $(this).parent().parent().remove();
    }
    
    function atualizaValores(campo){
        
        var vrDesconto = parseFloat($('#vrDesconto').val().replace('.', '').replace(',', '.'));
        var vrDinheiro = parseFloat($('#vrDinheiro').val().replace('.', '').replace(',', '.'));
        var vrCheque = parseFloat($('#vrCheque').val().replace('.', '').replace(',', '.'));
        var vrDebito = parseFloat($('#vrDebito').val().replace('.', '').replace(',', '.'));
        var vrDevido = parseFloat($('#vrDevido').val().replace('.', '').replace(',', '.'));
        
        var vrTotal = vrDevido - vrDesconto;
        if(vrTotal<0){
            vrTotal = 0;
        }
        
        if (campo == 'vrDinheiro'){
            vrDinheiro = vrDevido - vrDesconto - vrCheque - vrDebito;
            if(vrDinheiro<0){
                vrDinheiro = 0;
            }
        }else if (campo == 'vrCheque'){
            vrCheque = vrDevido - vrDesconto - vrDinheiro - vrDebito;
            if(vrCheque<0){
                vrCheque = 0;
            }
        }else if (campo == 'vrDebito'){
            vrDebito = vrDevido - vrDesconto - vrDinheiro - vrCheque;
            if(vrDebito<0){
                vrDebito = 0;
            }
        }   
        
        $('#vrDesconto').val($.format.number(vrDesconto, '#,##0.00'))
        $('#vrDinheiro').val($.format.number(vrDinheiro, '#,##0.00'));
        $('#vrCheque').val($.format.number(vrCheque, '#,##0.00'));
        $('#vrDevido').val($.format.number(vrDevido, '#,##0.00'));
        $('#vrDebito').val($.format.number(vrDebito, '#,##0.00'));
        $('#vrTotal').val($.format.number(vrTotal, '#,##0.00'));
        
        $('#'+campo).select();
        
    }
    function trocaPendencia(){
        limpaGeral();
        
        //pesquisar pendencia
        var tabela = $('#tabelaMovimento').find('tbody').empty();

        $.ajax({url:'MovProdServPendenciaAjax', type:'GET',data:{pendencia:$('#pendencia').val()}
        }).success(function(retorno){
            if (retorno.erro !== undefined){
                alert(retorno.erro);
            }else {
                var linha="";
                var campoHidden="";
                var campoExcluir='';
                $.each(retorno.relacao, function(i) {
                    campoHidden='<input type="hidden" id="campoCodigo" value="'+this.codigo+'"/>' //codigo do produto ou servico
                    linha='<tr><td id="tabProduto" class="column1">'+this.detalhe+' '+campoHidden+'</td><td class="column1" id="tabQuantidade" align="center">'+this.quantidade+'</td><td id="tabValor" align="right" class="column1">'+this.valor+'</td>';
                    if (this.codigo!=""){
                        campoExcluir='<td class="column1" align="center"><a id="excluiMovimento" href="javascript: void(0);" ><img src="imagens/icones/inclusao-titular-03.png"/></a></td>';
                        linha = linha+campoExcluir+'</tr>';
                    }else{
                        linha = linha+'<td></td></tr>';
                    }
                            
                    tabela.append(linha)
                });

            }
            atualizaTotal();
            $('#prodServ').focus();
        });
        

        
    }
    
    function excluiMovimento(){
        
        var numLinha = $('#tabelaMovimento tr').index($(this).closest('tr'));
        var linha = $("#tabelaMovimento tr:eq("+(parseInt(numLinha))+")");
        var linhaAnt = $("#tabelaMovimento tr:eq("+(parseInt(numLinha-1))+")");
        var linhaPos = $("#tabelaMovimento tr:eq("+(parseInt(numLinha+1))+")");
    
        var excluiPai = "NAO";
        //verifica se eh preciso excluir a data
        if (linhaAnt.find('#tabProduto').html().substring(0,6)!='&nbsp;'){
            //a linha anterior não eh produto, tenho que ver se a proxima também não eh para excluir
            if (linhaPos.html()==undefined){
                // a linha que está sendo excluida eh a ultima da tabela
                excluiPai = 'SIM';
                numLinha--;
            }else if (linhaPos.find('#tabProduto').html().substring(0,6)!='&nbsp;'){
                // a proxima linha nao eh detalhe
                excluiPai = 'SIM';
                numLinha--;
            }    
        }
            
        $("#tabelaMovimento tr:eq("+(parseInt(numLinha))+")").each(function(index){
            $(this).remove();
        });
        if (excluiPai=='SIM'){
            $("#tabelaMovimento tr:eq("+(parseInt(numLinha))+")").each(function(index){
                $(this).remove();
            });
        }
        
    }
    
    function adicionaMovimento(){
        
        var qtd = parseInt($('#quantidade').val());
        if(isNaN(qtd)){
            return false;
        }
        
        var codProdServ = $('#prodServ').val();
        var valorProdServ = parseFloat($('#valProdServ'+codProdServ).val());
        var tabela = $('#tabelaMovimento').find('tbody');
        var linha = '';
        
        var achou='N';
        var campoHidden="";
        var campoExcluir='';
        var diaIgual='N'
        $("#tabelaMovimento tr:gt(0)").each(function(index){
            if ($(this).find('#tabProduto').html().substring(0,10)==$('#dtAtual').val()){
                //achou registro na mesma data
                diaIgual='S';
            } 
            var qtd2=0;
            var val=0;
            if (diaIgual=='S'){
                if($(this).find('#tabProduto').find('#campoCodigo').val()==codProdServ){
                    achou='S';
                    
                    qtd2=parseInt($(this).find('#tabQuantidade').html());
                    val = ((qtd+qtd2)*valorProdServ)
                    
                    $(this).find('#tabQuantidade').html((qtd+qtd2));
                    $(this).find('#tabValor').html($.format.number(val, '#,##0.00'));
                    
                    
                    return;
                }
            }
            
        });
        
        if (achou=='N'){
            if (diaIgual=='N'){
                linha='<tr><td id="tabProduto" class="column1">'+$("#dtAtual").val()+'</td><td class="column1" id="tabQuantidade" align="center"></td><td id="tabValor"  align="right" class="column1"></td><td></td></tr>';
                tabela.append(linha);
            }
            
            campoHidden='<input type="hidden" id="campoCodigo" value="'+codProdServ+'"/>' //codigo do produto ou servico
            linha='<tr><td id="tabProduto" class="column1">&nbsp;&nbsp;&nbsp;'+$("#prodServ option:selected").text()+' '+campoHidden+'</td><td class="column1" id="tabQuantidade" align="center">'+qtd+'</td><td id="tabValor"  align="right" class="column1">'+$.format.number(qtd * valorProdServ, '#,##0.00')+'</td>';
            campoExcluir='<td class="column1" align="center"><a id="excluiMovimento" href="javascript: void(0);" ><img src="imagens/icones/inclusao-titular-03.png"/></a></td>';
            linha = linha+campoExcluir+'</tr>';
            tabela.append(linha);
        }
        
        atualizaTotal();
        
        $('#quantidade').val(1);
        $('#prodServ').focus();
        

    }

    function atualizaTotal(){
        var val=0;
        var total=0;
        $("#tabelaMovimento tr:gt(0)").each(function(index){
            val = parseFloat($(this).find('#tabValor').html().replace('.', '').replace(',', '.'));
            if (!isNaN(val)){
                total = total + val;
            }
        });
        
        $('#vrDevido').val($.format.number(total, '#,##0.00'));
        atualizaValores('');
        
    }
    
    function novaPessoa(){
        $('#divNovaPessoa').show();
    }
    function cancelaNovaPessoa(){
        $('#divNovaPessoa').hide();
    }
    function atualizaNovaPessoa(){
        if ($('#nomePessoaNova').val()==""){
            alert('Informe o nome da Pessoa');
            return false;
        }
        var item = '<option selected value=0>'+$('#nomePessoaNova').val()+'</option>';
        $("#pendencia").append(item);

        $('#divNovaPessoa').hide();
        limpaGeral();
    }
    
    
    function atualizar(){
        
        if ($("#pendencia option:selected").text()==''){
            alert('Informe a Pessoa!')
            return false;
        }
        
        concatenaMovimento();        
        
        //alert($('#detMov').val());
        $('#acao').val('atualiza');
        $('#nome').val($("#pendencia option:selected").text());

        document.forms[0].submit();

    }
    
    function excluiPendencia(){
        if (confirm('Deseja realmente excluir o movimento selecionado?')){
            $('#acao').val('exclui');
            document.forms[0].submit();
        }
    }
    
    function imprime(){
        $('#acao').val('imprimeParcial');
        $('#nome').val($("#pendencia option:selected").text());
        
        concatenaMovimento();        
        
        document.forms[0].submit();
    }
    
    function concatenaMovimento(){
        //concatenando o movimento
        var data = '';
        var codigo = '';
        var quantidade = '';
        var valor = '';
        $("#tabelaMovimento tr:gt(0)").each(function(index){
            //alert($(this).find('#tabProduto').html().substring(0,6));
            if (!($(this).find('#tabProduto').html().substring(0,6)=='&nbsp;')){
                data = $(this).find('#tabProduto').html().substring(0,10);
            }else{
                codigo = $(this).find('#tabProduto').find('#campoCodigo').val();
                quantidade = $(this).find('#tabQuantidade').html();
                valor = $(this).find('#tabValor').html().replace('.', '').replace(',', '.');
                
                $('#detMov').val($('#detMov').val() + data + '#' + codigo + '#' + quantidade + '#' + valor + ';');
            }
        });
        
    }
    
    function mostraBotoes(){
        $('#botaocheques').show();
        $('#botaosair').show();
        $('#botaocancelar').show();
        $('#botaoatualizar').show();
        $('#botaoobservacoes').show();
        $('#botaofechar').show();
    }
        
    function escondeBotoes(){
        $('#botaocheques').hide();
        $('#botaosair').hide();
        $('#botaocancelar').hide();
        $('#botaoatualizar').hide();
        $('#botaoobservacoes').hide();
        $('#botaofechar').hide();
    }
        
    
    
    function fechar(){
        escondeBotoes();
        var vrDesconto = parseFloat($('#vrDesconto').val().replace('.', '').replace(',', '.'));
        var vrDinheiro = parseFloat($('#vrDinheiro').val().replace('.', '').replace(',', '.'));
        var vrCheque = parseFloat($('#vrCheque').val().replace('.', '').replace(',', '.'));
        var vrDebito = parseFloat($('#vrDebito').val().replace('.', '').replace(',', '.'));
        var vrDevido = parseFloat($('#vrDevido').val().replace('.', '').replace(',', '.'));
        
        var vrTotal = vrDevido - vrDesconto;
        
        var vr1=0;
        var qtCheque=0;
        var ret; 
        var matSocio = 0;
        var catSocio = 0;
        var seqDep = 0;
        var nomeSocio = '';

        $("#tabelaCheque tr:gt(1)").each(function(index){
            qtCheque++;
            vr1 = vr1 + parseFloat($(this).find('#tdVrCheque').html().replace('.', '').replace(',', '.'));
        });
        
        if ($.format.number( vrCheque, '#,##0.00') != $.format.number( vr1, '#,##0.00')){
            alert('A Soma dos cheques não confere com o valor informado!');
            mostraBotoes();
            return false;
        }
		
        if ($.format.number( vrDinheiro + vrCheque + vrDebito, '#,##0.00') != $.format.number( vrTotal, '#,##0.00')){
            alert('A Soma dos valores não confere!');
            mostraBotoes();
            return false;
        }
        
        if (vrDevido == 0){
            alert('Não é possível receber valor ZERO!');
            mostraBotoes();
            return false;
        }
        
        //verificando o socio para controle de cheque e tx individual
        if ($("#pendencia option:selected").text().substring(0,1)=='#'){
            catSocio = $("#pendencia option:selected").text().substring(1,3);
            matSocio = $("#pendencia option:selected").text().substring(4,8);
            seqDep = $("#pendencia option:selected").text().substring(9,11);
            
            $.ajax({url:'MovimentoSocioAjax', type:'GET', async:false,
                                data:{
                                nome:'',
                                matricula:matSocio,
                                categoria:catSocio,
                                carteira:''
                                }
            }).success(function(retorno){
                if (retorno.erro !== undefined){
                    alert(retorno.erro);
                    mostraBotoes();
                }else {
                    $.each(retorno.jogador, function(i) {
                        nomeSocio = this.nome;
                    });    
                }
            });
            
        }
        
        if (vrDebito > 0 || ($('#controleParcPre').attr('checked') == 'checked' && qtCheque>0)){
            if (nomeSocio==''){
                alert('Sócio não encontrado para controlar o parcelamento/pré-datado do(s) cheques e/ou Débito!');
                mostraBotoes();
                return false;
            }
        }
        
        //validacoes da taxa individual
        if (vrDebito>0){
            
            if ($('#cdTxIndividual').val()=='' || $('#cdTxIndividual').val()=='0'){
                alert('Não é possível incluir débito automático neste Centro de Custos pois não existe Taxa vinculada a ele!');
                mostraBotoes();
                return false;
            }
            
            $.ajax({url:'TaxaIndividualAjax', type:'GET',async:false,
                                data:{
                                tipo:4,
                                categoria:catSocio,
                                matricula:matSocio,
                                dependente:seqDep,
                                taxa:$('#cdTxIndividual').val()
                                }
            }).success(function(retorno){
                ret = retorno;
            });
            if (ret!="OK"){
                alert('Pessoa não autorizada para lançamento nesta taxa!');
                mostraBotoes();
                return false;
            }

            if (parseInt(seqDep)>0){
                $.ajax({url:'TaxaIndividualAjax', type:'GET',async:false,
                                    data:{
                                    tipo:5,
                                    categoria:catSocio,
                                    matricula:matSocio,
                                    dependente:seqDep,
                                    taxa:$('#taxa').val()
                                    }
                }).success(function(retorno){
                    ret = retorno;
                });
                if (ret!="OK"){
                    alert('Dependente não autorizado para esta Taxa!');
                    mostraBotoes();
                    return false;
                }
            }

            $.ajax({url:'TaxaIndividualAjax', type:'GET',async:false,
                                data:{
                                tipo:6,
                                categoria:catSocio,
                                matricula:matSocio
                                }
            }).success(function(retorno){
                ret = retorno;
            });
            if (ret!="OK"){
                alert('Situacao da Pessoa nao permite lancamento de Débito!');
                mostraBotoes();
                return false;
            }

            $.ajax({url:'TaxaIndividualAjax', type:'GET',async:false,
                                data:{
                                tipo:7,
                                categoria:catSocio
                                }
            }).success(function(retorno){
                ret = retorno;
            });
            if (ret!="OK"){
                alert('Categoria não autorizada para lançamento de Débito!');
                mostraBotoes();
                return false;
            }

            $.ajax({url:'TaxaIndividualAjax', type:'GET',async:false,
                                data:{
                                tipo:8,
                                categoria:catSocio,
                                matricula:matSocio
                                }
            }).success(function(retorno){
                ret = retorno;
            });
            if (ret!="OK"){
                if ($('#app6182').val()=="true"){
                    if (!confirm('O título se encontra com carnes vencidos em aberto! Deseja continuar com a transação?')){
                        mostraBotoes();
                        return false;
                    }
                }else{
                    alert('O título se encontra com carnes vencidos em aberto, impossibilitando conclusão da transação!');
                    mostraBotoes();
                    return false;
                }
            }
        }

        //controle do parcelamento do cheque
        if ($('#controleParcPre').attr('checked') == 'checked' && qtCheque>0){
            
            if (!confirm('Confirma o controle de parcelamento/pré-datado do(s) cheque(s) para o título '+ catSocio + '/' + matSocio + ' - ' + nomeSocio + '?')){
                mostraBotoes();
                return false;
            }
            
            $.ajax({url:'MovimentoAjax', type:'GET', async:false,
                                data:{
                                tipo:2,
                                matricula:matSocio,
                                categoria:catSocio
                                }
            }).success(function(retorno){
                ret=retorno;
            });
            
            if (ret == 'JATEM'){
                if ($('#app6041').val()=='true'){
                    if (!confirm('Já existe um parcelamento ou cheque pré-datado que ainda não atingiu a data de depósito. Deseja continuar com outro parcelamento?')){
                        mostraBotoes();
                        return false;
                    }
                }else{
                    alert('Já existe um parcelamento ou cheque pré-datado que ainda não atingiu a data de depósito, impossibilitando a criação de um novo parcelamento!');
                    mostraBotoes();
                    return false;
                }
            }
            
            
        }
        
        // se tiver debito ou se tiver cheque com controle de parcelamento tem que verificar o saldo
        if (vrDebito > 0 || ($('#controleParcPre').attr('checked') == 'checked' && qtCheque>0)){
        
            $.ajax({url:'MovimentoAjax', type:'GET', async:false,
                                data:{
                                tipo:1,
                                matricula:matSocio,
                                categoria:catSocio,
                                valor:(vrCheque+vrDebito),
                                origem:'6160',
                                nome:$("#pendencia option:selected").text()
                                }
            }).success(function(retorno){
                ret=retorno;
            });
            
            if (ret == 'PROBLEMA'){
                if ($('#app6187').val()=='true'){
                    if (!confirm('O valor do Cheque vai ultrapassar o limite máximo estipulado para Taxas Individuais e Cheques Pré-Datados. Deseja continuar com a transação?')){
                        mostraBotoes();
                        return false;
                    }
                }else{
                    alert('O valor do Cheque vai ultrapassar o limite máximo estipulado para Taxas Individuais e Cheques Pré-Datados!');
                    mostraBotoes();
                    return false;
                }
            }
        }
        
        
        //concatenando os cheques
        var cheque = '';
        $("#tabelaCheque tr:gt(1)").each(function(index){
            cheque = cheque + $(this).find('#tdBanco').html() + '#';
            cheque = cheque + $(this).find('#tdNumCheque').html() + '#';
            cheque = cheque + $(this).find('#tdSerie').html() + '#';
            cheque = cheque + $(this).find('#tdDtDeposito').html() + '#';
            cheque = cheque + $(this).find('#tdVrCheque').html().replace('.', '').replace(',', '.') + ';';
        });
        
        $('#chq').val(cheque);

        concatenaMovimento();
        
        $('#nome').val($("#pendencia option:selected").text());
        
        document.forms[0].submit();
        
    }
    
</script>  


<body class="internas">

    <script type="text/javascript" language="JavaScript" src="js/format.js"></script>
    
    <%@include file="menuCaixa.jsp"%>
        
    <div class="divisoria"></div>
    <div id="titulo-subnav">Movimento de Caixa</div>
    <div class="divisoria"></div>
    
    <form action="c" method="POST">
        <input type="hidden" name="app" id="app" value="6160">
        <input type="hidden" name="acao" id="acao" value="grava">
        <input type="hidden" name="idCentroCusto" id="idCentroCusto" value="${centroCusto.id}">
        <input type="hidden" name="dtAtual" id="dtAtual" value="${dtAtual}">
        <input type="hidden" name="app6187" id="app6187" value="${app6187}">
        <input type="hidden" name="app6041" id="app6041" value="${app6041}">
        <input type="hidden" name="app6041" id="app6182" value="${app6182}">
        <input type="hidden" name="cdTxIndividual" id="cdTxIndividual" value="${centroCusto.taxa}">
        <input type="hidden" name="nome" id="nome" value="">
        <input type="hidden" name="chq" id="chq" value="">
        <input type="hidden" name="detMov" id="detMov" value="">
       
        <div style="position:absolute; top:160px;left:5px;">    
            <fieldset class="field-set legendaFrame recuoPadrao" style="width:550px;height: 147px">
                <div class="tituloCC" style="position:absolute; top:10px;left:15;width:550px;" align="center">    
                    ${centroCusto.descricao}
                </div>  
                <div style="position:absolute; top:32px;left:15px;">    
                    <p class="legendaCodigo MargemSuperior0">Nome:</p>
                    <div class="selectheightnovo">
                     <select name="pendencia" id="pendencia" class="campoSemTamanho alturaPadrao" style="width:400px;" onchange="trocaPendencia()">
                         <option value="0"></option>
                         <c:forEach var="pd" items="${pendencias}">
                             <option value="${pd.id}">${pd.descricao}</option>
                         </c:forEach>
                     </select>
                    </div>        
                </div>
                <div style="position:absolute; top:47px;left:430px;">    
                    <input class="botaoNovaPessoa" type="button" onclick="novaPessoa()" value="" title="Consultar" />
                </div>  
                <div style="position:absolute; top:47px;left:460px;">    
                    <input class="botaobuscainclusao" type="button" onclick="pesquisa()" value="" title="Consultar" />
                </div>  
                <div style="position:absolute; top:47px;left:495px;">    
                    <input class="botaoExclui" type="button" onclick="excluiPendencia()" value="" title="Consultar" />
                </div>  
                <div style="position:absolute; top:47px;left:525px;">    
                    <input class="botaoImprime" type="button" onclick="imprime()" value="" title="Consultar" />
                </div>  
                
                <div style="position:absolute; top:85px;left:15px;">    
                    <p class="legendaCodigo MargemSuperior0">Produtos e Serviços:</p>
                    <div class="selectheightnovo">
                     <select name="prodServ" id="prodServ" class="campoSemTamanho alturaPadrao" style="width:435px;" >
                         <c:forEach var="pdsr" items="${produtos}">
                            <c:if test="${pdsr.ativo == 'Ativo'}">
                                <option value="${pdsr.id}">${pdsr.descricao}</option>
                            </c:if>
                         </c:forEach>
                         <c:forEach var="pdsr" items="${produtos}">
                            <c:if test="${pdsr.ativo == 'Ativo'}">
                                 <input type="hidden" name="valProdServ${pdsr.id}" id="valProdServ${pdsr.id}" value="${pdsr.valPadrao}">
                            </c:if>
                         </c:forEach>
                     </select>
                    </div>        
                </div>
                <div style="position:absolute; top:85px;left:460px;">    
                    <p class="legendaCodigo MargemSuperior0" >Qtd.</p>
                    <input type="text" name="quantidade" id="quantidade" class="campoSemTamanho alturaPadrao" style="width:40px;" value="1" >
                </div>  
                <div style="position:absolute; top:100px;left:517px;">    
                    <input class="botaoAdiciona" type="button" onclick="adicionaMovimento()" value="" title="Consultar" />
                </div>  
            </fieldset>
        </div>
                
        <div style="position:absolute; top:160px;left:577px;">    
            <fieldset class="field-set legendaFrame recuoPadrao" style="width:170px;height: 410px">
                <div style="position:absolute; top:15px;left:25px;">    
                    <p class="legendaCodigo MargemSuperior0" >Dt. Vencimento</p>
                    <input type="text" name="dtVencimento" id="dtVencimento" class="campoSemTamanhoNegrito alturaPadrao" align="center" readonly style="width:120px;text-align:center;" value="${dtAtual}" >
                </div>  
                <div style="position:absolute; top:70px;left:25px;">    
                    <p class="legendaCodigo MargemSuperior0" >Vr. Devido</p>
                    <input type="text" name="vrDevido" id="vrDevido" class="campoSemTamanho alturaPadrao" onfocus="atualizaValores('vrDevido')" readonly style="width:120px;text-align:right;" >
                </div>  
                <div style="position:absolute; top:125px;left:25px;">    
                    <p class="legendaCodigo MargemSuperior0" >Vr. Desconto</p>
                    <input type="text" name="vrDesconto" id="vrDesconto" class="campoSemTamanho alturaPadrao" onfocus="atualizaValores('vrDesconto')" style="width:120px;text-align:right;" >
                </div>  
                <div style="position:absolute;top:147px;left:25px;"/>-</div>    
                
                <div style="border-bottom:1px solid black;position:absolute;top:177px;left:22px;width:160px;"/></div>    
                
                <div style="position:absolute; top:180px;left:25px;">    
                    <p class="legendaCodigo MargemSuperior0" >Vr. em Dinheiro</p>
                    <input type="text" name="vrDinheiro" id="vrDinheiro" class="campoSemTamanho alturaPadrao" onfocus="atualizaValores('vrDinheiro')" style="width:120px;text-align:right;" >
                </div>  
                <div style="position:absolute; top:235px;left:25px;">    
                    <p class="legendaCodigo MargemSuperior0" >Vr. em Cheque</p>
                    <input type="text" name="vrCheque" id="vrCheque" class="campoSemTamanho alturaPadrao" onfocus="atualizaValores('vrCheque')" style="width:120px;text-align:right;" >
                </div>  
                <div style="position:absolute;top:258px;left:25px;"/>+</div>    
            
                <div style="position:absolute; top:290px;left:25px;">    
                    <p class="legendaCodigo MargemSuperior0" >Vr. em Débito</p>
                    <input type="text" name="vrDebito" id="vrDebito" class="campoSemTamanho alturaPadrao" onfocus="atualizaValores('vrDebito')" style="width:120px;text-align:right;" >
                </div>  
                <div style="position:absolute;top:313px;left:25px;"/>+</div>    
    
                <div style="border-bottom:1px solid black;position:absolute;top:342px;left:22px;width:160px;"/></div>    
            
                <div style="position:absolute; top:345px;left:25px;">    
                    <p class="legendaCodigo MargemSuperior0" >Vr. Total Pagar</p>
                    <input type="text" name="vrTotal" id="vrTotal" class="campoSemTamanhoNegrito alturaPadrao" readonly style="width:120px;text-align:right;" >
                </div>  
                <div style="position:absolute;top:370px;left:25px;"/>=</div>    
                
            </fieldset>
        </div>  
           
        <div style="position:absolute; top:330px;left:5px;">    
            <fieldset class="field-set legendaFrame recuoPadrao" style="width:550px;height: 240px">
                <div style="width:545px;overflow:auto;height: 220px">    
                    <table id="tabelaMovimento" style="background:#fff">
                       <thead>
                        <tr class="odd">
                            <th>Produtos e Serviços</th>
                            <th>Qtd.</th>
                            <th>Valor</th>
                            <th align="center">Excluir</th>
                        </tr>
                       </thead>
                       <tbody>
                       </tbody>
                    </table>
                </div>  
            </fieldset>
        </div>  
                
        <div style="position:absolute; top:580px;left:20px;width:110px;overflow: hidden">
            <input type="button" class="botaocheques" id="botaocheques"  style="margin-left: -25px;" onclick="cheques()" value=" " />
        </div>        
        <div style="position:absolute; top:580px;left:145px;width:110px;overflow: hidden">
            <input type="button" class="botaosair" id="botaosair" style="margin-left: -25px;" onclick="sair()" value=" " />
        </div>        
        <div style="position:absolute; top:580px;left:270px;width:110px;overflow: hidden">
            <input type="button" class="botaocancelar" id="botaocancelar" style="margin-left: -25px;margin-top:0px" onclick="cancelar()" value=" " />
        </div>        
        <div style="position:absolute; top:580px;left:395px;width:110px;overflow: hidden">
            <input type="button" class="botaoatualizar" id="botaoatualizar" style="margin-left: -25px;" onclick="atualizar()" value=" " />
        </div>        
        <div style="position:absolute; top:580px;left:530px;width:110px;overflow: hidden">
            <input type="button" class="botaoobservacoes" id="botaoobservacoes" style="margin-left: -25px;" onclick="observacoes()" value=" " />
        </div>        
        <div style="position:absolute; top:580px;left:655px;width:110px;overflow: hidden">
            <input type="button" class="botaofechar" id="botaofechar" style="margin-left: -25px;" onclick="fechar()" value=" " />
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

                <table class="fmt" align="left" >
                    <tr>
                        <td>
                            <p class="legendaCodigo MargemSuperior0" >Categoria</p>
                            <input type="text" id="categoriaSocio" name="categoriaSocio" class="campoSemTamanho alturaPadrao larguraNumero" value="">
                        </td>

                        <td>
                            <p class="legendaCodigo MargemSuperior0" >Matricula</p>
                            <input type="text" id="matriculaSocio" name="matriculaSocio" class="campoSemTamanho alturaPadrao larguraNumero" value="">
                        </td>
                        <td>
                            <p class="legendaCodigo MargemSuperior0" >Nome</p>
                            <input type="text" id="nomeSocio" name="nomeSocio" class="campoSemTamanho alturaPadrao" style="width:300px" value="">
                        </td>
                        <td >    
                            <input type="button" class="botaobuscainclusao" style="margin-top:20px" onclick="carregaSocio()" value="" title="Consultar" />
                        </td>
                    </tr>
                </table>
                <br><br><br>
                    <table id="tabelaSocio" align="left" style="margin-left:15px;">
                        <thead>
                        <tr class="odd">
                            <th scope="col" class="nome-lista">Nome</th>
                            <th scope="col" align="left">Título</th>
                            <th scope="col" align="left">Tp Cadastro</th>
                            <th scope="col" align="left">Categoria</th>
                            <th scope="col" align="left">Tp Categoria</th>
                            <th scope="col" align="left">Carteira</th>
                            <th scope="col" align="left">Autorização</th>
                            <th scope="col" align="left">Foto</th>
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
                                         NOVA PESSOA
            ************************************************************************
            ************************************************************************
            ************************************************************************
        -->
        <div id="divNovaPessoa" style="overflow:auto;left: 25%;top: 20%;position: fixed;width: 490px; height:400px;">
            <table style="background:#fff">
                <tr>
                  <td>
                    <div class="divisoria"></div>
                    <div id="titulo-subnav">Pessoa</div>
                    <div class="divisoria"></div>

                    <table class="fmt" align="left" >
                        <tr>
                            <td>
                                <p class="legendaCodigo MargemSuperior0" >Nome</p>
                                <input type="text" id="nomePessoaNova" name="nomePessoaNova" class="campoSemTamanho alturaPadrao" style="width:400px;" value="">
                            </td>
                        </tr>
                    </table>
                    <br><br><br>
                    <input style="margin-left:15px;margin-top:05px;" type="button" id="cmdCancelar" name="cmdCancelar" class="botaoatualizar" onclick="atualizaNovaPessoa()"  />
                    <input style="margin-left:15px;" type="button" id="cmdAtualizar" name="cmdAtualizar" class="botaocancelar" onclick="cancelaNovaPessoa()" />
                </td>
              </tr>
            </table>
        </div>
    </form>

 </body>
</html>
