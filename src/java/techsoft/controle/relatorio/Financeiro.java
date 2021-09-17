package techsoft.controle.relatorio;

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
public class Financeiro {

    @App("1800")
    public static void valoresRecebidos(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String acao = request.getParameter("acao");

        if ("visualizar".equals(acao)) {
            StringBuilder sql = new StringBuilder();
            boolean analitico = request.getParameter("tipo").equals("A");
            boolean baixa = request.getParameter("periodo").equals("B");
            boolean detalhado = request.getParameter("tipo").equals("D");
            SimpleDateFormat fmt = new SimpleDateFormat("yyyy-MM-dd");
            Date d1 = Datas.parse(request.getParameter("dataInicio"));
            Date d2 = Datas.parse(request.getParameter("dataFim"));
            Date d3 = null;
            Date d4 = null;
            boolean filtrarVencimento = Boolean.parseBoolean(request.getParameter("filtrarVencimento"));
            boolean recebidoBanco = Boolean.parseBoolean(request.getParameter("recebidoBanco"));
            boolean recebidoCaixa = Boolean.parseBoolean(request.getParameter("recebidoCaixa"));
            boolean bancoBB = Boolean.parseBoolean(request.getParameter("bancoBB"));
            boolean bancoItau = Boolean.parseBoolean(request.getParameter("bancoItau"));
            boolean flagCurso = false;//escolinhas
            boolean flagAvulso = false;//Boleto Avulso

            if (filtrarVencimento) {
                d3 = Datas.parse(request.getParameter("vencimentoInicio"));
                d4 = Datas.parse(request.getParameter("vencimentoFim"));
            }

            StringBuilder tmp = new StringBuilder();
            for (String s : request.getParameterValues("taxas")) {
                tmp.append(s);
                tmp.append(", ");
                if (s.equals("8") ) {
                    flagCurso = true;
                }
                if (s.equals("0")) {
                    flagAvulso = true;
                }
            }
            tmp.delete(tmp.length() - 2, tmp.length());

            if (analitico || detalhado) {
                sql.append("EXEC SP_REL_RECEBIMENTO_ANALITICO ");

                if (baixa) {
                    sql.append("'");
                    sql.append(fmt.format(d1));
                    sql.append(" 00:00:00', '");
                    sql.append(fmt.format(d2));
                    sql.append(" 23:59:59', ");
                    sql.append("'01/01/1900 00:00:00', '12/31/9999 23:59:59', ");
                } else {
                    sql.append("'01/01/1900 00:00:00', '12/31/9999 23:59:59', ");
                    sql.append("'");
                    sql.append(fmt.format(d1));
                    sql.append(" 00:00:00', '");
                    sql.append(fmt.format(d2));
                    sql.append(" 23:59:59', ");
                }

                if (filtrarVencimento) {
                    sql.append("'");
                    sql.append(fmt.format(d3));
                    sql.append(" 00:00:00', '");
                    sql.append(fmt.format(d4));
                    sql.append(" 23:59:59', ");
                } else {
                    sql.append("'01/01/1900 00:00:00', '12/31/9999 23:59:59', ");
                }

                if (recebidoBanco) {
                    if (recebidoCaixa) {
                        sql.append("'T', ");
                    } else {
                        sql.append("'B', ");
                    }
                } else {
                    sql.append("'C', ");
                }
                if (bancoBB) {
                    if (bancoItau) {
                        sql.append("'T', ");
                    } else {
                        sql.append("'BB', ");
                    }
                } else {
                    sql.append("'ITAU', ");
                }
                
                sql.append("'(");
                sql.append(tmp);
                sql.append(")', ");
                
                if (detalhado) {
                    sql.append("'D'");
                }else{
                    sql.append("'A'");
                }
            } else {
                sql.append("select   'Contribuições', t3.descr_categoria, ");
                sql.append("t1.descr_tx_administrativa, ");
                sql.append("sum(t2.val_parc_adm) as val_devido, ");
                sql.append("sum(t2.val_desconto_adm) as val_desconto, ");
                sql.append("sum(t2.val_multa_adm) as val_multa, ");
                sql.append("sum(t2.val_juros_adm) as val_juros, ");
                sql.append("sum(t2.val_encargo_adm) as val_encargo, ");
                sql.append("sum(t2.val_pgto_parc_adm) as val_pago ");
                sql.append("from ");
                sql.append("tb_taxa_administrativa t1, ");
                sql.append("tb_parcela_administrativa t2, ");
                sql.append("tb_categoria t3, ");
                sql.append("tb_carne t4 ");
                sql.append("where t1.IND_TAXA_ADMINISTRATIVA <> 'I' ");
                sql.append(" and t2.cd_sit_parc_adm = 'PG' ");
                sql.append(" and t2.cd_tx_administrativa = t1.cd_tx_administrativa ");
                sql.append(" and t2.cd_categoria = t3.cd_categoria ");
                sql.append(" and t2.seq_carne = t4.seq_carne ");
                
                sql.append(" and T1.CD_TX_ADMINISTRATIVA IN( ");
                sql.append(tmp);
                sql.append(") ");
                

                if (baixa) {
                    sql.append("  AND     t2.DT_BAIXA_PARC_ADM >= '");
                    sql.append(fmt.format(d1));
                    sql.append(" 00:00:00' ");
                    sql.append(" AND     t2.DT_BAIXA_PARC_ADM <= '");
                    sql.append(fmt.format(d2));
                    sql.append(" 23:59:59' ");
                } else {
                    sql.append("  AND     t2.DT_PGTO_PARC_ADM >= '");
                    sql.append(fmt.format(d1));
                    sql.append(" 00:00:00' ");
                    sql.append(" AND     t2.DT_PGTO_PARC_ADM <= '");
                    sql.append(fmt.format(d2));
                    sql.append(" 23:59:59' ");
                }

                if (filtrarVencimento) {
                    sql.append(" AND t2.DT_VCTO_PARC_ADM >= '");
                    sql.append(fmt.format(d3));
                    sql.append(" 00:00:00' ");
                    sql.append(" AND t2.DT_VCTO_PARC_ADM <= '");
                    sql.append(fmt.format(d4));
                    sql.append(" 23:59:59' ");
                }


                if (!recebidoBanco) {
                    sql.append(" and t2.local_pgto_parc_adm <> 'B' ");
                }
                if (!recebidoCaixa) {
                    sql.append(" and t2.local_pgto_parc_adm <> 'C' ");
                }
                
                if (!bancoBB) {
                    sql.append(" and ISNULL(t4.BANCO_PGTO, '') <> 'BB' ");
                }
                if (!bancoItau) {
                    sql.append(" and ISNULL(t4.BANCO_PGTO, '') <> 'ITAU' ");
                }

                sql.append("group by t3.descr_categoria, t1.descr_tx_administrativa ");

                /**
                 * ***********************
                 * Somente taxas individuais
                 ************************
                 */
                sql.append(" Union all ");

                sql.append("select   'Contribuições Individuais e Cursos', 'Taxas Individuais', ");
                sql.append("t1.descr_tx_administrativa, ");
                sql.append("sum(t2.val_parc_adm) as val_devido, ");
                sql.append("sum(t2.val_desconto_adm) as val_desconto, ");
                sql.append("sum(t2.val_multa_adm) as val_multa, ");
                sql.append("sum(t2.val_juros_adm) as val_juros, ");
                sql.append("sum(t2.val_encargo_adm) as val_encargo, ");
                sql.append("sum(t2.val_pgto_parc_adm) as val_pago ");
                sql.append("from ");
                sql.append("tb_taxa_administrativa t1, ");
                sql.append("tb_parcela_administrativa t2, ");
                sql.append("tb_categoria t3, ");
                sql.append("tb_carne t4 ");
                sql.append("where t1.IND_TAXA_ADMINISTRATIVA = 'I' ");
                sql.append(" and t2.cd_sit_parc_adm = 'PG' ");
                sql.append(" and t2.cd_tx_administrativa = t1.cd_tx_administrativa ");
                sql.append(" and t2.cd_categoria = t3.cd_categoria ");
                sql.append(" and t2.seq_carne = t4.seq_carne ");

                sql.append(" and T1.CD_TX_ADMINISTRATIVA IN( ");
                sql.append(tmp);
                sql.append(") ");

                if (baixa) {
                    sql.append("  AND     t2.DT_BAIXA_PARC_ADM >= '");
                    sql.append(fmt.format(d1));
                    sql.append(" 00:00:00' ");
                    sql.append(" AND     t2.DT_BAIXA_PARC_ADM <= '");
                    sql.append(fmt.format(d2));
                    sql.append(" 23:59:59' ");
                } else {
                    sql.append("  AND     t2.DT_PGTO_PARC_ADM >= '");
                    sql.append(fmt.format(d1));
                    sql.append(" 00:00:00' ");
                    sql.append(" AND     t2.DT_PGTO_PARC_ADM <= '");
                    sql.append(fmt.format(d2));
                    sql.append(" 23:59:59' ");
                }

                if (filtrarVencimento) {
                    sql.append(" AND t2.DT_VCTO_PARC_ADM >= '");
                    sql.append(fmt.format(d3));
                    sql.append(" 00:00:00' ");
                    sql.append(" AND t2.DT_VCTO_PARC_ADM <= '");
                    sql.append(fmt.format(d4));
                    sql.append(" 23:59:59' ");
                }


                if (!recebidoBanco) {
                    sql.append(" and t2.local_pgto_parc_adm <> 'B' ");
                }
                if (!recebidoCaixa) {
                    sql.append(" and t2.local_pgto_parc_adm <> 'C' ");
                }
                
                if (!bancoBB) {
                    sql.append(" and ISNULL(t4.BANCO_PGTO, '') <> 'BB' ");
                }
                if (!bancoItau) {
                    sql.append(" and ISNULL(t4.BANCO_PGTO, '') <> 'ITAU' ");
                }
                

                sql.append("group by t1.descr_tx_administrativa ");

                if (flagCurso) {
                    sql.append(" Union ");
                    sql.append("select      'Contribuições Individuais e Cursos', 'Cursos', ");
                    sql.append("            t1.descr_curso, ");
                    sql.append("            sum(t2.val_parc_cur) as val_devido, ");
                    sql.append("            sum(t2.val_desconto_parc_cur) as val_desconto, ");
                    sql.append("            sum(t2.val_multa_parc_cur) as val_multa, ");
                    sql.append("            sum(t2.val_juros_parc_cur) as val_juros, ");
                    sql.append("            sum(t2.val_encar_parc_cur) as val_encargo, ");
                    sql.append("            sum(t2.val_pgto_parc_cur) As val_pago ");
                    sql.append("From ");
                    sql.append("            tb_curso t1, ");
                    sql.append("            tb_parcela_curso t2, ");
                    sql.append("            tb_turma t3, ");
                    sql.append("            tb_categoria t4, ");
                    sql.append("            tb_carne t5 ");
                    sql.append("Where ");
                    sql.append("            t2.cd_sit_parc_cur = 'PG'  ");
                    sql.append(" and        t2.seq_turma = t3.seq_turma ");
                    sql.append(" and        t3.cd_curso = t1.cd_curso ");
                    sql.append(" and        t2.cd_categoria = t4.cd_categoria ");
                    sql.append(" and        t2.seq_carne = t5.seq_carne ");

                    if (baixa) {
                        sql.append("  and t2.DT_BAIXA_PARC_CUR >= '");
                        sql.append(fmt.format(d1));
                        sql.append(" 00:00:00' ");
                        sql.append(" AND     t2.DT_BAIXA_PARC_CUR <= '");
                        sql.append(fmt.format(d2));
                        sql.append(" 23:59:59' ");
                    } else {
                        sql.append("  and t2.DT_PGTO_PARC_CUR >= '");
                        sql.append(fmt.format(d1));
                        sql.append(" 00:00:00' ");
                        sql.append(" AND     t2.DT_PGTO_PARC_CUR <= '");
                        sql.append(fmt.format(d2));
                        sql.append(" 23:59:59' ");
                    }

                    if (filtrarVencimento) {
                        sql.append(" AND t2.DT_VCTO_PARC_CUR >= '");
                        sql.append(fmt.format(d3));
                        sql.append(" 00:00:00' ");
                        sql.append(" AND t2.DT_VCTO_PARC_CUR <= '");
                        sql.append(fmt.format(d4));
                        sql.append(" 23:59:59' ");
                    }

                    if (!recebidoBanco) {
                        sql.append(" and t2.local_pgto_parc_cur <> 'B' ");
                    }
                    if (!recebidoCaixa) {
                        sql.append(" and t2.local_pgto_parc_cur <> 'C' ");
                    }
                    
                    if (!bancoBB) {
                        sql.append(" and ISNULL(t5.BANCO_PGTO, '') <> 'BB' ");
                    }
                    if (!bancoItau) {
                        sql.append(" and ISNULL(t5.BANCO_PGTO, '') <> 'ITAU' ");
                    }

                    sql.append("Group By t1.descr_curso ");

                }//if flagCurso

                if (flagAvulso && recebidoBanco) {
                    sql.append(" Union ");
                    sql.append("SELECT      'Contribuições Individuais e Cursos', 'Boletos Avulsos', ");
                    sql.append("            DESCR_TX_ADMINISTRATIVA, ");
                    sql.append("            SUM(VR_BOLETO)                      VAL_DEVIDO, ");
                    sql.append("            SUM(ISNULL(T1.VR_DESCONTO, 0))      VAL_DESCONTO, ");
                    sql.append("            SUM(ISNULL(T1.VR_ENCARGOS, 0))      VAL_MULTA, ");
                    sql.append("            SUM(0)                              VAL_JUROS, ");
                    sql.append("            SUM(ISNULL(T1.VR_ENCARGOS, 0))      VAL_ENCARGO, ");
                    sql.append("            SUM(VR_PAGO)                        VAL_PAGO ");
                    sql.append("FROM ");
                    sql.append("             TB_BOLETO_AVULSO       T1, ");
                    sql.append("             TB_TAXA_ADMINISTRATIVA T2 ");
                    sql.append("WHERE ");
                    sql.append("             T1.CD_TX_ADMINISTRATIVA = T2.CD_TX_ADMINISTRATIVA AND ");
                    sql.append("             T1.CD_SITUACAO = 'PG' AND ");
                    
                    if (baixa) {
                        sql.append("      DT_BAIXA >= '");
                        sql.append(fmt.format(d1));
                        sql.append(" 00:00:00' ");
                        sql.append(" AND     DT_BAIXA <= '");
                        sql.append(fmt.format(d2));
                        sql.append(" 23:59:59' ");
                    } else {
                        sql.append("    DT_PAGAMENTO >= '");
                        sql.append(fmt.format(d1));
                        sql.append(" 00:00:00' ");
                        sql.append(" AND     DT_PAGAMENTO <= '");
                        sql.append(fmt.format(d2));
                        sql.append(" 23:59:59' ");
                    }

                    if (filtrarVencimento) {
                        sql.append(" AND DT_VENCIMENTO >= '");
                        sql.append(fmt.format(d3));
                        sql.append(" 00:00:00' ");
                        sql.append(" AND DT_VENCIMENTO <= '");
                        sql.append(fmt.format(d4));
                        sql.append(" 23:59:59' ");
                    }

                    sql.append("Group By DESCR_TX_ADMINISTRATIVA ");
                }//if flagAvulso

                sql.append(" order by 1,2,3");
            }

            String titulo = "Valores recebidos ";
            String titulo2 = null;
            if (recebidoBanco && recebidoCaixa) {
                titulo += "no Banco e no Clube";
            } else {
                if (recebidoBanco) {
                    titulo += "no Banco";
                } else {
                    titulo += "no Clube";
                }
            }

            if (recebidoBanco) {
                if(bancoBB){
                    if (bancoItau){
                        titulo += " (BB e ITAU)";
                    }else{
                        titulo += " (BB)";
                    }
                }else{
                    titulo += " (ITAU)";
                }
            }

            
            if (baixa) {
                titulo2 = "Período (Data de baixa)  "
                        + request.getParameter("dataInicio")
                        + " a "
                        + request.getParameter("dataFim");
            } else {
                titulo2 = "Período (Data de pagamento)  "
                        + request.getParameter("dataInicio")
                        + " a "
                        + request.getParameter("dataFim");
            }

            if (filtrarVencimento) {
                titulo2 += " e Data de Vencimento "
                        + request.getParameter("vencimentoInicio")
                        + " a "
                        + request.getParameter("vencimentoFim");
            }

            boolean quebra1 = true;
            boolean quebra2 = true;
            if (analitico || detalhado) {
                quebra1 = false;
                quebra2 = false;
            }

            request.setAttribute("titulo", titulo);
            request.setAttribute("titulo2", titulo2);
            request.setAttribute("sql", sql);
            request.setAttribute("quebra1", quebra1);
            request.setAttribute("quebra2", quebra2);
            request.setAttribute("total1", "4");
            request.setAttribute("total2", "5");
            request.setAttribute("total3", "6");
            request.setAttribute("total4", "7");
            request.setAttribute("total5", "8");
            request.setAttribute("total6", "9");

            request.getRequestDispatcher("/pages/listagem.jsp").forward(request, response);
        } else {
            request.setAttribute("taxas", ComboBoxLoader.listar("TB_TAXA_ADMINISTRATIVA"));
            request.getRequestDispatcher("/pages/1800.jsp").forward(request, response);
        }
    }

