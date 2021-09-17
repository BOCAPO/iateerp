/*
*   Valida a data no formato dd/MM/yyyy.
*   dataCaptada - Opcional. Deve ser passada como objeto text. 
*   retorno - true se a data for valida ou em branco, false
*             caso contrario.
*/
function isDataValida(data){
    if (data.length > 0){
        var dia, mes, ano;

        dia = data.charAt(0) + data.charAt(1);
        mes = data.charAt(3) + data.charAt(4);
        ano = data.charAt(6) + data.charAt(7) + data.charAt(8) + data.charAt(9);
        
        if (isNaN(dia) || isNaN(mes) || isNaN(ano)){
            alert("Entre o dia, mes e ano no formato dd/MM/yyyy. Dia e mes devem ter dois digitos e o ano quatro digitos. Ex.: 03/08/2007");
            return (false);
        }else{
            if (dia > 31) {
                alert("Dia do mes nao pode ser maior que 31.");
                return (false);
            }else if (mes > 12){
                alert("So exitem 12 meses.");
                return (false);
            }else if ((ano < 1900) || (ano > 9999)) {
                alert ("O ano deve estar no intervalo entre 1900 e 9999.");
                return (false);
            }else if (mes == "02"){
                if(dia >= 30){
                    alert("Fevereiro so vai ate dia 29.");
                    return (false);
                }else if ((dia == "29") && ((ano % 4) !== 0)){
                    alert("Nao estamos num ano bisexto, fevereiro so tem 28 dias.");
                    return (false);
                } 
            }else if (mes == "04" || mes == "06"  || mes == "09" || mes == "11"){
                if(dia > 30){
                    alert("Mês só possui 30 dias.");
                    return (false);
                }
            }
            // fim do bloco que verifica o mes de fevereiro
        } // fim do segundo bloco de if, caso a data ainda nao esteja invalida
    }// fim do bloco que verifica, a partir do tamanho do objeto, se a data foi digitada
    
    return (true);   
}
/*
*   Valida a data no formato dd/MM/yyyy.
*   dataCaptada - Opcional. Deve ser passada como objeto text. 
*   retorno - true se a data for valida ou em branco, false
*             caso contrario.
*/
function isMesAnoValido(data){
    if (data.length > 0){
        var mes, ano;
        mes = data.charAt(0) + data.charAt(1);
        ano = data.charAt(3) + data.charAt(4) + data.charAt(5) + data.charAt(6);
   
        if (isNaN(mes) || isNaN(ano)){
            alert("Entre o mes e ano no formato mm/yyyy. Mes deve ter dois digitos e o ano quatro digitos. Ex.: 08/2013");
            return (false);
        }else{
            if (mes > 12){
                alert("So exitem 12 meses.");
                return (false);
            }else if ((ano < 1900) || (ano > 9999)) {
                alert ("O ano deve estar no intervalo entre 1900 e 9999.");
                return (false);
            } 
        } 
    }
    
    return (true);   
}
/*
*   Valida a data no formato dd/MM.
*   dataCaptada - Opcional. Deve ser passada como objeto text. 
*   retorno - true se a data for valida ou em branco, false
*             caso contrario.
*/
function isDiaMesValido(data){

    if (data.length > 0){
        var dia, mes;

        dia = data.charAt(0) + data.charAt(1);
        mes = data.charAt(3) + data.charAt(4);
   
        if (isNaN(dia) || isNaN(mes) || data.charAt(4) == ''){
            alert("Entre o dia e o mes no formato dd/MM. Dia e mes devem ter dois digitos. Ex.: 03/08");
            return (false);
        }else{
            if (dia > 31) {
                alert("Dia do mes nao pode ser maior que 31.");
                return (false);
            }else if (mes > 12){
                alert("So exitem 12 meses.");
                return (false);
            }else if (mes == "02"){
                if(dia >= 30){
                    alert("Fevereiro so vai ate dia 29.");
                    return (false);
                }else if ((dia == "29") && ((ano % 4) !== 0)){
                    alert("Nao estamos num ano bisexto, fevereiro so tem 28 dias.");
                    return (false);
                } 
            } // fim do bloco que verifica o mes de fevereiro
        } // fim do segundo bloco de if, caso a data ainda nao esteja invalida
    }// fim do bloco que verifica, a partir do tamanho do objeto, se a data foi digitada
    
    return (true);   
}

