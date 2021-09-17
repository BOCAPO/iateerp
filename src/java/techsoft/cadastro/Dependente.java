
package techsoft.cadastro;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;
import techsoft.db.Pool;
import techsoft.seguranca.Auditoria;
import techsoft.seguranca.ParametroAuditoria;

public class Dependente {

    private static final Logger log = Logger.getLogger("techsoft.cadastro.Dependente");

    private int matricula;
    private int seqDependente;
    private int idCategoria;
    
    //dados gerais
    private String nome;
    private int idTipoDependente;
    private String casoEspecial;
    private Date dataCasoEspecial;
    private boolean masculino = true;
    private Date dataNascimento;

    //permissoes
    private Date dataValidadeRetiradaMaterial;
    //a explicacao do porque usar um Map esta no metodo carregarMateriais
    private Map<Integer, String> materiais = new HashMap<Integer, String>();
    private Date dataValidadeRetiradaConvite;
    private Date dataValidadeReservaChurrasqueira;

    //dados complementares
    private String telefoneCelular;
    private String telefoneResidencial;
    private String telefoneComercial;
    private String fax;
    private String email;
    private int idCargoEspecial;
    private boolean cargoAtivo;
    private boolean empTodosMat;
    private boolean consumoTodasTx;

    //Usada para bloquear a utilizacao de taxa
    private Map<Integer, String> taxasIndividuais = new HashMap<Integer, String>();
    
    public boolean isCargoAtivo() {
        return cargoAtivo;
    }

    public void setCargoAtivo(boolean cargoAtivo) {
        this.cargoAtivo = cargoAtivo;
    }

    public String getCasoEspecial() {
        return casoEspecial;
    }

    public void setCasoEspecial(String casoEspecial) {
        this.casoEspecial = casoEspecial;
    }

    public Date getDataCasoEspecial() {
        return dataCasoEspecial;
    }

    public void setDataCasoEspecial(Date dataCasoEspecial) {
        this.dataCasoEspecial = dataCasoEspecial;
    }

    public Date getDataNascimento() {
        return dataNascimento;
    }

    public void setDataNascimento(Date dataNascimento) {
        this.dataNascimento = dataNascimento;
    }

    public Date getDataValidadeReservaChurrasqueira() {
        return dataValidadeReservaChurrasqueira;
    }

    public void setDataValidadeReservaChurrasqueira(Date dataValidadeReservaChurrasqueira) {
        this.dataValidadeReservaChurrasqueira = dataValidadeReservaChurrasqueira;
    }

    public Date getDataValidadeRetiradaConvite() {
        return dataValidadeRetiradaConvite;
    }

    public void setDataValidadeRetiradaConvite(Date dataValidadeRetiradaConvite) {
        this.dataValidadeRetiradaConvite = dataValidadeRetiradaConvite;
    }

    public Date getDataValidadeRetiradaMaterial() {
        return dataValidadeRetiradaMaterial;
    }

    public void setDataValidadeRetiradaMaterial(Date dataValidadeRetiradaMaterial) {
        this.dataValidadeRetiradaMaterial = dataValidadeRetiradaMaterial;
    }

    public Map<Integer, String> getMateriais() {
        return materiais;
    }

