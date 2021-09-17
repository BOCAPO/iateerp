<%@page import="techsoft.curso.TurmaHorario"%>
<%@page import="techsoft.curso.Turma"%>
<%@taglib prefix="sql" uri="http://java.sun.com/jsp/jstl/sql"%>
<%@include file="head.jsp"%>

<style type="text/css">
    table.fmt {border:none;width:0;padding:0px;margin-left:0em;margin-top:0em;}
    table.fmt tr td {border:none;background:none;padding:0px;margin:0em auto;}
</style>  

<body class="internas">

    <%@include file="menu.jsp"%>
    <script type="text/javascript" language="JavaScript" src="js/format.js"></script>
    
    <div class="divisoria"></div>
    <div id="titulo-subnav">Alterar Desconto</div>
    <div class="divisoria"></div>

    <script type="text/javascript" language="JavaScript">

        function validarForm(){

            if(!trim(document.forms[0].descontoPessoal.value) == ''){
                if(parseInt(document.forms[0].descontoPessoal.value) < 0
                || parseInt(document.forms[0].descontoPessoal.value) > 100){
                    alert('O desconto pessoal deve ser entre 0 e 100 %.');
                    return;                
                }
            }else{
                alert('Infome o valor do desconto pessoal');
                return;
            }
            
            document.forms[0].submit();

        }

    </script>        

    <form action="c">
        <input type="hidden" name="app" value="3076">
        <input type="hidden" name="idTurma" value="${turma.id}">
        <input type="hidden" name="matricula" value="${socio.matricula}">
        <input type="hidden" name="idCategoria" value="${socio.idCategoria}">
        <input type="hidden" name="seqDependente" value="${socio.seqDependente}">
        <input type="hidden" name="acao" value="gravar">


        <p class="legendaCodigo" >Desconto</p>        
        <input type="text" name="descontoPessoal" value="${descontoPessoal}"class="campoSemTamanho alturaPadrao larguraNumero" onkeypress="onlyPositiveFloat(event)"><br>
        <br>
        <input type="submit" id="atualizar" class="botaoatualizar" value=" " />
        <input type="button" class="botaoVoltar"  onclick="window.location='c?app=3070&idTurma=${turma.id}';" value=" " />
    </form>

        
</body>
</html>
