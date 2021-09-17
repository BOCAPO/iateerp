<%@taglib prefix="sql" uri="http://java.sun.com/jsp/jstl/sql"%>
<%@include file="head.jsp"%>
<%@include file="menuCaixa.jsp"%>

<style type="text/css">
    table.fmt {border:none;width:0;padding:0px;margin-left:0em;margin-top:0em;}
    table.fmt tr td {border:none;background:none;padding:0px;margin:0em auto;}
    
    .campoCargoEspecial{ 
        font-family:Arial; 
        font-size:20px;  
        color: red;
        font-weight: bold;
        margin-left:15px;
    }      
</style>  

<script type="text/javascript" language="JavaScript" src="js/format.js"></script>

<script type="text/javascript" language="JavaScript">    
    $(document).ready(function () {
            $('#tabela tr:gt(0)').css('background', 'white');

            $('#tabela tr:gt(0)').hover(function() {
                    $(this).css('background','#f4f9fe');
            }, function() {
                    $(this).css('background','white');
            })
            
            $('#impressao').hide();
            $("#dtInicio").mask("99/99/9999");
            $("#dtFim").mask("99/99/9999");
            $('#pesqFuncionario').hide();

    });        
       
    function validarForm(){
        if(trim(document.forms[0].dtInicio.value) == ''){
            alert('Informe a data inicial.');
            return;
        }
        if(trim(document.forms[0].dtFim.value) == ''){
            alert('Informe a data final.');
            return;
        }
        
        if(trim(document.forms[0].nomeFuncionario.value) == ''){
            if(trim(document.forms[0].idCategoria.value) == ''){
                alert('Informe a categoria do sócio.');
                return;
            }

            if(trim(document.forms[0].matricula.value) == ''){
                alert('Informe a matrícula do sócio.');
                return;
            }
            
            $('#idFuncionario').val('');
            $('#acao').val('consultar');
            document.forms[0].submit();
        }else{
            $('#pesqFuncionario').show();
            carregaFuncionario();
        }
    }
    
    function imprime(){
        $('#acao').val('imprime');
        document.forms[0].submit();
    }
        

    
    function carregaFuncionario(){
        //pesquisar funcionario
        var tabela = $('#tabelaFuncionario').find('tbody').empty();

        $.ajax({url:'TaxaIndividualFuncionarioAjax', type:'GET',
                            data:{
                            nome:$('#nomeFuncionario').val(),
                            matricula:''
                            }
        }).success(function(retorno){
            if (retorno.erro !== undefined){
                alert(retorno.erro);
            }else {

                var linha="";
                var foto="";
                var campoHidden="";
                $.each(retorno.funcionario, function(i) {
                    foto='<img src="f?tb=5&cd='+this.codigo+'" class="recuoPadrao MargemSuperiorPadrao" width="80" height="100">';
                    campoHidden='<input type="hidden" name="idFuncionario" id="idFuncionario" value="'+this.codigo+'">';
                    
                    linha='<tr><td class="column1"><a href="#" id="tabNome" class="column1" onclick="selecionaFuncionario('+i+')">'+this.nome+'</a></td><td class="column1">'+this.cargo+campoHidden+'</td><td class="column1">'+this.setor+'</td><td>'+foto+'</td></tr>';
                    tabela.append(linha)
                });

            }
        });
    }
    
    function selecionaFuncionario(indice){
        $("#tabelaFuncionario tr:eq("+(parseInt(indice)+1)+")").each(function(index){
            $('#idFuncionario').val($(this).find('#idFuncionario').val());
        });
        
        $('#acao').val('consultar');
        document.forms[0].submit();
    }
    
    function cancelaPesquisaFuncionario(){
        $('#pesqFuncionario').hide();
    }
    
    

</script>

