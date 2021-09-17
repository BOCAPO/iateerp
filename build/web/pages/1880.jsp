<%@taglib prefix="sql" uri="http://java.sun.com/jsp/jstl/sql"%>
<%@include file="head.jsp"%>

<script type="text/javascript">
    $(document).ready(function() {
        $('#tabela tr:gt(0)').css('background', 'white');

        $('#tabela tr:gt(0)').hover(function() {
            $(this).css('background', '#f4f9fe');
        }, function() {
            $(this).css('background', 'white');
        })

    });

    function unselect() {
        if (document.forms[0].autorizar.checked) {
            for (i = 0; i < document.forms[0].idAplicacao.length; i++) {
                document.forms[0].idAplicacao[i].disabled = false;
            }
            gerarSenha();
        } else {
            for (i = 0; i < document.forms[0].idAplicacao.length; i++) {
                document.forms[0].idAplicacao[i].checked = false;
                document.forms[0].idAplicacao[i].disabled = true;
            }
        }
    }

    function gerarSenha() {
        var i = 0;
        var novaSenha = "";
        var num = 0;

        for (i = 0; i < 6; i++) {
            num = Math.round((42.0 * Math.random()) + 49);
            if (num > 57 && num < 65) {
                i = i - 1;
            } else {
                novaSenha = novaSenha + String.fromCharCode(num);
            }
        }
        //
        document.forms[0].senha.value = novaSenha;
        document.forms[0].senhaGerada.value = novaSenha;
        document.forms[0].autorizar.checked = true;

    }

    function imprimirSenha() {
        window.open('c?app=1880&acao=imprimir&usuario=' + document.forms[0].usuario.value + '&senha=' + document.forms[0].senhaGerada.value, 'page', 'toolbar=no,location=no,status=no,menubar=no,scrollbars=no,resizable=no,width=600,height=600');
    }

</script>

<body class="internas">

    <style type="text/css">
        table.fmt {border:none;width:0;padding:0px;margin-left:0em;margin-top:0em;}
        table.fmt tr td {border:none;background:none;padding:0px;margin:0em auto;}
    </style>    

    <%@include file="menu.jsp"%>


    <form method="POST" action="c">
        <input type="hidden" name="app" value="1880">
        <input type="hidden" name="acao" value="atualizar">
        <input type="hidden" name="matricula" value="${autorizacao.socio.matricula}">
        <input type="hidden" name="idCategoria" value="${autorizacao.socio.idCategoria}">
        <input type="hidden" name="seqDependente" value="${autorizacao.socio.seqDependente}">



        <div class="divisoria"></div>
        <div id="titulo-subnav">Autorização para acesso aos serviços online</div>
        <div class="divisoria"></div>

        <table class="fmt">
            <tr>
                <td>
                    <fmt:formatNumber pattern="00" var="usr1" value="${autorizacao.socio.idCategoria}"/>
                    <fmt:formatNumber pattern="0000" var="usr2" value="${autorizacao.socio.matricula}"/>
                    <fmt:formatNumber pattern="00" var="usr3" value="${autorizacao.socio.seqDependente}"/>

                    <p class="legendaCodigo">Usuario:</p>
                    <input type="text" name="usuario" readonly maxlength="50" class="campoSemTamanho alturaPadrao" value="${usr1}${usr2}${usr3}" />

                </td>
                <td>
                    <p class="legendaCodigo">Senha:</p>
                    <input type="password" name="senha" maxlength="50" class="campoSemTamanho alturaPadrao" value="${autorizacao.senha}" />
                </td>    
            </tr>
        </table>    


        <div class="divisoria"></div>
        <div id="titulo-subnav">Nova Senha</div>
        <div class="divisoria"></div>

        <br>
        <table class="fmt">
            <tr>
                <td>
                    <input type="text" name="senhaGerada" readonly maxlength="50" class="campoSemTamanho alturaPadrao" value="" />
                </td>
                <td>
                    <input type="button" class="botaoGerar3" onclick="gerarSenha()" value=" " />
                </td>    
                <td>
                    <input type="button" class="botaoImprimirBemPequeno" onclick="imprimirSenha()" value=" " />
                </td>    
            </tr>
        </table>    


        <div class="divisoria"></div>
        <div id="titulo-subnav">Autorizações</div>
        <div class="divisoria"></div>

        <br>
        &nbsp;&nbsp;&nbsp;<input type="checkbox" width="100px" value="true" name="autorizar" onchange="unselect()" <c:if test="${autorizacao.autorizado}">checked</c:if>><spam class="legendaSemMargem larguraData"> Autorizar Acesso</spam>
            <br>
            <p class="legendaCodigo">Aplicações:</p>
            <div class="recuoPadrao" style="overflow:auto;height:200px;width:330px;">
            <c:forEach var="aplicacao" items="${aplicacoes}">
                <input type="checkbox" name="idAplicacao" value="${aplicacao.id}" <c:if test="${autorizacao.aplicacoesInternet[aplicacao.id] != null}">checked</c:if> <c:if test="${!autorizacao.autorizado}">disabled</c:if>>${aplicacao.descricao}<br>
            </c:forEach>
        </div>

        <input type="submit" class="botaoatualizar" value=" " />
        <input type="button" class="botaoVoltar"  onclick="window.location = 'c?app=9030&acao=consultar&matricula=${autorizacao.socio.matricula}&categoria=${autorizacao.socio.idCategoria}';" value=" " />

        <div class="divisoria"></div>
        <div id="titulo-subnav">Log</div>
        <div class="divisoria"></div>

        <br>

        <sql:query var="rs" dataSource="jdbc/iate">
            SELECT
            T1.DT_HORA_TRANSACAO,
            T2.NOME_PESSOA,
            T3.DESCR_APLICACAO,
            T1.DE_ATUALIZACAO,
            T1.ED_ESTACAO
            FROM
            iate_hist..TB_LOG_INTERNET  T1,
            TB_PESSOA T2,
            TB_APLICACAO_INTERNET T3
            WHERE
            T1.CD_MATRICULA = T2.CD_MATRICULA AND
            T1.CD_CATEGORIA = T2.CD_CATEGORIA AND
            T1.SEQ_DEPENDENTE = T2.SEQ_DEPENDENTE AND
            T1.CD_APLICACAO = T3.CD_APLICACAO AND
            T1.CD_MATRICULA = ${autorizacao.socio.matricula} AND
            T1.CD_CATEGORIA = ${autorizacao.socio.idCategoria}
            ORDER BY
            1 DESC
        </sql:query>    

        <table id="tabela" style="width:98%;margin-left:15px;">
            <thead>
                <tr class="odd">
                    <th scope="col" class="nome-lista">Data e Hora</th>
                    <th scope="col" >Pessoa</th>
                    <th scope="col" >Aplicacao</th>
                    <th scope="col" >Descrição</th>
                    <th scope="col" >Estação</th>
                </tr>	
            </thead>
            <tbody>
                <c:forEach var="row" items="${rs.rows}">

                    <tr>
                        <fmt:formatDate var="dtLog" value="${row.DT_HORA_TRANSACAO}" pattern="dd/MM/yyyy HH:mm:ss:SSS" />
                        <td align="center">${dtLog}</td>

                        <td align="left">${row.NOME_PESSOA}</td>
                        <td align="left">${row.DESCR_APLICACAO}</td>
                        <td align="left">${row.DE_ATUALIZACAO}</td>
                        <td align="left">${row.ED_ESTACAO}</td>
                    </tr>	
                </c:forEach>
            </tbody>
        </table>        
    </form>

</body>
</html>
