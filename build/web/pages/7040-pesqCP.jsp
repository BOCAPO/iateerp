
<%@include file="head.jsp"%>

<style type="text/css">
    table.fmt {border:none;width:0;padding:0px;margin-left:0em;margin-top:0em;}
    table.fmt tr td {border:none;background:none;padding:0px;margin:0em auto;}
</style>  

<script type="text/javascript" language="JavaScript">
    $(document).ready(function () {
            $('#tabela tr:gt(0)').css('background', 'white');
            
            $("#foto").live("click", selecionar);
            $("#categoria").focus();
                
    });        
    
    function validarForm(){
        var matricula = trim(document.forms[0].matricula.value);
        var categoria = trim(document.forms[0].categoria.value);
        var nome = trim(document.forms[0].nome.value);
        
        if(nome == '' && (matricula == '' || categoria == 0)){
            alert('Informe o nome ou a categoria e matrícula para pesquisa!');
            return false;
        } else if(nome != '' && nome.length < 3){
            alert('Nome para pesquisa deve ter no mínimo 3 letras.');
            return false;
        } else {
            $('#acao').val('pesqCP');
        }
    }
    
    function selecionar(){
        var chk = $(this).closest('tr').find('input:checkbox');
        
        if (chk.attr('checked')){
            chk.attr('checked', false);
        }else{
            chk.attr('checked', true);
        }
    }
    
    function validaSelecao(){
        selecao = new Array();
        
        $("input:checkbox").each(function(){
            if (this.checked == true) {
		selecao.push($(this).val());
            }            
            
        });
        var tamanho = selecao.length;
        
        if (tamanho==0){
            alert('Nenhuma Pessoa foi selecionada.');
            return;
        }

        for ( var i = 0 ; i < tamanho ; i++ ) {
          if (selecao[i].substring(4, 6) == 90 && tamanho > 1){
            alert('Não é possível selecionar várias pessoas quando forem não sócias.');
            return;
          }
        }        
        
        var mat = selecao[0].substring(0, 4);
        var cat = selecao[0].substring(4, 6);
        var dep = selecao[0].substring(6, 8);
        
        for ( var i = 1 ; i < tamanho ; i++ ) {
          if (selecao[i].substring(4, 6) != cat || selecao[i].substring(0, 4) != mat){
            alert('Só é possível selecionar várias pessoas quando forem do mesmo título.');
            return;
          }
        }        
        
        if (cat==90){
            $.ajax({url:'EntradaAjax', async:false, dataType:'text', type:'GET',data:{
                                tipo:1,
                                matricula:mat,
                                categoria:cat,
                                dependente:dep}
            }).success(function(retorno){
                if (retorno==0){
                    alert('Pessoa não matriculada em Curso algum, ou sem Passaporte emitido.');
                    return;
                } 
            });
        }
        
        document.forms[0].submit();
    }
    
</script>  


