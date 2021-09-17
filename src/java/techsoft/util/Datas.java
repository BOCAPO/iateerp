
package techsoft.util;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public final class Datas {

    public static Date parseComFormato(String data, String formato){
        SimpleDateFormat f = new SimpleDateFormat(formato);
        try{
            Date d = f.parse(data);
            return d;
        }catch(Exception e){
            return null;
        }
    }
    
    public static Date parse(String data){
        SimpleDateFormat f = new SimpleDateFormat("dd/MM/yyyy");
        try{
            Date d = f.parse(data);
            return d;
        }catch(Exception e){
            return null;
        }
    }

    public static Date parse(String data, String hora){
        SimpleDateFormat f = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        try{
            Date d = f.parse(data + " " + hora);
            return d;
        }catch(Exception e){
            return null;
        }
    }
    
    public static Date parseDataHora(String dataHora){
        SimpleDateFormat f = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        try{
            Date d = f.parse(dataHora);
            return d;
        }catch(Exception e){
            return null;
        }
    }    
    public static String toSqlIsoDate(Date date){
        Calendar cal = Calendar.getInstance();           
        cal.setTime(date);
        String sql = "'" + cal.get(Calendar.YEAR) + "-"
                + cal.get(Calendar.MONTH) + "-"
                + cal.get(Calendar.DAY_OF_MONTH) + "'";

        return sql;
    }
    
    public static String toSqlIsoTimestamp(Date date){
        Calendar cal = Calendar.getInstance();           
        cal.setTime(date);
        String sql = "'" + cal.get(Calendar.YEAR) + "-"
                + cal.get(Calendar.MONTH) + "-"
                + cal.get(Calendar.DAY_OF_MONTH) + " "
                + cal.get(Calendar.HOUR_OF_DAY) + ":"
                + cal.get(Calendar.MINUTE) + ":"
                + cal.get(Calendar.SECOND) + "'";

        return sql;        
    }
    
    public static String nomeMes(int mes) {
        String[] nome = {"Janeiro",
                         "Fevereiro",
                         "Março",
                         "Abril",
                         "Maio",
                         "Junho",
                         "Julho",
                         "Agosto",
                         "Setembro",
                         "Outubro",
                         "Novembro",
                         "Dezembro"};
        if ((mes >= 1) && (mes <= 12)) {
            return nome[mes - 1];
        } else {
            return "";
        }
    }
}
