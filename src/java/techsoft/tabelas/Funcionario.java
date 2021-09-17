package techsoft.tabelas;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Logger;
import techsoft.db.DBUtil;
import techsoft.db.Pool;
import techsoft.db.PoolFoto;
import techsoft.db.SQLType;
import techsoft.seguranca.Auditoria;
import techsoft.seguranca.ParametroAuditoria;

public class Funcionario {
    
    private int id;
    private String primeiroNome;
    private int matricula;
    /*
     * esse atributo e simplesmente um artificio usado para validar alteracao
     * de matricula no metodo atualizar, no banco de dados nao fica armazenado
     * a matricula anterior.
     */
    private int matriculaAnterior;
    private String login;
    private String sangue;
    private String nome;
    private String endereco;
    private String telefone1;
    private String telefone2;
    private String CPF;
    private int idCargo;
    private int idSetor;
    private String tipo = "F";
    private String estacionamento = "I";
    private float valorMaximoConsumo;
    private FuncionarioHorario[] horarios = new FuncionarioHorario[7];
    
    private static final Logger log = Logger.getLogger("techsoft.tabelas.Funcionario");
    
    private static final DBUtil.ObjectFactory<Funcionario> builder = new DBUtil.ObjectFactory<Funcionario>() {
        @Override
        public Funcionario buildFrom(ResultSet rs) throws SQLException {
            Funcionario f = new Funcionario();
            f.id = rs.getInt("CD_FUNCIONARIO");
            f.primeiroNome = rs.getString("NO_ABREVIADO");
            f.matricula = rs.getInt("NUM_MATRIC_FUNCIONARIO");
            f.matriculaAnterior = f.matricula;
            f.login = rs.getString("USER_ACESSO_SISTEMA");
            f.sangue = rs.getString("TP_SANGUE");
            f.nome = rs.getString("NOME_FUNCIONARIO");
            f.endereco = rs.getString("ENDERECO_FUNCIONARIO");
            f.telefone1 = rs.getString("TEL1_FUNCIONARIO");
            f.telefone2 = rs.getString("TEL2_FUNCIONARIO");
            f.idCargo = rs.getInt("CD_CARGO");
            f.idSetor = rs.getInt("CD_SETOR");
            f.tipo = rs.getString("TP_FUNCIONARIO");
            f.estacionamento = rs.getString("CD_ESTACIONAMENTO");
            f.CPF = rs.getString("CPF_FUNCIONARIO");
            f.valorMaximoConsumo = rs.getFloat("VR_MAXIMO_CONSUMO");
            if(rs.getInt("TRAB_SEGUNDA") > 0){
                f.horarios[0] = new FuncionarioHorario(rs.getString("HH_ENTRA_SEGUNDA"), rs.getString("HH_SAI_SEGUNDA"));
            }
            if(rs.getInt("TRAB_TERCA") > 0){
                f.horarios[1] = new FuncionarioHorario(rs.getString("HH_ENTRA_TERCA"), rs.getString("HH_SAI_TERCA"));
            }
            if(rs.getInt("TRAB_QUARTA") > 0){
                f.horarios[2] = new FuncionarioHorario(rs.getString("HH_ENTRA_QUARTA"), rs.getString("HH_SAI_QUARTA"));
            }
            if(rs.getInt("TRAB_QUINTA") > 0){
                f.horarios[3] = new FuncionarioHorario(rs.getString("HH_ENTRA_QUINTA"), rs.getString("HH_SAI_QUINTA"));
            }
            if(rs.getInt("TRAB_SEXTA") > 0){
                f.horarios[4] = new FuncionarioHorario(rs.getString("HH_ENTRA_SEXTA"), rs.getString("HH_SAI_SEXTA"));
            }
            if(rs.getInt("TRAB_SABADO") > 0){
                f.horarios[5] = new FuncionarioHorario(rs.getString("HH_ENTRA_SABADO"), rs.getString("HH_SAI_SABADO"));
            }
            if(rs.getInt("TRAB_DOMINGO") > 0){
                f.horarios[6] = new FuncionarioHorario(rs.getString("HH_ENTRA_DOMINGO"), rs.getString("HH_SAI_DOMINGO"));
            }            
            return f;
        }
    };
    
    public int getId() {
        return id;
    }

    public String getPrimeiroNome() {
        return primeiroNome;
    }

    public void setPrimeiroNome(String primeiroNome) {
        this.primeiroNome = primeiroNome;
    }

    public int getMatricula() {
        return matricula;
    }

