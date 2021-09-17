<%@page contentType="text/html; charset=utf-8" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@taglib prefix="joda" uri="http://www.joda.org/joda/time/tags"%>
<fmt:setLocale value="pt_BR"/>


<html>
    <!-- ${pageContext.request.servletPath} -->
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
        <link rel="icon" href="imagens/icones/favicon.ico" type="image/x-icon"/>
        <link rel="shortcut icon" href="imagens/icones/favicon.ico" type="image/x-icon"/>
	<title>Iate Clube de Brasília</title>

        <link href="css/main.css" rel="stylesheet" type="text/css"/>
        <link href="css/formularios.css" rel="stylesheet" type="text/css"/>
	<link href="css/menu.css" rel="stylesheet" type="text/css"/>
	<link href="css/calendario.css" rel="stylesheet" type="text/css"/>
        <link href="css/screen.css" rel="stylesheet" type="text/css"/>
	<link href="css/tabelas.css" rel="stylesheet" type="text/css"/>
        <link href="css/iate_forms.css" rel="stylesheet" type="text/css"/>
        <link href="css/redmond/jquery-ui-1.10.1.custom.min.css" rel="stylesheet" type="text/css"/>
        <script type="text/javascript" src="js/menu.js"></script>
        <script src="js/jquery-1.8.2.min.js" type="text/javascript"></script>
        <script src="js/masked-input-plugin.js" type="text/javascript"></script>
        <script src="js/jquery-ui-1.10.1.custom.min.js" type="text/javascript"></script>
        <script src="js/jquery.format-1.2.min.js" type="text/javascript"></script>
        <script type="text/javascript">
            jQuery(function($){
                $.datepicker.regional['pt-BR'] = {
                    closeText: 'Fechar',
                    prevText: '&#x3c;Anterior',
                    nextText: 'Pr&oacute;ximo&#x3e;',
                    currentText: 'Hoje',
                    monthNames: ['Janeiro','Fevereiro','Mar&ccedil;o','Abril','Maio','Junho','Julho','Agosto','Setembro','Outubro','Novembro','Dezembro'],
                    monthNamesShort: ['Jan','Fev','Mar','Abr','Mai','Jun','Jul','Ago','Set','Out','Nov','Dez'],
                    dayNames: ['Domingo','Segunda-feira','Ter&ccedil;a-feira','Quarta-feira','Quinta-feira','Sexta-feira','S&aacute;bado'],
                    dayNamesShort: ['Dom','Seg','Ter','Qua','Qui','Sex','S&aacute;b'],
                    dayNamesMin: ['Dom','Seg','Ter','Qua','Qui','Sex','S&aacute;b'],
                    weekHeader: 'Sm',
                    dateFormat: 'dd/mm/yy',
                    firstDay: 0,
                    isRTL: false,
                    showMonthAfterYear: false,
                    yearSuffix: ''};
                $.datepicker.setDefaults($.datepicker.regional['pt-BR']);
            }); 
        </script>            
    </head>

    <body class="internas">

            <div id="rodape">
                <div id="copyright"><img src="imagens/copyright.png" /></div>
            </div>

    </body>
</html>

        
