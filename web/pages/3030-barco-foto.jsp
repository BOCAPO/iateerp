<%@include file="head.jsp"%>

<style>
    table {border:none;width:0;padding:0px;margin-left:0em;margin-top:0em;}
    tr {border:none;background:none;padding:0px;margin:0em auto;}
    td {border:none;background:none;padding:0px;margin:0em auto;}
    
input[type="file"]
{
}    
    
</style>  

<script type="text/javascript" language="JavaScript">
    function mostraDocumento(idFoto, idBarco){
        window.open('c?app=2000&acao=showFotoDetalhe&idFoto='+idFoto+'&idBarco'+idBarco,'page','toolbar=no,location=no,status=no,menubar=no,scrollbars=no,resizable=no,width=600,height=600'); 
    }
</script>


<body class="internas">
            
    <%@include file="menu.jsp"%>
    
    <div class="divisoria"></div>
    <div id="titulo-subnav">Fotos da Embarcação - Nova</div>
    <div class="divisoria"></div>   

    <form method="POST" action="upload" enctype="multipart/form-data">
        <input type="hidden" name="idBarco" value="${barco.id}">
        <input type="hidden" name="app" value="2000">

        
        <table align="left">
           <tr>
             <td>
                 <input type="file" class="botaoEscolherArquivo" name="arquivo"/>
             </td>
             <td>
                 <input type="submit" class="botaoAtualizarFoto" value="">
             </td>
             <td>
                <input type="button" class="botaoVoltar"  onclick="window.location='c?app=2000&acao=showForm&matricula=${barco.socio.matricula}&idCategoria=${barco.socio.idCategoria}&seqDependente=0';" value=" " />
             </td>
           </tr>
        </table>  
    </form>

    <br><br><br>
    <div class="divisoria"></div>
    <div id="titulo-subnav">Fotos da Embarcação - Cadastradas</div>
    <div class="divisoria"></div>   
        
        <table>
            <c:forEach items="${barco.listarFotos()}" var="idFoto">
            <tr>
                <td >
                    <a href="#" onclick="mostraDocumento('${idFoto}','${barco.id}');">
                        <img src="f?tb=3&cd=${idFoto}&brc=${barco.id}" style="height: 300px;">
                    </a>
                </td>
                <td>
                    <a href="javascript: if(confirm('confirma exclusao?')) window.location.href='c?app=2000&idFoto=${idFoto}&idBarco=${barco.id}&acao=excluirFoto'"><img src="imagens/btn-excluir-foto.png" class="botaoExcluirFoto" /></a>
                </td>
            </tr>                
            </c:forEach>
        </table>
                 
    
</body>
</html>
