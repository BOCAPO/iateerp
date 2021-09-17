<!DOCTYPE html>
<%@page contentType="text/html" pageEncoding="utf-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@page import="java.text.*"%>
<%@page import="java.util.Date"%>
<%@page import="techsoft.db.Pool"%>
<%@page import="java.sql.*"%>

<fmt:setLocale value="pt_BR"/>
<html>
    <!-- ${pageContext.request.servletPath} -->
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
        <link rel="icon" href="imagens/icones/favicon.ico" type="image/x-icon"/>
        <link rel="shortcut icon" href="imagens/icones/favicon.ico" type="image/x-icon"/>
	<title>Iate Clube de Brasília</title>

<style type="text/css">
.tituloIate {
	font-family: Georgia, "Times New Roman", Times, serif;
	font-size: 20px;
	font-weight: bold;
	color: #00F;
}

.tituloConvite{
	font-family: "Times New Roman", Times, serif;
	font-size: 24px;
	font-weight: bold;
}
.textoPreenchimentoObrigatorio{
	font-family: "Times New Roman", Times, serif;
	font-size: 20px;
	font-weight: bold;
}
.subTituloConvite{
	font-family: "Times New Roman", Times, serif;
	font-size: 12px;
	font-weight: bold;
        color: gray;
}
.numeroConvite{
	font-family: "Times New Roman";
	font-size: 9px;
	font-weight: bold;
        font-style: italic;
}
.codBarras{
	font-family: "C39P36DlTt";
	font-size: 40px;
}
.tituloInstrucoes{
	font-family: "MS Sans Serif";
	font-size: 11px;
}
.tituloLimiteUtil{
	font-family: "MS Sans Serif";
	font-size: 11px;
        color: red;
}
.textoSocioEstacionamento{
	font-family: "Times New Roman";
	font-size: 11px;
}
.textoInstrucoes{
	font-family: "Times New Roman";
	font-size: 9px;
}

.textoVersoGeral{
	font-family: "Times New Roman";
	font-size: 13px;
}
.textoConvComodoro{
	font-family: "Times New Roman";
	font-size: 11px;
}
.textoRAC{
	font-family: "Times New Roman";
	font-size: 11px;
}

.bordaExterna{
        position:relative; 
        border:1px solid black;
}

.quebra { 
    page-break-after: always; 
} 
</style>
</head>
<body>
<%

SimpleDateFormat fmtDate = new SimpleDateFormat("dd/MM/yyyy");


