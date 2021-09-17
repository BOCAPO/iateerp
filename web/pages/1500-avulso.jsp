<%@include file="head.jsp"%>

<style>
    table.fmt {border:none;width:0;padding:0px;margin-left:0em;margin-top:0em;}
    table.fmt tr td {border:none;background:none;padding:0px;margin:0em auto;}
</style>        
<script type="text/javascript" language="JavaScript" src="js/format.js"></script>

<script type="text/javascript" language="javascript">
    $(document).ready(function () {
            $("#dataRef").mask("99/99/9999");
    });
</script>

<script type="text/javascript" language="JavaScript">

    function validarForm(){
        if(document.forms[0].categoria.value == '' ){
            alert("Informe a Categoria!");    
            return false;    
            
        }
        if (isNaN(document.forms[0].categoria.value)) {    
               alert("Categoria inválida!");    
               return false;    
        } 
        if(document.forms[0].matricula.value == '' ){
            alert("Informe o Título!");    
            return false;    
            
        }
        if(document.forms[0].matricula.value != '' ){
            if (isNaN(document.forms[0].matricula.value)) {    
                   alert("Título inválido!");    
                   return false;    
            } 
        }
        if(!verifica_branco(document.forms[0].dataRef.value)){
           alert("É Obrigatório informar a Data!");
           return false;
        }

        if(!isDataValida(document.forms[0].dataRef.value)){
            return false; 
        }

        document.getElementById("botao").style.display = "none";
        
        document.forms[0].submit();
    }

</script>


<body class="internas">
            
    <%@include file="menu.jsp"%>

    <div class="divisoria"></div>
        <div id="titulo-subnav">Geração de Carne Avulso</div>
    <div class="divisoria"></div>

    <form action="c">
        <input type="hidden" name="app" value="1500"/>
        <input type="hidden" name="acao" value="gera"/>
        <input type="hidden" name="tipo" value="A"/>

        <table align="left" class="fmt">
          <tr>
            <td >
                <p class="legendaCodigo">Categoria: </p>
                <input type="text" name="categoria" maxlength="2" class="campoSemTamanho alturaPadrao" style="width: 50px"/>
            </td >
            <td >
                <p class="legendaCodigo">Titulo: </p>
                <input type="text" name="matricula" maxlength="5" class="campoSemTamanho alturaPadrao" style="width: 70px"/>
            </td>
            <td >
                <p class="legendaCodigo">Vencimento</p>
                <input type="text" name="dataRef" id="dataRef" maxlength="10" class="campoSemTamanho alturaPadrao larguraData" />
            </td>
          </tr>
          <tr>
              <td colspan="3">
                <br>
                <div id="botao">
                    <input type="button" class="botaoGerar"  onclick="validarForm()" value=" " />
                </div>        
            </td>
          </tr>
        </table>  

    </form>

</body>
</html>
