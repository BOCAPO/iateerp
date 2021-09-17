<%@include file="head.jsp"%>
<script type="text/javascript" language="JavaScript" src="js/format.js"></script>
<style type="text/css">
    table.fmt {border:none;width:0;padding:0px;margin-left:0em;margin-top:0em;}
    table.fmt tr td {border:none;background:none;padding:0px;margin:0em auto;}
</style>  

<script type="text/javascript" language="JavaScript">    
    $(document).ready(function () {
            
        $("#dtInicio").mask("99/99/9999");
        $("#dtFim").mask("99/99/9999");            
            
    });
    
    function validarForm(){
        if(trim(document.forms[0].dtInicio.value) == ''
                || trim(document.forms[0].dtFim.value) == ''){
            alert('É preciso preencher o período!');
            return;
        }            
        if(!isDataValida(document.forms[0].dtInicio.value)){
            return;
        }
        if(!isDataValida(document.forms[0].dtFim.value)){
            return;
        }            
        
        var j = 0;
        for(var i = 0; i < document.forms[0].servicos.length; i++){
            if(document.forms[0].servicos[i].checked){
                j++;
            }
        }
        if(j == 0){
            alert('Selecione pelo menos um Serviço.');
            return;
        }
        
        document.forms[0].submit();
    }
    
    

</script>

<body class="internas">

    <%@include file="menu.jsp"%>

    <div class="divisoria"></div>
    <div id="titulo-subnav">Relatório Quantitativo</div>
    <div class="divisoria"></div>
    
    <form method="POST" action="c">
	<input type="hidden" name="app" value="2450">
	<input type="hidden" name="acao" value="visualizar">
                     
        <table class="fmt">
            <tr>
                <td>
                    <fieldset class="field-set legendaFrame recuoPadrao" style="width:225px;height:45px;margin-top: 30px">
                        <legend >Tipo:</legend>        
                        <input type="radio" name="tipo" class="legendaCodigo" style="margin-top:3px" value="A" checked>Agendamento
                        <input type="radio" name="tipo" class="legendaCodigo" style="margin-top:3px" value="S">Cancelamento
                    </fieldset>
                </td>
            </tr>
            <tr>
                <td>
                    <fieldset class="field-set legendaFrame recuoPadrao" style="width:225px;height:50px;margin-top: 30px">
                        <legend >Período</legend>
                        <input type="text" id="dtInicio" name="dtInicio" class="campoSemTamanho alturaPadrao" style="width: 77px">        
                        &nbsp;&nbsp;&nbsp;a
                        <input type="text" id="dtFim"  name="dtFim" class="campoSemTamanho alturaPadrao" style="width: 77px">        
                    </fieldset>
                </td>
            </tr>
            <tr>
                <td>
                    <p class="legendaCodigo">Serviços:<span><input class="botaoMarcaDesmarca" type="button" id="marcaTaxas"  onclick="marcaDesmarca('servicos')" style="margin-top: 0px;" value="" title="Consultar" /></span></p>
                    <div id="divtaxas" class="recuoPadrao" style="overflow:auto;height:200px;width:225px;">
                        <c:forEach var="servico" items="${servicos}">
                            <input type="checkbox" name="servicos" value="${servico.id}">${servico.descricao}<br>
                        </c:forEach>
                    </div>
                </td>
            </tr>
        </table>
                    
        <input type="button" onclick="validarForm()" class="botaoatualizar" value=" " />        
        
    </form>

</body>
</html>
