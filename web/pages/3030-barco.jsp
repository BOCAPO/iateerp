<%@include file="head.jsp"%>

<style>
    table {border:none;width:0;padding:0px;margin-left:0em;margin-top:0em;}
    tr {border:none;background:none;padding:0px;margin:0em auto;}
    td {border:none;background:none;padding:0px;margin:0em auto;}
</style>  

<script type="text/javascript" language="javascript">
    $(document).ready(function () {
        $("#dataRegistro").mask("99/99/9999");
        $("#dataCadastro").mask("99/99/9999");
        $("#dataVencimentoSeguro").mask("99/99/9999");
        $("#dataVencimentoRegistro").mask("99/99/9999");
        $("#dataVencimentoHabilitacao").mask("99/99/9999");
        $("#dtIniDesconto").mask("99/99/9999");
        $("#dtFimDesconto").mask("99/99/9999");
    });     
</script>    

<body class="internas">
            
    <%@include file="menu.jsp"%>
    
    <div class="divisoria"></div>
    <c:if test='${app == "2001"}'>
        <div id="titulo-subnav">Inclus�o de Barco</div>
    </c:if>
    <c:if test='${app == "2002"}'>
        <div id="titulo-subnav">Altera��o de Barco</div>
    </c:if>
    <c:if test='${app == "2000"}'>
        <div id="titulo-subnav">Detalhes do Barco</div>
    </c:if>
    <div class="divisoria"></div>
    
    <script type="text/javascript" LANGUAGE="JavaScript" SRC="js/calendario.js"></script>
    <link rel="stylesheet" type="text/css" href="css/calendario.css"/>

    <script type="text/javascript" language="JavaScript" src="js/format.js"></script>
    
    <script type="text/javascript" language="JavaScript">

        function validarForm(){

            if(document.forms[0].nome.value == ''){
                alert('O Nome do Barco dever ser informado!');
                return;
            }
            if(document.forms[0].numCapitania.value == ''){
                alert('O N�mero da Capitania deve ser informado!');
                return;
            }
            if(document.forms[0].pes.value == ''){
                alert('O N�mero de Pes deve ser informado!');
                return;
            }
            if(document.forms[0].nome.value == ''){
                alert('O Nome do Barco dever ser informado!');
                return;
            }
            if(document.forms[0].nome.value == ''){
                alert('O Nome do Barco dever ser informado!');
                return;
            }
            if(document.forms[0].desconto.value > 100){
                alert('Desconto deve ser menor ou igual a 100%!');
                return;
            }
            if(!isDataValida(document.forms[0].dataRegistro.value)){
                return;
            }
            if(!isDataValida(document.forms[0].dataCadastro.value)){
                return;
            }
            if(!isDataValida(document.forms[0].dataVencimentoSeguro.value)){
                return;
            }
            if(!isDataValida(document.forms[0].dataVencimentoRegistro.value)){
                return;
            }
            if(!isDataValida(document.forms[0].dataVencimentoHabilitacao.value)){
                return;
            }
            if(!isDataValida(document.forms[0].dtIniDesconto.value)){
                return;
            }
            if(!isDataValida(document.forms[0].dtFimDesconto.value)){
                return;
            }

            document.forms[0].submit();
        }

    </script>

    <form action="c" method="POST">
        <input type="hidden" name="app" value="${app}">
        <input type="hidden" name="acao" value="gravar">
        <input type="hidden" name="matricula" value="${socio.matricula}">
        <input type="hidden" name="idCategoria" value="${socio.idCategoria}">
        <input type="hidden" name="seqDependente" value="${socio.seqDependente}">
        <input type="hidden" name="idBarco" value="${barco.id}">

        <table>
            <tr>
                <td>
                    <p class="legendaCodigo MargemSuperior0" >Barco:</p>
                    <input type="text" name="nome" class="campoSemTamanho alturaPadrao"  style="width:220px;" <c:if test='${app == "2000"}'>readonly</c:if> value="${barco.nome}">
                </td>
                <td>
                    <p class="legendaCodigo MargemSuperior0">Categoria N�utica:</p>
                    <div class="selectheightnovo">
                        <select name="idCategoriaNautica" class="campoSemTamanho alturaPadrao" style="width:180px;" <c:if test='${app == "2000"}'>disabled</c:if> >
                            <c:forEach var="categoria" items="${categorias}">
                                <option value="${categoria.id}" <c:if test='${barco.categoriaNautica.id == categoria.id}'>selected</c:if>>${categoria.descricao}</option>
                            </c:forEach>
                        </select>
                    </div>        
                </td>
                <td>

                    <p class="legendaCodigo MargemSuperior0">Tipo de Vaga:</p>
                    <div class="selectheightnovo">
                        <select name="idTipoVaga" class="campoSemTamanho alturaPadrao" style="width:100px;" <c:if test='${app == "2000"}'>disabled</c:if> >
                            <c:forEach var="tipo" items="${tipos}">
                                <option value="${tipo.id}" <c:if test='${barco.tipoVaga.id == tipo.id}'>selected</c:if>>${tipo.descricao}</option>
                            </c:forEach>
                        </select>
                    </div>        
                </td>
                <td>

                    <p class="legendaCodigo MargemSuperior0" >% Desc.</p>
                    <fmt:formatNumber value="${barco.desconto}" maxFractionDigits="2" minFractionDigits="2" var="desconto"/>
                    <input type="text" class="campoSemTamanho alturaPadrao larguraNumero"  name="desconto"  <c:if test='${app == "2000"}'>readonly</c:if> value="${desconto}">    
                </td>
                <td>
                    <p class="legendaCodigo MargemSuperior0" >Dt. Ini. Desc.</p>
                    <fmt:formatDate var="dtIniDesconto" value="${barco.dtIniDesconto}" pattern="dd/MM/yyyy"/>
                    <input type="text" name="dtIniDesconto" id="dtIniDesconto" class="campoSemTamanho alturaPadrao larguraNumero"  <c:if test='${app == "2000"}'>readonly</c:if> value="${dtIniDesconto}">
                </td>
                <td>
                    <p class="legendaCodigo MargemSuperior0" >Dt. Fim Desc.</p>
                    <fmt:formatDate var="dtFimDesconto" value="${barco.dtFimDesconto}" pattern="dd/MM/yyyy"/>
                    <input type="text" name="dtFimDesconto" id="dtFimDesconto" class="campoSemTamanho alturaPadrao larguraNumero"   <c:if test='${app == "2000"}'>readonly</c:if> value="${dtFimDesconto}">
                </td>
                
            </tr>
        </table>
                
        <table>
            <tr>
                <td>
                    <p class="legendaCodigo MargemSuperior0" >N� Capitania Barco</p>
                    <input type="text" class="campoSemTamanho alturaPadrao" style="width:100px;" maxlength="14" name="numCapitania"  <c:if test='${app == "2000"}'>readonly</c:if> value="${barco.numCapitania}">   
                </td>
                <td>

                    <p class="legendaCodigo MargemSuperior0" >N� Box</p>
                    <input type="text" class="campoSemTamanho alturaPadrao larguraNumero" onkeypress="onlyNumber(event)" name="box"  <c:if test='${app == "2000"}'>readonly</c:if> value="${barco.box}">    

                </td>
                <td>
                    <p class="legendaCodigo MargemSuperior0" >N� P�s</p>
                    <input type="text" class="campoSemTamanho alturaPadrao larguraNumero" onkeypress="onlyNumber(event)" name="pes"  <c:if test='${app == "2000"}'>readonly</c:if> value="${barco.pes}">  
                </td>
                <td>
                    <p class="legendaCodigo MargemSuperior0" >Dt. Registro</p>
                    <fmt:formatDate var="dataRegistro" value="${barco.dataRegistro}" pattern="dd/MM/yyyy"/>
                    <input type="text" name="dataRegistro" id="dataRegistro" class="campoSemTamanho alturaPadrao larguraData"  <c:if test='${app == "2000"}'>readonly</c:if> value="${dataRegistro}">
                </td>
                <td>

                    <p class="legendaCodigo MargemSuperior0" >Ano Fabr.</p>
                    <input type="text" class="campoSemTamanho alturaPadrao larguraNumero" onkeypress="onlyNumber(event)" name="anoFabricacao"  <c:if test='${app == "2000"}'>readonly</c:if> value="${barco.anoFabricacao}">   
                </td>
                <td>

                    <p class="legendaCodigo MargemSuperior0" >N� Habilita��o</p>
                    <input type="text" class="campoSemTamanho alturaPadrao larguraData" name="habilitacao"  <c:if test='${app == "2000"}'>readonly</c:if> value="${barco.habilitacao}">   
                </td>
                <td>

                    <p class="legendaCodigo MargemSuperior0" >Qt. Max</p>
                    <input type="text" class="campoSemTamanho alturaPadrao larguraNumero" onkeypress="onlyNumber(event)" name="tripulantes"  <c:if test='${app == "2000"}'>readonly</c:if> value="${barco.tripulantes}">    

                </td>
                <td>
                    <p class="legendaCodigo MargemSuperior0" >Dt. Cadastro</p>
                    <fmt:formatDate var="dataCadastro" value="${barco.dataCadastro}" pattern="dd/MM/yyyy"/>
                    <input type="text" name="dataCadastro" id="dataCadastro" class="campoSemTamanho alturaPadrao larguraData"  <c:if test='${app == "2000"}'>readonly</c:if> value="${dataCadastro}">
                </td>
            </tr>
        </table>
                
        <table>
            <tr>
                <td>

                    <p class="legendaCodigo MargemSuperior0" >Classificacao</p>
                    <input type="text" class="campoSemTamanho alturaPadrao" style="width:320px;" name="classificacao"  <c:if test='${app == "2000"}'>readonly</c:if> value="${barco.classificacao}">

                </td>
                <td>
                    <p class="legendaCodigo MargemSuperior0" >Modelo</p>
                    <input type="text" class="campoSemTamanho alturaPadrao" style="width:315px;" name="modelo"  <c:if test='${app == "2000"}'>readonly</c:if> value="${barco.modelo}"> 

                </td>
                <td>
                    <p class="legendaCodigo MargemSuperior0" >Patrim�nio</p>
                    <input type="text" class="campoSemTamanho alturaPadrao larguraData" onkeypress="onlyNumber(event)" name="patrimonio"  <c:if test='${app == "2000"}'>readonly</c:if> value="${barco.patrimonio}">

                </td>
            </tr>
        </table>
                
        <table>
            <tr>
                <td>
                    <input type="checkbox" class="legendaCodigo MargemSuperior0" name="documentacaoCompleta" value="1" <c:if test='${app == "2000"}'>disabled</c:if> <c:if test='${barco.documentacaoCompleta}'>checked</c:if>><spam class="legendaSemMargem larguraData">Documenta��o Completa</spam>
                </td>
                <td>

                    <p class="legendaCodigo MargemSuperior0" >Comp. Total</p>
                    <input type="text" class="campoSemTamanho alturaPadrao" style="width:72px;" onkeypress="onlyNumber(event)" name="comprimentoTotal"  <c:if test='${app == "2000"}'>readonly</c:if> value="${barco.comprimentoTotal}">

                </td>
                <td>
                    <p class="legendaCodigo MargemSuperior0" >Comp. Boca</p>
                    <input type="text" class="campoSemTamanho alturaPadrao" style="width:72px;" onkeypress="onlyNumber(event)" name="comprimentoBoca"  <c:if test='${app == "2000"}'>readonly</c:if> value="${barco.comprimentoBoca}">

                </td>
                <td>
                    <p class="legendaCodigo MargemSuperior0" >Comp. Pontal</p>
                    <input type="text" class="campoSemTamanho alturaPadrao" style="width:72px;" onkeypress="onlyNumber(event)" name="comprimentoPontal"  <c:if test='${app == "2000"}'>readonly</c:if> value="${barco.comprimentoPontal}">  

                </td>
                <td>
                    <p class="legendaCodigo MargemSuperior0" >Venc. Seguro</p>
                    <fmt:formatDate var="dataVencimentoSeguro" value="${barco.dataVencimentoSeguro}" pattern="dd/MM/yyyy"/>
                    <input type="text" name="dataVencimentoSeguro" id="dataVencimentoSeguro" class="campoSemTamanho alturaPadrao larguraData"  <c:if test='${app == "2000"}'>readonly</c:if> value="${dataVencimentoSeguro}">
                </td>
                <td>

                    <p class="legendaCodigo MargemSuperior0" >Venc. Registro</p>
                    <fmt:formatDate var="dataVencimentoRegistro" value="${barco.dataVencimentoRegistro}" pattern="dd/MM/yyyy"/>
                    <input type="text" name="dataVencimentoRegistro" id="dataVencimentoRegistro" class="campoSemTamanho alturaPadrao larguraData"  <c:if test='${app == "2000"}'>readonly</c:if> value="${dataVencimentoRegistro}">
                </td>
                <td>

                    <p class="legendaCodigo MargemSuperior0" >Venc. Habilitacao</p>
                    <fmt:formatDate var="dataVencimentoHabilitacao" value="${barco.dataVencimentoHabilitacao}" pattern="dd/MM/yyyy"/>
                    <input type="text" name="dataVencimentoHabilitacao" id="dataVencimentoHabilitacao" class="campoSemTamanho alturaPadrao larguraData"  <c:if test='${app == "2000"}'>readonly</c:if> value="${dataVencimentoHabilitacao}">
                </td>
            </tr>
        </table>
                
        <table>
            <tr>
                <td>
                    <p class="legendaCodigo MargemSuperior0" >Observa��es</p>
                    <textarea rows="5" cols="126" name="obs" class="campoSemTamanho"  <c:if test='${app == "2000"}'>readonly</c:if>>${barco.obs}</textarea>    
                </td>
            </tr>
            
        </table>
                
        
         <c:if test='${app != "2000"}'>
             <input type="button" class="botaoatualizar"  onclick="validarForm()" value=" " />
         </c:if> 
             
         <input type="button" class="botaoVoltar"  onclick="window.location='c?app=2000&acao=showForm&matricula=${socio.matricula}&idCategoria=${socio.idCategoria}&seqDependente=0';" value=" " />

    </form>

</body>
</html>
