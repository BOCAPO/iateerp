<%@include file="head.jsp"%>

<script type="text/javascript" language="JavaScript" src="js/format.js"></script>
<script type="text/javascript" language="JavaScript"> 
    
    function validarForm(){

        if(document.forms[0].arquivo.value == ''){
            alert('Você não informou o arquivo');
            return;
        }

        document.forms[0].submit();
    }

</script>

<style>
    table {border:none;width:0;padding:0px;margin-left:0em;margin-top:0em;}
    tr {border:none;background:none;padding:0px;margin:0em auto;}
    td {border:none;background:none;padding:0px;margin:0em auto;}
    
input[type="file"]
{
}    
    
</style>  

<body class="internas">
            
    <%@include file="menu.jsp"%>
    
    <div class="divisoria"></div>
    <div id="titulo-subnav">Visualizar Arquivo de Retorno</div>
    <div class="divisoria"></div>

    <form method="POST" action="upload" enctype="multipart/form-data">
        <input type="hidden" name="app" value="1940">
        <input type="hidden" name="acao" value="visualizar">
        <br><br>
        <table align="left">
           <tr>
             <td colspan="2">
                 <input type="file" class="botaoEscolherArquivo" name="arquivo"/>
             </td>
           </tr>
           <tr>
             <td>
                 <br><br>
                 <input type="button" class="botaoEmitir" onclick="validarForm()" value="">
             </td>
           </tr>
        </table>  
    </form>

    
</body>
</html>