<body class="internas">

    <div class="divisoria"></div>
    <div id="titulo-subnav">Extrato Pré-pago</div>
    <div class="divisoria"></div>
    
    <form method="POST" action="c">
	<input type="hidden" name="app" id="app" value="6290">
	<input type="hidden" name="acao" id="acao" value="consultar">
        <input type="hidden" name="idFuncionario" id="idFuncionario" value="${idFuncionario}">
        <input type="hidden" name="tipopessoa" id="idFuncionario" value="${tipoPessoa}">
        
        <table class="fmt">
            <tr rowspan="2">
                <td>
                    <c:if test='${tipoPessoa=="S"}'>
                        <img src="f?tb=6&mat=${matricula}&seq=0&cat=${categoria}" class="recuoPadrao MargemSuperiorPadrao" width="110" height="140">
                    </c:if>             
                    <c:if test='${tipoPessoa=="F"}'>
                        <img src="f?tb=5&cd=${f.id}" class="recuoPadrao MargemSuperiorPadrao" width="110" height="140">
                    </c:if>             
                </td>
                <td>
                    <table class="fmt">
                        <tr>
                            <td>
                                <p class="legendaCodigo MargemSuperior0" >Categoria:</p>
                                <input type="text" name="idCategoria" onkeypress="onlyNumber(event)" maxlength="2" class="campoSemTamanho alturaPadrao larguraNumero" value="${categoria}">
                            </td>
                            <td>
                                <p class="legendaCodigo MargemSuperior0" >Matrícula:</p>
                                <input type="text" name="matricula" onkeypress="onlyNumber(event)" maxlength="5" class="campoSemTamanho alturaPadrao larguraNumero" value="${matricula}">
                            </td>
                            <td colspan="2">
                                <br>
                                <p class="campoCargoEspecial MargemSuperior0" >${s.nome}</p>
                            </td>
                        </tr>
                        <tr>
                            <td colspan="3">
                                <p class="legendaCodigo MargemSuperior0" >Nome Funcionário:</p>
                                <input type="text" name="nomeFuncionario" id="nomeFuncionario" class="campoSemTamanho alturaPadrao" style="width: 250px" value="${nome}">
                            </td>
                        </tr>
                        <tr>
                            <br>
                            <td>
                                <p class="legendaCodigo MargemSuperior0" >Dt. Inicial</p>
                                <input type="text" id="dtInicio" name="dtInicio" class="campoSemTamanho alturaPadrao " style="width: 100px" value="${dtInicio}">
                            </td>
                            <td>
                                <p class="legendaCodigo MargemSuperior0" >Dt. Final</p>
                                <input type="text" id="dtFim" name="dtFim" class="campoSemTamanho alturaPadrao " style="width: 100px" value="${dtFim}">
                            </td>
                            <td>
                                &nbsp;
                            </td>
                        </tr>
                        
                    </table>
                    
                </td>
            </tr>
        </table>
                
        <table class="fmt">
            <tr>
                <td>
                    <input type="button" onclick="validarForm()" class="botaoatualizar" value=" " />        
                </td>
                <td>
                    <c:if test='${acao=="consultar"}'>
                        <input type="button" style="margin-top: 0px" onclick="imprime()" class="botaoimprimir" value=" " />        
                    </c:if>
                </td>
            </tr>
        </table>
                            
        
        <br>
        
        <c:if test='${acao=="consultar"}'>
            <sql:query var="rs" dataSource="jdbc/iate">
                EXEC SP_EXTRATO_PRE_PAGO
                <c:if test='${tipoPessoa=="S"}'>
                    ${matricula},
                    ${categoria}, 
                    NULL,
                </c:if>             
                <c:if test='${tipoPessoa=="F"}'>
                    NULL,
                    NULL,
                    ${idFuncionario}, 
                </c:if>             
                '${fn:substring(dtInicio,3,5)}/${fn:substring(dtInicio,0,2)}/${fn:substring(dtInicio,6,10)} 00:00:01',
                '${fn:substring(dtFim,3,5)}/${fn:substring(dtFim,0,2)}/${fn:substring(dtFim,6,10)} 23:59:59'
            </sql:query>  
        </c:if>             
        <table id="tabela" style="width:98%;margin-left:15px;">
            <thead>
                <tr class="odd">
                    <th scope="col">Data</th>
                    <th scope="col">Nome</th>
                    <th scope="col">Descrição</th>
                    <th scope="col">Situação</th>
                    <th scope="col">Saldo Anterior</th>
                    <th scope="col">Vr. Lançamento</th>
                    <th scope="col">Saldo Atual</th>
                </tr>
            </thead>
            <tbody>
                <c:set var="totalFinal" value="${0}" />
                <c:forEach var="ext" items="${rs.rows}">
                    <tr>
                        <fmt:formatDate var="data" value="${ext['Data/Hora']}" pattern="dd/MM/yyyy HH:mm:ss"/>
                        <fmt:formatNumber var="valor"  value="${ext.Valor}" pattern="#0.00"/>
                        <fmt:formatNumber var="saldoAnterior"  value="${ext['Saldo Anterior']}" pattern="#0.00"/>
                        <fmt:formatNumber var="saldoAtual"  value="${ext['Saldo Atual']}" pattern="#0.00"/>
                        
                        <td class="column1" align="center">${data}</td>
                        <td class="column1" align="left">${ext.Nome}</td>
                        <td class="column1" align="left">${ext.Taxa}</td>
                        
                        <td class="column1" align="center">${ext.Situacao}</td>
                            
                        <td class="column1" align="right">${saldoAnterior}</td>
                        
                        <c:if test='${ext.Tipo=="Débito"}'>
                            <td class="column1" align="right" style="color:red;">${valor} (-)</td>
                        </c:if>             
                        <c:if test='${ext.Tipo=="Crédito"}'>
                            <td class="column1" align="right" style="color:blue;">${valor} (+)</td>
                        </c:if>             
                            
                        <td class="column1" align="right">${saldoAtual}</td>
                    </tr>
                </c:forEach>
                    
            </tbody>
        </table>

    </form>
                
    <!--
        ************************************************************************
        ************************************************************************
        ************************************************************************
                           TABELA DE SELECAO DE FUNCIONARIO
        ************************************************************************
        ************************************************************************
        ************************************************************************
     -->
    <div id="pesqFuncionario" style="overflow:auto;left: 25%;top: 20%;position: fixed;width: 700px; height:400px;">
        <table style="background:#fff">
            <tr>
              <td>
                <div class="divisoria"></div>
                <div id="titulo-subnav">Seleção de Funcionario</div>
                <div class="divisoria"></div>

            <table class="fmt" align="left" >
                <tr>
                    <td>
                        <p class="legendaCodigo MargemSuperior0" >Nome</p>
                        <input type="text" id="nomeFuncionario" name="nomeFuncionario" class="campoSemTamanho alturaPadrao" style="width:300px" value="">
                    </td>
                    <td >    
                        <input type="button" class="botaobuscainclusao" style="margin-top:20px" onclick="carregaFuncionario()" value="" title="Consultar" />
                    </td>
                </tr>
            </table>
            <br><br><br>
                <table id="tabelaFuncionario" align="left" style="margin-left:15px;">
                    <thead>
                    <tr class="odd">
                        <th scope="col" class="nome-lista">Nome</th>
                        <th scope="col" align="left">Cargo</th>
                        <th scope="col" align="left">Setor</th>
                        <th scope="col" align="left">Foto</th>
                    </tr>	
                    </thead>
                    <tbody>
                    </tbody>
                </table>  
                <br>
                <input style="margin-left:15px;" type="button" id="cmdcancelar" name="cmdPesquisa" class="botaocancelar" onclick="cancelaPesquisaFuncionario()" />
            </td>
          </tr>
        </table>
    </div>                

</body>
</html>

