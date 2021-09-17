<%@include file="head.jsp"%>

    <style>
        table {border:none;width:0;padding:0px;margin-left:0em;margin-top:0em;}
        tr {border:none;background:none;padding:0px;margin:0em auto;}
        td {border:none;background:none;padding:0px;margin:0em auto;}
    </style>  

    <body class="internas">

    <%@include file="menu.jsp"%>
    
    <br>                
<form action="c" method="POST">
    <input type="hidden" name="app" value="2000">
    <input type="hidden" name="acao" value="imprimirRelatorio">
    <input type="hidden" name="sqlCompleto" value="${sqlCompleto}">
    <input type="hidden" name="sqlSimplificado" value="${sqlSimplificado}">
    <table border="1">
        <tr>
            <td>
                
              <p class="legendaCodigo MargemSuperior0" >Tipo</p>
                <div class="selectheightnovo">
                    <select name="tipoRel" class="campoSemTamanho alturaPadrao" style="width:100px;" >
                        <option value="C">Completo</option>
                        <option value="S">Simplificado</option>
                    </select>
                </div>        
            </td>
        </tr>        
        <tr>
            <td colspan="2">
                <br>
                <input type="submit" class="botaoimprimir" value=" " />
            <td>
        </tr>
    </table>    
</form>
    
</body>
</html>
