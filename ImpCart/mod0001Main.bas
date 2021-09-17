Attribute VB_Name = "mod0001Main"
'* mod0001Main - Módulo Inicial do aplicativo Clube
'*
Option Explicit
#If Win32 Then
   Declare Function GetAsyncKeyState% Lib "User32" (ByVal vKey%)
#Else
   Declare Function GetAsyncKeyState% Lib "User" (ByVal vKey%)
#End If
'*
'* RDO variables
'*
Global gCn As New ADODB.Connection
Global gCnFoto As New ADODB.Connection
Global ClientSideCon As New ADODB.Connection
Global gConn As String
Global gConnFoto As String
Global gMyrs As ADODB.Recordset
Global wSql As String
Global gConClient As String

Const TamFoto As Integer = 16384

Global Ret As Integer
Global ifile As Integer

'*
Sub Main()
    Const SWP_NOSIZE = 1
    Const SWP_NOMOVE = 2
    Const HWND_TOPMOST = -1
    Dim UserBD As String
    Dim SenhaBD As String
    Dim DSNBD As String
    Dim ServidorBD As String
    Dim NomeBD As String
    Dim NomeBDfoto As String
    Dim SENHA As New Cripton
    Dim parametros As String
    Dim Matricula As Integer
    Dim Categoria As Integer
    Dim SeqDependente As Integer
    Dim DtVenc As String
    Dim NomeImp As String
    Dim arq As Integer
    
    ifile = FreeFile()
    Open App.Path & "\log.log" For Binary Access Write As #ifile
    Put #ifile, , "Iniciando processamento" & vbCrLf
   
    
    On Error GoTo ErroMain
    
    parametros = Command$
    
    If parametros = "" Then
      parametros = "0100940001012014"
    End If
    
    If Not IsNumeric(parametros) Then
      End
    End If
    
    If Len(parametros) <> 16 Then
      End
    End If
    
    Put #ifile, , "Parametro recebido: " & parametros & vbCrLf
    
    Categoria = Mid(parametros, 1, 2)
    Matricula = Mid(parametros, 3, 4)
    SeqDependente = Mid(parametros, 7, 2)
    DtVenc = Mid(parametros, 9)
    DtVenc = Mid(DtVenc, 3, 2) & "/" & Mid(DtVenc, 1, 2) & "/" & Mid(DtVenc, 5)
      
    Put #ifile, , "Quebra dos parametros: " & _
            Categoria & " - " & _
            Matricula & " - " & _
            SeqDependente & " - " & _
            DtVenc & vbCrLf
    
    Open App.Path & "\Config.txt" For Input As #2

    Input #2, DSNBD
    Input #2, ServidorBD
    Input #2, NomeBD
    Input #2, UserBD
    Input #2, SenhaBD
    Input #2, NomeBDfoto
    Input #2, NomeImp
    
    SenhaBD = SENHA.Decripto(SenhaBD, "IATEBSB")
    
    Close #2

    Screen.MousePointer = vbHourglass

AberturaBancoDados:
    gConn$ = "Provider=SQLOLEDB.1;Password=" & SenhaBD & ";User ID=" & UserBD & ";Initial Catalog=" & NomeBD & ";Data Source=" & ServidorBD
    gConnFoto$ = "Provider=SQLOLEDB.1;Password=" & SenhaBD & ";User ID=" & UserBD & ";Initial Catalog=" & NomeBDfoto & ";Data Source=" & ServidorBD
    gConClient = "Provider=SQLOLEDB.1;Password=" & SenhaBD & ";User ID=" & UserBD & ";Initial Catalog=" & NomeBD & ";Data Source=" & ServidorBD

    Put #ifile, , "Vai abrir a primeira conexao com o banco: " & gConn$ & vbCrLf
    gCn.CursorLocation = adUseClient
    gCn.Open gConn
    Put #ifile, , "Conexao Aberta" & vbCrLf
    
    Put #ifile, , "Vai abrir a segunda conexao com o banco: " & gConnFoto$ & vbCrLf
    gCnFoto.CursorLocation = adUseClient
    gCnFoto.Open gConnFoto
    Put #ifile, , "Conexao Aberta" & vbCrLf
    

    Put #ifile, , "Vai abrir a segundaconexao com o banco: " & gConClient & vbCrLf
    ClientSideCon.CursorLocation = adUseClient
    ClientSideCon.Open gConClient
    
    Put #ifile, , "Conexao Aberta" & vbCrLf
   
    ImprimirCarteira Matricula, Categoria, SeqDependente, DtVenc, NomeImp
    
    Put #ifile, , "Fim de Processamento" & vbCrLf
    
    End
    
ErroMain:
   Put #ifile, , "erroMain: " & Err.Number & " - " & Err.Description & gConClient & vbCrLf & vbCrLf
   End