    @App("1850")
    public static void valoresRecebidosEscolinha(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String acao = request.getParameter("acao");

        if ("visualizar".equals(acao)) {
            StringBuilder sql = new StringBuilder();
            boolean sintetico = request.getParameter("tipo").equals("S");
            char tipo = request.getParameter("periodo").toString().charAt(0);
            SimpleDateFormat fmt = new SimpleDateFormat("yyyy-MM-dd");
            Date d1 = Datas.parse(request.getParameter("dataInicio"));
            Date d2 = Datas.parse(request.getParameter("dataFim"));
            boolean recebidoBanco = Boolean.parseBoolean(request.getParameter("recebidoBanco"));
            boolean recebidoCaixa = Boolean.parseBoolean(request.getParameter("recebidoCaixa"));
            boolean flagCurso = false;//escolinhas
            boolean flagNautica = false;//nautica

            sql.append("SELECT ");
            sql.append("T1.DESCR_CATEGORIA        as Categoria, ");
            sql.append("T2.DESCR_CURSO            as Curso, ");

            if (sintetico) {
                sql.append("ISNULL(SUM(T4.VAL_PARC_CUR), 0)           as Valor, ");
                sql.append("ISNULL(SUM(T4.VAL_DESCONTO_PARC_CUR), 0)  as Desconto, ");
                sql.append("ISNULL(SUM(T4.VAL_ENCAR_PARC_CUR), 0)     as Encargo, ");
                sql.append("ISNULL(SUM(T4.VAL_PGTO_PARC_CUR), 0)      as Total ");
            } else {
                sql.append("T3.NOME_PESSOA                       as Aluno, ");
                sql.append("ISNULL(T4.VAL_PARC_CUR, 0)           as Valor, ");
                sql.append("ISNULL(T4.VAL_DESCONTO_PARC_CUR, 0)  as Desconto, ");
                sql.append("ISNULL(T4.VAL_ENCAR_PARC_CUR, 0)     as Encargo, ");
                sql.append("ISNULL(T4.VAL_PGTO_PARC_CUR, 0)      as Total ");
            }

            sql.append("FROM ");
            sql.append("TB_CATEGORIA        T1, ");
            sql.append("TB_CURSO            T2, ");
            sql.append("TB_PESSOA           T3, ");
            sql.append("TB_PARCELA_CURSO    T4, ");
            sql.append("TB_TURMA            T5, ");
            sql.append("TB_MATRICULA_CURSO  T6 ");

            sql.append("WHERE ");
            sql.append("T1.CD_CATEGORIA     = T3.CD_CATEGORIA       AND ");
            sql.append("T2.CD_CURSO         = T5.CD_CURSO           AND ");
            sql.append("T5.SEQ_TURMA        = T6.SEQ_TURMA          AND ");
            sql.append("T4.SEQ_TURMA        = T6.SEQ_TURMA          AND ");
            sql.append("T4.CD_MATRICULA     = T6.CD_MATRICULA       AND ");
            sql.append("T4.CD_CATEGORIA     = T6.CD_CATEGORIA       AND ");
            sql.append("T4.SEQ_DEPENDENTE   = T6.SEQ_DEPENDENTE     AND ");
            sql.append("T3.CD_MATRICULA     = T6.CD_MATRICULA       AND ");
            sql.append("T3.CD_CATEGORIA     = T6.CD_CATEGORIA       AND ");
            sql.append("T3.SEQ_DEPENDENTE   = T6.SEQ_DEPENDENTE     AND ");
            sql.append("T4.CD_SIT_PARC_CUR  = 'PG'                  AND ");

            String periodo = null;
            if (tipo == 'B') {
                sql.append("T4.DT_BAIXA_PARC_CUR >= '");
                sql.append(fmt.format(d1));
                sql.append(" 00:00:00' ");
                sql.append(" AND T4.DT_BAIXA_PARC_CUR <= '");
                sql.append(fmt.format(d2));
                sql.append(" 23:59:59' AND ");

                periodo = "Data de Baixa de "
                        + request.getParameter("dataInicio")
                        + " a "
                        + request.getParameter("dataFim");
            } else if (tipo == 'P') {
                sql.append("T4.DT_PGTO_PARC_CUR >= '");
                sql.append(fmt.format(d1));
                sql.append(" 00:00:00' ");
                sql.append(" AND T4.DT_PGTO_PARC_CUR <= '");
                sql.append(fmt.format(d2));
                sql.append(" 23:59:59' AND ");

                periodo = "Data de Pagamento de "
                        + request.getParameter("dataInicio")
                        + " a "
                        + request.getParameter("dataFim");
            } else {
                sql.append("T4.DT_VCTO_PARC_CUR >= '");
                sql.append(fmt.format(d1));
                sql.append(" 00:00:00' ");
                sql.append(" AND T4.DT_VCTO_PARC_CUR <= '");
                sql.append(fmt.format(d2));
                sql.append(" 23:59:59' AND ");

                periodo = "Data de Vencimento de "
                        + request.getParameter("dataInicio")
                        + " a "
                        + request.getParameter("dataFim");
            }

            if (!recebidoBanco) {
                sql.append(" T4.LOCAL_PGTO_PARC_CUR <> 'B' AND ");
            }
            if (!recebidoCaixa) {
                sql.append(" T4.LOCAL_PGTO_PARC_CUR <> 'C' AND ");
            }

            sql.append("T1.CD_CATEGORIA IN (");
            for (String s : request.getParameterValues("categorias")) {
                sql.append(s);
                sql.append(", ");
            }
            sql.delete(sql.length() - 2, sql.length());

            sql.append(") AND ");
            sql.append("T2.CD_CURSO IN (");
            for (String s : request.getParameterValues("cursos")) {
                sql.append(s);
                sql.append(", ");
            }
            sql.delete(sql.length() - 2, sql.length());
            sql.append(") ");

            int inicio = 0;
            if (sintetico) {
                sql.append("GROUP BY ");
                sql.append("T1.DESCR_CATEGORIA  , ");
                sql.append("T2.DESCR_CURSO        ");
                sql.append("ORDER BY 1, 2 ");
                inicio = 2;
            } else {
                sql.append("ORDER BY 1, 2, 3 ");
                inicio = 3;
            }

            request.setAttribute("titulo", "Valores de Escolinhas");
            request.setAttribute("titulo2", periodo);
            request.setAttribute("sql", sql);
            request.setAttribute("quebra1", "true");
            request.setAttribute("quebra2", !sintetico);
            request.setAttribute("total1", inicio + 1);
            request.setAttribute("total2", inicio + 2);
            request.setAttribute("total3", inicio + 3);
            request.setAttribute("total4", inicio + 4);

            request.getRequestDispatcher("/pages/listagem.jsp").forward(request, response);
        } else {
            request.setAttribute("cursos", ComboBoxLoader.listar("VW_CURSOS"));
            request.setAttribute("categorias", ComboBoxLoader.listar("TB_CATEGORIA"));
            request.getRequestDispatcher("/pages/1850.jsp").forward(request, response);
        }
    }

