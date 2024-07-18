# Abstract

Spring MS Test 프로젝트는 마이크로서비스 개발을 지원하는 적절한 개발 환경을 제공하는 것을 목표로 합니다. 이 프로젝트는 JAVA와 Spring을 기반으로 하며, Docker와 Kubernetes를 사용하여 클라우드 컴퓨팅 환경에서 마이크로서비스를 운영하는 것을 목표로 합니다.

'적절한 수준'이란 엔터프라이즈 레벨의 서비스를 구축하는 데 필요한 다양한 도구들을 간단하게 테스트할 수 있는 수준을 의미합니다. 즉, 개발 환경을 운영하기 위해 다양한 최적 사례(best practices)를 따르기보다는 기본 기능을 쉽게 확인할 수 있도록 설정되었습니다.

이 프로젝트의 구조는 이러한 목표를 잘 반영합니다. 일반적인 모놀리식 프로젝트는 단일 레포지토리를 사용하지만, 엔터프라이즈 수준의 마이크로서비스 프로젝트는 다중 레포지토리 구조를 가집니다. 이 레포지토리는 두 가지 구조의 장점을 결합하여, 하나의 레포지토리가 여러 프로젝트를 포함할 수 있도록 설계되었습니다.

앞으로의 RECAP에서는 이 프로젝트를 어떻게 발전시켜 나갈지에 대해 간단히 논의할 것입니다.


목차:

