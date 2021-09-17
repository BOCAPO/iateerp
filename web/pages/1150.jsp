<%@include file="head.jsp"%>

<style>
    table {border:none;width:0;padding:0px;margin-left:0em;margin-top:0em;}
    tr {border:none;background:none;padding:0px;margin:0em auto;}
    td {border:none;background:none;padding:0px;margin:0em auto;}
</style>  

<script type="text/javascript" language="JavaScript">

    function validarForm(){

        if(document.forms[0].descricao.value == ''){
            alert('O campo descrição é de preenchimento obrigatório!');
            return;
        }
        if(document.forms[0].qtEstoque.value != '' ){
            if (isNaN(document.forms[0].qtEstoque.value)) {    
                   alert("Estoque inválido!");    
                   return false;    
            } 
        }
        if(document.forms[0].qtTotal.value != ''){
            if (isNaN(document.forms[0].qtTotal.value)) {    
                   alert("Qt. Total inválida!");    
                   return false;    
            } 
        }
        
        var vr1=0;
        var pz1=0;
        
        if(document.forms[0].vrEmprestimo.value != ''){
            if (isNaN(document.forms[0].vrEmprestimo.value.replace(".","").replace(",","."))) {    
                   alert("Vr. Empréstimo inválido!");    
                   return false;    
            } 
        }        
        
        if(document.forms[0].qtDiasIniCob.value != ''){
            if (isNaN(document.forms[0].qtDiasIniCob.value)) {    
                   alert("Dias inválido!");    
                   return false;    
            } 
            pz1 = parseInt(document.forms[0].qtDiasIniCob.value);
        }
        if(document.forms[0].vrDiaria.value != ''){
            if (isNaN(document.forms[0].vrDiaria.value.replace(".","").replace(",","."))) {    
                   alert("Vr. Diária inválida!");    
                   return false;    
            } 
            vr1 = parseFloat(document.forms[0].vrDiaria.value.replace(".","").replace(",","."));
        }        
        if(vr1>0 && pz1==0){
            alert("Informe a quantidade de dias!");    
            return false;    
        }
        
        vr1=0;
        pz1=0
        if(document.forms[0].pzPadraoDevolucao.value != ''){
            if (isNaN(document.forms[0].pzPadraoDevolucao.value)) {    
                   alert("Pz. Max. inválido!");    
                   return false;    
            } 
            pz1 = parseInt(document.forms[0].pzPadraoDevolucao.value);
        }
        if(document.forms[0].valMaterial.value != ''){
            if (isNaN(document.forms[0].valMaterial.value.replace(".","").replace(",","."))) {    
                   alert("Vr. Bem inválido!");    
                   return false;    
            } 
            vr1 = parseFloat(document.forms[0].valMaterial.value.replace(".","").replace(",","."));
        }        
        if(vr1>0 && pz1==0){
            alert("Informe o prazo máximo!");    
            return false;    
        }
        
        document.forms[0].submit();
    }

</script>

<body class="internas">
            
    <%@include file="menu.jsp"%>

    <div class="divisoria"></div>
    <c:if test='${app == "1151"}'>
        <div id="titulo-subnav">Inclusão de Material</div>
    </c:if>
    <c:if test='${app == "1152"}'>
        <div id="titulo-subnav">Alteração de Material</div>
    </c:if>
    <div class="divisoria"></div>

    <form action="c" id="it-bloco01">

        <input type="hidden" name="app" value="${app}"/>
        <input type="hidden" name="acao" value="gravar"/>   
        <input type="hidden" name="idMaterial" value="${material.id}"/>

        <table align="left">
          <tr>
            <td>
                <table align="left">
                    <tr>
                      <td>
                        <p class="legendaCodigo">Descrição:</p>
                        <input type="text" name="descricao" maxlength="25" class="campoSemTamanho alturaPadrao" style="width: 190px" value="${material.descricao}" />
                      </td>
                      <td>
                        <p class="legendaCodigo" >Estoque: &nbsp&nbsp</p>
                        <input type="text" name="qtEstoque" maxlength="3" class="campoSemTamanho alturaPadrao larguraNumero" value="${material.qtEstoque}" />
                      </td>
                      <td>
                        <p class="legendaCodigo">Qt. Total</p>
                        <input type="text" name="qtTotal" maxlength="3" class="campoSemTamanho alturaPadrao larguraNumero" value="${material.qtTotal}" />
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
                          <p class="legendaCodigo">Taxa para Cobrança:</p>
                          <select name="cdTxAdministrativa" class="campoSemTamanho alturaPadrao" style="width: 247px">
                              <c:forEach var="txsAdm" items="${taxasAdministrativas}">
                                  <option value="${txsAdm.id}" <c:if test='${material.cdTxAdministrativa == txsAdm.id}'>selected</c:if>>${txsAdm.descricao}</option>
                              </c:forEach>
                          </select>
                      </td>
                      <td >
                          <p class="legendaCodigo">Vr. Empréstimo:</p>
                          <input type="text" name="vrEmprestimo" maxlength="8" class="campoSemTamanho alturaPadrao" style="width: 85px" value="<fmt:formatNumber value="${material.vrEmprestimo}" pattern="#0.00"/>" />
                      </td>
                    </tr>
                </table>
            </td>
          </tr>
          <tr>
            <td>
                <table align="left">
                    <tr>
                      <td >
                          <p class="legendaCodigo">Dias:</p>
                          <input type="text" name="qtDiasIniCob" maxlength="3" class="campoSemTamanho alturaPadrao larguraNumero" value="${material.qtDiasIniCob}" />
                      </td>
                      <td >
                          <p class="legendaCodigo">Vr. Diária:</p>
                          <input type="text" name="vrDiaria" maxlength="8" class="campoSemTamanho alturaPadrao" style="width: 85px" value="<fmt:formatNumber value="${material.vrDiaria}" pattern="#0.00"/>" />
                      </td>
                      <td >
                          <p class="legendaCodigo">Vr. Bem:</p>
                          <input type="text" name="valMaterial" maxlength="8" class="campoSemTamanho alturaPadrao larguraNumero" value="<fmt:formatNumber value="${material.valMaterial}" pattern="#0.00"/>" />
                      </td>
                      <td >
                          <p class="legendaCodigo">Pz. Max:</p>
                          <input type="text" name="pzPadraoDevolucao" maxlength="3" class="campoSemTamanho alturaPadrao" style="width: 85px" value="${material.pzPadraoDevolucao}" />
                      </td>
                    </tr>
                </table>
            </td>
          </tr>
        </table>        
        
        <br><br><br><br><br><br><br><br><br><br><br><br>

        <input type="button" class="botaoatualizar"  onclick="validarForm()" value=" " />
        <input type="button" class="botaoVoltar"  onclick="window.location='c?app=1150';" value=" " />

    </form>
</body>
</html>