    @App("1840")
    public static void demonstrativoArrecadacao(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String acao = request.getParameter("acao");

        if ("visualizar".equals(acao)) {
            StringBuilder sql = new StringBuilder();
            boolean analitico = request.getParameter("tipo").equals("A");
            SimpleDateFormat fmt = new SimpleDateFormat("yyyy-MM-dd");
            Date iniVenc = Datas.parse(request.getParameter("iniVenc"));
            Date fimVenc = Datas.parse(request.getParameter("fimVenc"));
            boolean incluirCursos = Boolean.parseBoolean(request.getParameter("incluirCursos"));
            boolean incluirExcluidos = Boolean.parseBoolean(request.getParameter("incluirExcluidos"));

            sql.append("EXEC SP_REL_DEM_ARRECADACAO '");
            sql.append(fmt.format(iniVenc));
            sql.append("', '");
            sql.append(fmt.format(fimVenc));
            if (incluirCursos) {
                sql.append("', 'S', '");
            } else {
                sql.append("', 'N', '");
            }
            sql.append(request.getParameter("ordenacao").toString().charAt(0));
            sql.append("', '");
            if (analitico) {
                sql.append("A");
            } else {
                sql.append("S");
            }
            if (incluirExcluidos) {
                sql.append("', 'S'");
            } else {
                sql.append("', 'N'");
            }

            request.setAttribute("titulo", "Demonstrativo de Arrecadação ");
            request.setAttribute("titulo2", "Período:  " + request.getParameter("iniVenc") + " a " + request.getParameter("fimVenc"));
            request.setAttribute("sql", sql);
            request.setAttribute("quebra1", analitico);
            request.setAttribute("quebra2", "false");
            request.setAttribute("total1", -1);
            request.setAttribute("total2", -1);
            if (analitico) {
                request.setAttribute("total3", 3);
                request.setAttribute("total4", 4);
            } else {
                request.setAttribute("total3", 2);
                request.setAttribute("total4", 3);
            }

            request.getRequestDispatcher("/pages/listagem.jsp").forward(request, response);
        } else {
            request.getRequestDispatcher("/pages/1840.jsp").forward(request, response);
        }
    }

