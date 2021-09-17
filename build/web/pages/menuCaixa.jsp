<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>

<div id="blocoheader">

    <%@include file="topo.html"%>

    <div id="menu">
        <ul id="qm0" class="qmmc">

            <li><span class="qmdivider qmdividery" ></span></li>

            <!-- MENU MOVIMENTACAO -->
            <li><a class="qmparent" href="javascript:void(0)">MOVIMENTA��O</a>
                <ul>
                    <c:if test='<%=request.isUserInRole("6040")%>'>
                        <li><a href="c?app=6040">Entrada Movimenta��o</a></li>
                    </c:if>

                    <c:if test='<%=request.isUserInRole("6160")%>'>
                        <li><a href="c?app=6160">Lan�amento de Produtos e Servi�os</a></li>
                    </c:if>

                    <c:if test='<%=request.isUserInRole("6180")%>'>
                        <li><a href="c?app=6180">Taxas Individuais</a></li>
                    </c:if>
                        
                    <li><span class="qmdivider qmdividerx" ></span></li>
                    
                    <c:if test='<%=request.isUserInRole("6070")%>'>
                        <li><a href="c?app=6070">Estorno Movimenta��o</a></li>
                    </c:if>
                    
                    <c:if test='<%=request.isUserInRole("6040")%>'>
                        <li><a href="c?app=6040&acao=showReimpressao">Reimpress�o de Recibo</a></li>
                    </c:if>
                        
                    <li><span class="qmdivider qmdividerx" ></span></li>    
                    
                    <c:if test='<%=request.isUserInRole("6020")%>'>
                        <li><a href="c?app=6020&acao=showAbrir">Abrir o Caixa</a></li>
                    </c:if>

                    <c:if test='<%=request.isUserInRole("6020")%>'>
                        <li><a href="c?app=6020&acao=showFechar">Fechar o Caixa</a></li>
                    </c:if>
                        
                </ul>
            </li>
            <!-- FIM DO MENU MOVIMENTACAO -->

            <li><span class="qmdivider qmdividery" ></span></li>
            
            <!-- MENU CADASTRO -->
            <li><a class="qmparent" href="javascript:void(0)">CADASTRO</a>
                <ul>
                    <c:if test='<%=request.isUserInRole("6010")%>'>
                        <li><a href="c?app=6010">Transa��es</a></li>
                    </c:if>
                    <c:if test='<%=request.isUserInRole("6340")%>'>
                        <li><a href="c?app=6340">Formas de Pagamento</a></li>
                    </c:if>
                        
                    <li><span class="qmdivider qmdividerx" ></span></li>   
                    
                    <c:if test='<%=request.isUserInRole("6140")%>'>
                        <li><a href="c?app=6140">Produtos e Servi�os</a></li>
                    </c:if>

                    <c:if test='<%=request.isUserInRole("6150")%>'>
                        <li><a href="c?app=6150">Centro de Custos</a></li>
                    </c:if>
                        
                    <li><span class="qmdivider qmdividerx" ></span></li>       

                    <c:if test='<%=request.isUserInRole("6200")%>'>
                        <li><a href="c?app=6200">Atualiza Remessa</a></li>
                    </c:if>
                        
                    <li><span class="qmdivider qmdividerx" ></span></li>       

                    <c:if test='<%=request.isUserInRole("6330")%>'>
                        <li><a href="c?app=6330">Combust�vel</a></li>
                    </c:if>
                </ul>
            </li>
            <!-- FIM DO MENU CADASTRO -->

                <li><span class="qmdivider qmdividery" ></span></li>

                <!-- MENU RELAT�RIOS -->
                <li><a class="qmparent" href="javascript:void(0);">RELAT�RIOS</a>
                    <ul style="width:150px;">
                        <c:if test='<%=request.isUserInRole("6050")%>'>
                            <li><a href="c?app=6050">Recebimento de Cheques</a></li>
                        </c:if>

                        <c:if test='<%=request.isUserInRole("6060")%>'>
                            <li><a href="c?app=6060">Movimenta��o de Caixa</a></li>
                        </c:if>

                        <c:if test='<%=request.isUserInRole("6080")%>'>
                            <li><a href="c?app=6080">Cont�bil</a></li>
                        </c:if>

                        <c:if test='<%=request.isUserInRole("6170")%>'>
                            <li><a href="c?app=6170">Produtos e Servi�os</a></li>
                        </c:if>
                            
                        <li><a class="qmparent" href="javascript:void(0);">+ Taxas Individuais</a>
                            <ul>
                                <li><a class="qmparent" href="javascript:void(0);">+ S�cio</a>
                                    <ul>
                                        <c:if test='<%=request.isUserInRole("6190")%>'>
                                            <li><a href="c?app=6190">Anal�tico</a></li>
                                        </c:if>

                                        <c:if test='<%=request.isUserInRole("6210")%>'>
                                            <li><a href="c?app=6210">Sint�tico</a></li>
                                        </c:if>

                                        <li><span class="qmdivider qmdividerx" ></span></li>

                                        <c:if test='<%=request.isUserInRole("6220")%>'>
                                            <li><a href="c?app=6220">Por situa��o de Carn�</a></li>
                                        </c:if>
                                    </ul>
                                </li>
                                <li><a class="qmparent" href="javascript:void(0);">+ Funcion�rio</a>
                                    <ul>
                                        <c:if test='<%=request.isUserInRole("6300")%>'>
                                            <li><a href="c?app=6300">Anal�tico</a></li>
                                        </c:if>

                                        <c:if test='<%=request.isUserInRole("6310")%>'>
                                            <li><a href="c?app=6310">Sint�tico</a></li>
                                        </c:if>
                                    </ul>
                                </li>
                            </ul>
                        </li>
                            

                        <c:if test='<%=request.isUserInRole("6110")%>'>
                            <li><a href="c?app=6110">Recebimentos em Atraso</a></li>
                        </c:if>

                        <c:if test='<%=request.isUserInRole("6120")%>'>
                            <li><a href="c?app=6120&menu=menuCaixa.jsp">Extrato S�cio</a></li>
                        </c:if>
                        <c:if test='<%=request.isUserInRole("6280")%>'>
                            <li><a href="c?app=6280">Saldo Comprometido</a></li>
                        </c:if>
                        <c:if test='<%=request.isUserInRole("6290")%>'>
                            <li><a href="c?app=6290">Extrato Pr�-pago</a></li>
                        </c:if>
                        <c:if test='<%=request.isUserInRole("6320")%>'>
                            <li><a href="c?app=6320">Saldo Geral Pr�-pago</a></li>
                        </c:if>

                    </ul>
                </li>
                <!-- FIM DO MENU RELAT�RIOS -->

                <li><span class="qmdivider qmdividery" ></span></li>

                <li class="qmclear">&nbsp;</li>
        </ul>

        <!-- Create Menu Settings: (Menu ID, Is Vertical, Show Timer, Hide Timer, On Click ('all' or 'lev2'), Right to Left, Horizontal Subs, Flush Left, Flush Top) -->
        <script type="text/javascript">qm_create(0,false,0,250,false,false,false,false,false);</script>

    </div>

</div>

<div id="rodape">
    <div id="copyright"><img src="imagens/copyright.png" /></div>
</div>
            
