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
    Dim DtVenc As String
    Dim NomeImp As String
    Dim arq As Integer
    
    ifile = FreeFile()
    Open App.Path & "\log.log" For Binary Access Write As #ifile
    Put #ifile, , "Iniciando processamento" & vbCrLf
   
    On Error GoTo ErroMain
    
    parametros = Command$
    
    If parametros = "" Then
      parametros = "00022320092014"
    End If
    
    Put #ifile, , "Parametro recebido: " & parametros & vbCrLf
    
    If Not IsNumeric(parametros) Then
      End
    End If
    
    If Len(parametros) = 6 Then
       DtVenc = ""
    ElseIf Len(parametros) = 14 Then
       DtVenc = Mid(parametros, 7)
       DtVenc = Mid(DtVenc, 3, 2) & "/" & Mid(DtVenc, 1, 2) & "/" & Mid(DtVenc, 5)
    End If
    Matricula = Mid(parametros, 1, 6)
      
    Put #ifile, , "Quebra dos parametros: " & _
            Matricula & " - " & _
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
    
    Put #ifile, , "Vai abrir a conexao com o banco de fotos: " & gConnFoto$ & vbCrLf
    gCnFoto.CursorLocation = adUseClient
    gCnFoto.Open gConnFoto
    Put #ifile, , "Conexao Aberta" & vbCrLf
    

    Put #ifile, , "Vai abrir a segunda conexao com o banco: " & gConClient & vbCrLf
    ClientSideCon.CursorLocation = adUseClient
    ClientSideCon.Open gConClient
    
    Put #ifile, , "Conexao Aberta" & vbCrLf
   
    ImprimirCracha Matricula, DtVenc, NomeImp
    
    Put #ifile, , "Fim de Processamento" & vbCrLf
    
    End
    
ErroMain:
   Put #ifile, , "erroMain: " & Err.Number & " - " & Err.Description & gConClient & vbCrLf & vbCrLf
   End

End Sub



Private Sub ImprimirCracha(Matricula As Integer, DtVenc As String, Impressora As String)
Dim rstParametro As ADODB.Recordset
Dim sSql As String
Dim sSql2 As String
Dim rstCRACHA As ADODB.Recordset
Dim rstFuncionario As ADODB.Recordset
Dim vDias() As String
Dim vHorarios() As String
Dim I As Integer
Dim Tam As Integer
Dim Lin As Integer
Dim NrCracha As String
Dim x As Printer
Dim ALTURA2 As Integer
Dim rstFotoPessoa As ADODB.Recordset
Dim strSql As String

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
For I = 8 To 14
    Printer.DrawMode = I

    Printer.CurrentX = 191
    Printer.CurrentY = (ALTURA2) + 1
    Printer.Font = "Lucida Sans Unicode"
    Printer.FontBold = True
    Printer.FontSize = 5
    Printer.Print "TWI"

    Printer.Line (190, ALTURA2)-(194, ALTURA2 + 4), vbGreen, BF
    ALTURA2 = ALTURA2 + 4
Next I

Printer.KillDoc

Printer.FontTransparent = True

sSql = "EXEC SP_ATUALIZA_NR_CRACHA_IMPRESSO " & _
        Matricula & ", " & _
        IIf(DtVenc = "", "null", "'" & DtVenc & "'")

sSql2 = sSql

Set rstCRACHA = ClientSideCon.Execute(sSql)

If rstCRACHA.RecordCount > 0 Then
   NrCracha = rstCRACHA!NR_CRACHA
Else
   End
End If

strSql = "select foto_funcionario from tb_foto_funcionario where cd_funcionario = " & Matricula
Set rstFotoPessoa = gCnFoto.Execute(strSql)

If rstFotoPessoa.RecordCount > 0 Then
    If RecuperaFoto(rstFotoPessoa.Fields(0)) Then
       DoEvents
    End If
End If
rstFotoPessoa.Close


sSql = "EXEC SP_RECUPERA_FUNCIONARIOS 0, 0, '%', '', " & Matricula
Set rstFuncionario = ClientSideCon.Execute(sSql)

If rstFuncionario!E_Tipo_DE = "FUNCIONÁRIO" Then
      
      'Imprimir 47, 16, "Arial", 7, False, False, "Nome:"
      Imprimir 51, 17, "Arial", 16, True, False, Trim(rstFuncionario!E_Nome_Abrev_NU)
      'Imprimir 59, 16, "Arial", 7, False, False, "Setor:"
      Imprimir 63, 17, "Arial", 14, False, False, Trim(rstFuncionario!E_Setor_DE)
      Printer.PaintPicture LoadPicture(App.Path & "\cr_func_1.bmp"), 0, 0, 54, 86
      Printer.PaintPicture LoadPicture("pictemp.bmp"), 23, 14, 22, 30
      
      Printer.NewPage
      'Imprimir 15, 13, "Arial", 7, False, False, "Nome:"
      Imprimir 18, 14, "Arial", 8, False, False, Trim(rstFuncionario!E_Nome_Funcionario_DE)
      'Imprimir 21, 13, "Arial", 7, False, False, "Cargo/Função:"
      Imprimir 24, 14, "Arial", 8, False, False, rstFuncionario!E_Cargo_DE
      'Imprimir 27, 13, "Arial", 7, False, False, "Matrícula:"
      Imprimir 30, 14, "Arial", 8, False, False, rstFuncionario!EE_Matricula_NU
      'Imprimir 33, 13, "Arial", 7, False, False, "Tipo Sangüineo:"
      Imprimir 36, 14, "Arial", 8, False, False, IIf(IsNull(rstFuncionario!E_Sangue_NU), "", rstFuncionario!E_Sangue_NU)
      Imprimir 73, 3, "C39P36DlTt", 26, False, False, "!" & Format(NrCracha, "000000000") & "!"
      Imprimir 82, 22, "Arial", 6, False, False, Format(NrCracha, "000000000")
      Printer.PaintPicture LoadPicture(App.Path & "\cr_func_2.bmp"), 0, 0, 54, 85
      
      Printer.EndDoc

