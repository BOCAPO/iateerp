package techsoft.controle.cadastro;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import techsoft.cadastro.ComboBoxLoader;
import techsoft.controle.annotation.App;
import techsoft.controle.annotation.Controller;
import techsoft.util.Datas;

@Controller
public class ControleRelatorioBarco {

    @App("1350")
    public static void dadosCadastrais(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String acao = request.getParameter("acao");

        if ("visualizar".equals(acao)) {

            StringBuilder sql = new StringBuilder();

            SimpleDateFormat fmt = new SimpleDateFormat("yyyy-MM-dd");
            Date dtRefValor = Datas.parse(request.getParameter("dtRefValor"));

            sql.append("SELECT ");
            String coluna = "";
            String alias = "";
            boolean temValor = false;
            int contaCol = 0;

            for (String s : request.getParameterValues("colunas")) {
                contaCol++;

                coluna = s.substring(0, s.indexOf(';'));
                alias = s.substring(s.indexOf(';') + 1, s.length());

                if (alias.equals("Valor")) {
                    temValor = true;
                    coluna = coluna.replace("<DATA>", "'" + fmt.format(dtRefValor) + "'");
                }

                sql.append(coluna);
                sql.append(" AS '");
                sql.append(alias);
                sql.append("', ");
            }
            sql.delete(sql.length() - 2, sql.length());

            sql.append(" FROM TB_PESSOA T1, TB_CATEGORIA T2, ");

            //GATO PARA QUE OS BOXES TAMBEM POSSAM FAZER JOIN COM A CATEGORIA NAUTICA
            sql.append("(SELECT ' ' CD_CATEGORIA_NAUTICA, ' ' DESCR_CATEGORIA_NAUTICA UNION ALL ");
            sql.append("SELECT CD_CATEGORIA_NAUTICA, DESCR_CATEGORIA_NAUTICA FROM TB_CATEGORIA_NAUTICA ");
            sql.append(" WHERE CD_CATEGORIA_NAUTICA IN (");
            for (String s : request.getParameterValues("categoriasNauticas")) {
                sql.append("'");
                sql.append(s);
                sql.append("', ");
            }
            sql.delete(sql.length() - 2, sql.length());
            sql.append(")) T3, ");

            sql.append("tB_BARCO T4, VW_COMPLEMENTO T5,");

            //GATO PARA QUE OS BARCOS TAMBEM POSSAM FAZER JOIN COM A LOCAL DOS BOXES
            sql.append("(SELECT ' ' CD_LOCAL, ' ' DESCR_LOCAL UNION ALL ");
            sql.append("SELECT CD_LOCAL, DESCR_LOCAL FROM TB_LOCAL_BOX_NAUTICA ");

            String teste2 = request.getParameter("localBox");

            if (request.getParameter("localBox") != null) {
                sql.append(" WHERE CD_LOCAL IN (");
                for (String s : request.getParameterValues("localBox")) {
                    sql.append(s);
                    sql.append(", ");
                }
                sql.delete(sql.length() - 2, sql.length());
                sql.append(")");
            }

            sql.append(") T6 ");

            sql.append("WHERE T1.CD_MATRICULA = T4.CD_MATRICULA ");
            sql.append(" AND  T1.CD_CATEGORIA = T4.CD_CATEGORIA ");
            sql.append(" AND  T1.SEQ_DEPENDENTE = T4.SEQ_DEPENDENTE ");
            sql.append(" AND  T1.CD_MATRICULA = T5.CD_MATRICULA ");
            sql.append(" AND  T1.CD_CATEGORIA = T5.CD_CATEGORIA ");
            sql.append(" AND  T1.SEQ_DEPENDENTE = T5.SEQ_DEPENDENTE ");
            sql.append(" AND  T1.CD_CATEGORIA = T2.CD_CATEGORIA ");
            sql.append(" AND  T3.CD_CATEGORIA_NAUTICA = T4.CD_CATEGORIA_NAUTICA ");
            sql.append(" AND  T6.CD_LOCAL = ISNULL(T4.CD_LOCAL, 0) ");
            sql.append(" AND  T1.CD_MATRICULA >= ");
            sql.append(request.getParameter("tituloInicio"));
            sql.append(" AND  T1.CD_MATRICULA <= ");
            sql.append(request.getParameter("tituloFim"));

            String pesInicio = request.getParameter("pesInicio");
            String pesFim = request.getParameter("pesFim");
            if (pesInicio != null && !"".trim().equals(pesInicio)) {
                sql.append(" AND T4.NR_PES >= ");
                sql.append(pesInicio);
            }

            if (pesFim != null && !"".trim().equals(pesFim)) {
                sql.append(" AND T4.NR_PES <= ");
                sql.append(pesFim);
            }

            sql.append(" AND  T1.CD_CATEGORIA IN (");
            for (String s : request.getParameterValues("categorias")) {
                sql.append(s);
                sql.append(", ");
            }
            sql.delete(sql.length() - 2, sql.length());
            sql.append(")");

            sql.append(" AND  (T4.CD_TP_VAGA_ESTACIONAMENTO IN (");
            for (String s : request.getParameterValues("tipoVaga")) {
                sql.append("'");
                sql.append(s);
                sql.append("', ");
            }
            sql.delete(sql.length() - 2, sql.length());
            sql.append(") OR T4.IC_BOX = 'S')");

            boolean barco = Boolean.parseBoolean(request.getParameter("barco"));
            boolean box = Boolean.parseBoolean(request.getParameter("box"));

            if (barco) {
                if (!box) {
                    sql.append(" AND ISNULL(T4.IC_BOX, 'N') = 'N'");
                }
            } else {
                sql.append(" AND ISNULL(T4.IC_BOX, 'N') = 'S'");
            }

            sql.append(" ORDER BY ");

            String ordenacao1 = request.getParameter("ordenacao1");
            String ordenacao2 = request.getParameter("ordenacao2");
            String ordenacao3 = request.getParameter("ordenacao3");

            sql.append(ordenacao1);

            if (!ordenacao1.equals(ordenacao2)) {
                sql.append(", ");
                sql.append(ordenacao2);
            }

            if (!ordenacao1.equals(ordenacao3) && !ordenacao2.equals(ordenacao3)) {
                sql.append(", ");
                sql.append(ordenacao3);
            }

            request.setAttribute("titulo", "Relatório de Embarcações");
            request.setAttribute("titulo2", "");
            request.setAttribute("sql", sql);
            request.setAttribute("quebra1", "false");
            request.setAttribute("quebra2", "false");
            if (temValor) {
                request.setAttribute("total1", contaCol);
            } else {
                request.setAttribute("total1", "-1");
            }

            request.setAttribute("total2", "-1");
            request.setAttribute("total3", "-1");
            request.setAttribute("total4", "-1");

            request.getRequestDispatcher("/pages/listagem.jsp").forward(request, response);
        } else if ("gerarEtiqueta".equals(acao)) {
            StringBuilder sql = new StringBuilder();
            StringBuilder where = new StringBuilder();
            where.append("WHERE T1.CD_MATRICULA = T5.CD_MATRICULA ");
            where.append(" AND  T1.CD_CATEGORIA = T5.CD_CATEGORIA ");
            where.append(" AND  T1.SEQ_DEPENDENTE = T5.SEQ_DEPENDENTE ");
            where.append(" AND  T1.CD_MATRICULA = T4.CD_MATRICULA ");
            where.append(" AND  T1.CD_CATEGORIA = T4.CD_CATEGORIA ");
            where.append(" AND  T1.SEQ_DEPENDENTE = T4.SEQ_DEPENDENTE ");
            where.append(" AND  T1.CD_CATEGORIA = T2.CD_CATEGORIA ");
            where.append(" AND  T3.CD_CATEGORIA_NAUTICA = T4.CD_CATEGORIA_NAUTICA ");
            where.append(" AND  T1.CD_MATRICULA >= ");
            where.append(request.getParameter("tituloInicio"));
            where.append(" AND  T1.CD_MATRICULA <= ");
            where.append(request.getParameter("tituloFim"));

            where.append(" AND  T1.CD_CATEGORIA IN (");
            for (String s : request.getParameterValues("categorias")) {
                where.append(s);
                where.append(", ");
            }
            where.delete(where.length() - 2, where.length());
            where.append(")");

            where.append(" AND  T3.CD_CATEGORIA_NAUTICA IN (");
            for (String s : request.getParameterValues("categoriasNauticas")) {
                where.append("'");
                where.append(s);
                where.append("', ");
            }
            where.delete(where.length() - 2, where.length());
            where.append(")");

            where.append(" AND  T4.CD_TP_VAGA_COBRANCA IN (");
            for (String s : request.getParameterValues("vagasCobranca")) {
                where.append("'");
                where.append(s);
                where.append("', ");
            }
            where.delete(where.length() - 2, where.length());
            where.append(")");

            where.append(" AND  T4.CD_TP_VAGA_ESTACIONAMENTO IN (");
            for (String s : request.getParameterValues("vagasEstacionamento")) {
                where.append("'");
                where.append(s);
                where.append("', ");
            }
            where.delete(where.length() - 2, where.length());
            where.append(")");

            sql.append("SELECT ");
            sql.append(" T1.CD_MATRICULA AS MATRICULA, ");
            sql.append(" T1.CD_CATEGORIA AS CATEGORIA, ");
            sql.append(" T1.NOME_PESSOA AS NOME, ");
            sql.append(" T5.ENDERECO_R AS ENDERECO, ");
            sql.append(" T5.BAIRRO_R AS BAIRRO, ");
            sql.append(" T5.CIDADE_R AS CIDADE, ");
            sql.append(" T5.UF_R AS UF,");
            sql.append(" T5.CEP_R AS CEP,");
            sql.append(" T1.CD_SEXO AS SEXO");
            sql.append(" FROM ");
            sql.append(" TB_PESSOA T1, ");
            sql.append(" TB_CATEGORIA T2, ");
            sql.append(" TB_CATEGORIA_NAUTICA T3, ");
            sql.append(" TB_BARCO T4, ");
            sql.append(" TB_COMPLEMENTO T5 ");
            sql.append(where);
            sql.append(" AND T5.CD_END_CORRESPONDENCIA = 'R' ");
            sql.append(" UNION ");
            sql.append("SELECT ");
            sql.append(" T1.CD_MATRICULA AS MATRICULA, ");
            sql.append(" T1.CD_CATEGORIA AS CATEGORIA, ");
            sql.append(" T1.NOME_PESSOA AS NOME, ");
            sql.append(" T5.ENDERECO_C AS ENDERECO, ");
            sql.append(" T5.BAIRRO_C AS BAIRRO, ");
            sql.append(" T5.CIDADE_C AS CIDADE, ");
            sql.append(" T5.UF_C AS UF,");
            sql.append(" T5.CEP_C AS CEP,");
            sql.append(" T1.CD_SEXO AS SEXO");
            sql.append(" FROM ");
            sql.append(" TB_PESSOA T1, ");
            sql.append(" TB_CATEGORIA T2, ");
            sql.append(" TB_CATEGORIA_NAUTICA T3, ");
            sql.append(" TB_BARCO T4, ");
            sql.append(" TB_COMPLEMENTO T5 ");
            sql.append(where);
            sql.append(" AND T5.CD_END_CORRESPONDENCIA = 'C' ");

            request.setAttribute("sql", sql.toString());
            request.getRequestDispatcher("/pages/1930.jsp").forward(request, response);
        } else {
            request.setAttribute("LocaisBox", ComboBoxLoader.listar("TB_LOCAL_BOX_NAUTICA"));
            request.setAttribute("categorias", ComboBoxLoader.listar("TB_CATEGORIA"));
            request.setAttribute("categoriasNauticas", ComboBoxLoader.listar("TB_CATEGORIA_NAUTICA"));
            request.getRequestDispatcher("/pages/1350.jsp").forward(request, response);
        }
    }

