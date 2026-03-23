[Setup]
AppName=Pigeon.Nest
AppVersion=1.1
AppPublisher=Sixty Six Studios
DefaultDirName={pf}\Pigeon.Nest
DefaultGroupName=Pigeon.Nest
OutputDir=output
OutputBaseFilename=Pigeon.Nest_Setup
Compression=lzma
SolidCompression=yes
UninstallDisplayIcon={app}\Pigeon.Nest.exe
SetupIconFile=icon.ico

[Files]
Source: "Pigeon.Nest.exe"; DestDir: "{app}"; Flags: ignoreversion
Source: "JAR\Pigeon.Nest.jar"; DestDir: "{app}"; Flags: ignoreversion
Source: "icon.ico"; DestDir: "{app}"; Flags: ignoreversion
Source: "JRE*"; DestDir: "{app}\JRE"; Flags: recursesubdirs createallsubdirs

[Icons]
Name: "{group}\Pigeon.Nest"; Filename: "{app}\Pigeon.Nest.exe"; IconFilename: "{app}\icon.ico"
Name: "{commondesktop}\Pigeon.Nest"; Filename: "{app}\Pigeon.Nest.exe"; IconFilename: "{app}\icon.ico"

[Run]
Filename: "{app}\Pigeon.Nest.exe"; Description: "Launch Pigeon.Nest"; Flags: nowait postinstall skipifsilent

