package techsoft.carteirinha;

import com.sun.jna.Library; 
import com.sun.jna.Native;  
import com.sun.jna.WString; 

public interface MP2032 extends Library{
    
    //public MP2032 INSTANCE = (MP2032) Native.loadLibrary("mp2032", MP2032.class);
    public MP2032 INSTANCE = null;
    
    /**
     * Imprime uma imagem bitmap na impressora n�o fiscal com atributos especiais de impress�o.
     * 
     * @param sFileName STRING com o caminho completo do arquivo contendo o bitmap.
     * @param xScale indica o escalonamento da imagem na horizontal em porcentagem. Ex: 100 (%) indica largura atual; 200 (%) indica largura 2 vezes maior; -1 (menos um) indica ajuste da largura na p�gina.
     * @param yScale indica o escalonamento da imagem na vertical em porcentagem. Ex: 100 (%) indica altura atual; 50 (%) indica metade da altura. Ignorado se par�metro xScale seja &endash;1 (menos um).
     * @param iAngle usada para girar o bitmap na impress�o. Ex: 0 (�) indica sem rotacionamento da imagem; 45 (�) indica rotacionar a imagem em 45 graus.
     * 
     * @return 0 (zero): Erro de Comunica��o<br> 
     * 1 (um) : OK<br>
     * -1 (menos um): Erro de Execu��o<br>
     * -2 (menos dois): Arquivo inexistente<br>
     * -3 (menos tr�s): Erro na leitura do arquivo, arquivo inv�lido<br>
     * -4 (menos quatro): Par�metro inv�lido.
     */
    public int ImprimeBmpEspecial(String sFileName, int xScale, int yScale, int iAngle);
    
    /**
     * Esta fun��o tem por objetivo enviar textos para a impressora, com formata��es, informadas pelos par�metros.
     * @param BufTras Texto a ser impresso.
     * @param TpoLtra 
     * 1 = comprimido
     * 2 = normal
     * 3 = elite.
     * @param Italic 1 = ativa o modo it�lico, 0 = desativa o modo it�lico.
     * @param Sublin 1 = ativa o modo sublinhado, 0 = desativa o modo sublinhado.
     * @param expand 1 = ativa o modo expandido, 0 = desativa o modo expandido.
     * @param enfat 1 = ativa o modo enfatizado, 0 = desativa o modo enfatizado.
     * @return 1 (um): Sucesso. A fun��o foi executada sem problemas<br>
     * 0 (zero): Erro de comunica��o f�sica.
     */
    public int FormataTX(String BufTras, int TpoLtra, int Italic, int Sublin, int expand, int enfat);
    
    /**
     * Esta fun��o configura os c�digos de barras, definindo Altura, Largura e Posi��o dos caracteres.
     * 
     * @param Altura Inteiro entre 1 � 255. (default 162)
     * @param Largura
     *      Largura = 0 (barras finas)
     *      Largura = 1 (barras m�dias) - default
     *      Largura = 2 (barras grossas)
     * @param PosicaoCaracteres
     *      Posi��o = 0 (n�o imprime os caracteres do c�digo)
     *      Posi��o = 1 (imprime os caracteres acima do c�digo)
     *      Posi��o = 2 (imprime os caracteres abaixo do c�digo) - default
     *      Posi��o = 3 (imprime os caracteres acima e abaixo do c�digo)
     * @param Fonte
     *      Fonte = 0 (normal) 
     *      Fonte = 1 (condensado)
     * @param Margem Inteiro entre 0 � 575 (dots pitch) Margem = 0 (sem margem) default
     * @return 
     *      0 (zero): Erro de Comunica��o. 
     *      1 (um): OK.
     *      -1 (menos um): Erro de Execu��o.
     *      -2 (menos dois): Par�metro Inv�lido.
     */
    public int ConfiguraCodigoBarras(int Altura, int Largura, int PosicaoCaracteres, int Fonte, int Margem);
    
