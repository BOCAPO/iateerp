    function validarSenhas(){
        return (document.forms[0].novaSenha1.value == document.forms[0].novaSenha2.value);
    }

    function alterarSenha(){
        if(document.forms[0].novaSenha1.value == ''){
            alert('Informe a nova senha.');
            return false;
        }
        else
        {
            if(validarSenhas()){
                document.forms[0].submit();
            }else{
                alert('Os campos Nova Senha e Confirmação Nova Senha não conferem!.');
            }
        }
    }

    function exibirIconesSenhas(){
	if((document.forms[0].novaSenha1.value == document.forms[0].novaSenha2.value)
            && (document.forms[0].novaSenha2.value == '')){
            document["imgSenha1"].src="img/blank.gif";
            document["imgSenha2"].src="img/blank.gif";
	}else if(document.forms[0].novaSenha1.value == document.forms[0].novaSenha2.value){
            document["imgSenha1"].src="img/ok.png";
            document["imgSenha2"].src="img/ok.png";
	} else {
            document["imgSenha1"].src="img/no.png";
            document["imgSenha2"].src="img/no.png";
	}
    }