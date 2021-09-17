<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.0 Transitional//EN">
<!-- saved from url=(0054)https://www11.bb.com.br/site/mpag/simulador/boleto.jsp -->
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>

<HTML><HEAD>
<META http-equiv=Content-Type content="text/html; charset=iso-8859-1">
<SCRIPT language=JavaScript>ready = "yes";</SCRIPT>

<SCRIPT language=JavaScript>var estado=false; </SCRIPT>

<META http-equiv=PICS-Label 
content='(PICS-1.1 "http://www.rsac.org/ratingsv01.html" l gen true comment "RSACi North America Server" for "https://www2.bb.com.br" on "2000.06.15T13:48-0800" r (n 0 s 0 v 0 l 0))'>
<SCRIPT language=JavaScript>
var da = (document.all) ? 1 : 0;
var pr = (window.print) ? 1 : 0;
var mac = (navigator.userAgent.indexOf("Mac") != -1); 

function printPage() {
  if (pr) // NS4, IE5
    window.print()
  else if (da && !mac) // IE4 (Windows)
       vbPrintPage()
  else // other browsers
    alert("Desculpe seu browser não suporta esta função. Por favor utilize a barra de trabalho para imprimir a página.");
}

if (da && !pr && !mac) with (document) {
  writeln('<OBJECT ID="WB" WIDTH="0" HEIGHT="0" CLASSID="clsid:8856F961-340A-11D0-A96B-00C04FD705A2"></OBJECT>');
  writeln('<' + 'SCRIPT LANGUAGE="VBScript">');
  writeln('Sub window_onunload');
  writeln('  On Error Resume Next');
  writeln('  Set WB = nothing');
  writeln('End Sub');  
  writeln('Sub vbPrintPage');
  writeln('  OLECMDID_PRINT = 6');
  writeln('  OLECMDEXECOPT_DONTPROMPTUSER = 1');
  writeln('  OLECMDEXECOPT_PROMPTUSER = 1');
  writeln('  On Error Resume Next');
  writeln('  WB.ExecWB OLECMDID_PRINT, OLECMDEXECOPT_DONTPROMPTUSER');
  writeln('End Sub');
  writeln('<' + '/SCRIPT>');
}
</SCRIPT>
<LINK href="css/boleto.css" type=text/css rel=stylesheet>

<META content="MSHTML 6.00.2800.1498" name=GENERATOR></HEAD>
<BODY bgProperties=fixed bgColor=white leftMargin=0 topMargin=0 marginheight="0" 
marginwidth="0"><A name=topo></A>
<TABLE cellSpacing=0 cellPadding=0 width=660 border=0>
  <TBODY>
  <TR>
    <TD class=Normal>
      <DIV align=center>&nbsp;<B>Instruções:</B><BR>
      </DIV>
      <OL>
        <LI>Imprima em impressora jato de tinta (ink jet) ou laser em qualidade 
        normal ou alta Não use modo econômico.<BR>
        <LI>Utilize folha A4 (210 x 297mm) ou Carta (216 x 279mm) e margens 
        mínimas à esquerda e à direita do formulário.<BR>
        <LI>Corte na linha indicada. Não rasure, risque, fure ou dobre a região 
        onde se encontra o código de barras. </LI></OL></TD></TR></TBODY></TABLE><BR>
