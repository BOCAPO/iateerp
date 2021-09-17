<%@include file="head.jsp"%>
<%@include file="menu.jsp"%>

<script type="text/javascript" language="JavaScript" src="js/format.js"></script>
<style type="text/css">
    table.fmt {border:none;width:0;padding:0px;margin-left:0em;margin-top:0em;}
    table.fmt tr td {border:none;background:none;padding:0px;margin:0em auto;}
    
    .zera{ 
    font-family: "Trebuchet MS",Verdana,Arial,Helvetica,sans-serif;
    color: #678197;
    font-size: 12px;
    font-weight: normal;
    }    
</style>  


<script type="text/javascript" language="JavaScript">    
    $(document).ready(function () {
            
            $("#dataInicio").mask("99/99/9999");
            $("#dataFim").mask("99/99/9999");
            
    });    
    
    function validarForm(){
       
        if(trim(document.forms[0].dataInicio.value) == ''
                || trim(document.forms[0].dataFim.value) == ''){
            alert('Infome o período para o relatório');
            return;
        }
        
        if(!isDataValida(document.forms[0].dataInicio.value)){
            return;
        }
        if(!isDataValida(document.forms[0].dataFim.value)){
            return;
        }
        
        var k = 0;
        for(var i = 0; i < document.forms[0].dependencias.length; i++){
            if(document.forms[0].dependencias[i].checked){
                k++;
            }
        }
        if(k == 0){
            alert('Selecione pelo menos uma dependência.');
            return;
        }
                
        document.forms[0].submit();
    }

</script>

<body class="internas">

    <div class="divisoria"></div>
    <div id="titulo-subnav">Relatório de Reserva de Dependências</div>
    <div class="divisoria"></div>
    
    <form method="POST" action="c">
	<input type="hidden" name="app" value="1420">
	<input type="hidden" name="acao" value="visualizar">
        <table class="fmt">
            <tr valign="top">
                <td>
                    <p class="legendaCodigo">Dependências:<span><input class="botaoMarcaDesmarca" type="button" onclick="marcaDesmarca('dependencias')" style="margin-top: 2px;" value=""/></span></p>
                    <div class="recuoPadrao" style="overflow:auto;height:200px;width:227px;">
                        <c:forEach var="dependencia" items="${dependencias}">
                            <input type="checkbox" name="dependencias" value="${dependencia.id}">${dependencia.descricao}<br>
                        </c:forEach>
                    </div>
                </td>
                <td>
                    <br>   
                    <fieldset class="field-set legendaFrame recuoPadrao" style="width:200px;height:50px">
                        <legend >Período:</legend>        
                        &nbsp;<input type="text" id="dataInicio" name="dataInicio" class="campoSemTamanho alturaPadrao larguraNumero">
                        &nbsp;&nbsp;&nbsp;&nbsp;a
                        <input type="text" id="dataFim"  name="dataFim" class="campoSemTamanho alturaPadrao larguraNumero">        
                    </fieldset>
                    <br>    
                    <fieldset class="field-set legendaFrame recuoPadrao" style="width:200px;height:45px">
                        <legend >Tipo:</legend>                
                        &nbsp;&nbsp;<input type="radio" name="tipo" class="legendaCodigo" value="S" checked>Sintético
                        &nbsp;&nbsp;&nbsp;<input type="radio" name="tipo" class="legendaCodigo" value="A">Analítico
                    </fieldset>
                </td>
            </tr>
        </table>
            
            
        

        <input type="button" onclick="validarForm()" class="botaoatualizar" value=" " />        
        
    </form>

</body>
</html>
