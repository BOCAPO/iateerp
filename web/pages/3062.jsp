<%@page import="techsoft.curso.TurmaHorario"%>
<%@page import="techsoft.curso.Turma"%>
<%@include file="head.jsp"%>
<%@taglib prefix="sql" uri="http://java.sun.com/jsp/jstl/sql"%>

<script type="text/javascript" LANGUAGE="JavaScript" SRC="js/calendario.js"></script>
<link rel="stylesheet" type="text/css" href="css/calendario.css"/>

<script type="text/javascript" language="JavaScript" src="js/format.js"></script>

<style>
    table {border:none;width:0;padding:0px;margin-left:0em;margin-top:0em;}
    tr {border:none;background:none;padding:0px;margin:0em auto;}
    td {border:none;background:none;padding:0px;margin:0em auto;}
    
input[type="file"]
{
}    
    
</style>  

<script type="text/javascript" language="JavaScript">
    
    function validarForm(){
        document.forms[0].submit();
    }

</script>

<body class="internas">
            
    <%@include file="menu.jsp"%>
    
    <div class="divisoria"></div>
    <div id="titulo-subnav">Imprimir Passaporte</div>
    <div class="divisoria"></div>


    <form action="c">
        <input type="hidden" name="matricula" value="${socio.matricula}">
        <input type="hidden" name="seqDependente" value="${socio.seqDependente}">
        <input type="hidden" name="idCategoria" value="${socio.idCategoria}">
        <input type="hidden" name="idTurma" value="${turma.id}">
        <input type="hidden" name="app" value="3075">
        <input type="hidden" name="acao" value="imprimir">

        <table align="left" >
            <tr>
                <td rowspan="5" >.
                    <img src="f?tb=6&mat=${socio.matricula}&seq=${socio.seqDependente}&cat=${socio.idCategoria}" class="recuoPadrao MargemSuperiorPadrao" width="140" height="190">
                </td>    
                <td>
                    &nbsp
                </td>    
            </tr>    
            
            <tr>
                <td>
                    <p class="legendaCodigo MargemSuperior0">Nome</p>
                    <input type="text" class="campoSemTamanho alturaPadrao larguraNomeDependente" readonly value="${socio.nome}">
                </td>
            </tr>
            <tr>
                <td>
                    <p class="legendaCodigo MargemSuperior0">Área</p>
                    <sql:query var="rs" dataSource="jdbc/iate">
                        select * from TB_CURSO C INNER JOIN TB_MODALIDADE_CURSO M ON M.CD_MODALIDADE = C.CD_MODALIDADE WHERE C.CD_CURSO = ${turma.idCurso}
                    </sql:query>
                    <input type="text" name="modalidade" class="campoSemTamanho alturaPadrao larguraNomeDependente" readonly value="${rs.rows[0].DESCR_MODALIDADE}">
                </td>
            </tr>
            <tr>
                <td>
                    <p class="legendaCodigo MargemSuperior0">Categoria</p>
                    <c:forEach var="curso" items="${cursos}">
                        <c:if test="${turma.idCurso eq curso.id}"><input type="text" class="campoSemTamanho alturaPadrao larguraNomeDependente" readonly value="${curso.descricao}"/></c:if>
                    </c:forEach>                                    
                </td>
            </tr>
            <tr>
                <td>
                    <p class="legendaCodigo MargemSuperior0">Dias</p>
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
                    <input type="text" name="diasAula" class="campoSemTamanho alturaPadrao larguraData" readonly value="${diasAula}">
                </td>            
            </tr>    
       </table>
    
    </form>
    <br><br><br><br><br><br><br><br><br><br><br><br>
    

    <form method="POST" action="upload" enctype="multipart/form-data">
        <input type="hidden" name="matricula" value="${socio.matricula}">
        <input type="hidden" name="seqDependente" value="${socio.seqDependente}">
        <input type="hidden" name="idCategoria" value="${socio.idCategoria}">
        <input type="hidden" name="idTurma" value="${turma.id}">
        <input type="hidden" name="app" value="3075">
        
        <table align="left">
            <tr>
                <td colspan="3">
                    <input type="file" class="botaoEscolherArquivo" name="arquivo"/>
                </td>
            </tr>
            <tr>
                <td>
                   <input type="submit" class="botaoAtualizarFoto" value="">
                </td>
                <td>
                    <input type="button" class="botaoEmitir"  onclick="validarForm()" value="">
                </td>
                <td colspan="3">
                   <input type="button" class="botaoVoltar"  onclick="window.location='c?app=3070&idTurma=${turma.id}';" value=" " />
                </td>
           </tr>
        </table>  
    </form>

    
</body>
</html>
