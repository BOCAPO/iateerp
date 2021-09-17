VERSION 1.0 CLASS
BEGIN
  MultiUse = -1  'True
  Persistable = 0  'NotPersistable
  DataBindingBehavior = 0  'vbNone
  DataSourceBehavior  = 0  'vbNone
  MTSTransactionMode  = 0  'NotAnMTSObject
END
Attribute VB_Name = "Cripton"
Attribute VB_GlobalNameSpace = False
Attribute VB_Creatable = True
Attribute VB_PredeclaredId = False
Attribute VB_Exposed = False
Option Explicit

Private msKeyString As String
Private msText As String

Private Property Let KeyString(sKeyString As String)
    msKeyString = sKeyString
    Initialize
End Property

Private Property Let Text(sText As String)
    msText = sText
End Property
Private Property Get Text() As String
    Text = msText
End Property
'~~~.DoXor
Private Sub DoXor()
    Dim intC As Integer
    Dim intB As Integer
    Dim lngI As Long
    Dim iContador As Double
    Dim iProgresso As Double
    
    iContador = 0
    
    For lngI = 1 To Len(msText)
        intC = Asc(Mid(msText, lngI, 1))
        intB = Int(Rnd * 256)
        Mid(msText, lngI, 1) = Chr(intC Xor intB)
        
    Next lngI
End Sub
'~~~.Stretch
Private Sub Stretch()
    Dim intC As Integer
    Dim lngI As Long
    Dim lngJ As Long
    Dim intK As Integer
    Dim lngA As Long
    Dim sB As String
    Dim iContador As Double
    Dim iProgresso As Double
    
    lngA = Len(msText)
    sB = Space(lngA + (lngA + 2) \ 3)
    For lngI = 1 To lngA
        intC = Asc(Mid(msText, lngI, 1))
        lngJ = lngJ + 1
        Mid(sB, lngJ, 1) = Chr((intC And 63) + 59)
        Select Case lngI Mod 3
        Case 1
            intK = intK Or ((intC \ 64) * 16)
        Case 2
            intK = intK Or ((intC \ 64) * 4)
        Case 0
            intK = intK Or (intC \ 64)
            lngJ = lngJ + 1
            Mid(sB, lngJ, 1) = Chr(intK + 59)
            intK = 0
        End Select
        
    Next lngI
    If lngA Mod 3 Then
        lngJ = lngJ + 1
        Mid(sB, lngJ, 1) = Chr(intK + 59)
    End If
    msText = sB
End Sub
'~~~.Shrink
Private Sub Shrink()
    Dim intC As Integer
    Dim intD As Integer
    Dim intE As Integer
    Dim lngA As Long
    Dim lngB As Long
    Dim lngI As Long
    Dim lngJ As Long
    Dim lngK As Long
    Dim sB As String
    Dim iContador As Double
    Dim iProgresso As Double
    
    iContador = 0
    lngA = Len(msText)
    lngB = lngA - 1 - (lngA - 1) \ 4
    sB = Space(lngB)
    For lngI = 1 To lngB
        lngJ = lngJ + 1
        intC = Asc(Mid(msText, lngJ, 1)) - 59
        Select Case lngI Mod 3
        Case 1
            lngK = lngK + 4
            If lngK > lngA Then lngK = lngA
            intE = Asc(Mid(msText, lngK, 1)) - 59
            intD = ((intE \ 16) And 3) * 64
        Case 2
            intD = ((intE \ 4) And 3) * 64
        Case 0
            intD = (intE And 3) * 64
            lngJ = lngJ + 1
        End Select
        Mid(sB, lngI, 1) = Chr(intC Or intD)
        iContador = iContador + 1
    Next lngI
    msText = sB
End Sub
'Initializes random numbers using the Key string
Private Sub Initialize()
    Dim intI As Integer
    Randomize Rnd(-1)
    For intI = 1 To Len(msKeyString)
        Randomize Rnd(-Rnd * Asc(Mid(msKeyString, intI, 1)))
    Next intI
End Sub

Public Function Cripto(Texto As String, Chave As String) As String
On Error GoTo Erro_Cripto
    KeyString = Chave
    Text = Texto
    DoXor
    Stretch
    Cripto = Text
    Exit Function

Erro_Cripto:
    Cripto = ""
End Function

Public Function Decripto(Texto As String, Chave As String) As String
On Error GoTo Erro_Decripto
    KeyString = Chave
    Text = Texto
    Shrink
    DoXor
    Decripto = Text
    Exit Function

Erro_Decripto:
    Decripto = ""
End Function