<body class="internas">

    <script type="text/javascript" language="JavaScript" src="js/format.js"></script>
    
    <%@include file="menuAcesso.jsp"%>
        
    <div class="divisoria"></div>
    <div id="titulo-subnav">Seleção de Carteira/Passaporte</div>
    <div class="divisoria"></div>
    
    
    <form action="c" method="POST" onsubmit="return validarForm()">
        <input type="hidden" name="app" value="7040">
        <input type="hidden" name="acao" id="acao" value="acesso">
        <input type="hidden" name="origem" value="S">
        <input type="hidden" name="tipoOrigem" value="CP">
        <input type="hidden" name="idLocal" value="${idLocal}">
        <input type="hidden" name="placa" value="${placa}">
        <input type="hidden" name="qtd" value="${qtd}">
        <input type="hidden" name="entradaSaida" value="${entradaSaida}">

        <table class="fmt" align="left" >
            <tr>
                <td>
                    <p class="legendaCodigo MargemSuperior0">Categoria</p>
                    <div class="selectheightnovo">
                        <select name="categoria" id="categoria" class="campoSemTamanho">
                            <c:forEach var="cat" items="${categorias}">
                                <option value="${cat.id}" <c:if test='${categoria == cat.id}'>selected</c:if>>${cat.descricao}</option>
                            </c:forEach>
                        </select>
                    </div>        
                </td>
                
                <td>
                    <p class="legendaCodigo MargemSuperior0" >Matricula</p>
                    <input type="text" name="matricula" class="campoSemTamanho alturaPadrao larguraNumero" value="${matricula}">
                </td>
                <td>
                    <p class="legendaCodigo MargemSuperior0" >Nome</p>
                    <input type="text" name="nome" class="campoSemTamanho alturaPadrao" style="width:300px" value="${nome}">
                </td>
                <td >    
                    <input type="submit" class="botaobuscainclusao" style="margin-top:20px" onclick="return validarForm()" value="" title="Consultar" />
                </td>
                <td > 
                    &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
                    <input type="button" id="selecionar" name="selecionar" style="margin-top:15px" class="botaoSelecionar" onclick="validaSelecao()"  value=" " />
                    <input type="button" id="voltar" name="voltar" style="margin-top:15px" class="botaoVoltar" onclick="window.location='c?app=7040&acao=showForm&idLocal=${idLocal}&placa=${placa}&qtd=${qtd}';" value=" " />
                </td>
            </tr>
        </table>
        <br><br>
        
        <table id="tabela" style="width:98%;margin-left:15px;">
            <thead>
                <tr class="odd">
                    <th scope="col">Pessoa</th>
                    <th scope="col">Pessoa</th>
                </tr>	
            </thead>
            <tbody>
                <c:set var="i" value="0" scope="page" />
                <c:forEach var="socio" items="${socios}">
                    
                    <fmt:formatNumber var="mat" type="number" pattern="0000" value="${socio.matricula}" />
                    <fmt:formatNumber var="cat" type="number" pattern="00" value="${socio.idCategoria}" />
                    <fmt:formatNumber var="dep" type="number" pattern="00" value="${socio.seqDependente}" />

                    <c:if test='${socio.idCategoria != 91 && socio.idCategoria != 99}'>
                        <c:if test='${i == 0}'>
                            <tr valign="midle">
                        </c:if>
                        <td>
                            <table class="fmt">
                                <tr>
                                    <td>
                                        <input type="checkbox" class="legendaCodigo MargemSuperior0" name="sel" id="sel" value="${mat}${cat}${dep}">
                                    </td>
                                    <td>
                                        <img id="foto" name="foto" src="f?tb=6&mat=${socio.matricula}&seq=${socio.seqDependente}&cat=${socio.idCategoria}" class="recuoPadrao MargemSuperiorPadrao" width="120" height="160" onerror="this.src='images/tenis/avatar-default.png';"><BR>
                                    </td>
                                    <td>
                                        <table class="fmt">
                                            <tr>
                                                <td>
                                                    &nbsp;&nbsp;
                                                    <c:choose>
                                                          <c:when test='${socio.alerta==1}'>
                                                              <img src="img/socio-alerta1.png" title="Situação Especial"/>
                                                          </c:when>
                                                          <c:when test='${socio.alerta==2}'>
                                                              <img src="img/socio-alerta2.png" title="Excluído(a)" />
                                                          </c:when>
                                                    </c:choose>                        
                                                </td>
                                                <td>
                                                    &nbsp;&nbsp; ${socio.matricula}/${socio.idCategoria} - ${socio.nome} (${cat}${mat}${dep})
                                                </td>
                                            </tr>
                                        </table>
                                    </td>
                                </tr>
                            </table>
                        </td>
                        <c:choose>
                              <c:when test='${i == 0}'>
                                  <c:set var="i" value="1" scope="page" />
                              </c:when>
                              <c:otherwise>
                                  </tr>
                                  <c:set var="i" value="0" scope="page" />
                              </c:otherwise>
                        </c:choose>                        
                    </c:if>
                </c:forEach>
            </tbody>
        </table>
        
    </form>
        

    
    
</body>
</html>
