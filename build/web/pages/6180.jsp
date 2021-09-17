<%@taglib prefix="sql" uri="http://java.sun.com/jsp/jstl/sql"%>
<%@include file="head.jsp"%>

<style type="text/css">
    table.fmt {border:none;width:0;padding:0px;margin-left:0em;margin-top:0em;}
    table.fmt tr td {border:none;background:none;padding:0px;margin:0em auto;}
</style>  
<script type="text/javascript" language="JavaScript" src="js/format.js"></script>

<script type="text/javascript" language="JavaScript">
    $(document).ready(function() {
        $('#campoObservacao').hide();
        $('#pesqSocio').hide();
        $('#pesqCarro').hide();
        $('#divSenha').hide();
        $('#tipoPesquisa').hide();
        $("#pesqCarroSocio").hide();
        $("#pesqFuncionario").hide();
        $("#divTipoConta").hide();
        $("#divCombustivel").hide();

        $.format.locale({
            number: {
                groupingSeparator: '.',
                decimalSeparator: ','
            }
        });

        if (ehPosto()) {
            $("#divCombustivel").show();

            $('#divBotaoSair').css({'top': '375px'});
            $('#divBotaoCancelar').css({'top': '375px'});
            $('#divBotaoAtualizar').css({'top': '375px'});
            $('#divBotaoObservacoes').css({'top': '375px'});
            
            trocaCombustivel();
        }



    });
    
    function ehPosto(){
        $.ajax({url: 'TaxaIndividualAjax', type: 'GET', async: false,
            data: {
                tipo: 9,
                taxa: $('#taxa').val()
            }
        }).success(function(retorno) {
            ret = retorno;
        });

        if (ret == 'SIM') {
            return true;
        }else{
            return false;
        }
    }

    function trocaCombustivel() {
        $('#precoComb').val($.format.number(parseFloat($('#valComb' + $('#combustivel').val()).val()), '#,##0.000'));
        $("#calculo").val("V");
        trocaCalculo();
    }
    
    function trocaCalculo() {
        if($("#calculo").val() == "V"){
            $("#litrosComb").prop('disabled', true);
            $("#valorComb").prop('disabled', false);
        }else{
            $("#litrosComb").prop('disabled', false);
            $("#valorComb").prop('disabled', true);
        }
        $("#litrosComb").val("");
        $("#valorComb").val("");
    }

    function trocaTaxa() {
        $("#divCombustivel").hide();
        $('#divBotaoSair').css({'top': '300px'});
        $('#divBotaoCancelar').css({'top': '300px'});
        $('#divBotaoAtualizar').css({'top': '300px'});
        $('#divBotaoObservacoes').css({'top': '300px'});

        if (ehPosto()) {
            $("#divCombustivel").show();

            $('#divBotaoSair').css({'top': '375px'});
            $('#divBotaoCancelar').css({'top': '375px'});
            $('#divBotaoAtualizar').css({'top': '375px'});
            $('#divBotaoObservacoes').css({'top': '375px'});
            
            trocaCombustivel();
        }
    }
    
    function calculaCombustivel() {
        var preco = parseFloat($('#valComb' + $('#combustivel').val()).val());
        if($("#calculo").val() == "V"){
            var valor = parseFloat($('#valorComb').val().replace('.', '').replace(',', '.'));
            var litrosCalc = parseFloat(valor / preco);
            $('#litrosComb').val($.format.number(litrosCalc, '#,##0.00'));
        }else{
            var litros = parseFloat($('#litrosComb').val().replace('.', '').replace(',', '.'));
            var valorCalc = parseFloat(litros * preco);
            $('#valorComb').val($.format.number(valorCalc, '#,##0.00'));
        }

    }
    


    function cancelaPesquisaCarroSocio() {
        $("#pesqCarroSocio").hide();
    }
    function cancelaPesquisaFuncionario() {
        $("#pesqFuncionario").hide();
    }
    function observacoes() {
        $("#campoObservacao").show();
    }
    function fechaObservacao() {
        $("#campoObservacao").hide();
    }
    function cancelaObservacao() {
        $("textarea#observacao").val('');
        fechaObservacao();
    }
    function sair() {
        $('#app').val('6000');
        $('#acao').val('');
        document.forms[0].submit();
    }
    function cancelar() {
        $('#app').val('6180');
        $('#acao').val('');
        document.forms[0].submit();
    }

    function mostraPesquisaSocio() {
        $('#tabelaSocio').find('tbody').empty();
        $('#nomeSocio').val('');
        $('#matriculaSocio').val('');
        $('#categoriaSocio').val('');

        $("#pesqSocio").show();
        $("#categoriaSocio").focus();

    }

    function mostraPesquisaFuncionario() {
        $('#tabelaFuncionario').find('tbody').empty();
        $('#nomeFuncionario').val('');

        $("#pesqFuncionario").show();
        $("#matriculaFuncionario").focus();

    }

    function cancelaTipoPesquisa() {
        $('#tipoPesquisa').hide();
    }

    function defineTipoPesquisa() {
        $("#tipoPesquisa").hide();
        if ($("input[name='tipoPesq']:checked").val() == 'SOCIO') {
            mostraPesquisaSocio();
        } else if ($("input[name='tipoPesq']:checked").val() == 'FUNCIONARIO') {
            mostraPesquisaFuncionario();
        } else {
            $('#tabelaCarroSocio').find('tbody').empty();
            $('#txtPlaca').val('');

            $("#pesqCarroSocio").show();
            $("#txtPlaca").focus();

        }
    }


    function pesquisa() {

        if (ehPosto()) {
            $("#radioPlaca").show();
        } else {
            $("#radioPlaca").hide();
        }

        $("#tipoPesquisa").show();
        return false;

    }


    function carregaCarroSocio() {

        //pesquisar socio
        var tabela = $('#tabelaCarroSocio').find('tbody').empty();

        $.ajax({url: 'CarroSocioAjax', type: 'GET',
            data: {
                placa: $('#txtPlaca').val()
            }
        }).success(function(retorno) {
            if (retorno.erro !== undefined) {
                alert(retorno.erro);
            } else {

                var linha = "";

                $.each(retorno.carroSocio, function(i) {
                    var linkSel = "";
                    linkSel = "selecionaCarroSocio('" + i + "','" + this.tipo + "');";
                    linha = '<tr><td class="column1"><a href="#" id="tabTitulo" class="column1" onclick="' + linkSel + '">' + this.titulo + '</a></td>';
                    linha = linha + '<td class="column1" id="tabTipo" align="center">' + this.tipo + '</td>';
                    linha = linha + '<td class="column1" id="tabNome" align="center">' + this.nome + '</td>';

                    campoHidden = '<input type="hidden" name="idCarro" id="idCarro" value="' + this.cod + '">';
                    linha = linha + '<td class="column1" align="center">';
                    if (this.qtDocumento > 0) {
                        linha = linha + '<img src="imagens/icones/livre_r.jpg" height="20px"/>';
                    }
                    linha = linha + '</td><td class="column1">' + this.marca + ' ' + campoHidden + '</td>';
                    linha = linha + '<td class="column1" id="tabModelo" align="center">' + this.modelo + '</td>';
                    linha = linha + '<td id="tabPlaca" class="column1" align="center">' + this.placa + '</td>';
                    var linkDoc = "";
                    linkDoc = "mostraDocumento('" + this.cod + "','" + this.tipo + "');";
                    linha = linha + '<td class="column1" align="center"><a href="#" id="tabDocumento" class="column1" onclick="' + linkDoc + '"><img src="imagens/icones/inclusao-titular-07.png"/></a></td></tr>';

                    tabela.append(linha);
                });

            }
        });
    }

    function selecionaCarroSocio(indice, tipo) {
        $("#carro").val("");
        $("#pesqCarroSocio").hide();
        if (tipo == "SOCIO") {
            $("#tipoPessoa").val("S");
        } else {
            $("#tipoPessoa").val("F");
        }

        $("#tabelaCarroSocio tr:eq(" + (parseInt(indice) + 1) + ")").each(function(index) {
            $('#nome').val('#' + $(this).find('#tabTitulo').html() + ' - ' + ($(this).find('#tabNome').html()));
        });

        $("#tabelaCarroSocio tr:eq(" + (parseInt(indice) + 1) + ")").each(function(index) {
            $("#carro").val($(this).find('#idCarro').val());
        });

        if (tipo == "SOCIO") {
            validaSelecaoSocio();
        } else {
            validaSelecaoFuncionario();
        }
    }

    function validaSelecaoSocio() {
        //chamada apos selecionar o socio, seja pela pesquisa normal de socio (selecionaSocio) ou pelo carro (selecionaCarroSocio)

        $.ajax({url: 'TaxaIndividualAjax', type: 'GET', async: false,
            data: {
                tipo: 3,
                categoria: $('#nome').val().substr(1, 2),
                matricula: $('#nome').val().substr(4, 4),
                dependente: $('#nome').val().substr(9, 2)
            }
        }).success(function(retorno) {
            if (retorno != "OK") {
                if ($('#app6185').val() == "true") {
                    if (confirm('Este usuário não possui senha para lançamento de Taxas Individuais. Deseja cadastrar uma agora?')) {
                        window.open('c?app=1880&acao=showForm&matricula=' + $('#nome').val().substr(4, 4) + '&idCategoria=' + $('#nome').val().substr(1, 2) + '&seqDependente=' + $('#nome').val().substr(9, 2), 'page', 'toolbar=no,location=no,status=no,menubar=no,scrollbars=no,resizable=no,width=600,height=600');
                    }
                } else {
                    alert('Este usuário não possui senha para lançamento de Taxas Individuais.');
                }
            }
        });

        $.ajax({url: 'TaxaIndividualAjax', type: 'GET', async: false,
            data: {
                tipo: 11,
                categoria: $('#nome').val().substr(1, 2),
                matricula: $('#nome').val().substr(4, 4),
                dependente: $('#nome').val().substr(9, 2)
            }
        }).success(function(retorno) {
            $('input:radio[name="contaLanc"]').filter('[value="POS"]').removeAttr('disabled');
            if (retorno != "PROBLEMA") {
                $("#divTipoConta").show();
                $("#textoSaldoPre").text('Saldo: R$ ' + retorno);
                if (confirm('Este usuário possui saldo pré-pago de R$' + retorno + '. Deseja que o lançamento seja debitado da conta pré-paga?')) {
                    $('input:radio[name="contaLanc"]').filter('[value="PRE"]').attr('checked', true);
                } else {
                    $('input:radio[name="contaLanc"]').filter('[value="POS"]').attr('checked', true);
                }
            } else {
                $("#divTipoConta").hide();
                $('input:radio[name="contaLanc"]').filter('[value="POS"]').attr('checked', true);
            }
        });

    }

    function carregaSocio() {

        //pesquisar socio
        var tabela = $('#tabelaSocio').find('tbody').empty();

        $.ajax({url: 'TaxaIndividualSocioAjax', type: 'GET',
            data: {
                nome: $('#nomeSocio').val(),
                matricula: $('#matriculaSocio').val(),
                categoria: $('#categoriaSocio').val(),
                carteira: ''
            }
        }).success(function(retorno) {
            if (retorno.erro !== undefined) {
                alert(retorno.erro);
            } else {

                var linha = "";
                var tpCadastro = "";
                var tpCategoria = "";
                var foto = "";
                var autorizacao = "";
                $.each(retorno.jogador, function(i) {
                    if (this.dependente == 0) {
                        tpCadastro = "TITULAR";
                    } else {
                        tpCadastro = "DEPENDENTE";
                    }
                    if (this.tpCategoria == "SO") {
                        tpCategoria = "SÓCIO";
                    } else {
                        tpCategoria = "NÃO SÓCIO";
                    }
                    foto = '<img src="f?tb=6&mat=' + this.matricula + '&seq=' + this.dependente + '&cat=' + this.categoria + '" class="recuoPadrao MargemSuperiorPadrao" width="80" height="100">';
                    autorizacao = "";
                    if (parseInt(this.dependente) > 0) {
                        autorizacao = '<a  href="javascript:autorizaDependente(' + this.matricula + ',' + this.categoria + ',' + this.dependente + ')"><img src="imagens/icones/inclusao-titular-17.png"/></a>'
                    }


                    linha = '<tr><td class="column1"><a href="#" id="tabNome" class="column1" onclick="selecionaSocio(' + i + ')">' + this.nome + '</a></td><td class="column1" id="tabTitulo" align="center">' + this.titulo + '</td><td class="column1">' + tpCadastro + '</td><td class="column1">' + this.descricao + '</td><td class="column1">' + tpCategoria + '</td><td id="tabCarteira" class="column1">' + this.carteira + '</td><td align="center">' + autorizacao + '</td><td>' + foto + '</td></tr>';
                    tabela.append(linha)
                });

            }
        });
    }

    function carregaFuncionario() {
        //pesquisar socio
        var tabela = $('#tabelaFuncionario').find('tbody').empty();

        $.ajax({url: 'TaxaIndividualFuncionarioAjax', type: 'GET',
            data: {
                matricula: $('#matriculaFuncionario').val(),
                nome: $('#nomeFuncionario').val()
            }
        }).success(function(retorno) {
            if (retorno.erro !== undefined) {
                alert(retorno.erro);
            } else {

                var linha = "";
                var foto = "";
                var campoHidden = "";
                $.each(retorno.funcionario, function(i) {
                    foto = '<img src="f?tb=5&cd=' + this.codigo + '" class="recuoPadrao MargemSuperiorPadrao" width="80" height="100">';
                    campoHidden = '<input type="hidden" name="idFuncionario" id="idFuncionario" value="' + this.codigo + '">';

                    linha = '<tr><td class="column1"><a href="#" id="tabNome" class="column1" onclick="selecionaFuncionario(' + i + ')">' + this.nome + '</a></td><td class="column1">' + this.cargo + campoHidden + '</td><td class="column1">' + this.setor + '</td><td>' + foto + '</td></tr>';
                    tabela.append(linha)
                });

            }
        });
    }

    function autorizaDependente(matricula, categoria, dependente) {
        window.open('c?app=1740&acao=showForm&matricula=' + matricula + '&idCategoria=' + categoria + '&seqDependente=' + dependente + '&alerta=0', 'page', 'toolbar=no,location=no,status=no,menubar=no,scrollbars=no,resizable=no,width=600,height=600');
    }


    function selecionaSocio(indice) {
        $("#carro").val("");
        $("#tipoPessoa").val("S");

        $("#pesqSocio").hide();
        $("#tabelaSocio tr:eq(" + (parseInt(indice) + 1) + ")").each(function(index) {
            $('#nome').val('#' + $(this).find('#tabTitulo').html() + ' - ' + ($(this).find('#tabNome').html()));
        });

        validaSelecaoSocio();

    }

    function selecionaFuncionario(indice) {
        $("#tipoPessoa").val("F");

        $("#pesqFuncionario").hide();
        $("#tabelaFuncionario tr:eq(" + (parseInt(indice) + 1) + ")").each(function(index) {
            $('#nome').val('#' + $(this).find('#idFuncionario').val() + ' - ' + ($(this).find('#tabNome').html()));
        });

        validaSelecaoFuncionario();
    }


    function validaSelecaoFuncionario() {
        //chamada apos selecionar o funcionario, seja pela pesquisa normal de funcinario (selecionaFuncionario) ou pelo carro (selecionaCarroSocio)

        var nome = $('#nome').val();

        $.ajax({url: 'TaxaIndividualAjax', type: 'GET', async: false,
            data: {
                tipo: 13,
                codigo: nome.substr(1, nome.indexOf("-") - 2)
            }
        }).success(function(retorno) {
            $("#divTipoConta").show();
            $('input:radio[name="contaLanc"]').filter('[value="PRE"]').attr('checked', true);
            $('input:radio[name="contaLanc"]').filter('[value="POS"]').attr('disabled', true);
            if (retorno != "PROBLEMA") {
                $("#textoSaldoPre").text('Saldo: R$ ' + retorno);
                alert('Este funcionário possui saldo pré-pago de R$ ' + retorno);
            } else {
                $("#textoSaldoPre").text('Saldo: R$ 0,00');
                alert("Funcionário não possui saldo para lançamento!")
            }
        });
    }



    function cancelaPesquisaSocio() {
        $("#pesqSocio").hide();
    }

    function cancelaPesquisaCarro() {
        $("#pesqCarro").hide();
        $("#carro").val("");

        senhaAtualizacao();
    }

    function mostraBotoes() {
        $("#botaoSair").show();
        $("#botaoCancelar").show();
        $("#botaoAtualizar").show();
        $("#botaoObservacoes").show();
        $("#taxa").removeAttr('disabled');
        $("#valor").removeAttr('disabled');
        $("#qtVezes").removeAttr('disabled');
    }
    function escondeBotoes() {
        $("#botaoSair").hide();
        $("#botaoCancelar").hide();
        $("#botaoAtualizar").hide();
        $("#botaoObservacoes").hide();
        $("#taxa").attr('disabled', true);
        $("#valor").attr('disabled', true);
        $("#qtVezes").attr('disabled', true);
    }


    function carregaCarro() {

        //pesquisar socio
        var tabela = $('#tabelaCarro').find('tbody').empty();

        var matricula = "";
        var categoria = "";
        if ($('#tipoPessoa').val() == "S") {
            matricula = $('#nome').val().substr(4, 4);
            categoria = $('#nome').val().substr(1, 2);
        } else {
            var nome = $('#nome').val();
            matricula = nome.substr(1, nome.indexOf("-") - 2);
            categoria = "";
        }

        $.ajax({url: 'TaxaIndividualCarroAjax', type: 'GET',
            data: {
                matricula: matricula,
                categoria: categoria,
                tipo: $('#tipoPessoa').val()
            }
        }).success(function(retorno) {
            if (retorno.erro !== undefined) {
                alert(retorno.erro);
            } else {

                var linha = "";
                var campoHidden = "";
                $.each(retorno.carro, function(i) {
                    campoHidden = '<input type="hidden" name="idCarro" id="idCarro" value="' + this.cod + '">';
                    linha = '<tr><td class="column1" align="center">'
                    if (this.qtDocumento > 0) {
                        linha = linha + '<img src="imagens/icones/livre_r.jpg" height="20px"/>';
                    }
                    linha = linha + '</td><td class="column1"><a href="#" id="tabMarca" class="column1" onclick="selecionaCarro(' + i + ')">' + this.marca + '</a>' + campoHidden + '</td><td class="column1" id="tabModelo" align="center">' + this.modelo + '</td><td id="tabPlaca" class="column1" align="center">' + this.placa + '</td><td class="column1" align="center"><a href="#" id="tabDocumento" class="column1" onclick="mostraDocumento(' + this.cod + ')"><img src="imagens/icones/inclusao-titular-07.png"/></a></td></tr>';
                    tabela.append(linha)
                });

            }
        });
    }

    function selecionaCarro(indice) {
        $("#pesqCarro").hide();
        $("#tabelaCarro tr:eq(" + (parseInt(indice) + 1) + ")").each(function(index) {
            $("#carro").val($(this).find('#idCarro').val());
        });

        senhaAtualizacao();
    }

    function mostraDocumento(idCarro, tipo) {
        if (tipo == "SOCIO") {
            window.open('c?app=2194&acao=mostraFoto&idCarro=' + idCarro, 'page', 'toolbar=no,location=no,status=no,menubar=no,scrollbars=no,resizable=no,width=600,height=600');
        } else {
            window.open('c?app=3244&acao=mostraFoto&idCarro=' + idCarro, 'page', 'toolbar=no,location=no,status=no,menubar=no,scrollbars=no,resizable=no,width=600,height=600');
        }
    }

    function cancelaSenha() {
        $("#divSenha").hide();
        mostraBotoes();
    }
    
    function confirmaSenha() {
        $('#cmdAtualizarSenha').hide();
        $('#cmdCancelarSenha').hide();


        var ret = "";
        if ($('#tipoPessoa').val() == "S") {
            $.ajax({url: 'TaxaIndividualAjax', type: 'GET', async: false,
                data: {
                    tipo: 10,
                    categoria: $('#nome').val().substr(1, 2),
                    matricula: $('#nome').val().substr(4, 4),
                    dependente: $('#nome').val().substr(9, 2),
                    senha: $('#senhaSocio').val()
                }
            }).success(function(retorno) {
                ret = retorno;
            });
        } else {
            var nome = $('#nome').val();
            $.ajax({url: 'TaxaIndividualAjax', type: 'GET', async: false,
                data: {
                    tipo: 15,
                    cdFuncionario: nome.substr(1, nome.indexOf("-") - 2),
                    senha: $('#senhaSocio').val()
                }
            }).success(function(retorno) {
                ret = retorno;
            });
        }

        if (ret != "OK") {
            alert(ret);
            $('#cmdAtualizarSenha').show();
            $('#cmdCancelarSenha').show();
            return false;
        }

        $("#divSenha").hide();
        $("#senha").val("sim");
        finalizaAtualizacao();
    }

    function atualizar() {
        escondeBotoes();

        if ($('#nome').val() == '') {
            alert('Selecione a Pessoa para inclusão da Taxa!');
            mostraBotoes()
            return false;
        }

        if (ehPosto()){
            $('#valor').val($('#valorComb').val());
        }
        
        var valor = parseFloat($('#valor').val().replace('.', '').replace(',', '.'));
        if (valor == 0 || isNaN(valor)) {
            alert('Informe o Valor da Taxa a ser incluída!');
            mostraBotoes()
            return false;
        }
            
        var qtVezes = parseInt($('#qtVezes').val());
        if (qtVezes == 0 || isNaN(qtVezes)) {
            alert('Informe a quantidade de vezes que será repetida a taxa!');
            mostraBotoes()
            return false;
        }
        if (qtVezes > 1 && $("input[name='contaLanc']:checked").val() == 'PRE') {
            alert('Não é possível repetir cobrança para lançamentos Pré-Pagos!')
            mostraBotoes();
            return false;
        }

        if ($('#tipoPessoa').val() == "S") {
            //validacoes de socio
            var ret = '';
            $.ajax({url: 'TaxaIndividualAjax', type: 'GET', async: false,
                data: {
                    tipo: 4,
                    categoria: $('#nome').val().substr(1, 2),
                    matricula: $('#nome').val().substr(4, 4),
                    dependente: $('#nome').val().substr(9, 2),
                    taxa: $('#taxa').val()
                }
            }).success(function(retorno) {
                ret = retorno;
            });
            if (ret != "OK") {
                alert('Pessoa não autorizada para lançamento nesta taxa!');
                mostraBotoes()
                return false;
            }

            if (parseInt($('#nome').val().substr(9, 2)) > 0) {
                $.ajax({url: 'TaxaIndividualAjax', type: 'GET', async: false,
                    data: {
                        tipo: 5,
                        categoria: $('#nome').val().substr(1, 2),
                        matricula: $('#nome').val().substr(4, 4),
                        dependente: $('#nome').val().substr(9, 2),
                        taxa: $('#taxa').val()
                    }
                }).success(function(retorno) {
                    ret = retorno;
                });
                if (ret != "OK") {
                    alert('Dependente não autorizado para esta Taxa!');
                    mostraBotoes()
                    return false;
                }
            }

            $.ajax({url: 'TaxaIndividualAjax', type: 'GET', async: false,
                data: {
                    tipo: 6,
                    categoria: $('#nome').val().substr(1, 2),
                    matricula: $('#nome').val().substr(4, 4)
                }
            }).success(function(retorno) {
                ret = retorno;
            });
            if (ret != "OK") {
                alert('Situacao da Pessoa nao permite lancamento de Débito!');
                mostraBotoes()
                return false;
            }

            if ($("input[name='contaLanc']:checked").val() == 'POS') {
                //validacoes de socio POS-PAGO

                $.ajax({url: 'TaxaIndividualAjax', type: 'GET', async: false,
                    data: {
                        tipo: 7,
                        categoria: $('#nome').val().substr(1, 2)
                    }
                }).success(function(retorno) {
                    ret = retorno;
                });
                if (ret != "OK") {
                    if ($('#app6183').val() == "true") {
                        if (!confirm('A categoria não está autorizada para o lançamento de Débito! Deseja continuar com a transação?')) {
                            mostraBotoes()
                            return false;
                        }
                    } else {
                        alert('Categoria não autorizada para lançamento de Débito!');
                        mostraBotoes()
                        return false;
                    }
                }

                $.ajax({url: 'TaxaIndividualAjax', type: 'GET', async: false,
                    data: {
                        tipo: 8,
                        categoria: $('#nome').val().substr(1, 2),
                        matricula: $('#nome').val().substr(4, 4)
                    }
                }).success(function(retorno) {
                    ret = retorno;
                });
                if (ret != "OK") {
                    if ($('#app6182').val() == "true") {
                        if (!confirm('O título se encontra com carnes vencidos em aberto! Deseja continuar com a transação?')) {
                            mostraBotoes()
                            return false;
                        }
                    } else {
                        alert('O título se encontra com carnes vencidos em aberto, impossibilitando conclusão da transação!');
                        mostraBotoes()
                        return false;
                    }
                }

                $.ajax({url: 'MovimentoAjax', type: 'GET', async: false,
                    data: {
                        tipo: 1,
                        matricula: $('#nome').val().substr(4, 4),
                        categoria: $('#nome').val().substr(1, 2),
                        valor: valor,
                        origem: '6180',
                        nome: $('#nome').val()
                    }
                }).success(function(retorno) {
                    ret = retorno;
                });

                if (ret == 'PROBLEMA') {
                    if ($('#app6187').val() == 'true') {
                        if (!confirm('O valor do Carnê vai ultrapassar o limite máximo estipulado para Taxas Individuais e Cheques Pré-Datados. Deseja continuar com a transação?')) {
                            mostraBotoes()
                            return false;
                        }
                    } else {
                        alert('O valor do Carnê vai ultrapassar o limite máximo estipulado para Taxas Individuais e Cheques Pré-Datados!');
                        mostraBotoes()
                        return false;
                    }
                }
            } else {
                //validacoes de socio PRE-PAGO

                $.ajax({url: 'TaxaIndividualAjax', type: 'GET', async: false,
                    data: {
                        tipo: 12,
                        matricula: $('#nome').val().substr(4, 4),
                        categoria: $('#nome').val().substr(1, 2),
                        dependente: $('#nome').val().substr(9, 2),
                        valor: valor
                    }
                }).success(function(retorno) {
                    ret = retorno;
                });

                if (ret == 'PROBLEMA') {
                    alert('Não há saldo suficiente para lançamento na conta pré-paga!');
                    mostraBotoes()
                    return false;
                }
            }

        } else {
            //validacoes de FUNCIONARIO
            var nome = $('#nome').val();
            $.ajax({url: 'TaxaIndividualAjax', type: 'GET', async: false,
                data: {
                    tipo: 14,
                    codigo: nome.substr(1, nome.indexOf("-") - 2),
                    valor: valor
                }
            }).success(function(retorno) {
                ret = retorno;
            });

            if (ret == 'PROBLEMA') {
                alert('Não há saldo suficiente para lançamento!');
                mostraBotoes()
                return false;
            }
        }

        //em qualquer situacao (pos, pre, socio e funci) verifica se eh abastecimento e pede a vinculacao com o carro.
        if ($("#carro").val() == '') {

            if (ehPosto()) {
                $("#pesqCarro").show();
                carregaCarro();
                return false;
            }
        }

        senhaAtualizacao();

    }

    function senhaAtualizacao() {
        if ($('#app6186').val() == 'true') {
            if (confirm('Deseja que o lançamento seja autenticado por senha?')) {
                $('#senhaSocio').val('');
                $('#divSenha').show();
                $('#senhaSocio').focus();
            } else {
                finalizaAtualizacao();
            }
        } else {
            $('#senhaSocio').val('');
            $('#divSenha').show();
            $('#senhaSocio').focus();
        }

    }

    function finalizaAtualizacao() {
        if ($("input[name='contaLanc']:checked").val() == 'PRE') {
            var msg = 'Confirma a Inclusão da seguinte Taxa: \n\n Cobrar de: ' + $("#nome").val() + '\n Referente a: ' + $("#taxa option:selected").text() + '\n Valor: R$ ' + $.format.number(parseFloat($("#valor").val().replace(".", "").replace(",", ".")), '#,##0.00') + '\n Débito na conta PRÉ-PAGA';
        } else {
            var msg = 'Confirma a Inclusão da seguinte Taxa: \n\n Cobrar de: ' + $("#nome").val() + '\n Referente a: ' + $("#taxa option:selected").text() + '\n Valor: ' + $("#qtVezes").val() + 'x R$ ' + $.format.number(parseFloat($("#valor").val().replace(".", "").replace(",", ".")), '#,##0.00') + '\n Será cobrado a partir de: ' + $("#mes").val() + '/' + $("#ano").val();
        }

        if (!confirm(msg)) {
            mostraBotoes();
            return false;
        }

        $("#taxa").removeAttr('disabled');
        $("#valor").removeAttr('disabled');
        $("#qtVezes").removeAttr('disabled');
        $("#nome").removeAttr('disabled');
        $("#mes").removeAttr('disabled');
        $("#ano").removeAttr('disabled');
        $("#ano").removeAttr('disabled');
        $("#precoComb").removeAttr('disabled');
        $("#litrosComb").removeAttr('disabled');
        $("#valorComb").removeAttr('disabled');

        document.forms[0].submit();

    }