    @App("1660")
    public static void autorizacaoEmbarque(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String acao = request.getParameter("acao");

        if ("visualizar".equals(acao)) {
            StringBuilder sql = new StringBuilder();
            String titulo2 = null;

            sql.append("SELECT ");
            sql.append("NOME_SOCIO AS 'Sócio', ");
            sql.append("NOME_CONVIDADO AS 'Convidado', ");
            sql.append("DE_EMBARCACAO AS 'Embarcação', ");
            sql.append("DT_EMISSAO AS 'Emissão', ");
            sql.append("DT_VALIDADE AS 'Validade', ");
            sql.append("DT_UTILIZACAO AS 'Utilização', ");
            sql.append("CD_SIT_AUT_EMBARQUE AS 'Sit.' ");
            sql.append("FROM ");
            sql.append("TB_AUTORIZACAO_EMBARQUE ");
            sql.append("WHERE CD_SIT_AUT_EMBARQUE IN (");

            for (String s : request.getParameterValues("situacoes")) {
                sql.append("'");
                sql.append(s);
                sql.append("', ");
            }
            sql.delete(sql.length() - 2, sql.length());
            sql.append(") ");

            SimpleDateFormat fmt = new SimpleDateFormat("yyyy-MM-dd");
            Date d1 = Datas.parse(request.getParameter("dataInicio"));
            Date d2 = Datas.parse(request.getParameter("dataFim"));
            if (request.getParameter("tipoData").equals("E")) {
                sql.append(" AND DT_EMISSAO > '");
                sql.append(fmt.format(d1));
                sql.append(" 00:00:00'");
                sql.append(" AND DT_EMISSAO <= '");
                sql.append(fmt.format(d2));
                sql.append(" 23:59:59'");
                titulo2 = "Dt. Emissão entre "
                        + request.getParameter("dataInicio")
                        + " e " + request.getParameter("dataFim");
            } else {
                sql.append(" AND DT_VALIDADE > '");
                sql.append(fmt.format(d1));
                sql.append(" 00:00:00'");
                sql.append(" AND DT_VALIDADE <= '");
                sql.append(fmt.format(d2));
                sql.append(" 23:59:59'");
                titulo2 = "Dt. Validade entre "
                        + request.getParameter("dataInicio")
                        + " e " + request.getParameter("dataFim");
            }

            sql.append(" ORDER BY ");
            sql.append(request.getParameter("ordenacao1"));
            sql.append(", ");
            sql.append(request.getParameter("ordenacao2"));

            request.setAttribute("titulo", "Relatório de Autorização de Embarque");
            request.setAttribute("titulo2", titulo2);
            request.setAttribute("sql", sql);
            request.setAttribute("quebra1", "true");
            request.setAttribute("quebra2", "false");
            request.setAttribute("total1", "-1");
            request.setAttribute("total2", "-1");
            request.setAttribute("total3", "-1");
            request.setAttribute("total4", "-1");

            request.getRequestDispatcher("/pages/listagem.jsp").forward(request, response);
        } else {
            request.getRequestDispatcher("/pages/1660.jsp").forward(request, response);
        }
    }

