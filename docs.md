# CI/CD and Kubernetes Operations Guide

This document outlines the procedures for setting up and updating the Java Microservices application using Jenkins and Kubernetes.

## 1. Kubernetes Initial Setup

### Prerequisites
- A running Kubernetes cluster.
- `kubectl` configured to communicate with your cluster.

### Step-by-Step Deployment

#### 1. Deploy Infrastructure
Start by deploying the required databases (MySQL, MongoDB) and the Kafka platform.

```bash
kubectl apply -f k8s/namespace.yaml
kubectl apply -f k8s/infrastructure/
```

**Verify**:
```bash
kubectl get pods
```
Wait until all infrastructure pods are in `Running` state before proceeding.

#### 2. Deploy Microservices
Deploy the application services (Product, Inventory, Order, Notification, API Gateway).

```bash
kubectl apply -f k8s/
```

**Verify**:
```bash
kubectl get pods
kubectl get services
```

The application should now be accessible via the **API Gateway** (NodePort 30080 or via LoadBalancer if configured).

---

## 2. Jenkins Setup for CI/CD

### Prerequisites
- Jenkins installed and running.
- **Plugins**:
    - Docker Pipeline
    - Kubernetes CLI Plugin
    - Git Plugin
    - Pipeline Utility Steps
- **Credentials**:
    - `docker-hub-credentials`: Username/Password for Docker Hub.
    - `git-credentials`: SSH Key or Username/Password for the Git repository.
    - `kubeconfig`: Kubernetes config file (Secret File) to allow Jenkins to deploy to the cluster.

### Pipeline Configuration

For each microservice, you can create a **Pipeline** job.

**Pipeline Script Example (Jenkinsfile concept)**:

```groovy
pipeline {
    agent any
    
    environment {
        DOCKER_IMAGE = 'bomeravi/product-service' // Change for each service
        REGISTRY_CREDENTIALS = 'docker-hub-credentials'
    }

    stages {
        stage('Checkout') {
            steps {
                git credentialsId: 'git-credentials', url: 'https://github.com/your-repo/java-microservices.git'
            }
        }

        stage('Build with Maven') {
            steps {
                // Ensure Maven is configured or use a Maven container
                sh './mvnw clean package -DskipTests'
            }
        }

        stage('Build Docker Image') {
            steps {
                script {
                    docker.build("${DOCKER_IMAGE}:${BUILD_NUMBER}")
                    docker.build("${DOCKER_IMAGE}:latest")
                }
            }
        }

        stage('Push Docker Image') {
            steps {
                script {
                    docker.withRegistry('', REGISTRY_CREDENTIALS) {
                        docker.image("${DOCKER_IMAGE}:${BUILD_NUMBER}").push()
                        docker.image("${DOCKER_IMAGE}:latest").push()
                    }
                }
            }
        }

        stage('Deploy to Kubernetes') {
            steps {
                withKubeConfig([credentialsId: 'kubeconfig']) {
                    // Update image tag in deployment.yaml
                    sh "sed -i 's|image: ${DOCKER_IMAGE}:.*|image: ${DOCKER_IMAGE}:${BUILD_NUMBER}|' product-service/deployment.yaml"
                    
                    // Apply deployment
                    sh "kubectl apply -f product-service/deployment.yaml"
                    
                    // Force rollout to ensure new image is pulled if using latest (optional if using unique tags)
                    sh "kubectl rollout status deployment/product-service"
                }
            }
        }
    }
}
```

### Usage
- Create a new Item in Jenkins -> Pipeline.
- Use the script above (adapted for each service) or point it to a `Jenkinsfile` in the repository if you create one.
- Each service directory has a `deployment.yaml` specifically for this purpose.

---

## 3. Kubernetes Update Procedures

### Updating a Microservice (Manual)
If you need to manually update a service without Jenkins:

1.  **Build and Push**:
    ```bash
    ./mvnw clean package -pl product-service -am
    docker build -t bomeravi/product-service:v2 ./product-service
    docker push bomeravi/product-service:v2
    ```

2.  **Update Manifest**:
    Edit `k8s/product-service.yaml` and change the image tag to `v2`.

3.  **Apply**:
    ```bash
    kubectl apply -f k8s/product-service.yaml
    ```

### Updating Infrastructure
To update infrastructure (e.g., adding a new topic to Kafka or changing database settings):

1.  Edit the corresponding file in `k8s/infrastructure/`.
2.  Apply the change:
    ```bash
    kubectl apply -f k8s/namespace.yaml
kubectl apply -f k8s/infrastructure/kafka-platform.yaml
    ```

### Restarting a Service
If a service is stuck or acting up:
```bash
kubectl rollout restart deployment/product-service
```
