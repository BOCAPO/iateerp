package techsoft.operacoes;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.math.BigDecimal;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.Logger;
import techsoft.db.Pool;
import techsoft.seguranca.Auditoria;

public class ConvenioBB {

    private static final Logger log = Logger.getLogger("techsoft.operacoes.ConvenioBB");
        
    public static void gerarMovItau109(
            Auditoria audit,
            String debitoConta,
            Date dataVencimento,
            Date dataPagamento,
            String categoria
            ){
            Connection cn = null;

        try {
            cn = Pool.getInstance().getConnection();
            CallableStatement cal = null;
            
            cal = cn.prepareCall("{call SP_GERA_MOV_ITAU_NAO_PAGOS (?, ?, ?, ?)}");
        
            cal.setDate(1, new java.sql.Date(dataVencimento.getTime()));
            cal.setDate(2, new java.sql.Date(dataPagamento.getTime()));
            cal.setString(3, categoria);
            cal.setString(4, debitoConta);
            

            cal.execute();
            cn.commit();
            
            audit.registrarMudanca("{call SP_GERA_MOV_ITAU_NAO_PAGOS}", dataVencimento.toString() , dataPagamento.toString(), categoria, debitoConta);            
            
        }catch (SQLException e) {
            log.severe(e.getMessage());
        }finally{
            try {
                cn.close();
            } catch (SQLException e) {
                log.severe(e.getMessage());
            }
        }
    }            
    
    public static void gerarArquivo(
            Auditoria audit,
            boolean debitoConta,
            Date dataVencimento,
            String idCategoria,
            int titulo,
            int idDeclaracao,
            boolean somenteBoleto,
            boolean correspondenciaBoleto,
            boolean naoInformado,
            String arquivo,
            String banco
            ){
            Connection cn = null;

        try {
            cn = Pool.getInstance().getConnection();
            CallableStatement cal = null;
            
            if(banco.equals("B")){
                cal = cn.prepareCall("{call sp_gera_arquivo_bb (?, ?, ?, ?, ?, ?, ?, ?)}");
            }else{
                cal = cn.prepareCall("{call sp_gera_arquivo_itau (?, ?, ?, ?, ?, ?, ?, ?)}");
            }
            
            if(debitoConta){
                cal.setString(1, "S");
            }else{
                cal.setString(1, "N");
            }
            
            cal.setDate(2, new java.sql.Date(dataVencimento.getTime()));
            
            if(titulo > 0){
                cal.setInt(3, titulo);
            }else{
                cal.setNull(3, java.sql.Types.INTEGER);
            }
            
            cal.setString(4, idCategoria);
            
            if(naoInformado){
                cal.setInt(5, 1);
            }else{
                cal.setInt(5, 0);
            }

            if(somenteBoleto){
                cal.setInt(6, 1);
            }else{
                cal.setInt(6, 0);
            }
            
            if(correspondenciaBoleto){
                cal.setInt(7, 1);
            }else{
                cal.setInt(7, 0);
            }
            
            cal.setInt(8, idDeclaracao);

            cal.execute();
            cn.commit();
            audit.registrarMudanca("{call sp_gera_arquivo_bb}");            
            ResultSet rs = cal.getResultSet();
            FileWriter w = new FileWriter(arquivo);
            while(rs.next()){
                w.write(rs.getString("DE_LINHA"));
                w.write("\r\n");
            }
            w.close();

        }catch (SQLException e) {
            log.severe(e.getMessage());
        }catch (IOException e) {
            log.severe(e.getMessage());            
        }finally{
            try {
                cn.close();
            } catch (SQLException e) {
                log.severe(e.getMessage());
            }
        }
    }        
    
