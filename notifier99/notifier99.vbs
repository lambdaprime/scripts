Dim WshShell, cwd
Set WshShell = CreateObject("WScript.Shell")
cwd = CreateObject("Scripting.FileSystemObject").GetParentFolderName(WScript.ScriptFullName)
WshShell.Run chr(34) & cwd & "\run.bat" & Chr(34), 0 
Set WshShell = Nothing