<DIV 
style="Z-INDEX: 1; LEFT: 5px; WIDTH: 670px; POSITION: absolute; TOP: 145px; HEIGHT: 636px"><IMG 
height=636 alt="" src="boleto_jsp_arquivos/formularioBoleto.gif" width=647 
border=0></DIV>
<DIV 
style="Z-INDEX: 2; LEFT: 7px; WIDTH: 96px; POSITION: absolute; TOP: 161px; HEIGHT: 22px"><IMG 
height=22 alt="" src="boleto_jsp_arquivos/imgbb.gif" width=150 border=0></DIV>
<DIV 
style="Z-INDEX: 2; LEFT: 7px; WIDTH: 96px; POSITION: absolute; TOP: 444px; HEIGHT: 22px"><IMG 
height=22 alt="" src="boleto_jsp_arquivos/imgbb.gif" width=150 border=0></DIV>
<DIV class=Titulo11 
style="Z-INDEX: 2; LEFT: 7px; POSITION: absolute; TOP: 127px">Corte na linha 
pontilhada</DIV>
<DIV class=Titulo11 
style="Z-INDEX: 2; LEFT: 5px; POSITION: absolute; TOP: 138px"><IMG height=5 
alt="" src="boleto_jsp_arquivos/linhaPontilhada.gif" width=650 border=0></DIV>
<DIV class=Campo 
style="Z-INDEX: 2; LEFT: 549px; POSITION: absolute; TOP: 145px"><B>Recibo do 
Sacado</B></DIV>
<DIV class=CampoTitulo 
style="Z-INDEX: 2; LEFT: 163px; POSITION: absolute; TOP: 167px">001-9</DIV>
<DIV class=CampoTitulo 
style="Z-INDEX: 2; LEFT: 210px; WIDTH: 438px; POSITION: absolute; TOP: 166px; TEXT-ALIGN: right"><LAYER 
width="438px" name="helpns">${s.linhaDigitavel}</LAYER></DIV>
<DIV class=CampoTitulo 
style="Z-INDEX: 2; LEFT: 163px; POSITION: absolute; TOP: 450px">001-9</DIV>
<DIV class=CampoTitulo 
style="Z-INDEX: 2; LEFT: 210px; WIDTH: 438px; POSITION: absolute; TOP: 449px; TEXT-ALIGN: right"><LAYER 
width="438px" name="helpns">${s.linhaDigitavel}</LAYER></DIV>
<DIV class=Titulo11 
style="Z-INDEX: 2; LEFT: 15px; POSITION: absolute; TOP: 184px">Cedente</DIV>
<DIV class=Titulo11 
style="Z-INDEX: 2; LEFT: 317px; POSITION: absolute; TOP: 184px">Código do 
Cedente</DIV>
<DIV class=Titulo11 
style="Z-INDEX: 2; LEFT: 438px; POSITION: absolute; TOP: 184px">Espécie</DIV>
<DIV class=Titulo11 
style="Z-INDEX: 2; LEFT: 480px; POSITION: absolute; TOP: 184px">Quantidade</DIV>
<DIV class=Titulo11 
style="Z-INDEX: 2; LEFT: 536px; POSITION: absolute; TOP: 184px">Nosso 
número</DIV>
<DIV class=Titulo11 
style="Z-INDEX: 2; LEFT: 15px; POSITION: absolute; TOP: 209px">Número do 
documento</DIV>
<DIV class=Titulo11 
style="Z-INDEX: 2; LEFT: 135px; POSITION: absolute; TOP: 209px">Contrato</DIV>
<DIV class=Titulo11 
style="Z-INDEX: 2; LEFT: 214px; POSITION: absolute; TOP: 209px">CPF/CEI/CNPJ</DIV>
<DIV class=Titulo11 
style="Z-INDEX: 2; LEFT: 353px; POSITION: absolute; TOP: 209px">Vencimento</DIV>
<DIV class=Titulo11 
style="Z-INDEX: 2; LEFT: 480px; POSITION: absolute; TOP: 209px">Valor 
documento</DIV>
<DIV class=Titulo11 
style="Z-INDEX: 2; LEFT: 15px; POSITION: absolute; TOP: 234px">(-)Desconto/Abatimento</DIV>
<DIV class=Titulo11 
style="Z-INDEX: 2; LEFT: 135px; POSITION: absolute; TOP: 234px">(-)Outras 
opções</DIV>
<DIV class=Titulo11 
style="Z-INDEX: 2; LEFT: 251px; POSITION: absolute; TOP: 234px">(+)Mora/Multa</DIV>
<DIV class=Titulo11 
style="Z-INDEX: 2; LEFT: 367px; POSITION: absolute; TOP: 234px">(+)Outros 
acréscimos</DIV>
<DIV class=Titulo11 
style="Z-INDEX: 2; LEFT: 480px; POSITION: absolute; TOP: 234px">(=)Valor 
cobrado</DIV>
<DIV class=Titulo11 
style="Z-INDEX: 2; LEFT: 15px; POSITION: absolute; TOP: 259px">Sacado</DIV>
<DIV class=Titulo11 
style="Z-INDEX: 2; LEFT: 15px; POSITION: absolute; TOP: 284px">Instruções</DIV>
<DIV class=Titulo11 
style="Z-INDEX: 2; LEFT: 574px; POSITION: absolute; TOP: 284px">Autenticação 
Mecânica</DIV>
<DIV class=Titulo11 
style="Z-INDEX: 2; LEFT: 7px; POSITION: absolute; TOP: 415px">Corte na linha 
pontilhada</DIV>
<DIV class=Titulo11 
style="Z-INDEX: 2; LEFT: 5px; POSITION: absolute; TOP: 426px"><IMG height=5 
alt="" src="boleto_jsp_arquivos/linhaPontilhada.gif" width=650 border=0></DIV>
<DIV class=Titulo11 
style="Z-INDEX: 2; LEFT: 15px; POSITION: absolute; TOP: 467px">Local de 
pagamento</DIV>
<DIV class=Titulo11 
style="Z-INDEX: 2; LEFT: 480px; POSITION: absolute; TOP: 467px">Vencimento</DIV>
<DIV class=Titulo11 
style="Z-INDEX: 2; LEFT: 15px; POSITION: absolute; TOP: 492px">Cedente</DIV>
<DIV class=Titulo11 
style="Z-INDEX: 2; LEFT: 480px; POSITION: absolute; TOP: 492px">Agência/Código 
do cedente</DIV>
<DIV class=Titulo11 
style="Z-INDEX: 2; LEFT: 15px; POSITION: absolute; TOP: 517px">Data do 
documento</DIV>
<DIV class=Titulo11 
style="Z-INDEX: 2; LEFT: 133px; POSITION: absolute; TOP: 517px">Nº do 
documento</DIV>
<DIV class=Titulo11 
style="Z-INDEX: 2; LEFT: 295px; POSITION: absolute; TOP: 517px">Espécie 
DOC</DIV>
<DIV class=Titulo11 
style="Z-INDEX: 2; LEFT: 364px; POSITION: absolute; TOP: 517px">Aceite</DIV>
<DIV class=Titulo11 
style="Z-INDEX: 2; LEFT: 405px; POSITION: absolute; TOP: 517px">Data 
process.</DIV>
<DIV class=Titulo11 
style="Z-INDEX: 2; LEFT: 480px; POSITION: absolute; TOP: 517px">Nosso 
Número</DIV>
<DIV class=Titulo11 
style="Z-INDEX: 2; LEFT: 15px; POSITION: absolute; TOP: 542px">Uso do 
Banco/Convênio</DIV>
<DIV class=Titulo11 
style="Z-INDEX: 2; LEFT: 133px; POSITION: absolute; TOP: 542px">Carteira</DIV>
<DIV class=Titulo11 
style="Z-INDEX: 2; LEFT: 223px; POSITION: absolute; TOP: 542px">Espécie</DIV>
<DIV class=Titulo11 
style="Z-INDEX: 2; LEFT: 283px; POSITION: absolute; TOP: 542px">Quantidade</DIV>
<DIV class=Titulo11 
style="Z-INDEX: 2; LEFT: 405px; POSITION: absolute; TOP: 542px">Valor</DIV>
<DIV class=Titulo11 
style="Z-INDEX: 2; LEFT: 480px; POSITION: absolute; TOP: 542px">(=) Valor do 
documento</DIV>
<DIV class=Titulo11 
style="Z-INDEX: 2; LEFT: 15px; POSITION: absolute; TOP: 567px">Instrucoes</DIV>
<DIV class=Titulo11 
style="Z-INDEX: 2; LEFT: 480px; POSITION: absolute; TOP: 567px">(-) Desconto / 
Abatimento</DIV>
<DIV class=Titulo11 
style="Z-INDEX: 2; LEFT: 480px; POSITION: absolute; TOP: 592px">(-) Outras 
deduções</DIV>
<DIV class=Titulo11 
style="Z-INDEX: 2; LEFT: 480px; POSITION: absolute; TOP: 618px">(+) Mora / 
Multa</DIV>
<DIV class=Titulo11 
style="Z-INDEX: 2; LEFT: 480px; POSITION: absolute; TOP: 643px">(+) Outros 
acréscimos</DIV>
<DIV class=Titulo11 
style="Z-INDEX: 2; LEFT: 480px; POSITION: absolute; TOP: 668px">(=) Valor 
cobrado</DIV>
<DIV class=Titulo11 
style="Z-INDEX: 2; LEFT: 463px; POSITION: absolute; TOP: 573px">27</DIV>
<DIV class=Titulo11 
style="Z-INDEX: 2; LEFT: 463px; POSITION: absolute; TOP: 620px">35</DIV>
<DIV class=Titulo11 
style="Z-INDEX: 2; LEFT: 463px; POSITION: absolute; TOP: 596px">19</DIV>
<DIV class=Titulo11 
style="Z-INDEX: 2; LEFT: 15px; POSITION: absolute; TOP: 693px">Sacado</DIV>
<DIV class=Titulo11 
style="Z-INDEX: 2; LEFT: 480px; POSITION: absolute; TOP: 739px">Cód. baixa</DIV>
<DIV class=Titulo11 
style="Z-INDEX: 2; LEFT: 15px; POSITION: absolute; TOP: 740px">Sacador/Avalista</DIV>
<DIV class=Titulo11 
style="Z-INDEX: 2; LEFT: 470px; POSITION: absolute; TOP: 751px">Autenticação 
mecânica - Ficha de Compensação</DIV>
<DIV class=Campo 
style="Z-INDEX: 2; LEFT: 15px; POSITION: absolute; TOP: 197px">IATE CLUBE DE BRASILIA</DIV>
<DIV class=Campo 
style="Z-INDEX: 2; LEFT: 317px; WIDTH: 120px; POSITION: absolute; TOP: 197px">${s.codigoCedente}</DIV>
<DIV class=Campo 
style="Z-INDEX: 2; LEFT: 438px; WIDTH: 28px; POSITION: absolute; TOP: 197px">R$</DIV>
<DIV class=Campo 
style="Z-INDEX: 2; LEFT: 480px; WIDTH: 50px; POSITION: absolute; TOP: 197px">0001</DIV>
<DIV class=Campo 
style="Z-INDEX: 2; LEFT: 531px; WIDTH: 118px; POSITION: absolute; TOP: 197px; TEXT-ALIGN: right">${s.nossoNumero}</DIV>
<DIV class=Campo 
style="Z-INDEX: 2; LEFT: 15px; WIDTH: 110px; POSITION: absolute; TOP: 222px">1</DIV>
<DIV class=Campo 
style="Z-INDEX: 2; LEFT: 135px; WIDTH: 70px; POSITION: absolute; TOP: 222px"></DIV>
<DIV class=Campo 
style="Z-INDEX: 2; LEFT: 214px; WIDTH: 125px; POSITION: absolute; TOP: 222px"></DIV>
<DIV class=Campo 
style="Z-INDEX: 2; LEFT: 353px; WIDTH: 118px; POSITION: absolute; TOP: 222px"><fmt:formatDate value="${s.dataPagamento}" pattern="dd/MM/yyyy"/></DIV>

