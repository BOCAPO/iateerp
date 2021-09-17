package techsoft.operacoes;

import java.math.BigDecimal;

public class ResultadoBaixaArquivoConvenioBB {
    
    BigDecimal valorAcatado = BigDecimal.ZERO;
    BigDecimal valorAvulso = BigDecimal.ZERO;
    BigDecimal valorRejeitado = BigDecimal.ZERO;
    BigDecimal valorAMais = BigDecimal.ZERO;
    BigDecimal valorPgAnt = BigDecimal.ZERO;
    int totalAcatado = 0;
    int totalAvulso = 0;
    int totalRejeitado = 0;
    int totalAMais = 0;
    int totalPgAnt = 0;
    int totalRegistros = 0;

    public BigDecimal getValorAcatado() {
        return valorAcatado;
    }

    public BigDecimal getValorAvulso() {
        return valorAvulso;
    }

    public BigDecimal getValorRejeitado() {
        return valorRejeitado;
    }

    public BigDecimal getValorAMais() {
        return valorAMais;
    }

    public BigDecimal getValorPgAnt() {
        return valorPgAnt;
    }

    public int getTotalAcatado() {
        return totalAcatado;
    }

    public int getTotalAvulso() {
        return totalAvulso;
    }

    public int getTotalRejeitado() {
        return totalRejeitado;
    }

    public int getTotalAMais() {
        return totalAMais;
    }

    public int getTotalPgAnt() {
        return totalPgAnt;
    }

    public int getTotalRegistros() {
        return totalRegistros;
    }
    
    
}
