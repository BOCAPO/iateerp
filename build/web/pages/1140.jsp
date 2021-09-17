



<%@include file="head.jsp"%>

<style type="text/css">
    table.fmt {border:none;width:0;padding:0px;margin-left:0em;margin-top:0em;}
    table.fmt tr td {border:none;background:none;padding:0px;margin:0em auto;}
    
    #historico {
        left: 40%;
        top: 40%;
        position: fixed;
    }
</style>  

<body class="internas">

    <script type="text/javascript" language="JavaScript" src="js/format.js"></script>
    <script type="text/javascript" language="JavaScript">
        $(document).ready(function () {
                $('#tabela tr:gt(0)').css('background', 'white');
                
                $('#tabela tr:gt(0)').hover(function() {
                        $(this).css('background','#f4f9fe');
                }, function() {
                        $(this).css('background','white');
                })
                
                $('#historico').hide();
                $('#divSenha').hide();

        });        

        function cancelaSenha() {
            $("#divSenha").hide();
        }        
        function confirmaSenha() {
            var ret="";
            $.ajax({url:'TaxaIndividualAjax', type:'GET',async:false,
                                data:{
                                tipo:10,
                                matricula:$('#matricula').val(),
                                dependente:$('#seqDependente').val(),
                                categoria:$('#idCategoria').val(),
                                senha:$('#senhaSocio').val()
                                }
            }).success(function(retorno){
                ret = retorno;
            });
            if (ret!="OK"){
                alert(ret);
                return false;
            }

            $("#divSenha").hide();
            $("#senha").val("S");
            
            document.forms[0].submit();
        }        

        function validarForm(){
            document.forms[0].submit();
        }
        
        function emprestimo(){
            if(document.forms[0].quantidade.value == ''){
                alert('Informe a quantidade!');
                return;
            }
            if (isNaN(document.forms[0].quantidade.value)){
                alert('Quantidade inválida!');
                return;
            }
            
            if ($('#seqDependente').val()>0){
                var ret = "";
                $.ajax({url:'MaterialAjax', async:false, dataType:'text', type:'GET',
                                    data:{
                                    tipo:1,
                                    matricula:$('#matricula').val(),
                                    seqDependente:$('#seqDependente').val(),
                                    idCategoria:$('#idCategoria').val(),
                                    idMaterial:$('#idMaterial').val()
                                    }
                }).success(function(retorno){
                    ret = retorno;
                });
                
                if (ret!=""){
                    alert(ret);
                    return false;
                }
            }    
            
            
            $('#app').val('1141');
            
            if ($('#app1145').val()=='true'){
                if (confirm('Deseja que o empréstimo seja autenticado por senha?')){
                    $('#senhaSocio').val('');
                    $('#divSenha').show();
                    $('#senhaSocio').focus();
                }else{
                    document.forms[0].submit();
                }
            }else{
                $('#senhaSocio').val('');
                $('#divSenha').show();
                $('#senhaSocio').focus();
            }
        }
        
        function devolucao(){
            selecionado = false;

            $("input:checkbox").each(function(){
                if (this.checked == true) {
                    selecionado = true;
                    return
                }            

            });

            if (!selecionado){
                alert('Nenhum emprestimo foi selecionado!');
                return;
            }
            
            $('#app').val('1161');
            document.forms[0].submit();
        }
        function mostraDivHistorico(){
            $('#historico').show();
        }
        function submetePesquisa(){

            $('#acao').val($('input[name=tipoHist]:checked').val());

            document.forms[0].submit();
        }
    </script>  

    
    <%@include file="menu.jsp"%>
        
    
    <div class="divisoria"></div>
    <div id="titulo-subnav">Empréstimo de Material - Entrega/Devolução</div>
    <div class="divisoria"></div>
    <form action="c" method="POST">
        <input type="hidden" name="app" id="app" value="1140">
        <input type="hidden" name="acao" id="acao" value="listaMateriais">

        <input type="hidden" name="matricula" id="matricula" value="${socio.matricula}">
        <input type="hidden" name="seqDependente" id="seqDependente" value="${socio.seqDependente}">
        <input type="hidden" name="idCategoria" id="idCategoria" value="${socio.idCategoria}">
        <input type="hidden" name="titulo" value="${titulo}">
        <input type="hidden" name="app1145" id="app1145" value="${app1145}">
        <input type="hidden" name="senha" id="senha" value="">

        <table class="fmt" align="left" >
            <tr>
              <td colspan="2">
                  <p class="legendaCodigo MargemSuperior0" >Nome</p>
                  <input type="text" name="nome" disabled class="campoSemTamanho alturaPadrao" style="width:330px" value="${socio.nome}">
              </td>
            </tr>
            <tr>
              <td>
                <p class="legendaCodigo MargemSuperior0">Material</p>
                <div class="selectheightnovo2">
                  <select name="idMaterial" id="idMaterial" class="campoSemTamanho alturaPadrao" style="width:245px">
                      <c:forEach var="material" items="${materiais}">
                          <option value="${material.id}">${material.descricao}</option>
                      </c:forEach>
                  </select>
                </div>        
              </td>
              <td>
                  <p class="legendaCodigo MargemSuperior0" >Quantidade</p>
                  <input type="text" name="quantidade" class="campoSemTamanho alturaPadrao" style="width:70px" value="">
              </td>
            </tr>
            <tr>
              <td colspan="2">
                  <br>
                  <input type="checkbox"  onclick="validarForm()" name="todosEmp" class="legendaCodigo" value="S" <c:if test='${todosEmp == "S"}'>checked</c:if>>Mostrar todos os empréstimos
              </td>

            </tr>
        </table>
        
        <table id="tabela" style="width:98%;margin-left:15px;">
            <thead>
                <tr class="odd">
                    <th scope="col" >Selecionar</th>
                    <th scope="col" class="nome-lista">Material</th>
                    <th scope="col">Dt. Empréstimo</th>
                    <th scope="col">Usuário</th>
                    <th scope="col">Dt. Devolução</th>
                </tr>
            </thead>
            <tbody>
            <c:forEach var="emprestimo" items="${emprestimos}">
                <tr>
                    <td id="tabSel" class="column1" align="center" style="width:30px">
                        <c:if test='${emprestimo.dtDevolucao == null}'>
                            <input type="checkbox" name="sel"  value="${emprestimo.id}" />
                        </c:if>
                        
                            
                    </td>

                    <td class="column1">${emprestimo.descricao}</td>
                    <fmt:formatDate var="dataEmp" value="${emprestimo.dtEmprestimo}" pattern="dd/MM/yyyy" />
                    <td class="column1" align="center">${dataEmp}</td>
                    <td class="column1" align="center">${emprestimo.usuario}</td>
                    <fmt:formatDate var="dataDevol" value="${emprestimo.dtDevolucao}" pattern="dd/MM/yyyy" />
                    <td class="column1" align="center" dataDevol>${dataDevol}</td>

                </tr>
            </c:forEach>
            </tbody>
        </table>
        
        <input type="button" onclick="emprestimo()"id="atualizar" class="botaoEmprestimo" value=" " />
        <input type="button" onclick="devolucao()" id="atualizar" class="botaoDevolucao" value=" " />
        <input type="button" onclick="mostraDivHistorico()" id="atualizar" class="botaoHistorico" value=" " />
        <input type="button" class="botaoVoltar"  onclick="window.location='c?app=1140';" value=" " />


        <!--
            ************************************************************************
            ************************************************************************
            ************************************************************************
                                     H I S T O R I C O
            ************************************************************************
            ************************************************************************
            ************************************************************************
        -->
        <div id="historico" >
            <table style="background:#fff">
                <tr>
                  <td>
                    <div class="divisoria"></div>
                    <div id="titulo-subnav">Tipo de Histórico</div>
                    <div class="divisoria"></div>
                    <table class="fmt" align="left" align="left">
                      <tr>
                        <td>
                            <input type="radio" id="tipoHist" name="tipoHist" checked value="EMP"/>Empréstimo<br>
                            <input type="radio" id="tipoHist" name="tipoHist" value="DEV"/>Devolução<br>
                        <td>
                      </tr>
                      <tr>
                        <td align="center">
                            <br>
                            <input type="button" id="cmdPesquisa" name="cmdPesquisa" class="botaoatualizar" onclick="submetePesquisa()" />
                        </td>
                      </tr>
                    </table>  
                </td>
              </tr>
            </table>
        </div>        

        <!--
            ************************************************************************
            ************************************************************************
            ************************************************************************
                                       S E N H A 
            ************************************************************************
            ************************************************************************
            ************************************************************************
        -->
        <div id="divSenha" style="overflow:auto;left: 25%;top: 20%;position: fixed;width: 500px; height:400px;">
            <table style="background:#fff">
                <tr>
                  <td>
                    <div class="divisoria"></div>
                    <div id="titulo-subnav">Senha</div>
                    <div class="divisoria"></div>

                    <table class="fmt" align="left" >
                        <tr>
                            <td>
                                <p class="legendaCodigo MargemSuperior0" >Senha</p>
                                <input type="password" id="senhaSocio" name="senhaSocio" class="campoSemTamanhoNegrito" style="width: 100px; height:30px;"value="">
                            </td>
                        </tr>
                    </table>
                    <br><br><br><br>
                    <table class="fmt" align="left" >
                        <tr>
                            <td>
                                <input type="button" id="cmdAtualizarSenha" name="cmdAtualizarSenha"   class="botaoatualizar" onclick="confirmaSenha()" />
                            </td>
                            <td>
                                <input style="margin-left:15px;margin-top: -5px;" type="button" id="cmdCancelarSenha" name="cmdCancelarSenha" class="botaocancelar" onclick="cancelaSenha()" />
                            </td>
                        </tr>
                    </table>

                </td>
              </tr>
            </table>
        </div>                
        
    </form>
        
</body>
</html>
