<%@page contentType="text/html" pageEncoding="ISO-8859-1"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
        <style>
            * {
              padding: 0;
              margin: 0;
            }
            .fit {
              max-width: 100%;
              max-height: 100%;
            }
            .center {
              display: block;
              margin: auto;
            }
        </style>        
        <script src="js/jquery-1.8.2.min.js" type="text/javascript"></script>
        <script type="text/javascript" language="JavaScript">
            function set_body_height()
            {
                var wh = $(window).height();
                $('body').attr('style', 'height:' + wh + 'px;');
            }
            $(document).ready(function() {
                set_body_height();
                $(window).bind('resize', function() { set_body_height(); });
            });
        </script>        
    </head>
    <body>
        <img id="center fit" src="f?tb=10&mat=${matricula}&cat=${categoria}&seq=${seqDependente}" class="recuoPadrao MargemSuperiorPadrao">
    </body>
</html>
