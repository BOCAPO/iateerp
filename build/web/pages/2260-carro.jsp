<%@include file="head.jsp"%>

<script type="text/javascript" language="JavaScript">

    function validarForm(){


        if(document.forms[0].placa.value == ''){
            alert('O campo placa � de preenchimento obrigat�rio!');
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
    <c:if test='${acao == "inserirCarro"}'>
        <div id="titulo-subnav">Inclus�o de Carro</div>
    </c:if>
    <c:if test='${acao == "alterarCarro"}'>
        <div id="titulo-subnav">Altera��o de Carro</div>
    </c:if>
    <div class="divisoria"></div>

    <form action="c" id="it-bloco01">

        <input type="hidden" name="app" value="2260"/>
        <c:if test='${carro == null}'>
            <input type="hidden" name="acao" value="inserirCarro"/>
        </c:if>
        <c:if test='${carro == null}'>
            <input type="hidden" name="acao" value="alterarCarro"/>
        </c:if>            
        <input type="hidden" name="acao" value="${acao}"/>
        <input type="hidden" name="idCarro" value="${carro.id}"/>
        <input type="hidden" name="matricula" value="${titular.matricula}"/>
        <input type="hidden" name="idCategoria" value="${titular.idCategoria}"/>
        <table class="fmt">
            <tr>
                <td>
                    <p class="legendaCodigo">Placa:</p>
                    <input type="text" name="placa" maxlength="8" class="campoSemTamanho alturaPadrao" value="${carro.placa}" />
                </td>
                <td>
        
                    <p class="legendaCodigo">Modelo:</p>
                    <div class="selectheightnovo">
                        <select name="idModelo" class="campoSemTamanho" >
                            <c:forEach var="modelo" items="${modelos}">
                                <option value="${modelo.id}" <c:if test='${carro.modelo.id == modelo.id}'>selected</c:if>>${modelo.descricao}</option>
                            </c:forEach>
                        </select> <br>
                    </div>
                </td>
                <td>

                    <p class="legendaCodigo">Cor:</p>
                    <div class="selectheightnovo">
                        <select name="idCor" class="campoSemTamanho">
                            <c:forEach var="cor" items="${cores}">
                                <option value="${cor.id}" <c:if test='${carro.cor.id == cor.id}'>selected</c:if>>${cor.descricao}</option>
                            </c:forEach>
                        </select> <br>    
                    </div>
                </td>
            </tr>
        </table>    

        <br>

        <input type="button" class="botaoatualizar"  onclick="validarForm()" value=" " />
        <input type="button" class="botaoVoltar"  onclick="window.location='c?app=2260&acao=showForm&matricula=${titular.matricula}&idCategoria=${titular.idCategoria}';" value=" " />    
        
    </form>
</body>
</html>
