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
        </style>        
        <script src="js/jquery-1.8.2.min.js" type="text/javascript"></script>
        <script type="text/javascript" language="JavaScript">
            $(document).ready(function() {
              ajustaTamanhoJanela();
            });
            
            function ajustaTamanhoOriginal()
            {
                $('#foto').removeAttr('style');
            }
            function ajustaTamanhoJanela()
            {
                var wh = $(window).height();
                $('#foto').attr('style', 'height:' + (wh-41) + 'px');
            }
            function aumentaZoom()
            {
                var tam = document.getElementById('foto').clientHeight; 
                tam = tam * 1.1;
                $('#foto').removeAttr('style');
                $('#foto').attr('style', 'height:' + tam + 'px');
            }
            function diminuiZoom()
            {
                var tam = document.getElementById('foto').clientHeight; 
                tam = tam * 0.9;
                $('#foto').removeAttr('style');
                $('#foto').attr('style', 'height:' + tam + 'px');
            }
        </script>        
    </head>
    <body>
        <input type="button" onclick="ajustaTamanhoOriginal()" value="Original" />
        <input type="button" onclick="ajustaTamanhoJanela()" value="Tela" />
        <input type="button" onclick="aumentaZoom()" value="+" />
        <input type="button" onclick="diminuiZoom()" value="-" />
        <br>
        
        <img id="foto" src="f?tb=20&cd=${idFoto}&brc=${idObjeto}"  class="recuoPadrao MargemSuperiorPadrao">
    </body>
</html>

