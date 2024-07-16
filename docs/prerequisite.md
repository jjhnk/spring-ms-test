# Requirements
- [Requirements](#requirements)
  - [PowerShell](#powershell)
  - [Chocolatey](#chocolatey)
  - [WSL](#wsl)
  - [Docker](#docker)
  - [Visual Studio Code](#visual-studio-code)
  - [vscode 관련 extension](#vscode-관련-extension)
  - [JAVA](#java)
  - [GRADLE](#gradle)
  - [MINIKUBE](#minikube)
  - [Maven](#maven)
  - [DBeaver](#dbeaver)
- [Optional](#optional)
  - [Git For Windows](#git-for-windows)
  - [Nexus OSS](#nexus-oss)
  - [Terminal](#terminal)


## [PowerShell](https://learn.microsoft.com/en-us/powershell/scripting/install/installing-powershell-on-windows?view=powershell-7.4&viewFallbackFrom=powershell-7&WT.mc_id=THOMASMAURER-blog-thmaure)

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

## [Chocolatey](https://chocolatey.org/install)

```pwsh
# run administrative shell
Set-ExecutionPolicy Bypass -Scope Process -Force; [System.Net.ServicePointManager]::SecurityProtocol = [System.Net.ServicePointManager]::SecurityProtocol -bor 3072; iex ((New-Object System.Net.WebClient).DownloadString('https://community.chocolatey.org/install.ps1'))
```

## [WSL](https://learn.microsoft.com/en-us/windows/wsl/install)

```pwsh
# run administrative shell
wsl --install
wsl -l -o
wsl --install -d Ubuntu
```

## [Docker](https://www.docker.com/products/docker-desktop/)

```pwsh
# run administrative shell
choco install docker-desktop
```

⚠️ Chocolatey를 통해 docker-desktop을 다운받으면 업데이트 시에 문제가 있을 수 있다.<br/> https://www.docker.com/products/docker-desktop/ 에서 다운을 받아도 상관이 없다.

## [Visual Studio Code](https://code.visualstudio.com/download)

```pwsh
# run administrative shell
choco install vscode
```

## vscode 관련 extension

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

## [JAVA](https://adoptium.net/temurin/releases/)

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

## [GRADLE](https://gradle.org/)

gradle은 8.8버전을 사용한다.

```pwsh
# run administrative shell
choco install gradle
# choco install gradle --version 8.8
```

## [MINIKUBE](https://minikube.sigs.k8s.io/docs/)

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

## Maven

maven은 패키지 관리 도구이다. 본 프로젝트에서는 api, util 계층을 분할하기 위해서 maven을 사용한다.

```pwsh
# run administrative shell
choco install maven
```

⚠️ 설치가 된 이후 아래의 Path가 생성되었는지 확인한다.
> Path: %UserProfile%/.m2

## [DBeaver](https://dbeaver.io/)

DBeaver는 다양한 데이터베이스 관리 및 개발을 지원하는 통합 개발 환경(IDE)이다. 별도로 데이터베이스 관리 도구를 사용하여도 되나, MongoDB, MySQL, PostgreSQL이 사용되므로 DBeaver를 사용하는 것을 권장하는 바이다.

```pwsh
# run administrative shell
choco install dbeaver
```


# Optional

## [Git For Windows](https://git-scm.com/download/win)

실질적으로 Git이 사용되는 일은 거의 없다. 다만 종종 Git Bash를 를 사용한다.

```pwsh
# run administrative shell
choco install git
```

## [Nexus OSS](https://www.sonatype.com/products/sonatype-nexus-oss)

Nexus OSS는 Sonatype에서 개발한 아티팩트 저장소 관리자이다. 주로 개발자가 소프트웨어 컴포넌트를 효율적으로 저장, 관리, 배포할 수 있도록 설계되었다. Nexus OSS는 다양한 프로그래밍 언어와 패키지 형식을 지원하여 개발 및 배포 과정을 단순화합니다.

**주요 기능**

1. **아티팩트 저장소**: Maven, npm, NuGet, Docker, PyPI 등 다양한 형식의 아티팩트를 저장하고 관리할 수 있습니다.
2. **프로세스 자동화**: CI/CD 파이프라인과 통합되어 자동으로 빌드된 아티팩트를 저장하고 배포하는 과정을 자동화할 수 있습니다.
3. **보안 및 라이선스 관리**: 저장된 아티팩트의 보안 취약점 및 라이선스 문제를 모니터링하고 관리할 수 있습니다.
4. **확장성**: 플러그인 아키텍처를 통해 기능을 확장할 수 있으며, 대규모 프로젝트에서도 효율적으로 운영할 수 있습니다.
5. **유연한 배포**: 내부 네트워크 또는 퍼블릭 클라우드 환경에 배포하여 사용할 수 있습니다.

**활용 사례**

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


## [Terminal](https://github.com/microsoft/terminal)

```pwsh
# run administrative shell
choco install microsoft-windows-terminal
choco upgrade microsoft-windows-terminal
```