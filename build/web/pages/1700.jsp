<%@include file="head.jsp"%>

<script type="text/javascript" language="JavaScript" src="js/format.js"></script>
<script type="text/javascript" language="JavaScript"> 
    
    function validarForm(){

        if(document.forms[0].arquivo.value == ''){
            alert('Você não informou o arquivo');
            return;
        }
        
        if(document.forms[0].dias.value == ''){
            document.forms[0].dias.value = 0;
        }
        
        if(document.forms[0].dias.value != 5){
            if(!confirm('Deseja realmente não cobrar multa após ' + document.forms[0].dias.value + ' dias de atraso?')){
                return;
            }
        }

        $('#btnAtualizar').hide();
        
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
    <div id="titulo-subnav">Baixa Arquivo</div>
    <div class="divisoria"></div>
    
    <form method="POST" action="upload" enctype="multipart/form-data">
        <input type="hidden" name="app" value="1700">
        <input type="hidden" name="acao" value="baixar">
        <input type="hidden" name="force" value="false">
        
        <table>
            <tr>
                <td>
                    <p class="legendaCodigo MargemSuperior0">Banco</p>
                    <div class="selectheightnovo">
                        <select name="banco" id="banco"  style="width:150px;" class="campoSemTamanho alturaPadrao">
                            <option value="B" selected>Banco do Brasil</option>
                            <option value="I">Itaú</option>
                        </select>
                    </div>
                </td>
            </tr>
        </table>
        
        <br>
        
        <table align="left">
           <tr>
             <td colspan="2">
                 <input type="file" class="botaoEscolherArquivo" name="arquivo"/>
                 <br>
             </td>
           </tr>
        </table>
        <br><br>
        <table align="left">
           <tr>
             <td style="width:220px">
                 <p class="legendaCodigo MargemSuperior0">Qt. dias após venc não cobrar multa:</p>
             </td>
             <td>
                 <input type="text" name="dias" class="campoSemTamanho alturaPadrao larguraNumeroPequeno" onkeypress="onlyNumber(event)" value="5">
             </td>
           </tr>
        </table>
        <br><br><br>
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
