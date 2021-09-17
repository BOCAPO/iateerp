<%@include file="head.jsp"%>

<script type="text/javascript" LANGUAGE="JavaScript" SRC="js/calendario.js"></script>
<script type="text/javascript" language="JavaScript" src="js/format.js"></script>
<script type="text/javascript" language="JavaScript">
    
    $(document).ready(function () {
        $("#dtValidadeAtestado").mask("99/99/9999");
    });     

    function mostraDocumento(matricula, categoria, seqDependente){
        window.open('c?app=3040&acao=mostraFoto&matricula='+matricula+'&categoria='+categoria+'&seqDependente='+seqDependente,'page','toolbar=no,location=no,status=no,menubar=no,scrollbars=no,resizable=no,width=600,height=600'); 
    }

    function excluiFoto(){
        $.ajax({url:'AtestadoAjax', type:'GET',async:false,
                            data:{
                            tipo:1,
                            categoria:$('#idCategoria').val(),
                            matricula:$('#matricula').val(),
                            dependente:$('#seqDependente').val()
                            }
        }).success(function(retorno){
            $('#imgAtestado').removeAttr('src');
            $('#imgAtestado').show();
            alert('Imagem do atestado excluída com sucesso!');
        });
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
    
    <form method="POST" action="upload" enctype="multipart/form-data">
        <input type="hidden" id="matricula" name="matricula" value="${socio.matricula}">
        <input type="hidden" id="idCategoria" name="idCategoria" value="${socio.idCategoria}">
        <input type="hidden" id="seqDependente" name="seqDependente" value="${socio.seqDependente}">
        <input type="hidden" name="idTurma" value="${idTurma}">
        <input type="hidden" name="app" value="3040">
        <input type="hidden" name="acao" value="gravar">
        <input type="hidden" name="fazMatricula" value="NAO">


        <div class="divisoria"></div>
        <div id="titulo-subnav">Atestado Médico</div>
        <div class="divisoria"></div>
               
        <table class="fmt">
            <tr>
                <td>
                    <p class="legendaCodigo MargemSuperior0">Dt. Vencimento</p>
                    <fmt:formatDate var="dtValidadeAtestado" value="${socio.dtValidadeAtestado}" pattern="dd/MM/yyyy"/>
                    <input type="text" id="dtValidadeAtestado" name="dtValidadeAtestado" class="campoSemTamanho alturaPadrao" style="width: 100px" value="${dtValidadeAtestado}">
                </td>    
                <td rowspan="4">
                    <a href="javascript: mostraDocumento(${socio.matricula}, ${socio.idCategoria}, ${socio.seqDependente});">
                        <img id="imgAtestado" src="f?tb=10&mat=${socio.matricula}&cat=${socio.idCategoria}&seq=${socio.seqDependente}" class="recuoPadrao MargemSuperiorPadrao" width="120" height="160">
                    </a>
                </td>    
            </tr>    
       </table>
        
        <br>
        <table class="fmt">
           <tr>
             <td colspan="3">
                 <input type="file" class="botaoEscolherArquivo" name="arquivo"/>
             </td>
           </tr>
           <tr>
             <td>
                 <input type="submit" id="atualizar" class="botaoatualizar" value="">
             </td>
             <td>
                 <a href="javascript: if(confirm('confirma exclusao?')) excluiFoto()"><img src="imagens/btn-excluir-foto.png" class="botaoExcluirFoto" /></a><BR>
             </td>
             <td colspan="3">
                <input type="button" class="botaoVoltar"  onclick="window.location='c?app=3070&idTurma=${idTurma}';" value=" " />
             </td>
           </tr>
        </table>  
    </form>
    
</body>
</html>
