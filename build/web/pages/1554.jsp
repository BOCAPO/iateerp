<%@page contentType="text/html" pageEncoding="ISO-8859-1"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>

<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
        <title>Autorização de Embarque</title>
    </head>
    <body>
        <table border="1" width="100%">
            <tr>
                <td>Autorização de Embarque<BR>Diretoria de Esportes Náuticos</td>
            </tr>
            <tr>
            <table border="1" width="100%">
                <tr>
                    <td>Convidado:</td>
                </tr>
                <tr>
                    <td>${autorizacao.convidado}</td>
                </tr>                
            </table>
            </tr>
            <tr>
            <table border="1" width="100%">
                <tr>
                    <fmt:formatDate var="dataEmissao" value="${autorizacao.dataEmissao}" pattern="dd/MM/yyyy" />
                    <td>Emissão: ${dataEmissao}</td>
                    <fmt:formatDate var="dataValidade" value="${autorizacao.dataValidade}" pattern="dd/MM/yyyy" />
                    <td>Válida p/ o dia: ${dataValidade}</td>                    
                </tr>
            </table>
            </tr>            
            <tr>
            <table border="1" width="100%">
                <tr>
                    <td>Embarcação: ${autorizacao.embarcacao}</td>
                    <td>Capacidade: ${autorizacao.capacidade}</td>                    
                </tr>
            </table>
            </tr>                        
            <tr>
            <table border="1" width="100%">
                <tr>
                    <td>Sócio:</td>
                </tr>
                <tr>
                    <td>${autorizacao.responsavel.nome}</td>
                </tr>                
            </table>
            </tr>            
            <tr>
                <td>
                    Instruções:<BR>
                    -Obrigatório apresentação de identificação.<BR>
                    -Válido somente para embarque nessa data.
                </td>   
            </tr>
            <tr>
                <td>
                    __________________<BR>
                    Diretoria Náutica
                </td>
            </tr>                
            <tr>
                <td>${autorizacao.numAutorizacao}</td>
            </tr>                                
            <tr>
                <td>CODIGO DE BARRAS  --->${autorizacao.numAutorizacao}<--- CODIGO DE BARRAS</td>
            </tr>                                            
        </table>
    </body>
</html>
