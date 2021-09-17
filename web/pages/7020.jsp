<%@include file="head.jsp"%>

<style>
    table {border:none;width:0;padding:0px;margin-left:0em;margin-top:0em;}
    tr {border:none;background:none;padding:0px;margin:0em auto;}
    td {border:none;background:none;padding:0px;margin:0em auto;}
</style>  
  

<body class="internas">
            
    <%@include file="menuAcesso.jsp"%>
    
    <div class="divisoria"></div>
    <c:if test='${app == "7021"}'>
        <div id="titulo-subnav">Inclusão de Tipo de Evento de Acesso</div>
    </c:if>
    <c:if test='${app == "7022"}'>
        <div id="titulo-subnav">Alteração de Tipo de Evento de Acesso</div>
    </c:if>
    <div class="divisoria"></div>
    
    <script type="text/javascript" LANGUAGE="JavaScript" SRC="js/calendario.js"></script>
    <link rel="stylesheet" type="text/css" href="css/calendario.css"/>

    <script type="text/javascript" language="JavaScript" src="js/format.js"></script>
    <script type="text/javascript" language="javascript">
    $(document).ready(function () {
            $("#dtInicio").mask("99/99/9999");
            $("#dtFim").mask("99/99/9999");
            });
    </script>
    
    <script type="text/javascript" language="JavaScript">

        function validarForm(){

            if(document.forms[0].descricao.value == ''){
                alert('A descrição deve ser informada!');
                return;
            }
            
            if($('#dtInicio').val() == ''){
               alert("Informe a data de início!");
               return false;
            }
            if(!isDataValida($('#dtInicio').val())){
                return false; 
            }
            
            if($('#dtFim').val() == ''){
               alert("Informe a data de Fim!");
               return false;
            }
            if(!isDataValida($('#dtFim').val())){
                return false; 
            }
            document.forms[0].submit();
        }

    </script>

    <form action="c" method="POST">
        <input type="hidden" name="app" value="${app}">
        <input type="hidden" name="acao" value="gravar">
        <input type="hidden" name="id" value="${tipoEvento.id}">

        <table>
            <tr>
                <td>
                    <p class="legendaCodigo MargemSuperior0" >Descrição</p>
                    <input type="text" name="descricao" class="campoSemTamanho alturaPadrao"  style="width:220px;" value="${tipoEvento.descricao}">
                </td>
                <td>
                    <p class="legendaCodigo MargemSuperior0">Situacao</p>
                    <div class="selectheightnovo">
                        <select name="situacao" class="campoSemTamanho alturaPadrao" style="width:135px;" >
                            <option value="AL" <c:if test='${tipoEvento.situacao == "AL"}'>selected</c:if>>Alerta</option>
                            <option value="PA" <c:if test='${tipoEvento.situacao == "PA"}'>selected</c:if>>Proíbe Acesso</option>
                            <option value="PP" <c:if test='${tipoEvento.situacao == "PP"}'>selected</c:if>>Proíbe Acesso Piscina</option>
                        </select>
                    </div>        
                    
                </td>
                <td>
                    <p class="legendaCodigo MargemSuperior0">Status</p>
                    <div class="selectheightnovo">
                        <select name="status" class="campoSemTamanho alturaPadrao" style="width:70px;" >
                            <option value="AT" <c:if test='${tipoEvento.status == "AT"}'>selected</c:if>>Ativo</option>
                            <option value="CA" <c:if test='${tipoEvento.status == "CA"}'>selected</c:if>>Cancelado</option>
                        </select>
                    </div>        
                    
                </td>
                <td>
                    <p class="legendaCodigo MargemSuperior0">Dt. Início</p>
                    <fmt:formatDate var="dtInicio" value="${tipoEvento.dtInicio}" pattern="dd/MM/yyyy"/>
                    <input id="dtInicio" type="text" name="dtInicio" class="campoSemTamanho alturaPadrao larguraData" value="${dtInicio}">
                    
                </td>
                <td>
                    <p class="legendaCodigo MargemSuperior0">Dt. Fim</p>
                    <fmt:formatDate var="dtFim" value="${tipoEvento.dtFim}" pattern="dd/MM/yyyy"/>
                    <input id="dtFim" type="text" name="dtFim" class="campoSemTamanho alturaPadrao larguraData" value="${dtFim}">
                    
                </td>
            </tr>
        </table>
                
        <br>
        <input type="button" class="botaoatualizar"  onclick="validarForm()" value=" " />
        <input type="button" class="botaoVoltar"  onclick="window.location='c?app=7020';" value=" " />

    </form>

</body>
</html>
