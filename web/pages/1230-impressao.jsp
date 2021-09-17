<%@include file="head.jsp"%>

<script type="text/javascript" LANGUAGE="JavaScript" SRC="js/calendario.js"></script>
<script type="text/javascript" language="JavaScript" src="js/format.js"></script>
<script type="text/javascript" language="JavaScript">

    function validarForm(){

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
    <div id="titulo-subnav">Emissão de Permissão Provisória</div>
    <div class="divisoria"></div>


    <form action="c">
        <input type="hidden" name="idPermissao" value="${permissao.id}">
        <input type="hidden" name="app" value="1234">
        <input type="hidden" name="acao" value="emitirCracha">

        <table align="left" >
            <tr>
                <td rowspan="4" >.
                    <img src="f?tb=2&cd=${permissao.id}" onerror="this.src='images/tenis/avatar-default.png';"  class="recuoPadrao MargemSuperiorPadrao" width="120" height="160">
                </td>    
            </tr>    
            
            <tr>
                <td>
                    &nbsp;
                </td>    
            </tr>    
            <tr>
                <td>
                    <p class="legendaCodigo MargemSuperior0">Nome:</p>
                    <input type="text" name="primeiroNome" class="campoSemTamanho alturaPadrao larguraData" style="width: 380px"  readonly value="${permissao.nome}">
                </td>    
            </tr>    
            <tr>
                <td>
                    <p class="legendaCodigo MargemSuperior0">Atividade</p>
                    <input type="text" name="nome" class="campoSemTamanho alturaPadrao" style="width: 380px" readonly value="${funcionario.atividade}">
                </td>    
            </tr>          
       </table>
    
    </form>
    <br><br><br><br><br><br><br><br><br><br><br>
    

    <form method="POST" action="upload" enctype="multipart/form-data">
        <input type="hidden" name="idPermissao" value="${permissao.id}">
        <input type="hidden" name="app" value="1234">
        <input type="hidden" name="acao" value="atualizarFoto">    
        
        <table align="left">
           <tr>
                 <td colspan="3">
                 <input type="file" class="botaoEscolherArquivo" name="arquivo"/>
             </td>
           </tr>
           <tr>
             <td>
                 <input type="submit" class="botaoAtualizarFoto" value="">
             </td>
             <td>
                 <input type="button" class="botaoEmitir" onclick="validarForm()" value="">
             </td>
             <td>
                 <a href="javascript: if(confirm('confirma exclusao?')) window.location.href='c?app=1234&idPermissao=${permissao.id}&acao=excluirFoto'"><img src="imagens/btn-excluir-foto.png" class="botaoExcluirFoto" /></a><BR>
             </td>
           </tr>
           <tr>
             <td colspan="3">
                <input type="button" class="botaoVoltar"  onclick="window.location='c?app=1230';" value=" " />
             </td>
           </tr>
        </table>  
    </form>

    
</body>
</html>
