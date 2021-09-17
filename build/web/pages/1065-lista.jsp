
<%@include file="head.jsp"%>

<style type="text/css">
    table.fmt {border:none;width:0;padding:0px;margin-left:0em;margin-top:0em;}
    table.fmt tr td {border:none;background:none;padding:0px;margin:0em;}
</style>  
<style type="text/css">
    #cancelamento {
        margin-top: -50px;
        margin-left: -50px;
        left: 50%;
        top: 50%;
        position: fixed;
    }
</style>

<script type="text/javascript" language="javascript">
    $(document).ready(function () {
        $("#dtUtilizacao").mask("99/99/9999");
    });     
</script>

<script type="text/javascript" language="JavaScript">

    $(document).ready(function () {
            $('#tabela tr:gt(0)').css('background', 'white');

            $('#tabela tr:gt(0)').hover(function() {
                    $(this).css('background','#f4f9fe');
            }, function() {
                    $(this).css('background','white');
            })
            
            $("#cancelarReserva").live("click", cancelarReserva);
            $('#cancelamento').hide();
    });            
    
    function validarForm(){

        if($('#dtUtilizacao').val() == ''){
           alert("Informe a Data de Utilização!");
           return false;
        }

        if(!isDataValida($('#dtUtilizacao').val())){
            return false; 
        }

        document.forms[0].submit();

    }    
    
    function habilitaCancelamento(idRes){
        $('#idSeqCancelamento').val('');
        $('#idSeqCancelamento').val(idRes);
        $('#cancelamento').show();
    }

    function cancelarReserva(){ 
        if ($('#usuario').val() == ''){
            alert('Informe o Usuário!');
            return;
        }
        if ($('#senha').val() == ''){
            alert('Informe a Senha!');
            return;
        }
        if ($('#usuario').val() == $('#usuarioAtual').val()){
            alert('O Supervisor não pode ser o mesmo usuário logado no sistema!');
            return false;
        }
        
        var ret = "";
        $.ajax({url:'ReservaChurrasqueiraAjax', async:false, dataType:'text', type:'GET',
                            data:{
                            tipo:6,
                            usuario:$('#usuario').val(),
                            senha:$('#senha').val()
                            }
        }).success(function(retorno){
            ret = retorno;
        });
        
        if (ret!="OK"){
            alert(ret);
            return false
        }        
        
        var ret = "";
        $.ajax({url:'ReservaChurrasqueiraAjax', async:false, dataType:'text', type:'GET',
                            data:{
                            tipo:7,
                            idRes:$('#idSeqCancelamento').val(),
                            usuario:$('#usuarioAtual').val()
                            }
        }).success(function(retorno){
             var endereco = "";
             endereco = "c?app=1068&idSeqCancelamento=" + $('#idSeqCancelamento').val();
             window.location.href=endereco;
        });
        


    }

</script>  

<body class="internas">

    <script type="text/javascript" language="JavaScript" src="js/format.js"></script>
    
    <%@include file="menu.jsp"%>
        
    <div class="divisoria"></div>
    <div id="titulo-subnav">Mapa Churrasqueiras</div>
    <div class="divisoria"></div>
    
    
    <form action="c" method="POST" onsubmit="return validarForm()">
        <input type="hidden" name="app" value="1065">
        <input type="hidden" name="acao" value="consultar">
        <input type="hidden" id="idSeqCancelamento" name="idSeqCancelamento" value="">
        <input type="hidden" id="usuarioAtual" name="usuarioAtual" value="${usuarioAtual}">
        <table class="fmt" align="left" >
            <tr>
              <td>
                  <p class="legendaCodigo MargemSuperior0" >Dt. Utilização</p>
                  <input type="text" id="dtUtilizacao" name="dtUtilizacao" class="campoSemTamanho alturaPadrao larguraData" value="${dtUtil}">
              </td>
              <td >    
                  <input class="botaobuscainclusao" style="margin-top:20px" type="submit" onclick="validarForm()" value="" title="Consultar" />
              </td>
            </tr>
        </table>
        <br><br>
        <table class="fmt" style="width:98%;margin-left:15px;">
            <tr valign="top" >
                <td style="width: 550px">
                    <img src="imagens/LAYOUTCH_NOVO.JPG">
                </td>
                <td>
                    <table id="tabela" style="border:1px solid #dae1f0; margin-left: 0px">
                        <thead>
                            <tr class="odd">
                                <th scope="col" class="nome-lista">Churrasqueira</th>
                                <th scope="col" align="center">Situação</th>
                                <th scope="col" align="center">Cancelar</th>
                                <th scope="col" align="center">Comprovante</th>
                            </tr>
                        </thead>
                        <tbody style="border:1px solid #dae1f0;">
                            <c:forEach var="reserva" items="${reservas}">
                                <tr style="border:1px solid #dae1f0;">
                                    <td class="column1" style="border:1px solid #dae1f0;">${reserva.deChurrasqueira}</td>
                                    <td align="center" style="border:1px solid #dae1f0;">
                                        <c:choose>
                                            <c:when test='${reserva.status == "AC"}'>
                                                RESERVADA
                                            </c:when>
                                            <c:when test='${reserva.status == "CF"}'>
                                                <c:if test='${reserva.origem=="C"}'>
                                                    CONFIRMADA - Clube
                                                </c:if>
                                                <c:if test='${reserva.origem=="I"}'>
                                                    CONFIRMADA - Internet
                                                </c:if>
                                            </c:when>
                                            <c:otherwise>
                                                DISPONÍVEL
                                            </c:otherwise>
                                        </c:choose>
                                        
                                    </td>
                                    <td align="center" style="border:1px solid #dae1f0;"> 
                                        <c:if test='${reserva.status == "CF" && podeReservar == "S"}'>
                                            <a href="javascript:habilitaCancelamento(${reserva.numero});">
                                                <img src="imagens/icones/inclusao-titular-05.png"/>
                                            </a>
                                        </c:if>
                                    </td>
                                    <td align="center" style="border:1px solid #dae1f0;">
                                        <c:if test='${reserva.status == "CF" }'>
                                            <a href="javascript: window.location.href='c?app=1069&idReserva=${reserva.numero}'">
                                                <img src="imagens/icones/inclusao-titular-15.png"/>
                                            </a>
                                        </c:if>
                                    </td>
                                </tr>
                            </c:forEach>
                        </tbody>
                    </table>
                </td>
            </tr>
        </table>

        
        <div id="cancelamento" >
            <table style="background:#fff">
                <tr>
                  <td>
                    <div class="divisoria"></div>
                    <div id="titulo-subnav">Cancelamento</div>
                    <div class="divisoria"></div>
                    <table class="fmt" align="left" align="left">
                      <tr>
                        <td>
                            <p class="legendaCodigo">Usuário</p>
                            <input type="text" id="usuario"  name="usuario" maxlength="10" class="campoSemTamanho alturaPadrao larguraData" value=""/>
                        </td>
                        <td>
                            <p class="legendaCodigo">Senha</p>
                            <input type="password" id="senha" name="senha" maxlength="10" class="campoSemTamanho alturaPadrao larguraData" value=""/>
                        </td>
                      </tr>
                      <tr>
                        <td>
                            <br>
                            <input type="button" id="cancelarReserva" name="cancelarReserva" class="botaoatualizar" value=" " />
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
