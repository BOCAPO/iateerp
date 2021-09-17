<%@include file="head.jsp"%>
<%@include file="menu.jsp"%>

<script type="text/javascript" language="JavaScript" src="js/format.js"></script>
<style type="text/css">
    table.fmt {border:none;width:0;padding:0px;margin-left:0em;margin-top:0em;}
    table.fmt tr td {border:none;background:none;padding:0px;margin:0em auto;}
    
    .zera{ 
    font-family: "Trebuchet MS",Verdana,Arial,Helvetica,sans-serif;
    color: #678197;
    font-size: 12px;
    font-weight: normal;
    }    
</style>

<body class="internas">

    <div class="divisoria"></div>
    <div id="titulo-subnav">Impressão de Etiquetas</div>
    <div class="divisoria"></div>
    
    <form method="POST" action="c">
	<input type="hidden" name="app" value="1930">
        <input type="hidden" name="sql" value="${sql}">

        <table class="fmt">
            <tr>
                <td>
                    <table class="fmt">
                        <tr>
                            <td>
                                <fieldset class="field-set legendaFrame recuoPadrao" style="width:240px;height: 130px;">
                                    <legend >Ordenação:</legend>
                                    <select name="ord1" class="campoSemTamanho alturaPadrao" style="margin-top:3px;width:210px">
                                        <option value="Categoria">Categoria</option>
                                        <option value="Matricula">Matricula</option>
                                        <option value="Nome">Nome</option>
                                        <option value="CEP">CEP</option>
                                    </select>
                                    <BR>
                                    <select name="ord2" class="campoSemTamanho alturaPadrao" style="margin-top:3px;width:210px">
                                        <option value="Categoria">Categoria</option>
                                        <option value="Matricula">Matricula</option>
                                        <option value="Nome">Nome</option>
                                        <option value="CEP">CEP</option>
                                    </select>
                                    <BR>
                                    <select name="ord3" class="campoSemTamanho alturaPadrao" style="margin-top:3px;width:210px">
                                        <option value="Categoria">Categoria</option>
                                        <option value="Matricula">Matricula</option>
                                        <option value="Nome">Nome</option>
                                        <option value="CEP">CEP</option>
                                    </select>
                                    <BR>
                                    <select name="ord4" class="campoSemTamanho alturaPadrao" style="margin-top:3px;width:210px">
                                        <option value="Categoria">Categoria</option>
                                        <option value="Matricula">Matricula</option>
                                        <option value="Nome">Nome</option>
                                        <option value="CEP">CEP</option>
                                    </select>                                    
                                </fieldset>
                            </td>
                        </tr>
                        <tr>
                            <td>
                                <input type="checkbox" name="imprimirCategoriaMatricula" class="legendaCodigo" style="margin-top:7px" value="true" checked>Imprimir Categoria/Matricula
                            </td>
                        </tr>
                        <tr>
                            <td>
                                <input type="checkbox" name="imprimirLogotipo" class="legendaCodigo" style="margin-top:7px" value="true">Imprimir Logotipo
                            </td>
                        </tr>
                        <tr>
                            <td>
                                <input type="checkbox" name="imprimirTexto" class="legendaCodigo" style="margin-top:7px" value="true">Imprimir Texto
                            </td>
                        </tr>
                        <tr>
                            <td>
                                <input type="text" class="campoSemTamanho alturaPadrao" name="texto" maxlength="32"  style="width:240px">
                            </td>
                        </tr>
                        <tr>
                            <td>
                                <br>
                                <input type="submit" class="botaoatualizar" value=" " />        
                                <input type="button" class="botaoVoltar"  onclick="history.go(-1);" value=" " />
                            </td>
                        </tr>
                    </table>
                </td>
            </tr>

        
        
        
    </form>

</body>
</html>
