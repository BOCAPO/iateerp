<!DOCTYPE html>
<%@page contentType="text/html" pageEncoding="utf-8"%>
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
                margin-bottom: 0em;
                margin-left: 30px;
            }
            
            #broadcast {
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
                margin-bottom: 0em;
                margin-right: 30px;
            }

            #content {
                width: 100%;
                position: absolute;
                top: 150px;
                bottom: 0px;
                border-spacing: 20px;
                display: table;
                table-layout: fixed;
            }
            
            .clear-float {
                clear: both;
            }
            
            .court {
                color: #FFF;
                border-radius: 25px;
                background-color: rgba(0, 0, 0, 0.6);
                background-size: contain;
                display: table-cell;
                overflow: hidden;
                position: relative;
                border-style: groove;
                box-shadow: 6px 6px 4px 0px rgba(0, 0, 0, 0.8);
            }
            
            .linkable:hover {
                cursor: pointer;
                background-color: rgba(200, 200, 200, 0.6);
            }
            
            .court > p.name {
                color: #FFF;
                margin-top: 7px;
                margin-left: 50px;
                font-family: Arial;
                font-weight: bold;
                font-size: 150%;
                text-shadow: 4px 4px 2px rgba(0, 0, 0, 0.3);
            }
            
            .court > p.restriction {
                color: #CCC;
                font-style: italic;
                margin-top: -30px;
                margin-left: 52px;
                font-family: Arial;
                font-weight: bold;
                font-size: 85%;
                text-shadow: 4px 4px 2px rgba(0, 0, 0, 0.3);
            }
            
            .court > p.free {
                position: absolute;
                margin-bottom: 0px;
                margin-right: 18px;
                bottom: 0px;
                right: 0px;
                text-align: right;
                color: #CCC;
                font-style: italic;
                font-family: Arial;
                font-weight: bold;
                font-size: 85%;
                text-shadow: 4px 4px 2px rgba(0, 0, 0, 0.3);
            }

            .court > p.status {
                color: #FFF;
                margin-bottom: 10px;
                margin-right: 18px;
                font-family: Arial;
                font-weight: bold;
                font-size: 150%;
                position: absolute;
                bottom: 0px;
                right: 0px;
                text-shadow: 4px 4px 2px rgba(0, 0, 0, 0.3);
            }
            
            .court > p.remark {
                position: absolute;
                margin-left: 15px;
                margin-top: 0px;
                top: 45px;
                left: 0px;
                color: #FFF;
                font-family: Arial;
                font-weight: bold;
                font-size: 100%;
                text-shadow: 4px 4px 2px rgba(0, 0, 0, 0.3);
            }

            .court-row {
                display: table-row;
            }
            
            
            .green-light, .yellow-light, .red-light {
                position: absolute;
                left: 0px;
                top: 0px;
                margin-top: 4px;
                margin-left: 4px;
                height: 30px;
                width: 30px;
                display: block;
                border-radius:25px;
                border-style: groove;
                box-shadow: 4px 4px 2px 0px rgba(0, 0, 0, 0.3);
            }

            .green-light {
                background: -webkit-linear-gradient(-45deg, rgba(48,224,113,1) 0%,rgba(27,181,81,1) 24%,rgba(6,128,49,1) 50%,rgba(25,159,72,1) 79%,rgba(30,192,87,1) 100%); /* Chrome10+,Safari5.1+ */
            }

            .yellow-light {
                background: -webkit-linear-gradient(-45deg, rgba(231,229,87,1) 0%,rgba(211,205,31,1) 24%,rgba(150,145,6,1) 50%,rgba(188,183,28,1) 79%,rgba(223,216,37,1) 100%); /* Chrome10+,Safari5.1+ */
            }

            .red-light {
                background: -webkit-linear-gradient(-45deg, rgba(219,139,121,1) 0%,rgba(203,82,57,1) 24%,rgba(147,42,21,1) 50%,rgba(183,71,49,1) 79%,rgba(206,94,72,1) 100%); /* Chrome10+,Safari5.1+ */
            }
            
            .link-spanner {
                position:absolute; 
                width:100%;
                height:100%;
                top:0;
                left: 0;
                z-index: 1;
            }
            
            .agenda .qtip-titlebar {
                font-family: Arial;
                font-weight: bold;
                font-size: 180%;
                height: 16px;
            }
            
            .agenda .qtip-content {
                font-family: Arial;
                font-size: 130%;
            }
        </style>
        <script type="text/javascript">
            function escapeHTML() {
                return this.replace(/&/g,'&amp;').replace(/</g,'&lt;').replace(/>/g,'&gt;');
            }
    
            var quadras;
            function ajaxUpdate() {
                $.getJSON('ajax/tenis',
                    function(data) {
                        broadcast();
                        $('#date').text(data.date);
                        $('#time').html(data.time.substring(0, 2) + '<span class="blink">:</span>' + data.time.substring(3, 5));
                        blink();

                        var s = '';
                        quadras = data.quadras;
                        $.each(data.quadras, function(i, quadra) {
                            if (i >= 15) return; // Layout para somente 15 quadras
                            
                            if (i % 5 === 0) {
                                if (i !== 0) s = s + '</div>';
                                s = s + '<div class="court-row">';
                            }
                            
                            if (quadra.podeMarcar) {
                                s = s + '<div id="quadra-' + i + '" data-index="' + i + '" class="linkable court" onmouseover="showAgenda(' + i + ')">';
                                <c:if test="${admin}">
                                    s = s + '<a href="c?app=2371&id=' + quadra.id + '"><span class="link-spanner"></span></a>';                                            
                                </c:if>
                                <c:if test="${not admin}">
                                    s = s + '<a href="marcar?id=' + quadra.id + '"><span class="link-spanner"></span></a>';
                                </c:if>
                            } else
                                s = s + '<div id="quadra-' + i + '" data-index="' + i + '" class="court" onmouseover="showAgenda(' + i + ')">';
                                
                            switch(quadra.estado) {
                                case 'LIVRE':
                                    s += '<div class="green-light"></div>';
                                    s += '<p class="name">' + $('<div />').text(quadra.nome).html() + '</p>';
                                    s += '<p class="restriction">' + quadra.restricao + '</p>';
                                    s += '<p class="remark">' + $('<div />').text(quadra.observacao).html() + '</p>';
                                    s += '<p class="status">' + quadra.estado + '</p>';
                                    break;
                                case 'OCUPADA':
                                    s += (quadra.podeMarcar) ? '<div class="yellow-light"></div>' : '<div class="red-light"></div>';
                                    s += '<p class="name">' + $('<div />').text(quadra.nome).html() + '</p>';
                                    s += '<p class="restriction">' + quadra.restricao + '</p>';
                                    if (quadra.liberada !== undefined)
                                        s += '<p class="free">' + quadra.liberada + '</p>';
                                    s += '<p class="remark">';
                                        if (quadra.observacao !== '') s += $('<div />').text(quadra.observacao).html() + '<br />';
                                        if (quadra.periodo !== undefined) s += quadra.periodo;
                                        $.each(quadra.participantes, function(j, participante) {
                                            s += '<br />&nbsp;&nbsp;&nbsp;&bull; ' + $('<div />').text(participante.nome).html();
                                        });
                                    s += '</p>';
                                    s += '<p class="status">';
                                        s += quadra.estado;
                                    s += '</p>';
                                    break;
                                case 'BLOQUEADA':
                                    s += (quadra.podeMarcar) ? '<div class="yellow-light"></div>' : '<div class="red-light"></div>';
                                    s += '<p class="name">' + $('<div />').text(quadra.nome).html() + '</p>';
                                    if (quadra.liberada !== undefined)
                                        s += '<p class="free">' + quadra.liberada + '</p>';
                                    s += '<p class="remark">';
                                        if (quadra.periodo !== undefined) s += quadra.periodo + '<br />';
                                        s += $('<div />').text(quadra.observacao).html();
                                    s += '</p>';
                                    s += '<p class="status">';
                                        s += quadra.estado;
                                    s += '</p>';
                                    break;
                            }
                            s += '</div>';
                        });
                        if ((data.quadras.length - 1) % 5 !== 0) s = s + '</div>';
                        
                        $('#content').empty().append(s);
                        resizeCourt();
                        
                    }
                ).error(function() {
                    broadcast('Problemas de comunicação!', 'red');
                    $('#date').text('');
                    $('#time').text('');
                });
                
                setTimeout('ajaxUpdate()', 4321);
                //setTimeout('ajaxUpdate()', 432100);
            }
            
            var agenda = {
                i: 0,
                show: false
            };
            
            
            function showAgenda(i) {
                var quadra = quadras[i];

                var text = '';
                $.each(quadra.agenda, function(i, evento) {
                    text = text + '<strong>&#x25d5; ' + evento.intervalo + '</strong>';
                    <c:if test="${admin}">
                            if (evento.id !== undefined)
                                text = text + ' <a href="c?app=2370&id=' + evento.id + '">' + evento.nome + '</a>';
                            else
                                text = text + ' ' + evento.nome;
                    </c:if>
                    <c:if test="${not admin}">
                            text = text + ' ' + evento.nome;
                    </c:if>
                    text = text + '<br/>';
                });

                $(window).qtip({
                    content : {
                        <c:if test="${admin}">
                            title   : {text : 'Agenda ' + quadra.nome, button: true},
                        </c:if>
                        <c:if test="${not admin}">
                            title   : {text : 'Agenda ' + quadra.nome},
                        </c:if>
                        text    : text
                    },
                    position: {
                        my      : 'left center',
                        at      : 'right center',
                        target  : $('#quadra-' + i),
                        viewport: $('#content')
                    },
                    show    : {
                        ready   : true,
                        solo    : true
                    },
                    hide    : {
                        <c:if test="${admin}">
                            fixed   : true,
                            delay   : 100,
                        </c:if>
                        event   :'unfocus mouseleave'
                    },
                    style   : {
                        width   : 500,
                        classes : 'qtip-rounded qtip-shadow qtip-light agenda'
                    },
                    events  : {
                        hidden  : function(event, api) { api.destroy; agenda.show = false; }
                    },
                });
            }
            
            
            function resizeCourt() {
                $('.court').width(Math.floor(($(window).width() - 20) / 5) - 26);
                $('.court').height(Math.floor(($(window).height() - $('#header').height() - 20) / 3) - 26);
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

            $(document).ready(function() {
                ajaxUpdate();
                toggle();
            });

            $(window).resize(function() {
                resizeCourt();
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
            <hr />
            <div>
                <p id="broadcast"></p>
                <p id="date"></p>
            </div>
            <div class="clear-float"></div>
        </div>
            
        <div id="content"></div> 
    </body>

</html>
