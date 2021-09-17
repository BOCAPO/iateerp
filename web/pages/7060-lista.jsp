
<%@include file="head.jsp"%>

<style type="text/css">
    table.fmt {border:none;width:0;padding:0px;margin-left:0em;margin-top:0em;}
    table.fmt tr td {border:none;background:none;padding:0px;margin:0em auto;}
</style>  

<script type="text/javascript" language="JavaScript">
    $(document).ready(function () {
            $('#tabela tr:gt(0)').css('background', 'white');

            $('#tabela tr:gt(0)').hover(function() {
                    $(this).css('background','#f4f9fe');
            }, function() {
                    $(this).css('background','white');
            })
    });        
</script>  
<script type="text/javascript" language="JavaScript">
        function validarForm(){
            var numero = trim(document.forms[0].numero.value);
            var nome = trim(document.forms[0].nome.value);

            if(numero == '' && nome == ''){
                alert('É preciso informar ao menos 1 parâmetro de consulta');
                return false;
            } else if(nome != '' && nome.length < 3){
                alert('Nome para pesquisa deve ter no mínimo 3 letras.');
                return false;
            } else {
                document.forms[0].submit();
            }
        }
</script>  


<body class="internas">

    <script type="text/javascript" language="JavaScript" src="js/format.js"></script>
    
    <%@include file="menuAcesso.jsp"%>
        
    <div class="divisoria"></div>
    <div id="titulo-subnav">Consulta de Presença no Clube</div>
    <div class="divisoria"></div>
    
    
    <form action="c" method="POST" onsubmit="return validarForm()">
        <input type="hidden" name="app" value="7060">
        <input type="hidden" name="acao" value="consultar">

        <table class="fmt" align="left" >
            <tr>
                <td>
                    <p class="legendaCodigo MargemSuperior0" >Documento</p>
                    <input type="text" name="numero" class="campoSemTamanho alturaPadrao larguraData" value="${numero}">
                </td>
                <td>
                    <p class="legendaCodigo MargemSuperior0" >Nome</p>
                    <input type="text" name="nome" class="campoSemTamanho alturaPadrao" style="width:300px" value="${nome}">
                </td>
                <td >    
                    <input class="botaobuscainclusao" style="margin-top:20px" type="submit" onclick="return validarForm()" value="" title="Consultar" />
                </td>
            </tr>
        </table>
        <br><br>
        
        <table id="tabela" style="width:98%;margin-left:15px;">
            <thead>
                <tr class="odd">
                    <th scope="col">Alerta</th>
                    <th scope="col" class="nome-lista">Nome</th>
                    <th scope="col">Matricula</th>
                    <th scope="col">Data de Nascimento</th>
                    <th scope="col">Tipo de Cadastro</th>
                    <th scope="col">Categoria</th>
                    <th scope="col">Tipo de Categoria</th>
                </tr>	
            </thead>
            <tbody>
                <c:forEach var="socio" items="${socios}">
                    <tr onclick="this.getElementsByTagName('input')[0].click();">
                        <td class="column1" align="center">
                            <c:if test="${socio.alerta == 0}">
                                <img src="img/blank.gif" title="" />
                            </c:if>
                            <c:if test="${socio.alerta == 1}">
                                <img src="img/socio-alerta1.png" title="Situação Especial" />
                            </c:if>
                            <c:if test="${socio.alerta == 2}">
                                <img src="img/socio-alerta2.png" title="Excluído(a)" />
                            </c:if>
                            <c:if test="${socio.alerta == 3}">
                                <img src="img/socio-alerta3.png" title="Dependente c/ Idade Extrapolada" />
                            </c:if>
                            <c:if test="${socio.alerta == 4}">
                                <img src="img/socio-alerta4.png" title="Embarcação" />
                            </c:if>
                        </td>

                        <td class="column1">
                            <a href="c?app=7060&acao=detalhe&doc=${socio.nrCarteira}">
                                ${socio.nome}
                            </a>
                        </td>

                        <td class="column1">${socio.matricula}</td>

                        <fmt:formatDate var="nascimento" value="${socio.dataNascimento}" pattern="dd/MM/yyyy" />
                        <td class="column1">${nascimento}</td>

                        <c:choose>
                            <c:when test='${socio.seqDependente == 0}'>
                                <td class="column1">TITULAR</td>
                            </c:when>
                            <c:otherwise>
                                <td class="column1">DEPENDENTE</td>
                            </c:otherwise>
                        </c:choose>

                        <td class="column1">${socio.categoria}</td>

                        <c:choose>
                            <c:when test='${socio.tipoCategoria == "SO"}'>
                                <td class="column1">SÓCIO</td>
                            </c:when>
                            <c:otherwise>
                                <td class="column1">NÃO SÓCIO</td>
                            </c:otherwise>
                        </c:choose>
                    </tr>
                </c:forEach>
            </tbody>
        </table>
        
    </form>
        

    
    
</body>
</html>
