<%@taglib prefix="sql" uri="http://java.sun.com/jsp/jstl/sql"%>
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
        $("#pesqSocio").hide();
        $("#divValMulta").hide();

        
        $('#tabela tr:gt(0)').css('background', 'white');

        $('#tabela tr:gt(0)').hover(function() {
                $(this).css('background','#f4f9fe');
        }, function() {
                $(this).css('background','white');
        })
    });     

  function pesquisaSocio() {
        var tabela = $('#tabelaSocio').find('tbody').empty();
        $("#nomeSocio").val('');
        $("#matriculaSocio").val('');
        $("#categoriaSocio").val('');
        $("#pesqSocio").show();
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
    
    function validarForm(){

        if(document.forms[0].sacado.value == ''){
            alert('Informe o Sócio!');
            document.forms[0].sacado.focus();
            return false;
        }
        
        if(isNaN(document.forms[0].qtToalha.value)){
            alert('Informe corretamente a quantidade de toalhas!');
            document.forms[0].qtToalhas.focus();
            return false;
        }
        
        if(document.forms[0].armario.value == ''){
            if(document.forms[0].qtToalha.value == '' || document.forms[0].qtToalha.value == 0){
                alert('Informe corretamente a quantidade de toalhas!');
                document.forms[0].qtToalhas.focus();
                return false;
            }
        }
        
        document.forms[0].submit();

    }
    
    function cancelaMulta(){
        $("#divValMulta").hide();
    }
    
    function atualizaMulta(){
        var valor = parseFloat($('#valorMulta').val().replace('.', '').replace(',', '.'));
        if (valor == 0 || isNaN(valor)) {
            alert('Informe corretamente o Valor da Multa!');
            mostraBotoes()
            return false;
        }
        
        $("#divValMulta").hide();
        $("#valorMultaParm").val($("#valorMulta").val());
        
        submeteDevolucao();
    }
    
    function validaDevolucao(){
        if($('#gerarMulta').attr('checked')){
            $("#divValMulta").show();
            return;
        }
        
        submeteDevolucao();
    }
    
    function submeteDevolucao(){
        
        $('#acao').val('devolver');
        
        if($("#id").val() == ''){
            //devolucao de armario com ou sem toalha
            if (confirm('Deseja realizar a devolução do armário e da(s) toalha(s), caso hajam?')){
                $('#tipoDevolucao').val('tudo');
                document.forms[0].submit();
            }else{
                if (confirm('Deseja realizar a devolução APENAS do armário?')){
                    $('#tipoDevolucao').val('armario');
                    document.forms[0].submit();
                }
            }
        }else{
            //devolucao so de toalha
            document.forms[0].submit();
        }
        
    }

    function validaDevolucaoToalha(id){
        
        $("#id").val(id);
        
        if ($('#gerarMulta'+id).attr('checked') == 'checked'){
            $("#divValMulta").show();
            return;
        }else{
            submeteDevolucao();
        }
       
    }
    
</script>

<body class="internas">
            
    <%@include file="menu.jsp"%>
    
    <c:choose>
       <c:when test='${armario.id gt 0}'>
            <div class="divisoria"></div>
            <div id="titulo-subnav">Empréstimo de Armários</div>
            <div class="divisoria"></div>
            
            <div class="divisoria"></div>
            <div id="titulo-subnav">Armário ${armario.descricao}</div>
            <div class="divisoria"></div>
       </c:when>
       <c:otherwise>
            <div class="divisoria"></div>
            <div id="titulo-subnav">Empréstimo de Kit Toalha</div>
            <div class="divisoria"></div>
       </c:otherwise>
    </c:choose>
    
    
    <form  action="c" method="POST">
        <input type="hidden" name="app" value="2681">
        <input type="hidden" name="acao" id="acao" value="gravar">
        <input type="hidden" name="tipoDevolucao" id="tipoDevolucao" value="">
        <input type="hidden" name="armario" id="armario" value="${armario.id}">
        <input type="hidden" name="idEmprestimo" id="idEmprestimo" value="${emprestimo.id}">
        <input type="hidden" name="id" id="id" value="${emprestimo.id}">
        <input type="hidden" name="matricula" id="matricula" value="">
        <input type="hidden" name="categoria" id="categoria" value="">
        <input type="hidden" name="dependente" id="dependente" value="">
        <input type="hidden" name="valorMultaParm" id="valorMultaParm" value="">
        

        <table align="left" class="fmt">
            
            <!--eh uma devolucao, soh mostra o campo do nome ja preenchido com o valor do emprestimo-->
            <c:if test='${emprestimo.id gt 0}'>
                <td>
                    <fmt:formatNumber var="titulo" value='${emprestimo.pessoa.matricula}' pattern='0000'/>
                    <fmt:formatNumber var="categoria" value='${emprestimo.pessoa.idCategoria}' pattern='00'/>

                    <p class="legendaCodigo MargemSuperior0" >Sócio</p>
                    <input type="text" id="sacado" name="sacado" class="campoSemTamanho alturaPadrao" readonly style="width: 280px" value="${categoria}/${titulo} - ${emprestimo.pessoa.nome}">
                    <br><br>
                    
                    <c:if test='<%=request.isUserInRole("2685")%>'>
                        <input type="checkbox" style="margin-left: 15px" name="gerarMulta" id="gerarMulta" value="true">Gerar Multa
                    </c:if>
                    
                </td>
            </c:if>
                
            <!--eh um emprestimo novo, mostra todos os campos-->
            <c:if test='${emprestimo.id == null}'>
                <td>
                    <p class="legendaCodigo MargemSuperior0" >Sócio</p>
                    <input type="text" id="sacado" name="sacado" class="campoSemTamanho alturaPadrao" readonly style="width: 280px">
                </td>
                <td>
                    <input class="botaobuscainclusao" style="margin-top:20px" type="button" onclick="pesquisaSocio()" value="" title="Consultar" />
                </td>
                <td >
                    <p class="legendaCodigo MargemSuperior0">Qt. Kit</p>
                    <input type="text" name="qtToalha" id="qtToalha" class="campoSemTamanho alturaPadrao" style="width: 70px">
                </td>
            </c:if>
        </table>          
          
        <br><br><br><br><br><br>
        
        <c:choose>
           <c:when test='${emprestimo.id gt 0}'>
               <input type="button" class="botaoDevolucao"  onclick="validaDevolucao()" value=" " />
           </c:when>
           <c:otherwise>
               <input type="button" class="botaoatualizar"  onclick="validarForm()" value=" " />
           </c:otherwise>
        </c:choose>
        
        
        <input type="button" class="botaoVoltar"  onclick="window.location='c?app=2680'" value=" " />

    </form>

    <!--Se for um emprestimo de toalhas ou clicou em algum armario para devolucao, mostra a lista de itens-->
    <c:if test='${armario.id == null || emprestimo.id gt 0}'>
        <br>
        
        <div class="divisoria"></div>
        <div id="titulo-subnav">Toalhas Pendentes de Devolução</div>
        <div class="divisoria"></div>
        
        
        <sql:query var="rs" dataSource="jdbc/iate">
            SELECT 
                    T3.NOME_PESSOA,
                    T3.CD_MATRICULA,
                    T4.DESCR_CATEGORIA,
                    T1.DT_EMPRESTIMO,
                    T2.NU_SEQ_ITEM_EMPRESTIMO
            FROM
                    TB_EMPRESTIMO_SAUNA T1,
                    TB_ITEM_EMPRESTIMO_SAUNA T2,
                    TB_PESSOA T3,
                    TB_CATEGORIA T4
            WHERE
                    T1.NU_SEQ_EMPRESTIMO = T2.NU_SEQ_EMPRESTIMO AND
                    T1.CD_MATRICULA = T3.CD_MATRICULA AND
                    T1.CD_CATEGORIA = T3.CD_CATEGORIA  AND
                    T1.SEQ_DEPENDENTE = T3.SEQ_DEPENDENTE AND
                    T3.CD_CATEGORIA = T4.CD_CATEGORIA AND
                    T2.CD_ITEM = 1 AND
                    T2.CD_SITUACAO = 'N'
                    
            <c:if test='${emprestimo.id gt 0}'>
                AND EXISTS (SELECT 1 FROM TB_ITEM_EMPRESTIMO_SAUNA T0 WHERE T1.NU_SEQ_EMPRESTIMO = T0.NU_SEQ_EMPRESTIMO AND T0.NU_SEQ_EMPRESTIMO = ${emprestimo.id})
            </c:if>
                
            ORDER BY T3.NOME_PESSOA, T1.DT_EMPRESTIMO

        </sql:query>    
    
    
        <table id="tabela">
            <thead>
            <tr class="odd">
                <th scope="col" class="nome-lista">Nome</th>
                <th scope="col" align="center">Título</th>
                <th scope="col" align="center">Categoria</th>
                <th scope="col" align="center">Dt. Emprestimo</th>
                <th scope="col" >Devolver</th>
            </tr>	
            </thead>
            <tbody>

            <c:forEach var="row" items="${rs.rows}">
                <tr height="1">
                    <th class="column1" align="left">${row.NOME_PESSOA}</th>
                    <th class="column1" align="center">${row.CD_MATRICULA}</th>
                    <th class="column1" align="center">${row.DESCR_CATEGORIA}</th>
                    
                    <fmt:formatDate var="dtEmp" value='${row.DT_EMPRESTIMO}' pattern='dd/MM/yyyy HH:mm:ss'/>
                    <th class="column1" align="center">${dtEmp}</th>

                    <th class="column1" align="center">
                        <a href="#" onclick="validaDevolucaoToalha(${row.NU_SEQ_ITEM_EMPRESTIMO});"><img src="imagens/icones/inclusao-titular-03.png"/></a>
                        
                        <c:if test='<%=request.isUserInRole("2685")%>'>
                            <input type="checkbox" style="margin-left: 15px;vertical-align: middle" name="gerarMulta${row.NU_SEQ_ITEM_EMPRESTIMO}" id="gerarMulta${row.NU_SEQ_ITEM_EMPRESTIMO}" value="true">Gerar Multa
                        </c:if>
                        
                        
                    </th>

                </tr>	

            </c:forEach>

            </tbody>
        </table>
    
    </c:if>                
   
        
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

    
    <!--
        ************************************************************************
        ************************************************************************
        ************************************************************************
                                    VALOR DA MULTA
        ************************************************************************
        ************************************************************************
        ************************************************************************
    -->

    <div id="divValMulta" style="overflow:auto;left: 25%;top: 20%;position: fixed;width: 500px; height:400px;">
        <table style="background:#fff">
            <tr>
                <td>
                    <div class="divisoria"></div>
                    <div id="titulo-subnav">Multa</div>
                    <div class="divisoria"></div>

                    <table class="fmt" align="left" >
                        <tr>
                            <td>
                                <p class="legendaCodigo MargemSuperior0">Taxa</p>
                                <div class="selectheightnovo">
                                    <select name="taxa" id="taxa" class="campoSemTamanho alturaPadrao" style="width:320px;" onchange="trocaTaxa()">
                                        <c:forEach var="tx" items="${taxas}">
                                            <option value="${tx.id}" <c:if test='${tx.id == taxa}'>selected</c:if>>${tx.descricao}</option>
                                        </c:forEach>
                                    </select>
                                </div>        
                            </td>
                            <td>
                                <p class="legendaCodigo MargemSuperior0" >Valor</p>
                                <input type="text" id="valorMulta" name="valorMulta" class="campoSemTamanho alturaPadrao larguraNumero" value="">
                            </td>

                        </tr>
                    </table>
                    
                    <br><br><br><br>
                    <input style="margin-left:15px;margin-top: 5px" type="button" id="cmdatualizar" name="cmdatualizar" class="botaoatualizar" onclick="atualizaMulta()" />
                    <input style="margin-left:15px;" type="button" id="cmdcancelar" name="cmdPesquisa" class="botaocancelar" onclick="cancelaMulta()" />
                </td>
            </tr>
        </table>
    </div>                
</body>
</html>
