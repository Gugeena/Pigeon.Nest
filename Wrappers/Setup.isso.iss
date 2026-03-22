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

[Files]
Source: "Pigeon.Nest.exe"; DestDir: "{app}"; Flags: ignoreversion

[Icons]
Name: "{group}\Pigeon.Nest"; Filename: "{app}\Pigeon.Nest.exe"
Name: "{commondesktop}\Pigeon.Nest"; Filename: "{app}\Pigeon.Nest.exe"

[Run]
Filename: "{app}\Pigeon.Nest.exe"; Description: "Launch Pigeon.Nest"; Flags: nowait postinstall skipifsilent

