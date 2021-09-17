<%@include file="head.jsp"%>

<style type="text/css">
    table.fmt {border:none;width:0;padding:0px;margin-left:0em;margin-top:0em;}
    table.fmt tr td {border:none;background:none;padding:0px;margin:0em auto;}
</style>  

<script type="text/javascript" language="javascript">
    $(document).ready(function () {
        $("#dataReferencia").mask("99/99/9999");
    });     
</script>  

<script type="text/javascript" language="JavaScript" src="js/format.js"></script>
<script type="text/javascript" language="JavaScript">

    function validarForm(){

        if (
            document.forms[0].dataReferencia.value == "" &&
            document.forms[0].categoria.value == "" &&
            document.forms[0].titulo.value == ""
            ){
            alert('Informe a categoria/título ou a data de referência para pesquisa!');
            return false;
        }
        
        if (document.forms[0].dataReferencia.value != ""){
            if(!isDataValida(document.forms[0].dataReferencia.value)){
                return;
            }
        }else{
            if (
                document.forms[0].categoria.value == "" ||
                document.forms[0].titulo.value == ""
                ){
                alert('Informe a categoria/título para pesquisa!');
                return false;
            }
        }
        
        document.forms[0].submit();
    }

</script>

<body class="internas">
            
    <%@include file="menu.jsp"%>

    <div class="divisoria"></div>
    <div id="titulo-subnav">Pessoas com autorização para reservar churrasqueiras</div>
    <div class="divisoria"></div>

    <form action="c" id="it-bloco01">

        <input type="hidden" name="app" value="2590"/>
        <input type="hidden" name="acao" value="gerar"/>   
        <table class="fmt">
            <tr>
                <td>
                    <p class="legendaCodigo MargemSuperior0" >Categoria</p> 
                    <input type="text" name="categoria" id="categoria" class="campoSemTamanho alturaPadrao" style="width: 50px">
                    
                </td>
                <td>
                    <p class="legendaCodigo MargemSuperior0" >Título</p> 
                    <input type="text" name="titulo" id="titulo" class="campoSemTamanho alturaPadrao" style="width: 50px">
                    
                </td>
                <td>
                    <p class="legendaCodigo MargemSuperior0" >Data Referência</p> 
                    <input type="text" name="dataReferencia" id="dataReferencia" class="campoSemTamanho alturaPadrao larguraData">
                    
                </td>
            </tr>
        </table>

        <br>

        <input type="button" id="btnAtualizar" class="botaoatualizar"  onclick="validarForm()" value=" " />

    </form>
</body>
</html>