Else

   sSql = "SELECT TRAB_SEGUNDA , HH_ENTRA_SEGUNDA, HH_SAI_SEGUNDA, TRAB_TERCA, HH_ENTRA_TERCA, HH_SAI_TERCA, TRAB_QUARTA, HH_ENTRA_QUARTA, HH_SAI_QUARTA, TRAB_QUINTA, HH_ENTRA_QUINTA, HH_SAI_QUINTA, TRAB_SEXTA, HH_ENTRA_SEXTA, HH_SAI_SEXTA, TRAB_SABADO, HH_ENTRA_SABADO, HH_SAI_SABADO, TRAB_DOMINGO, HH_ENTRA_DOMINGO, HH_SAI_DOMINGO FROM TB_FUNCIONARIO WHERE CD_FUNCIONARIO = " & Matricula
   
   Set rstParametro = gCn.Execute(sSql)
   
   Tam = 0
   For I = 0 To 18 Step 3
      If rstParametro.Fields(I) <> 0 Then
      
         Tam = Tam + 1
         ReDim Preserve vDias(Tam)
         ReDim Preserve vHorarios(Tam)
         
         Select Case I
         Case 0
            vDias(Tam) = "Segunda"
         Case 3
            vDias(Tam) = "Terça"
         Case 6
            vDias(Tam) = "Quarta"
         Case 9
            vDias(Tam) = "Quinta"
         Case 12
            vDias(Tam) = "Sexta"
         Case 15
            vDias(Tam) = "Sábado"
         Case 18
            vDias(Tam) = "Domingo"
         End Select
         
         vHorarios(Tam) = Mid(rstParametro.Fields(I + 1), 1, 2) & ":" & Mid(rstParametro.Fields(I + 1), 3, 2) & " às " & Mid(rstParametro.Fields(I + 2), 1, 2) & ":" & Mid(rstParametro.Fields(I + 2), 3, 2)
      
      End If
   Next
   
   rstParametro.Close

   Printer.PaintPicture LoadPicture(App.Path & "\cr_conc_1.bmp"), 0, 0, 54, 86
   Printer.PaintPicture LoadPicture("pictemp.bmp"), 22, 8, 22, 28
   
   
   IMPRIMIR2 38, 13, "Times New Roman", 8, False, False, "Nome: "
   IMPRIMIR2 41, 13, "Times New Roman", 16, True, False, Trim(rstFuncionario!E_Nome_Abrev_NU)
   IMPRIMIR2 51, 13, "Times New Roman", 8, False, False, "Nome Completo: "
   IMPRIMIR2 54, 13, "Times New Roman", 8, True, False, Trim(rstFuncionario!E_Nome_Funcionario_DE)
   IMPRIMIR2 60, 13, "Times New Roman", 8, False, False, "Área: "
   IMPRIMIR2 63, 13, "Times New Roman", 8, True, False, Trim(rstFuncionario!E_Setor_DE)
   IMPRIMIR2 69, 13, "Times New Roman", 8, False, False, "Categoria: "
   IMPRIMIR2 72, 13, "Times New Roman", 8, True, False, Trim(rstFuncionario!E_Cargo_DE)
   IMPRIMIR2 79, 13, "Times New Roman", 12, False, False, "Validade: "
   IMPRIMIR2 79, 31, "Times New Roman", 12, True, False, DtVenc
   
   Printer.NewPage
   Printer.PaintPicture LoadPicture(App.Path & "\cr_conc_2.bmp"), 0, 0, 54, 86
   Lin = 8
   For I = 1 To UBound(vDias)
      IMPRIMIR2 Lin, 12, "MS Sans Serif", 8, True, False, vDias(I)
      IMPRIMIR2 Lin, 32, "MS Sans Serif", 8, True, False, vHorarios(I)
      Lin = Lin + 3.5
   Next
   
   Imprimir 73, 3, "C39P36DlTt", 26, False, False, "!" & Format(NrCracha, "000000000") & "!"
   Imprimir 82, 22, "Arial", 6, False, False, Format(NrCracha, "000000000")
    
   Printer.EndDoc
End If

Exit Sub

Erro:
    Put #ifile, , "ErroImprimirCracha: " & Err.Number & " - " & Err.Description & vbCrLf & vbCrLf

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

Public Sub IMPRIMIR2(ByVal Lin As Currency, ByVal Col As Currency, ByVal Nome As String, ByVal Tamanho As Integer, ByVal Ng As Boolean, ByVal It As Boolean, ByVal mSg As String)
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
   Put #ifile, , "ErroImprimir2: " & Err.Number & " - " & Err.Description & gConClient & vbCrLf & vbCrLf
   End
End Sub


Public Function RecuperaFoto(ByVal objcoluna As Object) As Boolean
Dim Arquivo As Integer
Dim divisao As Integer
Dim vetor() As Byte
Dim resto As Long
Dim tamanhofoto As Long
Dim I As Integer

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

For I = 1 To divisao
   ReDim vetor(TamFoto)
   vetor() = objcoluna.GetChunk(TamFoto)
   Put Arquivo, , vetor()
Next I

Close Arquivo

RecuperaFoto = True

Exit Function

ErroFoto:
   Put #ifile, , "ErroRecuperaFoto: " & Err.Number & " - " & Err.Description & gConClient & vbCrLf & vbCrLf

   End

End Function



