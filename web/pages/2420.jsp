<%@include file="head.jsp"%>

<style type="text/css">
    table.fmt {border:none;width:0;padding:0px;margin-left:0em;margin-top:0em;}
    table.fmt tr td {border:none;background:none;padding:0px;margin:0em auto;}
</style>  

<script type="text/javascript" LANGUAGE="JavaScript" SRC="js/calendario.js"></script>
<link rel="stylesheet" type="text/css" href="css/calendario.css"/>

<script type="text/javascript" language="JavaScript" src="js/format.js"></script>
    
<script type="text/javascript" language="JavaScript">
    $(document).ready(function () {
        $("#dtEntrada").mask("99/99/9999");
        $("#dtCatalogacao").mask("99/99/9999");
        $("#dtDevolucao").mask("99/99/9999");

        $("#pesqSocio").hide();
    });     

    function pesquisaSocio() {
        if($("#perfilRetirante").val() == 0){
            $("#pesqSocio").show();
            var tabela = $('#tabelaSocio').find('tbody').empty();
            $("#nomeSocio").val('');
            $("#matriculaSocio").val('');
            $("#categoriaSocio").val('');
        }else{
            alert('Pesquisa apenas para sócios!');
            return false;
        }
    }
    function cancelaPesquisaSocio() {
        $("#pesqSocio").hide();
    }        
    function selecionaSocio(indice){
        $("#pesqSocio").hide();
        $("#tabelaSocio tr:eq("+(parseInt(indice)+1)+")").each(function(index){
            $('#nomeRetirante').val($(this).find('#nome2').val());
        });
    }

    function carregaSocio(lugar){
        $.ajax({url:'ReservaLugarEventoAjax', type:'GET',
                            data:{
                            nome:$('#nomeSocio').val(),
                            matricula:$('#matriculaSocio').val(),
                            categoria:$('#categoriaSocio').val()
                            }
        }).success(function(retorno){
            if (retorno.erro !== undefined){
                alert(retorno.erro);
            }else {
                var tabela = $('#tabelaSocio').find('tbody').empty();
                var linha="";
                var campoHidden="";
                var campoHidden2="";
                var tpCadastro="";
                var tpCategoria="";
                $.each(retorno.jogador, function(i) {
                    if (this.dependente==0){
                        tpCadastro="TITULAR";
                    }else{
                        tpCadastro="DEPENDENTE";
                    }
                    if (this.tpCategoria=="SO"){
                        tpCategoria="SÓCIO";
                    }else{
                        tpCategoria="NÃO SÓCIO";
                    }

                    campoHidden='<input type="hidden" id="campoTitulo"  value="'+this.titulo+'"/>'
                    campoHidden2='<input type="hidden" id="nome2"  value="'+this.nome2+'"/>'
                    linha='<tr><td class="column1"><a href="#" id="tabNome" class="column1" onclick="selecionaSocio('+i+')">'+this.nome+'</a> '+campoHidden2+'</td><td class="column1" align="center">'+this.matricula+' '+campoHidden+'</td><td class="column1">'+tpCadastro+'</td><td class="column1">'+this.descricao+'</td><td class="column1">'+tpCategoria+'</td></tr>';
                    tabela.append(linha)
                });
            }
        });    
    }
    
    function LimpaNomeRetirante(){
        $('#nomeRetirante').val('');
        if($("#perfilRetirante").val() == 0){
            $('#nomeRetirante').attr('readonly', true);
        }else{
            $('#nomeRetirante').attr('readonly', false);
        }
    }

    function validarForm(){

        if(document.forms[0].descricao.value == ''){
            alert('Informe a descrição do objeto!');
            document.forms[0].descricao.focus();
            return false;
        }

        if(!isDataValida(document.forms[0].dtEntrada.value)){
            return;
        }
        if(!isDataValida(document.forms[0].dtCatalogacao.value)){
            return;
        }
        if(!isDataValida(document.forms[0].dtDevolucao.value)){
            return;
        }
        
        if($('#situacao').val()==1){
        //DEVOLVIDO
            if($('#nomeRetirante').val()==''){
                alert('Para os objetos devolvidos informe pelo menos o Nome do Retirante!');
                return false;
            }
            
            if (confirm('Deseja imprimir o termo de devolução?')){
                $('#imprimeTermo').val("S");
            }else{
                $('#imprimeTermo').val("N");
            }
        }else{
            if ($('#nomeRetirante').val().trim()!='' || $('#docRetirante').val().trim()!='' || $('#telRetirante').val().trim()!='' || $('#funcDevolucao').val()!=0 || $('#dtDevolucao').val().trim()!=''){
                alert('Informe os dados do retirante apenas para objetos em situação de devolvido!');
                return false
            }
        }
        
        document.forms[0].submit();

    }
    
