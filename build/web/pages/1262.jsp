<%@include file="head.jsp"%>

<style type="text/css">
    table.fmt {border:none;width:0;padding:0px;margin-left:0em;margin-top:0em;}
    table.fmt tr td {border:none;background:none;padding:0px;margin:0em auto;}

</style>  

<body class="internas">
    <%@include file="menu.jsp"%>        
    
    <script type="text/javascript" language="JavaScript" src="js/format.js"></script>
    <script type="text/javascript" src="js/jquery.form.js"></script>
    
    <script type="text/javascript" language="JavaScript">
        $(document).ready(function () {
                $('#tabela tr:gt(0)').css('background', 'white');
                
                $('#tabela tr:gt(0)').hover(function() {
                        $(this).css('background','#f4f9fe');
                }, function() {
                        $(this).css('background','white');
                })
        });
        
        function submete (e) {
            $('#UploadForm').ajaxSubmit({
                success:  afterSuccess //call function after success
            });
        }
        function afterSuccess(retorno)  {
            $('#UploadForm').resetForm();  // reset form
            $('#fotoVisitante').attr('src', 'img/' + retorno);
            $('#arquivoFotoVisitante').attr('value', retorno);
        }

        function validarForm(){

            if(trim(document.forms['principal'].nome.value) == ''){
                alert('Informe o Nome do Visitante');
                return;
            }

            document.forms['principal'].submit();    
        }

    </script>
    
    <div class="divisoria"></div>
    <c:choose>
        <c:when test="${cracha.emprestado()}">
            <div id="titulo-subnav">Devolução de Crachá de Visitante</div>
        </c:when>
        <c:otherwise>
            <div id="titulo-subnav">Entrega de Crachá de Visitante</div>
        </c:otherwise>
    </c:choose>
    <div class="divisoria"></div>

    
    <table class="fmt">
        <tr>
            <td>
                <form name="principal" action="c" method="POST">
                    <c:choose>
                        <c:when test="${cracha.emprestado()}">
                            <input type="hidden" name="acao" value="devolver">
                        </c:when>
                        <c:otherwise>
                            <input type="hidden" name="acao" value="entregar">
                        </c:otherwise>
                    </c:choose>
                    <input type="hidden" name="app" value="1266">
                    <input type="hidden" name="idVisitante" value="${visitante.id}">
                    <input type="hidden" name="idCracha" value="${cracha.id}">
                    <input type="hidden" id="arquivoFotoVisitante" name="arquivoFotoVisitante">
                    <table class="fmt">
                        <tr>
                            <td>
                                <table class="fmt">
                                    <tr>
                                        <td>
                                            <td colspan="3">
                                                <p class="legendaCodigo MargemSuperior0" >Nome:</p>
                                                <input type="text" name="nome" value="${visitante.nome}" <c:if test="${cracha.emprestado()}">readonly</c:if> class="campoSemTamanho alturaPadrao" style="width:400px">
                                            </td>
                                        </td>
                                        <td>
                                            <td colspan="3">
                                                <c:if test="${not cracha.emprestado()}">
                                                    <input type="button" class="botaobuscainclusao" style="margin-top:20px" onclick="window.location='c?app=1265&acao=showFormSelecionarVisitante&idCracha=${cracha.id}';" value="" title="Selecionar Pessoa" />                                    
                                                </c:if>        
                                            </td>
                                        </td>
                                    </tr>
                                </table>
                            </td>
                        </tr>
                        <tr>
                            <td>
                                <table class="fmt">
                                    <tr>
                                        <td>
                                            <p class="legendaCodigo MargemSuperior0" >Documento:</p>
                                            <input type="text" name="documento" value="${visitante.documento}" <c:if test="${cracha.emprestado()}">readonly</c:if> class="campoSemTamanho alturaPadrao larguraData">
                                        </td>
                                        <td>
                                            <p class="legendaCodigo MargemSuperior0" >Placa:</p>
                                            <input type="text" name="placa" value="${visitante.placa}" <c:if test="${cracha.emprestado()}">readonly</c:if> class="campoSemTamanho alturaPadrao larguraData">
                                        </td>
                                        <td>
                                            <p class="legendaCodigo MargemSuperior0">Setor:</p>
                                            <select name="idSetor" class="campoSemTamanho alturaPadrao" style="width:200px" <c:if test="${cracha.emprestado()}">disabled</c:if>>
                                                <option value="0">NENHUM</option>
                                                <c:forEach var="setor" items="${setores}">
                                                    <option value="${setor.id}" <c:if test="${setorVisitado.id == setor.id}">selected</c:if> >${setor.descricao}</option>
                                                </c:forEach>
                                            </select>
                                        </td>
                                    </tr>
                                </table>
                            </td>
                        </tr>
                    </table>
                </form>
            </td>
            <td>
                <form action="upload" method="post" enctype="multipart/form-data" id="UploadForm">
                    <input type="hidden" name="app" value="1265">
                    <input type="hidden" name="acao" value="capturarFoto">    
                    <c:choose>
                        <c:when test="${visitante.id gt 0}">
                            <img id="fotoVisitante" src="f?tb=7&cd=${visitante.id}" class="recuoPadrao MargemSuperiorPadrao" width="120" height="160">
                        </c:when>
                        <c:otherwise>
                            <img id="fotoVisitante" src="" class="recuoPadrao MargemSuperiorPadrao" width="120" height="160">
                        </c:otherwise>
                    </c:choose>        
                    <c:if test="${not cracha.emprestado()}">
                        <br>&nbsp;&nbsp;&nbsp;<input name="arquivo" type="file" onchange="submete()"/>
                    </c:if>
                </form>
            </td>
        </tr>
        <tr colspan="2">
            <td colspan="2">
                <c:if test='<%=request.isUserInRole("1266")%>'>
                    <input type="button" onclick="validarForm()" class="botaoatualizar" value=" " />
                </c:if>
                <input type="button" class="botaoimprimirnovo" onclick="window.location='c?app=1265&acao=visualizarRelatorio&cracha=${cracha.id}';" value=" " />
            </td>
        </tr>
    </table>
        
    <table id="tabela" style="width:98%;margin-left:15px;">
        <thead>
            <tr class="odd">
                <th scope="col" class="nome-lista">Visitante</th>
                <th scope="col">Documento</th>
                <th scope="col">Entrada</th>
                <th scope="col">Func. Entrada</th>
                <th scope="col">Saída</th>
                <th scope="col">Func. Saída</th>
            </tr>
        </thead>
        <tbody>
        <c:forEach var="registro" items="${registros}">
            <tr>
                <td class="column1">${registro.visitante.nome}</td>
                <td class="column1" align="center">${registro.visitante.documento}</td>
                <fmt:formatDate var="entrada" pattern="dd/MM/yyyy HH:mm:ss" value="${registro.entrada}"/>
                <td class="column1"  align="center">${entrada}</td>
                <td class="column1"  align="center">${registro.usuarioEntrega}</td>
                <fmt:formatDate var="saida" pattern="dd/MM/yyyy HH:mm:ss" value="${registro.saida}"/>
                <td class="column1"  align="center">${saida}</td>
                <td class="column1"  align="center">${registro.usuarioDevolucao}</td>
            </tr>
        </c:forEach>
        </tbody>
    </table>
        
</body>
</html>
