# Abstract

Spring Ms Test 프로젝트는 적절한 수준의 개발 환경을 제공하여 마이크로 서비스의 개발을 지원하는 것을 목표로 합니다. 이 프로젝트는 JAVA와 Spring을 중심으로 개발되었으며, 마이크로 서비스는 클라우드 컴퓨팅 환경에서의 운영을 목표로 Docker와 Kubernetes를 사용합니다.

적절한 수준이란, 엔터프라이즈 레벨의 서비스를 구축하기 위해 필요한 다양한 도구들을 간단하게 테스트할 수 있는 수준을 의미합니다. 다시 말해, 개발 환경을 운영하기 위해 다양한 best practices 를 따르기보다는, 기본 기능을 쉽게 확인할 수 있도록 설정했습니다.

이 프로젝트의 구조는 이를 잘 보여줍니다. 일반적인 모놀리식 프로젝트는 단일 레포지토리를 사용하지만, 엔터프라이즈 수준의 마이크로 서비스 프로젝트는 다중 레포지토리 구조를 가집니다. 이 레포지토리는 두 가지 구조의 장점을 결합하여, 하나의 레포지토리가 여러 프로젝트를 포함할 수 있도록 설계되었습니다.

차후 RECAP에서는 프로젝트를 어떻게 발전시켜 나갈지에 대해 간단히 논의하겠습니다.

목차:

