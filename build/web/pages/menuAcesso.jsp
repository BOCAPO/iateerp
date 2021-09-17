
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>

<div id="blocoheader">

    <%@include file="topo.html"%>
    
    <div id="menu">
        <ul id="qm0" class="qmmc">

            <li><span class="qmdivider qmdividery" ></span></li>
            <!-- MENU CONTROLE -->
            <li><a class="qmparent" href="javascript:void(0)">CONTROLE</a>
                <ul>
                    <c:if test='<%=request.isUserInRole("7040")%>'>
                        <li><a href="c?app=7040">Entrada e Saída</a></li>
                    </c:if>
                </ul>
            </li>
            <!-- FIM DO MENU CONTROLE -->

            <li><span class="qmdivider qmdividery" ></span></li>
            <!-- MENU CADASTRO -->
            <li><a class="qmparent" href="javascript:void(0)">CADASTRO</a>
                <ul>
                    <c:if test='<%=request.isUserInRole("7010")%>'>
                        <li><a href="c?app=7010">Local de Acesso</a></li>
                    </c:if>
                    <c:if test='<%=request.isUserInRole("7020")%>'>
                        <li><a href="c?app=7020">Tipo Evento Acesso</a></li>
                    </c:if>
                    <c:if test='<%=request.isUserInRole("7030")%>'>
                        <li><a href="c?app=7030">Mensagens</a></li>
                    </c:if>
                </ul>
            </li>
            <!-- FIM DO MENU CADASTRO -->

            <li><span class="qmdivider qmdividery" ></span></li>
            <!-- MENU CONSULTA -->
            <li><a class="qmparent" href="javascript:void(0)">CONSULTA</a>
                <ul>
                    <c:if test='<%=request.isUserInRole("7060")%>'>
                        <li><a href="c?app=7060">Presença no Clube</a></li>
                    </c:if>
                </ul>
            </li>
            <!-- FIM DO MENU CONSULTA -->
            
            <li><span class="qmdivider qmdividery" ></span></li>
            <!-- MENU RELATÓRIO -->
            <li><a class="qmparent" href="javascript:void(0)">RELATÓRIO</a>
                <ul>
                    <c:if test='<%=request.isUserInRole("7080")%>'>
                        <li><a href="c?app=7080">Mensagens</a></li>
                    </c:if>
                </ul>
            </li>
            <!-- FIM DO MENU RELATÓRIO -->

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
            
