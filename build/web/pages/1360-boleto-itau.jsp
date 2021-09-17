<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.0 Transitional//EN">

<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>

<style type="text/css">
    .campos{ 
    font-family: "Trebuchet MS",Verdana,Arial,Helvetica,sans-serif;
    font-size: 9px;
    font-weight:bolder;
    }    
    .linhaDigitavel{ 
    font-family: "Trebuchet MS",Verdana,Arial,Helvetica,sans-serif;
    font-size: 14.5px;
    font-weight:bolder;
    }    
    .detParcelas{ 
    font-family: "Courier New";
    font-size: 9px;
    font-weight:bolder;
    }    
</style>

<DIV style="TOP: 45px; LEFT: 5px; POSITION: absolute;">
    <IMG src="boleto_jsp_arquivos/LayoutItau.jpg" border=0 style="width: 670px">
</DIV>

<fmt:formatDate var="dtDocumento" value="${s.dtDocumento}" pattern="dd/MM/yyyy"/>
<fmt:formatDate var="dtProcessamento" value="${s.dtProcessamento}" pattern="dd/MM/yyyy"/>
<fmt:formatDate var="dataPagamento" value="${s.dataPagamento}" pattern="dd/MM/yyyy"/>
<fmt:formatNumber value="${s.valorPago}" maxFractionDigits="2" minFractionDigits="2" var="valorPago"/>


<DIV class="campos" style="TOP: 176px; LEFT: 26px; POSITION: absolute; ">
    ${dtDocumento}
</DIV>
<DIV class="campos" style="TOP: 176px; LEFT: 140px; POSITION: absolute; ">
    ${s.numeroDocumento}
</DIV>
<DIV class="campos" style="TOP: 176px; LEFT: 270px; POSITION: absolute; ">
    DV
</DIV>
<DIV class="campos" style="TOP: 176px; LEFT: 345px; POSITION: absolute; ">
    A
</DIV>
<DIV class="campos" style="TOP: 176px; LEFT: 395px; POSITION: absolute; ">
    ${dtProcessamento}
</DIV>


<DIV class="campos" style="TOP: 205px; LEFT: 140px; POSITION: absolute; ">
    ${s.codCarteira}
</DIV>
<DIV class="campos" style="TOP: 205px; LEFT: 220px; POSITION: absolute; ">
    R$
</DIV>
<DIV class="campos" style="TOP: 205px; LEFT: 395px; POSITION: absolute; ">
    ${valorPago}
</DIV>


<DIV class="detParcelas" style="TOP: 233px; LEFT: 18px; POSITION: absolute; ">
    <c:forEach var="p" items="${parc}">
    ${p}<BR>
    </c:forEach>
</DIV>
<DIV class="campos" style="TOP: 350px; LEFT: 18px; POSITION: absolute; ">
    APOS VENCIMENTO ACESSO WWW.ITAU.COM.BR/BOLETOS PARA ATUALIZAR SEU BOLETO
</DIV>


<DIV class="campos" style="TOP: 366px; LEFT: 55px; POSITION: absolute; ">
    ${s.noSacado} - CPF/CNPJ: ${s.CPFPessoa}
</DIV>
<DIV class="campos" style="TOP: 383px; LEFT: 60px; POSITION: absolute; ">
    ${s.edSacado} - ${s.edSacadoCompl}
</DIV>


<DIV class="campos" style="TOP: 91px; LEFT: 570px; POSITION: absolute; ">
    ${dataPagamento}
</DIV>
<DIV class="campos" style="TOP: 120px; LEFT: 570px; POSITION: absolute; ">
    0522/08884-8
</DIV>
<DIV class="campos" style="TOP: 176px; LEFT: 570px; POSITION: absolute; ">
    ${s.codCarteira} / ${s.nossoNumero}
</DIV>
<DIV class="campos" style="TOP: 205px; LEFT: 570px; POSITION: absolute; ">
    ${valorPago}
</DIV>
<DIV class="campos" style="TOP: 234px; LEFT: 570px; POSITION: absolute; ">
    
</DIV>
<DIV class="campos" style="TOP: 350px; LEFT: 570px; POSITION: absolute; ">
    ${valorPago}
</DIV>


<DIV class="campos" style="TOP: 581px; LEFT: 26px; POSITION: absolute; ">
    ${dtDocumento}
</DIV>
<DIV class="campos" style="TOP: 581px; LEFT: 140px; POSITION: absolute; ">
    ${s.numeroDocumento}
</DIV>
<DIV class="campos" style="TOP: 581px; LEFT: 270px; POSITION: absolute; ">
    DV
</DIV>
<DIV class="campos" style="TOP: 581px; LEFT: 345px; POSITION: absolute; ">
    A
</DIV>
<DIV class="campos" style="TOP: 581px; LEFT: 395px; POSITION: absolute; ">
    ${dtProcessamento}
</DIV>


<DIV class="campos" style="TOP: 610px; LEFT: 140px; POSITION: absolute; ">
    ${s.codCarteira}
</DIV>
<DIV class="campos" style="TOP: 610px; LEFT: 220px; POSITION: absolute; ">
    R$
</DIV>
<DIV class="campos" style="TOP: 610px; LEFT: 395px; POSITION: absolute; ">
    ${valorPago}
</DIV>


<DIV class="detParcelas" style="TOP: 637px; LEFT: 19px; POSITION: absolute; ">
    <c:forEach var="p" items="${parc}">
    ${p}<BR>
    </c:forEach>
</DIV>

<DIV class="campos" style="TOP: 754px; LEFT: 19px; POSITION: absolute; ">
    APOS VENCIMENTO ACESSO WWW.ITAU.COM.BR/BOLETOS PARA ATUALIZAR SEU BOLETO
</DIV>

<DIV class="campos" style="TOP: 769px; LEFT: 55px; POSITION: absolute; ">
    ${s.noSacado} - CPF/CNPJ: ${s.CPFPessoa}
</DIV>
<DIV class="campos" style="TOP: 783px; LEFT: 60px; POSITION: absolute; ">
    ${s.edSacado} - ${s.edSacadoCompl}
</DIV>


<DIV class="campos" style="TOP: 522px; LEFT: 570px; POSITION: absolute; ">
    ${dataPagamento}
</DIV>
<DIV class="campos" style="TOP: 552px; LEFT: 570px; POSITION: absolute; ">
    0522/08884-8
</DIV>
<DIV class="campos" style="TOP: 581px; LEFT: 570px; POSITION: absolute; ">
    ${s.codCarteira} / ${s.nossoNumero}
</DIV>
<DIV class="campos" style="TOP: 610px; LEFT: 570px; POSITION: absolute; ">
    ${valorPago}
</DIV>
<DIV class="campos" style="TOP: 696px; LEFT: 570px; POSITION: absolute; ">
    
</DIV>
<DIV class="campos" style="TOP: 754px; LEFT: 570px; POSITION: absolute; ">
    ${valorPago}
</DIV>


<DIV class="linhaDigitavel" style="TOP: 486px; LEFT: 224px; POSITION: absolute; HEIGHT: 70px">
    ${s.linhaDigitavel}
</DIV>


<DIV style="LEFT: 25px; POSITION: absolute; TOP: 820px; HEIGHT: 70px">
    <TABLE cellSpacing=0 cellPadding=0 width=666 border=0>
        <TBODY>
            <TR>
                <TD colSpan=2>
                    ${codBar}
                </TD>
            </TR>
        </TBODY>
    </TABLE>
</DIV>
<BR>
<BR><BR><BR>