- [Abstract](#abstract)
- [Features](#features)
- [Prerequisite](#prerequisite)
- [Getting Started](#getting-started)
  - [시스템 구성도](#시스템-구성도)
  - [1. 아티팩트 배포](#1-아티팩트-배포)
  - [2. kubernetes-dashboard](#2-kubernetes-dashboard)
  - [3. grafana](#3-grafana)
  - [4. elasticserach](#4-elasticserach)
    - [Jaeger와 연계](#jaeger와-연계)
  - [5. Keycloak](#5-keycloak)
- [Documentations](#documentations)

# Features

링크: [Features](./docs/features.md)

목차:

- [1. 모니터링 (Monitoring)](./docs/features.md/#1-모니터링-monitoring)
- [2. 로깅 (Logging, Distributed Tracing)](./docs/features.md/#2-로깅-logging-distributed-tracing)
- [3. 중앙집중적 API Documentation (Centralized Documentation)](./docs/features.md/#3-중앙집중적-api-documentation-centralized-documentation)
- [4. 인증 (Authentication)](./docs/features.md/#4-인증-authentication)
- [5. 중앙집중적 환경설정 (Centralized Configuration)](./docs/features.md/#5-중앙집중적-환경설정-centralized-configuration)
- [6. 서비스 디스커버리 (Service Discovery)](./docs/features.md/#6-서비스-디스커버리-service-discovery)
- [7. 엣지 서버 (Edge Server)](./docs/features.md/#7-엣지-서버-edge-server)
- [8. 서비스 메시 (Service Mesh)](./docs/features.md/#8-서비스-메시-service-mesh)
- [9. 프로젝트에 대하여 (요약)](./docs/features.md/#9-프로젝트에-대하여-요약)


# Prerequisite

이 프로젝트는 Windows 환경의 WSL Docker를 기반으로 합니다. Linux Distribution 및 macOS는 지원하지 않습니다.

필요한 어플리케이션은 Chocolatey를 통해 다운로드합니다. 다른 설치 방식이 있다면 해당 방식을 사용해도 됩니다.

PowerShell을 기반으로 운영하며, 원활한 운영을 위해 PowerShell 7.4.x 버전 이상을 사용합니다.

개발 환경은 Visual Studio Code (VSCode)를 기준으로 구현됩니다. 다른 도구를 사용해도 무방합니다.

⚠️ 아래 내용은 Windows 11 및 PowerShell을 기준으로 설명되었으며, 다른 방식으로 패키지를 관리하는 경우 해당 방식을 사용할 수 있습니다.


링크: [Prerequisite](./docs/prerequisite.md)

- [Requirements](./docs/prerequisite.md#requirements)
  - [PowerShell](./docs/prerequisite.md#powershell)
  - [Chocolatey](./docs/prerequisite.md#chocolatey)
  - [WSL](./docs/prerequisite.md#wsl)
  - [Docker](./docs/prerequisite.md#docker)
  - [Visual Studio Code](./docs/prerequisite.md#visual-studio-code)
    - [VSCode 관련 extension](./docs/prerequisite.md#vscode-관련-extension)
  - [Java](./docs/prerequisite.md#java)
  - [Gradle](./docs/prerequisite.md#gradle)
  - [Minikube, Kubenetes-cli, Istioctl, Helm](./docs/prerequisite.md#minikube-kubenetes-cli-istioctl-helm)
  - [Maven](./docs/prerequisite.md#maven)
  - [DBeaver](./docs/prerequisite.md#dbeaver)
- [Optional](./docs/prerequisite.md#optional)
  - [Git For Windows](./docs/prerequisite.md#git-for-windows)
  - [Nexus OSS](./docs/prerequisite.md#nexus-oss)
  - [JMeter](./docs/prerequisite.md#jmeter)
  - [Terminal](./docs/prerequisite.md#terminal)

# Getting Started

## 시스템 구성도

![system-diagram](./images/system-diagram.png)

링크: [Getting Started](./docs/getting-started.md)

 [Git Repository 설치 및 초기 설정](./docs/getting-started.md#git-repository-설치-및-초기-설정)
- [Git repository pull](./docs/getting-started.md#git-repository-pull)
- [api 및 util artifact publishing](./docs/getting-started.md#api-및-util-artifact-publishing)
- [Ingress Gateway의 IP address에 대한 hostname 등록](./docs/getting-started.md#ingress-gateway의-ip-address에-대한-hostname-등록)
- [artifact build](./docs/getting-started.md#artifact-build)
- [minikube start \& import dependencies](./docs/getting-started.md#minikube-start--import-dependencies)
- [create configuration link file](./docs/getting-started.md#create-configuration-link-file)
- [Helm repository를 추가한다](./docs/getting-started.md#helm-repository를-추가한다)
- [install istio](./docs/getting-started.md#install-istio)
- [coredns update](./docs/getting-started.md#coredns-update)
- [Install persistence](./docs/getting-started.md#install-persistence)
- [Install redis](./docs/getting-started.md#install-redis)
- [Install Message Queue](./docs/getting-started.md#install-message-queue)
- [Install Logging](./docs/getting-started.md#install-logging)
- [Install Kubernetes Dashboard](./docs/getting-started.md#install-kubernetes-dashboard)

⚠️ 이 과정은 별도로 검증되지 않았으므로 오류가 있을 수 있습니다. 오류를 발견하면 issue에 제보해주시기 바랍니다.

## 1. 아티팩트 배포

초기 설정이 완료된 후, 앱 아티팩트를 배포합니다.

```pwsh
# hands-on 배포
# jar artifact 업데이트
minikube -p minikube docker-env -u --shell powershell | Invoke-Expression
./gradlew build

# docker iamge 업데이트
minikube -p minikube docker-env --shell powershell | Invoke-Expression
docker-compose build

$target = "hands-on"
$env = "dev-env"
kubectl delete namespace $target
Get-ChildItem -Path "kubernetes/helm/$target/components" -Directory | ForEach-Object { helm dep up $_.FullName } | Out-Null
Get-ChildItem -Path "kubernetes/helm/$target/environments" -Directory | ForEach-Object { helm dep up $_.FullName } | Out-Null
kubectl apply -f "./kubernetes/namespaces/${target}-namespace.yml"
helm install "${target}-dev-env" "kubernetes/helm/$target/environments/$env" -n "$target"
helm upgrade "${target}-dev-env" "kubernetes/helm/$target/environments/dev-env" -n "$target"

# 위에서 이미 jar artifact 및 docker image가 배포 되었으므로 생략
$target = "hy-oltp"
$env = "dev-env"
kubectl delete namespace $target
Get-ChildItem -Path "kubernetes/helm/$target/components" -Directory | ForEach-Object { helm dep up $_.FullName } | Out-Null
Get-ChildItem -Path "kubernetes/helm/$target/environments" -Directory | ForEach-Object { helm dep up $_.FullName } | Out-Null
kubectl apply -f "./kubernetes/namespaces/${target}-namespace.yml"
helm install "${target}-dev-env" "kubernetes/helm/$target/environments/$env" -n "$target"
helm upgrade "${target}-dev-env" "kubernetes/helm/$target/environments/dev-env" -n "$target"
```

Kubernetes 탭을 클릭하고 모든 리소스가 "all-green"이 될 때까지 기다립니다.

![vscode-kubernetes](./images/vscode-kubernetes.png)

* minikube tunnel을 실행합니다.

```pwsh
minikube tunnel
```

탭 그룹을 열고 아래의 서비스들을 등록합니다.

```txt
https://kiali.minikube.me/
https://kubernetes-dashboard.minikube.me/
https://grafana.minikube.me/
https://kibana.minikube.me/
https://tracing.minikube.me/
https://mail.minikube.me/
https://prometheus.minikube.me/
https://alertmanager.minikube.me/
https://redis-insight.minikube.me/
https://minikube.me/product-composite/openapi/swagger-ui.html
https://keycloak.minikube.me/
```

## 2. kubernetes-dashboard

https://kubernetes-dashboard.minikube.me/ 에 접속하기 위해서는 아래의 명령어를 통해 토큰을 획득해야 합니다.

```
kubectl -n kubernetes-dashboard create token admin-user
```

## 3. grafana

https://grafana.minikube.me/ 를 활용하기 위해서는 다음을 참조한다.

JVM (Micrometer)를 활성화하기 위해 다음을 import한다.

* [Dashboards]
* [Import]
* [Import via grafana.com]에 17175를 입력한다.

![grafana-setting](./images/grafana-setting-1.png)

* 아래 request를 요청합니다

```
Invoke-RestMethod -Uri 'https://grafana.minikube.me/api/dashboards/db' -Method Post -Headers @{'Content-Type'='application/json'} -Body '{"meta":{"type":"db","canSave":true,"canEdit":true,"canAdmin":true,"canStar":false,"canDelete":true,"slug":"hands-on-dashboard","url":"/d/ZF5_6XwVk/hands-on-dashboard","expires":"0001-01-01T00:00:00Z","created":"2023-05-31T16:17:58Z","updated":"2023-05-31T16:54:43Z","updatedBy":"Anonymous","createdBy":"Anonymous","version":5,"hasAcl":false,"isFolder":false,"folderId":0,"folderUid":"","folderTitle":"General","folderUrl":"","provisioned":false,"provisionedExternalId":"","annotationsPermissions":{"dashboard":{"canAdd":true,"canEdit":true,"canDelete":true},"organization":{"canAdd":true,"canEdit":true,"canDelete":true}},"isPublic":false},"dashboard":{"annotations":{"list":[{"builtIn":1,"datasource":{"type":"grafana","uid":"-- Grafana --"},"enable":true,"hide":true,"iconColor":"rgba(0, 211, 255, 1)","name":"Annotations & Alerts","target":{"limit":100,"matchAny":false,"tags":[],"type":"dashboard"},"type":"dashboard"}]},"editable":true,"fiscalYearStartMonth":0,"graphTooltip":0,"id":null,"links":[],"liveNow":false,"panels":[{"datasource":{"type":"prometheus","uid":"PBFA97CFB590B2093"},"fieldConfig":{"defaults":{"color":{"mode":"palette-classic"},"custom":{"axisLabel":"","axisPlacement":"auto","barAlignment":0,"drawStyle":"line","fillOpacity":0,"gradientMode":"none","hideFrom":{"legend":false,"tooltip":false,"viz":false},"lineInterpolation":"linear","lineWidth":1,"pointSize":5,"scaleDistribution":{"type":"linear"},"showPoints":"auto","spanNulls":false,"stacking":{"group":"A","mode":"none"},"thresholdsStyle":{"mode":"off"}},"mappings":[],"thresholds":{"mode":"absolute","steps":[{"color":"green","value":null},{"color":"red","value":80}]}},"overrides":[]},"gridPos":{"h":6,"w":24,"x":0,"y":0},"id":4,"options":{"legend":{"calcs":[],"displayMode":"list","placement":"bottom"},"tooltip":{"mode":"multi","sort":"none"}},"targets":[{"datasource":{"type":"prometheus","uid":"PBFA97CFB590B2093"},"editorMode":"builder","expr":"rate(resilience4j_retry_calls_total[30s])","interval":"","legendFormat":"{{kind}}","range":true,"refId":"A"}],"title":"Retry","type":"timeseries"},{"alert":{"alertRuleTags":{},"conditions":[{"evaluator":{"params":[0.5],"type":"lt"},"operator":{"type":"and"},"query":{"params":["A","10s","now"]},"reducer":{"params":[],"type":"max"},"type":"query"}],"executionErrorState":"alerting","for":"0m","frequency":"10s","handler":1,"name":"Circuit Breaker alert","noDataState":"no_data","notifications":[]},"datasource":{"type":"prometheus","uid":"PBFA97CFB590B2093"},"fieldConfig":{"defaults":{"color":{"mode":"palette-classic"},"custom":{"axisLabel":"","axisPlacement":"auto","barAlignment":0,"drawStyle":"line","fillOpacity":0,"gradientMode":"none","hideFrom":{"legend":false,"tooltip":false,"viz":false},"lineInterpolation":"linear","lineWidth":1,"pointSize":5,"scaleDistribution":{"type":"linear"},"showPoints":"auto","spanNulls":false,"stacking":{"group":"A","mode":"none"},"thresholdsStyle":{"mode":"off"}},"mappings":[],"thresholds":{"mode":"absolute","steps":[{"color":"green","value":null},{"color":"red","value":80}]}},"overrides":[]},"gridPos":{"h":5,"w":24,"x":0,"y":6},"id":2,"options":{"legend":{"calcs":[],"displayMode":"list","placement":"bottom"},"tooltip":{"mode":"multi","sort":"none"}},"targets":[{"datasource":{"type":"prometheus","uid":"PBFA97CFB590B2093"},"editorMode":"builder","expr":"resilience4j_circuitbreaker_state{state=\"closed\"}","legendFormat":"{{state}}","range":true,"refId":"A"},{"datasource":{"type":"prometheus","uid":"PBFA97CFB590B2093"},"editorMode":"builder","expr":"resilience4j_circuitbreaker_state{state=\"open\"}","hide":false,"legendFormat":"{{state}}","range":true,"refId":"B"},{"datasource":{"type":"prometheus","uid":"PBFA97CFB590B2093"},"editorMode":"builder","expr":"resilience4j_circuitbreaker_state{state=\"half_open\"}","hide":false,"legendFormat":"{{state}}","range":true,"refId":"C"}],"thresholds":[{"colorMode":"critical","op":"lt","value":0.5,"visible":true}],"title":"Circuit Breaker","type":"timeseries"},{"datasource":{"type":"prometheus","uid":"PBFA97CFB590B2093"},"gridPos":{"h":6,"w":24,"x":0,"y":11},"id":6,"options":{"alertName":"","dashboardAlerts":true,"dashboardTitle":"","maxItems":10,"showOptions":"changes","sortOrder":1,"stateFilter":{"alerting":false,"execution_error":false,"no_data":false,"ok":false,"paused":false,"pending":false},"tags":[]},"pluginVersion":"9.0.1","title":"Circuit Breaker Alerts ","type":"alertlist"}],"refresh":"","schemaVersion":36,"style":"dark","tags":[],"templating":{"list":[]},"time":{"from":"now-5m","to":"now"},"timepicker":{},"timezone":"","title":"Hands-on Dashboard","uid":"ZF5_6XwVk","version":3,"weekStart":""}}' -SkipCertificateCheck
```

## 4. elasticserach

Stack Management로 갑니다.

![elastic-search-setting-1](./images/elastic-search-setting-1.png)

Index Pattern에 logstash-*를 입력하고, 타임 필드를 @timestamp로 설정합니다.

![elastic-search-setting-2](./images/elastic-search-setting-2.png)

로그 대시보드를 설정하려면 Dashboard로 이동합니다.

![elastic-search-setting-3](./images/elastic-search-setting-3.png)

'kubernetes.namespace.name.keyword'를 입력하고 오른쪽으로 드래그하여 도넛 차트로 설정합니다.

![elastic-search-setting-4](./images/elastic-search-setting-4.png)

'kubernetes.container.name.keyword'를 입력하고 오른쪽으로 드래그합니다.

![elastic-search-setting-5](./images/elastic-search-setting-5.png)

오른쪽 상단에서 'Save to library'를 눌러 적절한 이름으로 저장합니다.

오른쪽 상단의 'Save' 버튼을 눌러 적절한 이름으로 저장합니다.

![elastic-search-setting-6](./images/elastic-search-setting-6.png)

오른쪽의 'Discover'를 눌러 실시간 로그 목록을 확인합니다.

![elastic-search-setting-7](./images/elastic-search-setting-7.png)

Search field names에 차례로 아래와 같이 입력합니다.

* kubernetes.namespace.name
* spring.level
* kubernetes.container.name
* spring.trace (⚠️ 중요)
* log

![elastic-search-setting-8](./images/elastic-search-setting-8.png)

오른쪽 상단의 Save 버튼을 눌러 적절한 이름으로 저장합니다.

![elastic-search-setting-9](./images/elastic-search-setting-9.png)

### Jaeger와 연계

⚠️ 현재는 이러한 기능이 있다는 것만 알아두세요. 실습은 나중에 진행합니다.

관심 있는 trace 중 하나를 선택하여 복사합니다(Ctrl + C).

![elastic-search-setting-10](./images/elastic-search-setting-10.png)

'https://tracing.minikube.me/'에 접속하여 검색창에 붙여넣기(Ctrl + V)합니다.

![elastic-search-setting-11](./images/elastic-search-setting-11.png)

로그를 분석합니다.

![elastic-search-setting-12](./images/elastic-search-setting-12.png)


이와 별개로 전후 관계 분석이 필요한 경우는 추후에 다루겠습니다.


## 5. Keycloak

'https://keycloak.minikube.me/'에 접속합니다. 계정은 'admin/admin'입니다.

'Create realm'을 누릅니다.

![keycloak-setting-1](./images/keycloak-setting-1.png)

realm 'dev'를 생성합니다.

![keycloak-setting-1](./images/keycloak-setting-2.png)

'Clients Scope'에서 'Create Client Scope'를 클릭합니다.

![keycloak-setting-1](./images/keycloak-setting-7.png)

Settings에서 Name을 'estate', Type을 'None'으로 설정하고, 'Include in token scope'를 On으로 설정합니다. (Default는 보안 관리 상 좋지 않습니다.)

![keycloak-setting-1](./images/keycloak-setting-8.png)

'Clients'에서 'account-console'을 클릭합니다.

![keycloak-setting-1](./images/keycloak-setting-9.png)

'Client Scopes' 탭에서 'Add client scopes'를 클릭합니다.

![keycloak-setting-1](./images/keycloak-setting-10.png)

'estate'를 체크하고 Default로 설정합니다.

![keycloak-setting-1](./images/keycloak-setting-11.png)

'microprofile-jwt'를 Default로 설정합니다.

![keycloak-setting-1](./images/keycloak-setting-12.png)

'Users' 탭에서 'Create new user'를 선택합니다.

![keycloak-setting-1](./images/keycloak-setting-13.png)

'Username'을 'user'로 하고 'create'를 누릅니다.

![keycloak-setting-1](./images/keycloak-setting-14.png)

'Password'를 'pass'로 설정합니다.

![keycloak-setting-1](./images/keycloak-setting-15.png)

'Realm settings'에서 'User registration', 'Forgot password' 및 'Remember Me'를 선택합니다.

![keycloak-setting-1](./images/keycloak-setting-17.png)

'Clients' 탭에서 'account-console'에 있는 'link'를 클릭합니다.

![keycloak-setting-1](./images/keycloak-setting-16.png)

Devtools를 활성화 한 뒤, 'Account Console'에서 'user/pass'를 입력하고 'sign in'을 합니다.

![keycloak-setting-1](./images/keycloak-setting-19.png)

⚠️ token의 'scope'에서 'estate'가 설정되어 있는 것을 확인합니다.

![keycloak-setting-1](./images/keycloak-setting-20.png)

https://minikube.me/estate/openapi/swagger-ui.index.html에 접속한 후

'Authorize'에서 해당 토큰을 입력하고 'login'합니다.

POST /estate/building에서 Try it out을 선택한 뒤 아래 body를 입력하고, try it out을 선택합니다. (units는 개인적인 개발 한계를 확인하기 위해 넣은 것이며 현재는 별도로 영향이 없습니다.)

```json
{"id":0,"name":"string","address":"string","city":"string","state":"string","zipCode":"string","description":"string"}
```

200 Ok를 확인합니다.

# Documentations

- [Kubernetes에 대하여](s/documentationdoc  #kubernetes에-대하여)
  - [클러스터란](./docs/documentation#클러스터란)
    - [클러스터 구성 예](./docs/documentation#클러스터-구성-예)
    - [조직 / 지역별 클러스터 운영 예시](./docs/documentation#조직--지역별-클러스터-운영-예시)
  - [Resources](./docs/documentation#resources)
    - [Node](./docs/documentation#node)
    - [Workload](./docs/documentation#workload)
    - [Network](./docs/documentation#network)
    - [Storage](./docs/documentation#storage)
    - [Configuration](./docs/documentation#configuration)
    - [Release](./docs/documentation#release)
  - [Namespace](./docs/documentation#namespace)
    - [네임스페이스(Namespace)](./docs/documentation#네임스페이스namespace)
- [Istio란](./docs/documentation#istio란)
  - [Istio Gateway, Virtual Services, Destination Rules](./docs/documentation#istio-gateway-virtual-services-destination-rules)
    - [1. Istio Gateway](./docs/documentation#1-istio-gateway)
    - [2. Virtual Services](./docs/documentation#2-virtual-services)
    - [3. Destination Rules](./docs/documentation#3-destination-rules)
- [Observability](./docs/documentation#observability)
  - [Istio Distributed Tracing (Jaeger, OpenZipkin)](./docs/documentation#istio-distributed-tracing-jaeger-openzipkin)
  - [Istio Metrics (Prometheus, Kiali, Grafana, Alert Manager, Mail Server)](./docs/documentation#istio-metrics-prometheus-kiali-grafana-alert-manager-mail-server)
    - [Prometheus \& Prometheus Alert Manager](./docs/documentation#prometheus--prometheus-alert-manager)
    - [Kiali](./docs/documentation#kiali)
    - [Grafana](./docs/documentation#grafana)
- [EFK Stack](./docs/documentation#efk-stack)
  - [Fluentd](./docs/documentation#fluentd)
  - [ElasticSearch](./docs/documentation#elasticsearch)
  - [Kibana](./docs/documentation#kibana)
- [Kubernetes Dashboard](./docs/documentation#kubernetes-dashboard)
- [Auth Server](./docs/documentation#auth-server)
  - [Spring Auth Server](./docs/documentation#spring-auth-server)
  - [Keycloak](./docs/documentation#keycloak)
  - [Resource Server](./docs/documentation#resource-server)
  - [OAuth Grant Types](./docs/documentation#oauth-grant-types)
- [Project](./docs/documentation#project)
  - [Artifact 저장소](./docs/documentation#artifact-저장소)
  - [MyBatis, QueryDSL, jOOQ](./docs/documentation#mybatis-querydsl-jooq)