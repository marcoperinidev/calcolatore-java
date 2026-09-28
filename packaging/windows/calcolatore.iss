#define MyAppVersion GetEnv("APP_VERSION")

[Setup]
AppId=CalcolatoreJavaVolta
AppName=Calcolatore
AppVersion={#MyAppVersion}
DefaultDirName={autopf}\Calcolatore
DefaultGroupName=Calcolatore
OutputDir=..\..\dist
OutputBaseFilename=Calcolatore-Setup-{#MyAppVersion}
Compression=lzma
SolidCompression=yes
WizardStyle=modern
PrivilegesRequired=admin

[Files]
Source: "..\..\dist-app\Calcolatore\*"; DestDir: "{app}"; Flags: ignoreversion recursesubdirs createallsubdirs

[Icons]
Name: "{group}\Calcolatore"; Filename: "{app}\Calcolatore.exe"
Name: "{autodesktop}\Calcolatore"; Filename: "{app}\Calcolatore.exe"

[Run]
Filename: "{app}\Calcolatore.exe"; Description: "Avvia Calcolatore"; Flags: postinstall nowait skipifsilent