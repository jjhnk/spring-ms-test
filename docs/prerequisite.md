# Requirements

- [Requirements](#requirements)
  - [PowerShell](#powershell)
  - [Chocolatey](#chocolatey)
  - [WSL](#wsl)
  - [Docker](#docker)
  - [Visual Studio Code](#visual-studio-code)
    - [VSCode 관련 extension](#vscode-관련-extension)
  - [Java](#java)
  - [Gradle](#gradle)
  - [Minikube, Kubenetes-cli, Istioctl, Helm](#minikube-kubenetes-cli-istioctl-helm)
  - [Maven](#maven)
  - [DBeaver](#dbeaver)
- [Optional](#optional)
  - [Git For Windows](#git-for-windows)
  - [Nexus OSS](#nexus-oss)
  - [JMeter](#jmeter)
  - [Terminal](#terminal)

## PowerShell

[PowerShell 설치 가이드](https://learn.microsoft.com/en-us/powershell/scripting/install/installing-powershell-on-windows?view=powershell-7.4&viewFallbackFrom=powershell-7&WT.mc_id=THOMASMAURER-blog-thmaure)

```pwsh
# run administrative shell
winget search Microsoft.PowerShell
winget install --id Microsoft.Powershell --source winget
```

설치된 PowerShell은 아래의 경로에 저장됩니다:

> %ProgramFiles%/PowerShell/7 ($env:ProgramFiles/PowerShell/7)

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

## Minikube, Kubenetes-cli, Istioctl, Helm

[Kubernetes Minikube 설치 가이드](https://minikube.sigs.k8s.io/docs/)

```pwsh
# run administrative shell
choco install minikube --version 1.33.1
choco install kubernetes-cli --version 1.30.2
choco install kubernetes-helm --version 3.15.1
choco install istioctl --version 1.22.0

# for verification
minikube version | Select-String minikube; kubectl version --client -o json | jq -r .clientVersion.gitVersion; helm version --short; istioctl version --remote=false;

# should be
# minikube version: v1.33.1
# v1.30.2
# v3.15.1+ge211f2a
# 1.22.0
```

사용되는 명령어는 아래와 같습니다. 해당 내용은 나중에 다시 설명합니다.

```txt
minikube start
minikube stop
minikube tunnel
minikube -p minikube docker-env --shell powershell | Invoke-Expression
minikube -p minikube docker-env -u --shell powershell | Invoke-Expression
```

Helm은 Kubernetes Helm은 Kubernetes 애플리케이션을 효율적으로 관리하고 배포하기 위한 패키지 관리 도구입니다.

Istio는 서비스 메쉬 기술의 대표적인 오픈 소스 프로젝트로, 마이크로서비스 아키텍처에서 서비스 간의 트래픽 관리를 위한 인프라를 제공합니다. 다음은 Istio의 주요 특징과 기능에 대한 간략한 설명입니다:

1. **서비스 간 통신 관리**:
   - 서비스 간의 트래픽을 제어하고 모니터링하는 기능을 제공합니다.
   - 동적 라우팅, 로드 밸런싱, 서비스 디스커버리, 장애 조치 등을 지원합니다.

2. **보안**:
   - 서비스 간의 통신을 자동으로 암호화하여 보안을 강화합니다.
   - 인증 및 권한 부여를 통해 서비스 간의 접근 제어를 관리할 수 있습니다.

3. **관찰 가능성**:
   - 서비스 간의 트래픽에 대한 상세한 모니터링 및 로깅 기능을 제공합니다.
   - 트레이싱, 메트릭 수집, 로그 수집 등을 통해 각 서비스의 성능과 상태를 모니터링할 수 있습니다.

4. **정책 관리**:
   - 다양한 트래픽 제어 정책을 설정할 수 있습니다.
   - 레이트 리미팅, 트래픽 셰이핑, 리트라이, 타임아웃 등의 정책을 통해 서비스 간의 트래픽을 세밀하게 제어할 수 있습니다.

5. **서비스 메시 아키텍처**:
   - 사이드카 프록시 패턴을 사용하여 각 서비스 인스턴스에 Envoy 프록시를 배포합니다.
   - 이 프록시는 트래픽을 가로채고 제어하여 Istio의 기능을 구현합니다.

6. **확장성**:
   - Istio는 플러그인 아키텍처를 통해 쉽게 확장할 수 있습니다.
   - 다양한 백엔드 시스템과 통합할 수 있는 확장 포인트를 제공합니다.


## Maven

Maven은 패키지 관리 도구입니다. 본 프로젝트에서는 API, 유틸 계층을 분할하기 위해 Maven을 사용합니다.

```pwsh
# run administrative shell
choco install maven
```

⚠️ 설치 후 아래 경로가 생성되었는지 확인합니다.

> Path: %UserProfile%/.m2 ($env:USERPROFILE/.m2)

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
5. **유연한 배포**: 내부 네트워크 또는 퍼블릭 클라우드 환경에 배포 가능.

활용 사례: 대규모 소프트웨어 개발 프로젝트에서 빌드된 모든 라이브러리와 애플리케이션을 중앙에서 관리하고, 팀 간의 협업을 원활하게 하며, 배포 과정을 단순화.

```pwsh
# run administrative shell
choco install nexus-repository
# Start-Service -Name nexus
# Stop-Service -Name nexus
```

⚠️ 설치 후 Windows Services에 자동으로 등록되며 `localhost:8081`을 통해 서비스됩니다. 초기 계정은 `admin/admin`입니다. 서비스를 종료하려면 `Stop-Service -Name nexus`를 입력합니다.

## JMeter

서버에 대한 성능 및 부하를 측정할 수 있는 도구.

```pwsh
# run administrative shell
choco install jmeter
```

## Terminal

[Microsoft Terminal 설치 가이드](https://github.com/microsoft/terminal)

```pwsh
# run administrative shell
choco install microsoft-windows-terminal
choco upgrade microsoft-windows-terminal
```