End Sub


Public Sub ImprimirCarteira(Matricula As Integer, Categoria As Integer, SeqDependente As Integer, DtVenc As String, Impressora As String)
Dim sCategoria As String
Dim sDescrCategoria As String
Dim sGparentesco As String
Dim sSql As String
Dim rstCarteira As ADODB.Recordset
Dim strSql As String
Dim strSql2 As String
Dim nrCarteira As Long
Dim nrCarteiraStr As String
Dim rstCargoEspecial As ADODB.Recordset
Dim CorTitular As String
Dim CorDependente As String
Dim CorFundoCargo As String
Dim CorFonteCargo As String
Dim TextoCargo As String
Dim rstPessoa As ADODB.Recordset
Dim NomePessoa As String
Dim DescrCategoria As String
Dim rstFotoPessoa As ADODB.Recordset
Dim ALTURA2 As Integer
Dim i As Integer
Dim AbrevCategoria As String
Dim x As Printer

On Error GoTo Erro

Put #ifile, , "Vai procurar a impressora" & vbCrLf

For Each x In Printers
   If x.DeviceName = Impressora Then
      Set Printer = x
      Put #ifile, , "Impressora encontrada " & vbCrLf
      Exit For
   End If
Next
Put #ifile, , "Termino de procura da impressora" & vbCrLf

Printer.ScaleMode = 6
ALTURA2 = 1
For i = 8 To 14
    Printer.DrawMode = i

    Printer.CurrentX = 191
    Printer.CurrentY = (ALTURA2) + 1
    Printer.Font = "Lucida Sans Unicode"
    Printer.FontBold = True
    Printer.FontSize = 5
    Printer.Print "TWI"

    Printer.Line (190, ALTURA2)-(194, ALTURA2 + 4), vbGreen, BF
    ALTURA2 = ALTURA2 + 4
Next i

Printer.KillDoc

Printer.FontTransparent = True

strSql = "select foto_pessoa from tb_foto_pessoa where " _
        & " cd_matricula = " & Matricula & " and " _
        & " seq_dependente = " & SeqDependente & " and " _
        & " cd_categoria = " & Categoria

Set rstFotoPessoa = gCnFoto.Execute(strSql)

If rstFotoPessoa.RecordCount > 0 Then
    ' pessoa já possui foto
    
     
    If RecuperaFoto(rstFotoPessoa.Fields(0)) Then
       DoEvents
    End If
End If
rstFotoPessoa.Close


sSql = "select t1.nome_pessoa, t2.descr_categoria, T2.ABREV_CATEGORIA from tb_pessoa t1, tb_categoria t2 where t1.cd_categoria = t2.cd_categoria and " _
        & " t1.cd_matricula = " & Matricula & " and " _
        & " t1.seq_dependente = " & SeqDependente & " and " _
        & " t1.cd_categoria = " & Categoria

Set rstPessoa = gCn.Execute(sSql)

NomePessoa = rstPessoa!NOME_PESSOA
DescrCategoria = rstPessoa!DESCR_CATEGORIA
AbrevCategoria = rstPessoa!ABREV_CATEGORIA

rstPessoa.Close

strSql = "EXEC SP_ATUALIZA_NR_CART_IMPRESSA " & _
        Matricula & ", " & _
        SeqDependente & ", " & _
        Categoria & ", 'FLAVIO', '" & DtVenc & "'"

strSql2 = strSql

Set rstCarteira = ClientSideCon.Execute(strSql)

If rstCarteira.RecordCount > 0 Then
   If Len(rstCarteira!nr_Carteira) = 7 Then
   
      nrCarteiraStr = Format(rstCarteira![nr_Carteira], "0000000000")
      nrCarteira = nrCarteiraStr & calculaDigitoCarteirinha(nrCarteiraStr)

      strSql = "UPDATE TB_PESSOA SET NR_CARTEIRA_PESSOA = " & nrCarteira & _
               " WHERE CD_MATRICULA   = " & Matricula & _
               " AND   CD_CATEGORIA   = " & Categoria & _
               " AND   SEQ_DEPENDENTE = " & SeqDependente
      
      gCn.Execute strSql
   
      strSql = "UPDATE TB_HIST_EMISSAO_CARTEIRA SET NR_CARTEIRA = " & nrCarteira & _
               " WHERE NR_CARTEIRA = " & nrCarteiraStr
      
      gCn.Execute strSql
   
   Else
      nrCarteira = rstCarteira!nr_Carteira
   End If
   sCategoria = rstCarteira!ABREV_CATEGORIA
   CorTitular = rstCarteira!cor_titular & ""
   CorDependente = rstCarteira!cor_dependente & ""
