<%@include file="head.jsp"%>

<style type="text/css">
    table.fmt {border:none;width:0;padding:0px;margin-left:0em;margin-top:0em;}
    table.fmt tr td {border:none;background:none;padding:0px;margin:0em auto;}
    
    #historico {
        left: 40%;
        top: 40%;
        position: fixed;
    }
</style>  

<body class="internas">

    <script type="text/javascript" language="JavaScript" src="js/format.js"></script>
    <script type="text/javascript" language="JavaScript">
                
        function validarForm(){

            if(trim(document.forms[0].idCracha.value) == ''
                || $('#cracha').val() <= 0){
                alert('Informe a número do crachá!');
                return;
            }
            
            var ret = "";
            $.ajax({url:'AjaxRegistroVisitante', async:false, dataType:'text', type:'GET',
                                data:{
                                idCracha:$('#cracha').val()
                                }
            }).success(function(retorno){
                ret = retorno;
            });

            if (ret!=""){
                alert(ret);
                return;
            }

            document.forms[0].submit();    
        }

    </script>  
    
    <%@include file="menu.jsp"%>        
    
    <div class="divisoria"></div>
    <div id="titulo-subnav">Registro de Visitante</div>
    <div class="divisoria"></div>
    <form action="c" method="POST">
        <input type="hidden" name="app" value="1265">
        <input type="hidden" name="acao" value="listar">
        
        <p class="legendaCodigo MargemSuperior0" >Crachá:</p>
        <input type="text" name="idCracha" maxlength="9" id="cracha" onkeypress="onlyNumber(event)" class="campoSemTamanho alturaPadrao larguraData">
        <br><br>
        <input type="button" onclick="validarForm()" class="botaoatualizar" value=" " />
    </form>
        
</body>
</html>
