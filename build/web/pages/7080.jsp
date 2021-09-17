<%@include file="head.jsp"%>
<script type="text/javascript" language="JavaScript">
    function validaSelecao(){
        selecao = new Array();
        
        $("input:checkbox").each(function(){
            if (this.checked == true) {
		selecao.push($(this).val());
            }            
            
        });
        var tamanho = selecao.length;
        
        if (tamanho==0){
            alert('Selecione pelo menos uma mensagem!');
            return false;
        }
        
        document.forms[0].submit();
    }
    
</script>  


<body class="internas">

    <%@include file="menuAcesso.jsp"%>

    <div class="divisoria"></div>
    <div id="titulo-subnav">Relatório de Mensagens</div>
    <div class="divisoria"></div>
        
    <form method="POST" action="c">
    <input type="hidden" name="app" value="7080">
    <input type="hidden" name="acao" value="imprime">

    <p class="legendaCodigo">Mensagens</p>
    <div class="recuoPadrao" style="overflow:auto;height:300px;width:400px;">
        <c:forEach var="mensagem" items="${mensagens}">
            <input type="checkbox" name="idMensagem" value="${mensagem.id}" >${mensagem.descricao}<br>
        </c:forEach>
    </div>
    <br>
    <input type="checkbox" class="recuoPadrao" name="soAtivas" value="SA" >Somente Ativas<br>
    <input type="submit" class="botaoimprimir" onclick="return validaSelecao()"  value=" " />
    
</form>

</body>
</html>