    public void setMateriais(Map<Integer, String> materiais) {
        this.materiais = materiais;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public int getIdCargoEspecial() {
        return idCargoEspecial;
    }

    public void setIdCargoEspecial(int idCargoEspecial) {
        this.idCargoEspecial = idCargoEspecial;
    }

    public int getIdCategoria() {
        return idCategoria;
    }

    public void setIdCategoria(int idCategoria) {
        this.idCategoria = idCategoria;
    }

    public int getIdTipoDependente() {
        return idTipoDependente;
    }

    public void setIdTipoDependente(int idTipoDependente) {
        this.idTipoDependente = idTipoDependente;
    }

    public boolean isMasculino() {
        return masculino;
    }

    public void setMasculino(boolean masculino) {
        this.masculino = masculino;
    }

    public int getMatricula() {
        return matricula;
    }

    public void setMatricula(int matricula) {
        this.matricula = matricula;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public int getSeqDependente() {
        return seqDependente;
    }

    public void setSeqDependente(int seqDependente) {
        this.seqDependente = seqDependente;
    }

    public String getTelefoneCelular() {
        return telefoneCelular;
    }

    public void setTelefoneCelular(String telefoneCelular) {
        this.telefoneCelular = telefoneCelular;
    }

    public String getTelefoneComercial() {
        return telefoneComercial;
    }

    public void setTelefoneComercial(String telefoneComercial) {
        this.telefoneComercial = telefoneComercial;
    }

    public String getTelefoneResidencial() {
        return telefoneResidencial;
    }

    public void setTelefoneResidencial(String telefoneResidencial) {
        this.telefoneResidencial = telefoneResidencial;
    }
    public String getFax() {
        return fax;
    }

    public void setFax(String fax) {
        this.fax = fax;
    }
    
    public Map<Integer, String> getTaxasIndividuais() {
        return taxasIndividuais;
    }

    public void setTaxasIndividuais(Map<Integer, String> taxasAdministrativas) {
        this.taxasIndividuais = taxasIndividuais;
    }
    public boolean isEmpTodosMat() {
        return empTodosMat;
    }
    public void setEmpTodosMat(boolean empTodosMat) {
        this.empTodosMat = empTodosMat;
    }
    public boolean isConsumoTodasTx() {
        return consumoTodasTx;
    }
    public void setConsumoTodasTx(boolean consumoTodasTx) {
        this.consumoTodasTx = consumoTodasTx;
    }
    
    
    public static Dependente getInstance(int matricula, int seqDependente, int idCategoria){

        String sql = "SELECT NOME_PESSOA, CD_MATRICULA, SEQ_DEPENDENTE, "
            + "CD_CATEGORIA, CD_TP_DEPENDENTE, DT_NASCIMENTO, CD_SEXO, "
            + "CD_CASO_ESPECIAL, DT_CASO_ESPECIAL, DT_VALID_RET_CONVITE, "
            + "DT_VALID_RESERVA, DT_VALID_MATERIAL, CD_CARGO_ESPECIAL, "
            + "CD_CARGO_ATIVO, DE_EMAIL, IC_EMP_TODOS_MAT, IC_TX_IND_TODAS_TX FROM TB_PESSOA WHERE CD_MATRICULA = ? "
            + "AND SEQ_DEPENDENTE = ? AND CD_CATEGORIA = ?";
        Dependente d = null;
        Connection cn = null;

        try {
            cn = Pool.getInstance().getConnection();
            PreparedStatement ps = cn.prepareStatement(sql);
            ps.setInt(1, matricula);
            ps.setInt(2, seqDependente);
            ps.setInt(3, idCategoria);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                d = new Dependente();
                d.setMatricula(rs.getInt("CD_MATRICULA"));
                d.setSeqDependente(rs.getInt("SEQ_DEPENDENTE"));
                d.setIdCategoria(rs.getInt("CD_CATEGORIA"));
                d.setNome(rs.getString("NOME_PESSOA"));
                d.setIdTipoDependente(rs.getInt("CD_TP_DEPENDENTE"));
                d.setCasoEspecial(rs.getString("CD_CASO_ESPECIAL"));
                d.setDataCasoEspecial(rs.getDate("DT_CASO_ESPECIAL"));
                if(rs.getString("CD_SEXO").equalsIgnoreCase("M")){
                    d.setMasculino(true);
                }else{
                    d.setMasculino(false);
                }
                d.setDataNascimento(rs.getDate("DT_NASCIMENTO"));
                d.setDataValidadeRetiradaMaterial(rs.getDate("DT_VALID_MATERIAL"));
                d.setDataValidadeRetiradaConvite(rs.getDate("DT_VALID_RET_CONVITE"));
                d.setDataValidadeReservaChurrasqueira(rs.getDate("DT_VALID_RESERVA"));

                String sqlFone = "SELECT * FROM TB_TELEFONE_PESSOA WHERE CD_MATRICULA = ? "
                    + "AND SEQ_DEPENDENTE = ? AND CD_CATEGORIA = ?";
                PreparedStatement pFone = cn.prepareStatement(sqlFone);
                pFone.setInt(1, d.getMatricula());
                pFone.setInt(2, d.getSeqDependente());
                pFone.setInt(3, d.getIdCategoria());

                ResultSet rsFone = pFone.executeQuery();
                while(rsFone.next()){
                    char c = rsFone.getString("IC_TIPO").charAt(0);
                    String fone = rsFone.getString("NR_TELEFONE");
                    switch(c){
                        case 'L':
                            d.setTelefoneCelular(fone);
                        break;
                        case 'C':
                            d.setTelefoneComercial(fone);
                        break;
                        case 'R':
                            d.setTelefoneResidencial(fone);
                        break;
                        case 'F':
                            d.setFax(fone);
                        break;
                    }
                }
                rsFone.close();

                d.setEmail(rs.getString("DE_EMAIL"));
                d.setIdCargoEspecial(rs.getInt("CD_CARGO_ESPECIAL"));
                if(rs.getString("CD_CARGO_ATIVO") == null || rs.getString("CD_CARGO_ATIVO").charAt(0) == 'N'){
                    d.setCargoAtivo(false);
                }else{
                    d.setCargoAtivo(true);
                }
                if(rs.getString("IC_EMP_TODOS_MAT") == null || rs.getString("IC_EMP_TODOS_MAT").charAt(0) == 'N'){
                    d.setEmpTodosMat(false);
                }else{
                    d.setEmpTodosMat(true);
                }
                if(rs.getString("IC_TX_IND_TODAS_TX") == null || rs.getString("IC_TX_IND_TODAS_TX").charAt(0) == 'N'){
                    d.setConsumoTodasTx(false);
                }else{
                    d.setConsumoTodasTx(true);
                }

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

        return d;
    }

    public void carregarMateriais(){

        /*
         * To usando um map ao inves de um List de objetos materiais
         * porque a 3116-material-dependente.jsp tem um loop que marca
         * os materiais selecionados e a forma mais facil de fazer isso
         * via taglibs e expression language que eu encontrei foi colocando
         * um map. Tem a opcao de fazer uma taglib function mas ...
         */

        String sql = "SELECT CD_MATERIAL FROM TB_MATERIAL_AUTORIZADO_DEPENDENTE "
             + "WHERE CD_MATRICULA = ? AND CD_CATEGORIA = ? AND SEQ_DEPENDENTE = ?";

        materiais.clear();
        Connection cn = null;

        try {
            cn = Pool.getInstance().getConnection();
            PreparedStatement p = cn.prepareStatement(sql);
            p.setInt(1, matricula);
            p.setInt(2, idCategoria);
            p.setInt(3, seqDependente);
            
            ResultSet rs = p.executeQuery();
            while(rs.next()){
                //Integer i = new Integer(rs.getInt("CD_MATERIAL"));
                //materiais.put(i, i.toString());
                materiais.put(rs.getInt("CD_MATERIAL"), rs.getString("CD_MATERIAL"));
            }
        } catch (Exception e) {
            log.severe(e.getMessage());
        }finally{
            try {
                cn.close();
            } catch (SQLException e) {
                log.severe(e.getMessage());
            }
        }

    }

    public void inserir(Auditoria audit) throws InserirDependenteException{
        Connection cn = null;

        try {
            String carteirinha = null;//recebe o numero de carteirinha gerado nessa rotina
            int seq = 0;//seq dependente

            // Verifica se ja este dependente ja foi cadastrado
            cn = Pool.getInstance().getConnection();
            String sql = "SELECT * FROM TB_PESSOA "
                    + "WHERE CD_MATRICULA = ? AND CD_CATEGORIA = ? ORDER BY SEQ_DEPENDENTE";
            PreparedStatement p = cn.prepareStatement(sql);
            p.setInt(1, matricula);
            p.setInt(2, idCategoria);
            ResultSet rs = p.executeQuery();

            while(rs.next()){
                seq = rs.getInt("SEQ_DEPENDENTE") + 1;
                if(rs.getString("NOME_PESSOA").trim().equalsIgnoreCase(nome.trim())){
                    log.warning("Dependente ja cadastrado [matricula = "
                            + matricula + " categoria = "
                            + idCategoria + "]");
                    throw new InserirDependenteException("Pessoa já existe nesta Matrícula.");
                }
            }

            if(seq == 0){
                log.warning("Impossível incluir dependente. Chave não encontrada. [matricula = "
                        + matricula + " categoria = "
                        + idCategoria + "]");
                throw new InserirDependenteException("Impossível incluir dependente. Chave não encontrada.");
            }

            try {
                carteirinha = Titular.proximoNumeroCarteirinha();
            } catch (ProximaCarteirinhaException e) {
                throw new InserirDependenteException(e.getMessage());
            }

            sql = "INSERT INTO TB_PESSOA (CD_MATRICULA, SEQ_DEPENDENTE, CD_CATEGORIA, "
                + "NOME_PESSOA, DT_NASCIMENTO, CD_SEXO, CD_SIT_PESSOA, DT_INCL_PESSOA, "
                + "CD_TP_DEPENDENTE, NR_CARTEIRA_PESSOA, NR_CARTEIRAS_EMITIDAS, "
                + "CD_CASO_ESPECIAL, DT_CASO_ESPECIAL, DT_VALID_RET_CONVITE, DT_VALID_RESERVA, "
                + "DT_VALID_MATERIAL, QT_ENTRADAS, CD_CARGO_ATIVO, CD_CARGO_ESPECIAL, DE_EMAIL, IC_EMP_TODOS_MAT, IC_TX_IND_TODAS_TX) "
                + "VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ? , ?, ?)";//22 parametros
            p = cn.prepareStatement(sql);
            ParametroAuditoria par = new ParametroAuditoria();
            p.setInt(1, par.getSetParametro(matricula));//CD_MATRICULA
            p.setInt(2, par.getSetParametro(seq));//SEQ_DEPENDENTE
            p.setInt(3, par.getSetParametro(idCategoria));//CD_CATEGORIA
            p.setString(4, par.getSetParametro(nome));//NOME_PESSOA
            p.setDate(5, new java.sql.Date(par.getSetParametro(dataNascimento).getTime()));//DT_NASCIMENTO
            if(masculino){
                p.setString(6, par.getSetParametro("M"));//CD_SEXO
            }else{
                p.setString(6, par.getSetParametro("F"));//CD_SEXO
            }
            p.setString(7, par.getSetParametro("NO"));//CD_SIT_PESSOA
            p.setDate(8, new java.sql.Date(par.getSetParametro(new Date()).getTime()));//DT_INCL_PESSOA
            p.setInt(9, par.getSetParametro(idTipoDependente));//CD_TP_DEPENDENTE
            p.setInt(10, par.getSetParametro(Integer.valueOf(carteirinha)));//NR_CARTEIRA_PESSOA
            p.setInt(11, par.getSetParametro(0));//NR_CARTEIRAS_EMITIDAS
            p.setString(12, par.getSetParametro(casoEspecial));//CD_CASO_ESPECIAL
            if(casoEspecial != null && casoEspecial.equalsIgnoreCase("U")){
                p.setDate(13, new java.sql.Date(par.getSetParametro(dataCasoEspecial).getTime()));//DT_CASO_ESPECIAL
            }else{
                p.setNull(13, java.sql.Types.DATE);//DT_CASO_ESPECIAL
                par.getSetNull();
            }
            if(dataValidadeRetiradaConvite == null){
                p.setNull(14, java.sql.Types.DATE);//DT_VALID_RET_CONVITE
                par.getSetNull();
            }else{
                p.setDate(14, new java.sql.Date(par.getSetParametro(dataValidadeRetiradaConvite).getTime()));//DT_VALID_RET_CONVITE
            }
            if(dataValidadeReservaChurrasqueira == null){
                p.setNull(15, java.sql.Types.DATE);//DT_VALID_RESERVA
                par.getSetNull();
            }else{
                p.setDate(15, new java.sql.Date(par.getSetParametro(dataValidadeReservaChurrasqueira).getTime()));//DT_VALID_RESERVA
            }
            if(dataValidadeRetiradaMaterial == null){
                p.setNull(16, java.sql.Types.DATE);//DT_VALID_MATERIAL
                par.getSetNull();
            }else{
                p.setDate(16, new java.sql.Date(par.getSetParametro(dataValidadeRetiradaMaterial).getTime()));//DT_VALID_MATERIAL
            }
            p.setInt(17, 0);//QT_ENTRADAS
            if(idCargoEspecial == 0){
                p.setNull(18, java.sql.Types.CHAR);//CD_CARGO_ATIVO
                par.getSetNull();
            }else{
                if(isCargoAtivo()){
                    p.setString(18, par.getSetParametro("S"));//CD_CARGO_ATIVO
                }else{
                    p.setString(18, par.getSetParametro("N"));//CD_CARGO_ATIVO
                }
            }
            if(idCargoEspecial == 0){
                p.setNull(19, java.sql.Types.INTEGER);//CD_CARGO_ESPECIAL
                par.getSetNull();
            }else{
                p.setInt(19, par.getSetParametro(idCargoEspecial));//CD_CARGO_ESPECIAL
            }
            p.setString(20, par.getSetParametro(email));//DE_EMAIL
            
            if(isEmpTodosMat()){
                p.setString(21, par.getSetParametro("S"));//IC_EMP_TODOS_MAT
            }else{
                p.setString(21, par.getSetParametro("N"));//IC_EMP_TODOS_MAT
            }
            if(isConsumoTodasTx()){
                p.setString(22, par.getSetParametro("S"));//IC_TX_IND_TODAS_TX
            }else{
                p.setString(22, par.getSetParametro("N"));//IC_TX_IND_TODAS_TX
            }
            
            p.executeUpdate();
            audit.registrarMudanca(sql, par.getParametroFinal());

            sql = "INSERT INTO TB_TELEFONE_PESSOA (CD_MATRICULA, CD_CATEGORIA, "
                    + "SEQ_DEPENDENTE, IC_TIPO, NR_TELEFONE) Values(?, ?, ?, ?, ?)";
            p = cn.prepareStatement(sql);
            if(telefoneCelular != null){
                par.limpaParametro();
                p.setInt(1, par.getSetParametro(matricula));
                p.setInt(2, par.getSetParametro(idCategoria));
                p.setInt(3, par.getSetParametro(seq));
                p.setString(4, par.getSetParametro("L"));
                p.setString(5, par.getSetParametro(telefoneCelular));
                p.executeUpdate();
                audit.registrarMudanca(sql, par.getParametroFinal());
            }
            if(telefoneComercial != null){
                par.limpaParametro();
                p.setInt(1, par.getSetParametro(matricula));
                p.setInt(2, par.getSetParametro(idCategoria));
                p.setInt(3, par.getSetParametro(seq));
                p.setString(4, par.getSetParametro("C"));
                p.setString(5, par.getSetParametro(telefoneComercial));
                p.executeUpdate();
                audit.registrarMudanca(sql, par.getParametroFinal());
            }
            if(telefoneResidencial != null){
                par.limpaParametro();
                p.setInt(1, par.getSetParametro(matricula));
                p.setInt(2, par.getSetParametro(idCategoria));
                p.setInt(3, par.getSetParametro(seq));
                p.setString(4, par.getSetParametro("R"));
                p.setString(5, par.getSetParametro(telefoneResidencial));
                p.executeUpdate();
                audit.registrarMudanca(sql, par.getParametroFinal());
            }
            if(fax != null){
                par.limpaParametro();
                p.setInt(1, par.getSetParametro(matricula));
                p.setInt(2, par.getSetParametro(idCategoria));
                p.setInt(3, par.getSetParametro(seq));
                p.setString(4, par.getSetParametro("F"));
                p.setString(5, par.getSetParametro(fax));
                p.executeUpdate();
                audit.registrarMudanca(sql, par.getParametroFinal());
            }

            sql = "DELETE TB_MATERIAL_AUTORIZADO_DEPENDENTE "
                + "WHERE CD_MATRICULA = ? AND CD_CATEGORIA = ? AND SEQ_DEPENDENTE = ?";
            p = cn.prepareStatement(sql);
            p.setInt(1, matricula);
            p.setInt(2, idCategoria);
            p.setInt(3, seqDependente);
            p.executeUpdate();

            sql = "INSERT INTO TB_MATERIAL_AUTORIZADO_DEPENDENTE VALUES(?, ?, ?, ?)";
            p = cn.prepareStatement(sql);
            for(int i : materiais.keySet()){
                par.limpaParametro();
                p.setInt(1, par.getSetParametro(matricula));
                p.setInt(2, par.getSetParametro(idCategoria));
                p.setInt(3, par.getSetParametro(seq));
                p.setInt(4, par.getSetParametro(i));
                p.executeUpdate();
                audit.registrarMudanca(sql, par.getParametroFinal());

            }

            cn.commit();

        } catch (SQLException e) {
            try {
                cn.rollback();
            } catch (SQLException ex) {
                log.severe(ex.getMessage());
            }

            log.severe(e.getMessage());
        }finally{
            try {
                cn.close();
            } catch (SQLException e) {
                log.severe(e.getMessage());
            }
        }
    }

    public void alterar(Auditoria audit){
        Connection cn = null;

        try {
            cn = Pool.getInstance().getConnection();
            String sql = "UPDATE TB_PESSOA SET NOME_PESSOA = ?, DT_NASCIMENTO = ?, CD_SEXO = ?, "
                    + "CD_TP_DEPENDENTE = ?, CD_CASO_ESPECIAL = ?, DT_CASO_ESPECIAL = ?, "
                    + "DT_VALID_RET_CONVITE = ?, DT_VALID_RESERVA = ?, DT_VALID_MATERIAL = ?, "
                    + "CD_CARGO_ATIVO = ?, CD_CARGO_ESPECIAL = ?, DE_EMAIL = ?, IC_EMP_TODOS_MAT = ?, IC_TX_IND_TODAS_TX = ? "
                    + "WHERE CD_MATRICULA = ? AND SEQ_DEPENDENTE = ? AND CD_CATEGORIA = ?";

            PreparedStatement p = cn.prepareStatement(sql);
            ParametroAuditoria par = new ParametroAuditoria();
            
            p.setString(1, par.getSetParametro(nome));//NOME_PESSOA
            p.setDate(2, new java.sql.Date(par.getSetParametro(dataNascimento).getTime()));//DT_NASCIMENTO
            if(masculino){
                p.setString(3, par.getSetParametro("M"));//CD_SEXO
            }else{
                p.setString(3, par.getSetParametro("F"));//CD_SEXO
            }
            p.setInt(4, par.getSetParametro(idTipoDependente));//CD_TP_DEPENDENTE
            p.setString(5, par.getSetParametro(casoEspecial));//CD_CASO_ESPECIAL
            if(casoEspecial.equalsIgnoreCase("U")){
                p.setDate(6, new java.sql.Date(par.getSetParametro(dataCasoEspecial).getTime()));//DT_CASO_ESPECIAL
            }else{
                p.setNull(6, java.sql.Types.DATE);//DT_CASO_ESPECIAL
                par.getSetNull();
            }
            if(dataValidadeRetiradaConvite == null){
                p.setNull(7, java.sql.Types.DATE);//DT_VALID_RET_CONVITE
                par.getSetNull();
            }else{
                p.setDate(7, new java.sql.Date(par.getSetParametro(dataValidadeRetiradaConvite).getTime()));//DT_VALID_RET_CONVITE
            }
            if(dataValidadeReservaChurrasqueira == null){
                p.setNull(8, java.sql.Types.DATE);//DT_VALID_RESERVA
                par.getSetNull();
            }else{
                p.setDate(8, new java.sql.Date(par.getSetParametro(dataValidadeReservaChurrasqueira).getTime()));//DT_VALID_RESERVA
            }
            if(dataValidadeRetiradaMaterial == null){
                p.setNull(9, java.sql.Types.DATE);//DT_VALID_MATERIAL
                par.getSetNull();
            }else{
                p.setDate(9, new java.sql.Date(par.getSetParametro(dataValidadeRetiradaMaterial).getTime()));//DT_VALID_MATERIAL
            }
            if(idCargoEspecial == 0){
                p.setNull(10, java.sql.Types.CHAR);//CD_CARGO_ATIVO
                par.getSetNull();
            }else{
                if(isCargoAtivo()){
                    p.setString(10, par.getSetParametro("S"));//CD_CARGO_ATIVO
                }else{
                    p.setString(10, par.getSetParametro("N"));//CD_CARGO_ATIVO
                }
            }
            if(idCargoEspecial == 0){
                p.setNull(11, java.sql.Types.INTEGER);//CD_CARGO_ESPECIAL
                par.getSetNull();
            }else{
                p.setInt(11, par.getSetParametro(idCargoEspecial));//CD_CARGO_ESPECIAL
            }
            p.setString(12, par.getSetParametro(email));//DE_EMAIL
            if(isEmpTodosMat()){
                p.setString(13, par.getSetParametro("S"));//IC_EMP_TODOS_MAT
            }else{
                p.setString(13, par.getSetParametro("N"));//IC_EMP_TODOS_MAT
            }
            if(isConsumoTodasTx()){
                p.setString(14, par.getSetParametro("S"));//IC_TX_IND_TODAS_TX
            }else{
                p.setString(14, par.getSetParametro("N"));//IC_TX_IND_TODAS_TX
            }

            p.setInt(15, par.getSetParametro(matricula));//CD_MATRICULA
            p.setInt(16, par.getSetParametro(seqDependente));//SEQ_DEPENDENTE
            p.setInt(17, par.getSetParametro(idCategoria));//CD_CATEGORIA

            
            
            p.executeUpdate();
            audit.registrarMudanca(sql, par.getParametroFinal());
            
            sql = "DELETE FROM TB_TELEFONE_PESSOA WHERE CD_MATRICULA = ? "
                    + "AND SEQ_DEPENDENTE = ? AND CD_CATEGORIA = ?";
            p = cn.prepareStatement(sql);
            p.setInt(1, matricula);//CD_MATRICULA
            p.setInt(2, seqDependente);//SEQ_DEPENDENTE
            p.setInt(3, idCategoria);//CD_CATEGORIA            
            p.executeUpdate();

            sql = "INSERT INTO TB_TELEFONE_PESSOA (CD_MATRICULA, CD_CATEGORIA, "
                    + "SEQ_DEPENDENTE, IC_TIPO, NR_TELEFONE) Values(?, ?, ?, ?, ?)";
            p = cn.prepareStatement(sql);
            if(telefoneCelular != null){
                par.limpaParametro();
                p.setInt(1, par.getSetParametro(matricula));
                p.setInt(2, par.getSetParametro(idCategoria));
                p.setInt(3, par.getSetParametro(seqDependente));
                p.setString(4, par.getSetParametro("L"));
                p.setString(5, par.getSetParametro(telefoneCelular));
                p.executeUpdate();
                audit.registrarMudanca(sql, par.getParametroFinal());
            }
            if(telefoneComercial != null){
                par.limpaParametro();
                p.setInt(1, par.getSetParametro(matricula));
                p.setInt(2, par.getSetParametro(idCategoria));
                p.setInt(3, par.getSetParametro(seqDependente));
                p.setString(4, par.getSetParametro("C"));
                p.setString(5, par.getSetParametro(telefoneComercial));
                p.executeUpdate();
                audit.registrarMudanca(sql, par.getParametroFinal());
            }
            if(telefoneResidencial != null){
                par.limpaParametro();
                p.setInt(1, par.getSetParametro(matricula));
                p.setInt(2, par.getSetParametro(idCategoria));
                p.setInt(3, par.getSetParametro(seqDependente));
                p.setString(4, par.getSetParametro("R"));
                p.setString(5, par.getSetParametro(telefoneResidencial));
                p.executeUpdate();
                audit.registrarMudanca(sql, par.getParametroFinal());
            }
            if(fax != null){
                par.limpaParametro();
                p.setInt(1, par.getSetParametro(matricula));
                p.setInt(2, par.getSetParametro(idCategoria));
                p.setInt(3, par.getSetParametro(seqDependente));
                p.setString(4, par.getSetParametro("F"));
                p.setString(5, par.getSetParametro(fax));
                p.executeUpdate();
                audit.registrarMudanca(sql, par.getParametroFinal());
            }

            sql = "DELETE TB_MATERIAL_AUTORIZADO_DEPENDENTE "
                + "WHERE CD_MATRICULA = ? AND CD_CATEGORIA = ? AND SEQ_DEPENDENTE = ?";
            p = cn.prepareStatement(sql);
            p.setInt(1, matricula);
            p.setInt(2, idCategoria);
            p.setInt(3, seqDependente);
            p.executeUpdate();

            sql = "INSERT INTO TB_MATERIAL_AUTORIZADO_DEPENDENTE VALUES(?, ?, ?, ?)";
            p = cn.prepareStatement(sql);
            for(int i : materiais.keySet()){
                par.limpaParametro();
                p.setInt(1, par.getSetParametro(matricula));
                p.setInt(2, par.getSetParametro(idCategoria));
                p.setInt(3, par.getSetParametro(seqDependente));
                p.setInt(4, par.getSetParametro(i));
                p.executeUpdate();
                audit.registrarMudanca(sql, par.getParametroFinal());
            }

            cn.commit();

        } catch (SQLException e) {
            try {
                cn.rollback();
            } catch (SQLException ex) {
                log.severe(ex.getMessage());
            }

            log.severe(e.getMessage());
        }finally{
            try {
                cn.close();
            } catch (SQLException e) {
                log.severe(e.getMessage());
            }
        }
    }

        public void excluir(Auditoria audit, boolean excluirTaxaFutura)throws ExcluirDependenteException{
        Connection cn = null;

        try {
           String sql = null;
            cn = Pool.getInstance().getConnection();

            if(!excluirTaxaFutura){
                sql = "SELECT 1 FROM TB_VAL_TAXA_INDIVIDUAL WHERE "
                        + "AA_COBRANCA >= Year(GETDATE()) AND MM_COBRANCA >= Month(GETDATE()) "
                        + "AND IC_SITUACAO_TAXA = 'N' AND CD_MATRICULA = ? AND CD_CATEGORIA = ? "
                        + "AND SEQ_DEPENDENTE = ?";
                PreparedStatement p = cn.prepareStatement(sql);
                p.setInt(1, matricula);
                p.setInt(2, idCategoria);
                p.setInt(3, seqDependente);

                ResultSet rs = p.executeQuery();
                if(rs.next()){
                    String err = "Existe cadastro de taxa individual futura. Deseja Continuar?";
                    log.fine(err);
                    throw new ExcluirDependenteException(err, true);
                }
            }

            sql = "{call SP_EXCLUI_PESSOA (?, ?, ?)}";
            CallableStatement cal = cn.prepareCall(sql);
            cal.setInt(1, matricula);
            cal.setInt(2, idCategoria);
            cal.setInt(3, seqDependente);
            cal.executeUpdate();

            cn.commit();
            audit.registrarMudanca(sql, String.valueOf(matricula), String.valueOf(idCategoria), String.valueOf(seqDependente));
        } catch (SQLException e) {
            try {
                cn.rollback();
            } catch (SQLException ex) {
                log.severe(ex.getMessage());
            }

            log.severe(e.getMessage());
        }finally{
            try {
                cn.close();
            } catch (SQLException e) {
                log.severe(e.getMessage());
            }
        }
    }

    public void carregarTaxasIndividuais(){
        String sql = "SELECT CD_TX_ADMINISTRATIVA FROM TB_TAXA_PESSOA_AUTORIZADA "
        + "WHERE CD_MATRICULA = ? AND SEQ_DEPENDENTE = ? AND CD_CATEGORIA = ?";
                  
        taxasIndividuais.clear();
        Connection cn = null;

        try {
            cn = Pool.getInstance().getConnection();
            PreparedStatement p = cn.prepareStatement(sql);
            p.setInt(1, matricula);
            p.setInt(2, seqDependente);
            p.setInt(3, idCategoria);
            
            ResultSet rs = p.executeQuery();
            while(rs.next()){
                taxasIndividuais.put(rs.getInt("CD_TX_ADMINISTRATIVA"), rs.getString("CD_TX_ADMINISTRATIVA"));
            }
        } catch (Exception e) {
            log.severe(e.getMessage());
        }finally{
            try {
                cn.close();
            } catch (SQLException e) {
                log.severe(e.getMessage());
            }
        }

    }
    
    public void atualizarTaxasIndividuais(Auditoria audit){
        String sql = "DELETE FROM TB_TAXA_PESSOA_AUTORIZADA WHERE CD_MATRICULA = ? "
                + "AND SEQ_DEPENDENTE = ? AND CD_CATEGORIA = ?";
       
        Connection cn = null;

        try {
            cn = Pool.getInstance().getConnection();
            PreparedStatement p = cn.prepareStatement(sql);
            p.setInt(1, matricula);
            p.setInt(2, seqDependente);
            p.setInt(3, idCategoria);
            p.executeUpdate();

            sql = "INSERT INTO TB_TAXA_PESSOA_AUTORIZADA "
                    + "(CD_MATRICULA, SEQ_DEPENDENTE, CD_CATEGORIA, CD_TX_ADMINISTRATIVA) VALUES (? , ?, ?, ?)";
            p = cn.prepareStatement(sql);
            ParametroAuditoria par = new ParametroAuditoria();
            for(int i : taxasIndividuais.keySet()){
                p.setInt(1, par.getSetParametro(matricula));
                p.setInt(2, par.getSetParametro(seqDependente));
                p.setInt(3, par.getSetParametro(idCategoria));
                p.setInt(4, par.getSetParametro(i));
                p.executeUpdate();
                audit.registrarMudanca(sql, par.getParametroFinal());
            }

            cn.commit();
        } catch (Exception e) {
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