String nrConvites = request.getParameter("idConvite");
String sql = "SELECT " 
               + "T1.NR_CONVITE, " 
               + "T1.CD_CATEGORIA, " 
               + "T1.CD_MATRICULA, " 
               + "T1.SEQ_DEPENDENTE, " 
               + "T1.DT_RETIRADA, " 
               + "T1.DT_MAX_UTILIZACAO, " 
               + "T1.NOME_SACADOR, " 
               + "T1.NOME_CONVIDADO, " 
               + "T1.DT_NASC_CONVIDADO, " 
               + "T1.CD_CATEGORIA_CONVITE, " 
               + "T1.CD_STATUS_CONVITE, " 
               + "T1.DT_VALID_EX_MEDICO_CONVITE, " 
               + "T1.USER_ACESSO_SISTEMA, " 
               + "CASE ESTACIONAMENTO_INTERNO WHEN 0 THEN 'PÚBLICO' ELSE 'INTERNO' END ESTACIONAMENTO, " 
               + "T1.CD_CONVENIO, " 
               + "T1.CD_TIPO_CONVITE, " 
               + "T1.NR_CHURRASQUEIRA, " 
               + "T1.CD_LOTE_FAMILIA, " 
               + "T1.DT_UTILIZACAO, " 
               + "T1.SEQ_RESERVA, " 
               + "T2.DESCR_TIPO_CONVITE, " 
               + "T2.ALT_COD_BARRAS, " 
               + "T2.ALT_TOPO_TABELA, " 
               + "T2.ALT_LINHA_FINA, " 
               + "T2.ALT_LINHA_GROSSA, " 
               + "T2.ALT_INSTRUCOES, " 
               + "T2.TXT_VALIDADE, " 
               + "T2.TXT_INSTRUCOES, " 
               + "T2.DE_RESPONSAVEL, "
               + "CASE WHEN CPF_CONVIDADO IS NULL THEN '' ELSE SUBSTRING(RIGHT('00000000000' + CPF_CONVIDADO, 11), 1, 3) + '.' + SUBSTRING(RIGHT('00000000000' + CPF_CONVIDADO, 11), 4, 3) + '.' + SUBSTRING(RIGHT('00000000000' + CPF_CONVIDADO, 11), 7, 3) + '-' + SUBSTRING(RIGHT('00000000000' + CPF_CONVIDADO, 11), 10, 2) END CPF_CONVIDADO, "
               + "RIGHT('00' + CONVERT(VARCHAR, CD_CATEGORIA), 2) + '/' + RIGHT('0000' + CONVERT(VARCHAR, CD_MATRICULA), 4) + ' - ' + NOME_SACADOR NOME_SACADOR_TITULO, "
               + "CASE WHEN HH_ENTRADA IS NULL THEN '' ELSE SUBSTRING(RIGHT('0000' + HH_ENTRADA, 4), 1, 2) + ':' + SUBSTRING(RIGHT('0000' + HH_ENTRADA, 4), 3, 2) END HH_ENTRADA, "
               + "CASE WHEN HH_SAIDA IS NULL THEN '' ELSE SUBSTRING(RIGHT('0000' + HH_SAIDA, 4), 1, 2) + ':' + SUBSTRING(RIGHT('0000' + HH_SAIDA, 4), 3, 2) END HH_SAIDA, "
               + "(select nome_pessoa from tb_pessoa where cd_cargo_especial = (select cd_cargo_comodoro from tb_parametro_sistema) and cd_cargo_ativo = 'S') NOME_COMODORO, "
               + "(SELECT DESCR_DEPENDENCIA FROM TB_DEPENDENCIA WHERE SEQ_DEPENDENCIA = T1.NR_CHURRASQUEIRA) DESCR_DEPENDENCIA "
            + "FROM "
                + "TB_CONVITE T1, "
                + "TB_TIPO_CONVITE T2 "
            + "WHERE "
                + "T1.CD_STATUS_CONVITE = 'NU' AND "
                + "T1.CD_CATEGORIA_CONVITE = T2.CD_TIPO_CONVITE AND "
                + "NR_CONVITE IN (" + nrConvites + ")";

//FALTA FAZER O TRATAMENTO DAS ALTURAS, TAMANHOS E ASSINATURA DOS CONVITES ESPECIAIS
//FORMATACAO DO CPF, HORARIO DA CHURRASQUEIRA E NOME DO SOCIO NO VERSO DO CONVITE GERAL
Connection cn = Pool.getInstance().getConnection();
ResultSet rs = cn.createStatement().executeQuery(sql);
int lin = 0;
int col = 0;
int qtConvite = 0;  
int codBarras = 0;
int topoTabela = 0;
int linhaFina = 0;
int linhaGrossa = 0;
int instrucoes = 0;
String textoValidade = "";
String textoInstrucoes = "";
String categoriaConvite = "";%>

<table><tr>
            
