<%@taglib prefix="sql" uri="http://java.sun.com/jsp/jstl/sql"%>
<%@include file="head.jsp"%>

<style type="text/css">
    table.fmt {border:none;width:0;padding:0px;margin-left:0em;margin-top:0em;}
    table.fmt tr td {border:none;background:none;padding:0px;margin:0em auto;}

</style>  

<body class="internas">

    <%@include file="menu.jsp"%>
    <script type="text/javascript" language="JavaScript" src="js/format.js"></script>
    <script type="text/javascript" language="JavaScript">
        $(document).ready(function () {
                $('#tabela tr:gt(0)').css('background', 'white');

                $('#tabela tr:gt(0)').hover(function() {
                        $(this).css('background','#f4f9fe');
                }, function() {
                        $(this).css('background','white');
                })
                
        });        
        function validarForm(){
            document.forms[0].submit();
        }
        function agendaDia(dia){
            $('#acao').val('det');
            $('#data').val(dia);
            document.forms[0].submit();
        }
        
    </script>
    
    <div class="divisoria"></div>
    <div id="titulo-subnav">Agenda da Academia</div>
    <div class="divisoria"></div>

    <form action="c">
        <input type="hidden" name="app" value="3230">
        <input type="hidden" name="acao" id="acao" value="">
        <input type="hidden" name="data" id="data" value="">
        
        <table class="fmt">
            <tr>
                <td>
                    <p class="legendaCodigo MargemSuperior0">Mês</p>
                    <div class="selectheightnovo">
                        <select name="mes" class="campoSemTamanho alturaPadrao">
                            <option value="1" <c:if test='${mes == "1"}'>selected</c:if>>Janeiro</option>
                            <option value="2" <c:if test='${mes == "2"}'>selected</c:if>>Fevereiro</option>
                            <option value="3" <c:if test='${mes == "3"}'>selected</c:if>>Março</option>
                            <option value="4" <c:if test='${mes == "4"}'>selected</c:if>>Abril</option>
                            <option value="5" <c:if test='${mes == "5"}'>selected</c:if>>Maio</option>
                            <option value="6" <c:if test='${mes == "6"}'>selected</c:if>>Junho</option>
                            <option value="7" <c:if test='${mes == "7"}'>selected</c:if>>Julho</option>
                            <option value="8" <c:if test='${mes == "8"}'>selected</c:if>>Agosto</option>
                            <option value="9" <c:if test='${mes == "9"}'>selected</c:if>>Setembro</option>
                            <option value="10" <c:if test='${mes == "10"}'>selected</c:if>>Outubro</option>
                            <option value="11" <c:if test='${mes == "11"}'>selected</c:if>>Novembro</option>
                            <option value="12" <c:if test='${mes == "12"}'>selected</c:if>>Dezembro</option>
                        </select>
                    </div>
                </td>
                <td>
                    <p class="legendaCodigo MargemSuperior0">Ano</p>
                    <div class="selectheightnovo">
                        <select name="ano" class="campoSemTamanho alturaPadrao">
                        <sql:query var="rsAno" dataSource="jdbc/iate">
                            {call SP_RECUPERA_ANO_ACADEMIA}
                        </sql:query>                    
                        <c:forEach var="rowAno" items="${rsAno.rows}">
                            <option value="${rowAno.NU_ANO}" <c:if test='${rowAno.IC_ATUAL == "S"}'>selected</c:if>>${rowAno.NU_ANO}</option>
                        </c:forEach>
                        </select>
                    </div>
                </td>
                <td>
                    <p class="legendaCodigo MargemSuperior0">Serviço</p>
                    <div class="selectheightnovo">
                        <select name="idServico" class="campoSemTamanho alturaPadrao larguraCidade">
                            <c:forEach var="servico" items="${servicos}">
                                <option value="${servico.id}" <c:if test='${idServico == servico.id}'>selected</c:if>>${servico.descricao}</option>
                            </c:forEach>
                        </select>
                    </div>
                </td>
                <td>
                    <p class="legendaCodigo MargemSuperior0">Profissional:</p>
                    <div class="selectheightnovo">
                        <select name="idFuncionario" class="campoSemTamanho alturaPadrao larguraCidade">
                            <option value="0">TODOS</option>
                            <c:forEach var="funcionario" items="${funcionarios}">
                                <option value="${funcionario.id}" <c:if test='${idFuncionario == funcionario.id}'>selected</c:if>>${funcionario.descricao}</option>
                            </c:forEach>
                        </select>
                    </div>
                </td>
                <td>
                    <p class="legendaCodigo MargemSuperior0">Turno:</p>
                    <div class="selectheightnovo">
                        <select name="turno" class="campoSemTamanho alturaPadrao ">
                            <option value="0" <c:if test='${turno == "0"}'>selected</c:if>>TODOS</option>
                            <option value="M" <c:if test='${turno == "M"}'>selected</c:if>>Manhã</option>
                            <option value="T" <c:if test='${turno == "T"}'>selected</c:if>>Tarde</option>
                            <option value="N" <c:if test='${turno == "N"}'>selected</c:if>>Noite</option>
                        </select>
                    </div>
                </td>
                <td>
                    <p class="legendaCodigo MargemSuperior0">Tipo de Vaga:</p>
                    <div class="selectheightnovo">
                        <select name="tipoVaga" class="campoSemTamanho alturaPadrao ">
                            <option value="T" <c:if test='${tipoVaga == "T"}'>selected</c:if>>TODAS</option>
                            <option value="C" <c:if test='${tipoVaga == "C"}'>selected</c:if>>Apenas Clube</option>
                            <option value="I" <c:if test='${tipoVaga == "I"}'>selected</c:if>>Apenas Internet</option>
                        </select>
                    </div>
                </td>
                <td>
                    <c:if test='<%=request.isUserInRole("3235")%>'>
                        <input type="checkbox" class="legendaCodigo MargemSuperior0" style="margin-top:20px" <c:if test="${ignorarAbertura}">checked</c:if> name="ignorarAbertura"  id="ignorarAbertura" value="true"/><span class="legendaSemMargem larguraData">Ignorar Abertura</span>
                    </c:if>
                </td>
                <td >    
                    <input class="botaobuscainclusao" style="margin-top:20px" type="button" onclick="validarForm()" value="" title="Consultar"  />
                </td>
            </tr>
        </table>
        <table class="fmt">
            <tr>
                <td >    
                    <img src="imagens/icones/ocupado_r.png" style="width:25px; height:25px; margin-left: 15px;"/> 
                </td>
                <td >    
                    <div class="legendaSemMargem" style="margin-top: 0px;"> &nbsp;- Agenda lotada</div> 
                </td>
                <td >    
                    &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; <img src="imagens/icones/livre_r.jpg" style="width:30px; height:25px; margin-top: 0px;"/> 
                </td>
                <td >    
                    <div class="legendaSemMargem" style="margin-top: 0px;"> &nbsp;- Vaga Disponível</div> 
                </td>
                <td >    
                    &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; <img src="imagens/icones/Sem_Agenda.png" style="width:23px; height:23px; margin-top: 0px;"/> 
                </td>
                <td >    
                    <div class="legendaSemMargem" style="margin-top: 0px;"> &nbsp;- Sem agenda</div> 
                </td>
                
            </tr>
        </table>
        
        <div class="divisoria"></div>
    </form>
        
    <table id="tabela" style="width:98%;margin-left:15px;">
        <thead>
        <tr class="odd">
            <th scope="col" align="center">Domingo</th>
            <th scope="col" align="center">Segunda-feira</th>
            <th scope="col" align="center">Terça-feira</th>
            <th scope="col" align="center">Quarta-feira</th>
            <th scope="col" align="center">Quinta-feira</th>
            <th scope="col" align="center">Sexta-feira</th>
            <th scope="col" align="center">Sábado</th>
        </tr>	
        </thead>
        <tbody>
            <sql:query var="rs" dataSource="jdbc/iate">
                SELECT DATEPART(DW, '${ano}-${mes}-1')-1 DIA
            </sql:query>                    
            <c:forEach var="row" items="${rs.rows}">
                <c:set var="dia" value="${row.DIA}"/>
            </c:forEach>
            <c:forEach var="i" begin="1" end="${dia}">
                <td class="column1" align="center" style="font-size: 18px !important; vertical-align: top !important; background-color: rgb(180,180,180) !important"></td>
            </c:forEach>
                
            
            <sql:query var="rs2" dataSource="jdbc/iate">
                {call SP_AGENDA_MES_ACADEMIA (${mes}, ${ano}, ${idServico}, ${idFuncionario}, '${turno}', '${tipoVaga}', 
                <c:choose>
                    <c:when test="${ignorarAbertura}">
                        'S'
                    </c:when>
                    <c:otherwise>
                        NULL
                    </c:otherwise>
                </c:choose>    
                )}
            </sql:query>                    
            <c:forEach var="row2" items="${rs2.rows}">
                <c:if test='${dia == 7}'>
                    <c:set var="dia" value="0"/>
                    </tr><tr>
                </c:if>                
                
                <td class="column1" align="center" style="font-size: 18px !important; vertical-align: top !important; height:93px !important;">
                    <fmt:formatNumber var="diaTemp" value='${row2.DIA}' pattern='00'/>
                    <fmt:formatNumber var="mesTemp" value='${mes}' pattern='00'/>
                    <c:set var="data" value="${diaTemp}/${mesTemp}/${ano}"/>
                    
                    <a onclick="agendaDia('${data}')" href="#">
                        <b>${data}</b>
                        <br>
                        
                        <c:if test='${row2.QT_VAGA gt 0 && row2.SALDO gt 0}'>
                            <img src="imagens/icones/livre_r.jpg" />
                        </c:if>
                        <c:if test='${row2.QT_VAGA gt 0 && row2.SALDO le 0}'>
                            <img src="imagens/icones/ocupado_r.png" />
                        </c:if>
                        
                    </a>
                </td>
                
                <c:set var="dia" value="${dia+1}"/>
            </c:forEach>
            <c:forEach var="i" begin="${dia}" end="6">
                <td class="column1" align="center" style="font-size: 18px !important; vertical-align: top !important; background-color: rgb(180,180,180) !important"></td>
            </c:forEach>
                
        </tbody>
    </table>
        
</body>
</html>