- [Abstract](#abstract)
- [Features](#features)
- [Prerequisite](#prerequisite)
  - [Requirements](#requirements)
    - [PowerShell](#powershell)
    - [Chocolatey](#chocolatey)
    - [Terminal (Optional)](#terminal-optional)
    - [WSL](#wsl)
    - [Docker](#docker)
    - [Visual Studio Code](#visual-studio-code)
      - [vscode 관련 extension](#vscode-관련-extension)
    - [JAVA](#java)
    - [GRADLE](#gradle)
    - [MINIKUBE](#minikube)
    - [Maven](#maven)
  - [Optional](#optional)
    - [Git For Windows](#git-for-windows)
    - [Nexus OSS](#nexus-oss)
      - [주요 기능](#주요-기능)
      - [활용 사례](#활용-사례)
- [Getting Started](#getting-started)
- [Related Documentations](#related-documentations)

# Features

링크: [Features](./docs/featuers.md)

목차:

- [1. 모니터링 (Monitoring)](./docs/featuers.md/#1-모니터링-monitoring)
- [2. 로깅 (Logging, Distributed Tracing)](./docs/featuers.md/#2-로깅-logging-distributed-tracing)
- [3. 중앙집중적 API Documentation (Centralized Documentation)](./docs/featuers.md/#3-중앙집중적-api-documentation-centralized-documentation)
- [4. 인증 (Authentication)](./docs/featuers.md/#4-인증-authentication)
- [5. 중앙집중적 환경설정 (Centralized Configuration)](./docs/featuers.md/#5-중앙집중적-환경설정-centralized-configuration)
- [6. 서비스 디스커버리 (Service Discovery)](./docs/featuers.md/#6-서비스-디스커버리-service-discovery)
- [7. 엣지 서버 (Edge Server)](./docs/featuers.md/#7-엣지-서버-edge-server)
- [8. 서비스 메시 (Service Mesh)](./docs/featuers.md/#8-서비스-메시-service-mesh)
- [9. 프로젝트에 대하여 (요약)](./docs/featuers.md/#9-프로젝트에-대하여-요약)

# Prerequisite

해당 프로젝트는 Windows 환경에서 WSL DOCKER에 기반합니다. Linux Distribution 및 macOS는 지원하지 않습니다.

관련 어플리케이션은은 Chocolatey를 기반으로 다운로드 합니다. 다른 Installation 방식이 있다면 해당 방식을 사용하시면 됩니다.

PowerShell을 기반으로 운영합니다. PowerShell의 원할한 운영을 위해 7.4.x 버전 이상을 사용합니다.

개발 환경은 vscode를 기준으로 구현됩니다. 다른 도구는 상황에 맞게 적용하면 됩니다.

⚠️ 아래는 Windows 11 / PowerShell을 기준으로 설명되었으나, 별도로 다른 방식으로 패키지 관리를 한다면 다른 방식을 사용하면 됩니다.

## Requirements

### [PowerShell](https://learn.microsoft.com/en-us/powershell/scripting/install/installing-powershell-on-windows?view=powershell-7.4&viewFallbackFrom=powershell-7&WT.mc_id=THOMASMAURER-blog-thmaure)

Installation
```pwsh
# run administrative shell
winget search Microsoft.PowerShell
winget install --id Microsoft.Powershell --source winget
```

설치된 PowerShell은 아래의 Path에 저장된다.

> %ProgramFiles%/PowerShell/7

만약 vscode의 terminal의 profile의 환경에 설치된 PowerShell을 추가하고자 한다면 다음과 같이 설치한다.

```code
[CTRL+SHIFT+P]
> Preferences: Open User Settings (JSON)

# for key "terminal.integrated.profiles.windows", add json value "PowerShell": { "path": "${env:programfiles}\\PowerShell\\7\\pwsh.exe", "icon": "terminal-powershell" }

# for example
"terminal.integrated.profiles.windows": {
  "PowerShell": {
    "path": "${env:programfiles}\\PowerShell\\7\\pwsh.exe",
    "icon": "terminal-powershell"
  },
  ...
}

# for set it as default add line below
"terminal.integrated.defaultProfile.windows": "PowerShell"
```

### [Chocolatey](https://chocolatey.org/install)

```pwsh
# run administrative shell
Set-ExecutionPolicy Bypass -Scope Process -Force; [System.Net.ServicePointManager]::SecurityProtocol = [System.Net.ServicePointManager]::SecurityProtocol -bor 3072; iex ((New-Object System.Net.WebClient).DownloadString('https://community.chocolatey.org/install.ps1'))
```

### [Terminal](https://github.com/microsoft/terminal) (Optional)

```pwsh
# run administrative shell
choco install microsoft-windows-terminal
choco upgrade microsoft-windows-terminal
```

### [WSL](https://learn.microsoft.com/en-us/windows/wsl/install)

```pwsh
# run administrative shell
wsl --install
wsl -l -o
wsl --install -d Ubuntu
```

### [Docker](https://www.docker.com/products/docker-desktop/)

```pwsh
# run administrative shell
choco install docker-desktop
```

⚠️ Chocolatey를 통해 docker-desktop을 다운받으면 업데이트 시에 문제가 있을 수 있다. https://www.docker.com/products/docker-desktop/ 에서 다운을 받아도 상관이 없다.

### [Visual Studio Code](https://code.visualstudio.com/download)

```pwsh
# run administrative shell
choco install vscode
```

#### vscode 관련 extension

아래는 필수이다.

``` txt
1. Extension Pack for Java, Debugger For Java, Test Runner For Java, Maven For Java, Project Manager For Java, Gradle For JAva, Language Support for Java(TM) by RedHat
2. KUBERNETES, Bridge To Kubernetes
3. Docker
4. Wsl
5. Spring Boot Dashboard, Spring Boot Tools, Spring Boot Extension Pack
 ```

만약 일일히 치는게 귀찮다면 아래를 붙여넣기 하자. 다만 일일히 검증은 안해서 이상한 게 들어가거나 빠졌을 수 있다.

``` pwsh
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

### [JAVA](https://adoptium.net/temurin/releases/)

java는 최소 21버전을 사용한다.

```pwsh
# run administrative shell
choco install openjdk
# choco install openjdk --version 21.0.2
```

만약 일반적인 상황에서 Temurin을 사용해야할 경우나, Microsoft Build of OpenJDK를 사용해야 할 경우가 있다면 아래를 참조한다.

일반적으로 production의 환경에서는 temurin가 선호되며, Azure Cloud를 사용하는 경우 Microsoft Build of OpenJDK를 추천한다.

```pwsh
# run administrative shell
# install temurin java
choco install temurin
# install Microsoft build of OpenJDK
choco install microsoft-openjdk
```

### [GRADLE](https://gradle.org/)

gradle은 8.8버전을 사용한다.

```pwsh
# run administrative shell
choco install gradle
# choco install gradle --version 8.8
```

### [MINIKUBE](https://minikube.sigs.k8s.io/docs/)

Kubernetes Distribution으로는 Minikube를 사용한다. Minikube는 local host에서 kubernetes를 활용하기 위한 도구이며 해당 프로젝트에서는 개인 컴퓨터에 node 설치를 지양하고자 minikube를 활용한다.

```pwsh
# run administrative shell
choco install minikube
# choco install kubernetes-cli
```

사용되는 명령어는 아래와 같다. 해당 내용은 나중에 다시 설명하도록 한다.

> minikube start
> minikube stop
> minikube tunnel
> minikube -p minikube docker-env --shell powershell | Invoke-Expression
> minikube -p minikube docker-env -u --shell powershell | Invoke-Expression

### Maven

maven은 패키지 관리 도구이다. 본 프로젝트에서는 api, util 계층을 분할하기 위해서 maven을 사용한다.

```pwsh
# run administrative shell
choco install maven
```

⚠️ 설치가 된 이후 아래의 Path가 생성되었는지 확인한다.
> Path: %UserProfile%/.m2


## Optional

### [Git For Windows](https://git-scm.com/download/win)

실질적으로 Git이 사용되는 일은 거의 없다. 다만 종종 Git Bash를 를 사용한다.

```pwsh
# run administrative shell
choco install git
```

### [Nexus OSS](https://www.sonatype.com/products/sonatype-nexus-oss)

Nexus OSS는 Sonatype에서 개발한 아티팩트 저장소 관리자이다. 주로 개발자가 소프트웨어 컴포넌트를 효율적으로 저장, 관리, 배포할 수 있도록 설계되었다. Nexus OSS는 다양한 프로그래밍 언어와 패키지 형식을 지원하여 개발 및 배포 과정을 단순화합니다.

#### 주요 기능

1. **아티팩트 저장소**: Maven, npm, NuGet, Docker, PyPI 등 다양한 형식의 아티팩트를 저장하고 관리할 수 있습니다.
2. **프로세스 자동화**: CI/CD 파이프라인과 통합되어 자동으로 빌드된 아티팩트를 저장하고 배포하는 과정을 자동화할 수 있습니다.
3. **보안 및 라이선스 관리**: 저장된 아티팩트의 보안 취약점 및 라이선스 문제를 모니터링하고 관리할 수 있습니다.
4. **확장성**: 플러그인 아키텍처를 통해 기능을 확장할 수 있으며, 대규모 프로젝트에서도 효율적으로 운영할 수 있습니다.
5. **유연한 배포**: 내부 네트워크 또는 퍼블릭 클라우드 환경에 배포하여 사용할 수 있습니다.

#### 활용 사례

Nexus OSS는 다양한 개발 환경에서 널리 사용됩니다. 예를 들어, 대규모 소프트웨어 개발 프로젝트에서 빌드된 모든 라이브러리와 애플리케이션을 중앙에서 관리하고, 팀 간의 협업을 원활하게 하며, 배포 과정을 단순화하는 데 사용됩니다.

해당 프로젝트에서는 docker 및 application image 관리를 위해서 사용했으나, 별도로 학습 난이도를 위해 disable 하였다.

다른 아티팩트 저장소 관리자로는 Google Artifact Registry, Azure Artifacts, GitHub Packages 등이 있다.

```pwsh
# run administrative shell
choco install nexus-repository
# Start-Service -Name nexus
# Stop-Service -Name nexus
```

⚠️ 위와 같이 설치하면 Windows Services에 자동으로 등록되며 localhost:8081을 통해 service된다. 초기 계정은 admin/admin이다. 만약 nexus 서비스를 종료하려면 Stop-Service -Name nexus를 입력한다.

# Getting Started

# Related Documentations