<%
while(rs.next()){
    if (qtConvite==4){
        qtConvite=0;
        lin = 0;
        col = 0;%>
        </tr></table><div class="quebra"> </div>
        <table><tr>
<%  }else if (qtConvite==2){%>
        </tr><tr>
<%  }
    
    codBarras = rs.getInt("ALT_COD_BARRAS");
    topoTabela = rs.getInt("ALT_TOPO_TABELA");
    linhaFina = rs.getInt("ALT_LINHA_FINA");
    linhaGrossa = rs.getInt("ALT_LINHA_GROSSA");
    instrucoes = rs.getInt("ALT_INSTRUCOES");
    textoValidade = rs.getString("TXT_VALIDADE");
    textoInstrucoes = rs.getString("TXT_INSTRUCOES");
    categoriaConvite = rs.getString("CD_CATEGORIA_CONVITE");
    //****************************************************************
    //****************************************************************
    //INICIO CONVITE COMODORO
    //****************************************************************
    //****************************************************************
    if ("CO".equals(categoriaConvite)) {
        
%>      <td>
        <div class="bordaExterna" style="top:<%=lin%>px;left:<%=col%>px;width:310px;height:420px;">    
            <div style="position:absolute;top:2px;left:2px;">
                <img src="imagens/logo-intro.png" width="60" height="45"/>
            </div>    
            <div style="position:absolute;top:3px;left:62px;width:240px;" align="center">
                <span class="tituloConvite">Convite Individual</span> 
            </div>    
            <div style="position:absolute;top:31px;left:62px;width:240px;" align="center">
                <span class="subTituloConvite">INSTITUCIONAL</span> 
            </div>    

            <div style="position:absolute;top:90px;left:0px;width:300px;" align="center">
                <span class="textoVersoGeral">
                    O Comodoro do ICB Convida:<br><br>
                    __________________________________________<br>
                    para frequentar o clube até <%=fmtDate.format(rs.getDate("DT_MAX_UTILIZACAO"))%>
                </span> 
            </div>    
            <div style="position:absolute;top:125px;left:0px;width:300px;" align="center">
                <span class="textoVersoGeral"><b><%=rs.getString("NOME_CONVIDADO")%></b></span> 
            </div>    

            <div class="bordaExterna" style="top:220px;left:8px;width:90px;height:80px;"></div>    
            <div style="border-bottom:1px solid black;position:absolute;top:260px;left:8px;width:90px;"/></div>    
            <div style="position:absolute;top:220px;left:15px;" align="left">
                <span class="textoConvComodoro">
                   Estacionamento:<br><b><%=rs.getString("ESTACIONAMENTO")%></b><br>
                </span> 
            </div>    
            <div style="position:absolute;top:260px;left:15px;" align="left">
                <span class="textoConvComodoro">
                   Emissão:<br><b><%=fmtDate.format(rs.getDate("DT_RETIRADA"))%></b>
                </span> 
            </div>    


            <div style="position:absolute;top:255px;left:100px;width:210px;" align="center">
                <span class="textoConvComodoro">
                    __________________________________<br>
                    <%=rs.getString("NOME_SACADOR")%><br>
                    Comodoro
                </span> 
            </div>    


            <!--numero do convite e código de barras-->    
            <div style="position:absolute;top:350px;left:0px;width:294px;height:20px;" align="center">
                <span class="numeroConvite"><%=rs.getString("NR_CONVITE")%></span> 
            </div>    
            <div style="position:absolute;top:370px;left:0px;width:294px;height:50px;" align="center">
                <span class="codBarras">!<%=rs.getString("NR_CONVITE")%>!</span> 
            </div>    
        </div>
        </td>
<%  
        qtConvite = qtConvite + 1;            
    }else{
        //****************************************************************
        //****************************************************************
        //INICIO DEMAIS CONVITES
        //****************************************************************
        //****************************************************************
%>    
        <td>
        <div class="bordaExterna" style="top:<%=lin%>px;left:<%=col%>px;width:310px;height:420px;">    
            <div style="position:absolute;top:2px;left:2px;">
                <img src="imagens/logo-intro.png" width="60" height="45"/>
            </div>    
            <div style="position:absolute;top:3px;left:62px;width:240px;" align="center">
                <span class="tituloConvite">Convite Individual</span> 
            </div>    
            <div style="position:absolute;top:31px;left:62px;width:240px;" align="center">
                <span class="subTituloConvite"><%=rs.getString("DESCR_TIPO_CONVITE")%></span> 
            </div>    

            <div style="position:absolute;top:50px;left:0px;">
                <img src="imagens/mensagemIdentidade.GIF" width="310" height="50"/>
            </div>    

            <!--montando a caixa central com informacoes do convidado, socio, convite e instrucoes-->    
            <div class="bordaExterna" style="top:<%=topoTabela%>px;left:8px;width:294px;height:<%=(linhaFina*4)+linhaGrossa+instrucoes%>px;"></div>    
            <div class="tituloInstrucoes" style="position:absolute;top:<%=topoTabela%>px;left:8px;">&nbsp;Convidado: <%=rs.getString("NOME_CONVIDADO")%></div>    
            <div style="border-bottom:1px solid black;position:absolute;top:<%=topoTabela+linhaFina%>px;left:8px;width:294px;"/></div>    
            <div class="tituloInstrucoes" style="position:absolute;top:<%=topoTabela+linhaFina%>px;left:8px;">&nbsp;CPF: <%=rs.getString("CPF_CONVIDADO")%></div>    
            <div style="border-bottom:1px solid black;position:absolute;top:<%=topoTabela+(linhaFina*2)%>px;left:8px;width:294px;"></div>    
            <div class="tituloInstrucoes" style="position:absolute;top:<%=topoTabela+(linhaFina*2)%>px;left:8px;">&nbsp;Emissão: <%=fmtDate.format(rs.getDate("DT_RETIRADA"))%></div>    
            <div style="border-bottom:1px solid black;position:absolute;top:<%=topoTabela+(linhaFina*3)%>px;left:8px;width:294px;"></div>    
            <div style="border-left:1px solid black;position:absolute;top:<%=topoTabela+linhaFina%>px;left:130px;height:<%=linhaFina*2%>px;"></div>    
            <div class="tituloInstrucoes" style="position:absolute;top:<%=topoTabela+linhaFina%>px;left:130px;">&nbsp;Dt. Nascimento: <%if (rs.getDate("DT_NASC_CONVIDADO") != null){out.print(fmtDate.format(rs.getDate("DT_NASC_CONVIDADO")));}%></div>    
            <div class="tituloLimiteUtil" style="position:absolute;top:<%=topoTabela+(linhaFina*2)%>px;left:130px;"><b>&nbsp;<%=textoValidade%> <%=fmtDate.format(rs.getDate("DT_MAX_UTILIZACAO"))%></b></div>    
            <div class="tituloInstrucoes" style="position:absolute;top:<%=topoTabela+(linhaFina*3)%>px;left:8px;"><b>&nbsp;ESTACIONAMENTO: <%=rs.getString("ESTACIONAMENTO")%></b></div>    
            <div style="border-bottom:1px solid black;position:absolute;top:<%=topoTabela+(linhaFina*4)%>px;left:8px;width:294px;"></div>    
            <div class="tituloInstrucoes" style="position:absolute;top:<%=topoTabela+(linhaFina*4)%>px;left:8px;">&nbsp;<%=rs.getString("DE_RESPONSAVEL")%><br><b>&nbsp;<%=rs.getString("NOME_SACADOR")%></b></div>    
            <div style="border-bottom:1px solid black;position:absolute;top:<%=topoTabela+(linhaFina*4)+linhaGrossa%>px;left:8px;width:294px;"></div>    
            <div class="tituloInstrucoes" style="position:absolute;top:<%=topoTabela+(linhaFina*4)+linhaGrossa%>px;left:8px;">&nbsp;Instruções:</div>    
            <div class="textoInstrucoes" style="position:absolute;top:<%=topoTabela+(linhaFina*4)+linhaGrossa+15%>px;left:10px;"><%=textoInstrucoes%></div>    

<%
            if (categoriaConvite.equals("CH")){
%>           
                <!--Para churrasqueira imprime o numero da churrasqueira e o horario-->    
                <div style="border:1px solid black;position:absolute;top:<%=topoTabela+(linhaFina*4)+linhaGrossa+instrucoes+5%>px;left:20px;width:265px;height:20px;"></div>    
                <div style="border-left:1px solid black;position:absolute;top:<%=topoTabela+(linhaFina*4)+linhaGrossa+instrucoes+5%>px;left:145px;height:20px;"></div>    
                <div class="tituloInstrucoes" style="position:absolute;top:<%=topoTabela+(linhaFina*4)+linhaGrossa+instrucoes+8%>px;left:20px;">&nbsp;<%=rs.getString("DESCR_DEPENDENCIA")%></div>    
                <div class="tituloInstrucoes" style="position:absolute;top:<%=topoTabela+(linhaFina*4)+linhaGrossa+instrucoes+8%>px;left:150px;">&nbsp;Horário: <%=rs.getString("HH_ENTRADA")%> às <%=rs.getString("HH_SAIDA")%></div>    
<%
            }
            if (categoriaConvite.equals("CH") || categoriaConvite.equals("SA") || categoriaConvite.equals("SI")){
%>           
                <!--numero do convite e código de barras para convite de sauna, sinuca e churras -->
                <div style="position:absolute;top:273px;left:0px;width:294px;height:20px;" align="center">
                    <span class="numeroConvite"><%=rs.getString("NR_CONVITE")%></span> 
                </div>    
                <div style="position:absolute;top:289px;left:0px;width:294px;height:50px;" align="center">
                    <span class="codBarras">!<%=rs.getString("NR_CONVITE")%>!</span> 
                </div>    
                <div style="border-bottom:1px dashed black;position:absolute;top:334px;left:8px;width:294px;"></div>    
                <div style="border-bottom:1px solid black;position:absolute;top:359px;left:10px;width:290px;"></div>    
            <div class="tituloInstrucoes" style="position:absolute;top:344px;left:8px;">Convidado:</div>    

<%
            }
            if (categoriaConvite.equals("EC") || categoriaConvite.equals("ED") || categoriaConvite.equals("EV") || categoriaConvite.equals("EO") || categoriaConvite.equals("EA") || categoriaConvite.equals("EN") || categoriaConvite.equals("EP")){
%>           
                <!--Para convites especiais imprime a assinatura do comodoro, exceto para especial de socio-->    
                <div style="position:absolute;top:<%=topoTabela+(linhaFina*4)+linhaGrossa+instrucoes+35%>px;left:0px;width:294px;height:55px;" align="center">
                    <span class="textoRAC">
                        ______________________________________<br>
                        <%=rs.getString("NOME_COMODORO")%><br>
                        Comodoro<br>
                    </span> 
                </div>    
<%                    
            }
%>           

            <!--numero do convite e código de barras para todos-->        
            <div style="position:absolute;top:<%=codBarras%>px;left:0px;width:294px;height:20px;" align="center">
                <span class="numeroConvite"><%=rs.getString("NR_CONVITE")%></span> 
            </div>    
            <div style="position:absolute;top:<%=codBarras+15%>px;left:0px;width:294px;height:50px;" align="center">
                <span class="codBarras">!<%=rs.getString("NR_CONVITE")%>!</span> 
            </div> 
        </div>
        </td>
            
<%      
        qtConvite = qtConvite + 1;
        
        //*************************************************-->    
        //*************************************************-->    
        //imprime o verso para convites gerais e de eventos-->    
        //************************************************-->    
        //*************************************************-->    

        if (categoriaConvite.equals("GR") || categoriaConvite.substring(0,1).equals("V")){
%>
            <td>
            <div class="bordaExterna" style="top:<%=lin%>px;left:<%=col%>px;width:310px;height:420px;">    
                <div style="position:absolute;top:3px;left:0;width:300px;" align="center">
                    <span class="textoPreenchimentoObrigatorio">Preenchimento Obrigatório</span> 
                </div>    
                <div style="position:absolute;top:40px;left:20px;width:300px;" align="left">
                    <span class="textoVersoGeral">
                        Eu, ______________________________________,<br>
                        declaro não ter acessado o Iate Clube de Brasília por<br>
                        por mais de 12 (doze) vezes no corrente ano.<br><br>
                        Brasília, _____ de _________________ de 20____.
                    </span> 
                </div>    
                <div style="position:absolute;top:160px;left:0px;width:300px;" align="center">
                    <span class="textoRAC">
                        ______________________________________<br>
                        Assinatura do Convidado
                    </span> 
                </div>    
                <div style="position:absolute;top:200px;left:20px;width:300px;" align="left">
                    <span class="textoRAC">
                        <b>RAC - art 12:</b> O acesso de um mesmo convidado ao Clube <br>
                        será limitado a 12 vezes ao ano, sendo o controle exercido <br>
                        pela Secretaria.
                    </span> 
                </div>    

                <div style="position:absolute;top:290px;left:0px;width:300px;" align="center">
                    <span class="textoRAC">
                        ______________________________________<br>
                        Assinatura do Sócio<br>
                        <%=rs.getString("NOME_SACADOR_TITULO")%>
                    </span> 
                </div>    

                <!--numero do convite e código de barras-->    
                <div style="position:absolute;top:350px;left:0px;width:294px;height:20px;" align="center">
                    <span class="numeroConvite"><%=rs.getString("NR_CONVITE")%></span> 
                </div>    
                <div style="position:absolute;top:370px;left:0px;width:294px;height:50px;" align="center">
                    <span class="codBarras">!<%=rs.getString("NR_CONVITE")%>!</span> 
                </div>    
            </div>    
            </td>
                    
<%
            qtConvite = qtConvite + 1;
        //FIM DO VERSO
        }   
    //FIM DEMAIS CONVITES
    }    
//FIM LOOP
}

cn.close();
%>
</tr></table>

</body>
</html>

