package techsoft.operacoes;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;
import techsoft.db.Pool;
import techsoft.db.PoolFoto;

public class Visitante {
    private int id;
    private String nome;
    private String documento;
    private String placa;
    
    private static final Logger log = Logger.getLogger("techsoft.operacoes.Visitante");

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
    
    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getDocumento() {
        return documento;
    }

    public void setDocumento(String documento) {
        this.documento = documento;
    }

    public String getPlaca() {
        return placa;
    }

    public void setPlaca(String placa) {
        this.placa = placa;
    }

    public static Visitante getInstance(int id){
        Visitante v = null;
        Connection cn = null;
                
        try {
            cn = Pool.getInstance().getConnection();
            PreparedStatement p = cn.prepareStatement("SELECT * FROM TB_VISITANTE WHERE CD_VISITANTE = ?");
            p.setInt(1, id);
        
            ResultSet rs = p.executeQuery();
            if(rs.next()) {                
                v = new Visitante();
                v.setId(rs.getInt("CD_VISITANTE"));
                v.setNome(rs.getString("NO_VISITANTE"));
                v.setDocumento(rs.getString("NR_DOCUMENTO"));
                v.setPlaca(rs.getString("DE_PLACA"));
            }

        } catch (SQLException e) {
            log.severe(e.getMessage());
        }finally{
            try {
                cn.close();
            } catch (SQLException e) {
                log.severe(e.getMessage());
            }
        }

        return v;
    }    
    
    public static List<Visitante> consultar(String nome, String documento){
        ArrayList<Visitante> l = new ArrayList<Visitante>();
        String sql = "SELECT * FROM TB_VISITANTE WHERE 1=1 ";
        Connection cn = null;
        
        if(nome != null && !nome.trim().equals("")){
            sql += " AND NO_VISITANTE LIKE ? ";
        }

        if(documento != null && !documento.trim().equals("")){
            sql += " AND NR_DOCUMENTO LIKE ? ";
        }        
   
        
        try {
            cn = Pool.getInstance().getConnection();
            PreparedStatement p = cn.prepareStatement(sql);
            
            int i = 1;
            if(nome != null && !nome.trim().equals("")){
                p.setString(i++, nome + "%");
            }

            if(documento != null && !documento.trim().equals("")){
                p.setString(i++, documento + "%");
            }
        
            ResultSet rs = p.executeQuery();
            while (rs.next()) {                
                Visitante v = new Visitante();
                v.setId(rs.getInt("CD_VISITANTE"));
                v.setNome(rs.getString("NO_VISITANTE"));
                v.setDocumento(rs.getString("NR_DOCUMENTO"));
                v.setPlaca(rs.getString("DE_PLACA"));
                
                l.add(v);
            }

        } catch (SQLException e) {
            log.severe(e.getMessage());
        }finally{
            try {
                cn.close();
            } catch (SQLException e) {
                log.severe(e.getMessage());
            }
        }

        return l;
    }    
    
    public void atualizarFoto(File f){

        Connection cn = null;
        String sql =  "SELECT * FROM TB_FOTO_VISITANTE WHERE CD_VISITANTE = ? ";
        BufferedInputStream bin = null;
        
        try {
            cn = PoolFoto.getInstance().getConnection();
            PreparedStatement p = cn.prepareStatement(sql, ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
            p.setInt(1, id);
            ResultSet rs = p.executeQuery();
            
            try{
                bin = new BufferedInputStream(new FileInputStream(f));
            }catch(FileNotFoundException e){
                log.severe(e.getMessage());
            }
                            
            if(rs.next()){
                rs.updateBinaryStream("FOTO_VISITANTE", bin);
                rs.updateRow();
            }else{
                rs.moveToInsertRow();
                rs.updateInt("CD_VISITANTE", id);
                rs.updateBinaryStream("FOTO_VISITANTE", bin);
                rs.insertRow();
            }

            cn.commit();
        } catch (SQLException e) {
            log.severe(e.getMessage());
        }finally{
            try {
                cn.close();
            } catch (SQLException e) {
                log.severe(e.getMessage());
            }
            try {
                bin.close();
            } catch (IOException e) {
                log.severe(e.getMessage());
            }
        }

    }        
}
