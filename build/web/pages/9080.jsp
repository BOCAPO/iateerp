<%@include file="head.jsp"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="sql" uri="http://java.sun.com/jsp/jstl/sql" %>

<script type="text/javascript" LANGUAGE="JavaScript" SRC="js/calendario.js"></script>
<link rel="stylesheet" type="text/css" href="css/calendario.css"/>

<SCRIPT type="text/javascript" LANGUAGE="JavaScript">
    var now = new Date();
    var cal = new CalendarPopup("divCalendario");
    cal.offsetX = -152;
    cal.offsetY = 25;
    cal.setCssPrefix("BBCAL");
</SCRIPT>
<script type="text/javascript" language="JavaScript" src="js/format.js"></script>
<script type="text/javascript" language="JavaScript">
    
    $(document).ready(function () {
        $("#dataVencimento").mask("99/99/9999");
        $('#divImpressora').hide();

    });        
    
    
    function validarForm(){

        if(!isDataValida(document.forms[0].dataVencimento.value)){
            return;
        }
        
        $('#divImpressora').show();
    }
    
    function submete(){
        document.forms[0].submit();
    }
        
    function cancelarImpressora(){
        $('#divImpressora').hide();
    }

</script>

<style>
    table {border:none;width:0;padding:0px;margin-left:0em;margin-top:0em;}
    tr {border:none;background:none;padding:0px;margin:0em auto;}
    td {border:none;background:none;padding:0px;margin:0em auto;}
    
input[type="file"]
{
}    
    
</style>  

