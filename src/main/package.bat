@SET /p name=����ѹ����lst�ļ�����:
@IF "%name%" == "" GOTO end
@IF NOT EXIST %name%.lst GOTO end

"c:\Program Files\WinRAR\WinRAR.exe" a  %name% @%name%.lst

:end
pause