    /**
     * Esta fun��o faz a impress�o do c�digo de barras CODE39.
     * O tamanho da string � dada pela tabela abaixo:<br>
     * <br>
     * <table>
     * <tr>
     * <td>Largura das Barras</td>
     * <td>Quantidade de Caracteres</td>
     * </tr>
     * <tr>
     * <td>0</td>
     * <td>15</td>
     * </tr>
     * <tr>
     * <td>1</td>
     * <td>9</td>
     * </tr>
     * <tr>
     * <td>2</td>
     * <td>6</td>
     * </tr>
     * </table>
     * <br>
     * A Largura das Barras � 1 (default).<br>
     * <pre>
     * Observa��es:
     *      Ser� acrescentado, automaticamente, o d�gito verificador.
     *      Aceita d�gitos entre 0 � 9.
     *      Aceita letras de A � Z (mai�sculas e min�sculas).
     *      Aceita os caracteres: "espa�o em branco", "$", "%", "+", "-", "." e "/".
     *      As letras n�o podem se mai�sculas e min�sculas simultaneamente.
     * </pre>
     * @param Codigo STRING com o c�digo que ser� gerado.
     * @return 
     *      0 (zero): Erro de Comunica��o. 
     *      1 (um): OK.
     *      -1 (menos um): Erro de Execu��o.
     *      -2 (menos dois): Par�metro Inv�lido.
     */
    public int ImprimeCodigoBarrasCODE39(String Codigo);
    
    /**
     * Seleciona a largura da bitola do papel da impressora. A largura default utilizada pela DLL � de 48 mm.
     * @param width indica a bitola do papel em mil�metros. Podendo ser: 48, 58, 76, 80 ou 112.
     * @return 
     *      0 (zero): Erro de Comunica��o.<br>
     *      1 (um): OK.<br>
     *      -4 (menos quatro): Par�metro inv�lido.
     */
    public int AjustaLarguraPapel(int width);
    
    /**
     * Esta fun��o tem por objetivo abrir a porta de comunica��o, onde a impressora est� conectada.
     * @param Porta nome da porta de comunica��o. Ex: USB, COM2, LPT1...
     * @return 
     *      menor ou igual a 0 (zero): problemas ao abrir a porta de comunica��o.<br>
     *      1 (um): porta de comunica��o iniciada sem problemas.
     */
    public int IniciaPorta(String Porta);
    
    /**
     * Esta fun��o tem por objetivo fechar a porta de comunica��o, liberando a porta para outras atividades.
     * @return 1 (um): Sucesso. A fun��o foi executada sem problemas<br>
     * 0 (zero): Erro ao fechar a porta de comunica��o.
     */
    public int FechaPorta();
    
    /**
     * Esta fun��o � utilizada para configurar o modelo da impressora n�o fiscal em uso.<br>
     * <strong>IMPORTANTE</strong><br>
     * Esta fun��o deve ser usada antes da fun��o {@link #IniciaPorta(String)}.
     * 
     * @param ModeloImpressora
     * 0: MP-20 TH, MP-2000 CI ou MP-2000 TH 
     * 1: MP-20 MI, MP-20 CI ou MP-20 S
     * 2: Blocos t�rmicos (com comunicacao serial DTR/DSR)
     * 3: Bloco 112 mm
     * 4: ThermalKiosk
     * 5: MP-4000 TH
     * 7: MP-4200 TH
     * O Default � 0 (zero). 
     * 
     * @return 1 (um): OK, -2 (menos dois): Par�metro Inv�lido. 

     */
    public int ConfiguraModeloImpressora(int ModeloImpressora);
    
    /**
     * Esta fun��o aciona a guilhotina, contando o papel em modo parcial ou total.
     * @param Modo 
     *      0 (zero): Modo Parcial (Parcial Cut)<br>
     *      1 (um): Modo Total (Full Cut).
     * 
     * @return 
     *      0 (zero): Erro de Comunica��o.<br>
     *      1 (um): OK.<br>
     *      -2 (menos dois): Par�metro Inv�lido.
     */
    public int AcionaGuilhotina(int Modo);

}
