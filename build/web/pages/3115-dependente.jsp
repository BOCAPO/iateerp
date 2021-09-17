<%@include file="head.jsp"%>

<style type="text/css">
    table.fmt {border:none;width:0;padding:0px;margin-left:0em;margin-top:0em;}
    table.fmt tr td {border:none;background:none;padding:0px;margin:0em auto;}
</style>  

<body class="internas">

    <%@include file="menu.jsp"%>
        
    <script type="text/javascript" language="JavaScript" src="js/format.js"></script>
    
    <script type="text/javascript" language="javascript">
        $(document).ready(function () {
                $('#tabela tr:gt(0)').css('background', 'white');

                $('#tabela tr:gt(0)').hover(function() {
                        $(this).css('background','#f4f9fe');
                }, function() {
                        $(this).css('background','white');
                })
                
                $("#dataNascimento").mask("99/99/9999");
                $("#dataCasoEspecial").mask("99/99/9999");
                $("#dataValidadeRetiradaMaterial").mask("99/99/9999");
                $("#dataValidadeRetiradaConvite").mask("99/99/9999");
                $("#dataValidadeReservaChurrasqueira").mask("99/99/9999");
                
                
        });        
    </script>  

    
    <script type="text/javascript" language="JavaScript" src="js/format.js"></script>
    <script type="text/javascript" language="JavaScript">

        function validarForm(){

            if(document.forms[0].nome.value == ''
                || document.forms[0].dataNascimento.value == ''){
                alert('Para inclusão, informe Nome e Data de Nascimento');
                return;
            }
            if(!isDataValida(document.forms[0].dataNascimento.value)){
                return;
            }
            if(!isDataValida(document.forms[0].dataCasoEspecial.value)){
                return;
            }
            if(!isDataValida(document.forms[0].dataValidadeRetiradaMaterial.value)){
                return;
            }
            if(!isDataValida(document.forms[0].dataValidadeRetiradaConvite.value)){
                return;
            }
            if(!isDataValida(document.forms[0].dataValidadeReservaChurrasqueira.value)){
                return;
            }

            document.forms[0].submit();
        }

    </script>

    <div class="divisoria"></div>
    <div id="titulo-subnav">Titular</div>
    <div class="divisoria"></div>
    
    <table class="fmt" align="left" >
        <tr>
            <td>
                <table class="fmt" align="left">
                  <tr>
                    <td>
                        <p class="legendaCodigo MargemSuperior0" >Matrícula</p>
                        <input type="text" name="nome" class="campoSemTamanho alturaPadrao larguraNumero" disabled value="${titular.socio.matricula}">
                    </td>
                    <td>
                        <p class="legendaCodigo MargemSuperior0" >Categoria</p>

                        <c:forEach var="categoria" items="${categorias}">
                            <c:if test='${titular.socio.idCategoria == categoria.id}'>
                                <input type="text" name="nome" class="campoSemTamanho alturaPadrao larguraEmail" disabled value="${categoria.descricao}">
                            </c:if>
                        </c:forEach>
                        
                    </td>
                    <td>
                        <p class="legendaCodigo MargemSuperior0" >Nome</p>
                        <input type="text" name="nome" class="campoSemTamanho alturaPadrao larguraNomeTitDep" disabled value="${dependente.nome}">
                    </td>
                  </tr>
                </table>
            </td>
        </tr>
    </table>    

    <br><br><br><br>
    <form method="POST" action="c">
        <input type="hidden" name="app" value="${app}" />
        <input type="hidden" name="acao" value="gravar" />
        <input type="hidden" name="matricula" value="${titular.socio.matricula}" />
        <input type="hidden" name="seqDependente" value="${dependente.seqDependente}" />
        <input type="hidden" name="idCategoria" value="${titular.socio.idCategoria}" />

        <div class="divisoria"></div>
        <c:if test='${app == "9035" || app == "9045"}'>
            <div id="titulo-subnav">Inclusão de Dependente - Dados Gerais</div>
        </c:if>
        <c:if test='${app == "9036" || app == "9046"}'>
            <div id="titulo-subnav">Alteração de Dependente - Dados Gerais</div>
        </c:if>
        <div class="divisoria"></div>

        <table class="fmt" align="left" >
            <tr>
                <td rowspan="4" >
                    <c:if test='${app == "9035" || app == "9045"}'>
                        <img src="f?tb=6&mat=&seq=&cat=" class="recuoPadrao MargemSuperiorPadrao"  onerror="this.src='images/tenis/avatar-default.png';" width="120" height="160"><BR>
                    </c:if>
                    <c:if test='${app != "9035" && app != "9045"}'>
                        <img src="f?tb=6&mat=${titular.socio.matricula}&seq=${dependente.seqDependente}&cat=${titular.socio.idCategoria}" class="recuoPadrao MargemSuperiorPadrao"  onerror="this.src='images/tenis/avatar-default.png';" width="120" height="160"><BR>
                    </c:if>
                </td>    
                <td>
                    &nbsp;
                </td>    
            </tr>    
            
            <tr>
                <td>
                    <table class="fmt" align="left">
                      <tr>
                        <td width="20%">
                            <p class="legendaCodigo MargemSuperior0" >Nome</p>
                            <input type="text" name="nome" class="campoSemTamanho alturaPadrao larguraNomeDependente" <c:if test='${app == "9030"}'>disabled</c:if> value="${dependente.nome}">
                        </td>
                        <td>
                            <p class="legendaCodigo MargemSuperior0">Tipo de Dependente</p>
                            <select name="idTipoDependente" class="campoSemTamanho alturaPadrao larguraComboTipoDependente" <c:if test='${app == "9030"}'>disabled</c:if> >
                                <c:forEach var="tipo" items="${tipos}">
                                    <option value="${tipo.id}" <c:if test='${dependente.idTipoDependente == tipo.id}'>selected</c:if>>${tipo.descricao}</option>
                                </c:forEach>
                            </select>
                        </td>
                      </tr>
                    </table>
                </td>
            </tr>

            <tr>
                <td>
                    <table class="fmt" align="left">
                    <tr>
                        <td>
                            <p class="legendaCodigo">Sit. Especial</p>
                            <select name="casoEspecial" class="campoSemTamanho alturaPadrao larguraComboSituacaoEspecial" <c:if test='${app == "9030"}'>disabled</c:if> >
                                <option value="N" <c:if test='${dependente.casoEspecial == "N"}'>selected</c:if>>Nenhuma</option>
                                <option value="D" <c:if test='${dependente.casoEspecial == "D"}'>selected</c:if>>Deficiente</option>
                                <option value="U" <c:if test='${dependente.casoEspecial == "U"}'>selected</c:if>>Universitário</option>
                            </select>
                        </td>
                        <td>
                            <p class="legendaCodigo">Até</p>
                            <fmt:formatDate var="dataCasoEspecial" value="${dependente.dataCasoEspecial}" pattern="dd/MM/yyyy"/>
                            <input type="text" name="dataCasoEspecial"  id="dataCasoEspecial" class="campoSemTamanho alturaPadrao larguraData"  <c:if test='${app == "9030"}'>disabled</c:if>  value="${dataCasoEspecial}" />
                        </td>
                        <td>
                            <br>
                            <fieldset class="field-set legendaFrame recuoPadrao larguraFrameTipoSexo">
                                <legend>Sexo</legend>
                                <input type="radio" name="sexo" class="legendaCodigo"  <c:if test='${app == "9030"}'>disabled</c:if> value="M" <c:if test='${dependente.masculino}'>checked</c:if> />M
                                <input type="radio" name="sexo" class="legendaCodigo"  <c:if test='${app == "9030"}'>disabled</c:if> value="F" <c:if test='${!dependente.masculino}'>checked</c:if> />F
                            </fieldset>
                        </td>
                        <td>
                            <p class="legendaCodigo">Dt. Nascimento</p>
                            <input type="text" name="dataNascimento" id="dataNascimento" class="campoSemTamanho alturaPadrao larguraData"  <c:if test='${app == "9030"}'>disabled</c:if>  value="<fmt:formatDate value="${dependente.dataNascimento}" pattern="dd/MM/yyyy" />" />
                        </td>
                     </tr>
                  </table>    
                </td>
            </tr>


        </table>    
        
        <br /><br /><br /><br /><br /><br /><br /><br /><br /><br />
       

        <div class="divisoria"></div>
        <div id="titulo-subnav">Permissões</div>
        <div class="divisoria"></div>
        
        <table class="fmt" align="left" align="top">
            <tr align="top">
                <td align="top"> 
                    <p class="legendaCodigo">Retirada de material até:</p>
                    <input type="text" name="dataValidadeRetiradaMaterial" id="dataValidadeRetiradaMaterial" class="campoSemTamanho alturaPadrao larguraDataPermDependente"  <c:if test='${app == "9030"}'>disabled</c:if>  value="<fmt:formatDate value="${dependente.dataValidadeRetiradaMaterial}" pattern="dd/MM/yyyy"/>" />
                    <p class="legendaCodigo">Retirada de convite até:</p>
                    <input type="text" name="dataValidadeRetiradaConvite" id="dataValidadeRetiradaConvite"class="campoSemTamanho alturaPadrao larguraDataPermDependente"   <c:if test='${app == "9030"}'>disabled</c:if> value="<fmt:formatDate value="${dependente.dataValidadeRetiradaConvite}" pattern="dd/MM/yyyy" />" />
                    <p class="legendaCodigo">Reserva de churrasqueira até:</p>
                    <input type="text" name="dataValidadeReservaChurrasqueira" id="dataValidadeReservaChurrasqueira" class="campoSemTamanho alturaPadrao larguraDataPermDependente"  <c:if test='${app == "9030"}'>disabled</c:if>  value="<fmt:formatDate value="${dependente.dataValidadeReservaChurrasqueira}" pattern="dd/MM/yyyy" />" />
                    <p class="legendaCodigo"></p>
                    <input class="campoSemTamanho" type="checkbox" name="empTodosMat"  <c:if test='${app == "9030"}'>disabled</c:if>  value="1" <c:if test="${dependente.empTodosMat}">checked</c:if> /> Permite retirada de qualquer material
                    <p class="legendaCodigo"></p>
                    <input class="campoSemTamanho" type="checkbox" name="consumoTodasTx"  <c:if test='${app == "9030"}'>disabled</c:if>  value="1" <c:if test="${dependente.consumoTodasTx}">checked</c:if> /> Permite consumo em qualquer taxa
                
                </td>
                <td align="top" width="60px" > 
                    &nbsp;
                </td>
                <td width="320px" >
                    
                    <p class="legendaCodigo">Material autorizados<span><input class="botaoMarcaDesmarca" type="button"  onclick="marcaDesmarca('idMaterial')" style="margin-top: 2px;" value="" title="Consultar" /></span></p>
                    <div class="recuoPadrao" style="overflow:auto;height:210px;">
                        <c:forEach var="material" items="${materiais}">
                            <input type="checkbox" name="idMaterial"  <c:if test='${app == "9030"}'>disabled</c:if>  value="${material.id}" <c:if test="${dependente.materiais[material.id] != null}">checked</c:if> />
                            ${material.descricao}
                            <br />
                        </c:forEach>
                    </div>
                    
                </td>
            </tr>
        </table>
        <br /><br /><br /><br /><br /><br /><br /><br /><br /><br /><br /><br /><br /><br />



        
        <div class="divisoria"></div>
        <div id="titulo-subnav">Dados Complementares</div>
        <div class="divisoria"></div>
        
        <table class="fmt" align="left">
            <tr>
                <td>
                    <table class="fmt" align="left">
                        <tr>
                            <td>
                                <p class="legendaCodigo">Tel. Celular</p>
                                <input type="text" name="telefoneCelular" class="campoSemTamanho alturaPadrao" style="width: 125px;"  <c:if test='${app == "9030"}'>disabled</c:if> value="${dependente.telefoneCelular}" />
                            </td>
                            <td>
                                <p class="legendaCodigo">Tel. Residencial</p>
                                <input type="text" name="telefoneResidencial" class="campoSemTamanho alturaPadrao" style="width: 125px;"  <c:if test='${app == "9030"}'>disabled</c:if> value="${dependente.telefoneResidencial}" />
                            </td>
                            <td>
                                <p class="legendaCodigo">Tel. Comercial</p>
                                <input type="text" name="telefoneComercial" class="campoSemTamanho alturaPadrao" style="width: 125px;" <c:if test='${app == "9030"}'>disabled</c:if> value="${dependente.telefoneComercial}" />
                            </td>
                            <td>
                                <p class="legendaCodigo">Fax</p>
                                <input type="text" name="fax" class="campoSemTamanho alturaPadrao" style="width: 125px;" <c:if test='${app == "9030"}'>disabled</c:if> value="${dependente.fax}" />
                            </td>
                        </tr>
                    </table>
                </td>
            </tr>
            <tr>
                <td>
                    <table class="fmt" align="left">
                        <tr>
                            <td>
                                <p class="legendaCodigo">em@il</p>
                                <input type="text" name="email" class="campoSemTamanho alturaPadrao larguraEmailDependente"  <c:if test='${app == "9030"}'>disabled</c:if> value="${dependente.email}" />
                            </td>
                            <td>
                                <p class="legendaCodigo">Cargo Especial</p>
                                <select name="idCargoEspecial" class="campoSemTamanho alturaPadrao" <c:if test='${app == "9030"}'>disabled</c:if> >
                                    <option value="0" <c:if test='${dependente.idCargoEspecial == 0}'>selected</c:if>>Nenhum</option>
                                    <c:forEach var="cargo" items="${cargos}">
                                        <option value="${cargo.id}" <c:if test='${dependente.idCargoEspecial == cargo.id}'>selected</c:if>>${cargo.descricao}</option>
                                    </c:forEach>
                                </select>
                            </td>
                            <td width="200px">
                                <br><br>
                                <input type="checkbox" name="cargoAtivo" class="legendaCodigo"  <c:if test='${app == "9030"}'>disabled</c:if>  value="1" <c:if test='${dependente.cargoAtivo}'>checked</c:if> />
                                <spam class="legendaSemMargem larguraData">Cargo ativo</spam>
                            </td>
                        </tr>
                    </table>
                </td>
            </tr>
        </table>
        <br /><br /><br /><br /><br /><br /><br /><br /><br />
        <c:if test='${app != "9030"}'>
            <input type="button" class="botaoatualizar" onclick="validarForm()" value=" " />
        </c:if>                 
        <input type="button" class="botaoVoltar"  onclick="window.location='c?app=9030&acao=consultar&matricula=${titular.socio.matricula}&categoria=${titular.socio.idCategoria}';" value=" " />
            
    </form>
        
    <br />
    <div class="divisoria"></div>
    <div id="titulo-subnav">Dependentes já cadastrados</div>
    <div class="divisoria"></div>
    <table id="tabela">
        <thead>
            <tr class="odd">
                <th scope="col" class="nome-lista">Nome</th>
                <th scope="col">Matrícula</th>
                <th scope="col">Nascimento</th>
                <th scope="col">Sexo</th>
                <th scope="col">Tipo Dependente</th>
            </tr>
        </thead>
        <tbody>
            <c:forEach var="d" items="${titular.dependentes}">
                <tr>
                    <td class="column1">${d.nome}</td>
                    <td class="column1">${d.matricula}/${d.seqDependente}</td>
                    <td class="column1"><fmt:formatDate value="${d.dataNascimento}" pattern="dd/MM/yyyy" /></td>
                    <c:choose>
                        <c:when test='${d.masculino}'>
                            <td class="column1">M</td>
                        </c:when>
                        <c:otherwise>
                            <td class="column1">F</td>
                        </c:otherwise>
                    </c:choose>
                    <c:forEach var="tipo" items="${tipos}">
                        <c:if test='${d.idTipoDependente == tipo.id}'><td class="column1">${tipo.descricao}</td></c:if>
                    </c:forEach>
                </tr>
            </c:forEach>
        </tbody>
    </table>

</body>
</html>
