<%@include file="head.jsp"%>

<style type="text/css">
    table.fmt {border:none;width:0;padding:0px;margin-left:0em;margin-top:0em;}
    table.fmt tr td {border:none;background:none;padding:0px;margin:0em auto;}
</style>  

<script type="text/javascript" LANGUAGE="JavaScript" SRC="js/calendario.js"></script>
<link rel="stylesheet" type="text/css" href="css/calendario.css"/>

<script type="text/javascript" language="JavaScript" src="js/format.js"></script>
    
<script type="text/javascript" language="JavaScript">
    $(document).ready(function () {
        $("#dtVencimento").mask("99/99/9999");
        
        $("#pesqSocio").hide();
        });     

  function pesquisaSocio() {
        if($("#tipo").val() == 'S'){
            $("#pesqSocio").show();
            var tabela = $('#tabelaSocio').find('tbody').empty();
            $("#nomeSocio").val('');
            $("#matriculaSocio").val('');
            $("#categoriaSocio").val('');
        }else{
            alert('Pesquisa apenas para sócios!');
            return false;
        }
    }
    function cancelaPesquisaSocio() {
        $("#pesqSocio").hide();
    }        
    function selecionaSocio(indice){
        $("#pesqSocio").hide();
        $("#tabelaSocio tr:eq("+(parseInt(indice)+1)+")").each(function(index){
            $('#matricula').val($(this).find('#campoTitulo').val().substring(0, 4));
            $('#categoria').val($(this).find('#campoTitulo').val().substring(4, 6));
            $('#dependente').val($(this).find('#campoTitulo').val().substring(6, 8));
            
            $('#sacado').val($(this).find('#nome2').val());
            
            
            
        });
    }

    function carregaSocio(lugar){
        $.ajax({url:'ReservaLugarEventoAjax', type:'GET',
                            data:{
                            nome:$('#nomeSocio').val(),
                            matricula:$('#matriculaSocio').val(),
                            categoria:$('#categoriaSocio').val()
                            }
        }).success(function(retorno){
            if (retorno.erro !== undefined){
                alert(retorno.erro);
            }else {
                var tabela = $('#tabelaSocio').find('tbody').empty();
                var linha="";
                var campoHidden="";
                var campoHidden2="";
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

                    campoHidden='<input type="hidden" id="campoTitulo"  value="'+this.titulo+'"/>'
                    campoHidden2='<input type="hidden" id="nome2"  value="'+this.nome2+'"/>'
                    linha='<tr><td class="column1"><a href="#" id="tabNome" class="column1" onclick="selecionaSocio('+i+')">'+this.nome+'</a> '+campoHidden2+'</td><td class="column1" align="center">'+this.matricula+' '+campoHidden+'</td><td class="column1">'+tpCadastro+'</td><td class="column1">'+this.descricao+'</td><td class="column1">'+tpCategoria+'</td></tr>';
                    tabela.append(linha)
                });
            }
        });    
    }
    
    function LimpaNomeRetirante(){
        $('#sacado').val('');
        $('#matricula').val('');
        $('#categoria').val('');
        $('#dependente').val('');
        
        if($("#tipo").val() == 'S'){
            $('#sacado').attr('readonly', true);
        }else{
            $('#sacado').attr('readonly', false);
        }
    }


    function validarForm(){

        if(document.forms[0].sacado.value == ''){
            alert('Informe o Sacado!');
            document.forms[0].sacado.focus();
            return false;
        }
        
        if(document.forms[0].dtVencimento.value == ''){
            alert('Informe a Data de Vencimento!');
            document.forms[0].dtVencimento.focus();
            return false;
        }
        if(!isDataValida(document.forms[0].dtVencimento.value)){
            return;
        }
        
        var valor = parseFloat($('#valor').val().replace('.', '').replace(',', '.'));
        if (valor == 0 || isNaN(valor)) {
            alert('Informe o Valor!');
            mostraBotoes()
            return false;
        }
        
        document.forms[0].submit();

    }
    
</script>

<body class="internas">
            
    <%@include file="menu.jsp"%>
    
    <div class="divisoria"></div>
    <div id="titulo-subnav">Inclusão de Boleto Avulso</div>
    <div class="divisoria"></div>
    
    <form  action="c" method="POST">
        <input type="hidden" name="app" value="${app}">
        <input type="hidden" name="acao" value="gravar">
        <input type="hidden" name="matricula" id="matricula" value="">
        <input type="hidden" name="categoria" id="categoria" value="">
        <input type="hidden" name="dependente" id="dependente" value="">

        <table align="left" class="fmt">
            <td>
                <p class="legendaCodigo MargemSuperior0" >Tipo</p>
                <div class="selectheightnovo">
                  <select name="tipo" id="tipo" class="campoSemTamanho alturaPadrao" onchange="LimpaNomeRetirante()" style="width:80px;" >
                    <option value="S">Sócio</option>
                    <option value="O">Outros</option>
                  </select>
                </div>
            </td>
            <td>
                <p class="legendaCodigo MargemSuperior0" >Sacado</p>
                <input type="text" id="sacado" name="sacado" class="campoSemTamanho alturaPadrao" readonly style="width: 280px" >
            </td>
            <td>
                <input class="botaobuscainclusao" style="margin-top:20px" type="button" onclick="pesquisaSocio()" value="" title="Consultar" />
            </td>
            <td>
              <p class="legendaCodigo MargemSuperior0" >Taxa</p>
              <div class="selectheightnovo">
                <select name="taxa" id="setor" class="campoSemTamanho alturaPadrao" style="width: 230px">
                  <c:forEach var="taxa" items="${taxas}">
                      <option value="${taxa.id}">${taxa.descricao}</option>
                  </c:forEach>
                </select>
              </div>
            </td>
            <td>
              <p class="legendaCodigo MargemSuperior0" >Dt. Vencimento</p>
              <input type="text" name="dtVencimento" id="dtVencimento" class="campoSemTamanho alturaPadrao" style="width: 90px">
            </td>
            <td >
                <p class="legendaCodigo MargemSuperior0">Valor</p>
                <input type="text" name="valor" id="valor" class="campoSemTamanho alturaPadrao" style="width: 70px">
            </td>
        </table>          
          

        <br><br><br><br>
        <input type="button" class="botaoatualizar"  onclick="validarForm()" value=" " />
        <input type="button" class="botaoVoltar"  onclick="window.location='c?app=2430'" value=" " />

    </form>

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

</body>
</html>