</script>  


<body class="internas">

    <script type="text/javascript" language="JavaScript" src="js/format.js"></script>

    <%@include file="menuCaixa.jsp"%>

    <div class="divisoria"></div>
    <div id="titulo-subnav">Taxas Individuais</div>
    <div class="divisoria"></div>

    <form action="c" method="POST">
        <input type="hidden" name="app" id="app" value="6180">
        <input type="hidden" name="acao" id="acao" value="grava">
        <input type="hidden" name="app6181" id="app6181" value="${app6181}">
        <input type="hidden" name="app6182" id="app6182" value="${app6182}">
        <input type="hidden" name="app6183" id="app6183" value="${app6183}">
        <input type="hidden" name="app6185" id="app6185" value="${app6185}">
        <input type="hidden" name="app6186" id="app6186" value="${app6186}">
        <input type="hidden" name="app6187" id="app6187" value="${app6187}">
        <input type="hidden" name="carro" id="carro" value="">
        <input type="hidden" name="senha" id="senha" value="">
        <input type="hidden" name="tipoPessoa" id="tipoPessoa" value="">


        <div style="position:absolute; top:160px;left:5px;">    
            <fieldset class="field-set legendaFrame recuoPadrao" style="width:430px;height: 127px">
                <div style="position:absolute; top:12px;left:15px;">    
                    <p class="legendaCodigo MargemSuperior0">Taxa</p>
                    <div class="selectheightnovo">
                        <select name="taxa" id="taxa" class="campoSemTamanho alturaPadrao" style="width:320px;" onchange="trocaTaxa()">
                            <c:forEach var="tx" items="${taxas}">
                                <option value="${tx.id}" <c:if test='${tx.id == taxa}'>selected</c:if>>${tx.descricao}</option>
                            </c:forEach>
                        </select>
                    </div>        
                </div>  
                <div style="position:absolute; top:12px;left:350px;">    
                    <p class="legendaCodigo MargemSuperior0" >Valor</p>
                    <input type="text" name="valor" id="valor" class="campoSemTamanho alturaPadrao larguraNumero" >
                </div>  
                <div style="position:absolute; top:65px;left:12px;">    
                    <p class="legendaCodigo MargemSuperior0" >Nome</p>
                    <input type="text" name="nome" id="nome" class="campoSemTamanho alturaPadrao" disabled style="width:360px;" >
                </div>  
                <div style="position:absolute; top:80px;left:395px;">    
                    <input class="botaobuscainclusao" type="button" onclick="return pesquisa()" value="" title="Consultar" />
                </div>  

            </fieldset>
        </div>  

        <div style="position:absolute; top:160px;left:450px;">    
            <fieldset class="field-set legendaFrame recuoPadrao" style="width:145px;height: 127px">
                <div style="position:absolute; top:12px;left:12px;">    
                    <p class="legendaCodigo MargemSuperior0" >Cobrar em (Mês/Ano)</p>
                    <input type="text" name="mes" id="mes" class="campoSemTamanhoNegrito alturaPadrao larguraNumeroPequeno" disabled value="${mes}" style="text-align:center;">
                    <b> &nbsp; / &nbsp; </b>
                    <input type="text" name="ano" id="ano" class="campoSemTamanhoSemMargemNegrito alturaPadrao larguraNumero" disabled value="${ano}" style="text-align:center;">
                </div>  
                <div style="position:absolute; top:65px;left:12px;">    
                    <p class="legendaCodigo MargemSuperior0" >Repetir Cobrança</p>
                    <input type="text" name="qtVezes" id="qtVezes" maxlength="2" class="campoSemTamanhoNegrito alturaPadrao larguraNumero" <c:if test='${app6181!="true"}'>disabled</c:if> value="1" style="text-align:center;"> Vez(es)
                </div>  
            </fieldset>
        </div>  


        <div id="divTipoConta" style="position:absolute; top:160px;left:620px;">    
            <fieldset class="field-set legendaFrame recuoPadrao" style="width:145px;height: 127px">
                <div style="position:absolute; top:25px;left:32px;">    
                    <input type="radio" id="contaLanc" name="contaLanc" checked value="POS"/>Pós-pago<br>
                    <br>
                    <input type="radio" id="contaLanc" name="contaLanc" value="PRE"/>Pré-pago<br>
                    <div id="textoSaldoPre" style="color:green;font-size:12px; font-weight: bold;font-family:MS Sans Serif; margin-left: 6px; "></div>                    
                </div>  
            </fieldset>
        </div>  

        <div id="divCombustivel" style="position:absolute; top:300px;left:5px;">    
            <fieldset class="field-set legendaFrame recuoPadrao" style="width:590px;height: 68px">
                <div style="position:absolute; top:12px;left:15px;">    
                    <p class="legendaCodigo MargemSuperior0">Combustível</p>
                    <div class="selectheightnovo">
                        <select name="combustivel" id="combustivel" class="campoSemTamanho alturaPadrao" style="width:250px;" onchange="trocaCombustivel()">
                        <c:forEach var="cb" items="${combustiveis}">
                            <option value="${cb.id}">${cb.descricao}</option>
                        </c:forEach>
                        </select>
                    </div>        
                    <c:forEach var="cb" items="${combustiveis}">
                        <input type="hidden" name="valComb${cb.id}" id="valComb${cb.id}" value="${cb.valor}">
                    </c:forEach>
                </div>  
                <div style="position:absolute; top:12px;left:280px;">    
                    <p class="legendaCodigo MargemSuperior0" >Preço</p>
                    <input type="text" name="precoComb" id="precoComb" readonly class="campoSemTamanho alturaPadrao larguraNumero" >
                </div>  
                <div style="position:absolute; top:12px;left:360px;">    
                    <p class="legendaCodigo MargemSuperior0">Cálculo</p>
                    <div class="selectheightnovo">
                        <select name="calculo" id="calculo" class="campoSemTamanho alturaPadrao larguraNumero" onchange="trocaCalculo()">
                            <option value="V">Valor</option>
                            <option value="L">Litro</option>
                        </select>
                    </div>        
                </div>  
                <div style="position:absolute; top:12px;left:435px;">    
                    <p class="legendaCodigo MargemSuperior0" >Qt. Litros</p>
                    <input type="text" name="litrosComb" id="litrosComb" class="campoSemTamanho alturaPadrao larguraNumero" onblur="calculaCombustivel()" >
                </div>  
                <div style="position:absolute; top:12px;left:510px;">    
                    <p class="legendaCodigo MargemSuperior0" >Valor Total</p>
                    <input type="text" name="valorComb" id="valorComb" class="campoSemTamanho alturaPadrao larguraNumero" onblur="calculaCombustivel()">
                </div>  
            </fieldset>
        </div>  


        <div id="divBotaoSair" style="position:absolute; top:300px;left:20px;width:140px;overflow: hidden">
            <input type="button" class="botaosair" id="botaoSair" style="margin-left: -10px;" onclick="sair()" value=" " />
        </div>        
        <div id="divBotaoCancelar" style="position:absolute; top:300px;left:171px;width:140px;overflow: hidden">
            <input type="button" class="botaocancelar" id="botaoCancelar" style="margin-left: -10px;margin-top:0px" onclick="cancelar()" value=" " />
        </div>        
        <div id="divBotaoAtualizar" style="position:absolute; top:300px;left:322px;width:140px;overflow: hidden">
            <input type="button" class="botaoatualizar"  id="botaoAtualizar" style="margin-left: -10px;" onclick="atualizar()" value=" " />
        </div>        
        <div id="divBotaoObservacoes" style="position:absolute; top:300px;left:473px;width:140px;overflow: hidden">
            <input type="button" class="botaoobservacoes" id="botaoObservacoes" style="margin-left: -10px;" onclick="observacoes()" value=" " />
        </div>        


        <!--
            ************************************************************************
            ************************************************************************
            ************************************************************************
                                    CAMPO DE OBSERVACAO
            ************************************************************************
            ************************************************************************
            ************************************************************************
        -->

        <div id="campoObservacao" style="overflow:auto;left: 25%;top: 20%;position: fixed;width: 500px; height:400px;">
            <table style="background:#fff">
                <tr>
                    <td>
                        <div class="divisoria"></div>
                        <div id="titulo-subnav">Observações do Caixa</div>
                        <div class="divisoria"></div>

                        <table class="fmt" align="left" style="margin-left:0px;">
                            <tr>
                                <td colspan="2">
                                    <textarea class="campoSemTamanho" id="observacao" rows="10" cols="80" name="observacao"></textarea><br>
                                </td>
                            </tr>
                            <tr>
                                <td>
                                    <br>
                                    <input style="margin-left:15px;" type="button" id="cmdatualizar" name="cmdPesquisa" class="botaoatualizar" onclick="fechaObservacao()" />
                                </td>
                                <td>
                                    <br>
                                    <input type="button" style="margin-top:-5px;" id="cmdcancelar" name="cmdPesquisa" class="botaocancelar" onclick="cancelaObservacao()" />
                                </td>
                            </tr>
                        </table>  
                        <br>
                    </td>
                </tr>
            </table>
        </div>                

        <!--
            ************************************************************************
            ************************************************************************
            ************************************************************************
                               TABELA DE SELECAO DE SOCIO
            ************************************************************************
            ************************************************************************
            ************************************************************************
        -->
        <div id="pesqSocio" style="overflow:auto;left: 25%;top: 20%;position: fixed;width: 700px; height:400px;">
            <table style="background:#fff">
                <tr>
                    <td>
                        <div class="divisoria"></div>
                        <div id="titulo-subnav">Seleção de Sócio</div>
                        <div class="divisoria"></div>

                        <table class="fmt" align="left" >
                            <tr>
                                <td>
                                    <p class="legendaCodigo MargemSuperior0" >Categoria</p>
                                    <input type="text" id="categoriaSocio" name="categoriaSocio" class="campoSemTamanho alturaPadrao larguraNumero" value="">
                                </td>

                                <td>
                                    <p class="legendaCodigo MargemSuperior0" >Matricula</p>
                                    <input type="text" id="matriculaSocio" name="matriculaSocio" class="campoSemTamanho alturaPadrao larguraNumero" value="">
                                </td>
                                <td>
                                    <p class="legendaCodigo MargemSuperior0" >Nome</p>
                                    <input type="text" id="nomeSocio" name="nomeSocio" class="campoSemTamanho alturaPadrao" style="width:300px" value="">
                                </td>
                                <td >    
                                    <input type="button" class="botaobuscainclusao" style="margin-top:20px" onclick="carregaSocio()" value="" title="Consultar" />
                                </td>
                            </tr>
                        </table>
                        <br><br><br>
                        <table id="tabelaSocio" align="left" style="margin-left:15px;">
                            <thead>
                                <tr class="odd">
                                    <th scope="col" class="nome-lista">Nome</th>
                                    <th scope="col" align="left">Título</th>
                                    <th scope="col" align="left">Tp Cadastro</th>
                                    <th scope="col" align="left">Categoria</th>
                                    <th scope="col" align="left">Tp Categoria</th>
                                    <th scope="col" align="left">Carteira</th>
                                    <th scope="col" align="left">Autorização</th>
                                    <th scope="col" align="left">Foto</th>
                                </tr>	
                            </thead>
                            <tbody>
                            </tbody>
                        </table>  
                        <br>
                        <input style="margin-left:15px;" type="button" id="cmdcancelar" name="cmdPesquisa" class="botaocancelar" onclick="cancelaPesquisaSocio()" />
                    </td>
                </tr>
            </table>
        </div>                



        <!--
            ************************************************************************
            ************************************************************************
            ************************************************************************
                               TABELA DE SELECAO DE CARRO
            ************************************************************************
            ************************************************************************
            ************************************************************************
        -->

        <div id="pesqCarro" style="overflow:auto;left: 25%;top: 20%;position: fixed;width: 700px; height:400px;">
            <table style="background:#fff">
                <tr>
                    <td>
                        <div class="divisoria"></div>
                        <div id="titulo-subnav">Seleção de Carro</div>
                        <div class="divisoria"></div>

                        <table id="tabelaCarro" align="left" style="margin-left:15px;">
                            <thead>
                                <tr class="odd">
                                    <th scope="col" align="center">Doc</th>
                                    <th scope="col" class="nome-lista">Marca</th>
                                    <th scope="col" align="center">Modelo</th>
                                    <th scope="col" align="center">Placa</th>
                                    <th scope="col" align="center">Documento</th>
                                </tr>	
                            </thead>
                            <tbody>
                            </tbody>
                        </table>  
                        <br>
                        <input style="margin-left:15px;" type="button" id="cmdcancelar" name="cmdPesquisa" class="botaocancelar" onclick="cancelaPesquisaCarro()" />
                    </td>
                </tr>
            </table>
        </div>                

        <!--
            ************************************************************************
            ************************************************************************
            ************************************************************************
                                       S E N H A 
            ************************************************************************
            ************************************************************************
            ************************************************************************
        -->
        <div id="divSenha" style="overflow:auto;left: 25%;top: 20%;position: fixed;width: 500px; height:400px;">
            <table style="background:#fff">
                <tr>
                    <td>
                        <div class="divisoria"></div>
                        <div id="titulo-subnav">Senha</div>
                        <div class="divisoria"></div>

                        <table class="fmt" align="left" >
                            <tr>
                                <td>
                                    <p class="legendaCodigo MargemSuperior0" >Senha</p>
                                    <input type="password" id="senhaSocio" name="senhaSocio" class="campoSemTamanhoNegrito" style="width: 100px; height:30px;"value="">
                                </td>
                            </tr>
                        </table>
                        <br><br><br><br>
                        <table class="fmt" align="left" >
                            <tr>
                                <td>
                                    <input type="button" id="cmdAtualizarSenha" name="cmdAtualizarSenha"   class="botaoatualizar" onclick="confirmaSenha()" />
                                </td>
                                <td>
                                    <input style="margin-left:15px;margin-top: -5px;" type="button" id="cmdCancelarSenha" name="cmdCancelarSenha" class="botaocancelar" onclick="cancelaSenha()" />
                                </td>
                            </tr>
                        </table>

                    </td>
                </tr>
            </table>
        </div>         


        <!--
            ************************************************************************
            ************************************************************************
            ************************************************************************
                                     TIPO DE PESQUISA
            ************************************************************************
            ************************************************************************
            ************************************************************************
        -->

        <div id="tipoPesquisa" style="overflow:auto;left: 25%;top: 20%;position: fixed;width: 420px; height:400px;" >
            <table style="background:#fff">
                <tr>
                    <td>
                        <div class="divisoria"></div>
                        <div id="titulo-subnav">Tipo de Pesquisa</div>
                        <div class="divisoria"></div>
                        <table class="fmt" align="left" align="left">
                            <tr>
                                <td>
                                    <div id="radioPlaca"><input type="radio" id="tipoPesq" name="tipoPesq" checked value="PLACA"/>Placa do Carro<br></div>
                                    <input type="radio" id="tipoPesq" name="tipoPesq" value="SOCIO"/>Sócio<br>
                                    <input type="radio" id="tipoPesq" name="tipoPesq" value="FUNCIONARIO"/>Funcionário<br>
                                </td>
                                <td>
                                    &nbsp;
                                </td>
                            </tr>
                            <tr>
                                <td>
                                    <br>
                                    <input type="button" id="cmdTipoPesquisa" name="cmdTipoPesquisa" class="botaoatualizar" value=" " onclick="defineTipoPesquisa()" value="" />
                                </td>
                                <td>
                                    <br>
                                    <input type="button" style="margin-left:15px;margin-top: 0px;" id="cmdCancelaTipoPesquisa" name="cmdCancelaTipoPesquisa" class="botaocancelar" value=" " onclick="cancelaTipoPesquisa()" value="" />
                                </td>
                            </tr>
                        </table>  
                    </td>
                </tr>
            </table>
        </div>        


        <!--
            ************************************************************************
            ************************************************************************
            ************************************************************************
                             TABELA DE SELECAO DE CARRO E SOCIO
            ************************************************************************
            ************************************************************************
            ************************************************************************
        -->
        <div id="pesqCarroSocio" style="overflow:auto;left: 25%;top: 20%;position: fixed;width: 700px; height:400px;">
            <table style="background:#fff">
                <tr>
                    <td>
                        <div class="divisoria"></div>
                        <div id="titulo-subnav">Seleção de Carro e Sócio</div>
                        <div class="divisoria"></div>

                        <table class="fmt" align="left" >
                            <tr>
                                <td>
                                    <p class="legendaCodigo MargemSuperior0" >Placa</p>
                                    <input type="text" id="txtPlaca" name="txtPlaca" class="campoSemTamanho alturaPadrao larguraNumero" value="">
                                </td>
                                <td >    
                                    <input type="button" class="botaobuscainclusao" style="margin-top:20px" onclick="carregaCarroSocio()" value="" title="Consultar" />
                                </td>
                            </tr>
                        </table>
                        <br><br><br>
                        <table id="tabelaCarroSocio" align="left" style="margin-left:15px;">
                            <thead>
                                <tr class="odd">
                                    <th scope="col" class="nome-lista">Tit/Mat</th>
                                    <th scope="col" class="nome-lista">Tipo</th>
                                    <th scope="col" class="nome-lista">Nome</th>
                                    <th scope="col" align="center">Doc</th>
                                    <th scope="col" class="nome-lista">Marca</th>
                                    <th scope="col" align="center">Modelo</th>
                                    <th scope="col" align="center">Placa</th>
                                    <th scope="col" align="center">Documento</th>
                                </tr>	
                            </thead>
                            <tbody>
                            </tbody>
                        </table>  
                        <br>
                        <input style="margin-left:15px;" type="button" id="cmdcancelar" name="cmdPesquisa" class="botaocancelar" onclick="cancelaPesquisaCarroSocio()" />
                    </td>
                </tr>
            </table>
        </div>                

        <!--
            ************************************************************************
            ************************************************************************
            ************************************************************************
                               TABELA DE SELECAO DE FUNCIONARIO
            ************************************************************************
            ************************************************************************
            ************************************************************************
        -->
        <div id="pesqFuncionario" style="overflow:auto;left: 25%;top: 20%;position: fixed;width: 700px; height:400px;">
            <table style="background:#fff">
                <tr>
                    <td>
                        <div class="divisoria"></div>
                        <div id="titulo-subnav">Seleção de Funcionario</div>
                        <div class="divisoria"></div>

                        <table class="fmt" align="left" >
                            <tr>
                                <td>
                                    <p class="legendaCodigo MargemSuperior0" >Matricula</p>
                                    <input type="text" id="matriculaFuncionario" name="matriculaFuncionario" class="campoSemTamanho alturaPadrao larguraNumero" value="">
                                </td>
                                <td>
                                    <p class="legendaCodigo MargemSuperior0" >Nome</p>
                                    <input type="text" id="nomeFuncionario" name="nomeFuncionario" class="campoSemTamanho alturaPadrao" style="width:300px" value="">
                                </td>
                                <td >    
                                    <input type="button" class="botaobuscainclusao" style="margin-top:20px" onclick="carregaFuncionario()" value="" title="Consultar" />
                                </td>
                            </tr>
                        </table>
                        <br><br><br>
                        <table id="tabelaFuncionario" align="left" style="margin-left:15px;">
                            <thead>
                                <tr class="odd">
                                    <th scope="col" class="nome-lista">Nome</th>
                                    <th scope="col" align="left">Cargo</th>
                                    <th scope="col" align="left">Setor</th>
                                    <th scope="col" align="left">Foto</th>
                                </tr>	
                            </thead>
                            <tbody>
                            </tbody>
                        </table>  
                        <br>
                        <input style="margin-left:15px;" type="button" id="cmdcancelar" name="cmdPesquisa" class="botaocancelar" onclick="cancelaPesquisaFuncionario()" />
                    </td>
                </tr>
            </table>
        </div>                


    </form>

</body>
</html>
