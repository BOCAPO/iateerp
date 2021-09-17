<%@include file="head.jsp"%>

<body class="internas">
            
    <%@include file="menu.jsp"%>

    <style type="text/css">
        table.fmt {border:none;width:0;padding:0px;margin-left:0em;margin-top:0em;}
        table.fmt tr td {border:none;background:none;padding:0px;margin:0em auto;}
    </style>    
    
    <script type="text/javascript" language="javascript">
        $(document).ready(function () {
                $('#dataAdmissao').mask('99/99/9999');
                
                $('#tabela tr:gt(0)').css('background', 'white');

                $('#tabela tr:gt(0)').hover(function() {
                        $(this).css('background','#f4f9fe');
                }, function() {
                        $(this).css('background','white');
                })
        });        
    </script>            

    <div class="divisoria"></div>
    <div id="titulo-subnav">Histórico do Título</div>
    <div class="divisoria"></div>

    <br>

    <script type="text/javascript" language="JavaScript" src="js/format.js"></script>
    <script type="text/javascript" language="JavaScript">

        function validarForm(){

            if(!isDataValida(document.forms[0].dataAdmissao.value)){
                return;
            }

            document.forms[0].submit();
        }

    </script>

<form action="c" method="POST">
    <input type="hidden" name="app" value="1540">
    <input type="hidden" name="acao" value="atualizarDataAdmissao">
    <input type="hidden" name="matricula" value="${titular.socio.matricula}">
    <input type="hidden" name="idCategoria" value="${titular.socio.idCategoria}">

    <table class="fmt">
        <tr>
            <td>
              <p class="legendaCodigo MargemSuperior0" >Titular</p>
              <input type="text" name="email" class="campoSemTamanho alturaPadrao larguraNomePessoa"  readonly  value="${titular.socio.nome}"><br>
            </td>
            <td>
              <p class="legendaCodigo MargemSuperior0" >Data de Admissão</p>
              <fmt:formatDate var="dataAdmissao" value="${titular.dataAdmissao}" pattern="dd/MM/yyyy" />
              <input type="text"  class="campoSemTamanho alturaPadrao larguraData" name="dataAdmissao" id="dataAdmissao" value="${dataAdmissao}" /><br>
            </td>
            <td>
                <input class="botaoSalva" style="margin-top:20px" type="button" onclick="validarForm()" value="" title="Atualizar Data de Admissão" />

            </td>            
        </tr>        
    </table>

</form>

    <br>                
    <table class="fmt">
        <tr>
            <td>
                <c:if test='<%=request.isUserInRole("1541")%>'>
                    <div class="botaoincluirgeral">
                        <a href="c?app=1541&acao=showForm&matricula=${titular.socio.matricula}&idCategoria=${titular.socio.idCategoria}"><img src="imagens/btn-incluir.png" width="100" height="25" /></a>
                        &nbsp
                        <a href="c?app=1540&acao=imprimir&matricula=${titular.socio.matricula}&idCategoria=${titular.socio.idCategoria}"><img src="imagens/btn-imprimir.png" width="100" height="25" /></a>
                    </div>
                </c:if>
                
            </td>            
        </tr>        
    </table>
    
    <br>
    <table id="tabela">
        <thead>
            <tr class="odd">
                <th scope="col">Nome</th>
                <th scope="col">Dt. Início</th>
                <th scope="col">Dt. Fim</th>
                <th scope="col">Alterar</th>
                <th scope="col">Excluir</th>
            </tr>
        </thead>
            
        <c:forEach var="historico" items="${historicos}">
            <tr>
                <td class="column1">${historico.nome}</td>
                <fmt:formatDate var="dataInicio" value="${historico.dataInicio}" pattern="dd/MM/yyyy" />
                <td class="column1" align="center">${dataInicio}</td>
                <fmt:formatDate var="dataFim" value="${historico.dataFim}" pattern="dd/MM/yyyy" />
                <td class="column1" align="center" >${dataFim}</td>
                <td align="center">    
                <c:if test='<%=request.isUserInRole("1542")%>'>
                    <a href="c?app=1542&acao=showForm&seqUtilizacao=${historico.seqUtilizacao}"><img src="imagens/icones/inclusao-titular-03.png"/></a>
                </c:if>
                </td>
                <td align="center">
                <c:if test='<%=request.isUserInRole("1543")%>'>
                    <a href="javascript: if(confirm('Confirma a exclusão do histórico?')) window.location.href='c?app=1543&seqUtilizacao=${historico.seqUtilizacao}&acao=excluir&matricula=${titular.socio.matricula}&idCategoria=${titular.socio.idCategoria}'"><img src="imagens/icones/inclusao-titular-05.png" /></a>
                </c:if>
                </td>

            </tr>
        </c:forEach>
    </table>
    
    <input type="button" class="botaoVoltar"  onclick="window.location='c?app=9030&acao=consultar&matricula=${titular.socio.matricula}&categoria=${titular.socio.idCategoria}';" value=" " />
    
</body>
</html>