Else
  GoTo Erro
End If

rstCarteira.Close


'*******************************************
'                  INICIO
'*******************************************
Dim IniCol As Integer
Dim Tit As String

Printer.PaintPicture LoadPicture(App.Path & "\logofundo.bmp"), 0, -1, 90, 54
Printer.PaintPicture LoadPicture("pictemp.bmp"), 1, 1, 24, 33
Printer.PaintPicture LoadPicture(App.Path & "\logo nova.jpg"), 49, 0, 13, 15

Tit = Trim(AbrevCategoria) & " - " & Trim(Matricula)
Printer.Font = "Arial Black"
Printer.FontSize = 26
IniCol = Printer.TextWidth(Tit)
IniCol = (62 - IniCol) / 2
Imprimir 17, IniCol + 24, "Arial Black", 26, False, False, Tit

Imprimir 36, 2, "Arial", 5, False, False, "Nome:"
Imprimir 43, 2, "Arial", 5, False, False, "Título:"
Imprimir 43, 16, "Arial", 5, False, False, "Categoria:"
Imprimir 43, 56, "Arial", 5, False, False, "Comodoro:"

'VERIFICAR
Imprimir 38, 3, "Arial", 8, True, False, NomePessoa
Imprimir 45, 3, "Arial", 8, True, False, Trim(AbrevCategoria) & " - " & Trim(Matricula)


If SeqDependente = 0 Then
   Imprimir 45, 17, "Arial", 8, True, False, DescrCategoria
   If CorTitular <> "" Then
      Printer.Line (1, 50)-(88, 54), CorTitular, BF
   End If
Else
   Imprimir 45, 17, "Arial", 8, True, False, "DEPENDENTE"
   If CorDependente <> "" Then
      Printer.Line (1, 50)-(88, 54), CorDependente, BF
   End If
End If

TextoCargo = ""

sSql = "SELECT " & _
           "T1.DESCR_CARGO_ESPECIAL, " & _
           "T3.DE_TIPO_CARGO, " & _
           "T1.DE_CATEGORIA, " & _
           "T1.COR_CARTEIRA, " & _
           "T1.COR_FONTE, " & _
           "T1.DE_GESTAO " & _
       "FROM " & _
           "TB_CARGO_ESPECIAL T1," & _
           "TB_PESSOA T2, " & _
           "TB_TIPO_CARGO T3 " & _
       "WHERE " & _
           "T1.CD_CARGO_ESPECIAL = T2.CD_CARGO_ESPECIAL AND " & _
           "T1.COR_CARTEIRA IS NOT NULL AND " & _
           "T2.CD_CARGO_ATIVO = 'S' AND " & _
           "T2.CD_MATRICULA = " & Matricula & " AND " & _
           "T2.CD_CATEGORIA = " & Categoria & " AND " & _
           "T2.SEQ_DEPENDENTE = " & SeqDependente & " AND " & _
           "T1.IC_TIPO_CARGO *= T3.CO_TIPO_CARGO"

Set rstCargoEspecial = ClientSideCon.Execute(sSql)

If rstCargoEspecial.RecordCount > 0 Then
   'VERIFICAR
   TextoCargo = rstCargoEspecial!DESCR_CARGO_ESPECIAL
   CorFundoCargo = vbWhite
   CorFonteCargo = vbWhite
   If Not IsNull(rstCargoEspecial!COR_CARTEIRA) Then
      CorFundoCargo = rstCargoEspecial!COR_CARTEIRA
   End If
   If Not IsNull(rstCargoEspecial!COR_FONTE) Then
      CorFonteCargo = rstCargoEspecial!COR_FONTE
   End If
   
   If Not IsNull(rstCargoEspecial!DE_GESTAO) Then
      TextoCargo = TextoCargo & " - GESTÃO " & rstCargoEspecial!DE_GESTAO
   End If

   Tit = TextoCargo
   Printer.Font = "Arial"
   Printer.FontSize = 8
   IniCol = Printer.TextWidth(Tit)
   IniCol = (88 - IniCol) / 2
   Printer.Line (IniCol - 1, 50)-(IniCol + Printer.TextWidth(Tit) + 1, 54), CorFundoCargo, BF
   
   Printer.ForeColor = CorFonteCargo
   Imprimir 50, IniCol, "Arial", 8, False, False, Tit

End If

rstCargoEspecial.Close

Printer.NewPage

Printer.ForeColor = vbRed

If Trim(TextoCargo) = "" Then
   Imprimir 3, 4, "Arial", 8, True, False, "Validade " & DtVenc
End If

Printer.ForeColor = vbBlack
Imprimir 3, 68, "Arial", 8, False, False, Format(nrCarteira, "000000000")

