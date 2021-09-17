<%@taglib prefix="sql" uri="http://java.sun.com/jsp/jstl/sql"%>
<%@include file="head.jsp"%>

<style type="text/css">
    table.fmt {border:none;width:0;padding:0px;margin-left:0em;margin-top:0em;}
    table.fmt tr td {border:none;background:none;padding:0px;margin:0em auto;}
</style> 

<script type="text/javascript" language="JavaScript">

    function validarForm(){

        if(document.forms[0].remessa.value == ''){
            alert('Informe o número da remessa!');
            return false;
        }
    
        document.forms[0].submit();
    }

</script>  

<body class="internas">
    <%@include file="menuCaixa.jsp"%>
    
    <div class="divisoria"></div>
    <div id="titulo-subnav">Altera Remessa</div>
    <div class="divisoria"></div>
    <br>
                
    <form action="c">
        <input type="hidden" name="app" value="6200">
        <input type="hidden" name="acao" value="gravar">
        
        <sql:query var="rs" dataSource="jdbc/iate">
            SELECT SEQ_REMESSA_DCO FROM TB_PARAMETRO_SISTEMA
        </sql:query>  
    
        <table class="fmt" align="left" >
            <tr valign="bottom">
              <td>
                  <p class="legendaCodigo MargemSuperior0" >Remessa Atual</p>
                  <c:forEach var="row" items="${rs.rows}">
                    <input type="text" id="remessa" name="remessa" class="campoSemTamanho alturaPadrao larguraData" value="${row.SEQ_REMESSA_DCO}"/>
                  </c:forEach>
              </td>
              <td>
                  <input type="button" class="botaoatualizar"  onclick="validarForm()" value=" " />
              </td>
            </tr>
        </table>        
    </form>
</body>
</html>
