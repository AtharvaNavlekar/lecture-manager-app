@echo off
echo Starting free AI proxy stack...

:: Start Antigravity proxy in background
start "Antigravity-Proxy" cmd /k "npx antigravity-claude-proxy@latest start --port 8080"
echo Antigravity proxy starting on port 8080...

:: Wait for it to be ready
timeout /t 5 /nobreak > nul

echo.
echo Proxy stack launched!
echo Primary:  http://localhost:8080 (Antigravity)
echo.
echo Set these in your environment:
echo   ANTHROPIC_BASE_URL=http://localhost:8080
echo   ANTHROPIC_AUTH_TOKEN=freecc
echo.
echo Use Ctrl+C in the Antigravity-Proxy window to stop it.
pause
