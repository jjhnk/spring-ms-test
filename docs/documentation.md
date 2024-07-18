목차

- [Kubernetes에 대하여](#kubernetes에-대하여)
  - [클러스터란](#클러스터란)
    - [클러스터 구성 예](#클러스터-구성-예)
    - [조직 / 지역별 클러스터 운영 예시](#조직--지역별-클러스터-운영-예시)
  - [Resources](#resources)
    - [Node](#node)
    - [Workload](#workload)
    - [Network](#network)
      - [1. Service](#1-service)
        - [주요 유형](#주요-유형)
      - [2. Endpoint](#2-endpoint)
      - [3. Ingress](#3-ingress)
        - [주요 기능](#주요-기능)
      - [예시](#예시)
      - [NetworkPolicy](#networkpolicy)
    - [Storage](#storage)
    - [Configuration](#configuration)
    - [Release](#release)
  - [Namespace](#namespace)
    - [네임스페이스(Namespace)](#네임스페이스namespace)
    - [설명](#설명)
- [Istio란](#istio란)
    - [Istio의 주요 기능](#istio의-주요-기능)
    - [Istio의 아키텍처](#istio의-아키텍처)
    - [배포 및 유연성](#배포-및-유연성)
    - [장점과 단점](#장점과-단점)
  - [Istio Gateway, Virtual Services, Destination Rules](#istio-gateway-virtual-services-destination-rules)
    - [1. Istio Gateway](#1-istio-gateway)
    - [2. Virtual Services](#2-virtual-services)
    - [3. Destination Rules](#3-destination-rules)
      - [사용 예시](#사용-예시)
- [Observability](#observability)
  - [Istio Distributed Tracing (Jaeger, OpenZipkin)](#istio-distributed-tracing-jaeger-openzipkin)
    - [Jaeger in Istio](#jaeger-in-istio)
    - [W3C Trace Context Headers](#w3c-trace-context-headers)
    - [OpenZipkin의 B3 Headers](#openzipkin의-b3-headers)
    - [통합 및 상호 운용성](#통합-및-상호-운용성)
    - [실습](#실습)
  - [Istio Metrics (Prometheus, Kiali, Grafana, Alert Manager, Mail Server)](#istio-metrics-prometheus-kiali-grafana-alert-manager-mail-server)
    - [Prometheus \& Prometheus Alert Manager](#prometheus--prometheus-alert-manager)
      - [주요 기능:](#주요-기능-1)
      - [구성 요소:](#구성-요소)
      - [실습](#실습-1)
    - [Kiali](#kiali)
      - [주요 기능](#주요-기능-2)
      - [활용 예시](#활용-예시)
      - [실습](#실습-2)
    - [Grafana](#grafana)
      - [주요 기능:](#주요-기능-3)
      - [실습:](#실습-3)
- [EFK Stack](#efk-stack)
  - [Fluentd](#fluentd)
      - [주요 기능:](#주요-기능-4)
      - [아키텍처 구성:](#아키텍처-구성)
  - [ElasticSearch](#elasticsearch)
    - [Elastic Observability](#elastic-observability)
      - [주요 기능:](#주요-기능-5)
  - [Kibana](#kibana)
      - [주요 기능:](#주요-기능-6)
      - [실습](#실습-4)
- [Kubernetes Dashboard](#kubernetes-dashboard)
- [Auth Server](#auth-server)
    - [인증 서버의 주요 개념](#인증-서버의-주요-개념)
    - [1. **Realm(영역)**](#1-realm영역)
    - [2. **Clients(클라이언트)**](#2-clients클라이언트)
    - [3. **Client Scopes(클라이언트 스코프)**](#3-client-scopes클라이언트-스코프)
    - [4. **Roles(역할)**](#4-roles역할)
    - [5. **Users(사용자)**](#5-users사용자)
  - [Spring Auth Server](#spring-auth-server)
  - [Keycloak](#keycloak)
    - [주요 기능](#주요-기능-7)
  - [Resource Server](#resource-server)
    - [주요 기능 및 역할](#주요-기능-및-역할)
    - [동작 과정](#동작-과정)
    - [예시](#예시-1)
  - [OAuth Grant Types](#oauth-grant-types)
    - [1. Authorization Code Grant](#1-authorization-code-grant)
    - [2. Client Credentials Grant](#2-client-credentials-grant)
    - [3. Password Grant (Resource Owner Password Credentials)](#3-password-grant-resource-owner-password-credentials)
    - [4. Implicit Grant](#4-implicit-grant)
    - [5. Refresh Token Grant](#5-refresh-token-grant)
    - [Keycloack 인증 관련](#keycloack-인증-관련)
- [Project](#project)
  - [Artifact 저장소](#artifact-저장소)
    - [Maven Repository](#maven-repository)
    - [Nexus OSS](#nexus-oss)
      - [적용](#적용)
  - [MyBatis, QueryDSL, jOOQ](#mybatis-querydsl-jooq)
    - [공통점](#공통점)
    - [차이점](#차이점)
      - [MyBatis](#mybatis)
      - [QueryDSL](#querydsl)
      - [jOOQ](#jooq)
    - [예시](#예시-2)
      - [MyBatis](#mybatis-1)
      - [QueryDSL](#querydsl-1)
      - [jOOQ](#jooq-1)


# Kubernetes에 대하여


## 클러스터란

⚠️ Visual Studio Code (VS code)의 kubernetes 탭을 키고 보면서 읽어보세요.

![kubernetes-00](../images/kubernetes-00.png)

Kubernetes 클러스터는 여러 대의 물리적 또는 가상 머신(Node)들이 모여서 하나의 단일 유닛처럼 동작하는 시스템입니다. 클러스터는 애플리케이션 컨테이너를 자동으로 배포, 관리, 확장하는 데 사용되며, 모든 작업은 마스터 노드에 의해 조정됩니다. 각 클러스터는 일반적으로 다음과 같은 요소로 구성됩니다:

1. **마스터 노드**: 클러스터를 관리하고 제어하는 역할을 합니다. API 서버, 스케줄러, 컨트롤러 매니저 등이 포함됩니다.
2. **워커 노드**: 실제 애플리케이션 컨테이너가 배포되고 실행되는 머신들입니다. 각 노드에는 kubelet, 컨테이너 런타임, kube-proxy가 실행됩니다.

⚠️ 일반적으로 Master Node 하나에 Worker Node 2개를 바탕으로 테스트합니다.

### 클러스터 구성 예

엔터프라이즈 환경에서 Kubernetes 클러스터는 다음과 같은 기준에 따라 구성되고 운영됩니다:

1. **개발/테스트 클러스터**:
   - **목적**: 개발자들이 새로운 기능을 테스트하고 애플리케이션을 개발하는 환경.
   - **수**: 팀별로 또는 프로젝트별로 1~2개.
   - **구성**: 상대적으로 작은 규모의 클러스터로, 비용 효율성을 위해 리소스가 제한될 수 있음.

2. **프로덕션 클러스터**:
   - **목적**: 실제 사용자에게 서비스가 제공되는 환경.
   - **수**: 고가용성과 재해 복구를 위해 각 주요 리전마다 1개 이상의 클러스터.
   - **구성**: 높은 가용성과 성능을 위해 다수의 노드로 구성되며, 리전별로 분산 배치됨.

3. **워크로드 전용 클러스터**:
   - **목적**: 특정 작업(예: 데이터베이스, AI/ML, CI/CD 파이프라인) 전용으로 사용.
   - **수**: 각 워크로드 유형별로 1개 이상의 클러스터.
   - **구성**: 해당 워크로드에 최적화된 설정과 리소스 할당.

4. **멀티테넌트 클러스터**:
   - **목적**: 여러 팀이나 프로젝트가 자원을 공유하면서도 독립적으로 운영할 수 있는 환경.
   - **수**: 큰 조직의 경우 몇 개의 멀티테넌트 클러스터.
   - **구성**: 네임스페이스와 리소스 쿼터를 사용하여 각 테넌트의 자원을 격리.

### 조직 / 지역별 클러스터 운영 예시

- **소규모 조직**: 개발, 테스트, 프로덕션 용도로 3~5개의 클러스터.
- **중규모 조직**: 각 주요 리전마다 프로덕션 클러스터를 운영하고, 개발/테스트 클러스터를 포함하여 5~10개의 클러스터.
- **대규모 조직**: 글로벌 리전별로 프로덕션 클러스터를 운영하고, 다양한 워크로드와 환경을 위해 10개 이상의 클러스터 운영.

- **미국 리전**:
  - 프로덕션 클러스터 1개
  - 개발 클러스터 1개
  - 테스트 클러스터 1개

- **유럽 리전**:
  - 프로덕션 클러스터 1개
  - 개발 클러스터 1개

- **아시아 리전**:
  - 프로덕션 클러스터 1개
  - 테스트 클러스터 1개

- **특정 워크로드 전용 클러스터**:
  - 데이터베이스 전용 클러스터 1개
  - CI/CD 파이프라인 클러스터 1개

## Resources

![kubernetes-01](../images/kubernetes-01.png)

### Node
**노드**는 Kubernetes 클러스터의 구성 요소로, 애플리케이션 컨테이너가 실제로 실행되는 서버입니다.

- **마스터 노드**: 클러스터를 제어하고 관리하는 역할을 합니다. API 서버, 스케줄러, 컨트롤러 매니저 등이 실행됩니다.
- **워크 노드**: 애플리케이션 컨테이너가 실행되는 서버로, 각 노드는 Kubelet, 컨테이너 런타임, kube-proxy 등을 실행합니다.

![kubernetes-03](../images/kubernetes-03.png)

1. describe: kubernetes의 설정을 설명(describe)합니다.
2. log: 해당 pod의 log를 출력합니다. (일반적으로 'elasticsearch'에서도 확인 가능합니다.)
3. terminal: 해당 pod의 'tty'를 입력합니다.
4. vscode의 'remote window'를 통해 연결합니다
5. 1과 같습니다.
7. get: 해당 pod의 일반적인 상황을 알려줍니다.

![kubernetes-04](../images/kubernetes-04.png)

terminal을 열면 위와 같은 그림이 나타납니다.

1. pod 내부의 product에 대해 terminal을 실행합니다.
2. sidecar인 istio proxy (istio envoy; istiod와 상호작용)의 terminal을 실행합니다.

⚠️ 일반적으로 pod 내부의 service는 보안을 위해 alpine을 적용하는 경우가 많습니다. Network Test 등을 위해서 sidecar을 이용하는 경우가 있습니다.

### Workload

**워크로드**는 Kubernetes 클러스터에서 실행되는 애플리케이션이나 작업을 의미합니다.

- **Pod**: Kubernetes에서 배포되고 관리되는 최소의 컴퓨팅 단위로, 하나 이상의 컨테이너로 구성됩니다.
- **Deployment**: 선언적으로 애플리케이션을 배포하고 관리하는 방식으로, 자동 롤아웃과 롤백 기능을 제공합니다.
- **StatefulSet**: 상태 정보를 가지는 애플리케이션을 배포하고 관리하는데 사용됩니다.
- **DaemonSet**: 클러스터의 각 노드에서 특정한 Pod를 실행하기 위해 사용됩니다.
- **Job**: 단발성 작업을 수행하기 위해 사용되며, 완료될 때까지 실행됩니다.
- **CronJob**: 정기적으로 반복 실행되는 작업을 정의합니다.

![kubernete-05](../images/kubernetes-05.png)

hands-on namespace(1)에 5개의 pods(2)가 있는 것을 확인할 수 있습니다.

### Network
**네트워크**는 Kubernetes 클러스터의 통신을 관리하는 시스템입니다.

![kubernetes-06](../images/kubernetes-06.png)

#### 1. Service
**Service**는 Kubernetes에서 애플리케이션을 네트워크에 노출하고 로드 밸런싱을 제공하기 위해 사용되는 추상화 개념입니다. 서비스는 클러스터 내부에서 실행되는 Pod들의 집합을 하나의 네트워크 엔드포인트로 노출합니다. 이는 Pod들이 동적으로 생성되고 삭제되더라도 안정적인 접근 방식을 제공하는 역할을 합니다.

##### 주요 유형
- **ClusterIP**: 기본 서비스 유형으로, 클러스터 내부에서만 접근할 수 있는 내부 IP 주소를 제공합니다.
- **NodePort**: 각 노드에서 특정 포트를 열어 외부 트래픽을 클러스터의 서비스로 라우팅합니다.
- **LoadBalancer**: 클라우드 제공업체의 로드 밸런서를 사용하여 외부 트래픽을 서비스로 라우팅합니다.
- **ExternalName**: 서비스가 DNS 이름을 통해 외부 서비스에 대한 접근을 제공하도록 합니다.

#### 2. Endpoint
**Endpoint**는 Service와 연결된 실제 Pod의 IP 주소와 포트를 나타냅니다. Kubernetes는 Service와 연결된 Pod들의 네트워크 엔드포인트를 자동으로 관리하며, 이를 통해 Service가 실제로 어느 Pod에 연결되는지를 정의합니다.

#### 3. Ingress
**Ingress**는 HTTP 및 HTTPS 트래픽을 클러스터 내의 서비스로 라우팅하는 규칙을 정의합니다. Ingress 리소스를 사용하면 외부 트래픽을 클러스터 내부의 서비스로 유연하게 라우팅할 수 있으며, URL 경로 기반 라우팅, 호스트 기반 라우팅, SSL/TLS 종료 등을 지원합니다.

##### 주요 기능
- **경로 기반 라우팅**: URL 경로에 따라 트래픽을 다양한 서비스로 라우팅합니다.
- **호스트 기반 라우팅**: 호스트 이름에 따라 트래픽을 라우팅합니다.
- **TLS/SSL 지원**: SSL 인증서를 사용하여 HTTPS를 통한 보안 트래픽을 제공합니다.

#### 예시

```yaml
apiVersion: v1
kind: Endpoints
metadata:
  name: my-service-a
subsets:
  - addresses:
      - ip: 192.168.1.1 # Endpoint의 IP는 각 Pod에 할당된 IP입니다.
    ports:
      - port: 9376

apiVersion: v1
kind: Endpoints
metadata:
  name: my-service-b
subsets:
  - addresses:
      - ip: 192.168.1.3
    ports:
      - port: 9376

apiVersion: v1
kind: Service
metadata:
  name: my-service
spec:
  selector:
    app: MyApp
  ports:
    - protocol: TCP
      port: 80 # my-service-a, my-service-b라는 pod의 endpoint '192.168.1.1:80', '192.168.1.3:80'이 내부 클러스터 80에서 expose됩니다. 일반적인 coredns resolution rule에 따르면 my-service.<namespace>.svc.cluster.local로 내부에서 접근 가능합니다.
      targetPort: 9376

apiVersion: networking.k8s.io/v1
kind: Ingress
metadata:
  name: my-ingress
spec:
  rules:
  - host: myapp.example.com
    http:
      paths:
      - path: /
        pathType: Prefix
        backend:
          service:
            name: my-service
            port:
              number: 80 # my-service라는 service의 클러스터 외부에서의 접근은 myapp.example.com:80에서 접근 가능합니다.
  tls:
  - hosts:
    - myapp.example.com
    secretName: my-tls-secret
```

⚠️ 4. Port forward는 클러스터 외부에 해당 Service를 expose합니다.

#### NetworkPolicy

NetworkPolicy는 클러스터 내의 Pod 간 트래픽을 제어하기 위한 방법입니다. Ingress 및 Egress 트래픽을 제어할 수 있습니다.

```yaml
apiVersion: networking.k8s.io/v1
kind: NetworkPolicy
metadata:
  name: deny-all
  namespace: development
spec:
  podSelector: {}
  policyTypes:
  - Ingress
  - Egress
  ingress: []
  egress: []

---

apiVersion: networking.k8s.io/v1
kind: NetworkPolicy
metadata:
  name: allow-ingress-from-cidr
  namespace: default
spec:
  podSelector:
    matchLabels:
      app: my-app
  policyTypes:
  - Ingress
  ingress:
  - from:
    - ipBlock:
        cidr: 192.168.1.0/24
        except:
        - 192.168.1.5/32'

---

apiVersion: cilium.io/v2
kind: CiliumNetworkPolicy
metadata:
  name: allow-egress-to-domain
  namespace: default
spec:
  endpointSelector:
    matchLabels:
      app: my-app
  egress:
  - toEndpoints:
    - matchLabels:
        "k8s:io.kubernetes.pod.namespace": "kube-system"
  - toFQDNs:
    - matchName: "*.example.com"

```

### Storage
**스토리지**는 컨테이너에서 생성된 데이터를 저장하고 관리하는 방법입니다.

- **PersistentVolume (PV)**: 클러스터 내에서 관리되는 스토리지 리소스를 나타냅니다.
- **PersistentVolumeClaim (PVC)**: 사용자와 애플리케이션이 스토리지를 요청하는 방식입니다.
- **StorageClass**: 다양한 스토리지 제공자의 스토리지 종류를 정의합니다.

⚠️ 이 프로젝트에서는 별도로 Storage를 설정하지 않았습니다. Job을 통해 Pods들의 상태를 공유할 수도 있습니다.

⚠️ 별도로 Storage를 설정하지 않았으므로 delete namespace 혹은 minikube stop을 하면 모든 설정이 날아갑니다.

### Configuration
**구성**은 Kubernetes 클러스터와 그 안에서 실행되는 애플리케이션의 설정을 정의하는 방법입니다.

- **ConfigMap**: 비밀이 아닌 설정 데이터를 저장하고 관리합니다.
- **Secret**: 비밀번호, API 키 등 민감한 데이터를 안전하게 저장합니다.

아래 그림은 Kubernetes ConfigMap이 어떻게 bind 되는지를 보여줍니다.

![kubernete-07](../images/kubernetes-07.png)

아래 그림은 Kubernetes SecretMap이 어떻게 bind 되는지 보여줍니다.

![kubernetes-08](../images/kubernetes-08.png)

⚠️ Secret Map은 Production에서 file 형식으로 암호화되는 경우가 많습니다.44444444444444444444444444444444444444

### Release
**릴리스**는 애플리케이션의 특정 버전을 배포하고 관리하는 과정입니다.

- **Rolling Update**: 애플리케이션을 점진적으로 업데이트하여 중단 없이 배포를 진행합니다.
- **Rollback**: 문제가 발생했을 때 이전 버전으로 되돌리는 기능을 제공합니다.
- **Helm**: Helm을 사용하면 애플리케이션을 릴리스 단위로 관리할 수 있습니다. `helm install`, `helm upgrade`, `helm rollback` 등의 명령어를 사용하여 애플리케이션 배포와 관리를 수행합니다.
- **Canary Deployment**: 새로운 애플리케이션 버전을 일부 사용자에게만 배포하여 테스트하는 방식입니다.
- **Blue-Green Deployment**: 두 개의 동일한 환경을 사용하여, 새로운 버전을 배포하고 테스트한 후 트래픽을 전환하는 방식입니다.

## Namespace

### 네임스페이스(Namespace)

![kubenetes-02](../images/kubernetes-02.png)

네임스페이스는 Kubernetes 클러스터 내에서 리소스를 논리적으로 분리하여 관리하기 위한 방법입니다. 이를 통해 팀, 애플리케이션, 환경을 격리하고, 각각에 대해 별도의 리소스 쿼터, 접근 제어, 네트워크 정책을 설정할 수 있습니다. 아래는 하나의 네임스페이스에 대해 리소스 쿼터, RBAC(Role-Based Access Control), 네트워크 정책을 설정하는 예시입니다.

```yaml
apiVersion: v1
kind: Namespace
metadata:
  name: app-team-dev

---

apiVersion: v1
kind: ResourceQuota
metadata:
  name: compute-resources
  namespace: app-team-dev
spec:
  hard:
    requests.cpu: "2"
    requests.memory: 4Gi
    limits.cpu: "4"
    limits.memory: 8Gi

---

apiVersion: rbac.authorization.k8s.io/v1
kind: Role
metadata:
  namespace: app-team-dev
  name: developer-role
rules:
- apiGroups: [""]
  resources: ["pods", "services", "deployments"]
  verbs: ["get", "list", "watch", "create", "update", "delete"]

---

apiVersion: rbac.authorization.k8s.io/v1
kind: RoleBinding
metadata:
  name: developer-binding
  namespace: app-team-dev
subjects:
- kind: User
  name: developer1
  apiGroup: rbac.authorization.k8s.io
roleRef:
  kind: Role
  name: developer-role
  apiGroup: rbac.authorization.k8s.io

---

apiVersion: networking.k8s.io/v1
kind: NetworkPolicy
metadata:
  name: default-deny-all
  namespace: app-team-dev
spec:
  podSelector: {}
  policyTypes:
  - Ingress
  - Egress
  ingress: []
  egress: []

---

apiVersion: networking.k8s.io/v1
kind: NetworkPolicy
metadata:
  name: allow-internal
  namespace: app-team-dev
spec:
  podSelector: {}
  policyTypes:
  - Ingress
  - Egress
  ingress:
  - from:
    - podSelector: {}
  egress:
  - to:
    - podSelector: {}

---

apiVersion: networking.k8s.io/v1
kind: NetworkPolicy
metadata:
  name: allow-external
  namespace: app-team-dev
spec:
  podSelector: {}
  policyTypes:
  - Egress
  egress:
  - to:
    - ipBlock:
        cidr: 0.0.0.0/0
```

### 설명

1. **네임스페이스(Namespace)**:
   - `app-team-dev` 네임스페이스를 생성하여, 특정 팀의 개발 환경을 격리합니다.

2. **리소스 쿼터(Resource Quota)**:
   - `compute-resources` 리소스 쿼터를 설정하여, 해당 네임스페이스 내에서 사용 가능한 CPU와 메모리 리소스를 제한합니다.
   - `requests.cpu`: 2, `requests.memory`: 4Gi
   - `limits.cpu`: 4, `limits.memory`: 8Gi

3. **역할(Role)과 역할 바인딩(Role Binding)**:
   - `developer-role` 역할을 생성하여, 특정 리소스(`pods`, `services`, `deployments`)에 대한 읽기, 쓰기, 업데이트, 삭제 권한을 부여합니다.
   - `developer-binding` 역할 바인딩을 통해, `developer1` 사용자에게 `developer-role` 역할을 부여합니다.

4. **기본 네트워크 정책(Network Policy)**:
   - `default-deny-all` 정책을 통해 기본적으로 모든 인그레스(Ingress)와 이그레스(Egress) 트래픽을 차단합니다.

5. **내부 트래픽 허용 정책**:
   - `allow-internal` 정책을 통해 네임스페이스 내의 모든 Pod 간의 인그레스와 이그레스 트래픽을 허용합니다.

6. **외부 트래픽 허용 정책**:
   - `allow-external` 정책을 통해 네임스페이스 내의 Pod가 외부 네트워크(인터넷)로 이그레스 트래픽을 허용합니다.

이는 다음과 같은 Best Practice를 따릅니다 (links: [Kubernetes best practices: Organizing with Namespaces](https://cloud.google.com/blog/products/containers-kubernetes/kubernetes-best-practices-organizing-with-namespaces), [10 Kubernetes Namespace Best Practices to Start Following](https://release.com/blog/10-kubernetes-namespace-best-practices-to-start-following))

- **리소스 격리**: 네임스페이스를 사용하여 팀, 애플리케이션, 환경별로 리소스를 격리합니다.
- **리소스 제한**: 리소스 쿼터를 통해 각 네임스페이스의 자원 사용을 제한하여 공평한 자원 분배를 보장합니다.
- **역할 기반 접근 제어**: RBAC를 통해 특정 사용자에게 필요한 권한만 부여하여 보안성을 높입니다.
- **네트워크 보안**: 기본 네트워크 정책으로 모든 트래픽을 차단하고, 필요한 트래픽만 허용하여 보안성을 강화합니다.

⚠️ 해당 프로젝트는 변경이 빈번하게 발생하기 때문에, 일반적인 Best Practice를 따르지 않고 Namespace를 기능별로 나누었습니다. 이는 일반적인 규칙에서 벗어나지만, 'hands-on' 및 'hy-oltp'와 같은 서비스를 다른 의존성 없이 하드 클린(리셋)할 수 있는 장점이 있습니다. 실제 프로젝트에서 dev/qa/uat/prod 환경을 구현할 때는 프로젝트의 namespace를 병합하면 됩니다. 그렇지 않다면, 이 프로젝트와 같이 어느 정도 안정성 테스트를 한 뒤 병합하는 것도 하나의 방법입니다.


# Istio란

Istio는 마이크로서비스 기반 애플리케이션의 복잡성을 관리하는 오픈 소스 Service Mesh입니다. 2016년 Google, IBM, Lyft에 의해 설립되었으며 현재 Cloud Native Computing Foundation(CNCF)의 중요한 구성 요소로 자리 잡았습니다.

다른 Service Mesh로서는 Linkerd (50명 이상 유료), Consul 등이 있습니다.

🔗 Relevant Links:
1. https://matduggan.com/k8s-service-meshes/
2. https://cloud.google.com/learn/what-is-istio
3. https://istio.io/

### Istio의 주요 기능

1. **트래픽 관리**: Istio는 라우팅 규칙, 재시도, 장애 복구, 오류 주입과 같은 기능을 통해 트래픽 동작을 세밀하게 제어할 수 있습니다. 이를 통해 A/B 테스트, 카나리아 배포, 블루/그린 배포 등의 고급 트래픽 관리 전략을 애플리케이션 코드를 변경하지 않고도 구현할 수 있습니다.

2. **보안**: Istio는 서비스 간 인증, 인가, 암호화를 위한 상호 TLS(mTLS)를 제공하여 보안을 강화합니다. 정책 집행을 지원하고 서비스 간의 안전한 통신을 보장하여 효과적으로 제로 트러스트 보안 모델을 구현합니다.

3. **가시성**: Istio는 모든 서비스 상호 작용에서 메트릭, 로그, 트레이스를 수집하여 Mesh 내 서비스의 동작을 깊이 있게 파악할 수 있도록 합니다. Prometheus, Grafana, Jaeger와 같은 모니터링 및 로깅 도구와 통합되어 개발자가 애플리케이션을 이해하고 문제를 해결할 수 있도록 돕습니다.

4. **정책 집행**: Istio는 관리자가 액세스 제어, 속도 제한 등의 정책을 정의하고 집행할 수 있게 합니다. 이를 통해 컴플라이언스를 유지하고 서비스가 지정된 경계 내에서 운영되도록 보장합니다.

### Istio의 아키텍처

Istio의 아키텍처는 두 가지 주요 구성 요소로 나뉩니다:

1. **컨트롤 플레인**: 프록시를 관리하고 구성하여 트래픽을 라우팅합니다. 주요 구성 요소는 `istiod`로, 이전에 별도로 존재하던 트래픽 관리, 보안, 구성 관리 등을 통합합니다.

2. **데이터 플레인**: 각 서비스 인스턴스 내에 사이드카로 배포된 지능형 프록시(Envoy) 세트로 구성됩니다. 이 프록시들은 마이크로서비스 간의 모든 네트워크 트래픽을 처리하며, 컨트롤 플레인에서 정의된 규칙을 구현하고 원격 측정 데이터를 수집합니다.

### 배포 및 유연성

Istio는 온프레미스, 클라우드, Kubernetes, 가상 머신을 포함한 다양한 환경에 배포할 수 있어 매우 유연합니다. 다중 클러스터 및 다중 클라우드 설정을 지원하여 다양한 환경에서 원활한 통신 및 관리를 보장합니다.

### 장점과 단점

**장점**:
- 마이크로서비스 통신의 보안 및 가시성을 향상시킵니다.
- 트래픽 관리 및 배포 전략을 단순화합니다.
- 더 나은 모니터링 및 문제 해결을 위해 광범위한 메트릭 및 로그를 제공합니다.

**단점**:
- 설정 및 구성에 시간이 걸리며 올바른 구현을 보장하기 위해 노력이 필요합니다.
- Istio 구성 및 정책을 관리하고 유지하는 데 운영 오버헤드가 추가될 수 있습니다.


## Istio Gateway, Virtual Services, Destination Rules

Istio는 Service Mesh 내에서 트래픽 흐름을 구성하기 위해 여러 커스텀 리소스를 사용합니다. 이 중 중요한 구성 요소는 Gateway, Virtual Services, 그리고 Destination Rules입니다.

### 1. Istio Gateway

Istio Gateway는 Service Mesh로 들어오고 나가는 트래픽을 관리하기 위해 사용됩니다. 이를 통해 Mesh의 Ingress과 Egress에서 트래픽에 대한 정책을 적용할 수 있습니다. Gateway는 Edge Load Balancer이고 HTTP/TCP 연결을 수신합니다. Kubernetes Ingress와 유사하지만 더 광범위한 기능을 제공합니다.

**주요 기능**:
- Edge Load Balancer 구성
- TLS 종료 관리
- HTTP/TCP 속성에 따른 트래픽 라우팅 처리

### 2. Virtual Services

Virtual Services는 Service Mesh 내에서 서비스의 요청이 라우팅되는 방식을 제어하는 규칙을 정의합니다. 이는 애플리케이션 레이어에서 작동하며, 마이크로서비스 간의 트래픽 관리를 위한 세밀한 정책을 구성할 수 있습니다. Virtual Services를 사용하면 오류 주입, 재시도, 트래픽 분할 등의 고급 트래픽 관리 정책을 구현할 수 있습니다.

**주요 기능**:
- 조건에 따른 트래픽 라우팅 (예: 헤더, URI 경로)
- 트래픽 시프트, 미러링 및 재시도 구현
- 테스트 목적으로 오류 주입 구성

### 3. Destination Rules

Destination Rules는 라우팅 후 서비스로 향하는 트래픽에 적용되는 정책을 정의합니다. 이를 통해 서비스의 다양한 버전과 같은 서브셋을 구성하고, 로드 밸런싱, 연결 풀 크기, 외부 감지와 같은 정책을 지정할 수 있습니다.

**주요 기능**:
- 서비스 서브셋 정의 (예: 버전)
- 로드 밸런싱 전략 구성
- 연결 풀 및 외부 감지 관리

#### 사용 예시

Case 1.

see [expose-prometheus](./kubernetes/helm/istio-system/templates/expose-prometheus.yml)

```yaml
apiVersion: networking.istio.io/v1beta1
kind: Gateway
metadata:
  name: prometheus-gateway
  namespace: istio-system
spec:
  selector:
    istio: ingressgateway
  servers:
  - hosts:
    - "prometheus.minikube.me"
    port:
      number: 443 # prometheus.minikube.me:443로 들어오고 나가는 트래픽을 획득합니다.
      name: https-prom
      protocol: HTTPS
    tls:
      mode: SIMPLE
      credentialName: hands-on-certificate

---

apiVersion: networking.istio.io/v1beta1
kind: VirtualService
metadata:
  name: prometheus-vs
  namespace: istio-system
spec:
  hosts:
  - "prometheus.minikube.me"
  gateways:
  - prometheus-gateway
  http:
  - route:
    - destination:
        host: prometheus
        port:
          number: 9090 # prometheus-gateway의 prometheus.minikube.me:443으로 들어온 traffic을 prometheus:9090으로 라우팅합니다.

---

apiVersion: networking.istio.io/v1beta1
kind: DestinationRule
metadata:
  name: prometheus
  namespace: istio-system
spec:
  host: prometheus
  trafficPolicy:
    tls:
      mode: DISABLE # 라우트된 트래픽에 대해서 tls를 없애고 http 통신으로 적용합니다.

```

Case 2.

```yaml
apiVersion: networking.istio.io/v1alpha3
kind: Gateway
metadata:
  name: my-gateway
spec:
  selector:
    istio: ingressgateway # 기본 Istio Ingress Gateway 사용
  servers:
  - port:
      number: 80
      name: http
      protocol: HTTP
    hosts:
    - "*"

---

apiVersion: networking.istio.io/v1alpha3
kind: VirtualService
metadata:
  name: my-service
spec:
  hosts:
  - my-service.example.com
  gateways:
  - my-gateway
  http:
  - match:
    - uri:
        prefix: /v1
    route:
    - destination:
        host: my-service
        subset: v1
        weight: 90
  - match:
    - uri:
        prefix: /v2
    route:
    - destination:
        host: my-service
        subset: v2
        weight: 10

---

apiVersion: networking.istio.io/v1alpha3
kind: DestinationRule
metadata:
  name: my-destination
spec:
  host: my-service
  trafficPolicy:
    loadBalancer:
      consistentHash:
        httpHeaderName: "user-specific-header"
    connectionPool:
      http:
        http1MaxPendingRequests: 1000
        maxRequestsPerConnection: 100
    outlierDetection:
      consecutive5xxErrors: 5
      interval: 5s
      baseEjectionTime: 30s
      maxEjectionPercent: 50
  subsets:
  - name: v1
    labels:
      version: v1
    trafficPolicy:
      loadBalancer:
        simple: ROUND_ROBIN
  - name: v2
    labels:
      version: v2
    trafficPolicy:
      loadBalancer:
        simple: LEAST_CONN
```

# Observability

🔗 Relevant Links:

https://istio.io/latest/docs/tasks/observability

## Istio Distributed Tracing (Jaeger, OpenZipkin)

https://tracing.minikube.me/

🔗 Relevant Links:

- https://istio.io/latest/docs/tasks/observability/distributed-tracing
- [Istio observability](https://istio.io/latest/docs/tasks/observability/distributed-tracing/jaeger/)
- [Jaeger의 Istio 통합 문서](https://www.jaegertracing.io/docs/1.21/operator/#istio-integration)
- [W3C Trace Context](https://www.w3.org/TR/trace-context/)
- [B3 Propagation](https://github.com/openzipkin/b3-propagation)
- [Jaeger Tracer Configuration](https://www.jaegertracing.io/docs/1.59/client-features/)

📜 Documentation:

- [product-service/build.gradle](https://github.com/jjhnk/spring-ms-test/blob/83099ff3d9fe43e466d76c178f46468578d29003/microservices/product-composite-service/build.gradle#L45-L46)
- [product-service/TracingConfig.java](https://github.com/jjhnk/spring-ms-test/blob/develop/microservices/product-composite-service/src/main/java/hy/microservices/composite/product/config/TracingConfig.java)

Istio, Jaeger, W3C Trace Context Headers, 그리고 OpenZipkin의 B3 Headers는 모두 microservice architecture에서 분산 추적과 가시성을 제공하는 데 사용됩니다. 이들 구성 요소와 표준은 서로 연동되어 microservice 간의 통신을 추적하고 모니터링하는 데 중요한 역할을 합니다.

Brave는 분산 추적 계측 라이브러리입니다. Brave는 일반적으로 production의 request에 대해 timestamp data를 수집하고 trace context를 상관시키고 전파합니다. 일반적으로 추적 데이터는 Zipkin 혹은 Jaeger (Zipkin Support) 서버로 전송됩니다. (https://github.com/openzipkin/brave)

![jaeger](../images/jaeger.png)

![istio-jaeger](../images/istio-jaeger-system-architecture.png)

![istio-jaeger](../images/Istio%20jaeger.webp)

### Jaeger in Istio
Jaeger는 오픈 소스 end-to-end 분산 추적 도구로, microservice 기반 애플리케이션의 모니터링과 문제 해결을 위해 사용됩니다. Istio는 Jaeger와 통합되어 추적 기능을 제공합니다. 통합 시, Istio는 서비스 요청에 추적 헤더를 삽입하여 Jaeger가 추적 데이터를 수집하고 시각화할 수 있도록 합니다. 이를 통해 서비스 간의 지연 시간, 종속성, 성능 병목 현상을 이해할 수 있습니다.

### W3C Trace Context Headers
W3C Trace Context는 추적 컨텍스트 전파를 위한 표준 HTTP headers를 정의하는 spec입니다. 이를 통해 서로 다른 시스템과 서비스 간에 일관된 추적 정보를 전파할 수 있습니다. W3C Trace Context spec에서 정의한 주요 헤더는 `traceparent`와 `tracestate`입니다.

- **traceparent**: 표준화된 형식으로 추적 정보를 전달합니다.
- **tracestate**: 키-값 형식으로 시스템별 추적 정보를 전달합니다.

이 headers들은 서로 다른 추적 시스템과 library 간에 일관된 추적을 유지할 수 있게 하여, 다양한 vendor의 서비스를 아우르는 end-to-end 추적을 용이하게 합니다.

### OpenZipkin의 B3 Headers

B3는 서비스 간의 추적 컨텍스트를 전파하기 위한 HTTP headers 세트입니다. 원래 Twitter에서 개발되고 OpenZipkin에서 대중화된 B3 headers는 Jaeger를 포함한 다양한 추적 시스템에서 널리 지원됩니다. 주요 B3 headers는 다음과 같습니다:

- **X-B3-TraceId**: 전체 추적의 고유 식별자.
- **X-B3-SpanId**: 단일 span의 고유 식별자.
- **X-B3-ParentSpanId**: 부모 span을 나타냅니다.
- **X-B3-Sampled**: 추적이 샘플링되어야 하는지를 나타냅니다.
- **X-B3-Flags**: 디버그 flag에 사용됩니다.

### 통합 및 상호 운용성
Istio는 W3C Trace Context headers와 B3 headers를 모두 지원하도록 구성할 수 있어, 다양한 추적 설정을 제공할 수 있습니다. 이러한 표준을 지원함으로써 Istio는 Jaeger와 Zipkin을 포함한 다양한 추적 backend와 호환성을 보장합니다.

1. **Istio와 Jaeger 통합**:
   - Istio 사이드카는 자동으로 추적 headers를 캡처하고 전파합니다.
   - 추적 데이터는 Jaeger로 전송되어 집계 및 시각화됩니다.

2. **Trace Context 전파**:
   - Istio는 W3C Trace Context headers 또는 B3 headers를 사용하여 추적 컨텍스트를 전파할 수 있습니다.
   - 이를 통해 서비스 경계를 넘어 추적 정보가 보존되고, 다양한 추적 시스템에서 해석할 수 있습니다.

3. **상호 운용성**:
   - W3C와 B3 headers를 모두 지원함으로써 다양한 추적 표준 및 도구 간의 원활한 상호 운용성을 제공합니다.
   - 이는 여러 추적 시스템을 사용하거나 한 표준에서 다른 표준으로 이동하는 조직에 유연성을 제공합니다.

### 실습

1. 'https://tracing.minikube.me/' 로 접속합니다.

- 'Search'를 선택합니다.
- 'Service'에서 product-composite을 선택합니다.
- 'Operation'에서 적절한 아이템을 선택합니다.
⚠️ 현재 APP에 대한 실습을 진행하지 않았으므로 별도로 REQUEST가 없을 수 있습니다. 학습한 뒤에 재방문하세요.
- '적절한' trace를 선택합니다.

![jaeger-2](../images/jaeger-2.png)

2. 적절한 Span을 선택하고 내용을 확인합니다.

- tag에서 request 관련 정보를 확인할 수 있습니다.

![jaeger-3](../images/jaeger-3.png)

3. 에러가 발생한 경우 에러 정보가 나타납니다.

![jaeger-4](../images/jaeger-4.png)

4. 에러가 나타난 span을 선택합니다.

- 어떠한 에러가 나타났는지 확인 가능합니다.

![jaeger-5](../images/jaeger-5.png)

5. 어떤 쿼리가 나타났는지, span id가 무엇인지 확인할 수 있습니다.

![jaeger-6](../images/jaeger-6.png)

## Istio Metrics (Prometheus, Kiali, Grafana, Alert Manager, Mail Server)

⚠️ Jaeger, Kiali, Prometheus는 Istio addon으로 설치됩니다 (🔗 [Link](./getting-started.md#install-istio)). 다른 add on은 'https://istio.io/latest/docs/ops/integrations/'에서 찾아볼 수 있습니다.

🔗 Relevant Links:

- https://istio.io/latest/docs/tasks/observability/kiali/
- https://kiali.io/docs/installation/quick-start/
- https://istio.io/latest/docs/ops/integrations/prometheus/
- https://istio.io/latest/docs/tasks/observability/metrics/querying-metrics/
- https://github.com/jjhnk/spring-ms-test/blob/83099ff3d9fe43e466d76c178f46468578d29003/config/repo/application.yml#L21-L30
- https://prometheus.io/docs/alerting/latest/alertmanager/
- https://github.com/maildev/maildev

### Prometheus & Prometheus Alert Manager

**Prometheus**는 신뢰성과 확장성을 염두에 두고 설계된 오픈 소스 모니터링 및 경고 시스템입니다.

**Alert Manager**는 Prometheus의 Alert 기능이 있으나, 별도로 output channel이 필요할 경우 사용할 수 있는 dedicated 서비스입니다.

⚠️ Compatibility의 문제로 인해 grafana:9.0.1을 사용하고 있습니다. ([link](https://github.com/jjhnk/spring-ms-test/blob/83099ff3d9fe43e466d76c178f46468578d29003/kubernetes/helm/monitoring/grafana.yml#L26C33-L26C38))

#### 주요 기능:
1. **데이터 모델**: Prometheus는 메트릭 이름과 레이블이라는 키-값 쌍 세트를 통해 식별되는 고차원 데이터 모델을 사용합니다.
2. **PromQL**: 사용자가 시계열 데이터를 선택하고 집계할 수 있는 유연한 쿼리 언어로, 즉석 그래프, 테이블 및 경고를 쉽게 생성할 수 있습니다.
3. **독립적인 서버 노드**: Prometheus 서버는 독립적으로 작동하며, 로컬 스토리지에만 의존하여 신뢰성을 높입니다.
4. **풀 모델**: 메트릭 수집은 HTTP를 통한 풀 모델을 통해 이루어지며, Prometheus는 지정된 간격으로 대상에서 데이터를 수집합니다.
5. **경고**: 경고는 PromQL 표현식을 기반으로 정의되며, 알림 및 사일런싱을 처리하는 Alertmanager가 이를 관리합니다.
6. **시각화**: Prometheus는 내장된 표현 브라우저와 Grafana 통합을 포함하여 여러 시각화 도구를 지원합니다.

#### 구성 요소:
- **Prometheus 서버**: 시계열 데이터를 수집하고 저장하는 핵심 구성 요소입니다.
- **클라이언트 라이브러리**: 애플리케이션 코드를 계측하여 메트릭을 노출하는 데 사용됩니다.
- **익스포터**: HAProxy, StatsD 등과 같은 서드파티 데이터를 Prometheus 메트릭으로 변환하는 브릿지 역할을 합니다.
- **Alertmanager**: 중복 제거, 그룹화 및 다양한 알림 채널로의 라우팅을 처리하여 경고를 관리합니다.
- **푸시 게이트웨이**: 짧은 수명 작업과 같이 스크랩할 수 없는 메트릭에 사용됩니다.

#### 실습

1. 'system_cpu_usage'를 입력한 뒤 아래 'Graph'를 선택합니다.

![prometheus-2](../images/prometheus-2.png)

### Kiali

**Kiali**는 Istio 서비스 메쉬의 모니터링 및 관리 도구입니다. Kiali는 서비스 메쉬의 상태를 시각화하고, 문제를 파악하며, 설정을 관리하는 기능을 제공합니다. Istio의 Kiali는 prometheus의 scraped trace를 분석하여 데이터를 시각화합니다.

#### 주요 기능
1. **토폴로지 시각화**: Kiali의 기본 페이지는 서비스 메쉬의 전체적인 상태를 시각화하는 토폴로지 개요입니다. 서비스와 애플리케이션 정보를 결합하여 시스템의 동작을 종합적으로 요약합니다. 다양한 필터링, 정렬, 표시 옵션을 제공합니다.

2. **그래프 뷰**: 실시간 요청 트래픽과 Istio 설정 정보를 결합하여 서비스 메쉬의 동작을 시각화합니다. mTLS 문제, 지연 문제, 오류 트래픽 등을 식별할 수 있으며, 강력한 검색 및 숨김 기능을 갖추고 있습니다.

3. **건강 상태**: 그래프의 색상은 서비스 메쉬의 건강 상태를 나타냅니다. 예를 들어, 노드가 빨간색이나 주황색으로 표시되면 주의가 필요할 수 있습니다. 엣지의 색상은 구성 요소 간의 요청 상태를 나타냅니다.

4. **세부 정보 뷰**: Kiali는 서비스 메쉬 구성 요소의 리스트와 세부 정보 뷰를 제공합니다. 각 뷰는 건강 상태, 세부 정보, YAML 정의 및 시각화를 위한 링크를 포함합니다.

5. **Istio 설정 관리**: Kiali는 Istio 설정 객체(예: Virtual Services, Gateways)에 대한 고급 필터링 및 탐색 기능을 제공하며, 인라인 구성 편집 및 강력한 의미적 검증을 지원합니다.

6. **다중 클러스터 지원**: Kiali는 다중 클러스터 구성을 지원하여 하나의 interface에서 모든 앱, 워크로드, 서비스 및 Istio 설정을 관리할 수 있습니다.

7. **트레이싱 통합**: Jaeger와 같은 분산 추적 도구와의 통합을 통해 로그와 추적 정보를 통합적으로 제공합니다.

#### 활용 예시
Kiali를 사용하면 서비스 메쉬 내의 다양한 문제를 빠르게 파악하고 해결할 수 있습니다. 예를 들어, 서비스 간의 지연 문제를 그래프를 통해 시각적으로 확인하고, 특정 서비스의 설정을 수정하여 문제를 해결할 수 있습니다.

#### 실습

1. 'https://kiali.minikube.me/kiali/'에 접속합니다.

- 전체적인 상태를 나타냅니다.
- Traffic Graph를 보여줍니다.
- Application 목록을 보여줍니다.
- Workloads 목록을 보여줍니다.
- Services 목록을 보여줍니다.
- Istio Config 목록을 보여줍니다 (Config Edit 가능).
- Mesh 형태를 보여줍니다.

![kiali-2](../images/kiali-2.png)

2. 아래와 같이 설정합니다.

- Traffic Graph를 선택합니다.
- 1~5처럼 선택합니다.

![kiali-3](../images/kiali-3.png)

1. 테스트를 위해 다음과 같은 과정을 통해 쿼리를 생성합니다 (Postman 혹은 JMeter를 사용해도 좋습니다).

- Authorizaiton 획득

```pwsh
$access_token = (Invoke-RestMethod -Uri 'https://minikube.me/oauth2/token' -Method Post -Headers @{ 'Accept' = 'application/json'; 'Content-Type' = 'application/x-www-form-urlencoded'; 'Authorization' = 'Basic d3JpdGVyOnNlY3JldC13cml0ZXI=' } -Body @{ 'grant_type' = 'client_credentials'; 'scope' = 'product:read product:write';} -SkipCertificateCheck).access_token
```

- POST: Product

```pwsh
Invoke-WebRequest -Uri 'https://minikube.me/product-composite' -Method POST -ContentType 'application/json' -Headers @{Authorization = "Bearer $access_token"} -Body '{"productId":13,"name":"product name C","weight":300,"recommendations":[{"recommendationId":1,"author":"author 1","rate":1,"content":"content 1"},{"recommendationId":2,"author":"author 2","rate":2,"content":"content 2"},{"recommendationId":3,"author":"author 3","rate":3,"content":"content 3"}],"reviews":[{"reviewId":1,"author":"author 1","subject":"subject 1","content":"content 1"},{"reviewId":2,"author":"author 2","subject":"subject 2","content":"content 2"},{"reviewId":3,"author":"author 3","subject":"subject 3","content":"content 3"}]}' -SkipCertificateCheck
```

- GET: Product

```pwsh
Invoke-RestMethod -Uri 'https://minikube.me/product-composite/13' -Headers @{ "Authorization" = "Bearer $access_token" } -Method Get -SkipCertificateCheck
```

### Grafana

⚠️ Grafana

**Grafana**는 오픈 소스 관측 및 데이터 시각화 플랫폼입니다. 다양한 소스(Prometheus, Loki, Elasticsearch, InfluxDB, Postgres 등)에서 메트릭, 로그 및 트레이스를 시각화할 수 있도록 지원합니다. Grafana는 사용자가 데이터를 쉽게 분석하고 이해할 수 있도록 다양한 도구와 기능을 제공합니다.

#### 주요 기능:
1. **시각화**: 빠르고 유연한 클라이언트 측 그래프와 다양한 옵션을 제공하는 패널 플러그인을 통해 메트릭과 로그를 다양한 방식으로 시각화할 수 있습니다.
2. **동적 대시보드**: 템플릿 변수와 같은 동적이고 재사용 가능한 대시보드를 생성할 수 있습니다. 템플릿 변수는 대시보드 상단에 드롭다운 형식으로 나타납니다.
3. **메트릭 탐색**: 애드혹 쿼리와 동적 드릴다운을 통해 데이터를 탐색할 수 있습니다. 다양한 시간 범위, 쿼리 및 데이터 소스를 나란히 비교할 수 있는 분할 뷰를 지원합니다.
4. **로그 탐색**: 메트릭에서 로그로 전환할 때 라벨 필터를 유지하여 데이터를 신속하게 탐색할 수 있습니다.
5. **경고**: 메트릭과 로그 데이터를 기반으로 경고를 설정하고 관리할 수 있습니다. 다양한 알림 채널을 통해 경고를 받을 수 있습니다.

#### 실습:

'https://grafana.minikube.me/'에 접속합니다.

[readme.md](../readme.md#3-grafana) 을 참조하여 JVM (Micrometer)- Kubernetes - Prometheus by Istio 및 Hands-on Dashboard를 만든다.

0. 테스트

테스트를 위해 다음과 같이 진행한다.

- Authorizaiton 획득

```pwsh
$access_token = (Invoke-RestMethod -Uri 'https://minikube.me/oauth2/token' -Method Post -Headers @{ 'Accept' = 'application/json'; 'Content-Type' = 'application/x-www-form-urlencoded'; 'Authorization' = 'Basic d3JpdGVyOnNlY3JldC13cml0ZXI=' } -Body @{ 'grant_type' = 'client_credentials'; 'scope' = 'product:read product:write';} -SkipCertificateCheck).access_token
```

- POST: Product

```pwsh
Invoke-WebRequest -Uri 'https://minikube.me/product-composite' -Method POST -ContentType 'application/json' -Headers @{Authorization = "Bearer $access_token"} -Body '{"productId":2,"name":"product name C","weight":300,"recommendations":[{"recommendationId":1,"author":"author 1","rate":1,"content":"content 1"},{"recommendationId":2,"author":"author 2","rate":2,"content":"content 2"},{"recommendationId":3,"author":"author 3","rate":3,"content":"content 3"}],"reviews":[{"reviewId":1,"author":"author 1","subject":"subject 1","content":"content 1"},{"reviewId":2,"author":"author 2","subject":"subject 2","content":"content 2"},{"reviewId":3,"author":"author 3","subject":"subject 3","content":"content 3"}]}' -SkipCertificateCheck
```

- GET: Product를 5회 이상 보낸다.

```pwsh
Invoke-RestMethod -Uri 'https://minikube.me/product-composite/2?delay=3' -Headers @{ "Cache-Control" = "no-store"; "Authorization" = "Bearer $access_token" } -Method Get -SkipCertificateCheck

# result
# Invoke-RestMethod:
# {
#   "timestamp": "2024-07-17T09:41:36.507\u002B00:00",
#   "path": "/product-composite/2",
#   "status": 500,
#   "error": "Internal Server Error",
#   "requestId": "9f164048-26132",
#   "message": "Did not observe any item or terminal signal within 2000ms in \u0027onErrorResume\u0027 (and no fallback has been configured)"
# }

# assert that below "Fallback product..."
# {
#     "productId": 2,
#     "name": "Fallback product2",
#     "weight": 2,
#     "recommendations": [
#         {
#            ...
# }
```

1. 'Hands-on Dashboard'으로 갑니다.

- Service가 Fault로부터 Retry를 한 횟수를 나타냅니다.
- Circuit Breaker가 나타난 상태를 나타냅니다 (refer to: https://github.com/resilience4j/resilience4j)
- Alert가 발생한 횟수를 나타냅니다.

📜 Document

- https://github.com/jjhnk/spring-ms-test/blob/83099ff3d9fe43e466d76c178f46468578d29003/microservices/product-composite-service/build.gradle#L51-L52
- https://github.com/jjhnk/spring-ms-test/blob/83099ff3d9fe43e466d76c178f46468578d29003/microservices/product-composite-service/build.gradle#L55
- https://github.com/jjhnk/spring-ms-test/blob/83099ff3d9fe43e466d76c178f46468578d29003/config/repo/product-composite.yml#L139-L164

![grafana-2](../images/grafana-2.png)


2. Notification Channel을 추가하기 위해서 다음의 과정을 수행합니다.

- 'Notification Channel'을 선택합니다
- 'Add Channel'을 선택합니다.

![grafana-3](../images/grafana-3.png)

3. 아래 화면과 같이 입력한 뒤 'Save'를 눌러 저장합니다.

- Name 'alertmanager'
- Type 'Prometeus AlertManager'
- Url 'http://alertmanager:9093'

![grafana-4](../images/grafana-4.png)

4. 다시 'Hands-on Dashboard'로 돌아가서 'Circuit Breaker'의 'Edit'을 선택합니다.

![grafana-5](../images/grafana-5.png)

5. 'Alert'의 'Notifications'에 'alertmanager'를 추가한 뒤 'Apply'를 누릅니다.

⚠️ 'Dashboard' 화면으로 돌아온 뒤 저장 (Ctrl+S)를 하여야 저장됩니다.

![grafana-6](../images/grafana-6.png)


6. 다시 한 번 Product (ID: 2)를 지우고 테스트를 진행합니다.


```pwsh
Invoke-RestMethod -Uri 'https://minikube.me/product-composite/2' -Headers @{ "Authorization" = "Bearer $access_token" } -Method DELETE -SkipCertificateCheck
```

이후는 0번 과정 참조.

7. MailDev(link: https://mail.minikube.me/)에 접속한다.

- Grafana 및 Prometheus Alertmanager에서 전송된 메일을 확인한다.

![maildev-1](../images/maildev-1.png)


8. RECAP

Grafana는 자체적인 Alert System을 지니고 있다 (3번 과정 참조). 하지만 별도로 AlertManager를 Notification Channel로 추가한 것은 다음과 같은 이유가 있다.

1. Scalability Message Operator: Grafana는 다양한 기능을 가지고 있으며, 하나의 기능 하나만으로 확장을 하기는 너무 많은 자원이 소요된다. 또한 Alert 기능은 상황에 resource 및 context에 따라서 다각화 할 필요가 있다.
2. Message Flexibility: Grafana 자체적인 메시지는 내부적으로 사용하기는 괜찮으나 외부에 메시지를 보내기에는 부족할 수 있다. 따라서 이러한 customization 기능을 활용하기 위해 AlertManager를 별도로 추가한다.

📜 Documentation

https://github.com/jjhnk/spring-ms-test/blob/83099ff3d9fe43e466d76c178f46468578d29003/kubernetes/helm/monitoring/alertmanager.yml#L29-L63 참조.

https://medium.com/@sanskaragrawalla/monitoring-alerting-with-prometheus-grafana-alertmanager-58e732183162 참조.

# EFK Stack

https://kibana.minikube.me

## Fluentd

**Fluentd**는 통합 로깅 계층을 제공하는 오픈 소스 데이터 수집기입니다. 다양한 데이터 소스로부터 데이터를 수집하고, 이를 일관된 JSON 형식으로 구조화하여 여러 백엔드 시스템으로 전송할 수 있습니다.

🔗 Relevant Links

- https://fluentbit.io/blog/2021/01/25/logging-fluentd-with-kubernetes/
- https://docs.fluentd.org/container-deployment/kubernetes


📜 Documentation

- https://github.com/jjhnk/spring-ms-test/blob/83099ff3d9fe43e466d76c178f46468578d29003/kubernetes/efk/Dockerfile#L1-L10: 'fluentd-kubernetes-daemonset:v1.4.2-debian-elasticsearch-1.1' 즉, kubernetes 환경에서 elasticsearch를 위한 fluentd 입니다.
- https://github.com/jjhnk/spring-ms-test/blob/83099ff3d9fe43e466d76c178f46468578d29003/kubernetes/efk/fluentd-ds.yml#L23-L66 를 확인하면, fluentd는 'Daemon Set'으로, 'Daemon Set'은 Almost (or all) nodes에 copy를 생성합니다. 각 node에 배포된 fluentd는 개별적으로 log input plugin을 output plugin을 통해 목표(elasticsearch)로 전달합니다.

![efk-1](../images/efk-1.webp)

![efk-2](../images/efk-02.png)

#### 주요 기능:
1. **통합 로깅 계층**: Fluentd는 데이터 소스와 백엔드 시스템을 분리하여 데이터를 수집, 필터링, 버퍼링, 출력하는 통합 로깅 계층을 제공합니다.
2. **플러그인 시스템**: 500개 이상의 커뮤니티 기여 플러그인을 통해 다양한 데이터 소스와 출력 대상으로 쉽게 확장할 수 있습니다.
3. **저자원 요구**: Fluentd는 C와 Ruby로 작성되어 적은 시스템 자원을 사용하며, 기본 인스턴스는 30-40MB의 메모리로 초당 13,000개의 이벤트를 처리할 수 있습니다.
4. **신뢰성**: 메모리 및 파일 기반 버퍼링을 통해 데이터 손실을 방지하며, 고가용성을 위해 견고한 페일오버를 지원합니다.
5. **유연한 구성**: 다양한 입력, 출력, 필터, 포맷터, 버퍼 플러그인을 사용하여 로그 데이터를 원하는 방식으로 처리할 수 있습니다.

#### 아키텍처 구성:
- **Input Plugin**: 다양한 소스로부터 데이터를 수집합니다. (예: 파일, HTTP, Syslog 등)
- **Filter Plugin**: 수집된 데이터를 필터링 및 변환합니다. (예: 레코드 변환기, grep 필터 등)
- **출력 플러그인**: 데이터를 다양한 출력 대상으로 전송합니다. (예: 파일, Elasticsearch, Kafka 등)
- **버퍼링**: 데이터 손실을 방지하기 위해 메모리 또는 파일에 데이터를 버퍼링합니다.
- **포맷터**: 데이터를 특정 형식으로 포맷팅하여 출력합니다.

Elastic Search의 [Logstash](https://www.elastic.co/logstash)가 사용된 ELK stack 또한 자주 사용되나, Logstash의 경우 Java 기반이므로 자원소모가 최근에는 주로 Fluentd 기반 EFK stack이 주로 사용되고 있습니다.

## ElasticSearch

🔗 Relevant Links

- https://www.elastic.co/

### Elastic Observability

**Elastic Observability**는 단일 스택에서 로그, 인프라 메트릭, 애플리케이션 추적, 사용자 경험 데이터, 시뮬레이션 모니터링, 범용 프로파일링 등을 통합하여 애플리케이션의 동작을 세밀하게 분석할 수 있는 도구입니다. Elastic Stack을 기반으로 구축되어 데이터의 수집, 처리, 시각화 및 경고 설정을 지원합니다.

#### 주요 기능:
1. **애플리케이션 성능 모니터링 (APM)**:
   - 실시간으로 소프트웨어 서비스와 애플리케이션의 성능 정보를 수집합니다.
   - 응답 시간, 데이터베이스 쿼리, 캐시 호출, 외부 HTTP 요청 등의 세부 성능 정보를 제공합니다.
   - 자동으로 처리되지 않은 오류 및 예외를 수집하여 분석합니다【50†source】.

2. **인프라 모니터링**:
   - 서버, Docker, Kubernetes, Prometheus 등에서 시스템 및 서비스 메트릭을 모니터링합니다.
   - 리소스 소비가 사용자에게 미치는 영향을 평가하는 데 도움을 줍니다.

3. **실제 사용자 모니터링 (RUM)**:
   - 웹 애플리케이션의 성능을 실제 사용자 경험 데이터를 통해 분석합니다.
   - 웹 애플리케이션의 핵심 웹 성능 지표(Core Web Vitals)를 제공합니다.

4. **로그 모니터링**:
   - 호스트, 서비스, Kubernetes, Apache 등에서 로그 데이터를 분석합니다.
   - 로그 스트림을 실시간으로 보거나 로그 이상 현상을 탐지할 수 있습니다.

5. **시뮬레이션 모니터링**:
   - 사용자가 사이트에서 수행하는 작업을 미리 정의된 간격으로 시뮬레이션하여 성능 데이터를 수집합니다.

6. **범용 프로파일링**:
   - 시스템 성능을 시각적으로 분석하고, 비용이 많이 드는 코드 라인을 식별하여 성능 저하를 해결합니다.

7. **경고 및 알림**:
   - Kibana의 경고 및 작업 기능을 통해 잠재적인 문제를 실시간으로 파악할 수 있습니다.
   - 중앙에서 모든 경고 규칙을 관리할 수 있습니다.


## Kibana

- https://kibana.minikube.me/app/home
- [getting-started](../readme.md#4-elasticserach)

**Kibana**는 Elasticsearch 데이터를 시각화하고 탐색할 수 있는 강력한 도구입니다. Elastic Stack의 일부로서, 다양한 데이터 소스로부터 수집한 데이터를 분석하고 시각화하는 데 사용됩니다. Kibana는 데이터 관리자, 분석가, 비즈니스 사용자 모두에게 유용한 기능을 제공합니다.

#### 주요 기능:
1. **데이터 검색 및 시각화**:
   - **검색**: Kibana는 다양한 데이터 소스에서 데이터를 검색하고 숨겨진 인사이트를 탐색할 수 있습니다.
   - **시각화**: 차트, 게이지, 맵, 그래프 등을 통해 데이터를 시각화할 수 있으며, 이러한 시각화를 대시보드에 결합하여 다각도로 데이터를 분석할 수 있습니다.
   - **Kibana Lens**: 드래그 앤 드롭 interface를 사용하여 데이터를 빠르게 시각화할 수 있습니다.

2. **데이터 관리 및 보안**:
   - **데이터 관리**: 인덱스 캐시를 새로 고치거나 지우고, 인덱스 수명 주기를 정의하며, 데이터 롤업, 원격 클러스터 복제 등의 작업을 수행할 수 있습니다.
   - **보안**: 역할 기반 액세스 제어를 통해 특정 사용자에게 특정 기능에 대한 접근 권한을 부여할 수 있습니다. 또한, Kibana는 감사 로그를 통해 사용자의 행동을 추적할 수 있습니다.

3. **경고 및 알림**:
   - 중요한 데이터 변화나 이상 현상을 감지하고, 이에 대한 알림을 설정하여 이메일, Slack, PagerDuty 등으로 알림을 받을 수 있습니다.

4. **대시보드 및 리포트**:
   - **대시보드**: 여러 시각화를 결합하여 포괄적인 데이터 대시보드를 만들 수 있습니다.
   - **리포트**: 대시보드를 PDF로 내보내거나 링크를 공유할 수 있어 팀과 인사이트를 쉽게 공유할 수 있습니다.

5. **머신 러닝**:
   - 데이터 행동을 모델링하고, 이상 탐지, 회귀 분석, 분류 분석 등을 수행할 수 있습니다. 이를 통해 예기치 않은 패턴을 식별하고 문제를 미리 예측할 수 있습니다.

6. **통합 및 확장성**:
   - 다양한 데이터 소스와의 통합을 지원하며, 특히 Elasticsearch와의 통합이 원활하여 대규모 데이터 분석이 가능합니다. Elastic Maps를 통해 지리적 데이터를 시각화하고 분석할 수 있습니다.

#### 실습

[getting-started](../readme.md#4-elasticserach)의 과정을 진행한다.

1. 준비

- Authorizaiton 획득

```pwsh
$access_token = (Invoke-RestMethod -Uri 'https://minikube.me/oauth2/token' -Method Post -Headers @{ 'Accept' = 'application/json'; 'Content-Type' = 'application/x-www-form-urlencoded'; 'Authorization' = 'Basic d3JpdGVyOnNlY3JldC13cml0ZXI=' } -Body @{ 'grant_type' = 'client_credentials'; 'scope' = 'product:read product:write';} -SkipCertificateCheck).access_token
```

- POST: Product

```pwsh
Invoke-WebRequest -Uri 'https://minikube.me/product-composite' -Method POST -ContentType 'application/json' -Headers @{Authorization = "Bearer $access_token"} -Body '{"productId":2,"name":"product name C","weight":300,"recommendations":[{"recommendationId":1,"author":"author 1","rate":1,"content":"content 1"},{"recommendationId":2,"author":"author 2","rate":2,"content":"content 2"},{"recommendationId":3,"author":"author 3","rate":3,"content":"content 3"}],"reviews":[{"reviewId":1,"author":"author 1","subject":"subject 1","content":"content 1"},{"reviewId":2,"author":"author 2","subject":"subject 2","content":"content 2"},{"reviewId":3,"author":"author 3","subject":"subject 3","content":"content 3"}]}' -SkipCertificateCheck
```

- GET: Product를 보낸다.

```pwsh
Invoke-RestMethod -Uri 'https://minikube.me/product-composite/2' -Headers @{ "Cache-Control" = "no-store"; "Authorization" = "Bearer $access_token" } -Method Get -SkipCertificateCheck
```

2. https://kibana.minikube.me/app/home에 접속한다.

3. Discover 탭에 들어간다.

- 'View surrounding documents'를 누른다. 해당 document 근처의 document가 같이 나타난다.

![elasticsearch-3](../images/elastic-search-3.png)

4. 오류가 있는 Product를 Get한다.

```pwsh
Invoke-RestMethod -Uri 'https://minikube.me/product-composite/999' -Headers @{ "Cache-Control" = "no-store"; "Authorization" = "Bearer $access_token" } -Method Get -SkipCertificateCheck
```

5. Warn이 있는 Product를 선택한다.

![elasticsearch-4](../images/elastic-search-4.png)

6. 해당 trace를 filter한다.

![elasticsearch-5](../images/elastic-search-5.png)

# Kubernetes Dashboard

Kubernetes Dashbaord에 접속하기 위해서는 다음과 같은 명령어를 사용한다.

```pwsh
kubectl -n kubernetes-dashboard create token admin-user
```

# Auth Server

### 인증 서버의 주요 개념

인증 서버(Authorization Server)는 현대의 아이덴티티 및 액세스 관리 시스템에서 중요한 구성 요소로, 클라이언트와 사용자의 인증 및 권한 부여를 처리합니다. 주요 개념에는 Realm, Clients, Client Scopes, Roles, Users가 있습니다. 각각의 개념을 한국어로 설명하면 다음과 같습니다:

### 1. **Realm(영역)**
Realm(영역)은 사용자를 관리하는 보안 도메인으로, 사용자의 자격 증명, 역할, 그룹 등을 관리합니다. 각 영역은 서로 다른 사용자 기반을 분리하기 위해 사용되며, 예를 들어 다양한 애플리케이션이나 고객 기반을 분리할 때 사용됩니다. 각 영역은 고유한 네임스페이스를 가지므로, 사용자, 역할, 클라이언트가 다른 영역과 겹치지 않게 됩니다.

### 2. **Clients(클라이언트)**
Clients(클라이언트)는 인증 서버로부터 인증 및 권한 부여를 요청하는 애플리케이션이나 서비스입니다. 각 클라이언트는 특정 영역에 등록되며 고유한 클라이언트 ID를 가지고 있습니다. 클라이언트는 다양한 설정(리디렉션 URI, 인증 흐름, 액세스 정책 등)으로 구성될 수 있습니다. 클라이언트는 다음과 같이 분류됩니다:

### 3. **Client Scopes(클라이언트 스코프)**
Client Scopes(클라이언트 스코프)는 클라이언트의 액세스 수준을 정의합니다. 이는 클라이언트가 수행할 수 있는 작업과 액세스할 수 있는 리소스를 정의하는 권한 세트입니다. 클라이언트 스코프는 클라이언트에게 부여되어 그들의 기능을 제한하고 필요한 것만 액세스할 수 있도록 합니다.

### 4. **Roles(역할)**
Roles(역할)은 사용자나 애플리케이션을 특정 권한 세트로 그룹화하는 방법입니다. 일반적으로 두 가지 유형의 역할이 있습니다:
- **영역 역할(Realm Roles)**: 해당 영역 내의 모든 사용자나 클라이언트에게 할당될 수 있는 전역 역할.
- **클라이언트 역할(Client Roles)**: 특정 클라이언트에 속한 역할로, 해당 클라이언트가 제공하는 리소스나 기능에 대한 접근을 허용합니다.

### 5. **Users(사용자)**
Users(사용자)는 인증 및 애플리케이션과 상호작용할 수 있는 엔티티입니다. 사용자는 속성, 자격 증명(예: 비밀번호), 역할 등을 가질 수 있으며, 영역 내에서 관리됩니다. 사용자의 접근 및 권한은 역할과 클라이언트 스코프를 통해 제어됩니다.

## Spring Auth Server

🔗 Relevant Links:

- https://spring.io/projects/spring-authorization-server

📜 Documents:

- isseur: https://github.com/jjhnk/spring-ms-test/blob/83099ff3d9fe43e466d76c178f46468578d29003/spring-cloud/authorization-server/src/main/java/hy/springcloud/authorizationserver/config/AuthorizationServerConfig.java#L109-L114
- client: https://github.com/jjhnk/spring-ms-test/blob/83099ff3d9fe43e466d76c178f46468578d29003/spring-cloud/authorization-server/src/main/java/hy/springcloud/authorizationserver/config/AuthorizationServerConfig.java#L54-L95
- user: https://github.com/jjhnk/spring-ms-test/blob/83099ff3d9fe43e466d76c178f46468578d29003/spring-cloud/authorization-server/src/main/java/hy/springcloud/authorizationserver/config/DefaultSecurityConfig.java#L42-L51
- jwks: https://minikube.me/oauth2/jwks
- token-endpoint: https://minikube.me/oauth2/token
- secret ref: https://github.com/jjhnk/spring-ms-test/blob/83099ff3d9fe43e466d76c178f46468578d29003/kubernetes/helm/hands-on/environments/dev-env/values.yaml#L97-L98
- certificate: https://github.com/jjhnk/spring-ms-test/blob/develop/kubernetes/helm/istio-system/templates/hands-on-certificate.yaml
- ca-issuer: https://github.com/jjhnk/spring-ms-test/blob/develop/kubernetes/helm/istio-system/templates/selfsigned-issuer.yaml

## Keycloak

🔗 Relevant Links:

- https://www.keycloak.org/

📜 Documentation

- openid-configuration: https://keycloak.minikube.me/realms/dev/.well-known/openid-configuration
- jwks-uri: https://keycloak.minikube.me/realms/dev/protocol/openid-connect/certs
- jwt-issuer: https://keycloak.minikube.me/realms/dev
- token-endpoint: https://keycloak.minikube.me/realms/dev/protocol/openid-connect/token
- authorization-endpoint: https://keycloak.minikube.me/realms/dev/protocol/openid-connect/auth
- secret ref: https://github.com/jjhnk/spring-ms-test/blob/83099ff3d9fe43e466d76c178f46468578d29003/kubernetes/helm/hy-oltp/environments/dev-env/values.yaml#L29-L31

**Keycloak**는 오픈 소스 아이덴티티 및 접근 관리 솔루션입니다. 주로 애플리케이션과 서비스에서 사용자의 인증 및 권한 부여를 간편하게 관리하기 위해 사용됩니다. Keycloak은 다음과 같은 주요 기능을 제공합니다:

### 주요 기능

1. **싱글 사인 온(SSO)**:
   - 사용자들이 한 번 로그인하면 여러 애플리케이션에 대해 자동으로 인증됩니다.
   - SAML 2.0, OpenID Connect, OAuth 2.0과 같은 표준 프로토콜을 지원합니다.

2. **아이덴티티 브로커링 및 소셜 로그인**:
   - 다양한 외부 아이덴티티 제공자(예: Google, Facebook)와 통합하여 소셜 로그인 기능을 제공합니다.
   - LDAP나 Active Directory 같은 엔터프라이즈 아이덴티티 제공자와도 연동할 수 있습니다.

3. **사용자 관리**:
   - 사용자 등록, 비밀번호 재설정, 계정 활성화/비활성화 등의 기능을 제공합니다.
   - 사용자 그룹 및 역할을 관리하여 세분화된 접근 제어를 설정할 수 있습니다.

4. **역할 기반 접근 제어(RBAC)**:
   - 애플리케이션에 대한 접근 권한을 역할 기반으로 관리할 수 있습니다.
   - 세부적인 권한 설정을 통해 보안을 강화할 수 있습니다.

5. **관리 콘솔**:
   - 웹 기반 관리 콘솔을 통해 손쉽게 설정 및 관리할 수 있습니다.
   - 관리자들은 여기서 클라이언트, 사용자, 역할 등을 관리할 수 있습니다.

6. **확장성 및 커스터마이징**:
   - 다양한 플러그인과 커스터마이징 옵션을 통해 필요에 맞게 기능을 확장할 수 있습니다.
   - 테마 커스터마이징을 통해 사용자 interface를 브랜드에 맞게 조정할 수 있습니다.

## Resource Server


**Resource Server**는 OAuth 2.0 프레임워크의 핵심 구성 요소 중 하나로, 클라이언트 애플리케이션이 액세스할 수 있는 보호된 리소스를 호스팅하는 서버입니다. Resource Server는 인증 서버에서 발급한 액세스 토큰을 사용하여 클라이언트의 요청을 인증하고 권한을 확인합니다.

### 주요 기능 및 역할

1. **보호된 리소스 호스팅**:
   - Resource Server는 사용자의 데이터, 파일, API 엔드포인트 등 보호된 리소스를 호스팅합니다.
   - 예를 들어, 사용자의 프로필 정보, 금융 정보, 개인 파일 등을 포함할 수 있습니다.

2. **액세스 토큰 검증**:
   - 클라이언트 애플리케이션이 Resource Server에 요청을 보낼 때, 요청에는 인증 서버에서 발급된 액세스 토큰이 포함됩니다.
   - Resource Server는 이 토큰을 검증하여 요청이 적법한지 확인합니다. 검증 과정에는 액세스 토큰의 유효성, 만료 시간, 서명 등을 확인하는 과정이 포함됩니다.

3. **권한 부여 결정**:
   - 액세스 토큰이 유효한 경우, Resource Server는 토큰에 포함된 클레임(예: 사용자 정보, 권한)을 기반으로 요청된 리소스에 대한 접근 권한을 결정합니다.
   - 예를 들어, 사용자가 특정 리소스에 읽기 권한만 가지고 있는지, 쓰기 권한도 가지고 있는지 등을 확인합니다.

4. **응답 처리**:
   - 요청이 승인되면 Resource Server는 클라이언트에게 요청된 데이터를 응답으로 반환합니다.
   - 요청이 거부되면 적절한 HTTP 상태 코드(예: 401 Unauthorized, 403 Forbidden)와 함께 에러 메시지를 반환합니다.

### 동작 과정

1. **클라이언트 인증**:
   - 클라이언트 애플리케이션이 사용자로부터 인증을 받습니다.
   - 클라이언트는 인증 서버로부터 액세스 토큰을 획득합니다.

2. **리소스 요청**:
   - 클라이언트는 획득한 액세스 토큰을 사용하여 Resource Server에 보호된 리소스를 요청합니다.
   - 요청 헤더에 액세스 토큰을 포함시킵니다.

3. **토큰 검증 및 권한 확인**:
   - Resource Server는 액세스 토큰을 검증합니다.
   - 토큰의 유효성, 만료 시간, 서명 등을 확인하고, 클레임을 통해 권한을 확인합니다.

4. **리소스 제공**:
   - 토큰이 유효하고 권한이 확인되면, Resource Server는 요청된 리소스를 클라이언트에게 제공합니다.
   - 그렇지 않은 경우, 적절한 에러 응답을 반환합니다.

### 예시

OAuth 2.0을 사용하는 웹 애플리케이션을 예로 들면, 사용자가 로그인하여 자신의 프로필 정보를 조회하거나, 파일을 업로드/다운로드할 때 Resource Server가 중요한 역할을 합니다. 이러한 애플리케이션에서 Resource Server는 사용자의 요청이 적법한지를 확인하고, 올바른 데이터에 대한 접근을 제공함으로써 데이터의 보안을 유지합니다.

## OAuth Grant Types

OAuth 2.0는 다양한 시나리오에서 안전하게 리소스에 접근할 수 있도록 여러 가지 grant types(권한 부여 유형)을 제공합니다. 가장 일반적으로 사용되는 grant types는 `authorization_code`, `client_credentials`, `password`, `implicit`, 그리고 `refresh_token`입니다.

### 1. Authorization Code Grant
**Authorization Code Grant**는 가장 안전한 방식으로 간주되며, 클라이언트 애플리케이션이 서버에서 코드를 받아서 액세스 토큰으로 교환하는 방식입니다.

- **흐름**:
  1. 사용자가 클라이언트 애플리케이션에서 인증 요청을 시작합니다.
  2. 클라이언트 애플리케이션은 사용자를 인증 서버로 리디렉션합니다.
  3. 사용자가 인증 서버에서 인증하고 권한을 부여하면, 인증 서버는 클라이언트 애플리케이션에 인증 코드를 보냅니다.
  4. 클라이언트 애플리케이션은 이 인증 코드를 사용해 인증 서버에 액세스 토큰을 요청합니다.
  5. 인증 서버는 액세스 토큰을 발급합니다.

- **사용 사례**: 보안이 중요한 서버 사이드 애플리케이션, 웹 애플리케이션, 모바일 애플리케이션에서 주로 사용됩니다.

![authorization_code](../images/images-oauth_auth_code.png)

```pwsh
# Step 1: Access Token Request
curl -X POST \
-d "grant_type=authorization_code" \
-d "code=AUTHORIZATION_CODE_RECEIVED_FROM_AUTH_SERVER" \
-d "redirect_uri=YOUR_REGISTERED_REDIRECT_URI" \
-d "client_id=YOUR_CLIENT_ID" \
-d "client_secret=YOUR_CLIENT_SECRET" \
https://your-auth-server.com/token

# Response
# {
#   "access_token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
#   "token_type": "Bearer",
#   "expires_in": 3600,
#   "refresh_token": "8xLOxBtZp8",
#   "scope": "read write"
# }

# Step 2: Resource Request
curl -H "Authorization: Bearer YOUR_ACCESS_TOKEN" \
https://your-resource-server.com/resource

# Response
# {
#   "data": "Protected resource data"
# }
```
### 2. Client Credentials Grant
**Client Credentials Grant**는 애플리케이션이 자체 자격 증명을 사용하여 인증 서버에서 직접 액세스 토큰을 요청하는 방식입니다.

- **흐름**:
  1. 클라이언트 애플리케이션이 인증 서버에 자격 증명(클라이언트 ID 및 클라이언트 비밀)을 보냅니다.
  2. 인증 서버는 자격 증명을 확인하고 액세스 토큰을 발급합니다.

- **사용 사례**: 애플리케이션 간 통신, 백엔드 서비스, 비사용자 관련 API 호출 등에 사용됩니다.

![client_credential](../images/images-oauth_client_credentials.png)

```pwsh
# Step 1: Access Token Request
curl -X POST \
-d "grant_type=client_credentials" \
-d "client_id=YOUR_CLIENT_ID" \
-d "client_secret=YOUR_CLIENT_SECRET" \
https://your-auth-server.com/token

# Response
# {
#   "access_token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
#   "token_type": "Bearer",
#   "expires_in": 3600,
#   "scope": "read write"
# }

# Step 2: Resource Request
curl -H "Authorization: Bearer YOUR_ACCESS_TOKEN" \
https://your-resource-server.com/resource

# Response
# {
#   "data": "Protected resource data"
# }
```

### 3. Password Grant (Resource Owner Password Credentials)
**Password Grant**는 사용자가 자신의 자격 증명(사용자명 및 비밀번호)을 사용하여 직접 클라이언트 애플리케이션에 제공하는 방식입니다.

- **흐름**:
  1. 사용자가 클라이언트 애플리케이션에 사용자명과 비밀번호를 입력합니다.
  2. 클라이언트 애플리케이션은 이 자격 증명을 인증 서버에 보냅니다.
  3. 인증 서버는 자격 증명을 확인하고 액세스 토큰을 발급합니다.

- **사용 사례**: 신뢰할 수 있는 애플리케이션에서만 사용해야 하며, 주로 기존 시스템에서 새로운 OAuth 2.0 시스템으로 마이그레이션할 때 사용됩니다.

![password](../images/images-oauth_password.png)

```pwsh
# Step 1: Access Token Request (username and password granted to application)
curl -X POST \
-d "grant_type=password" \
-d "username=YOUR_USERNAME" \
-d "password=YOUR_PASSWORD" \
-d "client_id=YOUR_CLIENT_ID" \
-d "client_secret=YOUR_CLIENT_SECRET" \
https://your-auth-server.com/token

# Response
# {
#   "access_token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
#   "token_type": "Bearer",
#   "expires_in": 3600,
#   "refresh_token": "8xLOxBtZp8",
#   "scope": "read write"
# }

# Step 2: Resource Request
curl -H "Authorization: Bearer YOUR_ACCESS_TOKEN" \
https://your-resource-server.com/resource

# Response
# {
#   "data": "Protected resource data"
# }
```

### 4. Implicit Grant
**Implicit Grant**는 액세스 토큰이 인증 코드 없이 직접 발급되는 방식입니다. 보안이 상대적으로 낮아 사용이 권장되지 않습니다.

- **흐름**:
  1. 사용자가 클라이언트 애플리케이션에서 인증 요청을 시작합니다.
  2. 클라이언트 애플리케이션은 사용자를 인증 서버로 리디렉션합니다.
  3. 사용자가 인증 서버에서 인증하고 권한을 부여하면, 인증 서버는 클라이언트 애플리케이션에 액세스 토큰을 직접 보냅니다.

- **사용 사례**: 브라우저 기반 애플리케이션에서 주로 사용되었지만, 현재는 보안 문제로 인해 사용이 줄어들고 있습니다.

![implicit](../images/images-oauth_implicit.png)

```pwsh
# Note: Implicit Grant does not involve a server-side token request with curl.
# The access token is obtained directly by the client.

# Step 2: Resource Request
curl -H "Authorization: Bearer YOUR_ACCESS_TOKEN" \
https://your-resource-server.com/resource

# Response
# {
#   "data": "Protected resource data"
# }
```

### 5. Refresh Token Grant
**Refresh Token Grant**는 기존의 액세스 토큰이 만료된 후 새 액세스 토큰을 발급받기 위해 사용됩니다.

- **흐름**:
  1. 클라이언트 애플리케이션이 만료된 액세스 토큰과 함께 refresh token을 인증 서버에 보냅니다.
  2. 인증 서버는 refresh token을 확인하고 새 액세스 토큰을 발급합니다.

- **사용 사례**: 장기적인 액세스가 필요한 애플리케이션에서 사용되며, 사용자가 다시 로그인할 필요 없이 새로운 액세스 토큰을 발급받을 수 있습니다.

### Keycloack 인증 관련

[getting-started](../readme.md/#5-keycloak)에서 설정한 Authorization은 'authorization_code'이나, 다른 'resource server'인 'account-console'의 'login resource'를 사용하여 'estate resource server'의 '/estate/building/* resource'의 Authorization을 허가하고 있습니다. 이는 scope를 활용한 방식으로 보안성이 매우 떨어집니다.

아래의 인증 요청에 대해

```
code: 3b295963-12f7-489a-9485-166290344e12.2da251af-cdf8-4be9-8cc4-e505b83bd0e7.3f0bf79e-5cd5-46be-b690-c24f10ea8d1d
grant_type: authorization_code
client_id: account-console
redirect_uri: https://keycloak.minikube.me/realms/dev/account/
code_verifier: 4SSFGYLol8HEJo724kxB51JBDxhgCR5Qbml16LpfwRfo4HadooOdoS53Lz4hCL58UZAbMNJSPtLy5eBIYCVl4FIArVZPXDqr
```

auth-server는 다음의 인증을 전달합니다.

```json
{
    "access_token": "eyJhbGciOiJSUzI1NiIsInR5cCIgOiAiSldUIiwia2lkIiA6ICItRGE0clpPNnhVRzhRcVNXSXIxVDNDMkFzUDFkZnZvM2hMR0NvUFJJSDVrIn0.eyJleHAiOjE3MjEyMjIxNzIsImlhdCI6MTcyMTIyMTg3MiwiYXV0aF90aW1lIjoxNzIxMjIxODcxLCJqdGkiOiI0OGVkZTMxNy04MWM5LTRiYmQtYmEwMi0yNjMzN2U5YWRlMWMiLCJpc3MiOiJodHRwczovL2tleWNsb2FrLm1pbmlrdWJlLm1lL3JlYWxtcy9kZXYiLCJhdWQiOiJhY2NvdW50Iiwic3ViIjoiZjkyNmNiZjEtODAxZi00YTgzLWEyOTYtZGJkNzZjNWQ4M2Y0IiwidHlwIjoiQmVhcmVyIiwiYXpwIjoiYWNjb3VudC1jb25zb2xlIiwic2lkIjoiMmRhMjUxYWYtY2RmOC00YmU5LThjYzQtZTUwNWI4M2JkMGU3IiwiYWNyIjoiMCIsInJlc291cmNlX2FjY2VzcyI6eyJhY2NvdW50Ijp7InJvbGVzIjpbIm1hbmFnZS1hY2NvdW50IiwibWFuYWdlLWFjY291bnQtbGlua3MiXX19LCJzY29wZSI6Im9wZW5pZCBtaWNyb3Byb2ZpbGUtand0IHByb2ZpbGUgZXN0YXRlIGVtYWlsIiwidXBuIjoidXNlciIsImVtYWlsX3ZlcmlmaWVkIjpmYWxzZSwibmFtZSI6ImFiYyBhYmMiLCJwcmVmZXJyZWRfdXNlcm5hbWUiOiJ1c2VyIiwiZ2l2ZW5fbmFtZSI6ImFiYyIsImZhbWlseV9uYW1lIjoiYWJjIiwiZW1haWwiOiJhYmNAbmF2ZXIuY29tIn0.DlWli46oLch2k61vRGs-evFRyOMk9EV70NYOG1kHk3rAbV3137VJKCAbVetN99wg1GVvEJFVscmuC7Nq1oVoLpEjcn6EDHkatNFUdOmG41Nqk8kczQLaKRwAnJIFS1mf3EbSs55iPRPr1XL82WCoyURpJ4IyVS5yHBfXgVe8OREZyio201b_41YDbD2m7OaVt2lOzzufZQRGoQuXO2-8od0k4abl7JpYAIlV5Q3cKKLfO54iuMA8_trXp0w4W9toeT_EaY_Y2CqZZoZEmttwNuk2PH2FmvWS9YRs8a1CSL4JKPQp_1leLoEFncRYrQREcDmT_rYfjNmBRpA9bneDpg",
    "expires_in": 300,
    "refresh_expires_in": 1800,
    "refresh_token": "eyJhbGciOiJIUzUxMiIsInR5cCIgOiAiSldUIiwia2lkIiA6ICIxYjAwYjllZi1kZDhmLTRkMWMtYTEyZS02NDFiZDM5ZmZjZmEifQ.eyJleHAiOjE3MjEyMjM2NzIsImlhdCI6MTcyMTIyMTg3MiwianRpIjoiNmExY2NmODgtOGE2Ny00YTE0LThhNTMtZTE5MzVjN2VkMWFmIiwiaXNzIjoiaHR0cHM6Ly9rZXljbG9hay5taW5pa3ViZS5tZS9yZWFsbXMvZGV2IiwiYXVkIjoiaHR0cHM6Ly9rZXljbG9hay5taW5pa3ViZS5tZS9yZWFsbXMvZGV2Iiwic3ViIjoiZjkyNmNiZjEtODAxZi00YTgzLWEyOTYtZGJkNzZjNWQ4M2Y0IiwidHlwIjoiUmVmcmVzaCIsImF6cCI6ImFjY291bnQtY29uc29sZSIsInNpZCI6IjJkYTI1MWFmLWNkZjgtNGJlOS04Y2M0LWU1MDViODNiZDBlNyIsInNjb3BlIjoib3BlbmlkIG1pY3JvcHJvZmlsZS1qd3Qgcm9sZXMgYWNyIHByb2ZpbGUgZXN0YXRlIGVtYWlsIHdlYi1vcmlnaW5zIGJhc2ljIn0.gy_6H2GasZ_QaCq3HQkjn69g36_ztRs5bR_7-p779JmuuyyFEBxZdOUKoyfjtUTnF_jJGqZAhwfAkjF8QDXkDQ",
    "token_type": "Bearer",
    "id_token": "eyJhbGciOiJSUzI1NiIsInR5cCIgOiAiSldUIiwia2lkIiA6ICItRGE0clpPNnhVRzhRcVNXSXIxVDNDMkFzUDFkZnZvM2hMR0NvUFJJSDVrIn0.eyJleHAiOjE3MjEyMjIxNzIsImlhdCI6MTcyMTIyMTg3MiwiYXV0aF90aW1lIjoxNzIxMjIxODcxLCJqdGkiOiJlMDJiNjgxMS0xYzM0LTQwNDgtYmUwMS0zY2IxZDAwNjkxOTIiLCJpc3MiOiJodHRwczovL2tleWNsb2FrLm1pbmlrdWJlLm1lL3JlYWxtcy9kZXYiLCJhdWQiOiJhY2NvdW50LWNvbnNvbGUiLCJzdWIiOiJmOTI2Y2JmMS04MDFmLTRhODMtYTI5Ni1kYmQ3NmM1ZDgzZjQiLCJ0eXAiOiJJRCIsImF6cCI6ImFjY291bnQtY29uc29sZSIsIm5vbmNlIjoiNzMxM2Y2ZDktYTljOS00MjIyLWEzN2EtNmVlMzI2MTc4ZDdiIiwic2lkIjoiMmRhMjUxYWYtY2RmOC00YmU5LThjYzQtZTUwNWI4M2JkMGU3IiwiYXRfaGFzaCI6Il9rdy0tMHNKUGhCWDQ2VWNFX2s3QlEiLCJhY3IiOiIwIiwidXBuIjoidXNlciIsImVtYWlsX3ZlcmlmaWVkIjpmYWxzZSwibmFtZSI6ImFiYyBhYmMiLCJwcmVmZXJyZWRfdXNlcm5hbWUiOiJ1c2VyIiwiZ2l2ZW5fbmFtZSI6ImFiYyIsImZhbWlseV9uYW1lIjoiYWJjIiwiZW1haWwiOiJhYmNAbmF2ZXIuY29tIn0.M8EwAJ_x5qv1S0Jp9JNackgZyyzS6NOuO9WSsLFm2mrs28u9WA4USJfY8gsSx0_XERe7Ya9Q9pymrtsJg7dxd-KID3GuTC47im-rPN1auw5X6k972ickPWw3FFCgFNkE4PfXhWHjeWQUxzVnQ4spQpQm4J6anakHPjVNg4-5dV-dz938sPz8JVfHNlC2P8UwCgnBgBBIv1k-tDmKlxc2PPcuBrTg_5nr-g77qvRWLfSAGmbSn7QYTkKku3rXR4yCpq347pLOaFnpFY1oZLQ7iLGkKD3AqdK2lvDFz_aSRFqh6qAa0A_097CTPASlu4YKZrqPWqvGbOF-0fmy6nq6vQ",
    "not-before-policy": 0,
    "session_state": "2da251af-cdf8-4be9-8cc4-e505b83bd0e7",
    "scope": "openid microprofile-jwt profile estate email"
}
```

'estate resource server'는 scope에 있는 estate를 바탕으로 '/estate/building/*' 인증을 허가합니다. 이는 보안 취약문제가 발생할 수도 있습니다. 곧 resource별 authorization 관리가 매우 어렵습니다.

Keycloak, Spring, Swagger 및 Postman 관련 Youtube 소개하니 개선해봅시다.

- https://www.youtube.com/@diveintodev/videos
- https://youtu.be/zIr9fbbLD9M?si=_GN1uGIw48aUbrun

resource-server 관리는 실습이 중요하니 직접해봅시다.

# Project

🔗 Relevant Links

- https://maven.apache.org/
- https://www.sonatype.com/products/sonatype-nexus-oss

📜 Documentation

- https://github.com/jjhnk/spring-ms-test/blob/83099ff3d9fe43e466d76c178f46468578d29003/api/build.gradle#L23-L33

## Artifact 저장소

아티팩트 저장소는 소프트웨어 개발 과정에서 빌드된 바이너리 파일, 라이브러리, 패키지 등을 중앙 집중식으로 저장하고 관리하는 시스템입니다. 이는 개발자들이 소스 코드, 의존성 및 배포 아티팩트를 쉽게 공유하고 재사용할 수 있도록 합니다.

### Maven Repository

1. **소개**: Maven Repository는 Apache Maven에서 사용되는 중앙 리포지토리 시스템입니다. Maven은 프로젝트 관리 및 이해를 돕기 위해 프로젝트 객체 모델(POM)을 기반으로 한 빌드 자동화 도구입니다.

2. **주요 기능**:
   - **의존성 관리**: 프로젝트 빌드에 필요한 라이브러리와 플러그인의 의존성을 중앙 리포지토리에서 관리합니다.
   - **POM 파일**: 프로젝트의 의존성, 빌드 순서, 플러그인 등을 기술한 XML 파일을 사용합니다.
   - **로컬 및 원격 리포지토리**: 로컬 리포지토리는 각 개발자의 로컬 머신에 위치하고, 중앙 리포지토리는 중앙 서버에 위치합니다.

3. **사용 예시**:
   - 프로젝트의 의존성을 자동으로 다운로드하고 관리.
   - 빌드 스크립트의 복잡성을 줄여 일관된 빌드 프로세스를 유지.

### Nexus OSS

1. **소개**: Nexus OSS(오픈 소스 버전)은 Sonatype에서 제공하는 강력한 아티팩트 저장소 관리 솔루션입니다. 이는 다양한 형식의 아티팩트를 저장하고 관리할 수 있는 중앙 집중식 리포지토리를 제공합니다.

2. **주요 기능**:
   - **아티팩트 저장 및 관리**: 다양한 형식의 아티팩트를 저장하고 버전 관리를 수행합니다.
   - **프록시 리포지토리**: 원격 리포지토리를 프록시하여 외부 리포지토리에서 아티팩트를 캐싱합니다.
   - **보안 및 권한 관리**: 사용자 및 그룹별로 접근 권한을 설정할 수 있습니다.
   - **자동화**: CI/CD 파이프라인과의 통합을 지원하여 자동으로 아티팩트를 배포하고 관리할 수 있습니다.

3. **사용 예시**:
   - 여러 팀이 공유하는 라이브러리의 중앙 저장소로 사용.
   - 빌드된 아티팩트를 저장하여 배포할 때 사용.


#### 적용

1. 'build.gradle'에 아래와 같이 접근 가능한 repository를 추가한다.

```json
publishing {
    publications {
        mavenJava(MavenPublication) {
            from components.java
        }
    }

    // maven local
    // mavenLocal()

    // local oss
    // repositories {
    //     maven {
    //         name = 'localNexus'
    //         url = uri('http://localhost:8081/repository/maven-releases/')
    //         allowInsecureProtocol = true
    //         credentials {
    //             username = project.findProperty("nexusUsername") ?: "admin"
    //             password = project.findProperty("nexusPassword") ?: "admin"
    //         }
    //     }
    // }
}
```

2. gradle project를 refresh한다.

## MyBatis, QueryDSL, jOOQ

### 공통점
1. **ORM (Object-Relational Mapping) 지원**: 세 라이브러리 모두 객체와 데이터베이스의 관계를 매핑하는 기능을 제공합니다.
2. **Java 기반**: 모두 Java 언어를 기반으로 하여 개발되었습니다.
3. **SQL 사용 가능**: SQL 쿼리를 직접 작성하거나, 쿼리를 생성하는 기능을 제공합니다.
4. **데이터베이스 추상화**: 데이터베이스에 대한 추상화를 제공하여, 개발자가 SQL을 직접 작성하지 않고도 데이터베이스와 상호작용할 수 있게 합니다.
5. **성능 최적화**: 효율적인 데이터베이스 접근을 위한 다양한 성능 최적화 기능을 제공합니다.

### 차이점

#### MyBatis
- **설명**: MyBatis는 SQL mapper 프레임워크로, XML 파일이나 애노테이션을 통해 SQL 쿼리를 정의하고, 이를 mapper interface와 연결하여 데이터베이스 작업을 수행합니다.
- **특징**:
  - Mapper XML 파일: Dynamic SQL 요소를 사용하여 조건부 쿼리를 작성합니다.
  - Mapper Interface: Mapper XML 파일과 interface를 연결하여 SQL 쿼리를 실행합니다.
  - SQLSession: SQLSession 객체를 사용하여 mapper interface를 호출하고 쿼리를 실행합니다.

#### QueryDSL
- **설명**: QueryDSL은 타입 세이프한 쿼리 언어로, 코드에서 직접 SQL, JPQL 등의 쿼리를 작성할 수 있게 합니다.
- **특징**:
  - Q-Type 생성: QueryDSL은 Entity 클래스에 대한 Q-타입을 생성합니다.
  - BooleanBuilder: 동적 조건을 추가하기 위해 BooleanBuilder를 사용합니다.
  - JPAQueryFactory: QueryFactory를 사용하여 쿼리를 생성하고 실행합니다.

#### jOOQ
- **설명**: jOOQ는 SQL Generator이자 Excute 라이브러리로, Type Safe SQL을 작성하고 실행할 수 있게 합니다.
- **특징**:
  - Codegen 사용: jOOQ Code generator 사용하여 데이터베이스 테이블에 대한 Java 클래스를 생성합니다.
  - DSLContext 사용: DSLContext를 사용하여 SQL 쿼리를 작성하고 실행합니다.
  - SQL과 Java의 통합: Java 코드로 SQL 쿼리를 작성할 수 있으며, 이는 SQL의 모든 기능을 Java에서 사용할 수 있게 합니다.


### 예시

아래와 같은 User에 대해 각각의 SQL Operator는 다음과 같이 접근합니다.

```java
import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class User {
    @Id
    private int id;
    private String name;

    // getters and setters
}
```

#### MyBatis

```xml
<?xml version="1.0" encoding="UTF-8" ?>
<!DOCTYPE mapper
  PUBLIC "-//mybatis.org//DTD Mapper 3.0//EN"
  "http://mybatis.org/dtd/mybatis-3-mapper.dtd">
<mapper namespace="org.example.UserMapper">
    <select id="selectUser" parameterType="map" resultType="User">
        SELECT * FROM users
        <where>
            <if test="id != null">
                AND id = #{id}
            </if>
            <if test="name != null">
                AND name = #{name}
            </if>
        </where>
    </select>
</mapper>
```

#### QueryDSL

📜 Documents

- https://github.com/jjhnk/spring-ms-test/blob/develop/oltp/estate-service/src/main/java/hy/oltp/core/estate/unit/persistence/UnitRepositoryCustom.java
- https://github.com/jjhnk/spring-ms-test/blob/develop/oltp/estate-service/src/main/java/hy/oltp/core/estate/unit/persistence/UnitRepository.java
- https://github.com/jjhnk/spring-ms-test/blob/develop/oltp/estate-service/src/main/java/hy/oltp/core/estate/unit/persistence/UnitRepositoryImpl.java

```java
JPAQueryFactory queryFactory = new JPAQueryFactory(em); // em: EntityManager

QUser qUser = QUser.user;
BooleanBuilder builder = new BooleanBuilder();

Integer id = 1;
String name = "John";

if (id != null) {
    builder.and(qUser.id.eq(id));
}
if (name != null) {
    builder.and(qUser.name.eq(name));
}

User user = queryFactory.selectFrom(qUser)
                        .where(builder)
                        .fetchOne();

System.out.println(user);

em.close();
emf.close();
```

#### jOOQ

🔗 Relevant Links:

- https://www.baeldung.com/jooq-intro
- https://www.jooq.org/doc/3.19/manual/

```groovy
implementation 'org.jooq:jooq:3.19.10'
implementation 'org.jooq:jooq-meta:3.19.10'
implementation 'org.jooq:jooq-codegen:3.19.10' # codegen
```

``` java
DSLContext create = DSL.using(connection, SQLDialect.MYSQL);

Integer id = 1;
String name = "John";

Condition condition = DSL.trueCondition();
if (id != null) {
    condition = condition.and(USER.ID.eq(id));
}
if (name != null) {
    condition = condition.and(USER.NAME.eq(name));
}

UserRecord user = create.selectFrom(USER)
                        .where(condition)
                        .fetchOne();

System.out.println(user);

connection.close();
```