    @App("1690")
    public static void vencimentos(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String acao = request.getParameter("acao");

        if ("visualizarDocumentacaoVencida".equals(acao)) {
            StringBuilder sql = new StringBuilder();
            sql.append("    SELECT ");
            sql.append("        CONVERT(VARCHAR, T1.CD_MATRICULA) + ' - ' + ");
            sql.append("        T1.NOME_PESSOA + ' - ' +  ");
            sql.append("        T4.DESCR_CATEGORIA AS 'Proprietário da Embarcação', ");
            sql.append("        T2.NO_BARCO as 'Embarcação', ");
            sql.append("        T3.DESCR_CATEGORIA_NAUTICA As 'Cat. Náutica', ");
            sql.append("        T2.DT_VENC_SEGURO as 'Dt.Venc.Seguro', ");
            sql.append("        T2.DT_VENC_REGISTRO as 'Dt.Venc.Registro', ");
            sql.append("        T2.DT_VENC_HABILITACAO as 'Dt.Venc.Habilitação'  ");
            sql.append("    From ");
            sql.append("        TB_PESSOA T1, ");
            sql.append("        TB_BARCO T2, ");
            sql.append("        TB_Categoria_Nautica T3, ");
            sql.append("        TB_Categoria T4 ");
            sql.append("    Where ");
            sql.append("        T1.CD_MATRICULA= T2.CD_MATRICULA AND ");
            sql.append("        T1.SEQ_DEPENDENTE= T2.SEQ_DEPENDENTE AND ");
            sql.append("        T1.CD_CATEGORIA= T2.CD_CATEGORIA AND ");
            sql.append("        T2.CD_CATEGORIA_NAUTICA = T3.CD_CATEGORIA_NAUTICA AND ");
            sql.append("        T1.CD_CATEGORIA = T4.CD_CATEGORIA AND ");
            sql.append("        (T2.DT_VENC_SEGURO < GETDATE() OR ");
            sql.append("        T2.DT_VENC_REGISTRO < GETDATE() OR ");
            sql.append("        T2.DT_VENC_HABILITACAO < GETDATE() ) ");
            sql.append(" AND T1.CD_MATRICULA BETWEEN ");
            sql.append(request.getParameter("tituloInicio"));
            sql.append(" AND ");
            sql.append(request.getParameter("tituloFim"));
            sql.append(" AND T1.CD_CATEGORIA IN (");

            for (String s : request.getParameterValues("categorias")) {
                sql.append(s);
                sql.append(", ");
            }
            sql.delete(sql.length() - 2, sql.length());
            sql.append(")");

            sql.append(" AND T2.CD_CATEGORIA_NAUTICA IN (");

            for (String s : request.getParameterValues("categoriasNauticas")) {
                sql.append("'");
                sql.append(s);
                sql.append("', ");
            }
            sql.delete(sql.length() - 2, sql.length());
            sql.append(")");

            request.setAttribute("titulo", "Relação de Embarcações com Documentação vencida");
            request.setAttribute("titulo2", "");
            request.setAttribute("sql", sql);
            request.setAttribute("quebra1", "true");
            request.setAttribute("quebra2", "false");
            request.setAttribute("total1", "-1");
            request.setAttribute("total2", "-1");
            request.setAttribute("total3", "-1");
            request.setAttribute("total4", "-1");

            request.getRequestDispatcher("/pages/listagem.jsp").forward(request, response);
        } else if ("visualizarPessoaAutorizada".equals(acao)) {
            StringBuilder sql = new StringBuilder();
            sql.append("    SELECT ");
            sql.append("        CONVERT(VARCHAR, T2.CD_MATRICULA) + ' - ' + ");
            sql.append("        T1.NO_PESSOA + ' - ' + ");
            sql.append("        T4.DESCR_CATEGORIA AS 'Proprietário da Embarcação',");
            sql.append("        T5.NO_BARCO + ' - ' + ");
            sql.append("        T3.DESCR_CATEGORIA_NAUTICA as 'Embarcação', ");
            sql.append("        T2.NOME_PESSOA as 'Pessoa Autorizada', ");
            sql.append("        T1.DE_OBSERVACAO as 'Observação'");
            sql.append("    From");
            sql.append("        TB_PESSOA_AUTOR_BARCO T1,");
            sql.append("        TB_PESSOA T2,");
            sql.append("        TB_Categoria_Nautica T3,");
            sql.append("        TB_Categoria T4,");
            sql.append("        TB_BARCO T5 ");
            sql.append("    Where");
            sql.append("        T1.CD_BARCO = T5.CD_BARCO AND ");
            sql.append("        T2.CD_MATRICULA= T5.CD_MATRICULA AND");
            sql.append("        T2.SEQ_DEPENDENTE= T5.SEQ_DEPENDENTE AND");
            sql.append("        T5.CD_CATEGORIA_NAUTICA = T3.CD_CATEGORIA_NAUTICA AND");
            sql.append("        T2.CD_CATEGORIA = T4.CD_CATEGORIA ");

            sql.append(" AND T2.CD_MATRICULA BETWEEN ");
            sql.append(request.getParameter("tituloInicio"));
            sql.append(" AND ");
            sql.append(request.getParameter("tituloFim"));
            sql.append(" AND T2.CD_CATEGORIA IN (");

            for (String s : request.getParameterValues("categorias")) {
                sql.append(s);
                sql.append(", ");
            }
            sql.delete(sql.length() - 2, sql.length());
            sql.append(")");

            sql.append(" AND T5.CD_CATEGORIA_NAUTICA IN (");

            for (String s : request.getParameterValues("categoriasNauticas")) {
                sql.append("'");
                sql.append(s);
                sql.append("', ");
            }
            sql.delete(sql.length() - 2, sql.length());
            sql.append(")");

            request.setAttribute("titulo", "Pessoas autorizadas para Embarcações");
            request.setAttribute("titulo2", "");
            request.setAttribute("sql", sql);
            request.setAttribute("quebra1", "true");
            request.setAttribute("quebra2", "true");
            request.setAttribute("total1", "-1");
            request.setAttribute("total2", "-1");
            request.setAttribute("total3", "-1");
            request.setAttribute("total4", "-1");

            request.getRequestDispatcher("/pages/listagem.jsp").forward(request, response);
        } else {
            request.setAttribute("categorias", ComboBoxLoader.listar("TB_CATEGORIA"));
            request.setAttribute("categoriasNauticas", ComboBoxLoader.listar("TB_CATEGORIA_NAUTICA"));
            request.getRequestDispatcher("/pages/1690.jsp").forward(request, response);
        }
    }