    @App("2460")
    public static void saldoCreditoPessoa(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String acao = request.getParameter("acao");

        if ("visualizar".equals(acao)) {
            StringBuilder sql = new StringBuilder();
            SimpleDateFormat fmt = new SimpleDateFormat("yyyy-MM-dd");
            Date iniVenc = Datas.parse(request.getParameter("iniVenc"));
            Date fimVenc = Datas.parse(request.getParameter("fimVenc"));
            Date iniBaixa = Datas.parse(request.getParameter("iniBaixa"));
            Date fimBaixa = Datas.parse(request.getParameter("fimBaixa"));
            Date iniPgto = Datas.parse(request.getParameter("iniPgto"));
            Date fimPgto = Datas.parse(request.getParameter("fimPgto"));

            sql.append("EXEC SP_REL_SALDO_CREDITO_PESSOA '");
            sql.append(fmt.format(iniVenc));
            sql.append("', '");
            sql.append(fmt.format(fimVenc));
            sql.append("', ");

            if (iniBaixa != null) {
                sql.append("'");
                sql.append(fmt.format(iniBaixa));
                sql.append(" 00:00:00', '");
                sql.append(fmt.format(fimBaixa));
                sql.append(" 23:59:59', ");
            }else{
                sql.append("NULL, NULL, ");
            }
            
            if (iniPgto != null) {
                sql.append("'");
                sql.append(fmt.format(iniPgto));
                sql.append(" 00:00:00', '");
                sql.append(fmt.format(fimPgto));
                sql.append(" 23:59:59'");
            }else{
                sql.append("NULL, NULL");
            }
            
            request.setAttribute("titulo", "Saldo de Crédito");
            request.setAttribute("titulo2", "Período:  " + request.getParameter("iniVenc") + " a " + request.getParameter("fimVenc"));
            request.setAttribute("sql", sql);
            request.setAttribute("quebra1", "false");
            request.setAttribute("quebra2", "false");
            request.setAttribute("total1", -1);
            request.setAttribute("total2", 4);
            request.setAttribute("total3", 5);
            request.setAttribute("total4", 6);

            request.getRequestDispatcher("/pages/listagem.jsp").forward(request, response);
        } else {
            request.getRequestDispatcher("/pages/2460.jsp").forward(request, response);
        }
    }
    
    
    @App("1830")
    public static void ocorrenciaBaixaAutomatica(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String acao = request.getParameter("acao");

        if ("visualizar".equals(acao)) {
            StringBuilder sql = new StringBuilder();
            char periodo = request.getParameter("periodo").toString().charAt(0);
            SimpleDateFormat fmt = new SimpleDateFormat("yyyy-MM-dd");
            Date d1 = Datas.parse(request.getParameter("dataInicio"));
            Date d2 = Datas.parse(request.getParameter("dataFim"));

            sql.append("select ");
            sql.append("BANCO_ORIGEM as Banco, ");
            sql.append("MSG_ERRO as Mensagem, ");
            sql.append("CD_CATEGORIA as Cat, ");
            sql.append("CD_MATRICULA as Mat, ");
            sql.append("SEQ_CARNE as Carne, ");
            sql.append("DT_VENC as 'Dt. Venc', ");
            sql.append("DT_PGTO as 'Dt. Pagto', ");
            sql.append("DT_MOVIMENTO as 'Dt. Mov', ");
            sql.append("VAL_DEVIDO as 'Vr. Devido', ");
            sql.append("VAL_ENCARGO as 'Vr. Encargo', ");
            sql.append("VAL_PAGO as 'Vr. Pago'");
            sql.append("from ");
            sql.append("tb_erro_baixa_carne ");
            sql.append("WHERE ");

            String titulo2 = null;
            if (periodo == 'M') {
                sql.append("dt_movimento >= '");
                sql.append(fmt.format(d1));
                sql.append(" 00:00:00' ");
                sql.append(" AND dt_movimento <= '");
                sql.append(fmt.format(d2));
                sql.append(" 23:59:59'");
                titulo2 = "Dt. Movimento: ";
            } else if (periodo == 'P') {
                sql.append("dt_pgto >= '");
                sql.append(fmt.format(d1));
                sql.append(" 00:00:00' ");
                sql.append(" AND dt_pgto <= '");
                sql.append(fmt.format(d2));
                sql.append(" 23:59:59'");
                titulo2 = "Dt. Pagamento: ";
            } else {
                sql.append("dt_venc >= '");
                sql.append(fmt.format(d1));
                sql.append(" 00:00:00' ");
                sql.append(" AND dt_venc <= '");
                sql.append(fmt.format(d2));
                sql.append(" 23:59:59'");
                titulo2 = "Dt. Vencimento: ";
            }

            sql.append("Order by 1, 2");

            request.setAttribute("titulo", "Relatório de Ocorrências Baixa Automática");
            request.setAttribute("titulo2", titulo2 + request.getParameter("dataInicio") + " a " + request.getParameter("dataFim"));
            request.setAttribute("sql", sql);
            request.setAttribute("quebra1", "true");
            request.setAttribute("quebra2", "true");
            request.setAttribute("total1", 9);
            request.setAttribute("total2", 10);
            request.setAttribute("total3", 11);
            request.setAttribute("total4", -1);

            request.getRequestDispatcher("/pages/listagem.jsp").forward(request, response);
        } else {
            request.getRequestDispatcher("/pages/1830.jsp").forward(request, response);
        }
    }

    @App("1752")
    public static void estornoManual(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String acao = request.getParameter("acao");

        if ("visualizar".equals(acao)) {
            StringBuilder sql = new StringBuilder();
            char periodo = request.getParameter("periodo").toString().charAt(0);
            SimpleDateFormat fmt = new SimpleDateFormat("yyyy-MM-dd");
            Date d1 = Datas.parse(request.getParameter("dataInicio"));
            Date d2 = Datas.parse(request.getParameter("dataFim"));

            sql.append("SELECT ");
            sql.append("T1.SEQ_CARNE AS 'SEQ. CARNE', ");
            sql.append("RIGHT('00' + cast(T1.cd_categoria as varchar), 2) + '/' + RIGHT('0000' + cast(T1.cd_matricula as varchar), 4) AS 'Cat./Matricula', ");
            sql.append("T1.DT_VENC_CARNE AS 'DT VENC', ");
            sql.append("T2.NOME_PESSOA AS NOME, ");
            sql.append("DT_PGTO_CARNE AS 'DT PGTO', ");
            sql.append("VAL_CARNE AS 'VAL DEVIDO', ");
            sql.append("VAL_DESC_CARNE AS 'VAL DESC', ");
            sql.append("VAL_ENCAR_CARNE AS 'VAL ENCAR', ");
            sql.append("VAL_PGTO_CARNE AS 'VAL PGTO', ");
            sql.append("DT_ESTORNO_CARNE AS 'DT ESTORNO', ");
            sql.append("USER_FUNCIONARIO AS 'FUNCIONÁRIO', ");
            sql.append("DE_MOTIVO_ESTORNO AS 'MOTIVO'");
            sql.append("FROM ");
            sql.append("TB_CARNE_ESTORNADO T1, ");
            sql.append("TB_PESSOA T2 ");
            sql.append("WHERE ");
            sql.append("T1.CD_MATRICULA = T2.CD_MATRICULA AND ");
            sql.append("T1.CD_CATEGORIA = T2.CD_CATEGORIA AND ");
            sql.append("T2.SEQ_DEPENDENTE = 0 AND ");

            String titulo2 = null;
            if (periodo == 'V') {
                sql.append(" T1.DT_VENC_cARNE >= '");
                sql.append(fmt.format(d1));
                sql.append(" 00:00:00' ");
                sql.append(" AND  T1.DT_VENC_cARNE <= '");
                sql.append(fmt.format(d2));
                sql.append(" 23:59:59'");
                titulo2 = "Dt. Vencimento: ";
            } else {
                sql.append(" T1.DT_ESTORNO_cARNE >= '");
                sql.append(fmt.format(d1));
                sql.append(" 00:00:00' ");
                sql.append(" AND T1.DT_ESTORNO_cARNE <= '");
                sql.append(fmt.format(d2));
                sql.append(" 23:59:59'");
                titulo2 = "Dt. Estorno: ";
            }

            request.setAttribute("titulo", "Relatório de Estorno Manual");
            request.setAttribute("titulo2", titulo2 + request.getParameter("dataInicio") + " a " + request.getParameter("dataFim"));
            request.setAttribute("sql", sql);
            request.setAttribute("quebra1", "false");
            request.setAttribute("quebra2", "false");
            request.setAttribute("total1", -1);
            request.setAttribute("total2", -1);
            request.setAttribute("total3", -1);
            request.setAttribute("total4", -1);

            request.getRequestDispatcher("/pages/listagem.jsp").forward(request, response);
        } else {
            request.getRequestDispatcher("/pages/1752.jsp").forward(request, response);
        }
    }

    @App("1470")
    public static void inadimplencia(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String acao = request.getParameter("acao");

        if ("visualizar".equals(acao)) {
            StringBuilder sql = new StringBuilder();
            Date d1 = Datas.parse(request.getParameter("dataInicio"));
            Date d2 = Datas.parse(request.getParameter("dataFim"));
            request.setAttribute("dataInicio", d1);
            request.setAttribute("dataFim", d2);

            sql.append("SELECT ");
            sql.append("DESCR_CATEGORIA, ");
            sql.append("VAL_DEVIDO, ");
            sql.append("PERC_DEVIDO, ");
            sql.append("VAL_NP, ");
            sql.append("PERC_NP, ");
            sql.append("VAL_ENCARGO, ");
            sql.append("PERC_ENCARGO, ");
            sql.append("VAL_TOTAL, ");
            sql.append("PERC_TOTAL, ");
            sql.append("PERC_INAD ");
            sql.append("FROM ");
            sql.append("TB_INADIMPLENCIA ");
            sql.append("WHERE ");
            sql.append("CD_CATEGORIA IN (");
            for (String s : request.getParameterValues("categorias")) {
                sql.append(s);
                sql.append(", ");
            }
            sql.delete(sql.length() - 2, sql.length());
            sql.append(") ORDER BY 2");

            request.setAttribute("titulo", "Relatório de Inadimplência");
            request.setAttribute("titulo2", request.getParameter("dataInicio") + " a " + request.getParameter("dataFim"));
            request.setAttribute("sql", sql);
            request.getRequestDispatcher("/pages/1470-impressao.jsp").forward(request, response);
        } else {
            request.setAttribute("categorias", ComboBoxLoader.listar("TB_CATEGORIA"));
            request.getRequestDispatcher("/pages/1470.jsp").forward(request, response);
        }
    }

