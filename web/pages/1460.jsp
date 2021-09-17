<%@include file="head.jsp"%>

<style type="text/css">
    table.fmt {border:none;width:0;padding:0px;margin-left:0em;margin-top:0em;}
    table.fmt tr td {border:none;background:none;padding:0px;margin:0em auto;}
</style>  

<script type="text/javascript" language="JavaScript" src="js/format.js"></script>
<script type="text/javascript" language="JavaScript">    
    $(document).ready(function () {
            $("#dataInicio").mask("99/99/9999");
            $("#dataFim").mask("99/99/9999");
           
    });   
    
    function validarForm(){
        var k = 0;
        
        if(trim(document.forms[0].dataInicio.value) == ''
            || trim(document.forms[0].dataFim.value) == ''){
            alert('� preciso informar as datas iniciais e finais para o relat�rio');
            return;
        }
        if(!isDataValida(document.forms[0].dataInicio.value)){
            return;
        }
        if(!isDataValida(document.forms[0].dataFim.value)){
            return;
        }
        
        if(!document.forms[0].reservada.checked 
                && !document.forms[0].confirmada.checked){
            alert('Selecione pelo menos uma situa��o da Reserva.');
            return;
        }
        
        for(var i = 0; i < document.forms[0].dependencias.length; i++){
            if(document.forms[0].dependencias[i].checked){
                k++;
            }
        }
        if(k == 0){
            alert('Selecione pelo menos uma depend�ncia');
            return;
        }
        
        k = 0;
        for(var i = 0; i < document.forms[0].tiposEvento.length; i++){
            if(document.forms[0].tiposEvento[i].checked){
                k++;
            }
        }
        if(k == 0){
            alert('Selecione pelo menos um tipo de evento');
            return;
        }        
        
        k = 0;
        for(var i = 0; i < document.forms[0].itensAluguel.length; i++){
            if(document.forms[0].itensAluguel[i].checked){
                k++;
            }
        }
        if(k == 0){
            alert('Selecione pelo menos um item de aluguel');
            return;
        }
        
        k = 0;
        for(var i = 0; i < document.forms[0].colunas.length; i++){
            if(document.forms[0].colunas[i].checked){
                k++;
            }
        }
        if(k == 0){
            alert('Selecione pelo menos uma informa��o a ser vista no Relat�rio');
            return;
        }        

        document.forms[0].submit();
    }

</script>

