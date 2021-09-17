<%@include file="head.jsp"%>
<%@include file="menuCaixa.jsp"%>

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
        
        document.forms[0].submit();
    }

</script>

<body class="internas">

    <div class="divisoria"></div>
    <div id="titulo-subnav">Relatório de Movimentação de Caixa</div>
    <div class="divisoria"></div>
    
    <form method="POST" action="c">
	<input type="hidden" name="app" value="6060">
	<input type="hidden" name="acao" value="visualizar">

        <table class="fmt">
            <tr>
                <td>
                    <table class="fmt">
                        <tr>
                            <td>
                                <fieldset class="field-set legendaFrame recuoPadrao" style="width:200px;height: 50px;">
                                    <legend >Período:</legend>        
                                    &nbsp;<input type="text" id="dataInicio" name="dataInicio" class="campoSemTamanho alturaPadrao larguraNumero" value="${dtAtual}">
                                    &nbsp;&nbsp;&nbsp;&nbsp;a
                                    <input type="text" id="dataFim"  name="dataFim" class="campoSemTamanho alturaPadrao larguraNumero" value="${dtAtual}">        
                                </fieldset>
                            </td>
                            <td>
                                <fieldset class="field-set legendaFrame recuoPadrao" style="width:240px;height: 50px;">
                                    <legend >Transação:</legend>                
                                    <select name="idTransacao" class="campoSemTamanho alturaPadrao" style="margin-top:3px;width:210px">
                                        <option value="0">TODAS</option>
                                        <option value="-1">ESTORNO</option>
                                        <c:forEach var="t" items="${transacoes}">
                                            <option value="${t.id}">${t.descricao}</option>
                                        </c:forEach>
                                    </select>
                                </fieldset>
                            </td>
                            <td>
                                <fieldset class="field-set legendaFrame recuoPadrao" style="width:240px;height: 50px;">
                                    <legend >Caixa:</legend>                
                                    <select name="idCaixa" class="campoSemTamanho alturaPadrao" style="margin-top:3px;width:210px">
                                        <option value="0">TODOS</option>
                                        <c:forEach var="c" items="${caixas}">
                                            <option value="${c.id}">${c.descricao}</option>
                                        </c:forEach>
                                    </select>
                                </fieldset>
                            </td>                            
                            <td>
                                <fieldset class="field-set legendaFrame recuoPadrao" style="width:120px;height: 50px;">
                                    <legend >Forma de Pagamento</legend>  
                                    <input  type="checkbox" name="formaPag" id="formaPag" value="true" style="margin-top:8px;margin-left:20px;">Detalhar<br>
                                </fieldset>
                            </td>
                        </tr>
                    </table>
                </td>
            </tr>
        </table>
        


        <input type="button" onclick="validarForm()" class="botaoatualizar" value=" " />        
        
    </form>

</body>
</html>
