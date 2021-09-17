<%@include file="head.jsp"%>

    <body class="internas">
        
    <script type="text/javascript" language="javascript">
        $(document).ready(function () {
                $('#tabela tr:gt(0)').css('background', 'white');

                $('#tabela tr:gt(0)').hover(function() {
                        $(this).css('background','#f4f9fe');
                }, function() {
                        $(this).css('background','white');
                })
                
                $('#divIncluir').hide();
                
        });        
        
        function mostraDivIncluir(){
            $('#divIncluir').show();
        }
        
        function submeteInclusao(){
            
            window.location.href='c?app=2001&acao=showForm'+
                                 '&matricula='+$('#matricula').val()+
                                 '&seqDependente=0'+
                                 '&idCategoria='+$('#idCategoria').val()+
                                 '&tipo='+$("input[name='tipo']:checked").val();
        }
        
    </script>      
                
    <%@include file="menu.jsp"%>
    
    <div class="divisoria"></div>
    <div id="titulo-subnav">Embarcações</div>
    <div class="divisoria"></div>
    <br>

    <form action="c" id="it-bloco01">
        <input type="hidden" name="app" value="2000">
        <input type="hidden" name="acao" value="showForm">
        <input type="hidden" name="matricula" id="matricula" value="${socio.matricula}">
        <input type="hidden" name="idCategoria" id="idCategoria" value="${socio.idCategoria}">
        <input type="hidden" name="seqDependente" value="0">
        
        <c:if test='<%=request.isUserInRole("2001")%>'>
            <div class="botaoincluirgeral">
                <a href="#" onclick="mostraDivIncluir()"><img src="imagens/btn-incluir.png"  width="100" height="25" /></a><br>
            </div>
        </c:if>

        <div class="botaoincluirgeral" style="margin-left: 100px;margin-top: -17px;">
            <p class="legendaCodigo MargemSuperior0">Tipo</p>
            <select name="tipo" id="tipo" class="campoSemTamanho alturaPadrao" style="width:245px" onchange="submit()">
                <option value="T" <c:if test='${tipo=="T"}'>selected</c:if>>TODOS</option>
                <option value="N" <c:if test='${tipo=="N"}'>selected</c:if>>Barco</option>
                <option value="S" <c:if test='${tipo=="S"}'>selected</c:if>>Box</option>
            </select>
        </div>
    </form>

    <br>           

    <table id="tabela">
        <thead>
            <tr class="odd">
                <th scope="col">Barco/Box</th>
                <th scope="col">Cat. Náutica/Local</th>
                <th scope="col">Vaga</th>
                <th scope="col">Nº Box</th>
                <th scope="col">Nº Pés</th>
                <th scope="col">Alterar</th>
                <th scope="col">Excluir</th>
                <th scope="col">Pessoas Aut.</th>
                <th scope="col">Movim.</th>
                <th scope="col">Fotos</th>
                <th scope="col">Registro</th>
                <th scope="col">Baixa</th>
            </tr>
        </thead>

        <c:forEach var="barco" items="${barcos}">
            <tr>
                
                <td class="column1">${barco.nome}</td>
                
                <c:if test='${barco.ehBox=="N"}'>
                    <td class="column1" align="center">${barco.categoriaNautica.descricao}</td>
                    <td class="column1" align="center">${barco.tipoVaga.descricao}</td>
                </c:if>
                <c:if test='${barco.ehBox=="S"}'>
                    <td class="column1" align="center">${barco.localBox.descricao}</td>
                    <td class="column1" align="center"></td>
                </c:if>
                <td class="column1" align="center">${barco.box}</td>
                <td class="column1" align="center">${barco.pes}</td>

                <td align="center">    
                <c:if test='<%=request.isUserInRole("2002")%>'>
                    <a href="c?app=2002&acao=showForm&idBarco=${barco.id}"><img src="imagens/icones/inclusao-titular-03.png"/></a>
                </c:if>
                </td>
                <td align="center">
                    <c:if test='<%=request.isUserInRole("2003")%>'>
                        <a href="javascript: if(confirm('Confirma a exclusão do barco?')) window.location.href='c?app=2003&idBarco=${barco.id}&acao=excluir'"><img src="imagens/icones/inclusao-titular-05.png" /></a>
                    </c:if>
                </td>
                <td align="center">    
                    <c:if test='<%=request.isUserInRole("2002")%>'>
                        <c:if test='${barco.ehBox=="N"}'>
                            <a href="c?app=2002&acao=showListaPessoa&idBarco=${barco.id}"><img src="imagens/icones/inclusao-titular-01.png"/></a>
                        </c:if>
                    </c:if>
                </td>
                <td align="center">    
                    <c:if test='${barco.ehBox=="N"}'>
                        <a href="c?app=2000&acao=showListaMovimento&idBarco=${barco.id}"><img src="imagens/icones/inclusao-titular-16.png"/></a>
                    </c:if>
                </td>
                <td align="center">    
                    <c:if test='${barco.ehBox=="N"}'>
                        <a href="c?app=2000&acao=showFoto&idBarco=${barco.id}"><img src="imagens/icones/inclusao-titular-07.png"/></a>
                    </c:if>
                </td>
                <td align="center">    
                    <a href="c?app=2000&acao=imprimirRegistro&idBarco=${barco.id}"><img src="imagens/icones/inclusao-titular-13.png"/></a>
                </td>
                <td align="center">    
                    <a href="c?app=2000&acao=imprimirBaixa&idBarco=${barco.id}"><img src="imagens/icones/inclusao-titular-13.png"/></a>
                </td>                
            </tr>
        </c:forEach>
    </table>
    <input type="button" class="botaoVoltar"  onclick="window.location='c?app=9030&acao=consultar&matricula=${socio.matricula}&categoria=${socio.idCategoria}';" value=" " />

    <div id="divIncluir" style="left: 40%; top: 30%; position: fixed;">
        <table style="background:#fff">
            <tr>
              <td>
                <div class="divisoria"></div>
                <div id="titulo-subnav">Tipo</div>
                <div class="divisoria"></div>
                <table class="fmt" align="left" align="left">
                  <tr>
                    <td>
                        <input type="radio" id="tipo" name="tipo" checked value="BA"/>Barco<br>
                        <input type="radio" id="tipo" name="tipo" value="BO"/>Box<br>

                    </td>
                  </tr>
                  <tr>
                    <td>
                        <br>
                        <input type="button" id="cmdPesquisa" name="cmdPesquisa" class="botaoatualizar" value=" " onclick="submeteInclusao()" value="" />
                    </td>
                  </tr>
                </table>  
            </td>
          </tr>
        </table>
    </div>        
</body>
</html>
