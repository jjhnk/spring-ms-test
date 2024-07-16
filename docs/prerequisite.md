# Requirements

- [Requirements](#requirements)
  - [PowerShell](#powershell)
    - [Installation](#installation)
  - [Chocolatey](#chocolatey)
  - [WSL](#wsl)
  - [Docker](#docker)
  - [Visual Studio Code](#visual-studio-code)
    - [VSCode 관련 extension](#vscode-관련-extension)
  - [Java](#java)
  - [Gradle](#gradle)
  - [Minikube](#minikube)
  - [Maven](#maven)
  - [DBeaver](#dbeaver)
- [Optional](#optional)
  - [Git For Windows](#git-for-windows)
  - [Nexus OSS](#nexus-oss)
  - [Terminal](#terminal)

## PowerShell

[PowerShell 설치 가이드](https://learn.microsoft.com/en-us/powershell/scripting/install/installing-powershell-on-windows?view=powershell-7.4&viewFallbackFrom=powershell-7&WT.mc_id=THOMASMAURER-blog-thmaure)

### Installation

```pwsh
# run administrative shell
winget search Microsoft.PowerShell
winget install --id Microsoft.Powershell --source winget
```

설치된 PowerShell은 아래의 경로에 저장됩니다:

> %ProgramFiles%/PowerShell/7

VSCode의 terminal profile에 설치된 PowerShell을 추가하려면 다음과 같이 설정합니다:

```json
[CTRL+SHIFT+P]
> Preferences: Open User Settings (JSON)

"terminal.integrated.profiles.windows": {
  "PowerShell": {
    "path": "${env:programfiles}\\PowerShell\\7\\pwsh.exe",
    "icon": "terminal-powershell"
  },
  ...
}

"terminal.integrated.defaultProfile.windows": "PowerShell"
```

## Chocolatey

[Chocolatey 설치 가이드](https://chocolatey.org/install)

```pwsh
# run administrative shell
Set-ExecutionPolicy Bypass -Scope Process -Force; [System.Net.ServicePointManager]::SecurityProtocol = [System.Net.ServicePointManager]::SecurityProtocol -bor 3072; iex ((New-Object System.Net.WebClient).DownloadString('https://community.chocolatey.org/install.ps1'))
```

## WSL

[WSL 설치 가이드](https://learn.microsoft.com/en-us/windows/wsl/install)

```pwsh
# run administrative shell
wsl --install
wsl -l -o
wsl --install -d Ubuntu
```

## Docker

[Docker 설치 가이드](https://www.docker.com/products/docker-desktop/)

```pwsh
# run administrative shell
choco install docker-desktop
```

⚠️ Chocolatey를 통해 Docker Desktop을 설치하면 업데이트 시 문제가 발생할 수 있습니다. [공식 사이트](https://www.docker.com/products/docker-desktop/)에서 다운로드 받아 설치하는 것도 가능합니다.

## Visual Studio Code

[Visual Studio Code 설치 가이드](https://code.visualstudio.com/download)

```pwsh
# run administrative shell
choco install vscode
```

### VSCode 관련 extension

아래는 필수 extension 목록입니다:

```txt
1. Extension Pack for Java, Debugger For Java, Test Runner For Java, Maven For Java, Project Manager For Java, Gradle For Java, Language Support for Java(TM) by RedHat
2. Kubernetes, Bridge To Kubernetes
3. Docker
4. WSL
5. Spring Boot Dashboard, Spring Boot Tools, Spring Boot Extension Pack
```

한 번에 설치하려면 아래 명령어를 사용합니다:

```pwsh
code --install-extension formulahendry.code-runner
code --install-extension dotjoshjohnson.xml
code --install-extension formulahendry.vscode-mysql
code --install-extension madhavd1.javadoc-tools
code --install-extension mindaro.mindaro
code --install-extension ms-azuretools.vscode-docker
code --install-extension ms-edgedevtools.vscode-edge-devtools
code --install-extension ms-kubernetes-tools.vscode-aks-tools
code --install-extension ms-kubernetes-tools.vscode-kubernetes-tools
code --install-extension ms-vscode-remote.remote-containers
code --install-extension ms-vscode-remote.remote-ssh
code --install-extension ms-vscode-remote.remote-ssh-edit
code --install-extension ms-vscode-remote.remote-wsl
code --install-extension ms-vscode.powershell
code --install-extension naco-siren.gradle-language
code --install-extension redhat.java
code --install-extension redhat.vscode-xml
code --install-extension redhat.vscode-yaml
code --install-extension ritwickdey.liveserver
code --install-extension vmware.vscode-boot-dev-pack
code --install-extension vmware.vscode-spring-boot
code --install-extension vscjava.vscode-gradle
code --install-extension vscjava.vscode-java-debug
code --install-extension vscjava.vscode-java-dependency
code --install-extension vscjava.vscode-java-pack
code --install-extension vscjava.vscode-java-test
code --install-extension vscjava.vscode-lombok
code --install-extension vscjava.vscode-maven
code --install-extension vscjava.vscode-spring-boot-dashboard
code --install-extension vscjava.vscode-spring-initializr
```

## Java

[Java 설치 가이드](https://adoptium.net/temurin/releases/)

Java 21 버전을 최소 사용합니다.

```pwsh
# run administrative shell
choco install openjdk
# choco install openjdk --version 21.0.2
```

Temurin 또는 Microsoft Build of OpenJDK를 사용해야 하는 경우 아래를 참조합니다.

```pwsh
# run administrative shell
# install temurin java
choco install temurin
# install Microsoft build of OpenJDK
choco install microsoft-openjdk
```

## Gradle

[Gradle 설치 가이드](https://gradle.org/)

Gradle 8.8 버전을 사용합니다.

```pwsh
# run administrative shell
choco install gradle
# choco install gradle --version 8.8
```

## Minikube

[Kubernetes Minikube 설치 가이드](https://minikube.sigs.k8s.io/docs/)

```pwsh
# run administrative shell
choco install minikube
# choco install kubernetes-cli
```

사용되는 명령어는 아래와 같습니다. 해당 내용은 나중에 다시 설명합니다.

```txt
minikube start
minikube stop
minikube tunnel
minikube -p minikube docker-env --shell powershell | Invoke-Expression
minikube -p minikube docker-env -u --shell powershell | Invoke-Expression
```

## Maven

Maven은 패키지 관리 도구입니다. 본 프로젝트에서는 API, 유틸 계층을 분할하기 위해 Maven을 사용합니다.

```pwsh
# run administrative shell
choco install maven
```

⚠️ 설치 후 아래 경로가 생성되었는지 확인합니다.

> Path: %UserProfile%/.m2

## DBeaver

[DBeaver 설치 가이드](https://dbeaver.io/)

DBeaver는 다양한 데이터베이스 관리 및 개발을 지원하는 통합 개발 환경(IDE)입니다. MongoDB, MySQL, PostgreSQL이 사용되므로 DBeaver를 사용하는 것을 권장합니다.

```pwsh
# run administrative shell
choco install dbeaver
```

# Optional

## Git For Windows

[Git For Windows 설치 가이드](https://git-scm.com/download/win)

Git 사용은 드물지만, 가끔 Git Bash를 사용합니다.

```pwsh
# run administrative shell
choco install git
```

## Nexus OSS

[Nexus OSS 설치 가이드](https://www.sonatype.com/products/sonatype-nexus-oss)

Nexus OSS는 소프트웨어 컴포넌트를 효율적으로 저장, 관리, 배포할 수 있도록 설계된 아티팩트 저장소 관리자입니다.

주요 기능:

1. **아티팩트 저장소**: 다양한 형식의 아티팩트를 저장하고 관리.
2. **프로세스 자동화**: CI/CD 파이프라인과 통합.
3. **보안 및 라이선스 관리**: 아티팩트의 보안 취약점 및 라이선스 문제 모니터링.
4. **확장성**: 플러그인 아키텍처를 통한 기능 확장.
5. **유연한 배포**: 내부 네트워크 또는 퍼블릭 클라우드 환경에

 배포 가능.

활용 사례: 대규모 소프트웨어 개발 프로젝트에서 빌드된 모든 라이브러리와 애플리케이션을 중앙에서 관리하고, 팀 간의 협업을 원활하게 하며, 배포 과정을 단순화.

```pwsh
# run administrative shell
choco install nexus-repository
# Start-Service -Name nexus
# Stop-Service -Name nexus
```

⚠️ 설치 후 Windows Services에 자동으로 등록되며 `localhost:8081`을 통해 서비스됩니다. 초기 계정은 `admin/admin`입니다. 서비스를 종료하려면 `Stop-Service -Name nexus`를 입력합니다.

## Terminal

[Microsoft Terminal 설치 가이드](https://github.com/microsoft/terminal)

```pwsh
# run administrative shell
choco install microsoft-windows-terminal
choco upgrade microsoft-windows-terminal
```
