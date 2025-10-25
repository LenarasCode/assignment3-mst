@echo off
echo ========================================
echo   Assignment 3 - GitHub Push Script
echo ========================================
echo.
echo Step 1: Create repository on GitHub
echo 1. Go to https://github.com
echo 2. Click "+" button and select "New repository"
echo 3. Name: assignment3-mst
echo 4. Description: Assignment 3 - MST Algorithms
echo 5. DO NOT initialize with README/gitignore
echo 6. Click "Create repository"
echo.
echo Step 2: Push your code
echo Running git push command...
echo.
echo When prompted for credentials:
echo Username: LenarasCode
echo Password: L3n@r@_2025_G!tH@b
echo.
git push -u origin main
echo.
echo If successful, your repository will be available at:
echo https://github.com/LenarasCode/assignment3-mst
echo.
pause
