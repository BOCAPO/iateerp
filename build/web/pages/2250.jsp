<%@include file="head.jsp"%>

<script type="text/javascript" language="JavaScript">

    function validarForm(){

        if(document.forms[0].descricao.value == ''){
            alert('O campo descrição é de preenchimento obrigatório!');
            return;
        }
        if(document.forms[0].texto.value == ''){
            alert('O campo texto é de preenchimento obrigatório!');
            return;
        }

        document.forms[0].submit();
    }
    function posCursor(el) {
        if (el.selectionStart) {
          return el.selectionStart;
        } else if (document.selection) {
          el.focus();

          var r = document.selection.createRange();
          if (r == null) {
            return 0;
          }

          var re = el.createTextRange(),
              rc = re.duplicate();
          re.moveToBookmark(r.getBookmark());
          rc.setEndPoint('EndToStart', re);

          return rc.text.length;
        } 
        return 0;
      }
      
      function teste(){
            var pos = posCursor(document.getElementById('texto'));
            $('#texto').val($('#texto').val().substring(0, pos) + '<<TIT>>' + $('#texto').val().substring(pos));
      }
      
      
</script>

<style>
       .texto{
                font-family: "Courier New";
                font-size: 14px;
        }
</style>

<body class="internas">
            
    <%@include file="menu.jsp"%>
    
    <div class="divisoria"></div>
    <div id="titulo-subnav">Alteração de Declaracao de Quitacao</div>
    <div class="divisoria"></div>
    
    <form action="c" id="it-bloco01">
        <input type="hidden" name="app" value="2250"/>
        <input type="hidden" name="acao" value="gravar"/>

        <p class="legendaCodigo">Descrição:</p>
        <input type="text" name="descricao" maxlength="40" class="campoDescricao" readonly value="${carta.descricao}" />


        <p class="legendaCodigo">Texto &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<img src="imagens/icones/inclusao-titular-07.png" style="width: 25px" onclick="teste()"/></p>

        <textarea class="campoSemTamanho texto" rows="10" cols="58" name="texto" id="texto">${carta.texto}</textarea><br>

        <input type="button" class="botaoatualizar"  onclick="validarForm()" value=" " />
        <input type="button" class="botaoVoltar"  onclick="window.location='c?app=2250';" value=" " />

    </form>
</body>
</html>
