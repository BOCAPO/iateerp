<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>


<div id="blocoheader">

    <%@include file="topo.html"%>
    
    <div id="menu">
        <ul id="qm0" class="qmmc">

            <li><span class="qmdivider qmdividery" ></span></li>

            <!-- MENU CADASTRO -->
            <li><a class="qmparent" href="javascript:void(0)">CADASTRO</a>
                <ul>
                    <!-- habilita com 9030 mas chama o 3110? -->
                    <c:if test='<%=request.isUserInRole("9030")%>'>
                        <li><a href="c?app=9030">Sócios e Dependentes</a></li>
                    </c:if>

                    <c:if test='<%=request.isUserInRole("1091")%>'>
                        <li><a href="c?app=1091">Transferência de Títulos</a></li>
                    </c:if>

                    <c:if test='<%=request.isUserInRole("1200")%>'>
                        <li><a href="c?app=1200">Cartas de Cobrança</a></li>
                    </c:if>

                    <li><a href="c?app=2250">Declaração de Quitação</a></li>

                    <li><a class="qmparent" href="javascript:void(0);">+ Embarcações</a>
                        <ul>
                            <li><a href="c?app=2005">Particulares</a></li>

                            <li><a href="c?app=1670">Iate</a></li>
                        </ul>
                    </li>
                </ul>
            </li>
            <!-- FIM DO MENU CADASTRO -->

            <li><span class="qmdivider qmdividery" ></span></li>

            <!-- MENU TABELAS -->
            <li><a class="qmparent" href="javascript:void(0)">TABELAS</a>
                <ul>
                    <c:if test='<%=request.isUserInRole("1080")%>'>
                        <li><a href="c?app=1080">Dependências</a></li>
                    </c:if>

                    <c:if test='<%=request.isUserInRole("1390")%>'>
                        <li><a href="c?app=1390">Ítens de Aluguel</a></li>
                    </c:if>

                    <c:if test='<%=request.isUserInRole("1150")%>'>
                        <li><a href="c?app=1150">Materiais</a></li>
                    </c:if>

                    <c:if test='<%=request.isUserInRole("1130")%>'>
                        <li><a href="c?app=1130">Categorias Náuticas</a></li>
                    </c:if>
                    <c:if test='<%=request.isUserInRole("2390")%>'>
                        <li><a href="c?app=2390">Local Box Náutica</a></li>
                    </c:if>

                    <c:if test='<%=request.isUserInRole("1760")%>'>
                        <li><a href="c?app=1760">Tipo de Vaga de Embarcações</a></li>
                    </c:if>

                    <c:if test='<%=request.isUserInRole("1210")%>'>
                        <li><a href="c?app=1210">Convênio</a></li>
                    </c:if>

                    <c:if test='<%=request.isUserInRole("2140")%>'>
                        <li><a href="c?app=2140">Lista Negra</a></li>
                    </c:if>

                    <li><span class="qmdivider qmdividerx" ></span></li>
                    
                    <li><a class="qmparent" href="javascript:void(0);">+ Funcionários</a>
                        <ul>
                            <c:if test='<%=request.isUserInRole("1050")%>'>
                                <li><a href="c?app=1050">Cadastro</a></li>
                            </c:if>

                            <c:if test='<%=request.isUserInRole("1030")%>'>
                                <li><a href="c?app=1030">Setores</a></li>
                            </c:if>

                            <c:if test='<%=request.isUserInRole("1040")%>'>
                                <li><a href="c?app=1040">Cargos</a></li>
                            </c:if>

                        </ul>
                    </li>

                    <li><span class="qmdivider qmdividerx" ></span></li>

                    <li><a class="qmparent" href="javascript:void(0);">+ Carros</a>
                        <ul>
                            <c:if test='<%=request.isUserInRole("2210")%>'>
                                <li><a href="c?app=2210">Cadastro</a></li>
                            </c:if>

                            <li><span class="qmdivider qmdividerx" ></span></li>

                            <c:if test='<%=request.isUserInRole("2160")%>'>
                                <li><a href="c?app=2160">Marca</a></li>
                            </c:if>

                            <c:if test='<%=request.isUserInRole("2180")%>'>
                                <li><a href="c?app=2180">Modelo</a></li>
                            </c:if>

                            <c:if test='<%=request.isUserInRole("2170")%>'>
                                <li><a href="c?app=2170">Cor</a></li>
                            </c:if>

                        </ul>
                    </li>

                    <li><span class="qmdivider qmdividerx" ></span></li>

                    <c:if test='<%=request.isUserInRole("1010")%>'>
                        <li><a href="c?app=1010">Categorias de Sócios</a></li>
                    </c:if>

                    <c:if test='<%=request.isUserInRole("1020")%>'>
                        <li><a href="c?app=1020">Tipos de Dependentes</a></li>
                    </c:if>

                    <c:if test='<%=request.isUserInRole("1100")%>'>
                        <li><a href="c?app=1100">Modalidades Esportivas</a></li>
                    </c:if>

                    <c:if test='<%=request.isUserInRole("1190")%>'>
                        <li><a href="c?app=1190">Cargos Especiais</a></li>
                    </c:if>

                    <c:if test='<%=request.isUserInRole("1110")%>'>
                        <li><a href="c?app=1110">Profissões</a></li>
                    </c:if>

                    <c:if test='<%=request.isUserInRole("2010")%>'>
                        <li><a href="c?app=2010">Tipos de Ocorrência</a></li>
                    </c:if>

                    <c:if test='<%=request.isUserInRole("1260")%>'>
                        <li><a href="c?app=1260">Crachá de Visitante</a></li>
                    </c:if>

                    <c:if test='<%=request.isUserInRole("2380")%>'>
                        <li><a href="c?app=2380">Tipo de Armário</a></li>
                    </c:if>
                    <c:if test='<%=request.isUserInRole("1320")%>'>
                        <li><a href="c?app=1320">Armário</a></li>
                    </c:if>

                    <c:if test='<%=request.isUserInRole("1950")%>'>
                        <li><a href="c?app=1950">Feriado</a></li>
                    </c:if>

                </ul>
            </li>
            <!-- FIM DO MENU TABELAS -->

            <li><span class="qmdivider qmdividery" ></span></li>

            <!-- MENU OPERAÇÕES -->
            <li><a class="qmparent" href="javascript:void(0)">OPERAÇÕES</a>
                <ul>
                    <c:if test='<%=request.isUserInRole("1070")%>'>
                        <li><a href="c?app=1070">Retirada de Convites</a></li>
                    </c:if>
                    <c:if test='<%=request.isUserInRole("1230")%>'>
                        <li><a href="c?app=1230">Permissão Provisória</a></li>
                    </c:if>

                    <c:if test='<%=request.isUserInRole("1265")%>'>
                        <li><a href="c?app=1265">Registro de Visitantes</a></li>
                    </c:if>

                    <c:if test='<%=request.isUserInRole("1550")%>'>
                        <li><a href="c?app=1550">Autorização de Embarque</a></li>
                    </c:if>

                    <li><span class="qmdivider qmdividerx" ></span></li>

                    <li><a class="qmparent" href="javascript:void(0);">+ Dependências</a>
                    <ul>
                        <li><a class="qmparent" href="javascript:void(0);">+ Geral</a>
                            <ul>
                                <c:if test='<%=request.isUserInRole("1060")%>'>
                                    <li><a href="c?app=1060">Reserva</a></li>
                                </c:if>

                                <c:if test='<%=request.isUserInRole("1450")%>'>
                                    <li><a href="c?app=1450">Tipos de Eventos</a></li>
                                </c:if>

                                <c:if test='<%=request.isUserInRole("1530")%>'>
                                    <li><a href="c?app=1530">Consultar Reserva</a></li>
                                </c:if>

                                <c:if test='<%=request.isUserInRole("1063")%>'>
                                    <li><a href="c?app=1063">Consultar Cancelamento</a></li>
                                </c:if>

                                <c:if test='<%=request.isUserInRole("1460")%>'>
                                    <li><a href="c?app=1460">Agenda Geral de Eventos</a></li>
                                </c:if>
                            </ul>
                        </li>
                        
                        <li><a class="qmparent" href="javascript:void(0);">+ Churrasqueira</a>
                            <ul>
                                <c:if test='<%=request.isUserInRole("1066")%>'>
                                    <li><a href="c?app=1066">Incluir Reservas</a></li>
                                </c:if>
                                <c:if test='<%=request.isUserInRole("1065")%>'>
                                    <li><a href="c?app=1065">Manter Reservas</a></li>
                                </c:if>

                                <c:if test='<%=request.isUserInRole("1069")%>'>
                                    <li><a href="c?app=1530&origem=CH">Consulta</a></li>
                                </c:if>
                                <c:if test='<%=request.isUserInRole("2590")%>'>
                                    <li><a href="c?app=2590&origem=CH">Pessoas Autorizadas</a></li>
                                </c:if>
                                <c:if test='<%=request.isUserInRole("2620")%>'>
                                    <li><a href="c?app=2620&origem=CH">Termo de Vistoria</a></li>
                                </c:if>
                            </ul>
                        </li>
                        
                        <li><a class="qmparent" href="javascript:void(0);">+ Tênis</a>
                            <ul>
                                <li><a class="qmparent" href="javascript:void(0);">+ Painel de Jogos</a>
                                    <ul>
                                        <li><a href="tenis" target="_blank">Telão</a></li>
                                        <li><a href="tenis" target="_blank">Totem</a></li>
                                        <c:if test='<%=request.isUserInRole("2370")%>'>
                                            <li><a href="c?app=2370" target="_blank">Funcionário</a></li>
                                        </c:if>                                
                                    </ul>
                                </li>

                                <c:if test='<%=request.isUserInRole("2340")%>'>
                                    <li><a href="c?app=2340">Cadastro de Quadras de Tênis</a></li>
                                </c:if>

                                <c:if test='<%=request.isUserInRole("2350")%>'>
                                    <li><a href="c?app=2350">Exceções da Agenda Semanal</a></li>
                                </c:if>

                                <c:if test='<%=request.isUserInRole("2360")%>'>
                                    <li><a href="c?app=2360">Relatório</a></li>
                                </c:if>
                            </ul>
                        </li>
                    </ul>
                    </li>

                    <li><a class="qmparent" href="javascript:void(0);">+ Movimento de Materiais</a>
                        <ul>
                            <c:if test='<%=request.isUserInRole("1140")%>'>
                                <li><a href="c?app=1140">Empréstimo/Devolução</a></li>
                            </c:if>

                            <c:if test='<%=request.isUserInRole("1890")%>'>
                                <li><a href="c?app=1890">Consulta/Relatório</a></li>
                            </c:if>
                        </ul>
                    </li>

                    <li><span class="qmdivider qmdividerx" ></span></li>

                    <li><a class="qmparent" href="javascript:void(0);">+ Eventos</a>
                        <ul>
                            <li><a class="qmparent" href="javascript:void(0);">+ Gerais</a>
                                <ul>
                                    <c:if test='<%=request.isUserInRole("1560")%>'>
                                        <li><a href="c?app=1560">Eventos</a></li>
                                    </c:if>

                                    <c:if test='<%=request.isUserInRole("1570")%>'>
                                        <li><a href="c?app=1570">Reserva de Lugares</a></li>
                                    </c:if>

                                    <c:if test='<%=request.isUserInRole("1580")%>'>
                                        <li><a href="c?app=1580">Relatório de Reservas</a></li>
                                    </c:if>

                                    <c:if test='<%=request.isUserInRole("1590")%>'>
                                        <li><a href="c?app=1590">Consulta Reservas</a></li>
                                    </c:if>
                                </ul>        
                            </li>
                            <c:if test='<%=request.isUserInRole("2030")%>'>
                                <li><a href="c?app=2030">Náutica</a></li>
                            </c:if>
                        </ul>
                    </li>

                    <li><span class="qmdivider qmdividerx" ></span></li>

                    <c:if test='<%=request.isUserInRole("1120")%>'>
                        <li><a href="c?app=1120">Exame Médico</a></li>
                    </c:if>

                    <li><span class="qmdivider qmdividerx" ></span></li>

                    <c:if test='<%=request.isUserInRole("2420")%>'>
                        <li><a href="c?app=2420">Achados e Perdidos</a></li>
                    </c:if>
                        
                    <li><span class="qmdivider qmdividerx" ></span></li>

                    <li><a class="qmparent" href="javascript:void(0);">+ Segurança</a>
                        <ul>

                            <c:if test='<%=request.isUserInRole("8030")%>'>
                                <li><a href="c?app=8030">Usuários do Sistema</a></li>
                            </c:if>

                            <c:if test='<%=request.isUserInRole("8020")%>'>
                                <li><a href="c?app=8020">Grupos e Permissões</a></li>
                            </c:if>

                            <c:if test='<%=request.isUserInRole("8050")%>'>
                                <li><a href="c?app=8050">Aplicações e Grupos</a></li>
                            </c:if>
                                
                            <c:if test='<%=request.isUserInRole("8040")%>'>
                                <li><a href="c?app=8040&acao=showFormConsultarLogs">Log</a></li>
                            </c:if>

                            <li><span class="qmdivider qmdividerx" ></span></li>

                            <c:if test='<%=request.isUserInRole("1960")%>'>
                                <li><a href="c?app=1960&acao=showFormConsultarLogs">Log Internet</a></li>
                            </c:if>
                        </ul>
                    </li>

                    </ul>
                </li>
                <!-- FIM DO MENU OPERAÇÕES -->
                
                <li><span class="qmdivider qmdividery" ></span></li>

                <!-- MENU FINANCEIRO -->
                <li><a class="qmparent" href="javascript:void(0)">FINANCEIRO</a>
                    <ul style="width:150px;">

                        <li><a class="qmparent" href="javascript:void(0);">+ Taxas</a>
                            <ul>
                                <c:if test='<%=request.isUserInRole("1410")%>'>
                                    <li><a href="c?app=1410">Descrição das Taxas</a></li>
                                </c:if>

                                <li><span class="qmdivider qmdividerx" ></span></li>

                                <c:if test='<%=request.isUserInRole("1220")%>'>
                                    <li><a href="c?app=1220">Administrativas</a></li>
                                </c:if>

                                <c:if test='<%=request.isUserInRole("2480")%>'>
                                    <li><a href="c?app=2480">Náuticas</a></li>
                                </c:if>
                                <c:if test='<%=request.isUserInRole("1430")%>'>
                                    <li><a href="c?app=1430">Náuticas - Até 2016</a></li>
                                </c:if>

                                <c:if test='<%=request.isUserInRole("3120")%>'>
                                    <li><a href="c?app=3120">Cursos</a></li>
                                </c:if>

                                <c:if test='<%=request.isUserInRole("1300")%>'>
                                    <li><a href="c?app=1300">Comissão de Permanência</a></li>
                                </c:if>

                                <c:if test='<%=request.isUserInRole("1970")%>'>
                                    <li><a href="c?app=1970">INPC</a></li>
                                </c:if>
                            </ul>
                        </li>

                        <li><span class="qmdivider qmdividerx" ></span></li>

                        <li><a class="qmparent" href="javascript:void(0);">+ Carnês</a>
                            <ul>
                                <c:if test='<%=request.isUserInRole("1500")%>'>
                                    <li><a href="c?app=1500">+ Geração</a>
                                        <ul>
                                            <li><a href="c?app=1500">Mensal</a></li>
                                            <li><a href="c?app=1500&acao=avulso">Avulsa</a></li>
                                        </ul>
                                    </li>
                                </c:if>

                                <li><span class="qmdivider qmdividerx" ></span></li>

                                <c:if test='<%=request.isUserInRole("1725")%>'>
                                    <li><a href="c?app=1720">Baixa Manual</a></li>
                                </c:if>

                                <c:if test='<%=request.isUserInRole("1750")%>'>
                                    <li><a href="c?app=1750">Estorno Manual</a></li>
                                </c:if>

                                <li><span class="qmdivider qmdividerx" ></span></li>

                                <li><a class="qmparent" href="javascript:void(0);">+ Convênios</a>
                                    <ul>
                                        <c:if test='<%=request.isUserInRole("1810")%>'>
                                            <li><a href="c?app=1810">Gera Arquivo</a></li>
                                        </c:if>

                                        <c:if test='<%=request.isUserInRole("1700")%>'>
                                            <li><a href="upload?app=1700">Baixa Arquivo</a></li>
                                        </c:if>

                                        <li><span class="qmdivider qmdividerx" ></span></li>

                                        <li><a href="upload?app=1940">Visualizar Arquivo Retorno</a></li>

                                        <li><span class="qmdivider qmdividerx" ></span></li>

                                        <c:if test='<%=request.isUserInRole("1820")%>'>
                                            <li><a href="c?app=1820">Gera DCO</a></li>
                                        </c:if>

                                        <c:if test='<%=request.isUserInRole("1710")%>'>
                                            <li><a href="upload?app=1710">Baixa DCO</a></li>
                                        </c:if>

                                        <li><span class="qmdivider qmdividerx" ></span></li>

                                        <li><a href="c?app=2640">Gerar Movimento Carteira 109 Itaú</a></li>
                                        <li><a href="c?app=2560">Gerar Arquivo Dia Itau</a></li>
                                    </ul>
                                </li>
                            </ul>
                        </li>

                        <li><span class="qmdivider qmdividerx" ></span></li>

                        <li><a class="qmparent" href="javascript:void(0);">+ Repasse Concessionário</a>
                            <ul>
                                <c:if test='<%=request.isUserInRole("2570")%>'>
                                    <li><a href="c?app=2570">Repasses</a></li>
                                </c:if>
                                <c:if test='<%=request.isUserInRole("2580")%>'>
                                    <li><a href="c?app=2580">Relatório</a></li>
                                </c:if>
                            </ul>
                        </li>

                        <li><span class="qmdivider qmdividerx" ></span></li>

                        <c:if test='<%=request.isUserInRole("1920")%>'>
                            <li><a href="c?app=1920">Cálculo de Encargos</a></li>
                        </c:if>
                        <c:if test='<%=request.isUserInRole("2330")%>'>
                            <li><a href="c?app=2330">Dias Sem Multa Internet</a></li>
                        </c:if>
                        <c:if test='<%=request.isUserInRole("2410")%>'>
                            <li><a href="c?app=2410">Parâmetros Financeiros</a></li>
                        </c:if>
                        <c:if test='<%=request.isUserInRole("2650")%>'>
                            <li><a href="c?app=2650">Liberação Boleto Internet</a></li>
                        </c:if>

                        <li><span class="qmdivider qmdividerx" ></span></li>

                        <c:if test='<%=request.isUserInRole("2430")%>'>
                            <li><a href="c?app=2430">Boleto Avulso</a></li>
                        </c:if>

                        <li><span class="qmdivider qmdividerx" ></span></li>

                        <c:if test='<%=request.isUserInRole("2550")%>'>
                            <li><a href="c?app=2550">Log Integração Benner</a></li>
                        </c:if>

                    </ul>
                    
                </li>
                <!-- FIM DO MENU FINANCEIRO -->

                <li><span class="qmdivider qmdividery" ></span></li>

                <!-- MENU CURSO -->
                <li><a class="qmparent" href="javascript:void(0)">CURSO</a>
                    <ul style="width:150px;">
                        <c:if test='<%=request.isUserInRole("3040")%>'>
                            <li><a href="c?app=3040&mostrarSomenteTurmasAtivas=true">Matrícula</a></li>
                        </c:if>

                        <c:if test='<%=request.isUserInRole("3030")%>'>
                            <li><a href="c?app=3030&mostrarSomenteTurmasAtivas=true">Turma</a></li>
                        </c:if>

                        <li><span class="qmdivider qmdividerx" ></span></li>

                        <c:if test='<%=request.isUserInRole("3020")%>'>
                            <li><a href="c?app=3020">Curso</a></li>
                        </c:if>

                        <c:if test='<%=request.isUserInRole("3010")%>'>
                            <li><a href="c?app=3010">Modalidade</a></li>
                        </c:if>
                            
                        <li><span class="qmdivider qmdividerx" ></span></li>
                        
                        <li><a class="qmparent" href="javascript:void(0);">+ Agenda Academia</a>
                            <ul>
                                <c:if test='<%=request.isUserInRole("3230")%>'>
                                    <li><a href="c?app=3230">Agenda</a></li>
                                </c:if>
                                    
                                <li><span class="qmdivider qmdividerx" ></span></li>
                                
                                <c:if test='<%=request.isUserInRole("3210")%>'>
                                    <li><a href="c?app=3210">Serviços</a></li>
                                </c:if>
                                <c:if test='<%=request.isUserInRole("3220")%>'>
                                    <li><a href="c?app=3220">Exceções</a></li>
                                </c:if>

                                <li><span class="qmdivider qmdividerx" ></span></li>

                                <c:if test='<%=request.isUserInRole("3200")%>'>
                                    <li><a href="c?app=3200">Local</a></li>
                                </c:if>
                                    
                                <li><span class="qmdivider qmdividerx" ></span></li>

                                <c:if test='<%=request.isUserInRole("2450")%>'>
                                    <li><a href="c?app=2450">Relatório Quantitativo</a></li>
                                </c:if>
                            </ul>
                        </li>
                        
                        <li><a class="qmparent" href="javascript:void(0);">+ Emprestimos Academia</a>
                            <ul>
                                <c:if test='<%=request.isUserInRole("2540")%>'>
                                    <li><a href="c?app=2540">Empréstimos</a></li>
                                </c:if>
                                    
                                <li><span class="qmdivider qmdividerx" ></span></li>
                                
                                <c:if test='<%=request.isUserInRole("2520")%>'>
                                    <li><a href="c?app=2520">Tipo de Armário</a></li>
                                </c:if>
                                    
                                <c:if test='<%=request.isUserInRole("2530")%>'>
                                    <li><a href="c?app=2530">Armário</a></li>
                                </c:if>
                                    
                                
                            </ul>
                        </li>
                        <li><a class="qmparent" href="javascript:void(0);">+ Emprestimos Sauna</a>
                            <ul>
                                <c:if test='<%=request.isUserInRole("2680")%>'>
                                    <li><a href="c?app=2680">Empréstimos</a></li>
                                </c:if>
                                    
                                <li><span class="qmdivider qmdividerx" ></span></li>
                                
                                <c:if test='<%=request.isUserInRole("2660")%>'>
                                    <li><a href="c?app=2660">Tipo de Armário</a></li>
                                </c:if>
                                    
                                <c:if test='<%=request.isUserInRole("2670")%>'>
                                    <li><a href="c?app=2670">Armário</a></li>
                                </c:if>
                                    
                                
                            </ul>
                        </li>
                    </ul>
                </li>
                <!-- FIM DO MENU CURSO -->

                <li><span class="qmdivider qmdividery" ></span></li>

                <!-- MENU RELATÓRIOS -->
                <li><a class="qmparent" href="javascript:void(0);">RELATÓRIOS</a>
                    <ul style="width:150px;">
                        <li><a class="qmparent" href="javascript:void(0);">+ Sócios</a>
                            <ul>
                                <c:if test='<%=request.isUserInRole("1240")%>'>
                                    <li><a href="c?app=1240">Dados Cadastrais Completos</a></li>
                                </c:if>

                                <c:if test='<%=request.isUserInRole("1640")%>'>
                                    <li><a href="c?app=1640">Dados Cadastrais Simplificado</a></li>
                                </c:if>

                                <c:if test='<%=request.isUserInRole("1250")%>'>
                                    <li><a href="c?app=1250">Aniversariantes</a></li>
                                </c:if>

                                <c:if test='<%=request.isUserInRole("1270")%>'>
                                    <li><a href="c?app=1270">Genérico</a></li>
                                </c:if>

                                <c:if test='<%=request.isUserInRole("6120")%>'>
                                    <li><a href="c?app=6120">Extrato de Sócio</a></li>
                                </c:if>

                                <c:if test='<%=request.isUserInRole("1370")%>'>
                                    <li><a href="c?app=1370">Cobrança</a></li>
                                </c:if>

                                <c:if test='<%=request.isUserInRole("1520")%>'>
                                    <li><a href="c?app=1520">Geral</a></li>
                                </c:if>

                                <c:if test='<%=request.isUserInRole("1630")%>'>
                                    <li><a href="c?app=1630">Cargos Especiais</a></li>
                                </c:if>

                                <c:if test='<%=request.isUserInRole("1990")%>'>
                                    <li><a href="c?app=1990">Etiqueta Manual</a></li>
                                </c:if>

                                <c:if test='<%=request.isUserInRole("2280")%>'>
                                    <li><a href="c?app=2280">Brinde Atualização Internet</a></li>
                                </c:if>
                                    
                                <c:if test='<%=request.isUserInRole("2600")%>'>
                                    <li><a href="c?app=2600">Resumo Quadro Social/Financeiro</a></li>
                                </c:if>
                                    
                                <c:if test='<%=request.isUserInRole("2630")%>'>
                                    <li><a href="c?app=2630">Quadro Social por Idade</a></li>
                                </c:if>

                                <c:if test='<%=request.isUserInRole("2690")%>'>
                                    <li><a href="c?app=2690">Endereços Repetidos</a></li>
                                </c:if>
                                <c:if test='<%=request.isUserInRole("2700")%>'>
                                    <li><a href="c?app=2700">Exportação CSV</a></li>
                                </c:if>
                            </ul>
                        </li>

                        <li><a class="qmparent" href="javascript:void(0);">+ Curso</a>
                            <ul>
                                <c:if test='<%=request.isUserInRole("1280")%>'>
                                    <li><a href="c?app=1280">Alunos por Modalidade</a></li>
                                </c:if>

                                <c:if test='<%=request.isUserInRole("3130")%>'>
                                    <li><a href="c?app=3130">Pauta</a></li>
                                </c:if>

                                <c:if test='<%=request.isUserInRole("3140")%>'>
                                    <li><a href="c?app=3140">Matrículas e Cancelamentos</a></li>
                                </c:if>

                                <c:if test='<%=request.isUserInRole("2150")%>'>
                                    <li><a href="c?app=2150">Desconto dos Alunos</a></li>
                                </c:if>
                                    
                                <c:if test='<%=request.isUserInRole("2400")%>'>
                                    <li><a href="c?app=2400">Alunos por Faixa Etária</a></li>
                                </c:if>
                                    
                                <c:if test='<%=request.isUserInRole("2490")%>'>
                                    <li><a href="c?app=2490">Desconto Familiar Academia</a></li>
                                </c:if>
                            </ul>
                        </li>

                        <li><a class="qmparent" href="javascript:void(0);">+ Barcos</a>
                            <ul>
                                <c:if test='<%=request.isUserInRole("1350")%>'>
                                    <li><a href="c?app=1350">Dados Cadastrais</a></li>
                                </c:if>

                                <c:if test='<%=request.isUserInRole("1660")%>'>
                                    <li><a href="c?app=1660">Autorização de Embarque</a></li>
                                </c:if>

                                <c:if test='<%=request.isUserInRole("1690")%>'>
                                    <li><a href="c?app=1690">Vencimentos</a></li>
                                </c:if>

                                <c:if test='<%=request.isUserInRole("1730")%>'>
                                    <li><a href="c?app=1730">Registros e Baixas</a></li>
                                </c:if>

                                <c:if test='<%=request.isUserInRole("2060")%>'>
                                    <li><a href="c?app=2060">Movimentação</a></li>
                                </c:if>
                            </ul>
                        </li>

                        <li><a class="qmparent" href="javascript:void(0);">+ Acesso</a>
                            <ul>
                                <c:if test='<%=request.isUserInRole("1290")%>'>
                                    <li><a href="c?app=1290">Relação de Acessos</a></li>
                                </c:if>

                                <c:if test='<%=request.isUserInRole("1480")%>'>
                                    <li><a href="c?app=1480">Estatística de Horários</a></li>
                                </c:if>

                                <c:if test='<%=request.isUserInRole("1490")%>'>
                                    <li><a href="c?app=1490">Autorização de Entrada</a></li>
                                </c:if>
                            </ul>
                        </li>

                        <li><a class="qmparent" href="javascript:void(0);">+ Financeiro</a>
                            <ul>
                                <c:if test='<%=request.isUserInRole("1800")%>'>
                                    <li><a href="c?app=1800">Valores Recebidos</a></li>
                                </c:if>

                                <c:if test='<%=request.isUserInRole("1850")%>'>
                                    <li><a href="c?app=1850">Valores de Escolinhas</a></li>
                                </c:if>

                                <c:if test='<%=request.isUserInRole("1840")%>'>
                                    <li><a href="c?app=1840">Demonstrativo Arrecadação</a></li>
                                </c:if>

                                <c:if test='<%=request.isUserInRole("1830")%>'>
                                    <li><a href="c?app=1830">Ocorrencias Baixa Automática</a></li>
                                </c:if>

                                <c:if test='<%=request.isUserInRole("1365")%>'>
                                    <li><a href="c?app=1365">Carnês para Sequencial</a></li>
                                </c:if>

                                <c:if test='<%=request.isUserInRole("1752")%>'>
                                    <li><a href="c?app=1752">Estorno Manual</a></li>
                                </c:if>

                                <c:if test='<%=request.isUserInRole("1470")%>'>
                                    <li><a href="c?app=1470">Inadimplência</a></li>
                                </c:if>

                                <c:if test='<%=request.isUserInRole("1510")%>'>
                                    <li><a href="c?app=1510">Taxas</a></li>
                                </c:if>

                                <c:if test='<%=request.isUserInRole("1620")%>'>
                                    <li><a href="c?app=1620">Títulos que não geram taxa</a></li>
                                </c:if>

                                <c:if test='<%=request.isUserInRole("2020")%>'>
                                    <li><a href="c?app=2020">Carnês e Parcelas</a></li>
                                </c:if>

                                <c:if test='<%=request.isUserInRole("2240")%>'>
                                    <li><a href="c?app=2240">Declaração de Quitação</a></li>
                                </c:if>
                                
                                <c:if test='<%=request.isUserInRole("2240")%>'>
                                    <li><a href="c?app=2290">Declaração de Quitação Enviada no Boleto</a></li>
                                </c:if>
                                
                                <c:if test='<%=request.isUserInRole("2320")%>'>
                                    <li><a href="c?app=2320">Envio de Boleto por Email</a></li>
                                </c:if>
                                    
                                <c:if test='<%=request.isUserInRole("2460")%>'>
                                    <li><a href="c?app=2460">Saldo de Crédito</a></li>
                                </c:if>
                                    
                                <c:if test='<%=request.isUserInRole("2470")%>'>
                                    <li><a href="c?app=2470">Créditos</a></li>
                                </c:if>
                            </ul>
                        </li>

                        <li><a class="qmparent" href="javascript:void(0);">+ Contabilidade</a>
                            <ul>
                                <c:if test='<%=request.isUserInRole("2440")%>'>
                                    <li><a href="c?app=2440">Valores a Receber</a></li>
                                </c:if>

                                <c:if test='<%=request.isUserInRole("6320")%>'>
                                    <li><a href="c?app=6320&origem=L">Saldo Iate Card Pré</a></li>
                                </c:if>
                                    
                                <c:if test='<%=request.isUserInRole("2500")%>'>
                                    <li><a href="c?app=2500">Receitas</a></li>
                                </c:if>
                                <c:if test='<%=request.isUserInRole("2510")%>'>
                                    <li><a href="c?app=2510">Cancelamento de Receitas</a></li>
                                </c:if>
                            </ul>
                        </li>
                        
                        
                        <li><a class="qmparent" href="javascript:void(0);">+ Eleições</a>
                            <ul>
                                <c:if test='<%=request.isUserInRole("1900")%>'>
                                    <li><a href="c?app=1900">Mapa de Sócios com Direito a Voto</a></li>
                                </c:if>

                                <c:if test='<%=request.isUserInRole("1910")%>'>
                                    <li><a href="c?app=1910">Folha de Votaçao</a></li>
                                </c:if>
                            </ul>
                        </li>

                        <c:if test='<%=request.isUserInRole("1400")%>'>
                            <li><a href="c?app=1400">Documentos Emitidos</a></li>
                        </c:if>

                        <c:if test='<%=request.isUserInRole("1420")%>'>
                            <li><a href="c?app=1420">Reserva de Dependência</a></li>
                        </c:if>

                        <c:if test='<%=request.isUserInRole("1980")%>'>
                            <li><a href="c?app=1980">Convites</a></li>
                        </c:if>
                        
                        <c:if test='<%=request.isUserInRole("2130")%>'>
                            <li><a href="c?app=2130">CPF dos Convidados</a></li>
                        </c:if>
                            
                        <c:if test='<%=request.isUserInRole("2200")%>'>
                            <li><a href="javascript:void(0);">Consumo de Funcionários</a></li>
                        </c:if>

                        <c:if test='<%=request.isUserInRole("2220")%>'>
                            <li><a href="c?app=2220">Cadastro de Carros</a></li>
                        </c:if>

                        <c:if test='<%=request.isUserInRole("2230")%>'>
                            <li><a href="c?app=2230">Permissão Provisória</a></li>
                        </c:if>

                        <c:if test='<%=request.isUserInRole("1870")%>'>
                            <li><a href="c?app=1870">Geração para EMIATE</a></li>
                        </c:if>
                    </ul>
                </li>
                <!-- FIM DO MENU RELATÓRIOS -->

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
            