Imprimir 9, 4, "Times New Roman", 9, False, False, "É dever do associado identificar-se, quando solicitado, na"
Imprimir 12, 4, "Times New Roman", 9, False, False, "portaria e em qualquer dependência do IATE, mediante a"
Imprimir 15, 4, "Times New Roman", 9, False, False, "apresentação da carteira social  (artigo 40, incisos VI e VII"
Imprimir 18, 4, "Times New Roman", 9, False, False, "do Estatuto do Clube)."
Imprimir 22, 4, "Times New Roman", 9, False, False, "A carteira social é pessoal e intransferível, sob pena de"
Imprimir 25, 4, "Times New Roman", 9, False, False, "suspensão quando emprestada para possibilitar o ingresso de"
Imprimir 28, 4, "Times New Roman", 9, False, False, "outrem ao Clube  (artigo 43, inciso V do Estatuto do Clube)."
Imprimir 32, 4, "Times New Roman", 9, False, False, "Em caso de roubo, furto ou extravio, comunicar"
Imprimir 35, 4, "Times New Roman", 9, False, False, "imediatamente à Secretaria do Clube."

Imprimir 42, 16, "C39P36DlTt", 30, False, False, "!" & Format(nrCarteira, "000000000") & "!"

Printer.EndDoc
'*******************************************
'                  FIM
'*******************************************

Exit Sub

Erro:
    Put #ifile, , "ErroImprimirCarteira: " & Err.Number & " - " & Err.Description & vbCrLf & vbCrLf

    End

End Sub


Public Sub Imprimir(ByVal Lin As Integer, ByVal Col As Integer, ByVal Nome As String, ByVal Tamanho As Integer, ByVal Ng As Boolean, ByVal It As Boolean, ByVal mSg As String)

   On Error GoTo Erro

   Printer.ScaleMode = 6
   Printer.FontName = Nome
   Printer.FontSize = Tamanho
   Printer.FontBold = Ng
   Printer.FontItalic = It
   Printer.CurrentX = Col
   Printer.CurrentY = Lin
   Printer.Print mSg

   Exit Sub

Erro:
   Put #ifile, , "ErroImprimir: " & Err.Number & " - " & Err.Description & gConClient & vbCrLf & vbCrLf
   End
  
End Sub



Public Function RecuperaFoto(ByVal objcoluna As Object) As Boolean
Dim Arquivo As Integer
Dim divisao As Integer
Dim vetor() As Byte
Dim resto As Long
Dim tamanhofoto As Long
Dim i As Integer

On Error GoTo ErroFoto

RecuperaFoto = False

Screen.MousePointer = vbHourglass

Arquivo = 4
Open "pictemp.bmp" For Binary Access Write As Arquivo

tamanhofoto = objcoluna.ActualSize

divisao = Val(tamanhofoto / TamFoto)

resto = tamanhofoto Mod TamFoto
ReDim vetor(resto)
vetor() = objcoluna.GetChunk(resto)
Put Arquivo, , vetor()

For i = 1 To divisao
   ReDim vetor(TamFoto)
   vetor() = objcoluna.GetChunk(TamFoto)
   Put Arquivo, , vetor()
Next i

Close Arquivo

RecuperaFoto = True

Exit Function

ErroFoto:
   Put #ifile, , "ErroRecuperaFoto: " & Err.Number & " - " & Err.Description & gConClient & vbCrLf & vbCrLf

   End

End Function





Public Function calculaDigitoCarteirinha(nrCarteira As String) As String
       Dim Soma, Digito As Integer
       
       On Error GoTo Erro
       
       '*
       If Len(nrCarteira) <> 10 Then
         GoTo Erro
       End If
       '*
       Soma = Val(Mid(nrCarteira, 10, 1)) * 2 + Val(Mid(nrCarteira, 9, 1)) * 3 + _
              Val(Mid(nrCarteira, 8, 1)) * 4 + Val(Mid(nrCarteira, 7, 1)) * 5 + _
              Val(Mid(nrCarteira, 6, 1)) * 6 + Val(Mid(nrCarteira, 5, 1)) * 7 + _
              Val(Mid(nrCarteira, 4, 1)) * 8 + Val(Mid(nrCarteira, 3, 1)) * 9 + _
              Val(Mid(nrCarteira, 2, 1)) * 2 + Val(Mid(nrCarteira, 1, 1)) * 3
       '*
       Digito = (Soma * 10) Mod 11
       If Digito = 10 Then
          Digito = 0
       End If
       '*
       calculaDigitoCarteirinha = Format(Str(Digito), "0")
       
       Exit Function

Erro:
   Put #ifile, , "ErrocalculaDigitoCarteirinha: " & Err.Number & " - " & Err.Description & gConClient & vbCrLf & vbCrLf

   End

End Function


