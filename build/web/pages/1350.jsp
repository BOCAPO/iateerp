<%@taglib prefix="sql" uri="http://java.sun.com/jsp/jstl/sql"%>

<%@include file="head.jsp"%>

<style type="text/css">
    table.fmt {border:none;width:0;padding:0px;margin-left:0em;margin-top:0em;}
    table.fmt tr td {border:none;background:none;padding:0px;margin:0em auto;}
</style>  

<script type="text/javascript" language="JavaScript" src="js/format.js"></script>
<script type="text/javascript" language="JavaScript">
    $(document).ready(function () {
        $("#dtRefValor").mask("99/99/9999");
    });

    function visualizar() {
        document.forms[0].acao.value = 'visualizar';
        validarForm();
    }

    function gerarEtiqueta() {
        document.forms[0].acao.value = 'gerarEtiqueta';
        validarForm();
    }

    function updateOrdenacao() {
        var combo1 = document.getElementById("ordenacao1");
        var combo2 = document.getElementById("ordenacao2");
        var combo3 = document.getElementById("ordenacao3");
        for (var i = document.forms[0].ordenacao1.length - 1; i >= 0; i--) {
            combo1.remove(i);
            combo2.remove(i);
            combo3.remove(i);
        }

        for (var i = 0; i < document.forms[0].colunas.length; i++) {
            if (document.forms[0].colunas[i].checked) {
                var opt1 = document.createElement("option");
                var opt2 = document.createElement("option");
                var opt3 = document.createElement("option");

                opt1.text = document.forms[0].colunas[i].title;
                opt1.value = document.forms[0].colunas[i].value.split(";")[0];

                opt2.text = document.forms[0].colunas[i].title;
                opt2.value = document.forms[0].colunas[i].value.split(";")[0];

                opt3.text = document.forms[0].colunas[i].title;
                opt3.value = document.forms[0].colunas[i].value.split(";")[0];

                combo1.add(opt1, null);
                combo2.add(opt2, null);
                combo3.add(opt3, null);
            }
        }

    }

    function validarForm() {
        var k = 0;
        for (var i = 0; i < document.forms[0].categorias.length; i++) {
            if (document.forms[0].categorias[i].checked) {
                k++;
            }
        }
        if (k == 0) {
            alert('Selecione pelo menos uma Categoria de Sócio.');
            return;
        }

        k = 0;
        for (var i = 0; i < document.forms[0].categoriasNauticas.length; i++) {
            if (document.forms[0].categoriasNauticas[i].checked) {
                k++;
            }
        }
        if (k == 0) {
            alert('Selecione pelo menos uma Categoria Náutica.');
            return;
        }

        k = 0;
        for (var i = 0; i < document.forms[0].tipoVaga.length; i++) {
            if (document.forms[0].tipoVaga[i].checked) {
                k++;
            }
        }
        if (k == 0) {
            alert('Selecione pelo menos um Tipo de Vaga.');
            return;
        }

        k = 0;
        var v = 0;
        for (var i = 0; i < document.forms[0].colunas.length; i++) {
            if (document.forms[0].colunas[i].checked) {
                k++;
                if (document.forms[0].colunas[i].title == "Valor") {
                    v++;
                }
            }
        }
        if (k == 0) {
            alert('Selecione pelo menos uma Coluna para Aparecer no Relatório.');
            return;
        }
        if (!document.forms[0].barco.checked && !document.forms[0].box.checked) {
            alert('Selecione pelo menos um Tipo (Barco e/ou Box).');
            return;
        }

        if (trim(document.forms[0].tituloInicio.value) == ''
                || trim(document.forms[0].tituloFim.value) == '') {
            alert('Informe o título inicial e final para o Relatório!');
            return;
        }
        if (parseInt(trim(document.forms[0].tituloInicio.value)) > parseInt(trim(document.forms[0].tituloFim.value))) {
            alert('Número do Título final deve ser posterior ao inicial.');
            return;
        }
        if (parseInt(trim(document.forms[0].pesInicio.value)) > parseInt(trim(document.forms[0].pesFim.value))) {
            alert('Número de pés final deve ser posterior ao inicial.');
            return;
        }

        if (v > 0) {
            if (trim(document.forms[0].dtRefValor.value) == '') {
                alert('É preciso preencher a Data de Referência');
                return;
            }
            if (!isDataValida(document.forms[0].dtRefValor.value)) {
                return;
            }
        }

        document.forms[0].submit();
    }

