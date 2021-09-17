<%@include file="head.jsp"%>

<script type="text/javascript" LANGUAGE="JavaScript" SRC="js/calendario.js"></script>
<script type="text/javascript" language="JavaScript" src="js/format.js"></script>
<script type="text/javascript" language="JavaScript">
    
    $(document).ready(function () {
        $("#dataVencimento").mask("99/99/9999");
    });        
    
    
    function validarForm(){

        if(!isDataValida(document.forms[0].dataVencimento.value)){
            return;
        }

        document.forms[0].submit();
    }
    
    function mostraDocumento(idCarro){
        window.open('c?app=2194&acao=mostraFoto&idCarro='+idCarro,'page','toolbar=no,location=no,status=no,menubar=no,scrollbars=no,resizable=no,width=600,height=600'); 
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
    <div id="titulo-subnav">Emissão de Crachá</div>
    <div class="divisoria"></div>


    <form action="c">
        <input type="hidden" name="idFuncionario" value="${funcionario.id}">
        <input type="hidden" name="app" value="1054">
        <input type="hidden" name="acao" value="emitirCracha">

        <table align="left" >
            <tr>
                <td rowspan="4">
                    <a href="javascript: mostraDocumento(${carro.id});">
                        <img src="f?tb=8&cd=${carro.id}" onerror="this.src='images/tenis/avatar-default.png';"  class="recuoPadrao MargemSuperiorPadrao" width="120" height="160">
                    </a>
                </td>    
            </tr>    
            
            <tr>
                <td>
                    <table align="left">
                      <tr>
                        <td>
                            <p class="legendaCodigo MargemSuperior0">Marca:</p>
                            <input type="text" name="marca" class="campoSemTamanho alturaPadrao" style="width: 175px" readonly value="${carro.modelo.marca.descricao}">
                        </td>
                        <td>
                            <p class="legendaCodigo MargemSuperior0">Modelo</p>
                            <input type="text" name="modelo" class="campoSemTamanho alturaPadrao" style="width: 175px" readonly value="${carro.modelo.descricao}">
                        </td>
                    </tr>            
                   </table>
                </td>    
            </tr>    
            <tr>
                <td>
                    <table align="left">
                      <tr>
                        <td>
                            <p class="legendaCodigo MargemSuperior0">Cor:</p>
                            <input type="text" name="cor" class="campoSemTamanho alturaPadrao" style="width: 175px" readonly value="${carro.cor.descricao}">
                        </td>
                        <td>
                            <p class="legendaCodigo MargemSuperior0">Placa:</p>
                            <input type="text" name="placa" class="campoSemTamanho alturaPadrao" style="width: 175px" readonly value="${carro.placa}">
                        </td>
                    </tr>            
                   </table>
                </td>    
            </tr>          
       </table>
    
    </form>
    <br><br><br><br><br><br><br><br><br><br><br>
    

    <form method="POST" action="upload" enctype="multipart/form-data">
        <input type="hidden" name="idCarro" value="${carro.id}">
        <input type="hidden" name="app" value="2194">
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
                 <a href="javascript: if(confirm('confirma exclusao?')) window.location.href='c?app=2194&idCarro=${carro.id}&acao=excluirFoto'"><img src="imagens/btn-excluir-foto.png" class="botaoExcluirFoto" /></a><BR>
             </td>
             <td colspan="3">
                <input type="button" class="botaoVoltar"  onclick="window.location='c?app=2190&acao=consultar&matricula=${carro.socio.matricula}&idCategoria=${carro.socio.idCategoria}&seqDependente=0';" value=" " />
             </td>
           </tr>
        </table>  
    </form>

    
</body>
</html>
