'VB script for creating windows shortcut

args = WScript.Arguments.Count
If args < 3 then
  WScript.Echo "usage: <shortcat target> <shortcut name> <icon location>"
  WScript.Quit   
End If

'Find curret user's desktop path
dim WSHShell, desktop, pathstring, objFSO
set objFSO=CreateObject("Scripting.FileSystemObject")
Set WSHshell = CreateObject("WScript.Shell")
desktop = WSHShell.SpecialFolders("Desktop")
desktopPath = objFSO.GetAbsolutePathName(desktop)
'WScript.Echo pathstring

shortcutName = WScript.Arguments.Item(1)
Set oWS = WScript.CreateObject("WScript.Shell")
   sLinkFile = desktopPath & "\" & shortcutName  & ".LNK"
   Set oLink = oWS.CreateShortcut(sLinkFile) 
   oLink.TargetPath = WScript.Arguments.Item(0) 
   '    oLink.Arguments = ""
   '    oLink.Description = "MyProgram"
   '    oLink.HotKey = "ALT+CTRL+F"
       oLink.IconLocation = WScript.Arguments.Item(2)   
       oLink.WindowStyle = "1"
   '    oLink.WorkingDirectory = "C:\Program Files\MyApp"
   oLink.Save