<body class="internas">
            
    <%@include file="menu.jsp"%>
    
    <div class="divisoria"></div>
    <div id="titulo-subnav">Emissão de Carteirinha</div>
    <div class="divisoria"></div>


    <form action="c">
        <input type="hidden" name="matricula" value="${socio.matricula}">
        <input type="hidden" name="seqDependente" value="${socio.seqDependente}">
        <input type="hidden" name="idCategoria" value="${socio.idCategoria}">
        <input type="hidden" name="app" value="9080">
        <input type="hidden" name="acao" value="emitirCarteirinha">

        <table align="left" >
            <tr>
                <td rowspan="4" >.
                    <img src="f?tb=6&mat=${socio.matricula}&seq=${socio.seqDependente}&cat=${socio.idCategoria}" class="recuoPadrao MargemSuperiorPadrao" width="120" height="160">
                </td>    
                <td>
                    &nbsp
                </td>    
            </tr>    
            
            <tr>
                <td>
                    <p class="legendaCodigo MargemSuperior0">Nome</p>
                    <input type="text" name="matricula" class="campoSemTamanho alturaPadrao larguraNomePessoa" onkeypress="onlyNumber(event)" disabled value="${socio.nome}">
                </td>
            </tr>
            <tr>
                <td>
                    <p class="legendaCodigo MargemSuperior0">Categoria</p>
                    <c:forEach var="categoria" items="${categorias}">
                        <c:if test='${socio.idCategoria == categoria.id}'>
                            <input type="text" name="matricula" class="campoSemTamanho alturaPadrao larguraNomePessoa" onkeypress="onlyNumber(event)" disabled value="${categoria.descricao}">
                        </c:if>
                    </c:forEach>
                </td>
            </tr>
            <tr>
              <td colspan="2">
                 <table align="left">
                    <tr>
                      <td>
                        <p class="legendaCodigo MargemSuperior0">Tipo de Pessoa</p>
                        <c:choose>
                        <c:when test="${socio.seqDependente == 0}">
                            <input type="text" name="matricula" class="campoSemTamanho alturaPadrao larguraCargoCarteira" onkeypress="onlyNumber(event)" disabled value="Titular">
                        </c:when>
                        <c:otherwise>
                            <input type="text" name="matricula" class="campoSemTamanho alturaPadrao larguraCargoCarteira" onkeypress="onlyNumber(event)" disabled value="Dependente">
                        </c:otherwise>
                        </c:choose>
                      </td>
                      <td>
                        <p class="legendaCodigo MargemSuperior0">Cargo Especial</p>
                        <input type="text" name="matricula" class="campoSemTamanho alturaPadrao larguraCargoCarteira" onkeypress="onlyNumber(event)" disabled value=${cargoEspecial}>
                      </td>
                      <td>
                        <fmt:formatDate var="vencimento" value="${dataVencimento}" pattern="dd/MM/yyyy"/>
                        <p class="legendaCodigo MargemSuperior0">Vencimento</p>
                        <input type="text" name="dataVencimento" id="dataVencimento" maxlength="10" value="${vencimento}" class="campoSemTamanho alturaPadrao larguraData">
                      </td>
                    </tr>
                </table>  
              </td>
            </tr>
       </table>
    
             
        <!--
            ************************************************************************
                                       IMPRESSORA
            ************************************************************************
        -->
        <div id="divImpressora" style="overflow:auto;left: 25%;top: 20%;position: fixed;width: 500px; height:400px;">
            <table style="background:#fff">
                <tr>
                    <td>
                        <div class="divisoria"></div>
                        <div id="titulo-subnav">Impressora</div>
                        <div class="divisoria"></div>

                        <table class="fmt" align="left" >
                            <tr>
                                <td>
                                    <p class="legendaCodigo MargemSuperior0">Impressora</p>
                                    <div class="selectheightnovo">
                                        <sql:query var="rs" dataSource="jdbc/iate">
                                            SELECT
                                                NU_SEQ_IMPRESSORA,
                                                DE_IMPRESSORA,
                                                CD_IP,
                                                CD_PORTA
                                            FROM 
                                                    TB_IMPRESSORA_CARTAO
                                            ORDER BY 
                                                    1
                                        </sql:query>  
                                                    
                                        <select name="impressora" class="campoSemTamanho alturaPadrao" style="width:300px;">
                                        <c:forEach var="row" items="${rs.rows}">
                                            <option value="${row.NU_SEQ_IMPRESSORA}">${row.DE_IMPRESSORA} (${row.CD_IP}:${row.CD_PORTA})</option>
                                        </c:forEach>
                                        </select>
                                    </div>        
                                </td>
                            </tr>
                        </table>
                        <br><br><br><br>
                        <table class="fmt" align="left" >
                            <tr>
                                <td>
                                    <input type="button" id="cmdSubmete" name="cmdSubmete"   class="botaoatualizar" onclick="submete()" />
                                </td>
                                <td>
                                    <input style="margin-left:15px;margin-top: -5px;" type="button" id="cmdCancelarImpressora" name="cmdCancelarImpressora" class="botaocancelar" onclick="cancelarImpressora()" />
                                </td>
                            </tr>
                        </table>

                    </td>
                </tr>
            </table>
        </div>         
    </form>
    <br><br><br><br><br><br><br><br><br><br><br><br>
    

    <form method="POST" action="upload" enctype="multipart/form-data">
        <input type="hidden" name="matricula" value="${socio.matricula}">
        <input type="hidden" name="seqDependente" value="${socio.seqDependente}">
        <input type="hidden" name="idCategoria" value="${socio.idCategoria}">
        <input type="hidden" name="app" value="9080">
        <input type="hidden" name="acao" value="atualizarFoto">    
        
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
                 <input type="button" class="botaoEmitir" onclick="validarForm()" value="">
             </td>
             <td>
                 <a href="javascript: if(confirm('confirma exclusao?')) window.location.href='c?app=9080&matricula=${socio.matricula}&seqDependente=${socio.seqDependente}&idCategoria=${socio.idCategoria}&acao=excluirFoto'"><img src="imagens/btn-excluir-foto.png" class="botaoExcluirFoto" /></a><BR>
             </td>
           </tr>
           <tr>
             <td colspan="3">
                <input type="button" class="botaoVoltar"  onclick="window.location='c?app=9030&acao=consultar&matricula=${socio.matricula}&categoria=${socio.idCategoria}';" value=" " />
             </td>
           </tr>
        </table>  
    </form>

</body>
</html>
