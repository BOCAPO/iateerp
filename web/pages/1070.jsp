
<%@include file="head.jsp"%>

<style type="text/css">
    table.fmt {border:none;width:0;padding:0px;margin-left:0em;margin-top:0em;}
    table.fmt tr td {border:none;background:none;padding:0px;margin:0em auto;}
</style>  

<script type="text/javascript" language="JavaScript">
    $(document).ready(function () {
            $('#tabela tr').css('background', 'white');

            $('#tabela tr').hover(function() {
                    $(this).css('background','#f4f9fe');
                    //$(this).find('img').width(120);
                    //$(this).find('img').height(160);
            }, function() {
                    $(this).css('background','white');
                    //$(this).find('img').width(0);
                    //$(this).find('img').height(0);
            })
    });        
</script>  


<body class="internas">

    <script type="text/javascript" language="JavaScript" src="js/format.js"></script>
    
    <%@include file="menu.jsp"%>
        
    <div class="divisoria"></div>
    <div id="titulo-subnav">Inclusão de Convite - Seleção de Tipo</div>
    <div class="divisoria"></div>
    
    
    <form action="c" method="POST">
        <input type="hidden" name="app" value="1071">
        <input type="hidden" name="acao" value="socio">

        <table id="tabela" style="width:98%;margin-left:15px;">
            <tbody>
                <tr>
                    <td class="column1" align="center" style="width:20px;"><input style="margin-top:7px;" type="radio" name="selecao" checked value="GR"/></td>
                    <td class="column1" align="left">GERAL</td>
                </tr>
                <tr>
                    <td class="column1" align="center" style="width:20px;"><input style="margin-top:7px;" type="radio" name="selecao"  value="CH"/></td>
                    <td class="column1" align="left">CHURRASQUEIRA</td>
                </tr>
                <tr>
                    <td class="column1" align="center" style="width:20px;"><input style="margin-top:7px;" type="radio" name="selecao"  value="SA"/></td>
                    <td class="column1" align="left">SAUNA</td>
                </tr>
                <tr>
                    <td class="column1" align="center" style="width:20px;"><input style="margin-top:7px;" type="radio" name="selecao"  value="SI"/></td>
                    <td class="column1" align="left">SINUCA</td>
                </tr>
                <tr>
                    <td class="column1" align="center" style="width:20px;"><input style="margin-top:7px;" type="radio" name="selecao"  value="ES"/></td>
                    <td class="column1" align="left">ESPECIAL SÓCIO</td>
                </tr>
                <tr>
                    <td class="column1" align="center" style="width:20px;"><input style="margin-top:7px;" type="radio" name="selecao"  value="EC"/></td>
                    <td class="column1" align="left">
                        ESPECIAL CONVENIO
                        <select name="conv" class="campoSemTamanho alturaPadrao" >
                        <c:forEach var="convenio" items="${convenios}">
                            <option value="${convenio.id}">${convenio.descricao}</option>
                        </c:forEach>
                        </select>
                    </td>
                    
                </tr>
                <tr>
                    <td class="column1" align="center" style="width:20px;"><input style="margin-top:7px;" type="radio" name="selecao"  value="ED"/></td>
                    <td class="column1" align="left">ESPECIAL DIRETOR</td>
                </tr>
                <tr>
                    <td class="column1" align="center" style="width:20px;"><input style="margin-top:7px;" type="radio" name="selecao"  value="EV"/></td>
                    <td class="column1" align="left">ESPECIAL VICE-DIRETOR</td>
                </tr>
                <tr>
                    <td class="column1" align="center" style="width:20px;"><input style="margin-top:7px;" type="radio" name="selecao"  value="EO"/></td>
                    <td class="column1" align="left">ESPECIAL VICE-COMODORO</td>
                </tr>
                <tr>
                    <td class="column1" align="center" style="width:20px;"><input style="margin-top:7px;" type="radio" name="selecao"  value="EA"/></td>
                    <td class="column1" align="left">ESPECIAL ASSESSOR-COMODORO</td>
                </tr>
                <tr>
                    <td class="column1" align="center" style="width:20px;"><input style="margin-top:7px;" type="radio" name="selecao"  value="EN"/></td>
                    <td class="column1" align="left">ESPECIAL CONSELHEIRO</td>
                </tr>
                <tr>
                    <td class="column1" align="center" style="width:20px;"><input style="margin-top:7px;" type="radio" name="selecao"  value="EP"/></td>
                    <td class="column1" align="left">ESPECIAL PRESIDENTE DO CONSELHO</td>
                </tr>
                <tr>
                    <td class="column1" align="center" style="width:20px;"><input style="margin-top:7px;" type="radio" name="selecao"  value="CO"/></td>
                    <td class="column1" align="left">INSTITUCIONAL</td>
                </tr>
                <tr>
                    <td class="column1" align="center" style="width:20px;"><input style="margin-top:7px;" type="radio" name="selecao"  value="VD"/></td>
                    <td class="column1" align="left">EVENTOS DIRETOR</td>
                </tr>
                <tr>
                    <td class="column1" align="center" style="width:20px;"><input style="margin-top:7px;" type="radio" name="selecao"  value="VV"/></td>
                    <td class="column1" align="left">EVENTOS VICE-DIRETOR</td>
                </tr>
                <tr>
                    <td class="column1" align="center" style="width:20px;"><input style="margin-top:7px;" type="radio" name="selecao"  value="VC"/></td>
                    <td class="column1" align="left">EVENTOS COMODORO</td>
                </tr>
                <tr>
                    <td class="column1" align="center" style="width:20px;"><input style="margin-top:7px;" type="radio" name="selecao"  value="VO"/></td>
                    <td class="column1" align="left">EVENTOS VICE-COMODORO</td>
                </tr>
                <tr>
                    <td class="column1" align="center" style="width:20px;"><input style="margin-top:7px;" type="radio" name="selecao"  value="VA"/></td>
                    <td class="column1" align="left">EVENTOS ASSESSOR-COMODORO</td>
                </tr>
                <tr>
                    <td class="column1" align="center" style="width:20px;"><input style="margin-top:7px;" type="radio" name="selecao"  value="VN"/></td>
                    <td class="column1" align="left">EVENTOS CONSELHEIRO</td>
                </tr>
                <tr>
                    <td class="column1" align="center" style="width:20px;"><input style="margin-top:7px;" type="radio" name="selecao"  value="VP"/></td>
                    <td class="column1" align="left">EVENTOS PRESIDENTE DO CONSELHO</td>
                </tr>
                <tr>
                    <td class="column1" align="center" style="width:20px;"><input style="margin-top:7px;" type="radio" name="selecao"  value="SP"/></td>
                    <td class="column1" align="left">ESPORTIVO</td>
                </tr>
            </tbody>
        </table>
        <input type="submit" id="atualizar" class="botaoatualizar"  value=" " />
        <input type="button" class="botaoVoltar"  onclick="window.location='c?app=1070';" value=" " />
        
    </form>
        

    
    
</body>
</html>
