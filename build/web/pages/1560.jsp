
<%@include file="head.jsp"%>

<style>
    table {border:none;width:0;padding:0px;margin-left:0em;margin-top:0em;}
    tr {border:none;background:none;padding:0px;margin:0em auto;}
    td {border:none;background:none;padding:0px;margin:0em auto;}
</style>  
  

<body class="internas">
            
    <%@include file="menu.jsp"%>
    
    <div class="divisoria"></div>
    <c:if test='${app == "1561"}'>
        <div id="titulo-subnav">Inclusão de Evento</div>
    </c:if>
    <c:if test='${app == "1562"}'>
        <div id="titulo-subnav">Alteração de Evento</div>
    </c:if>
    <div class="divisoria"></div>
    
    <script type="text/javascript" LANGUAGE="JavaScript" SRC="js/calendario.js"></script>
    <link rel="stylesheet" type="text/css" href="css/calendario.css"/>
    <script type="text/javascript" src="js/jscolor/jscolor.js"></script>
    <script type="text/javascript" language="JavaScript" src="js/format.js"></script>
    
    <script type="text/javascript" language="JavaScript">
        $(document).ready(function () {
            $("#data").mask("99/99/9999");
        });    
        function validarForm(){

            if(document.forms[0].descricao.value == ''){
                alert('A descrição deve ser informada!');
                return;
            }
            if(document.forms[0].animacao.value == ''){
                alert('A Animação deve ser informada!');
                return;
            }
            if(document.forms[0].local.value == ''){
                alert('Necessário preencher o Local!');
                return;
            }
            if(document.forms[0].data.value == ''){
                alert('Necessário preencher a Data!');
                return;
            }
            if(document.forms[0].hora.value == ''){
                alert('Necessário preencher a Hora!');
                return;
            }
            if(document.forms[0].qtMesas.value == ''){
                alert('Necessário preencher a Quantidade de Mesas!');
                return;
            }
            if(document.forms[0].qtCadeiras.value == ''){
                alert('Necessário preencher Quantidade de Cadeiras!');
                return;
            }
            if(document.forms[0].qtIngressos.value == ''){
                alert('Necessário preencher Quantidade de Ingressos!');
                return;
            }
            document.forms[0].submit();
        }

    </script>

    <form action="c" method="POST">
        <input type="hidden" name="app" value="${app}">
        <input type="hidden" name="acao" value="gravar">
        <input type="hidden" name="id" value="${evento.id}">

        <table>
            <tr>
                <td>
                    <br>
                    <p class="legendaCodigo MargemSuperior0" >Descrição</p>
                    <input type="text" name="descricao" id="descricao" class="campoSemTamanho alturaPadrao"  style="width:342px;" value="${evento.descricao}">
                </td>
            </tr>
            <tr>
                <td>
                    <br>
                    <p class="legendaCodigo MargemSuperior0" >Animação</p>
                    <input type="text" name="animacao" id="animacao" class="campoSemTamanho alturaPadrao"  style="width:342px;" value="${evento.animacao}">
                </td>
            </tr>
            <tr>
                <td>
                    <br>
                    <p class="legendaCodigo MargemSuperior0" >Local</p>
                    <input type="text" name="local" id="local" class="campoSemTamanho alturaPadrao"  style="width:342px;" value="${evento.local}">
                </td>
            </tr>
            <tr>
                <td>
                    <br>
                    <table class="fmt">
                        <tr>
                            <td>
                                <p class="legendaCodigo MargemSuperior0" >Data</p>
                                <fmt:formatDate var="data" value="${evento.data}" pattern="dd/MM/yyyy" />
                                <input type="text" name="data" id="data" class="campoSemTamanho alturaPadrao"  style="width:70px;" value="${data}">
                            </td>
                            <td>
                                <p class="legendaCodigo MargemSuperior0" >Hora</p>
                                <input type="text" name="hora" id="hora" class="campoSemTamanho alturaPadrao"  style="width:40px;" value="${evento.hora}">
                            </td>
                            <td>
                                <p class="legendaCodigo MargemSuperior0" >Mesas</p>
                                <input type="text" name="qtMesas" id="qtMesas" class="campoSemTamanho alturaPadrao"  style="width:43px;" value="${evento.qtMesas}">
                            </td>
                            <td>
                                <p class="legendaCodigo MargemSuperior0" >Cadeiras</p>
                                <input type="text" name="qtCadeiras" id="qtCadeiras" class="campoSemTamanho alturaPadrao"  style="width:60px;" value="${evento.qtCadeiras}">
                            </td>
                            <td>
                                <p class="legendaCodigo MargemSuperior0" >Ingressos</p>
                                <input type="text" name="qtIngressos" id="qtIngressos" class="campoSemTamanho alturaPadrao"  style="width:60px;" value="${evento.qtIngressos}">
                            </td>
                        </tr>
                    </table>
                </td>
            </tr>
        </table>
            
        
        <input type="button" class="botaoatualizar"  onclick="validarForm()" value=" " />
        <input type="button" class="botaoVoltar"  onclick="window.location='c?app=1560';" value=" " />

    </form>

</body>
</html>
