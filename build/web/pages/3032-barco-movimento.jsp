<%@include file="head.jsp"%>

<style>
    table {border:none;width:0;padding:0px;margin-left:0em;margin-top:0em;}
    tr {border:none;background:none;padding:0px;margin:0em auto;}
    td {border:none;background:none;padding:0px;margin:0em auto;}
</style>  

<body class="internas">
            
    <%@include file="menu.jsp"%>
    
    <div class="divisoria"></div>
        <div id="titulo-subnav">Registrar Movimento de Embarcação</div>
    <div class="divisoria"></div>
    
    <script type="text/javascript" LANGUAGE="JavaScript" SRC="js/calendario.js"></script>
    <link rel="stylesheet" type="text/css" href="css/calendario.css"/>

    <script type="text/javascript" language="JavaScript" src="js/format.js"></script>
    
    <script type="text/javascript" language="javascript">
        $(document).ready(function () {
            $("#dataMovimento").mask("99/99/9999");
            $("#horaMovimento").mask("99:99");
        });     
    </script>
    
    <script type="text/javascript" language="JavaScript">

        function validarForm(){

            if(!isDataValida(document.forms[0].dataMovimento.value)){
                return;
            }
            if(!isHoraValida(document.forms[0].horaMovimento.value)){
                return;
            }            

            document.forms[0].submit();
        }

    </script>

    <form action="c" method="POST">
        <input type="hidden" name="app" value="3036">
        <input type="hidden" name="acao" value="gravar">
        <input type="hidden" name="idBarco" value="${barco.id}">

        <table>
            <tr>
                <td>
                    <p class="legendaCodigo MargemSuperior0" >Dt. Movimento:</p>
                    <input type="text" name="dataMovimento" id="dataMovimento" class="campoSemTamanho alturaPadrao larguraData">
                </td>
                <td>
                    <p class="legendaCodigo MargemSuperior0" >Hora Movimento:</p>
                    <input type="text" name="horaMovimento" id="horaMovimento" class="campoSemTamanho alturaPadrao larguraData">
                </td>
                <td>
                    <p class="legendaCodigo MargemSuperior0" >Tipo</p>
                    <div class="selectheightnovo">
                        <select name="tipo" class="campoSemTamanho alturaPadrao larguraData">
                            <option value="E" <c:if test='${titular.socio.situacao == "E"}'>selected</c:if>>Entrada</option>
                            <option value="S" <c:if test='${titular.socio.situacao == "S"}'>selected</c:if>>Saída</option>
                        </select>        
                    </div>    
                    
                </td>
                <td>
                    <p class="legendaCodigo MargemSuperior0" >Local</p>
                    <div class="selectheightnovo">
                        <select name="local" class="campoSemTamanho alturaPadrao larguraData">
                            <option value="C" <c:if test='${titular.socio.situacao == "C"}'>selected</c:if>>Cais</option>
                            <option value="L" <c:if test='${titular.socio.situacao == "L"}'>selected</c:if>>Clube</option>
                            <option value="A" <c:if test='${titular.socio.situacao == "A"}'>selected</c:if>>Água</option>
                            <option value="R" <c:if test='${titular.socio.situacao == "R"}'>selected</c:if>>Rampa</option>
                        </select>                
                    </div>    
                </td>
                <td style="width:500px">
                    <p class="legendaCodigo MargemSuperior0" >Nr. Passag.:</p>
                    <input type="text" class="campoSemTamanho alturaPadrao larguraNumero" onkeypress="onlyNumber(event)" name="passageiros"><br>    
                </td>
            </tr>
        </table>
        
        <input type="button" class="botaoatualizar"  onclick="validarForm()" value=" " />
        <input type="button" class="botaoVoltar"  onclick="window.location='c?app=2000&acao=showListaMovimento&idBarco=${barco.id}';" value=" " />
    </form>

</body>
</html>
