<%@include file="head.jsp"%>

<style>
    table {border:none;width:500px;padding:0px;margin-left:0em;margin-top:0em;}
    tr {border:none;background:none;padding:0px;margin:0em auto;}
    td {border:none;background:none;padding:0px;margin:0em auto;}

    .larguraTipo{
        width:200px; 
    }
</style>  

<body class="internas">
    
    <script type="text/javascript">
        function onlyNumber(evt) {
            if(evt.which !== 0 && evt.which !== 8 && (evt.which < 48 || evt.which > 57))
                    evt.preventDefault();
        }
    </script>
    
    <%@include file="menu.jsp"%>
    
    <div class="divisoria"></div>
    <c:if test="${app == 2341}">
        <div id="titulo-subnav">Inclusão de Quadra de Tênis</div>
    </c:if>
    <c:if test="${app == 2342}">
        <div id="titulo-subnav">Alteração de Quadra de Tênis</div>
    </c:if>
    <div class="divisoria"></div>
    
    <form name="quadraForm" action="c" method="POST">
        <input type="hidden" name="app" value="${app}" />
        <input type="hidden" name="id" value="${id}" />

        <table align="left" >
            <tr>
              <td width="20%">
                  <p class="legendaCodigo MargemSuperior0">Nome</p>
                  <input type="text" name="nome" class="campoSemTamanho alturaPadrao larguraCidade" value="${quadra.nome}" maxlength="20"/>
              </td>
              <td width="20%">
                  <p class="legendaCodigo MargemSuperior0">Minutos Simples</p>
                  <input type="text" name="minutosSimples" class="campoSemTamanho alturaPadrao larguraData" maxlength="4" value="${quadra.duracaoSimples}" onkeypress="onlyNumber(event);" />
              </td>
              <td width="20%">
                  <p class="legendaCodigo MargemSuperior0">Minutos Duplas</p>
                  <input type="text" name="minutosDuplas" class="campoSemTamanho alturaPadrao" maxlength="4" value="${quadra.duracaoDuplas}" onkeypress="onlyNumber(event);" />
              </td>
              <td width="20%">
                  <p class="legendaCodigo MargemSuperior0">Segundos Marcação</p>
                  <input type="text" name="segundosMarcacao" class="campoSemTamanho alturaPadrao" maxlength="4" value="${quadra.duracaoMarcacao}" onkeypress="onlyNumber(event);" />
              </td>
            </tr>
        </table>          
                
        <BR><BR><BR><br>
        
        <input type="button" class="botaoatualizar"  onclick="document.quadraForm.submit()" value="" />
        <input type="button" class="botaoVoltar"  onclick="window.location='c?app=2340';" value="" />
</form>

</body>
</html>
