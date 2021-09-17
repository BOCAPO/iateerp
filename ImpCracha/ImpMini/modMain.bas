Attribute VB_Name = "modMain"
Option Explicit

Global iFile As Integer

Sub Main()
Dim Linha As String
Dim Lixo As String
Dim NomeImp As String
Dim x As Printer
Dim ArqConf As Integer

On Error GoTo Erro

iFile = FreeFile()
Open App.Path & "\log.log" For Binary Access Write As #iFile
Put #iFile, , "Iniciando processamento" & vbCrLf

Open App.Path & "\Config.txt" For Binary Access Read As #3

Input #3, Lixo
Input #3, Lixo
Input #3, Lixo
Input #3, Lixo
Input #3, Lixo
Input #3, Lixo
Input #3, NomeImp

Close #3

Put #iFile, , "Vai procurar a impressora: " & NomeImp & vbCrLf

For Each x In Printers
   Put #iFile, , "Impressora: " & x.DeviceName & vbCrLf
   If x.DeviceName = NomeImp Then
      Set Printer = x
      Put #iFile, , "Impressora encontrada " & vbCrLf
      Exit For
   End If
Next
Put #iFile, , "Termino de procura da impressora" & vbCrLf

Printer.FontName = "COURIER NEW"
Printer.FontSize = "8"

Put #iFile, , "Abrindo arquivo de texto" & vbCrLf
Open App.Path & "\texto.txt" For Binary Access Read As #4

Put #iFile, , "Iniciando Impressao!" & vbCrLf
Do While Not EOF(4)
   
   Line Input #4, Linha
   Printer.Print Linha
Loop

Put #iFile, , "Fechando arquivo de texto" & vbCrLf
Close #4

Printer.EndDoc

Put #iFile, , "Fim de Processamento" & vbCrLf
Close #iFile

Exit Sub
Erro:
   Put #iFile, , Err.Number & " - " & Err.Description & vbCrLf & vbCrLf
   Close #iFile

End Sub

