<%@include file="head.jsp"%>

<style type="text/css">
    table.fmt {border:none;width:0;padding:0px;margin-left:0em;margin-top:0em;}
    table.fmt tr td {border:none;background:none;padding:0px;margin:0em auto;}
</style> 

<script type="text/javascript" language="JavaScript" src="js/format.js"></script>

<script type="text/javascript" language="JavaScript">
    $(document).ready(function () {
        $('#nascimentoFim').mask('99/99/9999');
        $('#nascimentoInicio').mask('99/99/9999');

    });

    function visualizar() {
        document.forms[0].acao.value = 'visualizar';
        validarForm();
    }

    function gerarEtiqueta() {
        document.forms[0].acao.value = 'gerarEtiqueta';
        validarForm();
    }

    function validarForm() {
        var k = 0;
        for (var i = 0; i < document.forms[0].categorias.length; i++) {
            if (document.forms[0].categorias[i].checked) {
                k++;
            }
        }
        if (k == 0) {
            alert('Selecione pelo menos uma Categoria!');
            return;
        }

        k = 0;
        for (var i = 0; i < document.forms[0].estadosCivis.length; i++) {
            if (document.forms[0].estadosCivis[i].checked) {
                k++;
            }
        }
        if (k == 0) {
            alert('Selecione pelo menos um Estado Civil!');
            return;
        }

        if (!isDataValida(document.forms[0].nascimentoInicio.value)) {
            return;
        }
        if (!isDataValida(document.forms[0].nascimentoFim.value)) {
            return;
        }


        document.forms[0].submit();
    }

</script>

<body class="internas">

    <%@include file="menu.jsp"%>

    <div class="divisoria"></div>
    <div id="titulo-subnav">Relatório de Geral de Sócio</div>
    <div class="divisoria"></div>

    <form method="POST" action="c">
        <input type="hidden" name="app" value="1520">
        <input type="hidden" name="acao">
        <table class="fmt">
            <tr>
                <td>
                    <p class="legendaCodigo">Categorias:<span><input class="botaoMarcaDesmarca" type="button"  onclick="marcaDesmarca('categorias')" style="margin-top: 2px;" value="" title="Consultar" /></span></p>
                    <div class="recuoPadrao" style="overflow:auto;height:500px;width:300px;">
                        <c:forEach var="categoria" items="${categorias}">
                            <input type="checkbox" name="categorias" value="${categoria.id}">${categoria.descricao}<br>
                        </c:forEach>
                    </div>
                </td>
                <td>
                    <table class="fmt">
                        <tr>
                            <td>
                                <br>
                                <fieldset class="field-set legendaFrame recuoPadrao" style="width:200px;height: 50px">
                                    <legend >Título:</legend>
                                    &nbsp;<input type="text" name="tituloInicio" onkeypress="onlyNumber(event)" class="campoSemTamanho alturaPadrao larguraNumero">
                                    &nbsp;&nbsp;&nbsp;&nbsp;a
                                    <input type="text" name="tituloFim" onkeypress="onlyNumber(event)" class="campoSemTamanho alturaPadrao larguraNumero">        
                                </fieldset>                                
                            </td>
                        </tr>
                        <tr>
                            <td>
                                <br>
                                <fieldset class="field-set legendaFrame recuoPadrao" style="width:200px;height: 40px">
                                    <legend >Sexo:</legend>
                                    &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<input type="checkbox" name="masculino" value="true" checked>Masculino
                                    &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<input type="checkbox" name="feminino" value="true" checked>Feminino<br>
                                </fieldset>
                            </td>
                        </tr>
                        <tr>
                            <td>
                                <br>
                                <fieldset class="field-set legendaFrame recuoPadrao" style="width:200px;height: 50px">
                                    <legend >Nascimento:</legend>
                                    &nbsp;<input type="text" name="nascimentoInicio" id="nascimentoInicio" class="campoSemTamanho alturaPadrao larguraNumero">    
                                    &nbsp;&nbsp;&nbsp;&nbsp;a
                                    <input type="text" name="nascimentoFim" id="nascimentoFim" class="campoSemTamanho alturaPadrao larguraNumero">        
                                </fieldset>                                
                            </td>
                        </tr>
                        <tr>
                            <td>
                                <p class="legendaCodigo">Estado Civil:<span><input class="botaoMarcaDesmarca" type="button"  onclick="marcaDesmarca('estadosCivis')" style="margin-top: 2px;" value="" title="Consultar" /></span></p>
                                <div class="recuoPadrao" style="overflow:auto;height:140px;width:200px;">
                                    <input type="checkbox" name="estadosCivis" value="US">NÃO INFORMADO<BR>
                                    <input type="checkbox" name="estadosCivis" value="CA">Casado(a)<BR>
                                    <input type="checkbox" name="estadosCivis" value="DI">Divorciado(a)<BR>
                                    <input type="checkbox" name="estadosCivis" value="DE">Desquitado(a)<BR>
                                    <input type="checkbox" name="estadosCivis" value="OU">Outro<BR>
                                    <input type="checkbox" name="estadosCivis" value="SO">Solteiro(a)<BR>
                                    <input type="checkbox" name="estadosCivis" value="VI">Viúvo(a)<BR>
                                </div>
                            </td>
                        </tr>
                        <tr>
                            <td>
                                <br>
                                <fieldset class="field-set legendaFrame recuoPadrao" style="width:200px;height: 45px">
                                    <legend >Informações:</legend>
                                    &nbsp;<input type="radio" name="informacao" class="legendaCodigo" value="E" checked>Estado Civil
                                    <input type="radio" name="informacao" class="legendaCodigo" value="T">Telefone
                                </fieldset>        
                            </td>
                        </tr>
                        <tr>
                            <td>
                                <br>
                                <input class="legendaCodigo" type="checkbox" name="semCPFCNPJ" value="true">CPF/CNPJ não preenchido
                            </td>
                        </tr>
                        <tr>
                            <td>
                                <br>
                                <fieldset class="field-set legendaFrame recuoPadrao" style="width: 200px;">
                                    <legend >Espólio:</legend>        

                                    <table class="fmt">
                                        <tr>
                                            <td>
                                                <select name="espolio" class="campoSemTamanho alturaPadrao" style="width:175px">
                                                    <option value="T">Todos</option>
                                                    <option value="S">Espólio</option>
                                                    <option value="N">Não Espólio</option>
                                                </select>                
                                            </td>
                                        </tr>
                                    </table>
                                </fieldset>      
                            </td>
                        </tr>

                    </table>
                </td>
            </tr>
        </table>
        <input type="button" onclick="visualizar()" class="botaoatualizar" value=" " />        
        <input type="button" onclick="gerarEtiqueta()" class="botaoImprimirEtiquetas"  value=" " />
    </form>

</body>
</html>
