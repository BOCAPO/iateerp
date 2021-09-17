<%@include file="head.jsp"%>

<style type="text/css">
    table.fmt {border:none;width:0;padding:0px;margin-left:0em;margin-top:0em;}
    table.fmt tr td {border:none;background:none;padding:0px;margin:0em auto;}
</style>  

<script type="text/javascript" language="JavaScript" src="js/format.js"></script>
<script type="text/javascript" language="JavaScript">    

    function adicionarSocio(){
        
        if(trim($('#categoria').val()) == '' || trim($('#matricula').val()) == ''){
            return;
        }
        
        $.ajax({url:'MovimentoSocioAjax', type:'GET', async:false,
                            data:{
                            nome:'',
                            categoria:$('#categoria').val(),
                            matricula:$('#matricula').val(),
                            carteira:''
                            }
        }).success(function(retorno){
            if (retorno.erro !== undefined){
                alert(retorno.erro);
            }else {
                $.each(retorno.jogador, function(i) {
                    var x = document.getElementById("convites");
                    var a = document.createElement("option");
                    a.value = this.categoria + '/' + this.matricula;
                    a.text = this.categoria + '/' + this.matricula + ' - ' + this.nome;
                    x.add(a);
                    $('#categoria').val('');
                    $('#matricula').val('');
                    $('#categoria').focus();
                });    
            }
        });
    }
    
    function removerSocio(){
        var x = document.getElementById('convites');
        x.remove(x.selectedIndex);
    }
    
    function validarForm(){
        
        if(document.forms[0].convites.length == 0){
            alert('Selecione pelo menos um titulo para gerar etiqueta');
            return;
        }
        
        for(var i = 0; i < document.forms[0].convites.length; i++){
            document.forms[0].convites[i].selected = true;
        }
        
        document.forms[0].submit();
    }

</script>

<body class="internas">

    <%@include file="menu.jsp"%>

    <div class="divisoria"></div>
    <div id="titulo-subnav">Emissão de Etiqueta Manual</div>
    <div class="divisoria"></div>
    
    <form method="POST" action="c">
	<input type="hidden" name="app" value="1990">
        <input type="hidden" name="acao" value="gerarEtiqueta">
        
        <table class="fmt">
            <tr>
                <td>
                    <table class="fmt">
                        <tr>
                            <td>
                                <table class="fmt">
                                    <tr>
                                        <td>
                                            <p class="legendaCodigo">Categoria:</p>
                                            <input type="text" id="categoria" name="categoria" onkeypress="onlyNumber(event)" maxlength="2" class="campoSemTamanho alturaPadrao larguraNumeroPequeno">
                                        </td>
                                        <td>
                                            <p class="legendaCodigo">Titulo:</p>
                                            <input type="text" id="matricula" name="matricula" onkeypress="onlyNumber(event)" maxlength="4" class="campoSemTamanho alturaPadrao larguraNumeroPequeno">
                                        </td>
                                        <td>
                                            <input type="button" class="botaobuscainclusao" style="margin-top: 35px;margin-left: 15px;" onclick="adicionarSocio()"
                                        </td>                                        
                                    </tr>
                                </table>
                            </td>
                        </tr>
                        <tr>
                            <td>
                                <table class="fmt">
                                    <tr>
                                        <td>
                                            <p class="legendaCodigo">Sócios</p>
                                            <div class="selectheightnovo">
                                                <select id="convites" name="convites" multiple size="40" class="campoSemTamanho alturaPadrao" style="height: 200px;width:250px;" >
                                                </select>
                                            </div>
                                        </td>
                                        <td valign="top">
                                            <input type="button" class="botaoExclui" style="margin-top: 35px;margin-left: 05px;" onclick="removerSocio()"
                                        </td>
                                    </tr>
                                </table>
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