    @App("1510")
    public static void taxas(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String acao = request.getParameter("acao");

        if ("visualizar".equals(acao)) {
            StringBuilder sql = new StringBuilder();
            SimpleDateFormat fmt = new SimpleDateFormat("yyyy-MM-dd");
            Date vigencia = Datas.parse(request.getParameter("vigencia"));
            char tipo = request.getParameter("tipo").toString().charAt(0);
            String titulo = null;

            if (tipo == 'A') {
                sql.append("SELECT ");
                sql.append("T1.DESCR_CATEGORIA            AS Categoria      , ");
                sql.append("T2.DESCR_TX_ADMINISTRATIVA    AS Taxa        , ");
                sql.append("T3.DT_VALID_INIC_TX_ADM       AS 'Dt. Inicio Vigencia' , ");
                sql.append("T3.DT_VALID_FIM_TX_ADM        AS 'Dt. Fim Vigencia' , ");
                sql.append("T3.VAL_TX_ADM           AS Valor ");
                sql.append("FROM ");
                sql.append("TB_CATEGORIA              T1, ");
                sql.append("TB_TAXA_ADMINISTRATIVA    T2, ");
                sql.append("TB_VAL_TX_ADMINISTRATIVA  T3 ");
                sql.append("WHERE ");
                sql.append("T1.CD_CATEGORIA      = T3.CD_CATEGORIA AND ");
                sql.append("T2.CD_TX_ADMINISTRATIVA = T3.CD_TX_ADM    AND ");
                sql.append("T3.DT_VALID_INIC_TX_ADM <= '");
                sql.append(fmt.format(vigencia));
                sql.append("' AND ");
                sql.append("(T3.DT_VALID_FIM_TX_ADM >= '");
                sql.append(fmt.format(vigencia));
                sql.append("' OR T3.DT_VALID_FIM_TX_ADM IS NULL) ");
                sql.append("ORDER BY 1, 2");

                titulo = "Relatório de Taxas Administrativas";
            } else if (tipo == 'C') {
                sql.append("SELECT ");
                sql.append("T1.DESCR_CATEGORIA         AS Categoria, ");
                sql.append("T2.DESCR_CURSO             AS Curso, ");
                sql.append("T3.DT_VALID_INIC_TX_CURSO  AS 'Dt. Inicio Vigencia', ");
                sql.append("T3.DT_VALID_FIM_TX_CURSO   AS 'Dt. Fim Vigencia', ");
                sql.append("T3.VAL_TX_CURSO            AS Valor ");
                sql.append("FROM ");
                sql.append("TB_CATEGORIA     T1, ");
                sql.append("TB_CURSO         T2, ");
                sql.append("TB_VAL_TX_CURSO  T3 ");
                sql.append("WHERE ");
                sql.append("T1.CD_CATEGORIA           = T3.CD_CATEGORIA AND ");
                sql.append("T2.CD_CURSO               =  T3.CD_CURSO    AND ");
                sql.append("T3.DT_VALID_INIC_TX_CURSO <= '");
                sql.append(fmt.format(vigencia));
                sql.append("' AND ");
                sql.append("(T3.DT_VALID_FIM_TX_CURSO >= '");
                sql.append(fmt.format(vigencia));
                sql.append("' OR T3.DT_VALID_FIM_TX_CURSO IS NULL) ");
                sql.append("ORDER BY 1, 2");

                titulo = "Relatório de Taxas de Cursos";
            } else {
                sql.append("SELECT ");
                sql.append("CASE TP_VAGA_TX_BARCO ");
                sql.append("WHEN 'CO' THEN 'Coberta' ");
                sql.append("WHEN 'FC' THEN 'Fora Clube' ");
                sql.append("WHEN 'OU' THEN 'Outra' ");
                sql.append("WHEN 'PA' THEN 'Pátio' ");
                sql.append("WHEN 'PI' THEN 'Piscina' ");
                sql.append("WHEN 'PR' THEN 'Prateleira' ");
                sql.append("END AS 'Tipo de Vaga', ");
                sql.append("CONVERT(VARCHAR(20),CONVERT(VARCHAR, NR_PES_INIC_TX_BARCO) + ' até ' + CONVERT(VARCHAR, NR_PES_FIM_TX_BARCO)) AS 'Nr. Pés', ");
                sql.append("DT_INIC_VALID_TX_BARCO  AS 'Dt. Inicio Vigencia', ");
                sql.append("DT_FIM_VALID_TX_BARCO   AS 'Dt. Fim Vigencia', ");
                sql.append("VAL_TX_BARCO      AS 'Valor' ");
                sql.append("FROM ");
                sql.append("TB_TAXA_BARCO ");
                sql.append("WHERE ");
                sql.append("DT_INIC_VALID_TX_BARCO  <=  '");
                sql.append(fmt.format(vigencia));
                sql.append("' AND ");
                sql.append("(DT_FIM_VALID_TX_BARCO  >= '");
                sql.append(fmt.format(vigencia));
                sql.append("' OR DT_FIM_VALID_TX_BARCO  IS NULL) ");
                sql.append("ORDER BY 1, 2 ");

                titulo = "Relatório de Taxas Naúticas";
            }

            request.setAttribute("titulo", titulo);
            request.setAttribute("titulo2", "Vigência em:  " + request.getParameter("vigencia"));
            request.setAttribute("sql", sql);
            request.setAttribute("quebra1", "true");
            request.setAttribute("quebra2", "false");
            request.setAttribute("total1", -1);
            request.setAttribute("total2", -1);
            request.setAttribute("total3", -1);
            request.setAttribute("total4", -1);

            request.getRequestDispatcher("/pages/listagem.jsp").forward(request, response);
        } else {
            request.getRequestDispatcher("/pages/1510.jsp").forward(request, response);
        }
    }

    @App("1620")
    public static void titulosNaoGeramTaxas(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String acao = request.getParameter("acao");

        if ("visualizar".equals(acao)) {
            StringBuilder sql = new StringBuilder();
            int tituloInicio = Integer.parseInt(request.getParameter("tituloInicio"));
            int tituloFim = 9999;
            try {
                tituloFim = Integer.parseInt(request.getParameter("tituloFim"));
            } catch (NumberFormatException e) {
                tituloFim = 9999;
            }
            sql.append("EXEC SP_REL_NAO_GERA_TAXA ");
            sql.append(tituloInicio);
            sql.append(", ");
            sql.append(tituloFim);

            request.setAttribute("titulo", "Títulos que não geram Taxas");
            request.setAttribute("titulo2", "Título " + tituloInicio + " a " + tituloFim);
            request.setAttribute("sql", sql);
            request.setAttribute("quebra1", "true");
            request.setAttribute("quebra2", "false");
            request.setAttribute("total1", -1);
            request.setAttribute("total2", -1);
            request.setAttribute("total3", -1);
            request.setAttribute("total4", -1);

            request.getRequestDispatcher("/pages/listagem.jsp").forward(request, response);
        } else {
            request.getRequestDispatcher("/pages/1620.jsp").forward(request, response);
        }
    }

