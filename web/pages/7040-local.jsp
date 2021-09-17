
<%@include file="head.jsp"%>

<style type="text/css">
    table.fmt {border:none;width:0;padding:0px;margin-left:0em;margin-top:0em;}
    table.fmt tr td {border:none;background:none;padding:0px;margin:0em;}
</style>  
<style type="text/css">
    #local {
        margin-top: -110px;
        margin-left: -110px;
        left: 50%;
        top: 50%;
        position: fixed;
    }
</style>


<script type="text/javascript" language="JavaScript">
    function validarForm(){
        if($('#idLocal').val()==null){
            alert('Nenhum local foi selecionado!');
            return false
        }

        document.forms[0].submit();
    }
</script>  


<body class="internas">

    <script type="text/javascript" language="JavaScript" src="js/format.js"></script>
    
    <%@include file="menuAcesso.jsp"%>
        
    
    <form action="c" method="POST" onsubmit="return validarForm()">
        <input type="hidden" name="app" value="7040">
        <input type="hidden" name="acao" value="showForm">
        
        <div id="local" >
            <table>
                <tr>
                  <td>
                    <div class="divisoria"></div>
                    <div id="titulo-subnav">Local de Acesso</div>
                    <div class="divisoria"></div>
                    <table class="fmt" align="left">
                      <tr>
                        <td>
                            <br>
                            <div class="selectheightnovo">
                                <select name="idLocal" id="idLocal"  class="campoSemTamanho">
                                    <c:forEach var="local" items="${locais}">
                                        <option value="${local.id}">${local.descricao}</option>
                                    </c:forEach>
                                </select>
                            </div>        
                        </td>
                      </tr>
                      <tr>
                        <td align="center">
                            <br>
                            <input type="button" id="selecionar" name="selecionar" onclick="validarForm()"  class="botaoatualizar" value=" " />
                        </td>
                      </tr>
                    </table>  
                </td>
              </tr>
            <table>
        </div>        
        
    </form>
    
</body>
</html>
