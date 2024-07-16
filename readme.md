Spring Ms Test 프로젝트는 적절한 수준의 개발 환경을 제공하여 마이크로 서비스의 개발을 지원하는 것을 목표로 합니다. 이 프로젝트는 JAVA와 Spring을 중심으로 개발되었으며, 마이크로 서비스는 클라우드 컴퓨팅 환경에서의 운영을 목표로 Docker와 Kubernetes를 사용합니다.

적절한 수준이란, 엔터프라이즈 레벨의 서비스를 구축하기 위해 필요한 다양한 도구들을 간단하게 테스트할 수 있는 수준을 의미합니다. 다시 말해, 개발 환경을 운영하기 위해 다양한 best practices 를 따르기보다는, 기본 기능을 쉽게 확인할 수 있도록 설정했습니다.

이 프로젝트의 구조는 이를 잘 보여줍니다. 일반적인 모놀리식 프로젝트는 단일 레포지토리를 사용하지만, 엔터프라이즈 수준의 마이크로 서비스 프로젝트는 다중 레포지토리 구조를 가집니다. 이 레포지토리는 두 가지 구조의 장점을 결합하여, 하나의 레포지토리가 여러 프로젝트를 포함할 수 있도록 설계되었습니다.

차후 RECAP에서는 프로젝트를 어떻게 발전시켜 나갈지에 대해 간단히 논의하겠습니다.

# Features

해당 챕터에서는 해당 프로젝트에서 사용되는 다양한 도구들을 간략하게 보여드리겠습니다.

## 모니터링 (Monitoring)

1. Kubernetes 모니터링 (Kubenetes Monitoring)

![kubernetes-dashboard-1](./images/kubernetes-dashboard-1.png)

2. Node / Network 모니터링 (Kaili)

![kaili-1](./images/kiali-1.png)

3. System, Event 모니터링 (Prometheus, Grafana)

![grafana-1](./images/grafana-1.png)

4. Redis Cache 모니터링 (Redis Insight)

![redis-insight](./images/redis-insight-1.png)

5. 알림 기능 (mail-server, Alert Manager)

![mail-server](./images/mail-server-1.png)

## 로깅 (Logging, Distributed Tracing)

1. EFK stack

![elasticsearch-1](./images/elastic-search-1.png)

![elasticsearch-2](./images/elastic-search-2.png)

2. Prometheus

![prometheus-1](./images/prometheus-1.png)

## 중앙집중적 API Documentation (Centralized Documentation)

1. Swagger

![swagger-1](./images/swagger-1.png)

2. (TODO: 나중에 할거임.) REST API DOCS

## 인증 (Authentication)

1. Auth Server (Keycloak)

![keycloak-1](./images/keycloak-1.png)

![keycloak-2](./images/keycloak-2.png)

## 중앙집중적 환경설정 (Centralized Configuration)

"Centralized Configuration"이란 service의 configuration (e.g. application.yml)이 하나의 포인트에서 관리됨을 의미합니다.

이는 configuration file 혹은 config Map이 service에 종속되지 않고 별도로 inject 될 수 있는 환경이 필요합니다.

1. config with kubernetes

2. [config with config server](https://docs.spring.io/spring-cloud-config/docs/current/reference/html/)

## 서비스 디스커버리 (Service Discovery)

"Service Discovery"란 별도로 end point에 대한 정확한 정보 없이 DNS와 같은 시스템을 이용하여 해당 service를 찾아낼 수 있음을 의미합니다. Reverse Proxy를 좀 쉽게 하기 위한 좋은 도구입니다 ((see)[https://www.linkedin.com/pulse/role-reverse-proxy-microservices-architecture-getkitsune-maotc/])

1. [kubernetes istio](https://istio.io/latest/docs/ops/deployment/architecture/)

2. [eureka server](https://cloud.spring.io/spring-cloud-netflix/reference/html/)

![service discovery](./images/service-discovery.webp)


## 엣지 서버 (Edge Server)

reverse proxy라고 불리기도 하는 그거. 해당 프로젝트는 istio ingress controller를 쓸 예정입니다.

kubernetes ingress controller는 중요 요소이지만, 별도로 언급하지는 않습니다.

nginx는 그냥 보라고 넣었습니다.

spring gateway는 이전에 사용했기 때문에 reference를 넣었습니다.


1. [istio ingress controller](https://istio.io/latest/docs/tasks/traffic-management/ingress/ingress-control/)

2. [kubernetes ingress controller](https://kubernetes.io/docs/concepts/services-networking/ingress/)

3. [nginx ingress controller](https://docs.nginx.com/nginx-ingress-controller/)

4. [spring gateway](https://spring.io/projects/spring-cloud-gateway)

![ingress](./images/Istio%20Ingress%20Gateway.webp)


## 서비스 메시 (Service Mesh)

Service Mesh란 service application의 비지니스 로직이 아닌 부분들에 대해서 공통적인 관심사를 추출하여 처리하는 구조를 말합니다. 이는 Spring AOP와 비슷하다고 보시면 됩니다. NGINX가 설명이 잘 되어있어 해당 그림을 가져왔으나, 본 프로젝트에서는 **Istio**를 사용합니다.


![service-mesh-1](./images/service-mesh-1.png)

![service-mesh-2](./images/servicemesh-2.jpg)


간략한 서비스 흐름은 다음과 같습니다. 물론 Istio 사용합니다. CDN 및 DDoS Protection은 없습니다.

![service-flow](./images/service-flow.jpg)


## 프로젝트에 대하여 (요약)

해당 프로젝트는 Micro Service를 위해서 [Spring Cloud](https://spring.io/projects/spring-cloud)를 사용한 이후 kubernetes를 사용하였습니다.

프로젝트는 Rest Controller를 위주로 별도로 Front Application는 없이 rest api만 지원함을 목표로 하였습니다.

본 프로젝트는 크게 두 가지 프로젝트로 구성되어 있습니다.

1. hands-on: web flux([spring reactive](https://spring.io/reactive)) 기반 microservice OLTP
2. hy-oltp: web mvc 기반 monolithic [OLTP](https://ko.wikipedia.org/wiki/%EC%98%A8%EB%9D%BC%EC%9D%B8_%ED%8A%B8%EB%9E%9C%EC%9E%AD%EC%85%98_%EC%B2%98%EB%A6%AC) (참조: [OALP](https://ko.wikipedia.org/wiki/%EC%98%A8%EB%9D%BC%EC%9D%B8_%EB%B6%84%EC%84%9D_%EC%B2%98%EB%A6%AC)).
* hands-on은 OLTP이긴 하지만 "eventually consistency" concept를 지니고 있습니다. 즉 transaction이 엄밀하게 지켜지지는 않습니다.

마이크로 서비스가 주요 목표인 프로젝트에서, OLTP 서비스를 구현한 이유는 cloud 환경의 기능들을 잘 활용하면 기존의 어려운 문제점들을 해결하기 위한 하나의 실마리가 될 수 있다고 생각하였기 때문입니다.

프로젝트의 상세에 대해서는 추후 상세하게 말씀드리겠습니다.

# Prerequisite

# Getting Started

# Related Documentations