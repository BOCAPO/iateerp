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
        <div id="titulo-subnav">Inclusão de Box</div>
    </c:if>
    <c:if test='${app == "2002"}'>
        <div id="titulo-subnav">Alteração de Box</div>
    </c:if>
    <c:if test='${app == "2000"}'>
        <div id="titulo-subnav">Detalhes do Box</div>
    </c:if>
    <div class="divisoria"></div>
    
    <script type="text/javascript" LANGUAGE="JavaScript" SRC="js/calendario.js"></script>
    <link rel="stylesheet" type="text/css" href="css/calendario.css"/>

    <script type="text/javascript" language="JavaScript" src="js/format.js"></script>
    
    <script type="text/javascript" language="JavaScript">

        function validarForm(){

            if(document.forms[0].box.value == ''){
                alert('O Número do Box deve ser informado!');
                return;
            }
            if(document.forms[0].desconto.value > 100){
                alert('Desconto deve ser menor ou igual a 100%!');
                return;
            }
            if(!isDataValida(document.forms[0].dataCadastro.value)){
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
        <input type="hidden" name="ehBox" value="S">

        <table>
            <tr>
                <td>
                    <p class="legendaCodigo MargemSuperior0" >N° Box</p>
                    <input type="text" class="campoSemTamanho alturaPadrao larguraNumero" onkeypress="onlyNumber(event)" name="box"  <c:if test='${app == "2000"}'>readonly</c:if> value="${barco.box}">    

                </td>
                <td>
                    <p class="legendaCodigo MargemSuperior0">Local:</p>
                    <div class="selectheightnovo">
                        <select name="idLocal" class="campoSemTamanho alturaPadrao" style="width:180px;" <c:if test='${app == "2000"}'>disabled</c:if> >
                            <c:forEach var="local" items="${locais}">
                                <option value="${local.id}" <c:if test='${barco.localBox.id == local.id}'>selected</c:if>>${local.descricao}</option>
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
                <td>
                    <p class="legendaCodigo MargemSuperior0" >Dt. Cadastro</p>
                    <fmt:formatDate var="dataCadastro" value="${barco.dataCadastro}" pattern="dd/MM/yyyy"/>
                    <input type="text" name="dataCadastro" id="dataCadastro" class="campoSemTamanho alturaPadrao larguraData"  <c:if test='${app == "2000"}'>readonly</c:if> value="${dataCadastro}">
                </td>
                <td>
                    <p class="legendaCodigo MargemSuperior0" >Qt. M2</p>
                    <input type="text" class="campoSemTamanho alturaPadrao larguraNumero" onkeypress="onlyNumber(event)" name="qtM2"  <c:if test='${app == "2000"}'>readonly</c:if> value="${barco.qtM2}">  
                </td>
                <td>
                    <p class="legendaCodigo MargemSuperior0" >Qt. Geladeira</p>
                    <input type="text" class="campoSemTamanho alturaPadrao larguraNumero" onkeypress="onlyNumber(event)" name="qtGeladeira"  <c:if test='${app == "2000"}'>readonly</c:if> value="${barco.qtGeladeira}">  
                </td>
                <td>
                    <input type="checkbox" class="legendaCodigo" style="margin-top: 25px;" name="emprestimoChave" value="1" <c:if test='${app == "2000"}'>disabled</c:if> <c:if test='${barco.emprestimoChave}'>checked</c:if>><spam class="legendaSemMargem larguraData">Emprestimo de Chave</spam>
                </td>
            </tr>
        </table>
        
         <c:if test='${app != "2000"}'>
             <input type="button" class="botaoatualizar"  onclick="validarForm()" value="" />
         </c:if> 
             
         <input type="button" class="botaoVoltar"  onclick="window.location='c?app=2000&acao=showForm&matricula=${socio.matricula}&idCategoria=${socio.idCategoria}&seqDependente=0';" value="" />

    </form>

</body>
</html>
