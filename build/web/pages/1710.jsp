<%@include file="head.jsp"%>

<script type="text/javascript" language="JavaScript" src="js/format.js"></script>
<script type="text/javascript" language="JavaScript"> 
    
    function validarForm(){

        if(document.forms[0].arquivo.value == ''){
            alert('Você não informou o arquivo');
            return;
        }
        
        if(!isDataValida(document.forms[0].dataVencimento.value)){
            return;
        }
        
        if(document.forms[0].dataVencimento.value == ''){
            alert('É obrigatório informar a data de vencimento');
            return;
        }        

        $('#btnAtualizar').hide();
        document.forms[0].submit();
    }

</script>
<script type="text/javascript" language="javascript">
    $(document).ready(function () {
        $("#dataVencimento").mask("99/99/9999");
    });     
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
    <div id="titulo-subnav">Baixa DCO</div>
    <div class="divisoria"></div>
    

    <form method="POST" action="upload" enctype="multipart/form-data">
        <input type="hidden" name="app" value="1710">
        <input type="hidden" name="acao" value="baixar">
        <input type="hidden" name="force" value="false">
        <br>
        <table align="left">
           <tr>
             <td colspan="2">
                 <input type="file" class="botaoEscolherArquivo" name="arquivo"/>
             </td>
           </tr>
        </table>  
        <br><br>
        <table align="left">
           <tr>
             <td>
                 <p class="legendaCodigo MargemSuperior0">Data Vencimento:</p>
                 <input type="text"  class="campoSemTamanho alturaPadrao larguraData" id="dataVencimento" name="dataVencimento"/><br>
             </td>
           </tr>
        </table>  
        <br><br><br><br>
        <table align="left">
           <tr>
             <td>
                 <input type="button" id="btnAtualizar" class="botaoatualizar" onclick="validarForm()" value="">
             </td>
           </tr>
        </table>  
    </form>

    
</body>
</html>
