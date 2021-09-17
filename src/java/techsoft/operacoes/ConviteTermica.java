package techsoft.operacoes;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Logger;
import techsoft.carteirinha.JobImpressoraTermica;
import techsoft.db.Pool;

public class ConviteTermica {
    private static final Logger log = Logger.getLogger("techsoft.operacoes.ConviteTermica");
    private int nrConvite;
    private String iateHost;
    private int iatePort;
    private String iateApp;
    
    public ConviteTermica(int nrConvite, String iateHost, int iatePort, String iateApp){
        this.nrConvite = nrConvite;
        this.iateHost = iateHost;
        this.iatePort = iatePort;
        this.iateApp = iateApp;
        
    }
    
    public void emitir(String addr)throws IOException{
                    /*
      For X = 0 To frm1079CabConviteTermica.Controls.Count - 1
         If frm1079CabConviteTermica.Controls(X).Name = ArqCab Then
            SavePicture frm1079CabConviteTermica.Controls(X), App.Path & "\pct_conv.bmp"
            Ret = ImprimeBmpEspecial(App.Path & "\pct_conv.bmp", 50, 50, 0)
         End If
      Next
             */
        Connection cn = Pool.getInstance().getConnection();
        JobImpressoraTermica job = new JobImpressoraTermica();        
        
        try {
            String sql = "SELECT * FROM TB_CONVITE WHERE CD_STATUS_CONVITE = 'NU' "
                + " AND NR_CONVITE = ?";
            PreparedStatement p = cn.prepareStatement(sql);
            p.setInt(1, nrConvite);
            ResultSet rs = p.executeQuery();
            rs.next();    
            
            String s = rs.getString("CD_CATEGORIA_CONVITE");
            job.categoriaConvite = s;
            if(s.equals("EC")){
                job.descricaoTitularResponsavel = "Convênio:";
            }else if(s.equals("ED") || s.equals("VD")){
                job.descricaoTitularResponsavel = "Diretor:";
            }else if(s.equals("EV") || s.equals("VV")){
                job.descricaoTitularResponsavel = "Vice-Diretor:";
            }else if(s.equals("EO") || s.equals("VO")){
                job.descricaoTitularResponsavel = "Vice-Comodoro:";
            }else if(s.equals("EA") || s.equals("VA")){
                job.descricaoTitularResponsavel = "Acessor-Comodoro:";
            }else if(s.equals("EN") || s.equals("VN")){
                job.descricaoTitularResponsavel = "Conselheiro:";
            }else if(s.equals("EP") || s.equals("VP")){
                job.descricaoTitularResponsavel = "Presidente do Conselho:";
            }else if(s.equals("CO") || s.equals("VC")){
                job.descricaoTitularResponsavel = "Comodoro";
                job.nomeComodoro = rs.getString("NOME_SACADOR");
            }else{
                job.descricaoTitularResponsavel = "Sócio:";
            }

            if(s.equals("CH")){
                job.descricaoTituloValidade = "Val.p/ o Dia: ";
            }else{
                job.descricaoTituloValidade = "Dt.Lim.Util.: ";
            }
            
            job.nomeSacador = rs.getString("NOME_SACADOR");
            job.numeroConvite = String.format("%1$09d", rs.getInt("NR_CONVITE"));
            job.nomeConvidado = rs.getString("NOME_CONVIDADO");
            if (rs.getObject("DT_NASC_CONVIDADO") == null){
                job.nascimentoConvidado = "";
            }else{
                java.util.Date d = rs.getDate("DT_NASC_CONVIDADO");
                try{
                    job.nascimentoConvidado = String.format("%1$Td/%1$Tm/%1$TY", d);
                }catch(Exception e){
                    job.nascimentoConvidado = "";
                }
            }
            s = rs.getString("CPF_CONVIDADO");
            if ("".equals(s)){
                job.cpfConvidado = "              ";
            }else{
                try{
                    job.cpfConvidado = s.substring(0, 3) + "." + s.substring(3, 6) + "." + s.substring(6, 9) + "-" + s.substring(9, 11);
                }catch(Exception e){
                    job.cpfConvidado = "              ";
                }
            }
            job.dataMaximaUtilizacao = String.format("%1$Td/%1$Tm/%1$TY", rs.getDate("DT_MAX_UTILIZACAO"));
            job.dataHoje = String.format("%1$Td/%1$Tm/%1$TY", new java.util.Date());
            job.estacionamentoInterno = (rs.getInt("ESTACIONAMENTO_INTERNO") > 0);
            job.numeroChurrasqueira = rs.getString("NR_CHURRASQUEIRA");
            
            s = job.categoriaConvite;
            if(s.equals("EC") || s.equals("ED") || s.equals("EV") || s.equals("EO") || s.equals("EA") || s.equals("EN") || s.equals("EP")){
                sql = "select nome_pessoa from tb_pessoa where cd_cargo_especial = (select cd_cargo_comodoro from tb_parametro_sistema) and cd_cargo_ativo = 'S'";
                rs.close();
                rs = cn.createStatement().executeQuery(sql);
                rs.next();

                job.nomeComodoro = rs.getString("NOME_PESSOA");
                rs.close();
            }
            
            job.iateHost = iateHost;
            job.iatePort = iatePort;
            job.iateApp = iateApp;
            
            if (rs.getObject("HH_ENTRADA") != null){
                s = rs.getString("HH_ENTRADA");
                job.horarioEntradaChurrasqueira = s.substring(0, 2) + ":" + s.substring(2, 4);
            }else{
                job.horarioEntradaChurrasqueira = "";
            }
            
            if (rs.getObject("HH_SAIDA") != null){
                s = rs.getString("HH_SAIDA");
                job.horarioSaidaChurrasqueira = s.substring(0, 2) + ":" + s.substring(2, 4);
            }else{
                job.horarioSaidaChurrasqueira = "";
            }
            
        } catch (Exception e) {
            log.severe(e.getMessage());
            throw new IOException(e);
        }finally{
            try {
                cn.close();
            } catch (SQLException e) {
                log.severe(e.getMessage());
            }
        }        

        log.fine("Conectando com " + addr);
        Socket sock = new Socket(addr, 49001);
        ObjectOutputStream out = new ObjectOutputStream(sock.getOutputStream());
        out.writeObject(job);
        out.close();
    }

    
}
