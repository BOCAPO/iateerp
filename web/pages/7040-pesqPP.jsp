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
            alert('Nenhuma Permissão foi selecionada.');
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
    <div id="titulo-subnav">Seleção de Premissão Provisória</div>
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
                <th scope="col" class="nome-lista">Nome</th>
                <th scope="col" align="center">Início</th>
                <th scope="col" align="center">Fim</th>
                <th scope="col" class="nome-lista">Atividade</th>
                <th scope="col" class="nome-lista">Nr. Permissão</th>
            </tr>	
            </thead>
            <tbody>

            <c:forEach var="a" items="${permissoes}">

                <tr height="1">
                    <td class="column1" align="center">
                            <input type="radio" name="sel" value="${a.numero}" />
                    </td>
                    <td class="column1" align="left">${a.nome}</td>
                    <fmt:formatDate var="dataInicio" value="${a.dataInicio}" pattern="dd/MM/yyyy" />                
                    <td class="column1" align="center">${dataInicio}</td>
                    <fmt:formatDate var="dataFim" value="${a.dataFim}" pattern="dd/MM/yyyy" />                
                    <td class="column1" align="center">${dataFim}</td>
                    <td class="column1" align="center">${a.atividade}</td>
                    <td class="column1" align="left">${a.numero}</td>

                </tr>	

            </c:forEach>

            </tbody>
        </table>
    </form>

        
</body>
</html>