    public static ResultadoBaixaArquivoConvenioBB baixarArquivo(
            Auditoria audit,
            int dias,
            String arquivo,
            boolean force,
            String banco)throws ConvenioBBException{
        Connection cn = null;

        ResultadoBaixaArquivoConvenioBB result = null;
        
        try {
            cn = Pool.getInstance().getConnection();
            
            File f = new File(arquivo);
            
            if(!force){
                
                PreparedStatement p = cn.prepareStatement("SELECT DT_BAIXA_ARQUIVO FROM TB_BAIXA_ARQUIVO WHERE NO_ARQUIVO_BAIXA = ?");
                p.setString(1, f.getName().trim().toUpperCase());
                ResultSet rs = p.executeQuery();
                String msg = "ATENÇÃO:<BR><BR>Este arquivo já foi baixado na(s) seguinte(s) data(s): <BR>";
                boolean jaBaixado = false;
                SimpleDateFormat fmt = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
                while(rs.next()){
                    jaBaixado = true;
                    msg += fmt.format(rs.getTimestamp("DT_BAIXA_ARQUIVO"));
                    msg += "<BR>";
                }
                msg += "<BR>Deseja baixá-lo novamente?<BR>";
                p.close();
                if(jaBaixado){
                    throw new ConvenioBBException(msg, true);
                }                
            }

            PreparedStatement p = cn.prepareStatement("INSERT INTO TB_BAIXA_ARQUIVO VALUES(GETDATE(), ?)");
            p.setString(1, f.getName().trim().toUpperCase());
            p.executeUpdate();

            cn.commit();
            
            BufferedReader in = new BufferedReader(new FileReader(f));
            String s = null;
            result = new ResultadoBaixaArquivoConvenioBB();
            
            //verifica o header
            s = in.readLine();
            if(!s.startsWith("0")){
                throw new ConvenioBBException("Arquivo sem header, contate o Banco");
            }            
            CallableStatement cal = null;
                    
            if(banco.equals("B")){
                cal = cn.prepareCall("{call SP_BAIXA_CARNE_BANCO (?, ?, ?)}");
            }else{
                cal = cn.prepareCall("{call SP_BAIXA_CARNE_ITAU (?, ?, ?)}");
            }
            
            /*
             * Os acumuladores abaixo sao usados porque se somar varias vezes um
             * float, a imprecisao do float se acumula a cada soma e no final, o 
             * resultado eh diferente do esperado.
             */
            result.valorAcatado = BigDecimal.ZERO;
            result.valorAvulso = BigDecimal.ZERO;
            result.valorRejeitado = BigDecimal.ZERO;
            result.valorAMais = BigDecimal.ZERO;
            result.valorPgAnt = BigDecimal.ZERO;

            while((s = in.readLine()) != null){
                if(s.startsWith("1") || s.startsWith("7")){
                    result.totalRegistros++;
                
                    cal.setString(1, s);
                    cal.setString(2, f.getName());
                    cal.setInt(3, dias);
                    cal.execute();
                    ResultSet rs = cal.getResultSet();
                    if(rs.next()){
                        result.totalAcatado += rs.getInt("QT_ACATADO");
                        result.valorAcatado = result.valorAcatado.add(rs.getBigDecimal("VR_ACATADO"));
                        result.totalRejeitado += rs.getInt("QT_REJEITADO");
                        result.valorRejeitado = result.valorRejeitado.add(rs.getBigDecimal("VR_REJEITADO"));
                        result.totalAMais += rs.getInt("QT_AMAIS");
                        result.valorAMais = result.valorAMais.add(rs.getBigDecimal("VR_AMAIS"));
                        result.totalAvulso += rs.getInt("QT_AVULSO");
                        result.valorAvulso = result.valorAvulso.add(rs.getBigDecimal("VR_AVULSO"));
                        result.totalPgAnt += rs.getInt("QT_PGANT");
                        result.valorPgAnt = result.valorPgAnt.add(rs.getBigDecimal("VR_PGANT"));
                    }
                    cn.commit();
                }
            }
            
            cn.commit();
            in.close();            
            f.delete();
            audit.registrarMudanca("{call SP_BAIXA_CARNE_BANCO}");
        }catch (SQLException e) {
            log.severe(e.getMessage());
            throw new ConvenioBBException("SQLException -> " + e.getMessage());
        }catch (IOException e) {
            log.severe(e.getMessage());            
            throw new ConvenioBBException("IOException -> " + e.getMessage());
        }finally{
            try {
                cn.close();
            } catch (SQLException e) {
                log.severe(e.getMessage());
            }
        }
        
        return result;
    }        
    
    public static void gerarDCO(
            Auditoria audit,
            Date dataVencimento,
            String arquivo){
        Connection cn = null;

        try {
            cn = Pool.getInstance().getConnection();

            CallableStatement cal = cn.prepareCall("{call SP_GERA_ARQUIVO_DCO (?)}");            
            cal.setDate(1, new java.sql.Date(dataVencimento.getTime()));

            cal.execute();
            cn.commit();
            audit.registrarMudanca("{call SP_GERA_ARQUIVO_DCO}");            
            ResultSet rs = cal.getResultSet();
            FileWriter w = new FileWriter(arquivo);
            while(rs.next()){
                w.write(rs.getString("DE_LINHA"));
                w.write("\r\n");
            }
            w.close();

        }catch (SQLException e) {
            log.severe(e.getMessage());
        }catch (IOException e) {
            log.severe(e.getMessage());            
        }finally{
            try {
                cn.close();
            } catch (SQLException e) {
                log.severe(e.getMessage());
            }
        }
    }        
    
