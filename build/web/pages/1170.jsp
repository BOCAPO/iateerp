<%@include file="head.jsp"%>

<style>
    table {border:none;width:0;padding:0px;margin-left:0em;margin-top:0em;}
    tr {border:none;background:none;padding:0px;margin:0em auto;}
    td {border:none;background:none;padding:0px;margin:0em auto;}
</style>  

<body class="internas">
            
    <%@include file="menu.jsp"%>
    
    <div class="divisoria"></div>
        <div id="titulo-subnav">Registrar Ocorrência para Sócio</div>
    <div class="divisoria"></div>
    
    <script type="text/javascript" language="JavaScript" src="js/format.js"></script>
    
    <script type="text/javascript" language="JavaScript">
        $(document).ready(function () {
                $('#dataInicio').mask('99/99/9999');
                $('#dataFim').mask('99/99/9999');

        }); 
        
        function validarForm(){

            if(!isDataValida(document.forms[0].dataInicio.value)){
                return;
            }
            if(!isDataValida(document.forms[0].dataFim.value)){
                return;
            }            

            document.forms[0].submit();
        }

    </script>

    <form action="c" method="POST">
        <input type="hidden" name="app" value="${app}">
        <input type="hidden" name="acao" value="gravar">
        <input type="hidden" name="idOcorrencia" value="${ocorrencia.id}">
        <input type="hidden" name="matricula" value="${socio.matricula}">
        <input type="hidden" name="idCategoria" value="${socio.idCategoria}">
        <input type="hidden" name="seqDependente" value="${socio.seqDependente}">
        
        <table class="fmt" align="left" >
            <tr>
              <td>
                <p class="legendaCodigo MargemSuperior0">Tipo da Ocorrência</p>
                <select name="idTipoOcorrencia" class="campoSemTamanho alturaPadrao" style="width:235px">
                    <c:forEach var="tipo" items="${tipos}">
                        <option value="${tipo.id}" <c:if test='${ocorrencia.tipo.id == tipo.id}'>selected</c:if>>${tipo.descricao}</option>
                    </c:forEach>
                </select>
              </td>
              <td>
                <p class="legendaCodigo MargemSuperior0" >Dt. Início</p>
                <fmt:formatDate var="dataInicio" value="${ocorrencia.dataInicio}" pattern="dd/MM/yyyy"/>
                <input type="text" name="dataInicio" id="dataInicio" class="campoSemTamanho alturaPadrao larguraData" value="${dataInicio}">
              </td>
              <td>
                <p class="legendaCodigo MargemSuperior0" >Dt. Fim</p>
                <fmt:formatDate var="dataFim" value="${ocorrencia.dataFim}" pattern="dd/MM/yyyy"/>        
                <input type="text" name="dataFim" id="dataFim" class="campoSemTamanho alturaPadrao larguraData" value="${dataFim}">
              </td>
            </tr>
            <tr>
              <td colspan="4">
                <p class="legendaCodigo MargemSuperior0" >Descrição:</p>
                <input type="text" class="campoSemTamanho alturaPadrao" style="width:475px" name="descricao" value="${ocorrencia.descricao}"><br>    
              </td>
            </tr>
        </table>   
        
        <br><br><br><br><br><br>
        <input type="button" class="botaoatualizar"  onclick="validarForm()" value=" " />
        <input type="button" class="botaoVoltar"  onclick="window.location='c?app=1170&matricula=${socio.matricula}&idCategoria=${socio.idCategoria}&seqDependente=${socio.seqDependente}';" value=" " />        
    </form>

</body>
</html>