</script>

<body class="internas">

    <%@include file="menu.jsp"%>

    <div class="divisoria"></div>
    <div id="titulo-subnav">Relatório de Embarcações - Proprietário</div>
    <div class="divisoria"></div>

    <form method="POST" action="c">
        <input type="hidden" name="app" value="1350">
        <input type="hidden" name="acao">

        <table class="fmt">
            <tr >
                <td valign="top">
                    <br>
                    <fieldset class="field-set legendaFrame recuoPadrao" style="width:210px;height: 50px">
                        <legend >Título:</legend>
                        &nbsp;&nbsp;&nbsp;<input type="text" name="tituloInicio" onkeypress="onlyNumber(event)" class="campoSemTamanho alturaPadrao larguraNumero">        
                        &nbsp;&nbsp;&nbsp;&nbsp;a
                        <input type="text" name="tituloFim" onkeypress="onlyNumber(event)" class="campoSemTamanho alturaPadrao larguraNumero">        
                    </fieldset>                                
                </td>
                <td>
                    <p class="legendaCodigo">Categorias:<span><input class="botaoMarcaDesmarca" type="button"  onclick="marcaDesmarca('categorias')" style="margin-top: 2px;" value="" title="Consultar" /></span></p>
                    <div class="recuoPadrao" style="overflow:auto;height:200px;width:210px;">
                        <c:forEach var="categoria" items="${categorias}">
                            <input type="checkbox" name="categorias" value="${categoria.id}">${categoria.descricao}<br>
                        </c:forEach>
                    </div>
                </td>
            </tr>
        </table>

        <div class="divisoria"></div>
        <div id="titulo-subnav">Embarcação</div>
        <div class="divisoria"></div>


        <table class="fmt">
            <tr>
                <td>
                    <table class="fmt">
                        <tr>
                            <td>
                                <p class="legendaCodigo">Categorias Náuticas:<span><input class="botaoMarcaDesmarca" type="button"  onclick="marcaDesmarca('categoriasNauticas')" style="margin-top: 2px;" value="" title="Consultar" /></span></p>
                                <div class="recuoPadrao" style="overflow:auto;height:250px;width:210px;">
                                    <c:forEach var="categoriaNautica" items="${categoriasNauticas}">
                                        <input type="checkbox" name="categoriasNauticas" value="${categoriaNautica.cod}">${categoriaNautica.descricao}<br>
                                    </c:forEach>
                                </div>        
                            </td>
                        </tr>
                        <tr>
                            <td>
                                <br>
                                <fieldset class="field-set legendaFrame recuoPadrao" style="width:210px;height: 50px">
                                    <legend >Nº pés:</legend>
                                    &nbsp;&nbsp;&nbsp;<input type="text" name="pesInicio" onkeypress="onlyNumber(event)" class="campoSemTamanho alturaPadrao larguraNumero">       
                                    &nbsp;&nbsp;&nbsp;&nbsp;a
                                    <input type="text" name="pesFim" onkeypress="onlyNumber(event)" class="campoSemTamanho alturaPadrao larguraNumero">        
                                </fieldset>                                
                            </td>
                        </tr>
                    </table>
                </td>
                <td>
                    <table class="fmt">
                        <tr>
                            <td>
                                <p class="legendaCodigo">Tipo de Vaga:<span><input class="botaoMarcaDesmarca" type="button"  onclick="marcaDesmarca('tipoVaga')" style="margin-top: 2px;" value=""  /></span></p>
                                <div class="recuoPadrao" style="overflow:auto;height:270px;width:210px;">
                                    <sql:query var="rsTpVaga" dataSource="jdbc/iate">
                                        SELECT CO_TIPO_VAGA, DE_TIPO_VAGA FROM TB_TIPO_VAGA_BARCO ORDER BY DE_TIPO_VAGA
                                    </sql:query>                    
                                    <c:forEach var="rowTpVaga" items="${rsTpVaga.rows}">
                                        <input type="checkbox" name="tipoVaga" value="${rowTpVaga.CO_TIPO_VAGA}">${rowTpVaga.CO_TIPO_VAGA} - ${rowTpVaga.DE_TIPO_VAGA}<br>
                                    </c:forEach>
                                </div>        
                            </td>
                        </tr>
                        <tr>
                            <td>
                                <fieldset class="field-set legendaFrame recuoPadrao" style="width:210px;height: 50px">
                                    <legend >Tipo:</legend>
                                    <input style="margin-left: 40px; margin-top: 5px" type="checkbox" name="barco" value="true" checked>Barco
                                    <input style="margin-left: 40px; margin-top: 5px" type="checkbox" name="box" value="true" checked>Box<br>
                                </fieldset>
                            </td>
                        </tr>

                    </table>
                </td>
                <td>
                    <table class="fmt">
                        <tr>
                            <td>
                                <p class="legendaCodigo">Local de Box:<span><input class="botaoMarcaDesmarca" type="button"  onclick="marcaDesmarca('localBox')" style="margin-top: 2px;" value="" title="Consultar" /></span></p>
                                <div class="recuoPadrao" style="overflow:auto;height:330px;width:300px;">
                                    <c:forEach var="LocalBox" items="${LocaisBox}">
                                        <input type="checkbox" name="localBox" value="${LocalBox.id}">${LocalBox.descricao}<br>
                                    </c:forEach>
                                </div>        
                            </td>
                        </tr>
                    </table>
                </td>
            </tr>
        </table>

        <div class="divisoria"></div>
        <div id="titulo-subnav">Informação</div>
        <div class="divisoria"></div>

        <table class="fmt">
            <tr>
                <td>
                    <p class="legendaCodigo">Colunas que aparecerão no relatório:</p>
                    <div class="recuoPadrao" style="overflow:auto;height:210px;width:210px;">
                        <input type="checkbox" onchange="updateOrdenacao()" name="colunas" value="T1.CD_MATRICULA;Nr. Título" title="Nr. Título">Nr. Título<br>
                        <input type="checkbox" onchange="updateOrdenacao()" name="colunas" value="T1.CD_CATEGORIA;Cod. Categoria" title="Cod. Categoria">Cod. Categoria<br>
                        <input type="checkbox" onchange="updateOrdenacao()" name="colunas" value="T1.DE_EMAIL;Email" title="Email">Email<br>
                        <input type="checkbox" onchange="updateOrdenacao()" name="colunas" value="T2.DESCR_CATEGORIA;Descr. Categoria" title="Descr. Categoria">Descr. Categoria<br>
                        <input type="checkbox" onchange="updateOrdenacao()" name="colunas" value="T1.NOME_PESSOA;Nome Proprietário" title="Nome Proprietário">Nome Proprietário<br>
                        <input type="checkbox" onchange="updateOrdenacao()" name="colunas" value="CASE ISNULL(IC_BOX, 'N') WHEN 'S' THEN 'BOX (' + CONVERT(VARCHAR, NR_BOX_BARCO) + ')' ELSE  T4.NO_BARCO END;Nome Barco" title="Nome Barco">Nome Barco<br>
                        <input type="checkbox" onchange="updateOrdenacao()" name="colunas" value="T3.CD_CATEGORIA_NAUTICA;Cod. Categoria Náutica" title="Cod. Categoria Náutica">Cod. Categoria Náutica<br>
                        <input type="checkbox" onchange="updateOrdenacao()" name="colunas" value="T3.DESCR_CATEGORIA_NAUTICA;Descr. Categoria Náutica" title="Descr. Categoria Náutica">Descr. Categoria Náutica<br>
                        <input type="checkbox" onchange="updateOrdenacao()" name="colunas" value="T4.DT_CADASTRAMENTO_BARCO;Dt. Cadastramento Barco" title="Dt. Cadastramento Barco">Dt. Cadastramento Barco<br>
                        <input type="checkbox" onchange="updateOrdenacao()" name="colunas" value="T4.DT_REGISTRO_BARCO;Dt. Registro Barco" title="Dt. Registro Barco">Dt. Registro Barco<br>
                        <input type="checkbox" onchange="updateOrdenacao()" name="colunas" value="T4.NR_BOX_BARCO;Nr. Box Barco" title="Nr. Box Barco">Nr. Box Barco<br>
                        <input type="checkbox" onchange="updateOrdenacao()" name="colunas" value="T4.NR_CAPITANIA_BARCO;Nr. Capitania Barco" title="Nr. Capitania Barco">Nr. Capitania Barco<br>
                        <input type="checkbox" onchange="updateOrdenacao()" name="colunas" value="T4.NR_PES;Nr. Pes Barco" title="Nr. Pes Barco">Nr. Pes Barco<br>
                        <input type="checkbox" onchange="updateOrdenacao()" name="colunas" value="T4.CD_TP_VAGA_ESTACIONAMENTO;Tipo de Vaga" title="Tipo de Vaga">Tipo de Vaga<br>
                        <input type="checkbox" onchange="updateOrdenacao()" name="colunas" value="T4.NU_PATRIMONIO;Patrimonio" title="Patrimonio">Patrimonio<br>
                        <input type="checkbox" onchange="updateOrdenacao()" name="colunas" value="T4.VR_PERC_DESCONTO;% Desc." title="% Desc.">% Desc.<br>
                        <input type="checkbox" onchange="updateOrdenacao()" name="colunas" value="ISNULL(T5.TELEFONE_L, '') + '/' +  ISNULL(T5.TELEFONE_R, '') + '/' + ISNULL(T5.TELEFONE_C, '');Telefones." title="Telefones">Telefones<br>

                        <input type="checkbox" onchange="updateOrdenacao()" name="colunas" value="T4.QT_M2;Qt.M2" title="Qt.M2">Qt.M2<br>
                        <input type="checkbox" onchange="updateOrdenacao()" name="colunas" value="T4.QT_GELADEIRA;Qt.Geladeira" title="Qt.Geladeira">Qt.Geladeira<br>
                        <input type="checkbox" onchange="updateOrdenacao()" name="colunas" value="T4.IC_EMP_CHAVE;Emp.Chave" title="Emp.Chave">Emp.Chave<br>
                        <input type="checkbox" onchange="updateOrdenacao()" name="colunas" value="T6.DESCR_LOCAL;Local Box" title="Local Box">Local Box<br>
                        <input type="checkbox" onchange="updateOrdenacao()" name="colunas" value="T4.NU_COMPRIMENTO_TOTAL;Comprimento" title="Comprimento">Comprimento<br>
                        <input type="checkbox" onchange="updateOrdenacao()" name="colunas" value="T4.NU_COMPRIMENTO_BOCA;Boca" title="Boca">Boca<br>

                        <input type="checkbox" onchange="updateOrdenacao()" name="colunas" value="CASE ISNULL(IC_BOX, 'N') WHEN 'S' THEN (CONVERT(MONEY, QT_M2)/100) ELSE (CONVERT(MONEY, NU_COMPRIMENTO_BOCA)/100) * (CONVERT(MONEY, NU_COMPRIMENTO_TOTAL)/100) END;Área" title="Área">Área<br>

                        <!--a coluna abaixo tem que ser sempre a ULTIMA e não pode ter o nome "Valor" alterado-->
                        <input type="checkbox" onchange="updateOrdenacao()" name="colunas" value="dbo.FC_CALCULA_TAXA_BARCO_BOX(T4.CD_BARCO,<DATA>);Valor" title="Valor">Valor<br>

                    </div>                
                </td>

                <td valign="top">
                    <br>
                    <fieldset class="field-set legendaFrame recuoPadrao" style="height:60px;width:120px">
                        <legend >Dt. Referência Valor:</legend>        
                        <input type="text" id="dtRefValor" name="dtRefValor" style="margin-top: 8px; width: 85px"   class="campoSemTamanho alturaPadrao">       
                    </fieldset>      
                    <br>
                    <fieldset class="field-set legendaFrame recuoPadrao" style="height:160px;width:220px">
                        <legend >Ordenação:</legend>        
                        <p class="legendaCodigo MargemSuperior0">1º:</p>
                        <select id="ordenacao1" name="ordenacao1" class="campoSemTamanho alturaPadrao" style="width:190px">
                        </select>                
                        <p class="legendaCodigo MargemSuperior0">2º:</p>
                        <select id="ordenacao2" name="ordenacao2" class="campoSemTamanho alturaPadrao" style="width:190px">
                        </select>                        
                        <p class="legendaCodigo MargemSuperior0">3º:</p>
                        <select id="ordenacao3" name="ordenacao3" class="campoSemTamanho alturaPadrao" style="width:190px">
                        </select>                                
                    </fieldset>      
                </td>
            </tr>
        </table>

        <input type="button" onclick="visualizar()" class="botaoatualizar" value=" " />        
        <input type="button" onclick="gerarEtiqueta()" class="botaoImprimirEtiquetas"  value=" " />

    </form>

</body>
</html>
