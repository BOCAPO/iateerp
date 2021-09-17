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
                left: 0px;
                top: 0px;
                margin-top: 10px;
                margin-left: 10px;
                height: 120px;
                width: 120px;
                display: block;
                border-radius:100px;
                border-style: groove;
                border-width: thick;
                box-shadow: 12px 12px 4px 0px rgba(0, 0, 0, 0.3);
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
                window.location.replace('tenis?admin=true');
            }
            
            var comm = true;
            var jogadores = {};
            var jogo = {
                quadra   : ${jogo.quadra.id},
                restricao: null,
                pronto   : false,
                jogadores: jogadores
            };

            function ajaxUpdate() {
                $.getJSON('ajax/marcar', {
                    "id": "${jogo.quadra.id}"
                    }, function(quadra) {
                        $('#date').text(quadra.date);
                        $('#time').html(quadra.time.substring(0, 2) + '<span class="blink">:</span>' + quadra.time.substring(3, 5));
                        blink();
                        
                        if (comm === false) {
                            comm = true;
                            broadcast();
                        }
                        
                        if (timer.timeout === undefined) timer.timeout = quadra.timeout * 1000;

                        if (quadra.liberada !== undefined && quadra.estado !== 'LIVRE')
                            $('#court-status').text(quadra.estado + ' até ' + quadra.liberada);
                        else  
                            $('#court-status').text(quadra.estado);

                        var s = '';
                        s = s + '<p id="court-name">' + quadra.nome + '</p>';
                        
                        
                        <c:if test="${simples}">
                            jogo.restricao = 'SIMPLES';
                            $('#player1').css('opacity', '1.0').find('input').attr('disabled', null);
                            $('#player2').css('opacity', '1.0').find('input').attr('disabled', null);
                            $('#player3').css('opacity', '0.3').find('input').attr('disabled', 'disabled');
                            $('#player4').css('opacity', '0.3').find('input').attr('disabled', 'disabled');
                            s = s + '<p id="court-restriction">Simples</p>';
                        </c:if>
                        <c:if test="${not simples}">
                            jogo.restricao = 'DUPLAS';
                            $('#player1').css('opacity', '1.0').find('input').attr('disabled', null);
                            $('#player2').css('opacity', '1.0').find('input').attr('disabled', null);
                            $('#player3').css('opacity', '1.0').find('input').attr('disabled', null);
                            $('#player4').css('opacity', '1.0').find('input').attr('disabled', null);
                            s = s + '<p id="court-restriction">Duplas</p>';
                        </c:if>
                        
                        s = s + '<div id="schedule-button" class="active-button" onclick="excluir($(this));" onmousedown="$(this).css(\'border-style\', \'inset\');">';
                        s = s + '<p style="text-align: center;">Excluir Jogo</p></div>';
                        
                        s = s + '<p id="court-interval">${intervalo}</p>';
                        
                        switch(quadra.estado) {
                            case 'LIVRE':
                                s = s + '<div id="green-light" title="A quadra está livre"></div>';
                                break;
                            case 'OCUPADA':
                                s = s + '<div id="yellow-light" title="A quadra está ocupada"></div>';
                                break;
                            case 'FECHADA':
                                s = s + '<div id="red-light" title="A quadra está fechada"></div>';
                                break;
                        }
                        
                            
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
                
                setTimeout('ajaxUpdate()', 1000);
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
            
            $(document).ready(function() {
                resizeCourt();
                toggle();
                ajaxUpdate();
            });
    
            function excluir(botao) {
                $('#schedule-button').css('border-style', 'outset');
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
                            timerUpdate();
                        }
                    }
                });

            }
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
                    <div id="player1" class="player-card" data-title="Jogador 1">
                        <div class="info-pane">
                            <img class="player-avatar" src="${foto1}" onerror="this.src='images/tenis/avatar-default.png';"/>
                            <p class="player-name">${nome1}</p>
                        </div>
                    </div>
                    <div id="player2" class="player-card" data-title="Jogador 2">
                        <div class="info-pane">
                            <img class="player-avatar" src="${foto2}" onerror="this.src='images/tenis/avatar-default.png';"/>
                            <p class="player-name">${nome2}</p>
                        </div>
                    </div>
                    <div id="player3" class="player-card" data-title="Jogador 3">
                        <div class="info-pane">
                            <img class="player-avatar" src="${foto3}" onerror="this.src='images/tenis/avatar-default.png';"/>
                            <p class="player-name">${nome3}</p>
                        </div>
                    </div>
                    <div id="player4" class="player-card" data-title="Jogador 4">
                        <div class="info-pane">
                            <img class="player-avatar" src="${foto4}" onerror="this.src='images/tenis/avatar-default.png';"/>
                            <p class="player-name">${nome4}</p>
                        </div>
                    </div>
                </div>
                <p id='court-status'></p>
            </div>
        </div>

    </body>

</html>
