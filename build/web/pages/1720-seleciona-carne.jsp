<%@include file="head.jsp"%>

<style>
    table {border:none;width:300px;padding:0px;margin-left:1em;}
    tr {border:none;background:none;padding:0px;margin:0em auto;}
    td {border:none;background:none;padding:0px;margin:0em auto;}
</style>        
<script type="text/javascript" language="JavaScript" src="js/format.js"></script>

<script type="text/javascript" language="javascript">
    $(document).ready(function () {
            $("#dtVenc").mask("99/99/9999");
    });
</script>

<script type="text/javascript" language="JavaScript">

    function validarForm(){

        if(!verifica_branco(document.forms[0].seqCarne.value)){
            if(!verifica_branco(document.forms[0].categoria.value) || !verifica_branco(document.forms[0].matricula.value) || !verifica_branco(document.forms[0].dtVenc.value) ){
               alert("Informe corretamente o Sequencial do Carne ou a Matrícula, Categoria e Data de Vencimento!");
               return false;
            }
            if (isNaN(document.forms[0].categoria.value)) {    
                alert("Categoria inválida!");    
                return false;    
            } 
            if (isNaN(document.forms[0].matricula.value)) {    
                alert("Matricula inválida!");    
                return false;    
            } 
            if(!isDataValida(document.forms[0].dtVenc.value)){
                return false; 
            }
        }else{
            if (isNaN(document.forms[0].seqCarne.value)) {    
                alert("Sequencial inválido!");    
                return false;    
            } 
        }

        document.forms[0].submit();
    }

</script>


<body class="internas">
            
    <%@include file="menu.jsp"%>

    <div class="divisoria"></div>
        <div id="titulo-subnav">Baixa Manual de Carne - Seleção</div>
    <div class="divisoria"></div>
    
    
    <form action="c">
        <input type="hidden" name="app" value="1720"/>
        <input type="hidden" name="acao" value="validacarne"/>

        <c:if test='${bmc.valor == 0}'>
            <script type="text/javascript" language="JavaScript">
                alert('Carne não encontrado. Verifique os dados p/ seleção');
            </script>
        </c:if>
        
        
        <table align="left">
          <tr>
              <td colspan="3">
                <p class="legendaCodigo">Seq. Carne:</p>
                <input type="text" name="seqCarne" maxlength="8" class="campoSemTamanho alturaPadrao larguraData" />
            </td>
          </tr>
          <tr>
            <td>
                <p class="legendaCodigo">Categoria:</p>
                <input type="text" name="categoria" maxlength="2" class="campoSemTamanho alturaPadrao larguraData" />
            </td>
            <td>
                <p class="legendaCodigo">Matrícula:</p>
                <input type="text" name="matricula" maxlength="4" class="campoSemTamanho alturaPadrao larguraData"/>
            </td>
            <td>
                <p class="legendaCodigo">Dt. Vencimento</p>
                <input type="text" name="dtVenc" id="dtVenc" maxlength="10" class="campoSemTamanho alturaPadrao larguraData"/>
            </td>
          </tr>
        </table>  

        <br><br><br><br><br><br><br><br><br>
        <input type="button" class="botaoatualizar"  onclick="validarForm()" value=" " />
        <input type="button" class="botaoVoltar"  onclick="window.location='c?app=1720';" value=" " />
    </form>

    
</body>
</html>
