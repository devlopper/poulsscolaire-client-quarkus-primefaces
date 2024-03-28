@echo off
REM Obtenez la date et l'heure actuelles
for /f "tokens=1-3 delims=/ " %%a in ('date /t') do set "date=%%c%%b%%a"
for /f "tokens=1-3 delims=: " %%a in ('time /t') do set "time=%%a%%b%%c"

REM Générer un nombre aléatoire entre 1 et 99
set /a "randomNumber=%RANDOM% %% 99 + 1"

REM Affichez la date et l'heure formatées
set tag=%date%%time%%randomNumber%

echo Entrez les caractères suivants : %tag%
set /p input=

if %input% == %tag% (
    call build-tag.bat %tag%
	call publish-tag.bat %tag%
	echo Le tag de votre image est : %tag%
	echo %tag% >> tags.txt
) else (
    echo Les caractères saisient ne correspondent pas.
)