    @App("1730")
    public static void registroCancelamento(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String acao = request.getParameter("acao");

        if ("visualizar".equals(acao)) {
            StringBuilder sql = new StringBuilder();

            sql.append("SELECT ");
            sql.append("T1.CD_MATRICULA      AS 'Matr.', ");
            sql.append("T1.NOME_PESSOA    AS 'Nome', ");

            sql.append("CASE ISNULL(IC_BOX, 'N')");
            sql.append("WHEN 'S' THEN 'BOX (' + CONVERT(VARCHAR, T3.NR_BOX_BARCO) + ')' ");
            sql.append("ELSE T3.NO_BARCO ");
            sql.append("END 'Embarcacao/Box', ");

            sql.append("CASE ISNULL(IC_BOX, 'N')");
            sql.append("WHEN 'S' THEN T5.DESCR_LOCAL ");
            sql.append("ELSE T4.DESCR_CATEGORIA_NAUTICA ");
            sql.append("END 'Cat. Nautica/Local', ");

            sql.append("T3.DT_INCLUSAO    AS 'Data', ");
            sql.append("Case CD_SITUACAO_BARCO ");
            sql.append("WHEN 'I' THEN 'Registro' ");
            sql.append("WHEN 'E' THEN 'Baixa' ");
            sql.append("END as 'Situacao' ");
            sql.append("From ");
            sql.append("TB_PESSOA      T1, ");
            sql.append("TB_CATEGORIA      T2, ");
            sql.append("TB_HISTORICO_BARCO   T3, ");
            sql.append("TB_CATEGORIA_NAUTICA T4, ");
            sql.append("TB_LOCAL_BOX_NAUTICA T5 ");
            sql.append("Where ");
            sql.append("T1.CD_MATRICULA      = T3.CD_MATRICULA    AND ");
            sql.append("T1.CD_CATEGORIA      = T3.CD_CATEGORIA    AND ");
            sql.append("T1.SEQ_DEPENDENTE = T3.SEQ_DEPENDENTE     AND ");
            sql.append("T1.CD_CATEGORIA      = T2.CD_CATEGORIA    AND ");
            sql.append("T3.CD_CATEGORIA_NAUTICA *= T4.CD_CATEGORIA_NAUTICA AND ");
            sql.append("T3.CD_LOCAL *= T5.CD_LOCAL ");

            SimpleDateFormat fmt = new SimpleDateFormat("yyyy-MM-dd");
            Date d1 = Datas.parse(request.getParameter("dataInicio"));
            Date d2 = Datas.parse(request.getParameter("dataFim"));
            sql.append(" AND T3.DT_INCLUSAO >= '");
            sql.append(fmt.format(d1));
            sql.append(" 00:00:00'");
            sql.append(" AND T3.DT_INCLUSAO <= '");
            sql.append(fmt.format(d2));
            sql.append(" 23:59:59'");

            boolean registro = Boolean.parseBoolean(request.getParameter("registro"));
            boolean cancelamento = Boolean.parseBoolean(request.getParameter("cancelamento"));
            boolean barco = Boolean.parseBoolean(request.getParameter("barco"));
            boolean box = Boolean.parseBoolean(request.getParameter("box"));

            if (cancelamento) {
                if (!registro) {
                    sql.append(" AND T3.CD_SITUACAO_BARCO = 'E'");
                }
            } else {
                sql.append(" AND T3.CD_SITUACAO_BARCO = 'I'");
            }
            if (barco) {
                if (!box) {
                    sql.append(" AND ISNULL(T3.IC_BOX, 'N') = 'N'");
                }
            } else {
                sql.append(" AND ISNULL(T3.IC_BOX, 'N') = 'S'");
            }

            request.setAttribute("titulo", "Registro e Baixa de Embarcações");
            request.setAttribute("titulo2", request.getParameter("dataInicio")
                    + " a " + request.getParameter("dataFim"));
            request.setAttribute("sql", sql);
            request.setAttribute("quebra1", "false");
            request.setAttribute("quebra2", "false");
            request.setAttribute("total1", "-1");
            request.setAttribute("total2", "-1");
            request.setAttribute("total3", "-1");
            request.setAttribute("total4", "-1");

            request.getRequestDispatcher("/pages/listagem.jsp").forward(request, response);
        } else {
            request.getRequestDispatcher("/pages/1730.jsp").forward(request, response);
        }
    }

