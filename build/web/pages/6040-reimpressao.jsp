<%@include file="head.jsp"%>

<style type="text/css">
    table.fmt {border:none;width:0;padding:0px;margin-left:0em;margin-top:0em;}
    table.fmt tr td {border:none;background:none;padding:0px;margin:0em auto;}
</style>  

<body class="internas">

    <%@include file="menuCaixa.jsp"%>

    <script type="text/javascript" language="javascript">
        $(document).ready(function () {
                $('#tabela tr:gt(0)').css('background', 'white');

                $('#tabela tr:gt(0)').hover(function() {
                        $(this).css('background','#f4f9fe');
                }, function() {
                        $(this).css('background','white');
                })
                
                $("#campoObservacao").hide();
        });  
        
        function observacoes() {
            $("#campoObservacao").show();
        }        
        function fechaObservacao() {
            $("#campoObservacao").hide();
        }        
        function cancelaObservacao() {
            $("textarea#observacao").val('');
            fechaObservacao();
        }        
        
        function Imprime(dtSitCaixa, cdCaixa, seqAutenticacao, seqAbertura){
            
            $('#dtSitCaixa').val(dtSitCaixa);
            $('#cdCaixa').val(cdCaixa);
            $('#seqAutenticacao').val(seqAutenticacao);
            $('#seqAbertura').val(seqAbertura);
            $('#observacao').val($('#obs').val());

            document.forms[0].submit();
            
        }
        
    </script>  


    <div class="divisoria"></div>
    <div id="titulo-subnav">Reimpressão de Recibo</div>
    <div class="divisoria"></div>


    <form action="c">
        <input type="hidden" name="app" value="6040"/>
        <input type="hidden" name="acao" value="imprime"/>
        <input type="hidden" name="seqAutenticacao" id="seqAutenticacao" value=""/>
        <input type="hidden" name="cdCaixa" id="cdCaixa" value=""/>
        <input type="hidden" name="seqAbertura" id="seqAbertura" value=""/>
        <input type="hidden" name="dtSitCaixa" id="dtSitCaixa" value=""/>
        <input type="hidden" name="observacao" id="observacao" value=""/>
        
        <div class="botaoincluirgeral">
            <a href="#" onclick="observacoes()"><img src="imagens/btn-observacoes.png" width="100" height="25" /></a><br>
        </div>
        <br>
        <table id="tabela">
           <thead>
            <tr class="odd">
                <th>Nome Pagante</th>
                <th>Sq. Autenticação</th>
                <th>Transação</th>
                <th>Valor</th>
                <th>Imprimir</th>
            </tr>
           </thead>
            <c:forEach var="item" items="${movimentos}">
                <tr  height="1">
                    <td class="column1" align="left">${item.pagante}</td>
                    <fmt:formatNumber var="seqAutenticacao"  value="${item.seqAutenticacao}" pattern="000"/>
                    <td class="column1" align="center">${seqAutenticacao}</td>
                    <fmt:formatNumber var="valor"  value="${item.valor}" pattern="#,##0.00"/>
                    <td class="column1" align="left">${item.deTransacao}</td>
                    <td class="column1" align="right">${valor}</td>
                    <td class="column1" align="center">
                        <fmt:formatDate var="dtSitCaixa" value="${item.dtSitCaixa}" pattern="dd/MM/yyyy HH:mm:ss"/>
                        <a  href="javascript:Imprime('${dtSitCaixa}', ${item.cdCaixa}, ${item.seqAutenticacao}, ${item.seqAbertura})"><img src="imagens/icones/inclusao-titular-13.png"/></a>
                    </td>

                </tr>
            </c:forEach>
        </table>
        
        <div id="campoObservacao" style="overflow:auto;left: 25%;top: 20%;position: fixed;width: 500px; height:400px;">
            <table style="background:#fff">
                <tr>
                  <td>
                    <div class="divisoria"></div>
                    <div id="titulo-subnav">Observações do Caixa</div>
                    <div class="divisoria"></div>

                    <table class="fmt" align="left" style="margin-left:0px;">
                        <tr>
                            <td colspan="2">
                               <textarea class="campoSemTamanho" id="obs" rows="10" cols="80" name="obs"></textarea><br>
                            </td>
                        </tr>
                        <tr>
                            <td>
                                <br>
                                <input style="margin-left:15px;" type="button" id="cmdatualizar" name="cmdPesquisa" class="botaoatualizar" onclick="fechaObservacao()" />
                            </td>
                            <td>
                                <br>
                                <input type="button" style="margin-top:-5px;" id="cmdcancelar" name="cmdPesquisa" class="botaocancelar" onclick="cancelaObservacao()" />
                            </td>
                        </tr>
                    </table>  
                    <br>
                </td>
              </tr>
            </table>
        </div>                
        
    </form>            
</body>
</html>

