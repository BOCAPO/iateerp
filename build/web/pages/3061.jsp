<%@page import="techsoft.curso.TurmaHorario"%>
<%@page import="techsoft.curso.Turma"%>
<%@taglib prefix="sql" uri="http://java.sun.com/jsp/jstl/sql"%>
<%@include file="head.jsp"%>

<style type="text/css">
    table.fmt {border:none;width:0;padding:0px;margin-left:0em;margin-top:0em;}
    table.fmt tr td {border:none;background:none;padding:0px;margin:0em auto;}
</style>  

<body class="internas">

    <%@include file="menu.jsp"%>
    <script type="text/javascript" language="JavaScript" src="js/format.js"></script>
    
    <div class="divisoria"></div>
    <div id="titulo-subnav">Confirmação de Matrícula - Pessoa</div>
    <div class="divisoria"></div>

    <script type="text/javascript" language="JavaScript">
        $(document).ready(function () {
            $("#dtValidadeAtestado").mask("99/99/9999");
        });     
        
        function validarForm(){
            if(!trim(document.forms[0].descontoPessoal.value) == ''){
                if(parseInt(document.forms[0].descontoPessoal.value) < 0
                || parseInt(document.forms[0].descontoPessoal.value) > 100){
                    alert('O desconto pessoal deve ser entre 0 e 100 %.');
                    return;                
                }
            }else{
                alert('Infome o valor do desconto pessoal');
                return;
            }
            document.forms[0].submit();
        }
        
        function mostraDocumento(matricula, categoria, seqDependente){
            window.open('c?app=3040&acao=mostraFoto&matricula='+matricula+'&categoria='+categoria+'&seqDependente='+seqDependente,'page','toolbar=no,location=no,status=no,menubar=no,scrollbars=no,resizable=no,width=600,height=600'); 
        }

        function excluiFoto(){
            $.ajax({url:'AtestadoAjax', type:'GET',async:false,
                                data:{
                                tipo:1,
                                categoria:$('#idCategoria').val(),
                                matricula:$('#matricula').val(),
                                dependente:$('#seqDependente').val()
                                }
            }).success(function(retorno){
                $('#imgAtestado').removeAttr('src');
                $('#imgAtestado').show();
                alert('Imagem do atestado excluída com sucesso!');
            });
        }

    </script>        

    <form method="POST" action="upload" enctype="multipart/form-data">
        <input type="hidden" name="app" value="3040">
        <input type="hidden" name="idTurma" value="${turma.id}">
        <input type="hidden" id="matricula" name="matricula" value="${socio.matricula}">
        <input type="hidden" id="idCategoria" name="idCategoria" value="${socio.idCategoria}">
        <input type="hidden" id="seqDependente" name="seqDependente" value="${socio.seqDependente}">
        <input type="hidden" name="acao" value="gravar">
        <input type="hidden" name="fazMatricula" value="SIM">

        <table class="fmt" enctype="multipart/form-data">
            <tr>
                <td>
                    <p class="legendaCodigo MargemSuperior0" >Pessoa:</p>
                    <input type="text" size="50" class="campoSemTamanho alturaPadrao" disabled value="${socio.nome}"><br>
                </td>
                <td>
                    <p class="legendaCodigo MargemSuperior0" >% Desc. Familiar:</p>
                    <c:set var="descontoFamiliar" value="0"/>
                    <c:if test="${turma.idCurso eq 46}">
                        <sql:query var="rs" dataSource="jdbc/iate">
                            EXEC SP_RECUPERA_PARAMETROS_SISTEMA
                        </sql:query>    
                        <c:set var="percentualPessoa" value="${rs.rows[0].PERC_PROGRESSAO_DESCONTO_FAMIL}"/>
                        <c:set var="percentualTeto" value="${rs.rows[0].PERC_TETO_DESCONTO_FAMILIA}"/>
                        <c:set var="descontoInicial" value="${rs.rows[0].DESC_INICIAL_FAMILIA}"/>

                        <sql:query var="rs2" dataSource="jdbc/iate">
                            EXEC SP_RECUPERA_MATRICULAS_FAMILIA ?, ?, ?
                            <sql:param value="${socio.matricula}"/>
                            <sql:param value="${socio.idCategoria}"/>
                            <sql:dateParam value="${turma.dataInicio}" type="DATE"/>
                        </sql:query>

                        <c:choose>
                            <c:when test="${rs2.rowCount eq 0}">
                                <c:set var="descontoFamiliar" value="0"/>
                            </c:when>
                            <c:when test="${rs2.rowCount eq 1}">
                                <c:set var="descontoFamiliar" value="${descontoInicial}"/>
                            </c:when>
                            <c:otherwise>
                                <c:choose>
                                    <c:when test="${(percentualPessoa * (rs2.rowCount - 1)) + descontoInicial gt percentualTeto}">
                                        <c:set var="descontoFamiliar" value="${descontoInicial}"/>
                                    </c:when>
                                    <c:otherwise>
                                        <c:set var="descontoFamiliar" value="${(percentualPessoa * (rs2.rowCount - 1)) + descontoInicial}"/>
                                    </c:otherwise>
                                </c:choose>
                            </c:otherwise>
                        </c:choose>
                    </c:if>
                    <c:if test="${turma.idCurso ne 46}">
                        <sql:query var="rs3" dataSource="jdbc/iate">
                            EXEC SP_CALCULA_DESC_FAMILIAR_TIPO_S ?, ?, ?, ?
                            <sql:param value="${socio.matricula}"/>
                            <sql:param value="${socio.idCategoria}"/>
                            <sql:param value="${socio.seqDependente}"/>
                            <sql:param value="${turma.id}"/>
                        </sql:query>
                        <c:set var="descontoFamiliar" value="${rs3.rows[0].PERC_DESCONTO}"/>
                    </c:if>
                    <fmt:formatNumber var="descontoFamiliarFormatado" value="${descontoFamiliar}" pattern="#0.00"/>
                    
                    <input type="text" name="descontoFamiliarTela" class="campoSemTamanho alturaPadrao larguraData" disabled value="${descontoFamiliarFormatado}">
                    <input type="hidden" name="descontoFamiliar" value="${descontoFamiliarFormatado}">
                 </td>   
                 <td>
                    <p class="legendaCodigo MargemSuperior0" >$ Desc. Pessoal:</p>        
                    <input type="text" name="descontoPessoal" class="campoSemTamanho alturaPadrao larguraData" onkeypress="onlyPositiveFloat(event)" value="0,00"><br>
                 </td>    
            </tr>
        </table>
                    

        <div class="divisoria"></div>
        <div id="titulo-subnav">Turma</div>
        <div class="divisoria"></div>
            
        <table class="fmt">
            <tr>
                <td>
                    <p class="legendaCodigo MargemSuperior0">Curso:</p>
                    <c:forEach var="curso" items="${cursos}">
                        <c:if test="${turma.idCurso eq curso.id}"><input type="text" size="50" class="campoSemTamanho alturaPadrao" disabled  size="50" readonly value="${curso.descricao}"/></c:if>
                    </c:forEach>
                </td>
                <td colspan="2">
                    <p class="legendaCodigo MargemSuperior0">Turma:</p>
                    <input type="text" size="50" class="campoSemTamanho alturaPadrao" disabled  size="50" readonly value="${turma.deTurma}"/>
                </td>
            </tr>
            <tr>
                <td>
                    <p class="legendaCodigo MargemSuperior0">Professor:</p>
                    <c:forEach var="professor" items="${professores}">
                        <c:if test="${turma.idProfessor eq professor.id}"><input  size="50" class="campoSemTamanho alturaPadrao" disabled  type="text" size="50" readonly value="${professor.descricao}"/></c:if>
                    </c:forEach>
                </td>
                <td>
                    <p class="legendaCodigo MargemSuperior0">Periodo:</p>
                    <fmt:formatDate var="dataInicio" value="${turma.dataInicio}" pattern="dd/MM/yyyy"/>
                    <fmt:formatDate var="dataFim" value="${turma.dataFim}" pattern="dd/MM/yyyy"/>
                    <input type="text" size="30" class="campoSemTamanho alturaPadrao" style="width:185px" disabled  value="${dataInicio} até ${dataFim}"/>
                </td>
                
                <td>
                    <p class="legendaCodigo MargemSuperior0">Dias de Aula:</p>
                    <%
                    String diasAula = "";
                    Turma t = (Turma)request.getAttribute("turma");
                    java.util.List<TurmaHorario>[] horarios = t.getHorarios();
                    if(horarios[0].size() > 0) diasAula += "2/";
                    if(horarios[1].size() > 0) diasAula += "3/";
                    if(horarios[2].size() > 0) diasAula += "4/";
                    if(horarios[3].size() > 0) diasAula += "5/";
                    if(horarios[4].size() > 0) diasAula += "6/";
                    if(horarios[5].size() > 0) diasAula += "S/";
                    if(horarios[6].size() > 0) diasAula += "D/";

                    if(diasAula.length() > 0){
                        diasAula = diasAula.substring(0, diasAula.length() -1);
                        pageContext.setAttribute("diasAula", String.valueOf(diasAula));
                    }
                    %>
                    <input type="text" size="50" class="campoSemTamanho alturaPadrao larguraData" disabled  value="${diasAula}"/>
                </td>
            </tr>        
        </table>

        <div class="divisoria"></div>
        <div id="titulo-subnav">Atestado Médico</div>
        <div class="divisoria"></div>
               
        <table class="fmt">
            <tr>
                <td>
                    <p class="legendaCodigo MargemSuperior0">Dt. Vencimento</p>
                    <fmt:formatDate var="dtValidadeAtestado" value="${socio.dtValidadeAtestado}" pattern="dd/MM/yyyy"/>
                    <input type="text" id="dtValidadeAtestado" name="dtValidadeAtestado" class="campoSemTamanho alturaPadrao" style="width: 100px" value="${dtValidadeAtestado}">
                </td>    
                <td rowspan="4">
                    <a href="javascript: mostraDocumento(${socio.matricula}, ${socio.idCategoria}, ${socio.seqDependente});">
                        <img id="imgAtestado" src="f?tb=10&mat=${socio.matricula}&cat=${socio.idCategoria}&seq=${socio.seqDependente}" class="recuoPadrao MargemSuperiorPadrao" width="120" height="160">
                    </a>
                </td>    
            </tr>    
       </table>
        
        <br>
        <table class="fmt">
           <tr>
             <td colspan="3">
                 <input type="file" class="botaoEscolherArquivo" name="arquivo"/>
             </td>
           </tr>
           <tr>
             <td>
                 <input type="submit" id="atualizar" class="botaoatualizar" value="">
             </td>
             <td>
                 <a href="javascript: if(confirm('confirma exclusao?')) excluiFoto()"><img src="imagens/btn-excluir-foto.png" class="botaoExcluirFoto" /></a><BR>
             </td>
             <td colspan="3">
                <input type="button" class="botaoVoltar"  onclick="window.location='c?app=3040&mostrarSomenteTurmasAtivas=true';" value=" " />
             </td>
           </tr>
        </table>  
    </form>
        
</body>
</html>
