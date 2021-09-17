<%@include file="head.jsp"%>

<script type="text/javascript" language="JavaScript">
    $(document).ready(function () {
            $('#dataInicio').mask('99/99/9999');
            $('#dataFim').mask('99/99/9999');
    });        

    function validarForm(){


        if(document.forms[0].nome.value == ''){
            alert('O campo nome é de preenchimento obrigatório!');
            return;
        }
        
        document.forms[0].submit();
    }

</script>

<body class="internas">
    <style type="text/css">
        table.fmt {border:none;width:0;padding:0px;margin-left:0em;margin-top:0em;}
        table.fmt tr td {border:none;background:none;padding:0px;margin:0em auto;}
    </style>    
            
    <%@include file="menu.jsp"%>

    <div class="divisoria"></div>
    <c:if test='${app == "1541"}'>
        <div id="titulo-subnav">Inclusão de Historico de Utilização de Titulo</div>
    </c:if>
    <c:if test='${app == "1542"}'>
        <div id="titulo-subnav">Alteração de Historico de Utilização de Titulo</div>
    </c:if>
    <div class="divisoria"></div>

    <form action="c" id="it-bloco01">

        <input type="hidden" name="app" value="${app}"/>
        <input type="hidden" name="acao" value="gravar"/>
        <input type="hidden" name="seqUtilizacao" value="${historico.seqUtilizacao}"/>
        <input type="hidden" name="matricula" value="${titular.socio.matricula}"/>
        <input type="hidden" name="idCategoria" value="${titular.socio.idCategoria}"/>
        
        <table class="fmt">
            <tr>
                <td>
                    <p class="legendaCodigo">Nome:</p>
                    <input type="text" name="nome" maxlength="50" class="campoSemTamanho alturaPadrao larguraNomePessoa" value="${historico.nome}" />
                </td>
                <td>
                    <p class="legendaCodigo">Dt. Início:</p>
                    <fmt:formatDate var="dataInicio" value="${historico.dataInicio}" pattern="dd/MM/yyyy" />
                    <input type="text"  class="campoSemTamanho alturaPadrao larguraData" name="dataInicio" id="dataInicio" value="${dataInicio}" /><br>
                </td>
                <td>
                    <p class="legendaCodigo">Dt. Fim:</p>
                    <fmt:formatDate var="dataFim" value="${historico.dataFim}" pattern="dd/MM/yyyy" />
                    <input type="text"  class="campoSemTamanho alturaPadrao larguraData" name="dataFim" id="dataFim" value="${dataFim}" /><br>                            
                </td>            
            </tr>        
        </table>
                
        <br>

        <input type="button" class="botaoatualizar"  onclick="validarForm()" value=" " />
        <input type="button" class="botaoVoltar"  onclick="window.location='c?app=1540&matricula=${titular.socio.matricula}&idCategoria=${titular.socio.idCategoria}';" value=" " />

    </form>
</body>
</html>

