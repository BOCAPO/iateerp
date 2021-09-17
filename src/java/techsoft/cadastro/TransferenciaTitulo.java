package techsoft.cadastro;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import java.util.logging.Logger;
import techsoft.db.Pool;
import techsoft.seguranca.Auditoria;
import techsoft.seguranca.ParametroAuditoria;

public class TransferenciaTitulo {
    
    private Titular titular;
    private static final Logger log = Logger.getLogger("techsoft.cadastro.TransferenciaTitulo");
    
    private TransferenciaTitulo(Titular titular){
        this.titular = titular;
    }
    
    public void transferir(Titular novoTitular, Auditoria audit){

        Connection cn = null;

        try {
            cn = Pool.getInstance().getConnection();
            ParametroAuditoria par = new ParametroAuditoria();
            /*
             * 58 parametros
             */
            String sql = "{call SP_TRANSFERE_TITULO ("
                    + "?, ?, ?, ?, ?, ?, ?, ?, ?, ?, "
                    + "?, ?, ?, ?, ?, ?, ?, ?, ?, ?, "
                    + "?, ?, ?, ?, ?, ?, ?, ?, ?, ?, "
                    + "?, ?, ?, ?, ?, ?, ?, ?, ?, ?, "
                    + "?, ?, ?, ?, ?, ?, ?, ?, ?, ?, "
                    + "?, ?, ?, ?, ?, ?, ?, ?)}";
            CallableStatement cal = cn.prepareCall(sql);
            cal.setInt(1, par.getSetParametro(titular.getSocio().getMatricula()));//CD_MATRICULA
            cal.setInt(2, par.getSetParametro(0));//SEQ_DEPENDENTE
            cal.setInt(3, par.getSetParametro(titular.getSocio().getIdCategoria()));//CD_CATEGORIA
            cal.setNull(4, java.sql.Types.INTEGER);//CD_TP_DEPENDENTE
            par.getSetNull();
            cal.setString(5, par.getSetParametro(novoTitular.getSocio().getNome()));//NOME_PESSOA
            cal.setDate(6, new java.sql.Date(par.getSetParametro(novoTitular.getSocio().getDataNascimento().getTime())));//DT_NASCIMENTO
            if(novoTitular.getSocio().isMasculino()){
                cal.setString(7, par.getSetParametro("M"));//CD_SEXO
            }else{
                cal.setString(7, par.getSetParametro("F"));//CD_SEXO
            }
            cal.setString(8, par.getSetParametro(novoTitular.getSocio().getSituacao()));//CD_SIT_PESSOA
            cal.setDate(9, new java.sql.Date(par.getSetParametro(new Date().getTime())));//DT_INIC_SIT_PESSOA
            cal.setNull(10, java.sql.Types.DATE);//DT_FIM_SIT_PESSOA
            par.getSetNull();
            cal.setInt(11, par.getSetParametro(novoTitular.getSocio().getNrCarteira()));//NR_CARTEIRA_PESSOA
            cal.setNull(12, java.sql.Types.DATE);//DT_VENC_CARTEIRA
            par.getSetNull();
            cal.setInt(13, par.getSetParametro(0));//NR_CARTEIRAS_EMITIDAS
            cal.setNull(14, java.sql.Types.DATE);//DT_VALID_EX_MEDICO_PESSOA
            par.getSetNull();
            cal.setString(15, par.getSetParametro("N"));//CD_CASO_ESPECIAL
            cal.setNull(16, java.sql.Types.VARCHAR);//CD_DEP_ISENTO
            par.getSetNull();
            cal.setDate(17, new java.sql.Date(par.getSetParametro(new Date().getTime())));//DT_INCL_PESSOA
            cal.setInt(18, par.getSetParametro(novoTitular.getIdProfissao()));//CD_PROFISSAO
            cal.setString(19, par.getSetParametro(novoTitular.getEnderecoResidencial().getEndereco()));//ENDERECO_R
            cal.setString(20, par.getSetParametro(novoTitular.getEnderecoResidencial().getBairro()));//BAIRRO_R
            cal.setString(21, par.getSetParametro(novoTitular.getEnderecoResidencial().getCidade()));//CIDADE_R
            cal.setString(22, par.getSetParametro(novoTitular.getEnderecoResidencial().getUF()));//UF_R
            cal.setString(23, par.getSetParametro(novoTitular.getEnderecoResidencial().getCEP().replace(".", "").replace("-", "")));//CEP_R
            cal.setString(24, par.getSetParametro(novoTitular.getEnderecoResidencial().getTelefone()));//TELEFONE_R
            cal.setString(25, par.getSetParametro(novoTitular.getEnderecoComercial().getTelefone()));//TELEFONE_C
            cal.setString(26, par.getSetParametro(novoTitular.getEnderecoComercial().getCEP().replace(".", "").replace("-", "")));//CEP_C
            cal.setString(27, par.getSetParametro(novoTitular.getEstadoCivil()));//ESTADO_CIVIL
            cal.setInt(28, par.getSetParametro(Titular.getSdconvite(novoTitular.getSocio().getMatricula(), novoTitular.getSocio().getIdCategoria())));//SD_CONVITE
            cal.setNull(29, java.sql.Types.DATE);//DT_VENC_PROX_CARNE_TX_ADM
            par.getSetNull();
            cal.setNull(30, java.sql.Types.DATE);//DT_VENC_ULTIMO_CARNE_TX_ADM
            par.getSetNull();
            
            if(titular.getCpfCnpj() == null || titular.getCpfCnpj().equals("")){
                cal.setNull(31, java.sql.Types.NUMERIC);//CPF_CGC_PESSOA
                par.getSetNull();
            }else{
                cal.setLong(31, par.getSetParametro(Long.parseLong(titular.getCpfCnpj().replace(".", "").replace("-", "").replace("/", ""))));//CPF_CGC_PESSOA
            }
            cal.setString(32, par.getSetParametro(titular.getEnderecoComercial().getEndereco()));//ENDERECO_C
            cal.setString(33, par.getSetParametro(titular.getEnderecoComercial().getUF()));//UF_C
            cal.setString(34, par.getSetParametro(titular.getEnderecoComercial().getBairro()));//BAIRRO_C
            cal.setString(35, par.getSetParametro(titular.getEnderecoComercial().getCidade()));//CIDADE_C
            cal.setString(36, par.getSetParametro(String.valueOf(novoTitular.getDestinoCorrespondencia())));//CD_END_CORRESPONDENCIA
            cal.setString(37, par.getSetParametro(String.valueOf(novoTitular.getDestinoCarne())));//CD_END_CARNE
            cal.setInt(38, par.getSetParametro(novoTitular.getContaCorrente().getAgencia()));//NR_AGENCIA_CC
            if(novoTitular.isPessoaFisica()){
                cal.setString(39, par.getSetParametro("F"));//CD_IND_PES_FISICA_JURIDICA
            }else{
                cal.setString(39, par.getSetParametro("J"));//CD_IND_PES_FISICA_JURIDICA
            }
            cal.setString(40, par.getSetParametro(novoTitular.getContaCorrente().getDigitoAgencia()));//DV_AGENCIA_CC
            cal.setString(41, par.getSetParametro(novoTitular.getContaCorrente().getConta()));//NR_CONTA_CORRENTE
            cal.setString(42, par.getSetParametro(novoTitular.getContaCorrente().getDigitoConta()));//DV_CONTA_CORRENTE
            cal.setString(43, par.getSetParametro(novoTitular.getRG()));//NR_IDENTIDADE_PESSOA
            cal.setString(44, par.getSetParametro(novoTitular.getContaCorrente().getTitular()));//NO_TITULAR_CC
            cal.setInt(45, par.getSetParametro(0));//SD_CONVITE_CHURRASQUEIRA
            cal.setInt(46, par.getSetParametro(0));//SD_CONVITE_SINUCA
            cal.setInt(47, par.getSetParametro(0));//SD_CONVITE_SAUNA
            if(novoTitular.getIdCargoEspecial() == 0){
                cal.setNull(48, java.sql.Types.INTEGER);//CD_TIPO_PESSOA
                par.getSetNull();
            }else{
                cal.setInt(48, par.getSetParametro(novoTitular.getIdCargoEspecial()));//CD_TIPO_PESSOA
            }
            cal.setString(49, par.getSetParametro(novoTitular.getNacionalidade()));//NACIONALIDADE
            cal.setString(50, par.getSetParametro(novoTitular.getNaturalidade()));//NATURALIDADE            
            cal.setString(51, par.getSetParametro(novoTitular.getNomePai()));//NOME_PAI
            cal.setString(52, par.getSetParametro(novoTitular.getNomeMae()));//NOME_MAE

            cal.setInt(53, 0);//CD_ARMARIO
            if(novoTitular.getIdCargoEspecial() == 0){
                cal.setNull(54, java.sql.Types.CHAR);//CD_CARGO_ATIVO
                par.getSetNull();
            }else{
                if(novoTitular.isCargoAtivo()){
                    cal.setString(54, par.getSetParametro("S"));//CD_CARGO_ATIVO
                }else{
                    cal.setString(54, par.getSetParametro("N"));//CD_CARGO_ATIVO
                }
            }
            cal.setInt(55, par.getSetParametro(novoTitular.getProponente().getMatricula()));//CD_MATRICULA_PROPONENTE
            cal.setInt(56, par.getSetParametro(novoTitular.getProponente().getIdCategoria()));//CD_CATEGORIA_PROPONENTE
            cal.setString(57, par.getSetParametro(novoTitular.getProponente().getNome()));//NOME_PROPONENTE
            cal.setString(58, par.getSetParametro(novoTitular.getEmail()));//EMAIL


            cal.executeUpdate();
            cn.commit();

            audit.registrarMudanca(sql, par.getParametroFinal());
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
    
    public static TransferenciaTitulo validar(Titular titular, boolean ignorarTaxaIndividual)throws TransferenciaTituloException{
        Connection cn = null;

        try {
            String sql = "SELECT CD_IND_TRANSFERENCIA FROM TB_CATEGORIA WHERE CD_CATEGORIA = ?";
            cn = Pool.getInstance().getConnection();
            PreparedStatement p = cn.prepareStatement(sql);
            p.setInt(1, titular.getSocio().getIdCategoria());

            ResultSet rs = p.executeQuery();
            if(rs.next()){
                if(rs.getString(1).equalsIgnoreCase("N")){
                    throw new TransferenciaTituloException("Categoria do Título não permite transferência.");
                }
            }else{
                throw new TransferenciaTituloException("Não foi encontrada a categoria do Título.");
            }
            
            sql = "SELECT 1 FROM TB_MATRICULA_CURSO WHERE CD_MATRICULA = ? AND "
                + "CD_CATEGORIA = ? AND CD_SIT_MATRICULA IN ('NO', 'NT')";
            
            p = cn.prepareStatement(sql);
            p.setInt(1, titular.getSocio().getMatricula());
            p.setInt(2, titular.getSocio().getIdCategoria());

            rs = p.executeQuery();
            if (rs.next()) {
                throw new TransferenciaTituloException("Existe alguma pessoa da família matriculada em algum Curso!");
            }

            sql = "SELECT 1 FROM TB_BARCO WHERE CD_MATRICULA = ? AND CD_CATEGORIA = ?";
                    
            p = cn.prepareStatement(sql);
            p.setInt(1, titular.getSocio().getMatricula());
            p.setInt(2, titular.getSocio().getIdCategoria());

            rs = p.executeQuery();
            if (rs.next()) {
                throw new TransferenciaTituloException("Existe Embarcação associada a Família!");
            }

            if(!ignorarTaxaIndividual){
                sql = "SELECT COUNT(*) FROM TB_VAL_TAXA_INDIVIDUAL "
                + "WHERE AA_COBRANCA >= Year(GETDATE()) And MM_COBRANCA >= Month(GETDATE()) AND IC_SITUACAO_TAXA = 'N' AND "
                + "CD_MATRICULA = ? AND CD_CATEGORIA = ?";
                p = cn.prepareStatement(sql);
                p.setInt(1, titular.getSocio().getMatricula());
                p.setInt(2, titular.getSocio().getIdCategoria());

                rs = p.executeQuery();
                if (rs.next()) {
                    if(rs.getInt(1) > 0){
                        throw new TransferenciaTituloException("Existe cadastro de taxa individual futura. Deseja Continuar?", true);
                    }
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
        
        return new TransferenciaTitulo(titular);
    }    
}
