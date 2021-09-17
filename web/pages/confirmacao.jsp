<%@include file="head.jsp"%>

<style>
    table {border:none;width:90%;margin-left:0px;padding:0px;}
    tr {border:none;background:none;padding:0px;}
    td {border:none;background:none;padding:0px;}
</style>  

<script type="text/javascript" language="JavaScript"> 
    
    function direcionaSim(href){

        $('#lnkAtualizar').hide();
        $('#lnkCancelar').hide();
        
        location.href = href;
    }
    function direcionaNao(href){

        $('#lnkAtualizar').hide();
        $('#lnkCancelar').hide();
        
        location.href = href;
    }

</script>
<body class="internas">
            
    <%@include file="menu.jsp"%>
    
    <div class="divisoria"></div>
    <div id="titulo-subnav">Confirmação</div>
    <div class="divisoria"></div>


    <form action="c" id="it-bloco01">

        <input type="hidden" name="app" value="1810"/>
        <input type="hidden" name="acao" value="gerar"/>   

        <table align="center">
            <tr align="center">
                <td align="center">
                    <p class="legendaConfirmacao MargemSuperior0">${msg}</p>
                </td>
            </tr>
            <tr align="center">
                <td align="center">
                    <a href="javascript:direcionaSim('${sim}')"  id="lnkAtualizar"><img src="imagens/btn-atualizar.png" width="140" height="35" style="margin-top:20px; margin-left:10px;" /></a>
                    <a href="javascript:direcionaNao('${nao}')" id="lnkCancelar"><img src="imagens/btn-cancelar.png" width="140" height="35" style="margin-top:20px; margin-left:10px;" /></a>
                </td>
            </tr>
        </table>


    </form>
</body>
</html>



</html>

