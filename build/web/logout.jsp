<%
    request.getSession().invalidate();
    if (request.getParameter("destino")!=null){
        response.sendRedirect(request.getParameter("destino"));   
    }else{
        response.sendRedirect("index.html");   
    }
    
%>
