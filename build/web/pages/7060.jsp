
<%@include file="head.jsp"%>

<style type="text/css">
    table.fmt {border:none;width:0;padding:0px;margin-left:0em;margin-top:0em;}
    table.fmt tr td {border:none;background:none;padding:0px;margin:0em auto;}
</style>  

<script type="text/javascript" language="JavaScript">
    $(document).ready(function () {
            $('#tabela tr:gt(0)').css('background', 'white');

            $('#tabela tr:gt(0)').hover(function() {
                    $(this).css('background','#f4f9fe');
            }, function() {
                    $(this).css('background','white');
            })
            
            $("#dtInicial").mask("99/99/9999");
            $("#dtFinal").mask("99/99/9999");
            
    });        
</script>  
<script type="text/javascript" language="JavaScript">
        function validarForm(){
            if($('#dtInicial').val() == ''){
               alert("Informe a data de início!");
               return false;
            }
            if(!isDataValida($('#dtInicial').val())){
                return false; 
            }
            
            if($('#dtFinal').val() == ''){
               alert("Informe a data de Fim!");
               return false;
            }
            if(!isDataValida($('#dtFinal').val())){
                return false; 
            }
            
            document.forms[0].submit();

        }
</script>  


<body class="internas">

    <script type="text/javascript" language="JavaScript" src="js/format.js"></script>
    
    <%@include file="menuAcesso.jsp"%>
        
    <div class="divisoria"></div>
    <div id="titulo-subnav">Histórico de Acesso</div>
    <div class="divisoria"></div>
    
    
    <form action="c" method="POST" onsubmit="return validarForm()">
        <input type="hidden" name="app" value="7060">
        <input type="hidden" name="acao" value="detalhe">
        <input type="hidden" name="doc" value="${doc}">

        <table class="fmt" align="left" >
            <tr>
                <td>
                    <p class="legendaCodigo MargemSuperior0" >Nome</p>
                    <input type="text" name="nome" class="campoSemTamanho alturaPadrao" style="width:300px" disabled value="${nome}">
                </td>
                <td>
                    <p class="legendaCodigo MargemSuperior0" >Locais</p>
                    <div class="selectheightnovo">
                        <select name="idLocal" class="campoSemTamanho">
                            <c:forEach var="local" items="${locais}">
                                <option value="${local.id}" <c:if test='${idLocal == local.id}'>selected</c:if>>${local.descricao}</option>
                            </c:forEach>
                        </select>
                    </div>        
                </td>
                <td>
                    <p class="legendaCodigo MargemSuperior0" >Dt. Inicial</p>
                    <input type="text" name="dtInicial" id="dtInicial" class="campoSemTamanho alturaPadrao larguraData" value="${dtInicial}">
                </td>
                <td>
                    <p class="legendaCodigo MargemSuperior0" >Dt. Final</p>
                    <input type="text" name="dtFinal" id="dtFinal" class="campoSemTamanho alturaPadrao larguraData" value="${dtFinal}">
                </td>
                <td >    
                    <input class="botaobuscainclusao" style="margin-top:20px" type="submit" onclick="return validarForm()" value="" title="Consultar" />
                </td>
            </tr>
        </table>
        <br><br>
        
        <table id="tabela" style="width:98%;margin-left:15px;">
            <thead>
                <tr class="odd">
                    <th scope="col">Data</th>
                    <th scope="col">Tipo</th>
                    <th scope="col">Status</th>
                    <th scope="col">Descrição</th>
                    <th scope="col">Local</th>
                </tr>	
            </thead>
            <tbody>
                <c:forEach var="a" items="${lista}">
                    <tr>
                        <fmt:formatDate var="data" value="${a.data}" pattern="dd/MM/yyyy HH:mm:ss" />
                        <td class="column1" align="center">${data}</td>
                        
                        <td class="column1" align="center">${a.tipo}</td>
                        <td class="column1" align="center">${a.status}</td>
                        <td class="column1" align="center">${a.descricao}</td>
                        <td class="column1" align="center">${a.local}</td>
                    </tr>
                </c:forEach>
            </tbody>
        </table>
    </form>
    
</body>
</html>
