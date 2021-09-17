<%@page contentType="text/html" pageEncoding="utf-8"%>
<!DOCTYPE html>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@taglib prefix="joda" uri="http://www.joda.org/joda/time/tags"%>
<fmt:setLocale value="pt_BR" />

<html>
    <!-- ${pageContext.request.servletPath} -->
    <head>
        <title>Iate Clube de Brasília</title>
        <meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
        <link rel="icon" href="imagens/icones/favicon.ico" type="image/x-icon" />
        <link rel="shortcut icon" href="imagens/icones/favicon.ico" type="image/x-icon" />
        <script src="js/jquery-1.9.1.min.js" type="text/javascript"></script>
        <script src="js/jquery.qtip.min.js" type="text/javascript"></script>
        <link rel="stylesheet" type="text/css" href="css/jquery.qtip.min.css" />
        <script type="text/javascript" src="js/jquery.transit.min.js"></script>
        <script type="text/javascript" src="js/jss.js"></script>
        <style>
            body {
                margin: 0px; /* Sobrescreve as margens padrão dos browsers */
                background: url(images/tenis/tennis-wallpaper.jpg) top right no-repeat;
                background-size: 100% auto;
                background-attachment: fixed;
            }
            
            #header {
                width: 100%;
                position: absolute;
                top: 0px;
                height: 150px;
            }
            
            #header p {
                font-family: Arial;
                font-size: 350%;
                font-weight: bold;
                color: #FFF;
                text-shadow: 3px 3px 3px rgba(0, 0, 0, 0.5);
            }
            
            #header hr {
                color: #FFF;
                background-color: #EEE;
                opacity: 0.8;
                margin-top: 0em;
                margin-bottom: 0em;
                margin-left: 10px;
                margin-right: 10px;
                height: 4px;
            }
            
            #title {
                float: left;
                margin-top: 0em;
                margin-bottom: -14px;
                margin-left: 30px;
            }
            
            #broadcast {
                font-size: 200% !important;
                float: left;
                margin-top: 0em;
                margin-bottom: 0em;
                margin-left: 30px;
            }

            
            #date {
                float: right;
                margin-top: 0em;
                margin-bottom: 0em;
                margin-right: 30px;
            }
            
            #time {
                float: right;
                margin-top: 0em;
                margin-bottom: -14px;
                margin-right: 30px;
            }

            .clear-float {
                clear: both;
            }

            #content-wrapper {
                width: 100%;
                position: absolute;
                top: 150px;
                bottom: 0px;
            }

            #court {
                margin-left: 30px;
                margin-right: 30px;
                color: #FFF;
                border-radius: 75px;
                border-style: groove;
                border-width: thick;
                background-color: rgba(0, 0, 0, 0.5);
                overflow: hidden;
                position: relative;
                box-shadow: 12px 12px 8px 0px rgba(0, 0, 0, 0.8);
            }

            #schedule-button {
                z-index: 100;
                position: absolute;
                right: 0px;
                top: 0px;
                margin-top: 40px;
                margin-right: 40px;
                height: 80px;
                width: 180px;
                border-radius: 10px;
                background-color: #CCC;
                box-shadow: 10px 10px 5px 0px #000;
                color: #000;
                font-family: Arial;
                font-size: 150%;
                font-weight: bold;
                border-style: outset;
            }
            
            #timeout {
                -webkit-appearance: none;
                margin-top: 0px;
                margin-bottom: -2px;
                margin-left: 10px;
                margin-right: auto;
                height: 6px;
                opacity: 0.8;
                border-style: inset;
                border-width: 1px;
            }
            progress::-webkit-progress-bar { background: #EEE; }
            progress::-webkit-progress-value { background: blue; }
            
            .active-button {
                opacity: 1.0;
            }
            
            .active-button:hover {
                cursor: pointer;
            }
            
            .inactive-button {
                opacity: 0.3;
            }
            
            .inactive-button:hover {
                cursor: default;
            }

            #green-light, #yellow-light, #red-light {
                position: absolute;
                left: 10px;
                top: 10px;
                margin-top: 0px;
                margin-left: 0px;
                height: 120px;
                width: 120px;
                display: block;
                border-radius:100px;
                border-style: groove;
                border-width: thick;
                box-shadow: 12px 12px 4px 0px rgba(0, 0, 0, 0.3);
                font-family: Arial;
                font-size: 800%;
                font-weight: bold;
                color: #FFF;
                text-align: center;
                line-height: 120px;
            }
            
            #red-light:hover {
                cursor: pointer;
            }

            #green-light {
                background: -webkit-linear-gradient(-45deg, rgba(48,224,113,1) 0%,rgba(27,181,81,1) 24%,rgba(6,128,49,1) 50%,rgba(25,159,72,1) 79%,rgba(30,192,87,1) 100%); /* Chrome10+,Safari5.1+ */
            }

            #yellow-light {
                background: -webkit-linear-gradient(-45deg, rgba(231,229,87,1) 0%,rgba(211,205,31,1) 24%,rgba(150,145,6,1) 50%,rgba(188,183,28,1) 79%,rgba(223,216,37,1) 100%); /* Chrome10+,Safari5.1+ */
            }

            #red-light {
                background: -webkit-linear-gradient(-45deg, rgba(219,139,121,1) 0%,rgba(203,82,57,1) 24%,rgba(147,42,21,1) 50%,rgba(183,71,49,1) 79%,rgba(206,94,72,1) 100%); /* Chrome10+,Safari5.1+ */
            }


            #court-name {
                margin-left: 165px;
                margin-top: 15px;
                font-family: Arial;
                font-size: 620%;
                font-weight: bold;
                color: #FFF;
                text-shadow: 12px 12px 4px rgba(0, 0, 0, 0.3);
            }

            #court-restriction {
                color: #CCC;
                font-style: italic;
                margin-top: -120px;
                margin-left: 180px;
                font-family: Arial;
                font-weight: bold;
                font-size: 350%;
                text-shadow: 12px 12px 4px rgba(0, 0, 0, 0.3);
            }

            #court-interval {
                right: 40px;
                top: 100px;
                text-align: right;
                position: absolute;
                font-family: Arial;
                font-size: 200%;
                font-weight: bold;
                color: #CCC;
                text-shadow: 12px 12px 4px rgba(0, 0, 0, 0.3);
            }
            
            #card-wrapper {
                color: #000;
                position: absolute;
                top: 150px;
                border-spacing: 30px;
                width: 100%;
                display: table;
                table-layout: fixed;
            }
            
            #court-status {
                color: #FFF;
                margin-bottom: 1px;
                margin-right: 12px;
                font-family: Arial;
                font-weight: bold;
                font-size: 620%;
                position: absolute;
                bottom: 0px;
                right: 30px;
                text-shadow: 12px 12px 4px rgba(0, 0, 0, 0.3);
            }
            
            .player-card {
                display: table-cell;
                overflow: hidden;
                position: relative;
                border-radius: 20px;
                background-color: white;
                opacity: 0.3;
                box-shadow: 10px 10px 5px 0px #000;
                z-index: 100;
            }
            
            .player-title {
                font-family: Arial;
                font-weight: bold;
                font-size: 160%;
                margin-right: 30px;
                text-align: right;
                margin-top: 20px;
                margin-bottom: 0px;
            }
            
            .player-form {
                margin-left: 10px;
                font-weight: bold;
                font-family: Arial;
                font-size: 100%;
            }
            
            .login-pane fieldset {
                border-left: none; 
                border-right: none;
                border-bottom: none;
            }
            
            .login-pane legend {
                 font-size: 120%;
                 font-family: Arial;
                 font-weight: bold;
            }
            
            .login-pane {
            }
            
            .info-pane {
            }
            
            .player-avatar {
                position: absolute;
                margin: auto;
                left: 0;
                right: 0;
                top: 60px;
                width: 120px;
                max-height: 160px;
            }
            
            .player-name {
                position: absolute;
                left: 0;
                right: 0;
                bottom: 30px;
                font-family: Arial;
                font-weight: bold;
                font-size: 220%;
                font-style: italic;
                text-align: center;
            }
            
            .buscar-jogador {
                position: absolute;
                right: 6px;
                bottom: 5px;
                width: 32px;
                height: 32px;
            }
            
            .buscar-jogador:hover {
                cursor: pointer;
            }
            
            .buscar-socio, .buscar-convidado {
                float: right;
                width: 32px;
                height: 32px;
            }
            
            .buscar-socio:hover, .buscaro-convidado:hover {
                cursor: pointer;
            }

            .delete-player {
                position: absolute; 
                top: 8px; 
                right: 8px;
                width: 32px;
                height: 32px;
            }
            
            .delete-player:hover {
                cursor: pointer;
            }
            
            .qtip-titlebar {
                font-family: Arial;
                font-weight: bold;
                font-size: 200%;
                height: 20px;
            }
            
            .qtip-content {
                font-family: Arial;
                font-size: 180%;
            }
            
            .selectable:hover {
                background-color: #D95252;
                cursor: pointer;
            }
            
            .selectable {
                line-height: 20px;
                
            }
            
            .selectable td {
                padding-left: 10px;
            }
        </style>
        <script type="text/javascript">
            
            var timer = {
                interval : 200
            };
            
            function timerUpdate() {
                if (timer.timeout !== undefined) {
                    if (timer.now === undefined) timer.now = timer.timeout;
                    else if (timer.now > 0) timer.now = timer.now - timer.interval;
                    $('#timeout').attr('max', timer.timeout * 1.0);
                    $('#timeout').attr('value', timer.now * 1.0);
                    if ((timer.now / timer.timeout) > 0.2) jss('progress::-webkit-progress-value', { background: 'blue' });
                    else jss('progress::-webkit-progress-value', { background: 'red' });
                }
                if (timer.now === undefined || timer.now > 0) setTimeout('timerUpdate()', timer.interval);
                if (timer.now !== undefined && timer.now <= 0) back();
            }
            
            function back() {
                <c:if test="${admin}">
                    window.location.replace('c?app=2370');
                </c:if>
                <c:if test="${not admin}">
                    window.location.replace('tenis');
                </c:if>
            }
            
            var comm = true;
            var exiting = false;

            function ajaxUpdate() {
                $.getJSON('ajax/marcar', {
                    "id": "${quadra.id}"
                    }, function(quadra) {
                        $('#date').text(quadra.date);
                        $('#time').html(quadra.time.substring(0, 2) + '<span class="blink">:</span>' + quadra.time.substring(3, 5));
                        blink();
                        
                        if (comm === false) {
                            comm = true;
                            broadcast();
                        }
                        
                        if (timer.timeout === undefined) timer.timeout = quadra.timeout * 1000;

                        var s = '';
                        s += '<p id="court-name">' + quadra.nome + '</p>';

                        if (jogo.tipo === 'SIMPLES') {
                            $('#player1').css('opacity', '1.0').find('input').attr('disabled', null);
                            $('#player2').css('opacity', '1.0').find('input').attr('disabled', null);
                            $('#player3').css('opacity', '0.3').find('input').attr('disabled', 'disabled');
                            $('#player4').css('opacity', '0.3').find('input').attr('disabled', 'disabled');
                            if (jogadores.player3 !== undefined) delete jogadores.player3;
                            if (jogadores.player4 !== undefined) delete jogadores.player4;
                            s += '<p id="court-restriction">Jogo Simples</p>';
                            s += '<p id="court-interval">${horario}</p>';
                        }
                        
                        if (jogo.tipo === 'DUPLAS') {
                            $('#player1').css('opacity', '1.0').find('input').attr('disabled', null);
                            $('#player2').css('opacity', '1.0').find('input').attr('disabled', null);
                            $('#player3').css('opacity', '1.0').find('input').attr('disabled', null);
                            $('#player4').css('opacity', '1.0').find('input').attr('disabled', null);
                            s += '<p id="court-restriction">Jogo de Duplas</p>';
                            s += '<p id="court-interval">${horario}</p>';
                        }
                        
                        if (jogo.tipo === null) {
                            $('#player1').css('opacity', '0.3').find('input').attr('disabled', 'disabled');
                            $('#player2').css('opacity', '0.3').find('input').attr('disabled', 'disabled');
                            $('#player3').css('opacity', '0.3').find('input').attr('disabled', 'disabled');
                            $('#player4').css('opacity', '0.3').find('input').attr('disabled', 'disabled');
                            s = s + '<p id="court-restriction"></p>';
                        }
                        
                        if (jogo.pronto) {
                            s = s + '<div id="schedule-button" class="active-button" onclick="save();" onmousedown="$(this).css(\'border-style\', \'inset\');">';
                            s = s + '<p style="text-align: center;">Salvar Jogo</p></div>';
                        } else
                            s = s + '<div id="schedule-button" class="inactive-button"><p style="text-align: center;">Salvar Jogo</p></div>';
                        
                        s += '<div id="red-light" title="Excluir Jogo" onclick="excluir();">X</div>';
                            
                        $('#court-info').empty().append(s);   
                    }
                ).error(function() {
                    broadcast('Problemas de comunicação!', 'red');
                    comm = false;
                    $('#date').text('');
                    $('#time').text('');
                    $('#player1').css('opacity', '0.3').find('input').attr('disabled', 'disabled');
                    $('#player2').css('opacity', '0.3').find('input').attr('disabled', 'disabled');
                    $('#player3').css('opacity', '0.3').find('input').attr('disabled', 'disabled');
                    $('#player4').css('opacity', '0.3').find('input').attr('disabled', 'disabled');
                });
                
                if (!exiting) setTimeout('ajaxUpdate()', 1000);
            }
            
            function excluir() {
                if (exiting) return;
                $.ajax({
                    type       : 'POST',
                    url        : 'ajax/excluir',
                    data       : {id: ${jogo.id}},
                    success    : function(response) {
                        if (response.erro !== undefined) broadcast(response.erro, 'red');
                        else broadcast();
                        if (response.info !== undefined) {
                            broadcast(response.info, 'red');
                            timer.timeout = 4000;
                            delete timer.now;
                            exiting = true;
                            timerUpdate();
                        }
                    }
                });

            }

            function resizeCourt() {
                $('#court').height(Math.floor($(window).height() - $('#header').height() - 30));
                $('#card-wrapper').height(Math.floor($('#court').height() - 250));
                $('#timeout').width(Math.floor($(window).width() - 22));
            }
            
            function broadcast(message, color) {
                if (color !== undefined) $('#broadcast').attr('style', 'color: ' + color + ' !important;');
                else $('#broadcast').attr('style', 'color: #FFF important!;');
                if (message !== undefined) $('#broadcast').text(message);
                else $('#broadcast').text("");
            }
            
            var visibility = 'hidden';
            
            function toggle() {
                if (visibility === 'hidden')
                    visibility = 'visible';
                else
                    visibility = 'hidden';
                blink();
                setTimeout('toggle()', 800);
            }
    
            function blink() {
                $('.blink').css('visibility', visibility);
            }
    
            $(window).resize(function() {
                resizeCourt();
            });
            
            function onlyNumber(evt) {
                if(evt.which !== 0 && evt.which !== 8 && (evt.which < 48 || evt.which > 57))
                        evt.preventDefault();
            }
            
            var jogadores = {};
            var jogo = {
                quadra   : ${quadra.id},
                id       : ${jogo.id},
                restricao: null,
                tipo     : '${tipo}',
                pronto   : false,
                alterado : false,
                jogadores: jogadores
            };

            function auth(form) {
                var card = form.closest('.player-card');                
                var tipo = form.find('input[name="tipo"]').val();
                
                    if (tipo === 'socio') {
                        <c:if test="${admin}">
                            var matricula = parseInt(form.find('input[name="matricula"]').val());
                            var categoria = parseInt(form.find('input[name="categoria"]').val());
                            var dependente = parseInt(form.find('input[name="dependente"]').val());
                            if ((jogadores.player1 !== undefined && jogadores.player1.matricula === matricula && jogadores.player1.categoria === categoria && jogadores.player1.dependente === dependente) ||
                                (jogadores.player2 !== undefined && jogadores.player2.matricula === matricula && jogadores.player2.categoria === categoria && jogadores.player2.dependente === dependente) ||
                                (jogadores.player3 !== undefined && jogadores.player3.matricula === matricula && jogadores.player3.categoria === categoria && jogadores.player3.dependente === dependente) ||
                                (jogadores.player4 !== undefined && jogadores.player4.matricula === matricula && jogadores.player4.categoria === categoria && jogadores.player4.dependente === dependente)) {
                                broadcast('Não pode haver jogadores repetidos', 'red');
                                return;
                            }
                        </c:if>
                        <c:if test="${not admin}">
                            var login = form.find('input[name="usuario"]').val();
                            if ((jogadores.player1 !== undefined && jogadores.player1.usuario === login) ||
                                (jogadores.player2 !== undefined && jogadores.player2.usuario === login) ||
                                (jogadores.player3 !== undefined && jogadores.player3.usuario === login) ||
                                (jogadores.player4 !== undefined && jogadores.player4.usuario === login)) {
                                broadcast('Não pode haver jogadores repetidos', 'red');
                                return;
                            }
                        </c:if>
                    }
                
                if (tipo === 'convidado') {
                    var convite = parseInt(form.find('input[name ="convite"]').val());
                    if ((jogadores.player1 !== undefined && jogadores.player1.convite === convite) ||
                        (jogadores.player2 !== undefined && jogadores.player2.convite === convite) ||
                        (jogadores.player3 !== undefined && jogadores.player3.convite === convite) ||
                        (jogadores.player4 !== undefined && jogadores.player4.convite === convite)) {
                        broadcast('Não pode haver jogadores repetidos', 'red');
                        return;
                    }
                }
                
                $.ajax({
                    type   : form.attr('method'),
                    url    : form.attr('action'),
                    data   : form.serialize(),
                    success: function(response) {
                        if (response.erro !== undefined) broadcast(response.erro, 'red');
                        else broadcast();
                        if (response.jogador !== undefined) {
                            jogadores[card.attr('id')] = response.jogador;
                            if (response.jogador.foto !== undefined) card.find('.player-avatar').attr('src', response.jogador.foto);
                            else card.find('.player-avatar').attr('src', "images/tenis/avatar-default.png");
                            card.find('.player-name').text(response.jogador.nome);
                            card.transition({rotateY: '90deg'}, 100, function() { card.find('.login-pane').hide().siblings('.info-pane').show(); })
                                .transition({rotateY: '0deg'}, 150, 'ease');
                            checkPronto();
                        }
                    }
                });
            }
            
            
            function countJogadores() {
                var count = 0;
                if (jogadores.player1 !== undefined) count++;
                if (jogadores.player2 !== undefined) count++;
                if (jogadores.player3 !== undefined) count++;
                if (jogadores.player4 !== undefined) count++;
                return count;
            }
            
            function checkPronto() {
                jogo.pronto = false;
                var count = countJogadores();
                if (jogo.tipo === 'SIMPLES' && count >= 2) jogo.pronto = true;
                if (jogo.tipo === 'DUPLAS' && count >= 3) jogo.pronto = true;
                if (!jogo.alterado) jogo.pronto = false;
                return jogo.pronto;
            }
            
            function sair(button) {
                var card = button.closest('.player-card');
                card.find('input[type="text"]').val('');
                card.transition({rotateY: '90deg'}, 100, function() { card.find('.info-pane').hide().siblings('.login-pane').show(); })
                    .transition({rotateY: '0deg'}, 150, 'ease');
                delete jogadores[card.attr('id')];
                jogo.alterado = true;
                checkPronto();
            }
            
            function save() {
                if (!checkPronto()) return;
                $('#schedule-button').css('border-style', 'outset');
                $.ajax({
                    type       : 'POST',
                    url        : 'ajax/alterar',
                    data       : {jogo: JSON.stringify(jogo)},
                    success    : function(response) {
                        if (response.erro !== undefined) broadcast(response.erro, 'red');
                        else broadcast();
                        if (response.info !== undefined) {
                            jogo.pronto = false;
                            exiting = true;
                            ajaxUpdate();
                            broadcast(response.info, 'red');
                            timer.timeout = 4000;
                            delete timer.now;
                            timerUpdate();
                        }
                    }
                });
            }
    
            <c:if test="${admin}">
                var busca = {};
                
                function buscar(form) {
                    busca = {};
                    busca.tipo = form.find('input[name="tipo"]').val();
                    busca.form = form;
                    $.ajax({
                        type   : form.attr('method'),
                        url    : 'ajax/busca',
                        data   : form.serialize(),
                        success: function(response) {
                            if (response.erro !== undefined) broadcast(response.erro, 'red');
                            else {
                                broadcast();
                                busca.jogador = response.jogador;
                                if (busca.jogador.length > 0) janelaBusca();
                                else broadcast('Busca não retornou resultado', 'red');
                                }
                            }

                    });
                }
                
                function selected(i) {
                    if (busca.tipo === 'socio') {
                        busca.form.find('input[name="matricula"]').val(busca.jogador[i].matricula);
                        busca.form.find('input[name="categoria"]').val(busca.jogador[i].categoria);
                        busca.form.find('input[name="dependente"]').val(busca.jogador[i].dependente);
                    }
                    if (busca.tipo === 'convidado') {
                        busca.form.find('input[name="convite"]').val(busca.jogador[i].convite);
                    }
                    $(window).qtip('toggle', false);
                    auth(busca.form);
                }
                
                function janelaBusca() {
                    var tabela = $('#buscar-' + busca.tipo + '-window').find('tbody').empty();

                    $.each(busca.jogador, function(i) {
                        if (busca.tipo === 'socio')
                            tabela.append('<tr class="selectable" onclick="selected(' + i + ');"><td>' + this.titulo + '</td><td>' + this.descricao + '</td><td>' + this.nome + '</td></tr>');
                        if (busca.tipo === 'convidado')
                            tabela.append('<tr class="selectable" onclick="selected(' + i + ');"><td>' + this.convite + '</td><td>' + this.nome + '</td></tr>');
                    });
                    
                    $(window).qtip({
                        content: {
                            text : $('#buscar-' + busca.tipo + '-window').html(),
                            title: {
                                text  : $('#buscar-' + busca.tipo + '-title').html(),
                                button: true
                            }
                        },
                        position: {
                            my    : 'center',
                            at    : 'center'
                        },
                        show: {
                            ready: true,
                            solo : true,
                            modal: true
                        },
                        style: {
                            classes: 'qtip-light qtip-rounded'
                        },
                        events: {
                            hidden: function(event, api) { $(window).qtip('destroy'); tabela.empty(); }
                        },
                        hide : false
                    });
                }
                
            </c:if>

            function janelaJogo() {

                $(window).qtip({
                    content: {
                        text : $('#selecionar-tipo-jogo').html(),
                        title: {
                            text  : 'Selecione o tipo de jogo',
                        }
                    },
                    position: {
                        my    : 'center',
                        at    : 'center'
                    },
                    show: {
                        ready: true,
                        solo : true,
                        modal: {
                            on    : true,
                            escape: false,
                            blur  : false
                        }
                    },
                    hide : {
                        event: false
                    },
                    style: {
                        classes: 'qtip-light qtip-rounded'
                    },
                    events: {
                        hidden: function(event, api) { $(window).qtip('destroy'); }
                    },
                });
            }
            
            function selecionaJogo(botao) {
                if (botao.hasClass("botao-simples")) {
                    jogo.tipo = 'SIMPLES';
                    $(window).qtip('toggle', false);
                }
                
                if (botao.hasClass("botao-duplas")) {
                    jogo.tipo = 'DUPLAS';
                    $(window).qtip('toggle', false);
                }
            }
            
            $(document).ready(function() {
                $('.player-card').append($('#player-card').html());
                $('.player-card').each(function() {
                    $(this).find('.player-title').text($(this).attr('data-title'));
                });
                
                <c:if test="${not empty jogadores[0].socio}">
                    var form = $('#player1').find('input[name="matricula"]').parent();
                    form.find('input[name="matricula"]').val('${jogadores[0].socio.matricula}');
                    form.find('input[name="categoria"]').val('${jogadores[0].socio.idCategoria}');
                    form.find('input[name="dependente"]').val('${jogadores[0].socio.seqDependente}');
                    form.append('<input type="hidden" name="validate" value="false" />');
                    auth(form);
                    form.find('input[name="validate"]').remove();
                </c:if>
                <c:if test="${not empty jogadores[0].convite}">
                    var form = $('#player1').find('input[name="convite"]').parent();
                    form.find('input[name="convite"]').val('${jogadores[0].convite.numero}');
                    form.append('<input type="hidden" name="validate" value="false" />');
                    auth(form);
                    form.find('input[name="validate"]').remove();
                </c:if>

                <c:if test="${not empty jogadores[1].socio}">
                    var form = $('#player2').find('input[name="matricula"]').parent();
                    form.find('input[name="matricula"]').val('${jogadores[1].socio.matricula}');
                    form.find('input[name="categoria"]').val('${jogadores[1].socio.idCategoria}');
                    form.find('input[name="dependente"]').val('${jogadores[1].socio.seqDependente}');
                    form.append('<input type="hidden" name="validate" value="false" />');
                    auth(form);
                    form.find('input[name="validate"]').remove();
                </c:if>
                <c:if test="${not empty jogadores[1].convite}">
                    var form = $('#player2').find('input[name="convite"]').parent();
                    form.find('input[name="convite"]').val('${jogadores[1].convite.numero}');
                    form.append('<input type="hidden" name="validate" value="false" />');
                    auth(form);
                    form.find('input[name="validate"]').remove();
                </c:if>

                <c:if test="${not empty jogadores[2].socio}">
                    var form = $('#player3').find('input[name="matricula"]').parent();
                    form.find('input[name="matricula"]').val('${jogadores[2].socio.matricula}');
                    form.find('input[name="categoria"]').val('${jogadores[2].socio.idCategoria}');
                    form.find('input[name="dependente"]').val('${jogadores[2].socio.seqDependente}');
                    form.append('<input type="hidden" name="validate" value="false" />');
                    auth(form);
                    form.find('input[name="validate"]').remove();
                </c:if>
                <c:if test="${not empty jogadores[2].convite}">
                    var form = $('#player3').find('input[name="convite"]').parent();
                    form.find('input[name="convite"]').val('${jogadores[2].convite.numero}');
                    form.append('<input type="hidden" name="validate" value="false" />');
                    auth(form);
                    form.find('input[name="validate"]').remove();
                </c:if>

                <c:if test="${not empty jogadores[3].socio}">
                    var form = $('#player4').find('input[name="matricula"]').parent();
                    form.find('input[name="matricula"]').val('${jogadores[3].socio.matricula}');
                    form.find('input[name="categoria"]').val('${jogadores[3].socio.idCategoria}');
                    form.find('input[name="dependente"]').val('${jogadores[3].socio.seqDependente}');
                    form.append('<input type="hidden" name="validate" value="false" />');
                    auth(form);
                    form.find('input[name="validate"]').remove();
                </c:if>
                <c:if test="${not empty jogadores[3].convite}">
                    var form = $('#player4').find('input[name="convite"]').parent();
                    form.find('input[name="convite"]').val('${jogadores[3].convite.numero}');
                    form.append('<input type="hidden" name="validate" value="false" />');
                    auth(form);
                    form.find('input[name="validate"]').remove();
                </c:if>


                $('.info-pane').hide();
                resizeCourt();
                toggle();
                ajaxUpdate();
            });
            
        </script>
    </head>

    <body>
        
        <div id="header">
            <div>
                <p id="title">Iate Clube de Brasília</p>
                <p id="time"></p>
            </div>
            <div class="clear-float"></div>
            <progress id="timeout"></progress>
            <div>
                <p id="broadcast"></p>
                <p id="date"></p>
            </div>
            <div class="clear-float"></div>
        </div>

        
        <div id="content-wrapper">
            <div id="court">
                <div id="court-info"></div>
                <div id="card-wrapper">
                    <div id="player1" class="player-card" data-title="Jogador 1"></div>
                    <div id="player2" class="player-card" data-title="Jogador 2"></div>
                    <div id="player3" class="player-card" data-title="Jogador 3"></div>
                    <div id="player4" class="player-card" data-title="Jogador 4"></div>
                </div>
                <p id='court-status'></p>
            </div>
        </div>
        
        
        <!-- *************************** Templates ********************************* -->
        <div id="selecionar-tipo-jogo" style="display: none;">
            <div style="width: 200px; height: 100px; overflow-y: auto; margin: 50px auto 0px auto; text-align: center;">
                <button class="botao-simples" type="button" onclick="selecionaJogo($(this));" disabled="disabled">Jogo Simples</button>
                <button class="botao-duplas" type="button" onclick="selecionaJogo($(this));" disabled="disabled">Jogo de Duplas</button>
            </div>
        </div>
        
        <c:if test="${admin}">
            <div id="buscar-socio-title" style="display: none;">
                <img src="images/tenis/buscar-jogador.png" width=20 height=20 style="position: absolute; top: 3px; left: 2px;" />
                <p style="margin-top: 2px; margin-left: 15px;">Selecione o sócio</p>
            </div>
            <div id="buscar-socio-window" style="display: none;">
                <div style="width: 800px; height: 400px; overflow-y: auto;">
                    <table>
                        <thead>
                            <th style="padding-left: 10px;">Título</th>
                            <th style="padding-left: 10px;">Categoria</th>
                            <th style="padding-left: 10px;">Nome</th>
                        </thead>
                        <tbody>
                        </tbody>
                    </table>
                </div>
            </div>
            <div id="buscar-convidado-title" style="display: none;">
                <img src="images/tenis/buscar-jogador.png" width=20 height=20 style="position: absolute; top: 3px; left: 2px;" />
                <p style="margin-top: 2px; margin-left: 15px;">Selecione o convidado</p>
            </div>
            <div id="buscar-convidado-window" style="display: none;">
                <div style="width: 800px; height: 400px; overflow-y: auto;">
                    <table style="padding-left: 10px;">
                        <thead>
                            <th style="padding-left: 10px;">Convite</th>
                            <th style="padding-left: 10px;">Nome</th>
                        </thead>
                        <tbody>
                        </tbody>
                    </table>
                </div>
            </div>

        </c:if>

        <div id="player-card" style="display: none;">
            <div class="login-pane">
                <p class='player-title'></p>
                <fieldset>
                    <legend>&nbsp;Sócio&nbsp;</legend>
                    <c:if test="${admin}">
                        <form method="POST" action="ajax/auth" class="player-form" onsubmit="buscar($(this)); return false;">
                            <input name="admin" type="hidden" value="true"/>
                            <input name="tipo" type="hidden" value="socio" />
                            <input name="dependente" type="hidden" value="1" />
                            Matrícula<br />
                            <input name="matricula" type="text" maxlength="8" onfocus="this.select();" onkeypress="onlyNumber(event);" /><br />
                            Categoria<br />
                            <input name="categoria" type="text" maxlength="2" onfocus="this.select();" onkeypress="onlyNumber(event);" /><br />
                            Nome<br />
                            <input name="nome" type="text" maxlength="40" onfocus="this.select();" />
                            <input type="image" class="buscar-socio" src="images/tenis/buscar-jogador.png" title="Buscar sócio" />
                        </form>
                    </c:if>
                    <c:if test="${not admin}">
                        <form method="POST" action="ajax/auth" class="player-form">
                            <input name="tipo" type="hidden" value="socio" />
                            Usuário<br />
                            <input name="usuario" type="text" maxlength="8" onfocus="this.select();" onkeypress="onlyNumber(event);" /><br />
                            Senha<br />
                            <input name="senha" type="password" maxlength="6" onfocus="this.select();" /><br />
                            <input type="button" value="Entrar" onclick="auth($(this).parent());" />
                        </form>
                    </c:if>
                </fieldset>
                    
                <fieldset>
                    <legend>&nbsp;Convidado&nbsp;</legend>
                    <c:if test="${admin}">
                        <form method="POST" action="ajax/auth" class="player-form" onsubmit="buscar($(this)); return false;">
                            <input name="admin" type="hidden" value="true"/>
                            <input name="tipo" type="hidden" value="convidado" />
                            Convite<br />
                            <input name="convite" type="text" maxlength="10" onfocus="this.select();" onkeypress="onlyNumber(event);" /><br />
                            Nome<br />
                            <input name="nome" type="text" maxlength="40" onfocus="this.select();" />
                            <input type="image" class="buscar-convidado" src="images/tenis/buscar-jogador.png" title="Buscar convidado" />
                        </form>
                    </c:if>
                    <c:if test="${not admin}">
                        <form method="POST" action="ajax/auth" class="player-form">
                            <input name="tipo" type="hidden" value="convidado" />
                            Convite<br />
                            <input name="convite" type="text" maxlength="10" onfocus="this.select();" onkeypress="onlyNumber(event);" /><br />
                            <input type="button" value="Entrar" onclick="auth($(this).parent());" />
                        </form>
                    </c:if>
                </fieldset>
            </div>
            
            <div class="info-pane">
                <img class="delete-player" src="images/tenis/close-icon.svg" title="Excluir jogador" onclick="sair($(this));" />
                <img class="player-avatar" src="" />
                <p class="player-name"></p>
            </div>
        </div>
        
    </body>

</html>