<DIV class=Campo 
style="Z-INDEX: 2; LEFT: 480px; WIDTH: 169px; POSITION: absolute; TOP: 222px; TEXT-ALIGN: right"><fmt:formatNumber value="${s.valor}" pattern="#,##0.00"/></DIV>
<DIV class=Campo 
style="Z-INDEX: 2; LEFT: 15px; WIDTH: 108px; POSITION: absolute; TOP: 247px; TEXT-ALIGN: right"></DIV>
<DIV class=Campo 
style="Z-INDEX: 2; LEFT: 133px; WIDTH: 107px; POSITION: absolute; TOP: 247px; TEXT-ALIGN: right"></DIV>
<DIV class=Campo 
style="Z-INDEX: 2; LEFT: 248px; WIDTH: 108px; POSITION: absolute; TOP: 247px; TEXT-ALIGN: right"></DIV>
<DIV class=Campo 
style="Z-INDEX: 2; LEFT: 361px; WIDTH: 108px; POSITION: absolute; TOP: 247px; TEXT-ALIGN: right"></DIV>
<DIV class=Campo 
style="Z-INDEX: 2; LEFT: 480px; WIDTH: 169px; POSITION: absolute; TOP: 247px; TEXT-ALIGN: right"></DIV>
<DIV class=Campo 
style="Z-INDEX: 2; LEFT: 15px; POSITION: absolute; TOP: 272px">${s.noSacado}</DIV>
<DIV class=DetParcela 
style="Z-INDEX: 2; LEFT: 15px; WIDTH: 498px; POSITION: absolute; TOP: 298px;"><LAYER 
width="498px" name="helpns" align="left">
<br><br><br><br>
***********************  NAO RECEBER APOS O VENCIMENTO  ************************
</LAYER></DIV>
<DIV class=Campo 
style="Z-INDEX: 2; LEFT: 15px; POSITION: absolute; TOP: 480px">Pagável em 
qualquer Banco até o vencimento</DIV>
<DIV class=Campo 
style="Z-INDEX: 2; LEFT: 480px; WIDTH: 169px; POSITION: absolute; TOP: 480px; TEXT-ALIGN: right"><fmt:formatDate value="${s.dataPagamento}" pattern="dd/MM/yyyy"/></DIV>
<DIV class=Campo 
style="Z-INDEX: 2; LEFT: 15px; POSITION: absolute; TOP: 505px">IATE CLUBE DE BRASILIA</DIV>
<DIV class=Campo 
style="Z-INDEX: 2; LEFT: 480px; WIDTH: 169px; POSITION: absolute; TOP: 505px; TEXT-ALIGN: right">${s.codigoCedente}</DIV>

