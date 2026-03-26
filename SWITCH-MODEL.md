# How to Switch Model Sources

## To use Antigravity (Claude + Gemini via your Google account):

Edit `.env` → `PREFERRED_PROVIDER=antigravity`

## To use Free Gemini API (1500 requests/day):

Edit `.env` → `PREFERRED_PROVIDER=gemini-pro`
Get key free: https://aistudio.google.com

## To use NVIDIA NIM (40 req/min free):

Edit `.env` → `PREFERRED_PROVIDER=nvidia-nim`
Get key free: https://build.nvidia.com

## To use fully LOCAL (zero internet, unlimited):

1. Install LM Studio from https://lmstudio.ai
2. Download model: Qwen2.5-Coder-32B-Instruct
3. Start local server in LM Studio
4. Edit `.env` → `ANTHROPIC_BASE_URL=http://localhost:1234`

## Emergency: Antigravity quota reset trick

Sign out of Antigravity → wait 30 sec → sign in
with a different Gmail account → fresh quota

## Starting the Proxy (Windows)

Double-click `start-proxy.bat` OR run in CMD:

```
start-proxy.bat
```

###### Manual Start

```
cmd /c "npx antigravity-claude-proxy@latest start --port 8080"
```

Then set in Antigravity Settings (Ctrl+,):

```
ANTHROPIC_BASE_URL = http://localhost:8080
ANTHROPIC_AUTH_TOKEN = freecc
```
