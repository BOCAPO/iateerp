<%@page contentType="text/html" pageEncoding="ISO-8859-1"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>

<!DOCTYPE html>
<html>
    <head>
        <script type="text/javascript">
            window.onload=
                function(){
                    window.print();
                    if (document.forms[0].retorno.value==""){
                        history.go(-1);                        
                    }else{
                        window.location = "c?app="+document.forms[0].retorno.value;
                    }
                }
        </script>   
        
        <style type="text/css">
        .texto{
                font-family: "Courier New";
                font-size: 10px;
        }
        </style>

        <meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">

        <title>Iate Clube de Brasília</title>
    </head>
    <body>
        <form>
            <input type="hidden" name="retorno" id="retorno" value="${retorno}">
            
            <div class="texto">
                <c:forEach var="linha" items="${linhas}">
                    ${fn:replace(linha, ' ', '&nbsp;')}<br>
                </c:forEach>
            </div>
        </form>
            
    </body>
</html>
