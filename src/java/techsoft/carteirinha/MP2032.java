package techsoft.carteirinha;

import com.sun.jna.Library; 
import com.sun.jna.Native;  
import com.sun.jna.WString; 

public interface MP2032 extends Library{
    
    //public MP2032 INSTANCE = (MP2032) Native.loadLibrary("mp2032", MP2032.class);
    public MP2032 INSTANCE = null;
    
    /**
     * Imprime uma imagem bitmap na impressora não fiscal com atributos especiais de impressão.
     * 
     * @param sFileName STRING com o caminho completo do arquivo contendo o bitmap.
     * @param xScale indica o escalonamento da imagem na horizontal em porcentagem. Ex: 100 (%) indica largura atual; 200 (%) indica largura 2 vezes maior; -1 (menos um) indica ajuste da largura na página.
     * @param yScale indica o escalonamento da imagem na vertical em porcentagem. Ex: 100 (%) indica altura atual; 50 (%) indica metade da altura. Ignorado se parâmetro xScale seja &endash;1 (menos um).
     * @param iAngle usada para girar o bitmap na impressão. Ex: 0 (°) indica sem rotacionamento da imagem; 45 (°) indica rotacionar a imagem em 45 graus.
     * 
     * @return 0 (zero): Erro de Comunicação<br> 
     * 1 (um) : OK<br>
     * -1 (menos um): Erro de Execução<br>
     * -2 (menos dois): Arquivo inexistente<br>
     * -3 (menos três): Erro na leitura do arquivo, arquivo inválido<br>
     * -4 (menos quatro): Parâmetro inválido.
     */
    public int ImprimeBmpEspecial(String sFileName, int xScale, int yScale, int iAngle);
    
    /**
     * Esta função tem por objetivo enviar textos para a impressora, com formatações, informadas pelos parâmetros.
     * @param BufTras Texto a ser impresso.
     * @param TpoLtra 
     * 1 = comprimido
     * 2 = normal
     * 3 = elite.
     * @param Italic 1 = ativa o modo itálico, 0 = desativa o modo itálico.
     * @param Sublin 1 = ativa o modo sublinhado, 0 = desativa o modo sublinhado.
     * @param expand 1 = ativa o modo expandido, 0 = desativa o modo expandido.
     * @param enfat 1 = ativa o modo enfatizado, 0 = desativa o modo enfatizado.
     * @return 1 (um): Sucesso. A função foi executada sem problemas<br>
     * 0 (zero): Erro de comunicação física.
     */
    public int FormataTX(String BufTras, int TpoLtra, int Italic, int Sublin, int expand, int enfat);
    
    /**
     * Esta função configura os códigos de barras, definindo Altura, Largura e Posição dos caracteres.
     * 
     * @param Altura Inteiro entre 1 à 255. (default 162)
     * @param Largura
     *      Largura = 0 (barras finas)
     *      Largura = 1 (barras médias) - default
     *      Largura = 2 (barras grossas)
     * @param PosicaoCaracteres
     *      Posição = 0 (não imprime os caracteres do código)
     *      Posição = 1 (imprime os caracteres acima do código)
     *      Posição = 2 (imprime os caracteres abaixo do código) - default
     *      Posição = 3 (imprime os caracteres acima e abaixo do código)
     * @param Fonte
     *      Fonte = 0 (normal) 
     *      Fonte = 1 (condensado)
     * @param Margem Inteiro entre 0 à 575 (dots pitch) Margem = 0 (sem margem) default
     * @return 
     *      0 (zero): Erro de Comunicação. 
     *      1 (um): OK.
     *      -1 (menos um): Erro de Execução.
     *      -2 (menos dois): Parâmetro Inválido.
     */
    public int ConfiguraCodigoBarras(int Altura, int Largura, int PosicaoCaracteres, int Fonte, int Margem);
    
    /**
     * Esta função faz a impressão do código de barras CODE39.
     * O tamanho da string é dada pela tabela abaixo:<br>
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
     * A Largura das Barras é 1 (default).<br>
     * <pre>
     * Observações:
     *      Será acrescentado, automaticamente, o dígito verificador.
     *      Aceita dígitos entre 0 à 9.
     *      Aceita letras de A à Z (maiúsculas e minúsculas).
     *      Aceita os caracteres: "espaço em branco", "$", "%", "+", "-", "." e "/".
     *      As letras não podem se maiúsculas e minúsculas simultaneamente.
     * </pre>
     * @param Codigo STRING com o código que será gerado.
     * @return 
     *      0 (zero): Erro de Comunicação. 
     *      1 (um): OK.
     *      -1 (menos um): Erro de Execução.
     *      -2 (menos dois): Parâmetro Inválido.
     */
    public int ImprimeCodigoBarrasCODE39(String Codigo);
    
    /**
     * Seleciona a largura da bitola do papel da impressora. A largura default utilizada pela DLL é de 48 mm.
     * @param width indica a bitola do papel em milímetros. Podendo ser: 48, 58, 76, 80 ou 112.
     * @return 
     *      0 (zero): Erro de Comunicação.<br>
     *      1 (um): OK.<br>
     *      -4 (menos quatro): Parâmetro inválido.
     */
    public int AjustaLarguraPapel(int width);
    
    /**
     * Esta função tem por objetivo abrir a porta de comunicação, onde a impressora está conectada.
     * @param Porta nome da porta de comunicação. Ex: USB, COM2, LPT1...
     * @return 
     *      menor ou igual a 0 (zero): problemas ao abrir a porta de comunicação.<br>
     *      1 (um): porta de comunicação iniciada sem problemas.
     */
    public int IniciaPorta(String Porta);
    
    /**
     * Esta função tem por objetivo fechar a porta de comunicação, liberando a porta para outras atividades.
     * @return 1 (um): Sucesso. A função foi executada sem problemas<br>
     * 0 (zero): Erro ao fechar a porta de comunicação.
     */
    public int FechaPorta();
    
    /**
     * Esta função é utilizada para configurar o modelo da impressora não fiscal em uso.<br>
     * <strong>IMPORTANTE</strong><br>
     * Esta função deve ser usada antes da função {@link #IniciaPorta(String)}.
     * 
     * @param ModeloImpressora
     * 0: MP-20 TH, MP-2000 CI ou MP-2000 TH 
     * 1: MP-20 MI, MP-20 CI ou MP-20 S
     * 2: Blocos térmicos (com comunicacao serial DTR/DSR)
     * 3: Bloco 112 mm
     * 4: ThermalKiosk
     * 5: MP-4000 TH
     * 7: MP-4200 TH
     * O Default é 0 (zero). 
     * 
     * @return 1 (um): OK, -2 (menos dois): Parâmetro Inválido. 

     */
    public int ConfiguraModeloImpressora(int ModeloImpressora);
    
    /**
     * Esta função aciona a guilhotina, contando o papel em modo parcial ou total.
     * @param Modo 
     *      0 (zero): Modo Parcial (Parcial Cut)<br>
     *      1 (um): Modo Total (Full Cut).
     * 
     * @return 
     *      0 (zero): Erro de Comunicação.<br>
     *      1 (um): OK.<br>
     *      -2 (menos dois): Parâmetro Inválido.
     */
    public int AcionaGuilhotina(int Modo);

}
