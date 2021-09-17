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
    <input type="hidden" name="app" value="1360">
    <input type="hidden" name="acao" value="imprimirCarteira">
    <input type="hidden" name="matricula" value="${matricula}">
    <input type="hidden" name="idCategoria" value="${idCategoria}">
    <table>
        <tr>
            <td>
                
              <p class="legendaCodigo MargemSuperior0" >Início</p>
                <div class="selectheightnovo">
                    <select name="mesInicio" class="campoSemTamanho alturaPadrao" style="width:100px;" >
                        <option value="1">Janeiro</option>
                        <option value="2">Fevereiro</option>
                        <option value="3">Março</option>
                        <option value="4">Abril</option>
                        <option value="5">Maio</option>
                        <option value="6">Junho</option>
                        <option value="7">Julho</option>
                        <option value="8">Agosto</option>
                        <option value="9">Setembro</option>
                        <option value="10">Outubro</option>
                        <option value="11">Novembro</option>
                        <option value="12">Dezembro</option>
                    </select>
                </div>        

            </td>
            <td>
                <p class="legendaCodigo MargemSuperior0" >&nbsp</p>
                <input type="text" class="campoSemTamanho alturaPadrao larguraNumeroPequeno" name="anoInicio" maxlength="4" size="4" value="1900" onkeypress="onlyNumber(event)"/>
            </td>
            <td>
              <p class="legendaCodigo MargemSuperior0" >Fim</p>
                <div class="selectheightnovo">
                    <select name="mesFim" class="campoSemTamanho alturaPadrao" style="width:100px;" >
                        <option value="1">Janeiro</option>
                        <option value="2">Fevereiro</option>
                        <option value="3">Março</option>
                        <option value="4">Abril</option>
                        <option value="5">Maio</option>
                        <option value="6">Junho</option>
                        <option value="7">Julho</option>
                        <option value="8">Agosto</option>
                        <option value="9">Setembro</option>
                        <option value="10">Outubro</option>
                        <option value="11">Novembro</option>
                        <option value="12">Dezembro</option>
                    </select>
                </div>        
            </td>
            <td>
                <p class="legendaCodigo MargemSuperior0" >&nbsp</p>
                <fmt:formatDate var="anoFim" value="<%=new java.util.Date()%>" pattern="yyyy"/>                
                <input type="text" class="campoSemTamanho alturaPadrao larguraNumeroPequeno" name="anoFim" maxlength="4" size="4" value="${anoFim}"onkeypress="onlyNumber(event)"/>
            </td>
        </tr>  
    </table>    
    <br>        
            
    <table>
        <tr>
            <td>
                <input type="submit" class="botaoimprimirnovo"  value=" " />
            </td>
            <td>
                <input type="button" class="botaoVoltar"  onclick="window.location='c?app=1360&matricula=${matricula}&idCategoria=${idCategoria}&seqDependente=0';" value="" />
            </td>
        </tr>  
    </table>    

</form>
    
</body>
</html>
