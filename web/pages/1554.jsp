<%@page contentType="text/html" pageEncoding="ISO-8859-1"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>

<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
        <title>Autoriza��o de Embarque</title>
    </head>
    <body>
        <table border="1" width="100%">
            <tr>
                <td>Autoriza��o de Embarque<BR>Diretoria de Esportes N�uticos</td>
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
                    <td>Emiss�o: ${dataEmissao}</td>
                    <fmt:formatDate var="dataValidade" value="${autorizacao.dataValidade}" pattern="dd/MM/yyyy" />
                    <td>V�lida p/ o dia: ${dataValidade}</td>                    
                </tr>
            </table>
            </tr>            
            <tr>
            <table border="1" width="100%">
                <tr>
                    <td>Embarca��o: ${autorizacao.embarcacao}</td>
                    <td>Capacidade: ${autorizacao.capacidade}</td>                    
                </tr>
            </table>
            </tr>                        
            <tr>
            <table border="1" width="100%">
                <tr>
                    <td>S�cio:</td>
                </tr>
                <tr>
                    <td>${autorizacao.responsavel.nome}</td>
                </tr>                
            </table>
            </tr>            
            <tr>
                <td>
                    Instru��es:<BR>
                    -Obrigat�rio apresenta��o de identifica��o.<BR>
                    -V�lido somente para embarque nessa data.
                </td>   
            </tr>
            <tr>
                <td>
                    __________________<BR>
                    Diretoria N�utica
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
