@SET /p name=输入压缩的lst文件主名:
@IF "%name%" == "" GOTO end
@IF NOT EXIST %name%.lst GOTO end

"c:\Program Files\WinRAR\WinRAR.exe" a  %name% @%name%.lst

:end
pause