</script>

<body class="internas">
            
    <%@include file="menu.jsp"%>
    
    <div class="divisoria"></div>
    <c:if test='${app == "2421"}'>
        <div id="titulo-subnav">Inclusão de Objeto - Dados Cadastrais</div>
    </c:if>
    <c:if test='${app == "2422"}'>
        <div id="titulo-subnav">Alteração de Objeto - Dados Cadastrais</div>
    </c:if>
    <div class="divisoria"></div>
    
    <form  action="c" method="POST">
        <input type="hidden" name="app" value="${app}">
        <input type="hidden" name="acao" value="gravar">
        <input type="hidden" name="imprimeTermo" id="imprimeTermo" value="">

        <table align="left" class="fmt">
            
            <tr>
                <td>
                    <table align="left" class="fmt">
                      <tr>
                        <td >
                            <p class="legendaCodigo MargemSuperior0">Nr. Ident.</p>
                            <input type="text" name="id" id="id" class="campoSemTamanho alturaPadrao" style="width: 50px" readonly="" value="${objeto.id}">
                        </td>
                        <td>
                            <p class="legendaCodigo MargemSuperior0" >Descrição</p>
                            <input type="text" id="descricao" name="descricao" class="campoSemTamanho alturaPadrao" style="width: 250px" value="${objeto.descricao}">
                        </td>
                        <td>
                          <p class="legendaCodigo MargemSuperior0" >Setor de Origem</p>
                          <div class="selectheightnovo">
                            <select name="setorOrigem" id="setor" class="campoSemTamanho alturaPadrao"  style="width: 200px">
                              <option value="0" selected>Não informado</option>
                              <c:forEach var="setor" items="${setores}">
                                  <option value="${setor.id}" <c:if test='${objeto.setorOrigem.id == setor.id}'>selected</c:if>>${setor.descricao}</option>
                              </c:forEach>
                            </select>
                          </div>
                        </td>
                        <td >
                            <p class="legendaCodigo MargemSuperior0">Quant.</p>
                            <input type="text" name="qtObjeto" id="qtObjeto" class="campoSemTamanho alturaPadrao" style="width: 30px" onkeypress="onlyNumber(event)" value="${objeto.qtObjeto}">
                        </td>
                        <td>
                          <p class="legendaCodigo MargemSuperior0" >Unidade</p>
                          <div class="selectheightnovo">
                            <select name="unidade" id="unidade" class="campoSemTamanho alturaPadrao"  style="width: 110px">
                              <option value="0" selected>Não informado</option>
                              <option value="1" <c:if test='${objeto.unidade == 1}'>selected</c:if>>Unidade</option>
                              <option value="2" <c:if test='${objeto.unidade == 2}'>selected</c:if>>Conjunto</option>
                              <option value="3" <c:if test='${objeto.unidade == 3}'>selected</c:if>>Peça</option>
                              <option value="4" <c:if test='${objeto.unidade == 4}'>selected</c:if>>Kit</option>
                              <option value="5" <c:if test='${objeto.unidade == 5}'>selected</c:if>>Par</option>
                              <option value="6" <c:if test='${objeto.unidade == 6}'>selected</c:if>>Outros</option>
                            </select>
                          </div>
                        </td>
                        <td>
                            <p class="legendaCodigo MargemSuperior0" >Condição do Objeto</p>
                            <input type="text" id="condicao" name="condicao" class="campoSemTamanho alturaPadrao" style="width: 110px" value="${objeto.condicao}">
                        </td>
                      </tr>
                    </table>
                </td>
            </tr>
            <tr>
              <td>
                    <table align="left" class="fmt">
                      <tr>
                        <td>
                          <p class="legendaCodigo MargemSuperior0" >Definição</p>
                          <div class="selectheightnovo">
                            <select name="definicao" id="unidade" class="campoSemTamanho alturaPadrao"  style="width: 200px">
                                <option value="0" selected>Não informado</option>
                                <option value="1" <c:if test='${objeto.definicao == 1}'>selected</c:if>>Moda Praia</option>
                                <option value="2" <c:if test='${objeto.definicao == 2}'>selected</c:if>>Acessório Esportivo</option>
                                <option value="3" <c:if test='${objeto.definicao == 3}'>selected</c:if>>Acessório Aquático</option>
                                <option value="4" <c:if test='${objeto.definicao == 4}'>selected</c:if>>Vestiário</option>
                                <option value="5" <c:if test='${objeto.definicao == 5}'>selected</c:if>>Outros</option>
                                <option value="6" <c:if test='${objeto.definicao == 6}'>selected</c:if>>Acessórios</option>
                                <option value="7" <c:if test='${objeto.definicao == 7}'>selected</c:if>>Brinquedos</option>
                                <option value="8" <c:if test='${objeto.definicao == 8}'>selected</c:if>>Eletrônico</option>
                                <option value="9" <c:if test='${objeto.definicao == 9}'>selected</c:if>>Material escolar</option>
                                <option value="10" <c:if test='${objeto.definicao == 10}'>selected</c:if>>Moda praia</option>
                                <option value="11" <c:if test='${objeto.definicao == 11}'>selected</c:if>>Vestuário</option>
                                <option value="12" <c:if test='${objeto.definicao == 12}'>selected</c:if>>Acessórios esportivos</option>
                                <option value="13" <c:if test='${objeto.definicao == 13}'>selected</c:if>>Acessórios aquáticos</option>
                                <option value="14" <c:if test='${objeto.definicao == 14}'>selected</c:if>>Chapelaria</option>
                                <option value="15" <c:if test='${objeto.definicao == 15}'>selected</c:if>>Higiene e Limpeza</option>
                                <option value="16" <c:if test='${objeto.definicao == 16}'>selected</c:if>>Bolsas</option>
                                <option value="17" <c:if test='${objeto.definicao == 17}'>selected</c:if>>Calçados</option>
                                <option value="18" <c:if test='${objeto.definicao == 18}'>selected</c:if>>Óculos</option>
                                <option value="19" <c:if test='${objeto.definicao == 19}'>selected</c:if>>Saboneteira</option>
                                <option value="20" <c:if test='${objeto.definicao == 20}'>selected</c:if>>Acessórios diversos</option>
                                <option value="21" <c:if test='${objeto.definicao == 21}'>selected</c:if>>Utensílios de cozinha</option>
                                <option value="22" <c:if test='${objeto.definicao == 22}'>selected</c:if>>Mesa e banho</option>
                                <option value="23" <c:if test='${objeto.definicao == 23}'>selected</c:if>>Bolas em geral</option>
                                <option value="24" <c:if test='${objeto.definicao == 24}'>selected</c:if>>Garrafas</option>
                                <option value="25" <c:if test='${objeto.definicao == 25}'>selected</c:if>>Acessórios de natação</option>
                              
                            </select>
                          </div>
                        </td>
                        <td>
                          <p class="legendaCodigo MargemSuperior0" >Setor Encontrado</p>
                          <div class="selectheightnovo">
                            <select name="setorEncontrado" id="setor" class="campoSemTamanho alturaPadrao"  style="width: 200px">
                              <option value="0" selected>Não informado</option>
                              <c:forEach var="setor" items="${setores}">
                                  <option value="${setor.id}" <c:if test='${objeto.setorEncontrado.id == setor.id}'>selected</c:if>>${setor.descricao}</option>
                              </c:forEach>
                            </select>
                          </div>
                        </td>
                        <td>
                          <p class="legendaCodigo MargemSuperior0" >Modelo</p>
                          <div class="selectheightnovo">
                            <select name="modelo" id="modelo"  class="campoSemTamanho alturaPadrao"  style="width: 100px">
                              <option value="0" selected>Não informado</option>
                              <option value="1" <c:if test='${objeto.modelo == 1}'>selected</c:if>>Infantil</option>
                              <option value="2" <c:if test='${objeto.modelo == 2}'>selected</c:if>>Juvenil</option>
                              <option value="3" <c:if test='${objeto.modelo == 3}'>selected</c:if>>Adulto</option>
                            </select>
                          </div>
                        </td>
                        <td>
                          <p class="legendaCodigo MargemSuperior0" >Sexo</p>
                          <div class="selectheightnovo">
                            <select name="sexo" id="sexo" class="campoSemTamanho alturaPadrao"  style="width: 100px">
                              <option value="0" selected>Não informado</option>
                              <option value="1" <c:if test='${objeto.sexo == 1}'>selected</c:if>>Feminino</option>
                              <option value="2" <c:if test='${objeto.sexo == 2}'>selected</c:if>>Masculino</option>
                              <option value="3" <c:if test='${objeto.sexo == 3}'>selected</c:if>>Unisex</option>
                            </select>
                          </div>
                        </td>
                        <td>
                            <p class="legendaCodigo MargemSuperior0" >Marca</p>
                            <input type="text" id="marca" name="marca" class="campoSemTamanho alturaPadrao" style="width: 178px" value="${objeto.marca}">
                        </td>

                      </tr>
                    </table>
              </td>
            </tr>
            <tr>
              <td>
                    <table align="left" class="fmt">
                      <tr>
                        <td>
                            <p class="legendaCodigo MargemSuperior0" >Cor</p>
                            <input type="text" id="cod" name="cor" class="campoSemTamanho alturaPadrao" style="width: 118px" value="${objeto.cor}">
                        </td>
                        <td>
                          <p class="legendaCodigo MargemSuperior0" >Tamanho</p>
                          <div class="selectheightnovo">
                            <select name="tamanho" id="tamanho" class="campoSemTamanho alturaPadrao"  style="width: 80px">
                              <option value="0" selected>Não informado</option>
                              <option value="1" <c:if test='${objeto.tamanho == 1}'>selected</c:if>>PP</option>
                              <option value="2" <c:if test='${objeto.tamanho == 2}'>selected</c:if>>P</option>
                              <option value="3" <c:if test='${objeto.tamanho == 3}'>selected</c:if>>M</option>
                              <option value="4" <c:if test='${objeto.tamanho == 4}'>selected</c:if>>G</option>
                              <option value="5" <c:if test='${objeto.tamanho == 5}'>selected</c:if>>GG</option>
                              <option value="6" <c:if test='${objeto.tamanho == 6}'>selected</c:if>>XG</option>
                            </select>
                          </div>
                        </td>
                        <td>
                          <p class="legendaCodigo MargemSuperior0" >Dt. Entrada</p>
                          <fmt:formatDate var="dataEntrada" value="${objeto.dtEntrada}" pattern="dd/MM/yyyy"/>
                          <input type="text" name="dtEntrada" id="dtEntrada" class="campoSemTamanho alturaPadrao" style="width: 60px" value="${dataEntrada}">
                        </td>
                        <td>
                          <p class="legendaCodigo MargemSuperior0" >Dt. Catalog.</p>
                          <fmt:formatDate var="dataCatalogacao" value="${objeto.dtCatalogacao}" pattern="dd/MM/yyyy"/>
                          <input type="text" name="dtCatalogacao" id="dtCatalogacao" class="campoSemTamanho alturaPadrao" style="width: 60px" value="${dataCatalogacao}">
                        </td>
                        <td>
                          <p class="legendaCodigo MargemSuperior0" >Funcionário Catalogação</p>
                          <div class="selectheightnovo">
                            <select name="funcCatalogador" class="campoSemTamanho alturaPadrao"  style="width: 240px">
                              <option value="0" <c:if test='${objeto.funcCatalogador.id == 0}'>selected</c:if>>Não informado</option>
                              <c:forEach var="funcCatalog" items="${funcionarios}">
                                  <option value="${funcCatalog.id}" <c:if test='${objeto.funcCatalogador.id == funcCatalog.id}'>selected</c:if>>${funcCatalog.descricao}</option>
                              </c:forEach>
                            </select>
                          </div>
                        </td>
                        <td>
                            <p class="legendaCodigo MargemSuperior0" >Entregue por</p>
                            <input type="text" name="nomeEntrega" id="nomeEntrega" class="campoSemTamanho alturaPadrao" style="width: 135px" value="${objeto.nomeEntrega}">
                        </td>
                        <td >
                            <p class="legendaCodigo MargemSuperior0">Número da Prateleira</p>
                            <input type="text" name="nrPrateleira" id="nrPrateleira" class="campoSemTamanho alturaPadrao" style="width: 38px" onkeypress="onlyNumber(event)" value="${objeto.nrPrateleira}">
                        </td>

                      </tr>
                    </table>
              </td>
            </tr>
        </table>          
          
        <BR><BR><BR><BR><BR><BR><BR><BR><BR><BR>
        
        <div class="divisoria"></div>
        <div id="titulo-subnav">Devolução</div>
        <div class="divisoria"></div>


        <table align="left" class="fmt" >
            <tr>
              <td>
                <table align="left" class="fmt" >
                    <tr>
                      <td>
                         <table align="left" class="fmt">
                            <tr>
                                <td>
                                  <p class="legendaCodigo MargemSuperior0" >Situação</p>
                                  <div class="selectheightnovo">
                                    <select name="situacao" id="situacao" class="campoSemTamanho alturaPadrao"  style="width: 150px">
                                      <option value="0" <c:if test='${objeto.situacao == 0}'>selected</c:if>>Aguardando Retirada</option>
                                      <option value="1" <c:if test='${objeto.situacao == 1}'>selected</c:if>>Devolvido</option>
                                      <option value="2" <c:if test='${objeto.situacao == 2}'>selected</c:if>>Extraviado</option>
                                      <option value="3" <c:if test='${objeto.situacao == 3}'>selected</c:if>>Processo de Adoção</option>
                                    </select>
                                  </div>
                                </td>
                                <td>
                                  <p class="legendaCodigo MargemSuperior0" >Perfil</p>
                                  <div class="selectheightnovo">
                                    <select name="perfilRetirante" id="perfilRetirante" class="campoSemTamanho alturaPadrao" onchange="LimpaNomeRetirante()" style="width: 100px">
                                      <option value="0" <c:if test='${objeto.perfilRetirante == 0}'>selected</c:if>>Sócio</option>
                                      <option value="1" <c:if test='${objeto.perfilRetirante == 1}'>selected</c:if>>Convidado</option>
                                      <option value="2" <c:if test='${objeto.perfilRetirante == 2}'>selected</c:if>>Prestador</option>
                                      <option value="3" <c:if test='${objeto.perfilRetirante == 3}'>selected</c:if>>Outros</option>
                                    </select>
                                  </div>
                                </td>
                                <td>
                                    <p class="legendaCodigo MargemSuperior0" >Nome</p>
                                    <input type="text" id="nomeRetirante" name="nomeRetirante" <c:if test='${objeto.perfilRetirante == 0 || app == 2421}'>readonly</c:if> class="campoSemTamanho alturaPadrao" style="width: 278px" value="${objeto.nomeRetirante}">
                                </td>
                                <td>
                                    <input class="botaobuscainclusao" style="margin-top:20px" type="button" onclick="pesquisaSocio()" value="" title="Consultar" />
                                </td>
                                <td>
                                    <p class="legendaCodigo MargemSuperior0" >Documento</p>
                                    <input type="text" id="docRetirante" name="docRetirante" class="campoSemTamanho alturaPadrao" style="width: 100px" value="${objeto.docRetirante}">
                                </td>
                            </tr>
                        </table>  
                      </td>
                    </tr>
                    <tr>
                      <td>
                         <table align="left" class="fmt">
                            <tr>
                                <td>
                                    <p class="legendaCodigo MargemSuperior0" >Telefone</p>
                                    <input type="text" id="telRetirante" name="telRetirante" class="campoSemTamanho alturaPadrao" style="width: 150px" value="${objeto.telRetirante}">
                                </td>
                                <td>
                                  <p class="legendaCodigo MargemSuperior0" >Funcionário Devolução</p>
                                  <div class="selectheightnovo">
                                    <select name="funcDevolucao" id="funcDevolucao" class="campoSemTamanho alturaPadrao"  style="width: 305px">
                                      <option value="0" <c:if test='${objeto.funcDevolucao.id == 0}'>selected</c:if>>Não informado</option>
                                      <c:forEach var="funcDev" items="${funcionarios}">
                                          <option value="${funcDev.id}" <c:if test='${objeto.funcDevolucao.id == funcDev.id}'>selected</c:if>>${funcDev.descricao}</option>
                                      </c:forEach>
                                    </select>
                                  </div>
                                </td>
                                <td>
                                  <p class="legendaCodigo MargemSuperior0" >Dt. Devolução</p>
                                  <fmt:formatDate var="dataDevolucao" value="${objeto.dtDevolucao}" pattern="dd/MM/yyyy"/>
                                  <input type="text" name="dtDevolucao" id="dtDevolucao" class="campoSemTamanho alturaPadrao" style="width: 100px" value="${dataDevolucao}">
                                </td>
                                <td>
                                  <p class="legendaCodigo MargemSuperior0" >Dt. Prev. Adoção</p>
                                  <fmt:formatDate var="dataAdocao" value="${objeto.dtAdocao}" pattern="dd/MM/yyyy"/>
                                <input type="hidden" name="acao" value="${objeto.dtAdocao}">
                                  <input type="text" name="dtAdocao" id="dtAdocao" class="campoSemTamanho alturaPadrao" readonly style="width: 100px" value="${dataAdocao}">
                                </td>
                            </tr>
                        </table>  
                      </td>
                    </tr>
                </table>          
            </td>
            <c:if test='${app == "2422"}'>
                <td valign="midle"  >
                    &nbsp;&nbsp;&nbsp; <input type="button" class="botaoImprimirBemPequeno" onclick="window.location='c?app=2420&acao=imprimir&idObjeto=${objeto.id}'" value=" " />
                </td>
            </c:if>
            
          </tr>
        </table>          

        <br><br><br><br><br><br><br><br>
        <input type="button" class="botaoatualizar"  onclick="validarForm()" value=" " />
        <input type="button" class="botaoVoltar"  onclick="window.location='c?app=2420'" value=" " />

        <!--
            ************************************************************************
            ************************************************************************
            ************************************************************************
                               TABELA DE SELECAO DE SOCIO
            ************************************************************************
            ************************************************************************
            ************************************************************************
        -->

        <div id="pesqSocio" style="overflow:auto;left: 25%;top: 20%;position: fixed;width: 700px; height:400px;">
            <table style="background:#fff">
                <tr>
                    <td>
                        <div class="divisoria"></div>
                        <div id="titulo-subnav">Seleção de Sócio</div>
                        <div class="divisoria"></div>

                        <table class="fmt" align="left" >
                            <tr>
                                <td>
                                    <p class="legendaCodigo MargemSuperior0" >Categoria</p>
                                    <input type="text" id="categoriaSocio" name="categoriaSocio" class="campoSemTamanho alturaPadrao larguraNumero" value="">
                                </td>

                                <td>
                                    <p class="legendaCodigo MargemSuperior0" >Matricula</p>
                                    <input type="text" id="matriculaSocio" name="matriculaSocio" class="campoSemTamanho alturaPadrao larguraNumero" value="">
                                </td>
                                <td>
                                    <p class="legendaCodigo MargemSuperior0" >Nome</p>
                                    <input type="text" id="nomeSocio" name="nomeSocio" class="campoSemTamanho alturaPadrao" style="width:300px" value="">
                                </td>
                                <td >    
                                    <input type="button" class="botaobuscainclusao" style="margin-top:20px" onclick="carregaSocio()" value="" title="Consultar" />
                                </td>
                            </tr>
                        </table>
                        <br><br><br>
                        <table id="tabelaSocio" align="left" style="margin-left:15px;">
                            <thead>
                                <tr class="odd">
                                    <th scope="col" class="nome-lista">Nome</th>
                                    <th scope="col" align="left">Título</th>
                                    <th scope="col" align="left">Tp Cadastro</th>
                                    <th scope="col" align="left">Categoria</th>
                                    <th scope="col" align="left">Tp Categoria</th>
                                </tr>	
                            </thead>
                            <tbody>
                            </tbody>
                        </table>  
                        <br>
                        <input style="margin-left:15px;" type="button" id="cmdcancelar" name="cmdPesquisa" class="botaocancelar" onclick="cancelaPesquisaSocio()" />
                    </td>
                </tr>
            </table>
        </div>                

        
        
    </form>

</body>
</html>
