<%@include file="head.jsp"%>

<style type="text/css">
    table.fmt {border:none;width:0;padding:0px;margin-left:0em;margin-top:0em;}
    table.fmt tr td {border:none;background:none;padding:0px;margin:0em auto;}
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
                
                $("#divTipoImpressao").hide();
        });    
        
        function cancelaTipoImpressao(){
            $("#divTipoImpressao").hide();
        }
        
        function mostraTipoImpressao(id){
            $("#seqEmprestimo").val(id);
            $("#sel").val(id);
            $("#divTipoImpressao").show();
        }
        function defineTipoImpressao(){
            if ($("input[name='tipoImp']:checked").val() == 'tela'){
                if ($("#acao").val() == 'IMPEMP'){
                    $("#acao").val('IMPEMPTELA');
                }else{
                    $("#acao").val('IMPDEVTELA');
                }
            }
            document.forms[0].submit();
        }
    </script>  
    
    <%@include file="menu.jsp"%>
    
    <div class="divisoria"></div>
    <div id="titulo-subnav">${tituloPag}</div>
    <div class="divisoria"></div>
    
    <form action="c" method="POST">
       <input type="hidden" name="app" id="app" value="1140">
       <input type="hidden" name="acao" id="acao" value="${acao}">
       <input type="hidden" name="seqEmprestimo" id="seqEmprestimo" value="">
       <input type="hidden" name="sel" id="sel" value="">

        <input type="hidden" name="titulo" value="${titulo}">
        
        <table id="tabela" style="width:98%;margin-left:15px;">
            <thead>
                <tr class="odd">
                    <th scope="col" class="nome-lista">Material</th>
                    <th scope="col">Quantidade</th>
                    <th scope="col">Dt. Empréstimo</th>
                    <th scope="col">Usuário Empréstimo</th>

                    <c:if test='${acao == "IMPDEV"}'>
                        <th scope="col">Dt. Devolução</th>
                        <th scope="col">Usuário Devolução</th>
                    </c:if>
                    
                    <th scope="col">Comprovante</th>
                </tr>
            </thead>
            <tbody>
            <c:forEach var="item" items="${lista}">
                <tr>
                    <td id="tabNome" class="column1">${item.descricao}</td>
                    <td id="tabNome" class="column1" align="center">${item.qtTotal}</td>
                    
                    <fmt:formatDate var="dataEmp" value="${item.dtEmprestimo}" pattern="dd/MM/yyyy" />
                    <td id="tabNome" class="column1" align="center">${dataEmp}</td>
                    <td id="tabNome" class="column1" align="center">${item.usuario}</td>
                    
                    <c:if test='${acao == "IMPDEV"}'>
                        <fmt:formatDate var="dataDevol" value="${item.dtDevolucao}" pattern="dd/MM/yyyy" />
                        <td id="tabNome" class="column1" align="center">${dataDevol}</td>
                        <td id="tabNome" class="column1" align="center">${item.usuarioDevol}</td>
                    </c:if>

                    <td class="column1" align="center">
                        <a href="#" onclick="mostraTipoImpressao(${item.id})"><img src="imagens/icones/inclusao-titular-13.png" title="Imprime 2a via do comprovante"/></a>
                    </td>

                </tr>
            </c:forEach>
            </tbody>
        </table>
        
        <input type="submit" class="botaoVoltar"  onclick="validarForm()" value=" " />

    </form>

    <!--
        ************************************************************************
        ************************************************************************
        ************************************************************************
                                 TIPO DE IMPRESSAO
        ************************************************************************
        ************************************************************************
        ************************************************************************
    -->

    <div id="divTipoImpressao" style="overflow:auto;left: 25%;top: 20%;position: fixed;width: 420px; height:400px;" >
        <table style="background:#fff">
            <tr>
              <td>
                <div class="divisoria"></div>
                <div id="titulo-subnav">Tipo de Impressao</div>
                <div class="divisoria"></div>
                <table class="fmt" align="left" align="left">
                  <tr>
                    <td>
                        <input type="radio" id="tipoPesq" name="tipoImp" value="impressora" checked/>Impressora<br>
                        <input type="radio" id="tipoPesq" name="tipoImp" value="tela"/>Tela<br>
                    </td>
                    <td>
                        &nbsp;
                    </td>
                  </tr>
                  <tr>
                    <td>
                        <br>
                        <input type="button" id="cmdTipoPesquisa" name="cmdTipoPesquisa" class="botaoatualizar" value=" " onclick="defineTipoImpressao()" value="" />
                    </td>
                    <td>
                        <br>
                        <input type="button" style="margin-left:15px;margin-top: 0px;" id="cmdCancelaTipoPesquisa" name="cmdCancelaTipoImpressao" class="botaocancelar" value=" " onclick="cancelaTipoImpressao()" value="" />
                    </td>
                  </tr>
                </table>  
            </td>
          </tr>
        </table>
    </div>        
        
        
</body>
</html>
