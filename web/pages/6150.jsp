



<%@include file="head.jsp"%>

<style>
    table {border:none;width:0;padding:0px;margin-left:0em;margin-top:0em;}
    tr {border:none;background:none;padding:0px;margin:0em auto;}
    td {border:none;background:none;padding:0px;margin:0em auto;}
</style>  
  

<body class="internas">
            
    <%@include file="menuCaixa.jsp"%>
    
    <div class="divisoria"></div>
    <c:if test='${app == "6151"}'>
        <div id="titulo-subnav">Inclusão de Centro de Custo</div>
    </c:if>
    <c:if test='${app == "6152"}'>
        <div id="titulo-subnav">Alteração de Centro de Custo</div>
    </c:if>
    <div class="divisoria"></div>
    
    <script type="text/javascript" LANGUAGE="JavaScript" SRC="js/calendario.js"></script>
    <link rel="stylesheet" type="text/css" href="css/calendario.css"/>

    <script type="text/javascript" language="JavaScript" src="js/format.js"></script>
    
    <script type="text/javascript" language="JavaScript">

        function validarForm(){

            if(document.forms[0].descricao.value == ''){
                alert('A descrição deve ser informada!');
                return;
            }
            document.forms[0].submit();
        }

    </script>

    <form action="c" method="POST">
        <input type="hidden" name="app" value="${app}">
        <input type="hidden" name="acao" value="gravar">
        <input type="hidden" name="id" value="${item.id}">

        <table>
            <tr>
                <td>
                    <p class="legendaCodigo MargemSuperior0" >Descrição</p>
                    <input type="text" name="descricao" class="campoSemTamanho alturaPadrao"  style="width:272px;" value="${item.descricao}">
                </td>
                <td>
                    <p class="legendaCodigo MargemSuperior0">Taxa</p>
                    <div class="selectheightnovo">
                        <select name="taxa" class="campoSemTamanho alturaPadrao" style="width:200px;" >
                            <option value="0" selected>&lt;NENHUMA&gt;</option>
                            <c:forEach var="tx" items="${taxas}">
                                <option value="${tx.id}" <c:if test="${item.taxa eq tx.id}">selected</c:if>>${tx.descricao}</option>
                            </c:forEach>
                        </select>
                    </div>        
                    
                </td>
            </tr>
        </table>
        
        <input type="button" class="botaoatualizar"  onclick="validarForm()" value=" " />
        <input type="button" class="botaoVoltar"  onclick="window.location='c?app=6150';" value=" " />

    </form>

</body>
</html>
