    <%@taglib prefix="sql" uri="http://java.sun.com/jsp/jstl/sql"%>
    <%@include file="head.jsp"%>
    <c:choose>
        <c:when test="${menu eq 'menuCaixa.jsp'}">
            <%@include file="menuCaixa.jsp"%>
        </c:when>
        <c:otherwise>
            <%@include file="menu.jsp"%>
        </c:otherwise>
    </c:choose>

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
        });        

        function validarForm(destino){

            if(trim(document.forms[0].idCategoria.value) == ''){
                alert('Informe a categoria do sócio.');
                return;
            }

            if(trim(document.forms[0].matricula.value) == ''){
                alert('Informe a matrícula do sócio.');
                return;
            }

            if (destino=='tela'){
                $('#acao').val(destino);
                document.forms[0].submit();
            }else{
                $('#impressao').show();
            }
        }

        function cancelaImpressao(){
            $('#impressao').hide();
        }

        function atualizaImpressao(){
            $('#acao').val($('input[name=tipoImp]:checked').val());
            document.forms[0].submit();
        }

    </script>

    <body class="internas">

        <div class="divisoria"></div>
        <div id="titulo-subnav">Extrato de Sócio</div>
        <div class="divisoria"></div>

        <form method="POST" action="c">
            <input type="hidden" name="app" id="app" value="6120">
            <input type="hidden" name="acao" id="acao" value="visualizar">
            <input type="hidden" name="menu" id="menu" value="${menu}">

            <table class="fmt">
                <tr rowspan="2">
                    <td>
                        <img src="f?tb=6&mat=${s.matricula}&seq=0&cat=${s.idCategoria}" class="recuoPadrao MargemSuperiorPadrao" width="80" height="100">
                    </td>
                    <td>
                        <table class="fmt">
                            <tr>
                                <td>
                                    <p class="legendaCodigo MargemSuperior0" >Categoria:</p>
                                    <input type="text" name="idCategoria" onkeypress="onlyNumber(event)" maxlength="2" class="campoSemTamanho alturaPadrao larguraNumero" value="${s.idCategoria}">
                                </td>
                                <td>
                                    <p class="legendaCodigo MargemSuperior0" >Matrícula:</p>
                                    <input type="text" name="matricula" onkeypress="onlyNumber(event)" maxlength="5" class="campoSemTamanho alturaPadrao larguraNumero" value="${s.matricula}">
                                </td>
                                <td>
                                    <fieldset class="field-set legendaFrame recuoPadrao" style="width:260px;height: 45px;">
                                        <legend >Tipo:</legend>                
                                        <input type="radio" name="tipo" class="legendaCodigo" value="I" checked>INPC
                                        <input type="radio" name="tipo" class="legendaCodigo" value="C">Comissão de Permanência
                                    </fieldset>

                                </td>

                            </tr>
                            <tr>
                                <td colspan="3">
                                    <br>
                                    <p class="campoCargoEspecial MargemSuperior0" >${s.nome}</p>
                                </td>
                            </tr>

                        </table>

                    </td>
                </tr>
                <tr>
                </tr>
            </table>

            <input type="button" onclick="validarForm('tela')" class="botaoatualizar" value=" " />        
            <input type="button" onclick="validarForm('visualizar')" class="botaoImprimirPequeno" value=" " />       

            <br><br>

            <c:if test='${acao=="tela"}'>
                <sql:query var="rs" dataSource="jdbc/iate">
                    EXEC SP_EXTRATO_SOCIO ${s.matricula},${s.idCategoria}, 1
                </sql:query>  
            </c:if>             
            <table id="tabela" style="width:98%;margin-left:15px;">
                <thead>
                    <tr class="odd">
                        <th scope="col">Vencimento</th>
                        <th scope="col">Valor</th>
                        <th scope="col">Encargos</th>
                        <th scope="col">Total</th>
                    </tr>
                </thead>
                <tbody>
                    <c:set var="totalFinal" value="${0}" />
                    <c:forEach var="ext" items="${rs.rows}">
                        <tr>
                            <c:set var="totalFinal" value="${totalFinal + ext.total}" />
                            <fmt:formatNumber var="valor"  value="${ext.valor}" pattern="#0.00"/>
                            <fmt:formatNumber var="encargos"  value="${ext.encargos}" pattern="#0.00"/>
                            <fmt:formatNumber var="total"  value="${ext.total}" pattern="#0.00"/>

                            <td class="column1" align="center">${ext.vencimento}</td>
                            <td class="column1" align="right">${valor}</td>
                            <td class="column1" align="right">${encargos}</td>
                            <td class="column1" align="right">${total}</td>
                        </tr>
                    </c:forEach>
                        
                    <c:if test='${totalFinal gt 0}'>
                        <tr>
                            <fmt:formatNumber var="totalFinal2"  value="${totalFinal}" pattern="#0.00"/>

                            <td class="column1" align="center"></td>
                            <td class="column1" align="right"></td>
                            <td class="column1" align="right"><b>TOTAL</b></td>
                            <td class="column1" align="right"><b>${totalFinal2}</b></td>
                        </tr>
                    </c:if>
                    <c:if test='${totalFinal le 0 && acao == "tela"}'>
                        <tr>
                            <td class="column1" style="font-size: 16px" colspan="4" align="center"><br>*********************** NÃO CONSTAM DÉBITOS ***********************<br> &nbsp;</td>
                        </tr>
                    </c:if>
                </tbody>
            </table>

            <div id="impressao" style="overflow:auto;left: 25%;top: 20%;position: fixed;width: 400px; height:600px;">
                <table style="background:#fff">
                    <tr>
                      <td>
                        <div class="divisoria"></div>
                        <div id="titulo-subnav">Tipo de Impressão</div>
                        <div class="divisoria"></div>

                        <table class="fmt" align="left" >
                            <tr>
                                <td colspan="2">
                                    <p class="legendaCodigo MargemSuperior0" >Tipo</p>
                                    &nbsp;&nbsp;<input type="radio" name="tipoImp" class="legendaCodigo" style="margin-top:12px" value="matricial" checked >Matricial
                                    &nbsp;&nbsp;<input type="radio" name="tipoImp" class="legendaCodigo" style="margin-top:12px" value="visualizar" <c:if test='${periodo=="C"}'>checked</c:if>>Jato de Tinta
                                </td>
                            <tr>
                            </tr>
                                <td>
                                    <br>
                                    <input style="margin-left:15px;" type="button" id="cmdAtualizar" name="cmdAtualizar" class="botaocancelar" onclick="cancelaImpressao()" />
                                </td>
                                <td>
                                    <br>
                                    <input style="margin-left:15px;margin-top:05px;" type="button" id="cmdCancelar" name="cmdCancelar" class="botaoatualizar" onclick="atualizaImpressao()"  />
                                </td>
                            </tr>
                        </table>
                    </td>
                  </tr>
                </table>
            </div>
        </form>

    </body>
    </html>
