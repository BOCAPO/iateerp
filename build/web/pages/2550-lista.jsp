<%@taglib prefix="sql" uri="http://java.sun.com/jsp/jstl/sql"%>
<script type="text/javascript" language="JavaScript" src="js/format.js"></script>
<%@include file="head.jsp"%>
<style type="text/css">
    table.fmt {border:none;width:0;padding:0px;margin-left:0em;margin-top:0em;}
    table.fmt tr td {border:none;background:none;padding:0px;margin:0em auto;}
</style>  

<body class="internas">
            
        <%@include file="menu.jsp"%>
        
        <script type="text/javascript" language="javascript">
            
            $(document).ready(function () {
                    $('#tabela tr:gt(0)').css('background', 'white');
                    
                    $('#tabela tr:gt(0)').hover(function() {
                            $(this).css('background','#f4f9fe');
                    }, function() {
                            $(this).css('background','white');
                    })
                    
                    $("#dtReferencia").mask("99/99/9999");
                    
                    $("#divGeracao").hide();
            });       
            
            function mostraGeracao() {
                $("#divGeracao").show();
            }        
            function cancelaGeracao() {
                $("#divGeracao").hide();
            }        
            
            function atualizaGeracao() {
                
                if($("#dtReferencia").val()==''){
                    alert('Informe a data de referência!');
                    return
                }
                
                if(!isDataValida($("#dtReferencia").val())){
                    return;
                }
                
                if(!$("#chkGeraCarne").is(":checked") &&
                   !$("#chkGeraCancCarne").is(":checked") &&
                   !$("#chkGeraBaixaCarne").is(":checked") &&
                   !$("#chkGeraBolAvulso").is(":checked") &&
                   !$("#chkGeraBaixaBolAvulso").is(":checked") &&
                   !$("#chkGeraPessoas").is(":checked") &&
                   !$("#chkGeraCancPessoas").is(":checked") &&
                   !$("#chkGeraTaxa").is(":checked")){
               
                   alert('Selecione pelo menos 1 arquivo para ser gerado!');
                   return
                }
                
                $("#botoes").hide();
                
                //O AJAX SÓ EH CHAMADO DEPOIS DE 1 SEGUNDO PRA DAR TEMPO DA DIV QUE MOSTRA OS BOTOES FICAR ESCONDIDA
                
                setTimeout(function() {
                    $.ajax({url:'IntegracaoBennerAjax', async:false, type:'GET',
                        data:{
                            tipo:1,
                            dtReferencia:$("#dtReferencia").val(),
                            geraCarne:$("#chkGeraCarne").is(":checked"),
                            geraCancCarne:$("#chkGeraCancCarne").is(":checked"),
                            geraBaixaCarne:$("#chkGeraBaixaCarne").is(":checked"),
                            geraBolAvulso:$("#chkGeraBolAvulso").is(":checked"),
                            geraBaixaBolAvulso:$("#chkGeraBaixaBolAvulso").is(":checked"),
                            geraPessoas:$("#chkGeraPessoas").is(":checked"),
                            geraCancPessoas:$("#chkGeraCancPessoas").is(":checked"),
                            geraTaxa:$("#chkGeraTaxa").is(":checked")

                        }}).success(function(retorno){

                        alert('Arquivo(s) gerados(s) com sucesso!');
                        document.forms[0].submit();
                    });    
                }, 1000);
                
            }        
            
        </script>  

        <form method="POST">
            <input type="hidden" name="app" value="2550"/>
        </form>
        
        <div class="divisoria"></div>
        <div id="titulo-subnav">Log Integracao Benner</div>
        <div class="divisoria"></div>
        
        <br>
           
        <div id="botao" style='margin-left: 30px'>
            <input type="button" class="botaoGerar"  onclick="mostraGeracao()" value=" " />
        </div>        


        <table id="tabela">
            <thead>
            <tr class="odd">
                <th scope="col">Data de Geração</th>
                <th scope="col">Data de Referência</th>
                <th scope="col" >Arquivos</th>
            </tr>	
            </thead>
            <tbody>
                
            <sql:query var="rs" dataSource="jdbc/iate">
                SELECT DT_GERACAO, DT_REFERENCIA 
                FROM TB_INTEGRACAO_BENNER_LOG
                ORDER BY DT_GERACAO DESC
            </sql:query>                    
            <c:forEach var="row" items="${rs.rows}">
                
                <tr height="1">
                    <fmt:formatDate var="dtGeracao" value='${row.DT_GERACAO}' pattern='dd/MM/yyyy HH:mm:ss.SSS'/>
                    <th class="column1" align="center">${dtGeracao}</th>
                    
                    <fmt:formatDate var="dtReferencia" value='${row.DT_REFERENCIA}' pattern='dd/MM/yyyy'/>
                    <th class="column1" align="center">${dtReferencia}</th>
                    

                    <fmt:formatDate var="dtGeracaoSQL" value='${row.DT_GERACAO}' pattern='yyyy-MM-dd HH:mm:ss.SSS'/>
                    <sql:query var="rs2" dataSource="jdbc/iate">
                        SELECT DE_ARQUIVO, QT_REGISTRO, TAM_ARQUIVO, DT_ALTERACAO
                        FROM TB_INTEGRACAO_BENNER_DET_GERACAO
                        WHERE DT_GERACAO = '${dtGeracaoSQL}'
                        ORDER BY DE_ARQUIVO
                    </sql:query>                    
                        
                    <th class="column1" align="left">
                        <c:forEach var="row2" items="${rs2.rows}">
                            <fmt:formatDate var="dtAlteracao" value='${row2.DT_ALTERACAO}' pattern='dd/MM/yyyy HH:mm:ss'/>
                            <c:set var = "arquivo" value ="${row2.DE_ARQUIVO}" />
                            
                            <%
                                String arquivo2 = pageContext.getAttribute("arquivo").toString().replace("\\","/" );
                                pageContext.setAttribute("arquivo2", arquivo2);
                            %>
                            
                            
                            <a href="javascript: window.location.href='c?app=2550&acao=geraArquivo&dtGeracao=${dtGeracao}&arquivo=${arquivo2}'">
                                ${row2.DE_ARQUIVO}
                            </a>
                            
                            (Qt. Registros: ${row2.QT_REGISTRO} - Tamanho: ${row2.TAM_ARQUIVO} - Dt. Gravacao: ${dtAlteracao})
                            <br>
                        </c:forEach>
                    </th>
                    
                </tr>	

            </c:forEach>

            </tbody>
        </table>

        <div class="clear"></div>

        <br />
        <br />            

        <div id="rodape">
            <div id="copyright"><img src="imagens/copyright.png" /></div>
        </div>

    <!--
        ************************************************************************
        ************************************************************************
        ************************************************************************
                           OPCOES DE GERACAO
        ************************************************************************
        ************************************************************************
        ************************************************************************
    -->

    <div id="divGeracao" style="overflow:auto;left: 25%;top: 20%;position: fixed;width: 500px; height:400px;">
        <table style="background:#fff">
            <tr>
                <td>
                    <div class="divisoria"></div>
                    <div id="titulo-subnav">Arquivos a Gerar</div>
                    <div class="divisoria"></div>

                    <table class="fmt" align="left" >
                        <tr>
                            <td>
                                <p class="legendaCodigo">Dt. Referência</p>
                                <input type="text" id="dtReferencia" name="dtReferencia" style='width:92px;' value="" class="campoSemTamanho alturaPadrao">        
                            </td>
                        </tr>
                        <tr>
                            <td>
                                &nbsp;
                            </td>
                        </tr>
                        <tr>
                            <td>
                                <input type="checkbox" name="chkGeraCarne" id="chkGeraCarne"  style='margin-left: 15px' value="true">Geração de Receitas
                            </td>
                        </tr>
                        <tr>
                            <td>
                                <input type="checkbox" name="chkGeraCancCarne" id="chkGeraCancCarne" style='margin-left: 15px' value="true">Cancelamento de Receitas
                            </td>
                        </tr>
                        <tr>
                            <td>
                                <input type="checkbox" name="chkGeraBaixaCarne" id="chkGeraBaixaCarne" style='margin-left: 15px' value="true">Baixa de Receitas
                            </td>
                        </tr>
                        <tr>
                            <td>
                                <input type="checkbox" name="chkGeraBolAvulso" id="chkGeraBolAvulso" style='margin-left: 15px' value="true">Boletos Avulsos Gerados
                            </td>
                        </tr>
                        <tr>
                            <td>
                                <input type="checkbox" name="chkGeraBaixaBolAvulso" id="chkGeraBaixaBolAvulso" style='margin-left: 15px' value="true">Boletos Avulsos Baixados
                            </td>
                        </tr>
                        <tr>
                            <td>
                                <input type="checkbox" name="chkGeraPessoas" id="chkGeraPessoas" style='margin-left: 15px' value="true">Pessoas
                            </td>
                        </tr>
                        <tr>
                            <td>
                                <input type="checkbox" name="chkGeraCancPessoas" id="chkGeraCancPessoas" style='margin-left: 15px' value="true">Cancelamento de Pessoas
                            </td>
                        </tr>
                        <tr>
                            <td>
                                <input type="checkbox" name="chkGeraTaxa" id="chkGeraTaxa" style='margin-left: 15px' value="true">Taxas, Transações e Cursos
                            </td>
                        </tr>
                    </table>
                    <br><br><br><br><br><br><br><br><br>
                    <div id="botoes">
                        <table class="fmt" align="left" >
                            <tr>
                                <td>
                                    <input style="margin-top:5px;" type="button" id="cmdcancelar" name="cmdAtualizar" class="botaoatualizar" onclick="atualizaGeracao()" />
                                </td>
                                <td>
                                    <input style="margin-left:15px;" type="button" id="cmdcancelar" name="cmdPesquisa" class="botaocancelar" onclick="cancelaGeracao()" />
                                </td>
                            </tr>
                        </table>
                    </div>
                </td>
            </tr>
        </table>
    </div>                

        
    
</body>
</html>