    @App("2060")
    public static void movimentacao(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String acao = request.getParameter("acao");

        if ("visualizar".equals(acao)) {
            StringBuilder sql = new StringBuilder();

            sql.append("select ");
            sql.append("t1.dt_movimento as 'Data/Hora Movimento'      , ");
            sql.append("t3.NOME_PESSOA as Pessoa   , ");
            sql.append("substring(t2.NO_BARCO, 1, 40) as Barco   , ");
            sql.append("CASE t1.ic_entrada_saida   ");
            sql.append("WHEN 'E' THEN 'Entrada'    ");
            sql.append("Else 'Saída'    ");
            sql.append("END As Tipo,    ");
            sql.append("Case t1.ic_local_movimento    ");
            sql.append("WHEN 'C' THEN 'Cais'    ");
            sql.append("WHEN 'L' THEN 'Clube'    ");
            sql.append("WHEN 'A' THEN 'Água'    ");
            sql.append("Else 'Rampa'    ");
            sql.append("END as Local,    ");
            sql.append("t2.QT_MAX_TRIPULANTE as Lotação ");
            sql.append("From ");
            sql.append("tb_movimento_barco   t1, ");
            sql.append("tb_barco    t2, ");
            sql.append("tb_pessoa t3 ");
            sql.append("Where ");
            sql.append("t1.cd_barco    = t2.cd_barco     and ");
            sql.append("t2.cd_matricula      = t3.cd_matricula and ");
            sql.append("t2.cd_categoria      = t3.cd_categoria and ");
            sql.append("t2.seq_dependente = t3.seq_dependente  and ");
            sql.append("t1.ic_situacao       = 'N' ");

            SimpleDateFormat fmt = new SimpleDateFormat("yyyy-MM-dd");
            Date d1 = Datas.parse(request.getParameter("dataInicio"));
            Date d2 = Datas.parse(request.getParameter("dataFim"));
            sql.append(" AND T1.DT_MOVIMENTO >= '");
            sql.append(fmt.format(d1));
            sql.append(" 00:00:00'");
            sql.append(" AND T1.DT_MOVIMENTO <= '");
            sql.append(fmt.format(d2));
            sql.append(" 23:59:59'");

            sql.append(" AND T1.ic_local_movimento in (");
            for (String s : request.getParameterValues("locais")) {
                sql.append("'");
                sql.append(s);
                sql.append("', ");
            }
            sql.delete(sql.length() - 2, sql.length());
            sql.append(")");

            boolean entrada = Boolean.parseBoolean(request.getParameter("entrada"));
            boolean saida = Boolean.parseBoolean(request.getParameter("saida"));
            if (entrada) {
                if (!saida) {
                    sql.append(" AND T1.ic_entrada_saida = 'E' ");
                }
            } else {
                sql.append(" AND T1.ic_entrada_saida = 'S' ");
            }

            sql.append(" ORDER BY 1");

            request.setAttribute("titulo", "Relatório de Movimentação de Embarcação");
            request.setAttribute("titulo2", request.getParameter("dataInicio")
                    + " a " + request.getParameter("dataFim"));
            request.setAttribute("sql", sql);
            request.setAttribute("quebra1", "false");
            request.setAttribute("quebra2", "false");
            request.setAttribute("total1", "-1");
            request.setAttribute("total2", "-1");
            request.setAttribute("total3", "-1");
            request.setAttribute("total4", "-1");

            request.getRequestDispatcher("/pages/listagem.jsp").forward(request, response);
        } else {
            request.getRequestDispatcher("/pages/2060.jsp").forward(request, response);
        }
    }
}