function isHoraValida(hora){ 
    var hrs = parseInt(hora.substring(0,2)); 
    var min = parseInt(hora.substring(3,5)); 

    if(isNaN(hrs) || isNaN(min)){
        alert("Entre com a hora e minuto no formato HH:MM"); 
        return false;        
    }
    
    // verifica data e hora 
    if ((hrs < 00 ) || (hrs > 23) || ( min < 00) ||( min > 59)){ 
        alert("A hora deve estar no intervalo de 00:00 a 23:59"); 
        return false;
    } 

    return true;
} 

function marcaDesmarca(nome){        
    $('[name='+nome+']').each(function(){
        $(this).attr('checked', !$(this).attr('checked'));
    });            
}

function isDataHoraValida(value)
{
    var valid = false;
 
    // Define uma expressão regular para validar se a data/hora informada está
    // no formato nn/nn/nnnn nn:nn:nn, onde n é um número entre 0 e 9
    var regex = new RegExp("^([0-9]{2})/([0-9]{2})/([0-9]{4}) ([0-9]{2}):([0-9]{2}):([0-9]{2})$");
    var matches = regex.exec(value);
 
    if (matches != null)
    {
        var day = parseInt(matches[1], 10);
        var month = parseInt(matches[2], 10) - 1;
        var year = parseInt(matches[3], 10);
        var hour = parseInt(matches[4], 10);
        var minute = parseInt(matches[5], 10);
        var second = parseInt(matches[6], 10);
 
        var date = new Date(year, month, day, hour, minute, second, 0);
        valid = date.getFullYear() == year && date.getMonth() == month &&
            date.getDate() == day && date.getHours() == hour &&
            date.getMinutes() == minute && date.getSeconds() == second;
    }
 
    return valid;
}
      
function onlyNumber(evt){
    if(evt.which !== 0 && evt.which != 8){
        if(evt.which < 48 || evt.which > 57){
            evt.preventDefault();
        }
    }
}

function onlyPositiveFloat(evt){
    if(evt.which !== 0 && evt.which != 8 && evt.which != 44){
        if(evt.which < 48 || evt.which > 57){
            evt.preventDefault();
        }
    }
}

function isNumeric(s){

    var validChars = "0123456789.-";
    var chr;
    var result = true;
    var i;

    if(s.length === 0){
        return false;
    }
    
    for(i = 0; i < s.length && result === true; i++){
        chr = s.charAt(i);
        if (validChars.indexOf(chr) == -1){
            result = false;
        }
    }
    
    return result;
}

function trim(str){
    return str.replace(/^\s+|\s+$/g,"");
}

function verifica_branco(parametro)  {  // FUNCAO PARA VERIFICAÃ‡ÃƒO DE CAMPOS NÃƒO PREENCHIDOS
									// OU PREENCHIDOS APENAS COM ESPAÃ‡OS EM BRANCO
	teste_parametro = "false"; //variavel para teste de espacos em branco
	tamanho_parametro = parametro.length;
	if (tamanho_parametro != 0) {
		for (i = 0; i < tamanho_parametro; i++) {
		   if (parametro.charAt(i) != " ") {
		  	  teste_parametro = "true"; /*existe caracter diferente de branco*/
		   }
		}
		if (teste_parametro == "false") { //todos os caracteres digitados sÃ£o brancos 
			return false;
		} else {
			return true;
		}
	} else {
		return false;
	}
}
