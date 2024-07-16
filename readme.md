# Abstract

Spring Ms Test 프로젝트는 적절한 수준의 개발 환경을 제공하여 마이크로 서비스의 개발을 지원하는 것을 목표로 합니다. 이 프로젝트는 JAVA와 Spring을 중심으로 개발되었으며, 마이크로 서비스는 클라우드 컴퓨팅 환경에서의 운영을 목표로 Docker와 Kubernetes를 사용합니다.

적절한 수준이란, 엔터프라이즈 레벨의 서비스를 구축하기 위해 필요한 다양한 도구들을 간단하게 테스트할 수 있는 수준을 의미합니다. 다시 말해, 개발 환경을 운영하기 위해 다양한 best practices 를 따르기보다는, 기본 기능을 쉽게 확인할 수 있도록 설정했습니다.

이 프로젝트의 구조는 이를 잘 보여줍니다. 일반적인 모놀리식 프로젝트는 단일 레포지토리를 사용하지만, 엔터프라이즈 수준의 마이크로 서비스 프로젝트는 다중 레포지토리 구조를 가집니다. 이 레포지토리는 두 가지 구조의 장점을 결합하여, 하나의 레포지토리가 여러 프로젝트를 포함할 수 있도록 설계되었습니다.

차후 RECAP에서는 프로젝트를 어떻게 발전시켜 나갈지에 대해 간단히 논의하겠습니다.

목차:

- [Abstract](#abstract)
- [Features](#features)
- [Prerequisite](#prerequisite)
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

링크: [Prerequisite](./docs/prerequisite.md)

- [Requirements](./docs/prerequisite.md#requirements)
  - [PowerShell](./docs/prerequisite.md#powershell)
  - [Chocolatey](./docs/prerequisite.md#chocolatey)
  - [WSL](./docs/prerequisite.md#wsl)
  - [Docker](./docs/prerequisite.md#docker)
  - [Visual Studio Code](./docs/prerequisite.md#visual-studio-code)
  - [vscode 관련 extension](./docs/prerequisite.md#vscode-관련-extension)
  - [JAVA](./docs/prerequisite.md#java)
  - [GRADLE](./docs/prerequisite.md#gradle)
  - [MINIKUBE](./docs/prerequisite.md#minikube)
  - [Maven](./docs/prerequisite.md#maven)
  - [DBeaver](./docs/prerequisite.md#dbeaver)
- [Optional](./docs/prerequisite.md#optional)
  - [Git For Windows](./docs/prerequisite.md#git-for-windows)
  - [Nexus OSS](./docs/prerequisite.md#nexus-oss)
  - [Terminal](./docs/prerequisite.md#terminal)

# Getting Started

# Related Documentations