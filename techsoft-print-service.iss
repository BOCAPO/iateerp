#define MajorVersion "4"
#define MinorVersion "0"
#define ReleaseVersion ""
#define Versao MajorVersion + "." + MinorVersion + ReleaseVersion
#define MyAppName "TechSoft Print Service"
#define MyAppVerName MyAppName + " " + Versao
#define MyAppPublisher "Techsoft LTDA"
#define MyAppURL "http://www.iateclubedebrasilia.com.br"

;Diretorio contendo os arquivos para gerar o setup
#define SetupSrc "E:\NetBeansProjects\iate\dist\carteirinha"

[Setup]
AppName={#MyAppName}
AppVerName={#MyAppVerName}
AppPublisher={#MyAppPublisher}
AppPublisherURL={#MyAppURL}
AppSupportURL={#MyAppURL}
AppUpdatesURL={#MyAppURL}
DisableDirPage=yes
DefaultDirName="C:\TechsoftPS"
DisableProgramGroupPage=yes
OutputBaseFilename=setup-techsoft-print-service-{#Versao}
OutputDir=dist
Compression=lzma
SolidCompression=yes
PrivilegesRequired=admin
;Win2000 ou superior
MinVersion=0,5.0.2195
VersionInfoVersion=3.5.0.0
;atualizar o sistema para validar as variaveis de ambiente JAVA_HOME e PATH que esse setup altera
ChangesEnvironment=yes


[Languages]
Name: brazilianportuguese; MessagesFile: compiler:Languages\BrazilianPortuguese.isl

[Files]
Source: {#SetupSrc}\*; DestDir: "C:\TechsoftPS"; MinVersion: 0, 5.0.2195; Flags: ignoreversion recursesubdirs createallsubdirs
;Source: {#SetupSrc}\vbrun60sp6.exe*; DestDir: "C:\TechsoftPS"; MinVersion: 0, 5.0.2195; Flags: ignoreversion deleteafterinstall

[Run]
Filename: "{app}\InstallService-NT.bat"; WorkingDir: "{app}"; StatusMsg: "Instalando Serviço de Impressão Techsoft..."; Flags: shellexec
;Filename: "{app}\vbrun60sp6.exe"; WorkingDir: "{app}"; Parameters: "/Q"
Filename: "{cmd}"; Parameters: "/c net start TechSoftCarteirinha"; Flags: shellexec

[UninstallRun]
Filename: "{cmd}"; Parameters: "/c net stop TechSoftCarteirinha"; Flags: shellexec waituntilterminated
Filename: "{app}\UninstallService-NT.bat"; WorkingDir: "{app}"; StatusMsg: "Removendo Serviço de Impressão Techsoft..."; Flags: shellexec waituntilterminated

[Code]
{*
 * http://msdn2.microsoft.com/en-us/library/ms686206.aspx
 *
 * BOOL WINAPI SetEnvironmentVariable(
 * LPCTSTR lpName,
 * LPCTSTR lpValue
 *);
 *}
function SetEnvironmentVariable(lpName, lpValue: String): Boolean; external 'SetEnvironmentVariableA@kernel32.dll stdcall';

{*
 * Informa qual diretório está instalado o JDK
 *
 * @param JavaHome - Ponteiro para string que vai receber o diretório onde está instalado o JDK
 * @return - True se foi encontrada um JDK ou False caso contrário
 *}
function GetJavaHome(var JavaHome: String): Boolean;
var
    sVersao: String;//Recebe a string de versao da registry
    iVersaoMaior: Integer;//Separa a versao maior da string de versao
    iVersaoMenor: Integer;//Separa a versao menor da string de versao
    sJavaHome: String;//Path onde esta a maquina virtual
begin
    {*
     * Verifica se ha um JDK instalado
     *}
    if not RegQueryStringValue(HKEY_LOCAL_MACHINE, 'SOFTWARE\JavaSoft\Java Runtime Environment', 'CurrentVersion', sVersao) then
    begin
        JavaHome := 'Não foi encontrada uma instalação do Java Runtime Environment neste computador. Deve ser instalada a versão 1.6 ou superior.';
        Result := False;
        Exit;
    end;

    {*
     * Verifica se a versao eh maior ou igual a 1.6
     *}
    iVersaoMaior := StrToInt(Copy(sVersao, 1, 1));
    iVersaoMenor := StrToInt(Copy(sVersao, 3, 1));

    if iVersaoMaior < 1 then
    begin
        JavaHome := 'A versão do Java deve ser 1.6 ou superior. Sua versão é: ' + IntToStr(iVersaoMaior) + '.' + IntToStr(iVersaoMenor);
        Result := False;
        Exit;
    end;

    if iVersaoMenor < 6 then
    begin
        JavaHome := 'A versão do Java deve ser 1.6 ou superior. Sua versão é: ' + IntToStr(iVersaoMaior) + '.' + IntToStr(iVersaoMenor);
        Result := False;
        Exit;
    end;

    {*
     * Busca o PATH do JDK
     *}
    if not RegQueryStringValue(HKEY_LOCAL_MACHINE, 'SOFTWARE\JavaSoft\Java Runtime Environment\' + sVersao, 'JavaHome', sJavaHome) then
    begin
        JavaHome := 'Não foi encontrado o local de instalação do Java Runtime Environment neste computador. Deve ser instalada a versão 1.6 ou superior.';
        Result := False;
        Exit;
    end;

    JavaHome := sJavaHome;
    result := True;
end;

{*
 * So permite instalar se tiver JVM 1.6 ou superior instalada na maquina
 *}
function NextButtonClick(CurPageID: Integer): Boolean;
var
    sJavaHome: String;//Recebe o JavaHome ou a mensagem de erro caso ocorra
    sPath: String;//Recebe a variavel de ambiente PATH
begin

    //Verifica se a pagina atual é a página onde o usuario aperta o botão Instalar
    if(CurPageID = wpWelcome) then
    begin
            if(not GetJavaHome(sJavaHome))then
            begin
                MsgBox(sJavaHome, mbInformation, MB_OK);
                Result := False;
            end
            else
            begin
                RegWriteStringValue(HKEY_LOCAL_MACHINE, 'SYSTEM\ControlSet001\Control\Session Manager\Environment', 'JAVA_HOME', sJavaHome);
                RegQueryStringValue(HKEY_LOCAL_MACHINE, 'SYSTEM\ControlSet001\Control\Session Manager\Environment', 'Path', sPath);
                RegWriteStringValue(HKEY_LOCAL_MACHINE, 'SYSTEM\ControlSet001\Control\Session Manager\Environment', 'Path', '%JAVA_HOME%\bin;' + sPath);

                SetEnvironmentVariable('JAVA_HOME', sJavaHome);
                SetEnvironmentVariable('Path', sJavaHome + '\bin;' + sPath);
                Result := True;
            end;

    end
    else
        //Retorna TRUE para todas as outras paginas que não foram identificadas acima
        Result := True;
end;