    @App("2020")
    public static void carnesParcelas(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String acao = request.getParameter("acao");

        if ("visualizar".equals(acao)) {
            StringBuilder sql = new StringBuilder();
            int titulo = 0;

            try {
                titulo = Integer.parseInt(request.getParameter("titulo"));
            } catch (NumberFormatException e) {
                titulo = 0;
            }
            SimpleDateFormat fmt = new SimpleDateFormat("yyyy-MM-dd");
            Date d1 = Datas.parse(request.getParameter("vencimentoInicio"));
            Date d2 = Datas.parse(request.getParameter("vencimentoFim"));
            Date d3 = Datas.parse(request.getParameter("pagamentoInicio"));
            Date d4 = Datas.parse(request.getParameter("pagamentoFim"));

            boolean pago = Boolean.parseBoolean(request.getParameter("situacaoPaga"));
            boolean naoPago = Boolean.parseBoolean(request.getParameter("situacaoNaoPaga"));
            boolean clube = Boolean.parseBoolean(request.getParameter("clube"));
            boolean banco = Boolean.parseBoolean(request.getParameter("banco"));

            StringBuilder categorias = new StringBuilder();
            for (String s : request.getParameterValues("categorias")) {
                categorias.append(s);
                categorias.append(", ");
            }
            categorias.delete(categorias.length() - 2, categorias.length());

            if (request.getParameterValues("taxas") != null) {
                sql.append("SELECT ");
                sql.append("RIGHT('00' + CONVERT(VARCHAR, T1.CD_CATEGORIA), 2) + '/' + ");
                sql.append("RIGHT('0000' + CONVERT(VARCHAR, T1.CD_MATRICULA), 4) + '-' + ");
                sql.append("T2.NOME_PESSOA AS 'Socio', ");
                sql.append("T1.DT_VCTO_PARC_ADM AS 'Dt. Venc.', ");
                sql.append("T3.DESCR_TX_ADMINISTRATIVA as 'Taxa/Curso', ");
                sql.append("T1.VAL_PARC_ADM as 'Vr. Devido', ");
                sql.append("T1.CD_SIT_PARC_ADM as 'Sit.'");
                sql.append("From ");
                sql.append("TB_PARCELA_ADMINISTRATIVA  T1, ");
                sql.append("TB_PESSOA         T2, ");
                sql.append("TB_TAXA_ADMINISTRATIVA T3 ");
                sql.append("Where ");
                sql.append("T1.CD_MATRICULA = T2.CD_MATRICULA   AND ");
                sql.append("T1.CD_CATEGORIA = T2.CD_CATEGORIA   AND ");
                sql.append("T1.SEQ_DEPENDENTE = T2.SEQ_DEPENDENTE  AND ");
                sql.append("T1.CD_TX_ADMINISTRATIVA = T3.CD_TX_ADMINISTRATIVA ");

                sql.append(" AND T1.DT_VCTO_PARC_ADM BETWEEN '");
                sql.append(fmt.format(d1));
                sql.append("' AND '");
                sql.append(fmt.format(d2));
                sql.append("'");

                sql.append(" AND T1.CD_CATEGORIA IN (");
                sql.append(categorias);
                sql.append(") ");

                sql.append(" AND T1.CD_TX_ADMINISTRATIVA IN (");
                for (String s : request.getParameterValues("taxas")) {
                    sql.append(s);
                    sql.append(", ");
                }
                sql.delete(sql.length() - 2, sql.length());
                sql.append(") ");

                if (titulo > 0) {
                    sql.append(" AND T1.CD_MATRICULA = ");
                    sql.append(titulo);
                }

                if (pago) {
                    if (naoPago) {
                        sql.append(" AND T1.CD_SIT_PARC_ADM IN ('PG', 'NP') ");
                    } else {
                        sql.append(" AND T1.CD_SIT_PARC_ADM = 'PG' ");
                    }
                } else {
                    sql.append(" AND T1.CD_SIT_PARC_ADM = 'NP' ");
                }

                if (pago) {
                    if (clube) {
                        if (banco) {
                            sql.append(" AND (T1.local_pgto_parc_adm IN ('C', 'B')");
                        } else {
                            sql.append(" AND (T1.local_pgto_parc_adm = 'C'");
                        }
                    } else {
                        sql.append(" AND (T1.local_pgto_parc_adm = 'B'");
                    }
                    if (naoPago) {
                        sql.append(" OR T1.local_pgto_parc_adm IS NULL) ");
                    } else {
                        sql.append(") ");
                    }
                }

                if (d3 != null) {
                    sql.append(" AND T1.DT_PGTO_PARC_ADM BETWEEN '");
                    sql.append(fmt.format(d3));
                    sql.append("' AND '");
                    sql.append(fmt.format(d4));
                    sql.append("'");
                }
            }

            if (request.getParameterValues("cursos") != null) {
                if (request.getParameterValues("taxas") != null) {
                    sql.append(" UNION ALL ");
                }

                sql.append("SELECT ");
                sql.append("RIGHT('00' + CONVERT(VARCHAR, T1.CD_CATEGORIA), 2) + '/' + ");
                sql.append("RIGHT('0000' + CONVERT(VARCHAR, T1.CD_MATRICULA), 4) + '-' + ");
                sql.append("T2.NOME_PESSOA, ");
                sql.append("T1.DT_VCTO_PARC_CUR, ");
                sql.append("T4.DESCR_CURSO, ");
                sql.append("T1.VAL_PARC_CUR, ");
                sql.append("T1.CD_SIT_PARC_CUR ");
                sql.append("From ");
                sql.append("TB_PARCELA_CURSO T1, ");
                sql.append("TB_PESSOA         T2, ");
                sql.append("TB_TURMA T3, ");
                sql.append("TB_CURSO T4 ");
                sql.append("Where ");
                sql.append("T1.CD_MATRICULA = T2.CD_MATRICULA   AND ");
                sql.append("T1.CD_CATEGORIA = T2.CD_CATEGORIA   AND ");
                sql.append("T1.SEQ_DEPENDENTE = T2.SEQ_DEPENDENTE  AND ");
                sql.append("T1.SEQ_TURMA = T3.SEQ_TURMA AND ");
                sql.append("T3.CD_CURSO = T4.CD_CURSO ");

                sql.append(" AND T1.DT_VCTO_PARC_CUR BETWEEN '");
                sql.append(fmt.format(d1));
                sql.append("' AND '");
                sql.append(fmt.format(d2));
                sql.append("'");

                sql.append(" AND T1.CD_CATEGORIA IN (");
                sql.append(categorias);
                sql.append(") ");

                sql.append(" AND T3.CD_CURSO IN (");
                for (String s : request.getParameterValues("cursos")) {
                    sql.append(s);
                    sql.append(", ");
                }
                sql.delete(sql.length() - 2, sql.length());
                sql.append(") ");

                if (titulo > 0) {
                    sql.append(" AND T1.CD_MATRICULA = ");
                    sql.append(titulo);
                }

                if (pago) {
                    if (naoPago) {
                        sql.append(" AND T1.CD_SIT_PARC_CUR IN ('PG', 'NP') ");
                    } else {
                        sql.append(" AND T1.CD_SIT_PARC_CUR = 'PG' ");
                    }
                } else {
                    sql.append(" AND T1.CD_SIT_PARC_CUR = 'NP' ");
                }

                if (pago) {
                    if (clube) {
                        if (banco) {
                            sql.append(" AND (T1.LOCAL_PGTO_PARC_CUR IN ('C', 'B')");
                        } else {
                            sql.append(" AND (T1.LOCAL_PGTO_PARC_CUR = 'C'");
                        }
                    } else {
                        sql.append(" AND (T1.LOCAL_PGTO_PARC_CUR = 'B'");
                    }
                    if (naoPago) {
                        sql.append(" OR T1.LOCAL_PGTO_PARC_CUR IS NULL) ");
                    } else {
                        sql.append(") ");
                    }
                }

                if (d3 != null) {
                    sql.append(" AND T1.DT_PGTO_PARC_CUR BETWEEN '");
                    sql.append(fmt.format(d3));
                    sql.append("' AND '");
                    sql.append(fmt.format(d4));
                    sql.append("'");
                }
            }

            sql.append(" ORDER BY 1, 2, 3, 4, 5");
            request.setAttribute("titulo", "Relatório de Parcelas Administrativas/Cursos");
            request.setAttribute("titulo2", "");
            request.setAttribute("sql", sql);
            request.setAttribute("quebra1", "false");
            request.setAttribute("quebra2", "false");
            request.setAttribute("total1", 4);
            request.setAttribute("total2", -1);
            request.setAttribute("total3", -1);
            request.setAttribute("total4", -1);

            request.getRequestDispatcher("/pages/listagem.jsp").forward(request, response);
        } else {
            request.setAttribute("categorias", ComboBoxLoader.listar("TB_CATEGORIA"));
            request.setAttribute("taxas", ComboBoxLoader.listar("TB_TAXA_ADMINISTRATIVA"));
            request.setAttribute("cursos", ComboBoxLoader.listar("VW_CURSOS"));
            request.getRequestDispatcher("/pages/2000.jsp").forward(request, response);
        }
    }

    @App("2290")
    public static void declaracaoQuitacaoBanco(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String acao = request.getParameter("acao");

        if ("visualizar".equals(acao)) {
            StringBuilder sql = new StringBuilder();
            SimpleDateFormat fmt = new SimpleDateFormat("yyyy-MM-dd");
            Date d1 = Datas.parse(request.getParameter("dataInicio"));
            Date d2 = Datas.parse(request.getParameter("dataFim"));

            sql.append("SELECT ");
            sql.append("T1.CD_MATRICULA AS Título, ");
            sql.append("T1.CD_CATEGORIA AS Categoria, ");
            sql.append("T2.NOME_PESSOA AS Sócio, ");
            sql.append("CONVERT(VARCHAR(10), T1.DT_ENVIO, 103) AS 'Dt. Envio', ");
            sql.append("CONVERT(VARCHAR(15), T1.DE_ARQUIVO) AS Arquivo, ");
            sql.append("T1.AA_REFERENCIA AS 'Ano Ref.', ");
            sql.append("T1.TX_DECLARACAO AS Declaração ");
            sql.append("FROM ");
            sql.append("TB_DECLARACAO_QUITACAO T1, ");
            sql.append("TB_PESSOA T2 ");
            sql.append("WHERE ");
            sql.append("T1.CD_MATRICULA = T2.CD_MATRICULA AND ");
            sql.append("T1.CD_CATEGORIA = T2.CD_CATEGORIA AND ");
            sql.append("T2.SEQ_DEPENDENTE = 0 ");

            sql.append("AND DT_ENVIO >= '");
            sql.append(fmt.format(d1));
            sql.append("' AND DT_ENVIO <= '");
            sql.append(fmt.format(d2));
            sql.append(" 23:59:59'");

            String nome = request.getParameter("nome");
            if (nome != null && !nome.trim().equals("")) {
                sql.append(" AND T2.NOME_PESSOA LIKE '");
                sql.append(nome.trim().toUpperCase());
                sql.append("%'");
            }

            int matricula = 0;
            int categoria = 0;
            int anoRef = 0;
            try {
                matricula = Integer.parseInt(request.getParameter("matricula"));
            } catch (NumberFormatException e) {
                matricula = 0;
            }

            try {
                categoria = Integer.parseInt(request.getParameter("categoria"));
            } catch (NumberFormatException e) {
                categoria = 0;
            }

            try {
                anoRef = Integer.parseInt(request.getParameter("anoRef"));
            } catch (NumberFormatException e) {
                anoRef = 0;
            }

            if (matricula > 0) {
                sql.append(" AND T1.CD_MATRICULA = ");
                sql.append(matricula);
            }

            if (categoria > 0) {
                sql.append(" AND T1.CD_CATEGORIA = ");
                sql.append(categoria);
            }

            String arquivo = request.getParameter("arquivo");
            if (arquivo != null && !arquivo.trim().equals("")) {
                sql.append(" AND T1.DE_ARQUIVO LIKE '");
                sql.append(arquivo.trim().toUpperCase());
                sql.append("%'");
            }

            if (anoRef > 0) {
                sql.append(" AND T1.AA_REFERENCIA = ");
                sql.append(anoRef);
            }

            request.setAttribute("titulo", "Declarações de Quitação Enviadas no Boleto");
            request.setAttribute("titulo2", request.getParameter("dataInicio") + " a " + request.getParameter("dataFim"));
            request.setAttribute("sql", sql);
            request.setAttribute("quebra1", "false");
            request.setAttribute("quebra2", "false");
            request.setAttribute("total1", -1);
            request.setAttribute("total2", -1);
            request.setAttribute("total3", -1);
            request.setAttribute("total4", -1);

            request.getRequestDispatcher("/pages/listagem.jsp").forward(request, response);
        } else {
            request.getRequestDispatcher("/pages/2290.jsp").forward(request, response);
        }
    }

