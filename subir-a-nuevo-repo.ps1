# Script: subir este proyecto a un NUEVO repositorio en tu cuenta de GitHub
# Uso: .\subir-a-nuevo-repo.ps1 -NuevaRepoUrl "https://github.com/TU_USUARIO/NOMBRE_REPO.git"

param(
    [Parameter(Mandatory=$true)]
    [string]$NuevaRepoUrl
)

$ErrorActionPreference = "Stop"
Set-Location $PSScriptRoot

Write-Host "Remoto actual (origin):" -ForegroundColor Cyan
git remote -v

Write-Host "`nAgregando remoto 'nuevo' apuntando a tu nuevo repo..." -ForegroundColor Cyan
git remote remove nuevo 2>$null
git remote add nuevo $NuevaRepoUrl

Write-Host "Subiendo rama master al nuevo repo..." -ForegroundColor Cyan
git push -u nuevo master

Write-Host "`nListo. Tu proyecto esta en el nuevo repo." -ForegroundColor Green
Write-Host "Para clonar en otra PC: git clone $NuevaRepoUrl" -ForegroundColor Yellow
