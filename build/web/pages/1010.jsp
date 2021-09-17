



<%@include file="head.jsp"%>

<style>
    table {border:none;width:0;padding:0px;margin-left:0em;margin-top:0em;}
    tr {border:none;background:none;padding:0px;margin:0em auto;}
    td {border:none;background:none;padding:0px;margin:0em auto;}
</style>  
  

<body class="internas">
            
    <%@include file="menu.jsp"%>
    
    <div class="divisoria"></div>
    <c:if test='${app == "1011"}'>
        <div id="titulo-subnav">Inclusão de Categoria</div>
    </c:if>
    <c:if test='${app == "1012"}'>
        <div id="titulo-subnav">Alteração de Categoria</div>
    </c:if>
    <div class="divisoria"></div>
    
    <script type="text/javascript" LANGUAGE="JavaScript" SRC="js/calendario.js"></script>
    <link rel="stylesheet" type="text/css" href="css/calendario.css"/>
    <script type="text/javascript" src="js/jscolor/jscolor.js"></script>
    <script type="text/javascript" language="JavaScript" src="js/format.js"></script>
    
    <script type="text/javascript" language="JavaScript">

        function validarForm(){

            if(document.forms[0].id.value == ''){
                alert('O código deve ser informado!');
                return;
            }
            if(document.forms[0].descricao.value == ''){
                alert('A descrição deve ser informada!');
                return;
            }
            if(document.forms[0].abreviacao.value == ''){
                alert('Necessário preencher abreviação da Categoria!');
                return;
            }
            if(document.forms[0].idCatUsuario.value > 0){
                if(document.forms[0].qtUsuario.value == ''){
                    alert('Se o Título permite Usuário informe a quantidade.');
                    return;
                }
            }
            document.forms[0].submit();
        }

    </script>

    <form action="c" method="POST">
        <input type="hidden" name="app" value="${app}">
        <input type="hidden" name="acao" value="gravar">

        <table>
            <tr>
                <td>
                    <p class="legendaCodigo MargemSuperior0" >Cód.</p>
                    <input type="text" name="id" id="id" class="campoSemTamanho alturaPadrao"  <c:if test='${app == "1012"}'>readonly</c:if> style="width:50px;" value="${categoria.id}">
                </td>
                <td>
                    <p class="legendaCodigo MargemSuperior0" >Descrição</p>
                    <input type="text" name="descricao" id="descricao" class="campoSemTamanho alturaPadrao"  style="width:272px;" value="${categoria.descricao}">
                </td>
                <td>
                    <p class="legendaCodigo MargemSuperior0" >Abrev.</p>
                    <input type="text" name="abreviacao" id="abreviacao" class="campoSemTamanho alturaPadrao"  style="width:60px;" value="${categoria.abreviacao}">
                </td>
            </tr>
        </table>
            
        <table>
            <tr>
                <td>
                    <p class="legendaCodigo MargemSuperior0">Status</p>
                    <div class="selectheightnovo">
                        <select name="status" id="status" class="campoSemTamanho alturaPadrao" style="width:60px;" >
                            <option value="AT" <c:if test='${categoria.status == "Ativa"}'>selected</c:if>>Ativa</option>
                            <option value="IN" <c:if test='${categoria.status == "Inativa"}'>selected</c:if>>Inativa</option>
                        </select>
                    </div>        
                </td>
                <td>
                    <p class="legendaCodigo MargemSuperior0">Tipo</p>
                    <div class="selectheightnovo">
                        <select name="tipo" id="tipo" class="campoSemTamanho alturaPadrao" style="width:78px;" >
                            <option value="SO" <c:if test='${categoria.tipo == "Sócio"}'>selected</c:if>>Sócio</option>
                            <option value="NS" <c:if test='${categoria.tipo == "Não Sócio"}'>selected</c:if>>Não Sócio</option>
                        </select>
                    </div>        
                    
                </td>
                
                <td>
                    <p class="legendaCodigo MargemSuperior0">Tit. Transf.</p>
                    <div class="selectheightnovo">
                        <select name="tituloTransferivel" id="tituloTransferivel" class="campoSemTamanho alturaPadrao" style="width:65px;" >
                            <option value="S" <c:if test='${categoria.tituloTransferivel == "Sim"}'>selected</c:if>>SIM</option>
                            <option value="N" <c:if test='${categoria.tituloTransferivel == "Não"}'>selected</c:if>>NÃO</option>
                        </select>
                    </div>        
                    
                </td>
                <td>
                    <p class="legendaCodigo MargemSuperior0">Admite Dep.</p>
                    <div class="selectheightnovo">
                        <select name="admiteDependente" id="admiteDependente" class="campoSemTamanho alturaPadrao" style="width:75px;" >
                            <option value="S" <c:if test='${categoria.admiteDependente == "Sim"}'>selected</c:if>>SIM</option>
                            <option value="N" <c:if test='${categoria.admiteDependente == "Não"}'>selected</c:if>>NÃO</option>
                        </select>
                    </div>        
                    
                </td>
                <td>
                    <p class="legendaCodigo MargemSuperior0" >Vr.Max.Carne</p>
                    <input type="text" name="vrMaxCarne" id="vrMaxCarne" class="campoSemTamanho alturaPadrao"  style="width:80px;" value="${categoria.vrMaxCarne}">
                </td>
            </tr>
        </table>
            
        <table>
            <tr>
                <td>
                    <fieldset class="field-set legendaFrame recuoPadrao" style="width:220px;height:75px">
                        <legend >Renovação de Convite:</legend>
                        <table class="fmt">
                            <tr>
                                <td>
                                    <p class="legendaCodigo MargemSuperior0" >Quant.</p>
                                    <input type="text" name="qtConvite" name="qtConvite" class="campoSemTamanho alturaPadrao"  style="width:80px;" value="${categoria.qtConvite}">
                                </td>
                                <td>
                                    <p class="legendaCodigo MargemSuperior0" >Prazo(meses)</p>
                                    <input type="text" name="prazoRenovacaoConvite" name="prazoRenovacaoConvite" class="campoSemTamanho alturaPadrao"  style="width:90px;" value="${categoria.prazoRenovacaoConvite}">
                                </td>
                            </tr>
                        </table>
                    </fieldset>                                
                </td>
                <td>
                    <fieldset class="field-set legendaFrame recuoPadrao" style="width:180px;height: 75px">
                        <legend >Cor da Tarja da Carteira:</legend>
                        <table class="fmt">
                            <tr>
                                <td>
                                    <p class="legendaCodigo MargemSuperior0" id="lblCorFundo">Titular:</p>
                                    <input class="campoSemTamanho alturaPadrao larguraNumero color"  style="width:65px;" value="${categoria.corTitular}" name="corTitular" id="corTitular" onload="document.getElementById('corFundo').color.fromString('${categoria.corTitular}')">
                                </td>
                                <td>
                                    <p class="legendaCodigo MargemSuperior0" id="lblCorFonte">Dependente:</p>
                                    <input class="campoSemTamanho alturaPadrao larguraNumero color" size="10" style="width:65px;" value="${categoria.corDependente}" name="corDependente" id="corDependente" onload="document.getElementById('corFonte').color.fromString('${categoria.corDependente}')">
                                </td>   
                            </tr>
                        </table>
                    </fieldset>                                
                </td>
            </tr>
        </table>
                
        <table>
            <tr>
                <td>
                    <fieldset class="field-set legendaFrame recuoPadrao" style="width:417px;height: 75px">
                        <legend >Usuário no Título</legend>
                        <table class="fmt">
                            <tr>
                                <td>
                                    <p class="legendaCodigo MargemSuperior0">Categoria</p>
                                    <div class="selectheightnovo2">
                                      <select name="idCatUsuario" id="idCatUsuario" class="campoSemTamanho alturaPadrao larguraComboCategoria">
                                          <option value="0" selected>NENHUMA</option>
                                          <c:forEach var="catUsuario" items="${catUsuarios}">
                                              <option value="${catUsuario.id}" <c:if test="${categoria.idCatUsuario eq catUsuario.id}">selected</c:if>>${catUsuario.descricao}</option>
                                          </c:forEach>
                                      </select>
                                    </div>        
                                </td>
                                <td>
                                    <p class="legendaCodigo MargemSuperior0" >Quant.</p>
                                    <input type="text" name="qtUsuario" id="qtUsuario" class="campoSemTamanho alturaPadrao"  style="width:70px;" value="${categoria.qtUsuario}">
                                </td>
                            </tr>
                        </table>
                    </fieldset>                                
                </td>
            </tr>
        </table>
        
        <input type="button" class="botaoatualizar"  onclick="validarForm()" value=" " />
        <input type="button" class="botaoVoltar"  onclick="window.location='c?app=1010';" value=" " />

    </form>

</body>
</html>
