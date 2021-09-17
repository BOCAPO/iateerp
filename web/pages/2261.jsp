<%@include file="head.jsp"%>

    <style>
        table {border:none;width:0;padding:0px;margin-left:0em;margin-top:0em;}
        tr {border:none;background:none;padding:0px;margin:0em auto;}
        td {border:none;background:none;padding:0px;margin:0em auto;}
        .alerta {
                font-family: Georgia, "Times New Roman", Times, serif;
                font-size: 20px;
                font-weight: bold;
                color: #FF0000;
        }
        
    </style>  


    <body class="internas">

    <%@include file="menu.jsp"%>

    <div class="divisoria"></div>
    <div id="titulo-subnav">Pendência de Validação Cadastral</div>
    <div class="divisoria"></div>            

    <script type="text/javascript" language="JavaScript" src="js/format.js"></script>
    <script type="text/javascript" language="JavaScript">

        function validarForm(){


            document.forms[0].submit();
        }

    </script>

<form action="c" method="POST">
    <input type="hidden" name="app" value="2261">
    <input type="hidden" name="acao" value="atualizar">
    <input type="hidden" name="matricula" value="${dependente.matricula}">
    <input type="hidden" name="seqDependente" value="${dependente.seqDependente}">
    <input type="hidden" name="idCategoria" value="${dependente.idCategoria}">

    <c:if test="${dependente.showWarning}">
        <div class="alerta" align="center">
            Atenção: Esta alteração está sendo feita em um cadastro que já estava pendente de validação!
        </div>
    </c:if>
    <table>
        <tr>
            <td>
              <p class="legendaCodigo MargemSuperior0" >Em@il</p>
              <input type="text" name="email" class="campoSemTamanho alturaPadrao larguraCidade"    value="${dependente.email}"><br>
            </td>
            <td>
              <p class="legendaCodigo MargemSuperior0" >Tel. Celular</p>
              <input type="text" name="celular" class="campoSemTamanho alturaPadrao larguraData"    value="${dependente.celular}"><br>
            </td>
            <td>
              <p class="legendaCodigo MargemSuperior0" >Tel. Residencial</p>
              <input type="text" name="telefoneR" class="campoSemTamanho alturaPadrao larguraData"    value="${dependente.telefoneResidencial}"><br>
            </td>
            <td>
              <p class="legendaCodigo MargemSuperior0" >Tel. Comercial</p>
              <input type="text" name="telefoneC" class="campoSemTamanho alturaPadrao larguraData"    value="${dependente.telefoneComercial}"><br>
            </td>
        </tr>                
    </table>                  

    
    <input type="button" class="botaoatualizar"  onclick="validarForm()" value="" />


</form>


    
</body>
</html>
