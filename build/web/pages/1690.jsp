<%@include file="head.jsp"%>

<style type="text/css">
    table.fmt {border:none;width:0;padding:0px;margin-left:0em;margin-top:0em;}
    table.fmt tr td {border:none;background:none;padding:0px;margin:0em auto;}
    
    #tipoRel {
          left: 40%;
          top: 40%;
          position: fixed;
      }
</style>  

<script type="text/javascript" language="JavaScript" src="js/format.js"></script>
<script type="text/javascript" language="JavaScript">    
   $(document).ready(function () {
             $('#tipoRel').hide();
    });
    
    function mostraTipoRel(){
        if (validarForm()==false){
            return;
        }
        $('#tipoRel').show();
    }
    function submeteImpressao(){
        $('#acao').val($('input[name=tipoRel]:checked').val());
        document.forms[0].submit();
    }
    function validarForm(){
        var k = 0;
        for(var i = 0; i < document.forms[0].categorias.length; i++){
            if(document.forms[0].categorias[i].checked){
                k++;
            }
        }
        if(k == 0){
            alert('Selecione pelo menos uma Categoria de Sócio.');
            return false;
        }
        
        k = 0;
        for(var i = 0; i < document.forms[0].categoriasNauticas.length; i++){
            if(document.forms[0].categoriasNauticas[i].checked){
                k++;
            }
        }
        if(k == 0){
            alert('Selecione pelo menos uma Categoria Náutica.');
            return false;
        }        
    
        if(trim(document.forms[0].tituloInicio.value) == ''
            || trim(document.forms[0].tituloFim.value) == ''){
            alert('Informe o título inicial e final para o Relatório!');
            return false;
        }
        if(parseInt(trim(document.forms[0].tituloInicio.value)) > parseInt(trim(document.forms[0].tituloFim.value))){
            alert('Número do Título final deve ser posterior ao inicial.');
            return false;
        }        
        
        
    }

</script>

<body class="internas">

    <%@include file="menu.jsp"%>

    <div class="divisoria"></div>
    <div id="titulo-subnav">Relatório de Vencimentos e Pessoas Autorizadas em Embarcarções</div>
    <div class="divisoria"></div>
    
    <form method="POST" action="c">
	<input type="hidden" name="app" value="1690">
        <input type="hidden" name="acao" id="acao">
                    
        <table class="fmt">
            <tr >
                <td valign="top">
                    <br>
                    <fieldset class="field-set legendaFrame recuoPadrao" style="width:210px;height: 50px">
                        <legend >Título:</legend>
                        &nbsp;&nbsp;&nbsp;<input type="text" name="tituloInicio" onkeypress="onlyNumber(event)" class="campoSemTamanho alturaPadrao larguraNumero">        
                        &nbsp;&nbsp;&nbsp;&nbsp;a
                        <input type="text" name="tituloFim" onkeypress="onlyNumber(event)" class="campoSemTamanho alturaPadrao larguraNumero">        
                    </fieldset>                                
                </td>
                <td>
                    <p class="legendaCodigo">Categorias:<span><input class="botaoMarcaDesmarca" type="button"  onclick="marcaDesmarca('categorias')" style="margin-top: 2px;" value="" title="Consultar" /></span></p>
                    <div class="recuoPadrao" style="overflow:auto;height:200px;width:210px;">
                        <c:forEach var="categoria" items="${categorias}">
                            <input type="checkbox" name="categorias" value="${categoria.id}">${categoria.descricao}<br>
                        </c:forEach>
                    </div>
                </td>
                <td>
                    <p class="legendaCodigo">Categorias Náuticas:<span><input class="botaoMarcaDesmarca" type="button"  onclick="marcaDesmarca('categoriasNauticas')" style="margin-top: 2px;" value="" title="Consultar" /></span></p>
                    <div class="recuoPadrao" style="overflow:auto;height:200px;width:210px;">
                        <c:forEach var="categoriaNautica" items="${categoriasNauticas}">
                            <input type="checkbox" name="categoriasNauticas" value="${categoriaNautica.cod}">${categoriaNautica.descricao}<br>
                        </c:forEach>
                    </div>        
                </td>
            </tr>
        </table>

        <input type="button" onclick="mostraTipoRel()" class="botaoatualizar" value=" " />        
        
        <div id="tipoRel" >
            <table style="background:#fff">
                <tr>
                  <td>
                    <div class="divisoria"></div>
                    <div id="titulo-subnav">Tipo de Histórico</div>
                    <div class="divisoria"></div>
                    <table class="fmt" align="left" align="left">
                      <tr>
                        <td>
                            <input type="radio" name="tipoRel" checked value="visualizarPessoaAutorizada"/>Relação de Pessoas Autorizadas<br>
                            <input type="radio" name="tipoRel" value="visualizarDocumentacaoVencida"/>Embarcações com Documentação Vencida<br>
                        <td>
                      </tr>
                      <tr>
                        <td align="center">
                            <br>
                            <input type="button" id="cmdPesquisa" name="cmdPesquisa" class="botaoimprimirnovo" onclick="submeteImpressao()" />
                        </td>
                      </tr>
                    </table>  
                </td>
              </tr>
            <table>
        </div>        


    </form>

</body>
</html>
