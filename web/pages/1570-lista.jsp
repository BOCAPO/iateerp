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

    <%@include file="menu.jsp"%>

    <script type="text/javascript" language="javascript">
        $(document).ready(function () {
            $('#tabela tr:gt(0)').css('background', 'white');
            $('#tabelaVago tr:gt(0)').css('background', 'white');
            $('#tabelaReservado tr:gt(0)').css('background', 'white');
            $('#tabelaConfirmado tr:gt(0)').css('background', 'white');
            
            $('#tabelaVago tr:gt(0)').hover(function() {
                    $(this).css('background','#f4f9fe');
            }, function() {
                    $(this).css('background','white');
            })
            $('#tabelaReservado tr:gt(0)').hover(function() {
                    $(this).css('background','#f4f9fe');
            }, function() {
                    $(this).css('background','white');
            })
            $('#tabelaConfirmado tr:gt(0)').hover(function() {
                    $(this).css('background','#f4f9fe');
            }, function() {
                    $(this).css('background','white');
            })
            
            
            
            //$('#divMapa').hide();
            //$('#divTabela').show();
            $('#divMapa').show();
            $('#divTabela').hide();
        });  
        function mudaMapa(tipo){
            if (tipo=="M"){
                $('#divMapa').show();
                $('#divTabela').hide();
            }else{
                $('#divMapa').hide();
                $('#divTabela').show();
            }
        }
        
        function validaSelIncluir(sel){
            selecao = new Array();

            $("[name="+sel+"]").each(function(){
                if (this.checked == true) {
                    selecao.push($(this).val());
                }            

            });
            var tamanho = selecao.length;

            if (tamanho==0){
                alert('Nenhum Lugar foi selecionado!');
                return;
            }
            mesa = selecao[0].substring(0, 3);
            for ( var i = 0 ; i < tamanho ; i++ ) {
              if (selecao[i].substring(0, 3) != mesa){
                alert('Selecione somente lugares na mesma mesa!');
                return;
              }
            }                 
            
            $('#app').val(1571);
            $('#acao').val(sel);
            document.forms[0].submit();
        }
        
        function validaSelExcluir(sel){
            selecao = new Array();

            $("[name="+sel+"]").each(function(){
                if (this.checked == true) {
                    selecao.push($(this).val());
                }            

            });
            var tamanho = selecao.length;

            if (tamanho==0){
                alert('Nenhum Lugar foi selecionado!');
                return;
            }              
            
            $('#app').val(1573);
            $('#acao').val(sel);
            document.forms[0].submit();
        }

        function validaSelConfirmar(sel){
            selecao = new Array();

            $("[name="+sel+"]").each(function(){
                if (this.checked == true) {
                    selecao.push($(this).val());
                }            

            });
            var tamanho = selecao.length;

            if (tamanho==0){
                alert('Nenhum Lugar foi selecionado!');
                return;
            }              
            
            $('#app').val(1572);
            $('#acao').val('confirmar');
            document.forms[0].submit();
        }
        
        function validaSelImprimir(sel){
            selecao = new Array();

            $("[name="+sel+"]").each(function(){
                if (this.checked == true) {
                    selecao.push($(this).val());
                }            

            });
            var tamanho = selecao.length;

            if (tamanho==0){
                alert('Nenhum Lugar foi selecionado!');
                return;
            }              
            
            $('#app').val(1570);
            $('#acao').val('imprimir');
            document.forms[0].submit();
        }   
        function submeteAlteracao(lugar){
            $('#app').val(1572);
            $('#acao').val('showForm');
            $('#lugar').val(lugar);
            
            document.forms[0].submit();
        }
        
        
    </script>  


    <div class="divisoria"></div>
    <div id="titulo-subnav">Reserva de Lugares para Evento</div>
    <div class="divisoria"></div>

    
    <form action="c" id="it-bloco01">
        <input type="hidden" name="app" id="app" value="1570"/>
        <input type="hidden" name="acao" id="acao" value=""/>
        <input type="hidden" name="lugar" id="lugar" value=""/>
        
        <table class="fmt">
            <tr>
                <td>
                    <p class="legendaCodigo MargemSuperior0">Evento</p>
                    <div class="selectheightnovo2">
                        <select name="idEvento" id="idEvento" class="campoSemTamanho alturaPadrao" style="width:245px" onchange="submit()">
                        <c:forEach var="evento" items="${eventos}">
                            <option value="${evento.id}" <c:if test='${idEvento == evento.id}'>selected</c:if>>${evento.descricao}</option>
                        </c:forEach>
                        </select>
                    </div>
                </td>
                <td>
                    <fieldset class="field-set legendaFrame recuoPadrao" style="width:150px;height: 45px">
                        <legend >Visualização</legend>
                        &nbsp;<input type="radio" name="visualizacao" class="legendaCodigo" value="M" checked onclick="mudaMapa('M')">Mapa
                        <input type="radio" name="visualizacao" class="legendaCodigo" value="T"  onclick="mudaMapa('T')">Tabela
                    </fieldset>        
                </td>
            </tr>
        </table>

        <div class="divisoria"></div>

        <!--*************** M A P A **************************
            *************** M A P A **************************
            *************** M A P A **************************
            *************** M A P A **************************
            *************** M A P A **************************-->

        <div id="divMapa">
            <div style="left:15px;top: 235px;position: fixed;">
                <a href="#" onclick="validaSelIncluir('selMapa')"><img src="imagens/btn-incluir.png" width="100" height="25" /></a><br>
            </div>
            <div style="overflow:auto;width:700px;height:350px;left:0px;top: 260px;position: fixed;">
                <table id="tabela" style="width:auto;margin-left:15px;">
                    <thead>
                     <tr class="odd">
                        <th align="center">Mesa/Cadeira</th>
                        <c:forEach var="i" begin="1" end="${eventoSel.qtCadeiras}" step="1">
                            <th align="center" style="width:22px;">&#${64+i}</th>
                        </c:forEach>

                     </tr>
                    </thead>

                    <c:forEach var="i" begin="1" end="${eventoSel.qtMesas}" step="1">
                        <tr>
                            <td align="center" class="legendaCodigo" style="background:#f4f9fe; padding-top:0px; ">${i}</th>
                            <c:forEach var="j" begin="1" end="${eventoSel.qtCadeiras}" step="1">
                                <fmt:formatNumber var="mesa"  value="${i}" pattern="000"/>
                                <fmt:formatNumber var="cadeira"  value="${j}" pattern="00"/>

                                    <c:choose>
                                    <c:when test='${lugares[i-1][j-1] == "CO"}'>
                                        <td align="center" style="background-color: red;" onclick="submeteAlteracao('${mesa}${cadeira}')">
                                    </c:when>
                                    <c:when test='${lugares[i-1][j-1] == "RE"}'>
                                        <td align="center" style="background-color: green;" onclick="submeteAlteracao('${mesa}${cadeira}')">
                                    </c:when>
                                    <c:otherwise>
                                        <td align="center"><input type="checkbox" name="selMapa" value="${mesa}${cadeira}">
                                    </c:otherwise>
                                    </c:choose>
                                </td>
                            </c:forEach>
                        </tr>
                    </c:forEach>
                </table>
            </div>
        </div>

        <!--*************** T A B E L A **************************
            *************** T A B E L A **************************        
            *************** T A B E L A **************************        
            *************** T A B E L A **************************        
            *************** T A B E L A **************************        
        -->

        <div id="divTabela" >
            <div class="recuoPadrao" style="overflow:auto;height:350px;width:180px;left:0px;top: 260px;position: fixed;">
                <div class="recuoPadrao" style="left:140px;top: 235px;position: fixed;">
                    <a href="#" onclick="validaSelIncluir('selTabela')"><img src="imagens/icones/inclusao-titular-01.png" title="Incluir Reserva para os Lugares Selecionados"/></a>
                </div>
                <c:set var="conta" value="0"/>
                <table id="tabelaVago" style="margin-top:-20px;">
                    <thead>
                     <tr class="odd"><th>Lugares Vagos</th></tr>
                     <tr class="odd"><th>Lugares Vagos</th></tr>
                    </thead>
                    <c:forEach var="i" begin="1" end="${eventoSel.qtMesas}" step="1">
                        <c:forEach var="j" begin="1" end="${eventoSel.qtCadeiras}" step="1">
                            <c:if test='${lugares[i-1][j-1] == null}'>
                                <c:set var="conta" value="${conta+1}"/>
                                <fmt:formatNumber var="mesa"  value="${i}" pattern="000"/>
                                <fmt:formatNumber var="cadeira"  value="${j}" pattern="00"/>

                                <tr><td align="center">
                                    <input type="checkbox" name="selTabela" value="${mesa}${cadeira}"> 
                                    ${mesa} - &#${64+j}
                                </td></tr>
                            </c:if>

                        </c:forEach>
                    </c:forEach>
                </table>
            </div>
            <div class="recuoPadrao" style="overflow:auto;height:100px;width:180px;left:102px;top: 613px;position: fixed;">
                <input type="texto" readonly style="width:50px; text-align: right;" name="qtRegVagos" value="${conta}"> 
            </div>


            <div class="recuoPadrao" style="overflow:auto;height:350px;width:180px;left:220px;top: 260px;position: fixed;">
                <div class="recuoPadrao" style="left:330px;top: 235px;position: fixed;">
                    <a href="#" onclick="validaSelConfirmar('selReservado')"><img src="imagens/icones/inclusao-titular-17.png" title="Confirma a(s) Reserva(s) Selecionada(s)"/></a>
                </div>
                <div class="recuoPadrao" style="left:360px;top: 235px;position: fixed;">
                    <a href="#" onclick="validaSelExcluir('selReservado')"><img src="imagens/icones/inclusao-titular-05.png" title="Cancela a(s) Reserva(s) Selecionada(s)"/></a>
                </div>

                <c:set var="conta" value="0"/>
                <table id="tabelaReservado" style="margin-top:-20px;">
                    <thead>
                     <tr class="odd"><th>Reservados</th></tr>
                     <tr class="odd"><th>Reservados</th></tr>
                    </thead>
                    <c:forEach var="i" begin="1" end="${eventoSel.qtMesas}" step="1">
                        <c:forEach var="j" begin="1" end="${eventoSel.qtCadeiras}" step="1">
                            <c:if test='${lugares[i-1][j-1] == "RE"}'>
                                <c:set var="conta" value="${conta+1}"/>
                                <fmt:formatNumber var="mesa"  value="${i}" pattern="000"/>
                                <fmt:formatNumber var="cadeira"  value="${j}" pattern="00"/>
                                <tr><td align="center">
                                    <input type="checkbox" name="selReservado" value="${mesa}${cadeira}">
                                    ${mesa} - &#${64+j}
                                    &nbsp;&nbsp;&nbsp;<a href="#" onclick="submeteAlteracao('${mesa}${cadeira}')"><img src="imagens/icones/inclusao-titular-03.png" title="Altera a Reserva Selecionada"/></a>
                                </td></tr>
                            </c:if>

                        </c:forEach>
                    </c:forEach>
                </table>
            </div>  
            <div class="recuoPadrao" style="overflow:auto;height:100px;width:180px;left:322px;top: 613px;position: fixed;">
                <input type="texto" readonly style="width:50px; text-align: right;" name="qtRegVagos" value="${conta}"> 
            </div>


            <div class="recuoPadrao" style="overflow:auto;height:350px;width:180px;left:440px;top: 260px;position: fixed;">
                <div class="recuoPadrao" style="left:550px;top: 235px;position: fixed;">
                    <a href="#" onclick="validaSelImprimir('selConfirmado')"><img src="imagens/icones/inclusao-titular-13.png" title="Imprime a(s) Reserva(s) Selecionada(s)"/></a>
                </div>
                <div class="recuoPadrao" style="left:580px;top: 235px;position: fixed;">
                    <a href="#" onclick="validaSelExcluir('selConfirmado')"><img src="imagens/icones/inclusao-titular-05.png" title="Cancela a(s) Reserva(s) Selecionada(s)"/></a>
                </div>

                <c:set var="conta" value="0"/>
                <table id="tabelaConfirmado" style="margin-top:-20px;">
                    <thead>
                     <tr class="odd"><th>Confirmados</th></tr>
                     <tr class="odd"><th>Confirmados</th></tr>
                    </thead>
                    <c:forEach var="i" begin="1" end="${eventoSel.qtMesas}" step="1">
                        <c:forEach var="j" begin="1" end="${eventoSel.qtCadeiras}" step="1">
                            <c:if test='${lugares[i-1][j-1] == "CO"}'>
                                <c:set var="conta" value="${conta+1}"/>
                                <fmt:formatNumber var="mesa"  value="${i}" pattern="000"/>
                                <fmt:formatNumber var="cadeira"  value="${j}" pattern="00"/>
                                <tr><td align="center">
                                    <input type="checkbox" name="selConfirmado" value="${mesa}${cadeira}">
                                    ${mesa} - &#${64+j}
                                    &nbsp;&nbsp;&nbsp;<a href="#" onclick="submeteAlteracao('${mesa}${cadeira}')"><img src="imagens/icones/inclusao-titular-03.png" title="Altera a Reserva Selecionada"/></a>
                                </td></tr>
                            </c:if>

                        </c:forEach>
                    </c:forEach>

                </table>
             </div>          
            <div class="recuoPadrao" style="overflow:auto;height:100px;width:180px;left:542px;top: 613px;position: fixed;">
                <input type="texto" readonly style="width:50px; text-align: right;" name="qtRegVagos" value="${conta}"> 
            </div>
        </div>
    </form>
</body>
</html>