    public void setMatricula(int matricula) {
        this.matricula = matricula;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getSangue() {
        return sangue;
    }

    public void setSangue(String sangue) {
        this.sangue = sangue;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getEndereco() {
        return endereco;
    }

    public void setEndereco(String endereco) {
        this.endereco = endereco;
    }

    public String getTelefone1() {
        return telefone1;
    }

    public void setTelefone1(String telefone1) {
        this.telefone1 = telefone1;
    }

    public String getTelefone2() {
        return telefone2;
    }

    public void setTelefone2(String telefone2) {
        this.telefone2 = telefone2;
    }

    public int getIdCargo() {
        return idCargo;
    }

    public void setIdCargo(int idCargo) {
        this.idCargo = idCargo;
    }

    public int getIdSetor() {
        return idSetor;
    }

    public void setIdSetor(int idSetor) {
        this.idSetor = idSetor;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public String getEstacionamento() {
        return estacionamento;
    }

    public void setEstacionamento(String estacionamento) {
        this.estacionamento = estacionamento;
    }

    public float getValorMaximoConsumo() {
        return valorMaximoConsumo;
    }

    public void setValorMaximoConsumo(float valorMaximoConsumo) {
        this.valorMaximoConsumo = valorMaximoConsumo;
    }

    public FuncionarioHorario[] getHorarios() {
        return horarios;
    }
    
    public String getCPF() {
        return CPF;
    }

    public void setCPF(String CPF) {
        this.CPF = CPF;
    }
    
    public static Funcionario getInstance(int id){
        return DBUtil.queryObject(Funcionario.builder, "SELECT * FROM TB_FUNCIONARIO WHERE CD_FUNCIONARIO = ?", SQLType.INTEGER(id));
    }
    
    private boolean matriculaCadastrada(){
        Connection cn = null;
        boolean b = false;
        
        try {
            cn = Pool.getInstance().getConnection();
            PreparedStatement p = cn.prepareStatement("SELECT 1 FROM TB_FUNCIONARIO WHERE NUM_MATRIC_FUNCIONARIO = ?");
            p.setInt(1, matricula);
            ResultSet rs = p.executeQuery();
            if(rs.next()){
                b = true;
            }
        } catch (SQLException ex) {
            log.severe(ex.getMessage());
        }finally{
            try {
                cn.close();
            } catch (SQLException ex) {
                log.severe(ex.getMessage());
            }
        }

        return b;
    }
    
    public void inserir(Auditoria audit)throws InserirException{
        String sql = "INSERT INTO TB_FUNCIONARIO (CD_SETOR, CD_CARGO, "
                + "NO_ABREVIADO, NOME_FUNCIONARIO, ENDERECO_FUNCIONARIO, "
                + "TEL1_FUNCIONARIO, TEL2_FUNCIONARIO, NUM_MATRIC_FUNCIONARIO, "
                + "TP_FUNCIONARIO, TRAB_SEGUNDA, HH_ENTRA_SEGUNDA, "
                + "HH_SAI_SEGUNDA, TRAB_TERCA, HH_ENTRA_TERCA, HH_SAI_TERCA, "
                + "TRAB_QUARTA, HH_ENTRA_QUARTA, HH_SAI_QUARTA, TRAB_QUINTA, "
                + "HH_ENTRA_QUINTA, HH_SAI_QUINTA, TRAB_SEXTA, HH_ENTRA_SEXTA, "
                + "HH_SAI_SEXTA, TRAB_SABADO, HH_ENTRA_SABADO, HH_SAI_SABADO, "
                + "TRAB_DOMINGO, HH_ENTRA_DOMINGO, HH_SAI_DOMINGO, "
                + "CD_ESTACIONAMENTO, USER_ACESSO_SISTEMA, TP_SANGUE, "
                + "VR_MAXIMO_CONSUMO, CPF_FUNCIONARIO) VALUES (?, ?, ?, ?, "
                + "?, ?, ?, ?, ?, ?, ?, ?, ?, ?,"
                + "?, ?, ?, ?, ?, ?, ?, ?, ?, ?,"
                + "?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        Connection cn = null;
        
        if(matriculaCadastrada()){
            throw new InserirException("Já existe outro Funcionário cadastrado com esta Matrícula.");
        }
        
        try {
            cn = Pool.getInstance().getConnection();
            PreparedStatement p = cn.prepareStatement(sql);
            ParametroAuditoria par = new ParametroAuditoria();
            p.setInt(1, par.getSetParametro(idSetor));
            p.setInt(2, par.getSetParametro(idCargo));
            p.setString(3, par.getSetParametro(primeiroNome));
            p.setString(4, par.getSetParametro(nome));
            p.setString(5, par.getSetParametro(endereco));
            p.setString(6, par.getSetParametro(telefone1));
            p.setString(7, par.getSetParametro(telefone2));
            p.setInt(8, par.getSetParametro(matricula));
            p.setString(9, par.getSetParametro(tipo));
            
            int i = 10;
            for(int k = 0; k < 7; k++){
                if(horarios[k] != null){
                    p.setInt(i++, par.getSetParametro(1));
                    p.setString(i++, par.getSetParametro(horarios[k].getEntrada().replaceAll(":","")));
                    p.setString(i++, par.getSetParametro(horarios[k].getSaida().replaceAll(":","")));
                }else{
                    p.setInt(i++, par.getSetParametro(0));
                    p.setNull(i++, java.sql.Types.VARCHAR);
                    par.getSetNull();
                    p.setNull(i++, java.sql.Types.VARCHAR);
                    par.getSetNull();
                }
            }

            p.setString(31, par.getSetParametro(estacionamento));
            p.setString(32, par.getSetParametro(login));
            p.setString(33, par.getSetParametro(sangue));
            p.setFloat(34, par.getSetParametro(valorMaximoConsumo));
            p.setString(35, par.getSetParametro(CPF.replace(".", "").replace("-", "")));
            
            p.executeUpdate();
            cn.commit();
            audit.registrarMudanca(sql, par.getParametroFinal());
        } catch (SQLException ex) {
            log.severe(ex.getMessage());
        }finally{
            try {
                cn.close();
            } catch (SQLException ex) {
                log.severe(ex.getMessage());
            }
        }
    }
    
    public void alterar(Auditoria audit)throws AlterarException{
        String sql = " UPDATE TB_FUNCIONARIO SET CD_SETOR = ?, CD_CARGO = ?, "
           + "NO_ABREVIADO = ?, NOME_FUNCIONARIO = ?, ENDERECO_FUNCIONARIO = ?, "
           + "TEL1_FUNCIONARIO = ?, TEL2_FUNCIONARIO = ?, "
           + "NUM_MATRIC_FUNCIONARIO = ?, TP_FUNCIONARIO = ?, "
           + "TRAB_SEGUNDA = ?, HH_ENTRA_SEGUNDA = ?, HH_SAI_SEGUNDA = ?, "
           + "TRAB_TERCA = ?, HH_ENTRA_TERCA = ?, HH_SAI_TERCA = ?, "
           + "TRAB_QUARTA = ?, HH_ENTRA_QUARTA = ?, HH_SAI_QUARTA = ?, "
           + "TRAB_QUINTA = ?, HH_ENTRA_QUINTA = ?, HH_SAI_QUINTA = ?, "
           + "TRAB_SEXTA = ?, HH_ENTRA_SEXTA = ?, HH_SAI_SEXTA = ?, "
           + "TRAB_SABADO = ?, HH_ENTRA_SABADO = ?, HH_SAI_SABADO = ?, "
           + "TRAB_DOMINGO = ?, HH_ENTRA_DOMINGO = ?, HH_SAI_DOMINGO = ?, "
           + "CD_ESTACIONAMENTO = ?, USER_ACESSO_SISTEMA  = ?, "
           + "TP_SANGUE = ?, VR_MAXIMO_CONSUMO = ?, CPF_FUNCIONARIO = ? WHERE CD_FUNCIONARIO = ?";

        Connection cn = null;
        
        if((matricula != matriculaAnterior) && matriculaCadastrada()){
            throw new AlterarException("Já existe outro Funcionário cadastrado com esta Matrícula.");
        }
        
        try {
            cn = Pool.getInstance().getConnection();
            PreparedStatement p = cn.prepareStatement(sql);
            ParametroAuditoria par = new ParametroAuditoria();
            p.setInt(1, par.getSetParametro(idSetor));
            p.setInt(2, par.getSetParametro(idCargo));
            p.setString(3, par.getSetParametro(primeiroNome));
            p.setString(4, par.getSetParametro(nome));
            p.setString(5, par.getSetParametro(endereco));
            p.setString(6, par.getSetParametro(telefone1));
            p.setString(7, par.getSetParametro(telefone2));
            p.setInt(8, par.getSetParametro(matricula));
            p.setString(9, par.getSetParametro(tipo));
            
            int i = 10;
            String entrada = "";
            String saida = "";
            for(int k = 0; k < 7; k++){
                if(horarios[k] != null){
                    p.setInt(i++, par.getSetParametro(1));
                    entrada = horarios[k].getEntrada().replaceAll(":","");
                    saida = horarios[k].getSaida().replaceAll(":","");
                    p.setString(i++, par.getSetParametro(entrada));
                    p.setString(i++, par.getSetParametro(saida));
                }else{
                    p.setInt(i++, par.getSetParametro(0));
                    p.setNull(i++, java.sql.Types.VARCHAR);
                    par.getSetNull();
                    p.setNull(i++, java.sql.Types.VARCHAR);
                    par.getSetNull();
                }
            }

            p.setString(31, par.getSetParametro(estacionamento));
            p.setString(32, par.getSetParametro(login));
            p.setString(33, par.getSetParametro(sangue));
            p.setFloat(34, par.getSetParametro(valorMaximoConsumo));
            p.setString(35, par.getSetParametro(CPF.replace(".", "").replace("-", "")));
            p.setInt(36, par.getSetParametro(id));
            
            p.executeUpdate();
            cn.commit();
            audit.registrarMudanca(sql, par.getParametroFinal());
        } catch (SQLException ex) {
            log.severe(ex.getMessage());
        }finally{
            try {
                cn.close();
            } catch (SQLException ex) {
                log.severe(ex.getMessage());
            }
        }
    }
    
    public void excluir(Auditoria audit){
        Connection cn = null;
                
        try {
            cn = Pool.getInstance().getConnection();
            String sql = "DELETE FROM TB_FUNCIONARIO WHERE CD_FUNCIONARIO = ?";
            PreparedStatement p = cn.prepareStatement(sql);
            p.setInt(1, id);
            
            p.executeUpdate();
            cn.commit();
            audit.registrarMudanca(sql, String.valueOf(id));
        } catch (SQLException ex) {
            log.severe(ex.getMessage());
        }finally{
            try {
                cn.close();
            } catch (SQLException ex) {
                log.severe(ex.getMessage());
            }
        }
    }
    
    public void alterarSenha(Auditoria audit, String novaSenha){
        Connection cn = null;
                
        try {
            cn = Pool.getInstance().getConnection();
            String sql = "UPDATE TB_FUNCIONARIO SET SENHA_FUNCIONARIO = ? WHERE CD_FUNCIONARIO = ?";
            PreparedStatement p = cn.prepareStatement(sql);
            if ("".equals(novaSenha)){
                p.setNull(1, java.sql.Types.VARCHAR);
            }else{
                p.setString(1, novaSenha);
            }
            
            p.setInt(2, id);
            
            p.executeUpdate();
            cn.commit();
            audit.registrarMudanca(sql, String.valueOf(id));
        } catch (SQLException ex) {
            log.severe(ex.getMessage());
        }finally{
            try {
                cn.close();
            } catch (SQLException ex) {
                log.severe(ex.getMessage());
            }
        }
    }
    
    public void atualizarFoto(File f){

        Connection cn = null;
        BufferedInputStream bin = null;
        
        try {
            cn = PoolFoto.getInstance().getConnection();
            PreparedStatement p = cn.prepareStatement(
                "SELECT * FROM TB_foto_Funcionario WHERE CD_FUNCIONARIO = ?",
                ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
            p.setInt(1, id);
            ResultSet rs = p.executeQuery();
            
            try{
                bin = new BufferedInputStream(new FileInputStream(f));
            }catch(FileNotFoundException e){
                log.severe(e.getMessage());
            }
                            
            if(rs.next()){
                
                rs.updateBinaryStream("FOTO_FUNCIONARIO", bin);
                rs.updateRow();
            }else{
                rs.moveToInsertRow();
                rs.updateInt("CD_FUNCIONARIO", id);
                rs.updateBinaryStream("FOTO_FUNCIONARIO", bin);
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

    public void excluirFoto(){

        Connection cn = null;
        String sql =  "DELETE FROM TB_foto_Funcionario WHERE CD_FUNCIONARIO = ?";
        try {
            cn = PoolFoto.getInstance().getConnection();
            PreparedStatement p = cn.prepareStatement(sql);
            p.setInt(1, id);
            p.executeUpdate();

            cn.commit();
        } catch (SQLException e) {
            log.severe(e.getMessage());
        }finally{
            try {
                cn.close();
            } catch (SQLException e) {
                log.severe(e.getMessage());
            }
        }

    }

    public void alterarNumeroCracha(Auditoria audit, int novoNumero)throws AlterarException{
        Connection cn = null;
                
        try {
            cn = Pool.getInstance().getConnection();
            String sql = "select 1 from tb_funcionario where nr_cracha = ?";
            PreparedStatement p = cn.prepareStatement(sql);
            p.setInt(1, novoNumero);
            ResultSet rs = p.executeQuery();
            if(rs.next()){
                throw new AlterarException("Já existe outro Funcionário cadastrado com este Número.");
            }
            
            sql = "update tb_funcionario set nr_cracha = ? WHERE CD_FUNCIONARIO = ?";
            p = cn.prepareStatement(sql);
            p.setInt(1, novoNumero);
            p.setInt(2, id);
            
            p.executeUpdate();
            cn.commit();
            audit.registrarMudanca(sql, String.valueOf(novoNumero), String.valueOf(id));
        } catch (SQLException ex) {
            log.severe(ex.getMessage());
        }finally{
            try {
                cn.close();
            } catch (SQLException ex) {
                log.severe(ex.getMessage());
            }
        }
    }
}
