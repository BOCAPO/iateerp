



<%@include file="head.jsp"%>

<style>
    table {border:none;width:0;padding:0px;margin-left:0em;margin-top:0em;}
    tr {border:none;background:none;padding:0px;margin:0em auto;}
    td {border:none;background:none;padding:0px;margin:0em auto;}
</style>  
  

<body class="internas">
            
    <%@include file="menuAcesso.jsp"%>
    
    <div class="divisoria"></div>
    <c:if test='${app == "7011"}'>
        <div id="titulo-subnav">Inclusão de Local de Acesso</div>
    </c:if>
    <c:if test='${app == "7012"}'>
        <div id="titulo-subnav">Alteração de Local de Acesso</div>
    </c:if>
    <div class="divisoria"></div>
    
    <script type="text/javascript" LANGUAGE="JavaScript" SRC="js/calendario.js"></script>
    <link rel="stylesheet" type="text/css" href="css/calendario.css"/>

    <script type="text/javascript" language="JavaScript" src="js/format.js"></script>
    
    <script type="text/javascript" language="JavaScript">

        function validarForm(){

            if(document.forms[0].descricao.value == ''){
                alert('A descrição deve ser informada!');
                return;
            }
            document.forms[0].submit();
        }

    </script>

    <form action="c" method="POST">
        <input type="hidden" name="app" value="${app}">
        <input type="hidden" name="acao" value="gravar">
        <input type="hidden" name="id" value="${local.id}">

        <table>
            <tr>
                <td>
                    <p class="legendaCodigo MargemSuperior0" >Descrição</p>
                    <input type="text" name="descricao" class="campoSemTamanho alturaPadrao"  style="width:272px;" value="${local.descricao}">
                </td>
                <td>
                    <p class="legendaCodigo MargemSuperior0" >Estação</p>
                    <input type="text" name="estacao" class="campoSemTamanho alturaPadrao"  style="width:272px;" value="${local.estacao}">
                </td>
            </tr>
        </table>
            
        <table>
            <tr>
                <td>
                    <p class="legendaCodigo MargemSuperior0">Req. Ex. Méd.</p>
                    <div class="selectheightnovo">
                        <select name="requerExame" class="campoSemTamanho alturaPadrao" style="width:100px;" >
                            <option value="S" <c:if test='${local.requerExame == "S"}'>selected</c:if>>SIM</option>
                            <option value="N" <c:if test='${local.requerExame == "N"}'>selected</c:if>>NÃO</option>
                        </select>
                    </div>        
                    
                </td>
                <td>
                    <p class="legendaCodigo MargemSuperior0">Acesso Conv. Util.</p>
                    <div class="selectheightnovo">
                        <select name="convUtil" class="campoSemTamanho alturaPadrao" style="width:100px;" >
                            <option value="S" <c:if test='${local.convUtil == "S"}'>selected</c:if>>SIM</option>
                            <option value="N" <c:if test='${local.convUtil == "N"}'>selected</c:if>>NÃO</option>
                        </select>
                    </div>        
                    
                </td>
                <td>
                    <p class="legendaCodigo MargemSuperior0">Só Carro</p>
                    <div class="selectheightnovo">
                        <select name="soCarro" class="campoSemTamanho alturaPadrao" style="width:100px;" >
                            <option value="S" <c:if test='${local.soCarro == "S"}'>selected</c:if>>SIM</option>
                            <option value="N" <c:if test='${local.soCarro == "N"}'>selected</c:if>>NÃO</option>
                        </select>
                    </div>        
                    
                </td>
                <td>
                    <p class="legendaCodigo MargemSuperior0">Mostra Placa</p>
                    <div class="selectheightnovo">
                        <select name="motraPlaca" class="campoSemTamanho alturaPadrao" style="width:100px;" >
                            <option value="S" <c:if test='${local.motraPlaca == "S"}'>selected</c:if>>SIM</option>
                            <option value="N" <c:if test='${local.motraPlaca == "N"}'>selected</c:if>>NÃO</option>
                        </select>
                    </div>        
                    
                </td>
                <td>
                    <p class="legendaCodigo MargemSuperior0">Mostra Quant.</p>
                    <div class="selectheightnovo">
                        <select name="mostraQuantidade" class="campoSemTamanho alturaPadrao" style="width:100px;" >
                            <option value="S" <c:if test='${local.mostraQuantidade == "S"}'>selected</c:if>>SIM</option>
                            <option value="N" <c:if test='${local.mostraQuantidade == "N"}'>selected</c:if>>NÃO</option>
                        </select>
                    </div>        
                    
                </td>
            </tr>
        </table>
                
        
        <input type="button" class="botaoatualizar"  onclick="validarForm()" value=" " />
        <input type="button" class="botaoVoltar"  onclick="window.location='c?app=7010';" value=" " />

    </form>

</body>
</html>
