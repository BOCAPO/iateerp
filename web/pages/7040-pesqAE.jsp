<%@include file="head.jsp"%>

<style type="text/css">
    table.fmt {border:none;width:0;padding:0px;margin-left:0em;margin-top:0em;}
    table.fmt tr td {border:none;background:none;padding:0px;margin:0em auto;}
</style>  

<script type="text/javascript" language="JavaScript">
    function validaSelecao(){
        selecao = new Array();
        
        $("input:radio").each(function(){
            if (this.checked == true) {
		selecao.push($(this).val());
            }            
            
        });
        var tamanho = selecao.length;
        
        if (tamanho==0){
            alert('Nenhuma Autorização foi selecionada.');
            return;
        }


        document.forms[0].submit();
    }
    
    $(document).ready(function () {
            $('#tabela tr:gt(0)').css('background', 'white');

            $('#tabela tr:gt(0)').hover(function() {
                    $(this).css('background','#f4f9fe');
            }, function() {
                    $(this).css('background','white');
            })
    });        
</script>  

<body class="internas">

    <%@include file="menuAcesso.jsp"%>
    <script type="text/javascript" language="JavaScript" src="js/format.js"></script>
    <div class="divisoria"></div>
    <div id="titulo-subnav">Seleção de Autorização de Embarque</div>
    <div class="divisoria"></div>

    <br>
    <form action="c">
        <input type="hidden" name="app" value="7040">
        <input type="hidden" name="acao" id="acao" value="acesso">
        <input type="hidden" name="origem" value="S">
        <input type="hidden" name="idLocal" value="${idLocal}">
        <input type="hidden" name="placa" value="${placa}">
        <input type="hidden" name="qtd" value="${qtd}">
        <input type="hidden" name="entradaSaida" value="${entradaSaida}">
        
        <table class="fmt" align="left" >
            <tr>
                <td > 
                    &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
                    <input type="button" id="selecionar" name="selecionar" style="margin-top:15px" class="botaoSelecionar" onclick="validaSelecao()"  value=" " />
                    <input type="button" id="voltar" name="voltar" style="margin-top:15px" class="botaoVoltar" onclick="window.location='c?app=7040&acao=showForm&idLocal=${idLocal}&placa=${placa}&qtd=${qtd}';" value=" " />
                </td>
            </tr>
        </table>

        <table id="tabela">
            <thead>
            <tr class="odd">
                <th scope="col" align="center">Sel</th>
                <th scope="col" class="nome-lista">Responsável</th>
                <th scope="col" class="nome-lista">Convidado</th>
                <th scope="col" class="nome-lista">Categoria</th>
                <th scope="col" align="center">Emissão</th>
                <th scope="col" align="center">Validade</th>
                <th scope="col" align="center">Num. Autorização</th>
                <th scope="col" align="center">Dt. Utilização</th>
                <th scope="col" class="nome-lista">Embarcação</th>
                <th scope="col" align="center">Capacidade</th>
            </tr>	
            </thead>
            <tbody>

            <c:forEach var="a" items="${autorizacoes}">

                <tr height="1">
                    <td class="column1" align="center">
                            <input type="radio" name="sel" value="${a.numAutorizacao}" />
                    </td>
                    <td class="column1" align="left">${a.responsavel.nome}</td>
                    <td class="column1" align="left">${a.convidado}</td>
                    <td class="column1" align="left">${a.responsavel.categoria}</td>
                    <fmt:formatDate var="dataEmissao" value="${a.dataEmissao}" pattern="dd/MM/yyyy" />                
                    <td class="column1" align="center">${dataEmissao}</td>
                    <fmt:formatDate var="dataValidade" value="${a.dataValidade}" pattern="dd/MM/yyyy" />                
                    <td class="column1" align="center">${dataValidade}</td>                
                    <td class="column1" align="center">${a.numAutorizacao}</td>
                    <fmt:formatDate var="dataUtilizacao" value="${a.dataUtilizacao}" pattern="dd/MM/yyyy" />                
                    <td class="column1" align="center">${dataUtilizacao}</td>                
                    <td class="column1" align="left">${a.embarcacao}</td>
                    <td class="column1" align="center">${a.capacidade}</td>

                </tr>	

            </c:forEach>

            </tbody>
        </table>
    </form>

        
</body>
</html>