<DIV class=Campo 
style="Z-INDEX: 2; LEFT: 15px; WIDTH: 110px; POSITION: absolute; TOP: 530px"><fmt:formatDate value="${s.dtDocumento}" pattern="dd/MM/yyyy"/></DIV>
<DIV class=Campo 
style="Z-INDEX: 2; LEFT: 133px; WIDTH: 160px; POSITION: absolute; TOP: 530px">1</DIV>
<DIV class=Campo 
style="Z-INDEX: 2; LEFT: 295px; WIDTH: 60px; POSITION: absolute; TOP: 530px">R$</DIV>
<DIV class=Campo 
style="Z-INDEX: 2; LEFT: 364px; WIDTH: 35px; POSITION: absolute; TOP: 530px">N</DIV>

<DIV class=Campo 
style="Z-INDEX: 2; LEFT: 405px; WIDTH: 60px; POSITION: absolute; TOP: 530px"><fmt:formatDate value="${s.dtProcessamento}" pattern="dd/MM/yyyy"/></DIV>
<DIV class=Campo 
style="Z-INDEX: 2; LEFT: 480px; WIDTH: 169px; POSITION: absolute; TOP: 530px; TEXT-ALIGN: right">${s.nossoNumero}</DIV>
<DIV class=Campo 
style="Z-INDEX: 2; LEFT: 15px; POSITION: absolute; TOP: 556px"></DIV>
<DIV class=Campo 
style="Z-INDEX: 2; LEFT: 133px; POSITION: absolute; TOP: 556px">017</DIV>
<DIV class=Campo 
style="Z-INDEX: 2; LEFT: 223px; POSITION: absolute; TOP: 556px">R$</DIV>
<DIV class=Campo 
style="Z-INDEX: 2; LEFT: 283px; POSITION: absolute; TOP: 556px">0001</DIV>
<DIV class=Campo 
style="Z-INDEX: 2; LEFT: 405px; WIDTH: 65px; POSITION: absolute; TOP: 556px; TEXT-ALIGN: right"><fmt:formatNumber value="${s.valor}" pattern="#,##0.00"/></DIV>
<DIV class=Campo 
style="Z-INDEX: 2; LEFT: 480px; WIDTH: 169px; POSITION: absolute; TOP: 556px; TEXT-ALIGN: right"><fmt:formatNumber value="${s.valor}" pattern="#,##0.00"/></DIV>
<DIV class=DetParcela  
style="Z-INDEX: 2; LEFT: 15px; WIDTH: 430px; POSITION: absolute; TOP: 578px; "><LAYER 
width="430px" name="helpns" align="left">
<br><br><br><br>
***********************  NAO RECEBER APOS O VENCIMENTO  ************************
</LAYER></DIV>
<DIV class=Campo 
style="Z-INDEX: 2; LEFT: 480px; WIDTH: 169px; POSITION: absolute; TOP: 580px; TEXT-ALIGN: right"></DIV>
<DIV class=Campo 
style="Z-INDEX: 2; LEFT: 480px; WIDTH: 169px; POSITION: absolute; TOP: 605px; TEXT-ALIGN: right"></DIV>
<DIV class=Campo 
style="Z-INDEX: 2; LEFT: 480px; WIDTH: 169px; POSITION: absolute; TOP: 631px; TEXT-ALIGN: right"></DIV>
<DIV class=Campo 
style="Z-INDEX: 2; LEFT: 480px; WIDTH: 169px; POSITION: absolute; TOP: 656px; TEXT-ALIGN: right"></DIV>
<DIV class=Campo 
style="Z-INDEX: 2; LEFT: 480px; WIDTH: 169px; POSITION: absolute; TOP: 581px; TEXT-ALIGN: right"></DIV>
<DIV class=Campo 
style="Z-INDEX: 2; LEFT: 15px; POSITION: absolute; TOP: 704px">${s.noSacado}</DIV>
<DIV class=Campo 
style="Z-INDEX: 2; LEFT: 15px; POSITION: absolute; TOP: 717px">${s.edSacado}</DIV>
<DIV class=Campo 
style="Z-INDEX: 2; LEFT: 15px; POSITION: absolute; TOP: 729px">${s.edSacadoCompl}</DIV>
<DIV class=Titulo11 
style="Z-INDEX: 2; LEFT: 7px; POSITION: absolute; TOP: 830px">Corte na linha 
pontilhada</DIV>
<DIV class=Titulo11 
style="Z-INDEX: 2; LEFT: 7px; POSITION: absolute; TOP: 827px"><IMG height=5 
alt="" src="boleto_jsp_arquivos/linhaPontilhada.gif" width=650 border=0></DIV>
<DIV class=Titulo11 
style="Z-INDEX: 2; LEFT: 12px; POSITION: absolute; TOP: 760px; HEIGHT: 70px">
<TABLE cellSpacing=0 cellPadding=0 width=666 border=0>
  <TBODY>
  <TR>
    <TD colSpan=2>
        ${codBar}
    </TD></TR></TBODY></TABLE></DIV>
<BR>
<BR><BR><BR></BODY></HTML>