    @App("2320")
    public static void envioBoletoEmail(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String acao = request.getParameter("acao");

        if ("visualizar".equals(acao)) {
            StringBuilder sql = new StringBuilder();
            SimpleDateFormat fmt = new SimpleDateFormat("yyyy-MM-dd");
            Date d1 = Datas.parse(request.getParameter("dataInicio"));
            Date d2 = Datas.parse(request.getParameter("dataFim"));
            boolean sucesso = Boolean.parseBoolean(request.getParameter("sucesso"));
            boolean erro = Boolean.parseBoolean(request.getParameter("erro"));

            sql.append("SELECT ");
            sql.append("right('000' + convert(varchar, t1.cd_matricula), 4) + '/' + right('00' + convert(varchar, t1.cd_categoria), 2) + ' - ' + nome_pessoa AS Sócio, ");
            sql.append(" ");
            sql.append(" ");
            sql.append("CONVERT(VARCHAR(10), T1.DT_ENVIO, 103) AS 'Dt. Envio', ");
            sql.append("CONVERT(VARCHAR(40), T1.DE_EMAIL) AS Email, ");
            sql.append("CONVERT(VARCHAR(10), T3.DT_VENC_CARNE, 103) AS 'Dt. Venc', ");
            sql.append("T3.VAL_CARNE AS 'Vr. Carne', ");
            sql.append("T3.SEQ_CARNE AS 'Seq. Carne', ");
            sql.append("CONVERT(VARCHAR(100), T1.DE_ERRO) AS 'Mensagem de Erro' ");
            sql.append("FROM ");
            sql.append("TB_BOLETO_EMAIL T1, ");
            sql.append("TB_PESSOA T2, ");
            sql.append("TB_CARNE T3 ");
            sql.append("WHERE ");
            sql.append("T1.SEQ_CARNE = T3.SEQ_CARNE AND ");
            sql.append("T1.CD_MATRICULA = T2.CD_MATRICULA AND ");
            sql.append("T1.CD_CATEGORIA = T2.CD_CATEGORIA AND ");
            sql.append("T2.SEQ_DEPENDENTE = 0 ");

            sql.append("AND DT_ENVIO >= '");
            sql.append(fmt.format(d1));
            sql.append("' AND DT_ENVIO <= '");
            sql.append(fmt.format(d2));
            sql.append(" 23:59:59'");

            String nome = request.getParameter("nome");
            if (nome != null && !nome.trim().equals("")) {
                sql.append(" AND T2.NOME_PESSOA LIKE '");
                sql.append(nome.trim().toUpperCase());
                sql.append("%'");
            }

            int matricula = 0;
            int categoria = 0;
            try {
                matricula = Integer.parseInt(request.getParameter("matricula"));
            } catch (NumberFormatException e) {
                matricula = 0;
            }

            try {
                categoria = Integer.parseInt(request.getParameter("categoria"));
            } catch (NumberFormatException e) {
                categoria = 0;
            }

            if (matricula > 0) {
                sql.append(" AND T1.CD_MATRICULA = ");
                sql.append(matricula);
            }

            if (categoria > 0) {
                sql.append(" AND T1.CD_CATEGORIA = ");
                sql.append(categoria);
            }

            if (erro) {
                if (!sucesso) {
                    sql.append("AND T1.CD_SIT_ENVIO = 1");
                }
            } else {
                sql.append("AND T1.CD_SIT_ENVIO = 0");
            }

            request.setAttribute("titulo", "Boletos Enviados por Email");
            request.setAttribute("titulo2", request.getParameter("dataInicio") + " a " + request.getParameter("dataFim"));
            request.setAttribute("sql", sql);
            request.setAttribute("quebra1", "true");
            request.setAttribute("quebra2", "false");
            request.setAttribute("total1", -1);
            request.setAttribute("total2", -1);
            request.setAttribute("total3", -1);
            request.setAttribute("total4", -1);

            request.getRequestDispatcher("/pages/listagem.jsp").forward(request, response);
        } else {
            request.getRequestDispatcher("/pages/2320.jsp").forward(request, response);
        }
    }

    @App("2240")
    public static void declaracaoQuitacao(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String acao = request.getParameter("acao");

        if ("visualizar".equals(acao)) {
            StringBuilder sql = new StringBuilder();
            SimpleDateFormat fmt = new SimpleDateFormat("yyyy-MM-dd");
            int tituloInicio = Integer.parseInt(request.getParameter("tituloInicio"));
            int tituloFim = Integer.parseInt(request.getParameter("tituloFim"));
            Date d1 = Datas.parse(request.getParameter("vencimentoInicio"));
            Date d2 = Datas.parse(request.getParameter("vencimentoFim"));
            Date d3 = Datas.parse(request.getParameter("pagamentoInicio"));
            Date d4 = Datas.parse(request.getParameter("pagamentoFim"));
            boolean quitado = Boolean.parseBoolean(request.getParameter("quitado"));
            boolean pendente = Boolean.parseBoolean(request.getParameter("pendente"));

            StringBuilder categorias = new StringBuilder();
            for (String s : request.getParameterValues("categorias")) {
                categorias.append(s);
                categorias.append(", ");
            }
            categorias.delete(categorias.length() - 2, categorias.length());

            if (quitado) {
                sql.append("SELECT CD_MATRICULA, CD_CATEGORIA, 'PG' as SIT FROM TB_PESSOA T1 ");
                sql.append("WHERE NOT EXISTS ");
                sql.append("(SELECT 1 FROM TB_CARNE T2 ");
                sql.append("WHERE T1.CD_MATRICULA = T2.CD_MATRICULA ");
                sql.append("AND T1.CD_CATEGORIA = T2.CD_CATEGORIA ");
                sql.append("AND T1.SEQ_DEPENDENTE = T2.SEQ_DEPENDENTE ");
                sql.append("AND T2.CD_SIT_CARNE = 'NP' ");
                sql.append("AND T2.DT_VENC_CARNE BETWEEN '");
                sql.append(fmt.format(d1));
                sql.append("' AND '");
                sql.append(fmt.format(d2));
                sql.append("') ");
                sql.append("AND T1.SEQ_DEPENDENTE = 0 ");
                sql.append("AND T1.CD_CATEGORIA IN (");
                sql.append(categorias);
                sql.append(") AND T1.CD_MATRICULA BETWEEN ");
                sql.append(tituloInicio);
                sql.append(" AND ");
                sql.append(tituloFim);

                if (d3 != null) {
                    sql.append(" AND EXISTS ");
                    sql.append("(SELECT 1 FROM TB_CARNE T3 ");
                    sql.append("WHERE T1.CD_MATRICULA = T3.CD_MATRICULA ");
                    sql.append("AND T1.CD_CATEGORIA = T3.CD_CATEGORIA ");
                    sql.append("AND T1.SEQ_DEPENDENTE = T3.SEQ_DEPENDENTE ");
                    sql.append("AND T3.CD_SIT_CARNE IN ('AN', 'PG') ");
                    sql.append("AND T3.DT_VENC_CARNE BETWEEN '");
                    sql.append(fmt.format(d1));
                    sql.append("' AND '");
                    sql.append(fmt.format(d2));
                    sql.append("' ");
                    sql.append("AND T3.DT_PGTO_CARNE BETWEEN '");
                    sql.append(fmt.format(d3));
                    sql.append("' AND '");
                    sql.append(fmt.format(d4));
                    sql.append("') ");
                }
            }

            if (pendente) {
                if (quitado) {
                    sql.append(" UNION ALL ");
                }

                sql.append("SELECT CD_MATRICULA, CD_CATEGORIA, 'NP' as SIT FROM TB_PESSOA T1 ");
                sql.append("WHERE EXISTS ");
                sql.append("(SELECT 1 FROM TB_CARNE T2 ");
                sql.append("WHERE T1.CD_MATRICULA = T2.CD_MATRICULA ");
                sql.append("AND T1.CD_CATEGORIA = T2.CD_CATEGORIA ");
                sql.append("AND T1.SEQ_DEPENDENTE = T2.SEQ_DEPENDENTE ");
                sql.append("AND T2.CD_SIT_CARNE = 'NP' ");
                sql.append("AND T2.DT_VENC_CARNE BETWEEN '");
                sql.append(fmt.format(d1));
                sql.append("' AND '");
                sql.append("') ");
                sql.append("AND T1.SEQ_DEPENDENTE = 0 ");
                sql.append("AND T1.CD_CATEGORIA IN (");
                sql.append(categorias);
                sql.append(") AND T1.CD_MATRICULA BETWEEN ");
                sql.append(tituloInicio);
                sql.append(" AND ");
                sql.append(tituloFim);
            }

            request.setAttribute("sql", sql.toString());
            request.getRequestDispatcher("/pages/2240-impressao.jsp").forward(request, response);
        } else {
            request.setAttribute("categorias", ComboBoxLoader.listar("TB_CATEGORIA WHERE STATUS_CATEGORIA = 'AT'"));
            request.getRequestDispatcher("/pages/2240.jsp").forward(request, response);
        }
    }

    @App("1365")
    public static void consultaCarneSequencial(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        int seqCarne = 0;
        int seqCarneDetalhe = 0;
        try {
            seqCarne = Integer.parseInt(request.getParameter("seqCarne"));
        } catch (NumberFormatException e) {
            seqCarne = 0;
        }
        try {
            seqCarneDetalhe = Integer.parseInt(request.getParameter("seqCarneDetalhe"));
        } catch (NumberFormatException e) {
            seqCarneDetalhe = 0;
        }

        request.setAttribute("seqCarne", seqCarne);
        request.setAttribute("seqCarneDetalhe", seqCarneDetalhe);
        request.getRequestDispatcher("/pages/1365.jsp").forward(request, response);
    }

