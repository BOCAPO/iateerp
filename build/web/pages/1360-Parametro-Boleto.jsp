<%@include file="head.jsp"%>
<%@ taglib prefix="sql" uri="http://java.sun.com/jsp/jstl/sql" %>

<style type="text/css">
    table.fmt {border:none;width:0;padding:0px;margin-left:0em;margin-top:0em;}
    table.fmt tr td {border:none;background:none;padding:0px;margin:0em auto;}
</style>  

<body class="internas">
            
    <%@include file="menu.jsp"%>
    
    <div class="divisoria"></div>
    <div id="titulo-subnav">Impressão de Boleto Bancário</div>
    <div class="divisoria"></div>
    
    <script type="text/javascript" LANGUAGE="JavaScript" SRC="js/calendario.js"></script>
    <link rel="stylesheet" type="text/css" href="css/calendario.css"/>

    <script type="text/javascript" language="JavaScript" src="js/format.js"></script>
    
    <script type="text/javascript" language="JavaScript">
        $(document).ready(function () {
        
            $('#dtPagamento').mask('99/99/9999');
            $('#divSelBanco').hide();
            
            habilitaPagamento();
            
            $("input[name=banco]:radio").change(function() {
                habilitaPagamento();
            });            
            
        });   

        function validaDtPagamento(){
            if(document.forms[0].dtPagamento.value == ''){
                alert('Informe a Data de Pagamento!');
                return false;
            }
            
            if(!isDataValida(document.forms[0].dtPagamento.value)){
                return false;
            }
            
            return true;
            
        }

        function validarForm(){
            if (validaDtPagamento()){
                document.forms[0].submit();
            }
        }

        function habilitaPagamento(){
            if ($('input[name=banco]:checked').val() == 'ITAU112'){
                $("#dtPagamento").attr('disabled','disabled');
                $("#dtPagamento").val($("#dtVencimento").val());
            }else{
                $("#dtPagamento").removeAttr('disabled');
            }
        }
        
        function submeteImpressao(){
            semEncargos = "";
            semDebAnt = "";
            
            if (document.forms[0].semEncargos.checked){
                semEncargos = "S";
            }
            if (document.forms[0].semDebAnt.checked){
                semDebAnt = "S";
            }
            
            window.location.href='c?app=1360&acao=imprimeBoleto&idCarne='+
                                 document.forms[0].idCarne.value + '&dtPagamento=' +
                                 document.forms[0].dtPagamento.value + '&semEncargos=' +
                                 semEncargos + '&semDebAnt=' +
                                 semDebAnt + '&declaracaoQuitacao=' +
                                 document.forms[0].declaracaoQuitacao.checked + '&banco=' +
                                 $('input[name=banco]:checked').val()
                             
        }
    </script>

    <form action="c" method="POST">
        
        <input type="hidden" name="app" value="1360">
        <input type="hidden" name="acao" value="showFormImprimirBoleto">
        <input type="hidden" name="idCarne" value="${carne.id}">

        <table class="fmt">
            <tr>
                <td>
                    <p class="legendaCodigo MargemSuperior0" >Pessoa</p>
                    <input type="text" name="pessoa" class="campoSemTamanho alturaPadrao" disabled style="width:415px;" value="${carne.socio.nome}">
                </td>
                <td>
                    <p class="legendaCodigo MargemSuperior0">Dt. Max. Pagto.:</p>
                    <fmt:formatDate var="dataPag" value="${dataPagamento}" pattern="dd/MM/yyyy"/>
                    <input type="text" name="dtPagamento" id="dtPagamento" class="campoSemTamanho alturaPadrao larguraData" value="${dataPag}">
                </td>
            </tr>
        </table>
                
        <table class="fmt">
            <tr>
                <td>
                    <p class="legendaCodigo MargemSuperior0" >Dt. Vencimento</p>
                    <fmt:formatDate var="dataVencimento" value="${carne.dataVencimento}" pattern="dd/MM/yyyy"/>
                    <input type="text" id="dtVencimento" name="dtVencimento" class="campoSemTamanho alturaPadrao larguraData" disabled  value="${dataVencimento}">   
                </td>
                <td>

                    <p class="legendaCodigo MargemSuperior0" >Vr. Carne</p>
                    <fmt:formatNumber value="${carne.valor}" maxFractionDigits="2" minFractionDigits="2" var="valor"/>
                    <input type="text" class="campoSemTamanho alturaPadrao larguraNumero" disabled value="${valor}">    

                </td>
                <td>
                    <p class="legendaCodigo MargemSuperior0" >Vr. Encar.</p>
                    <fmt:formatNumber value="${carne.encargos}" maxFractionDigits="2" minFractionDigits="2" var="encargos"/>
                    <input type="text" class="campoSemTamanho alturaPadrao larguraNumero" disabled value="${encargos}">  
                </td>
                <td>

                    <p class="legendaCodigo MargemSuperior0" >Vr.Deb.Ant.</p>
                    <fmt:formatNumber value="${carne.debAnt}" maxFractionDigits="2" minFractionDigits="2" var="debAnt"/>
                    <input type="text" name="dataRegistro" class="campoSemTamanho alturaPadrao larguraNumero"  disabled value="${debAnt}">
                </td>
                <td>

                    <p class="legendaCodigo MargemSuperior0" >Vr. Total</p>
                    <fmt:formatNumber value="${carne.valorPago}" maxFractionDigits="2" minFractionDigits="2" var="valorPago"/>
                    <input type="text" class="campoSemTamanho alturaPadrao larguraNumero" disabled value="${valorPago}">   
                </td>
                
                <td style="width:500px;"> 
                    <p class="legendaCodigo MargemSuperior0" >&nbsp;</p>
                    <input type="checkbox" class="legendaCodigo MargemSuperior0" name="declaracaoQuitacao" value="S"><spam class="legendaSemMargem larguraData">Declar. Quitação</spam>
                </td>
                
            </tr>
        </table>

                
        <div class="divisoria"></div>
        <div id="titulo-subnav">Opções</div>
        <div class="divisoria"></div>
        <br>
        <table class="fmt" style="margin-left:15px;">
            <tr>
                <td style="width:500px;"> 
                    <input type="checkbox" name="semEncargos" value="S" <c:if test='${semEncargos eq "S"}'>checked</c:if>><spam class="legendaSemMargem larguraData">Não cobrar Encargos do mês atual</spam>
                </td>
            </tr>
            <tr>
                <td style="width:500px;"> 
                    <input type="checkbox" name="semDebAnt" value="S" <c:if test='${semDebAnt eq "S"}'>checked</c:if>><spam class="legendaSemMargem larguraData">Não incluir Débitos Anteriores</spam>
                </td>
            </tr>
        </table>
                
                
                
        <sql:query var="rs" dataSource="jdbc/iate">
            SELECT IATE.DBO.FC_DEFINE_CARTEIRA_BOLETO(${carne.id}) AS CARTEIRA_SUGERIDA
        </sql:query>

        <c:forEach var="row" items="${rs.rows}">
            <c:set var="cartSugerida" value="${row.CARTEIRA_SUGERIDA}"/>
        </c:forEach>
        
        <div class="divisoria"></div>
        <div id="titulo-subnav">Forma de Emissão do Boleto</div>
        <div class="divisoria"></div>
        
        <table class="fmt" style="margin-left:15px;">
            <c:if test='${cartSugerida eq 112}'>
                <tr>
                    <td>
                        <input type="radio" id="banco" name="banco" <c:if test='${banco eq null || banco eq "ITAU112"}'>checked</c:if> value="ITAU112"/>Itau (Carteira 112)<br>
                    </td>
                </tr>
            </c:if>            
            <tr>
                <td>
                    <input type="radio" id="banco" name="banco" <c:if test='${(cartSugerida eq 109 && banco eq null) || banco eq "ITAU109"}'>checked</c:if> value="ITAU109"/>Itau  (Carteira 109)<br>
                </td>
            </tr>
            

        </table>

        
        <table class="fmt">
            <tr>
                <td>
                    <input type="button" class="botaoatualizar"  onclick="validarForm()" value=" " />
                </td>
                <td>
                    <input type="button" class="botaoimprimirnovo"  onclick="submeteImpressao()" value=" " />
                </td>
                <td>
                    <input type="button" class="botaoVoltar"  onclick="window.location='c?app=1360&matricula=${carne.socio.matricula}&idCategoria=${carne.socio.idCategoria}&seqDependente=0';" value=" " />
                </td>
            </tr>
        </table>
        
    </form>
                
</body>
</html>