    public static ResultadoBaixaArquivoConvenioBB baixarDCO(
            Auditoria audit,
            Date dataVencimento,
            String arquivo,
            boolean force)throws ConvenioBBException{
        Connection cn = null;

        ResultadoBaixaArquivoConvenioBB result = null;
        
        try {
            cn = Pool.getInstance().getConnection();
            
            File f = new File(arquivo);
            
            if(!force){
                
                PreparedStatement p = cn.prepareStatement("SELECT DT_BAIXA_ARQUIVO FROM TB_BAIXA_ARQUIVO WHERE NO_ARQUIVO_BAIXA = ?");
                p.setString(1, f.getName().trim().toUpperCase());
                ResultSet rs = p.executeQuery();
                String msg = "ATENÇÃO:<BR><BR>Este arquivo já foi baixado na(s) seguinte(s) data(s): <BR>";
                boolean jaBaixado = false;
                SimpleDateFormat fmt = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
                while(rs.next()){
                    jaBaixado = true;
                    msg += fmt.format(rs.getTimestamp("DT_BAIXA_ARQUIVO"));
                    msg += "<BR>";
                }
                msg += "<BR>Deseja baixá-lo novamente?<BR>";
                p.close();
                if(jaBaixado){
                    throw new ConvenioBBException(msg, true);
                }                
            }

            PreparedStatement p = cn.prepareStatement("INSERT INTO TB_BAIXA_ARQUIVO VALUES(GETDATE(), ?)");
            p.setString(1, f.getName().trim().toUpperCase());
            p.executeUpdate();

            BufferedReader in = new BufferedReader(new FileReader(f));
            String s = null;
            result = new ResultadoBaixaArquivoConvenioBB();
            
            //verifica o header
            s = in.readLine();
            if(!s.startsWith("A")){
                throw new ConvenioBBException("Arquivo sem header, contate o Banco");
            }            
            
            CallableStatement cal = cn.prepareCall("{call SP_BAIXA_DCO_BANCO (?, ?, ?)}");
            
            /*
             * Os acumuladores abaixo sao usados porque se somar varias vezes um
             * float, a imprecisao do float se acumula a cada soma e no final, o 
             * resultado eh diferente do esperado.
             */
            result.valorAcatado = BigDecimal.ZERO;
            result.valorRejeitado = BigDecimal.ZERO;
            while((s = in.readLine()) != null){
                if(s.startsWith("F")){
                    result.totalRegistros++;
                    cal.setString(1, s);
                    cal.setString(2, f.getName());
                    cal.setDate(3, new java.sql.Date(dataVencimento.getTime()));
                    cal.execute();
                    ResultSet rs = cal.getResultSet();
                    if(rs.next()){
                        result.totalAcatado += rs.getInt("QT_SIM");
                        result.valorAcatado = result.valorAcatado.add(rs.getBigDecimal("VR_SIM"));
                        result.totalRejeitado += rs.getInt("QT_NAO");
                        result.valorRejeitado = result.valorRejeitado.add(rs.getBigDecimal("VR_NAO"));
                    }
                }
            }

            cn.commit();
            in.close();            
            f.delete();
            audit.registrarMudanca("{call SP_BAIXA_DCO_BANCO}");
        }catch (SQLException e) {
            log.severe(e.getMessage());
        }catch (IOException e) {
            log.severe(e.getMessage());            
        }finally{
            try {
                cn.close();
            } catch (SQLException e) {
                log.severe(e.getMessage());
            }
        }
        
        return result;
    }            

    public static void gerarMovDiaItau(
            Auditoria audit,
            Date dataReferencia,
            String arquivo){
        Connection cn = null;

        try {
            cn = Pool.getInstance().getConnection();

            CallableStatement cal = cn.prepareCall("{call SP_GERA_MOV_DIA_ITAU (?)}");            
            if(dataReferencia == null ){
                cal.setNull(1, java.sql.Types.DATE);
            }else{
                cal.setDate(1, new java.sql.Date(dataReferencia.getTime()));
            }
            

            cal.execute();
            cn.commit();
            audit.registrarMudanca("{call SP_GERA_MOV_DIA_ITAU}");            
            ResultSet rs = cal.getResultSet();
            FileWriter w = new FileWriter(arquivo);
            while(rs.next()){
                w.write(rs.getString("DE_LINHA"));
                w.write("\r\n");
            }
            w.close();

        }catch (SQLException e) {
            log.severe(e.getMessage());
        }catch (IOException e) {
            log.severe(e.getMessage());            
        }finally{
            try {
                cn.close();
            } catch (SQLException e) {
                log.severe(e.getMessage());
            }
        }
    }       
    
}