<body class="internas">

    <%@include file="menu.jsp"%>

    <div class="divisoria"></div>
    <div id="titulo-subnav">Agenda Geral de Eventos - Filtro</div>
    <div class="divisoria"></div>
    
    <form method="POST" action="c">
	<input type="hidden" name="app" value="1460">
        <input type="hidden" name="acao" value="visualizar">
                    
        <table class="fmt">
            <tr>
                <td>
                    <fieldset class="field-set legendaFrame recuoPadrao" style="height:65px;width:270px;">
                        <legend >Per�odo:</legend>
                        <input type="text" name="dataInicio" id="dataInicio" style="margin-top:6px;" class="campoSemTamanho alturaPadrao larguraData">    
                        &nbsp;&nbsp;&nbsp;a
                        <input type="text" name="dataFim" id="dataFim" style="margin-top:6px;"  class="campoSemTamanho alturaPadrao larguraData">        
                    </fieldset>                                
                </td>
                <td>
                    <fieldset class="field-set legendaFrame recuoPadrao" style="height:65px;width:270px;">
                        <legend >Situa��o da Reserva:</legend>
                        <input type="checkbox" name="reservada" style="margin-top:13px;margin-left:35px;" value="true" checked>Reservada
                        <input type="checkbox" name="confirmada" style="margin-top:13px;margin-left:35px;" value="true" checked>Confirmada<br>
                    </fieldset>
                </td>
                <td>
                    &nbsp;
                </td>
            </tr>
            <tr>
                <td>
                    <p class="legendaCodigo">Dependencias:<span><input class="botaoMarcaDesmarca" type="button"  onclick="marcaDesmarca('dependencias')" style="margin-top: 2px;" value="" title="Consultar" /></span></p>
                    <div class="recuoPadrao" style="overflow:auto;height:200px;width:270px;">
                        <c:forEach var="dependencia" items="${dependencias}">
                            <input type="checkbox" name="dependencias" value="${dependencia.id}">${dependencia.descricao}<br>
                        </c:forEach>
                    </div>
                </td>
                <td>
                    <p class="legendaCodigo">Tipos de Evento:<span><input class="botaoMarcaDesmarca" type="button"  onclick="marcaDesmarca('tiposEvento')" style="margin-top: 2px;" value="" title="Consultar" /></span></p>
                    <div class="recuoPadrao" style="overflow:auto;height:200px;width:270px;">
                        <input type="checkbox" name="tiposEvento" value="0">NENHUM<br>
                        <c:forEach var="tipoEvento" items="${tiposEvento}">
                            <input type="checkbox" name="tiposEvento" value="${tipoEvento.id}">${tipoEvento.descricao}<br>
                        </c:forEach>
                    </div>        
                </td>
                <td>
                    <p class="legendaCodigo">Que Contenham os �tens:<span><input class="botaoMarcaDesmarca" type="button"  onclick="marcaDesmarca('itensAluguel')" style="margin-top: 2px;" value="" title="Consultar" /></span></p>
                    <div class="recuoPadrao" style="overflow:auto;height:200px;width:270px;">
                        <input type="checkbox" name="itensAluguel" value="0">NENHUM<br>
                        <c:forEach var="itenAluguel" items="${itensAluguel}">
                            <input type="checkbox" name="itensAluguel" value="${itenAluguel.id}">${itenAluguel.descricao}<br>
                        </c:forEach>
                    </div>                
                </td>
            </tr>
        </table>
        
        <div class="divisoria"></div>
        <div id="titulo-subnav">Informa��es</div>
        <div class="divisoria"></div>

        <table class="fmt">
            <tr>
                <td>
                    <p class="legendaCodigo">Informa��es:</p>
                    <div class="recuoPadrao" style="overflow:auto;height:200px;width:270px;">
                        <input type="checkbox" name="colunas" value="CONVERT(VARCHAR, T1.DT_INIC_UTILIZACAO, 103) + ' ' + LEFT(CONVERT(VARCHAR, T1.DT_INIC_UTILIZACAO, 108), 5) + ' - ' + CONVERT(VARCHAR, T1.DT_FIM_UTILIZACAO, 103) + ' ' + LEFT(CONVERT(VARCHAR, T1.DT_FIM_UTILIZACAO, 108), 5) AS 'PER�ODO'">Per�odo<br>
                        <input type="checkbox" name="colunas" value="T2.DESCR_DEPENDENCIA AS 'DEPEND�NCIA'">Depend�ncia<br>
                        <input type="checkbox" name="colunas" value="ISNULL(T3.DESCR_TIPO_EVENTO, '<N�O INFORMADO>') AS 'TIPO DE EVENTO'">Tipo de Eventos<br>
                        <input type="checkbox" name="colunas" value="ISNULL(T1.NOME_INTERESSADO, '<N�O INFORMADO>') AS 'RESPONS�VEL'">Nome do Respons�vel<br>
                        <input type="checkbox" name="colunas" value="ISNULL(T1.TEL_CONTATO, '<N�O INFORMADO>') AS 'TEL. RESPONS�VEL'">Telefone do Respons�vel<br>
                        <input type="checkbox" name="colunas" value="ISNULL(T1.NO_CONTATO, '<N�O INFORMADO>') AS 'CONTATO'">Nome do Contato<br>
                        <input type="checkbox" name="colunas" value="ISNULL(T1.TEL_CONTATO_RES, '<N�O INFORMADO>') AS 'TEL. CONTATO'">Telefone do Contato<br>
                        <input type="checkbox" name="colunas" value="ISNULL(CONVERT(VARCHAR, T1.QT_PUBLICO_PREVISTO), '<N�O INFORMADO>') AS 'P�BLICO PREVISTO'">P�blico Previsto<br>
                        <input type="checkbox" name="colunas" value="T1.CD_STATUS_RESERVA AS 'SITU��O DA RESERVA'">Situa��o da Reserva<br>
                    </div>        
                </td>
                <td>
                    <p class="legendaCodigo">�tens:<span><input class="botaoMarcaDesmarca" type="button"  onclick="marcaDesmarca('itens')" style="margin-top: 2px;" value="" title="Consultar" /></span></p>
                    <div class="recuoPadrao" style="overflow:auto;height:190px;width:270px;">
                        <c:forEach var="itenAluguel" items="${itensAluguel}">
                            <input type="checkbox" name="itens" value="${itenAluguel.id}">${itenAluguel.descricao}<br>
                        </c:forEach>
                    </div>                
                </td>
            </tr>
        </table>
        
        <input type="button" onclick="validarForm()" class="botaoatualizar" value=" " />        
        
    </form>

</body>
</html>