    @App("2470")
    public static void relCreditos(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String acao = request.getParameter("acao");

        if ("visualizar".equals(acao)) {
            StringBuilder sql = new StringBuilder();
            boolean tipoSocio = request.getParameter("tipo").equals("S");
            boolean baixa = request.getParameter("periodo").equals("B");
            SimpleDateFormat fmt = new SimpleDateFormat("yyyy-MM-dd");
            Date d1 = Datas.parse(request.getParameter("dataInicio"));
            Date d2 = Datas.parse(request.getParameter("dataFim"));
            Date d3 = null;
            Date d4 = null;
            boolean filtrarVencimento = Boolean.parseBoolean(request.getParameter("filtrarVencimento"));
            boolean recebidoBanco = Boolean.parseBoolean(request.getParameter("recebidoBanco"));
            boolean recebidoCaixa = Boolean.parseBoolean(request.getParameter("recebidoCaixa"));
            
            boolean bancoBB = Boolean.parseBoolean(request.getParameter("bancoBB"));
            boolean bancoItau = Boolean.parseBoolean(request.getParameter("bancoItau"));
            
            boolean flagCurso = false;//escolinhas
            boolean flagAvulso = false;//Boleto Avulso

            if (filtrarVencimento) {
                d3 = Datas.parse(request.getParameter("vencimentoInicio"));
                d4 = Datas.parse(request.getParameter("vencimentoFim"));
            }

            StringBuilder tmp = new StringBuilder();
            for (String s : request.getParameterValues("taxas")) {
                tmp.append(s);
                tmp.append(", ");
                if (s.equals("8") ) {
                    flagCurso = true;
                }
                if (s.equals("0")) {
                    flagAvulso = true;
                }
            }
            tmp.delete(tmp.length() - 2, tmp.length());

            if (tipoSocio) {
                sql.append("SELECT ");
                sql.append("   T2.NOME_PESSOA 'Sócio', ");
                sql.append("   T2.CD_MATRICULA 'Matr.', ");
                sql.append("   T1.VAL_PARC_CRED 'Valor', ");
                sql.append("   T3.SEQ_CARNE 'ID' ");
                sql.append("FROM ");
                sql.append("   TB_PARCELA_CREDITO T1, ");
                sql.append("   TB_PESSOA T2, ");
                sql.append("   TB_CARNE T3 ");
                sql.append("WHERE ");
                sql.append("   T1.CD_MATRICULA = T2.CD_MATRICULA AND ");
                sql.append("   T1.CD_CATEGORIA = T2.CD_CATEGORIA AND ");
                sql.append("   T1.SEQ_DEPENDENTE = T2.SEQ_DEPENDENTE AND ");
                sql.append("   T1.SEQ_CARNE = T3.SEQ_CARNE AND ");
                
            } else {
                sql.append("SELECT ");
                sql.append("   T2.DESCR_TX_ADMINISTRATIVA 'Taxa', ");
                sql.append("   T1.VAL_PARC_CRED 'Valor', ");
                sql.append("   T3.SEQ_CARNE 'ID' ");
                sql.append("FROM ");
                sql.append("   TB_PARCELA_CREDITO T1, ");
                sql.append("   TB_TAXA_ADMINISTRATIVA T2, ");
                sql.append("   TB_CARNE T3 ");
                sql.append("WHERE ");
                sql.append("   T1.CD_TX_ADMINISTRATIVA = T2.CD_TX_ADMINISTRATIVA AND ");
                sql.append("   T1.SEQ_CARNE = T3.SEQ_CARNE AND ");
                
            }

            
            sql.append("   T1.CD_TX_ADMINISTRATIVA IN( ");
            sql.append(tmp);
            sql.append(") ");

            if (baixa) {
                sql.append(" AND     T3.DT_BAIXA_CARNE >= '");
                sql.append(fmt.format(d1));
                sql.append(" 00:00:00' ");
                sql.append(" AND     T3.DT_BAIXA_CARNE <= '");
                sql.append(fmt.format(d2));
                sql.append(" 23:59:59' ");
            } else {
                sql.append(" AND     T3.DT_PGTO_CARNE >= '");
                sql.append(fmt.format(d1));
                sql.append(" 00:00:00' ");
                sql.append(" AND     T3.DT_PGTO_CARNE <= '");
                sql.append(fmt.format(d2));
                sql.append(" 23:59:59' ");
            }

            if (filtrarVencimento) {
                sql.append(" AND T3.DT_VENC_CARNE >= '");
                sql.append(fmt.format(d3));
                sql.append(" 00:00:00' ");
                sql.append(" AND T3.DT_VENC_CARNE <= '");
                sql.append(fmt.format(d4));
                sql.append(" 23:59:59' ");
            }

            sql.append(" AND T3.CD_SIT_CARNE = 'PG' ");

            if (!recebidoBanco) {
                sql.append(" AND T3.LOCAL_PGTO_CARNE <> 'B' ");
            }
            if (!recebidoCaixa) {
                sql.append(" AND T3.LOCAL_PGTO_CARNE <> 'C' ");
            }

            if (!bancoBB) {
                sql.append(" and T3.BANCO_PGTO <> 'BB' ");
            }
            if (!bancoItau) {
                sql.append(" and T3.BANCO_PGTO <> 'ITAU' ");
            }
            
            
            String titulo = "Relatório de Créditos ";
            String titulo2 = null;
            if (recebidoBanco && recebidoCaixa) {
                titulo += "no Banco e no Clube";
            } else {
                if (recebidoBanco) {
                    titulo += "no Banco";
                } else {
                    titulo += "no Clube";
                }
            }

            if (baixa) {
                titulo2 = "Período (Data de baixa)  "
                        + request.getParameter("dataInicio")
                        + " a "
                        + request.getParameter("dataFim");
            } else {
                titulo2 = "Período (Data de pagamento)  "
                        + request.getParameter("dataInicio")
                        + " a "
                        + request.getParameter("dataFim");
            }

            if (filtrarVencimento) {
                titulo2 += " e Data de Vencimento "
                        + request.getParameter("vencimentoInicio")
                        + " a "
                        + request.getParameter("vencimentoFim");
            }

            boolean quebra1 = false;
            boolean quebra2 = false;

            request.setAttribute("titulo", titulo);
            request.setAttribute("titulo2", titulo2);
            request.setAttribute("sql", sql);
            request.setAttribute("quebra1", quebra1);
            request.setAttribute("quebra2", quebra2);
            if (tipoSocio){
                request.setAttribute("total1", "3");
            }else{
                request.setAttribute("total1", "2");
            }
            
            request.setAttribute("total2", "-1");
            request.setAttribute("total3", "-1");
            request.setAttribute("total4", "-1");
            request.setAttribute("total5", "-1");
            request.setAttribute("total6", "-1");

            request.getRequestDispatcher("/pages/listagem.jsp").forward(request, response);
        } else {
            request.setAttribute("taxas", ComboBoxLoader.listarSql("SELECT * FROM TB_TAXA_ADMINISTRATIVA WHERE IND_TAXA_ADMINISTRATIVA = 'C' "));
            request.getRequestDispatcher("/pages/2470.jsp").forward(request, response);
        }
    }

    @App("1505")
    public static void PendEnvioItau(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
            StringBuilder sql = new StringBuilder();


            sql.append("SELECT ");
            sql.append("	T2.SEQ_CARNE 'Seq. Carne', ");
            sql.append("	T2.CD_MATRICULA 'Título', ");
            sql.append("	T2.CD_CATEGORIA 'Categoria', ");
            sql.append("	T3.NOME_PESSOA 'Nome', ");
            sql.append("	T1.DT_ENVIO 'Data/Hora Geração', ");
            sql.append("	T2.DT_VENC_CARNE 'Dt. Venc. Original', ");
            sql.append("	T1.DT_VENCIMENTO 'Dt. Venc. Novo', ");
            sql.append("	T1.VR_TOTAL 'Vr. Total' ");
            sql.append("FROM  ");
            sql.append("	TB_ENVIO_CARNE_ITAU T1, ");
            sql.append("	TB_CARNE T2, ");
            sql.append("	TB_PESSOA T3 ");
            sql.append("WHERE ");
            sql.append("	T1.SEQ_CARNE = T2.SEQ_CARNE AND ");
            sql.append("	T2.CD_MATRICULA = T3.CD_MATRICULA AND ");
            sql.append("	T2.CD_CATEGORIA = T3.CD_CATEGORIA AND ");
            sql.append("	T2.SEQ_DEPENDENTE = T3.SEQ_DEPENDENTE AND ");
            sql.append("	T1.IC_ENVIADO = 'N' ");
            sql.append("ORDER BY  ");
            sql.append("	T1.DT_ENVIO ");


            request.setAttribute("titulo", "Boletos pendentes de envio para o Itaú");
            request.setAttribute("titulo2", "");
            request.setAttribute("sql", sql);
            request.setAttribute("quebra1", false);
            request.setAttribute("quebra2", false);
            request.setAttribute("total1", "-1");
            request.setAttribute("total2", "-1");
            request.setAttribute("total3", "-1");
            request.setAttribute("total4", "-1");
            request.setAttribute("total5", "-1");
            request.setAttribute("total6", "-1");

            request.getRequestDispatcher("/pages/listagem.jsp").forward(request, response);
        }
}
