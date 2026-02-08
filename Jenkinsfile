pipeline {
    agent any
    
    environment {
        DOCKER_USER = 'faso01'
        SRV_IMAGE = 'projet2-srv-calcul'
        CLI_IMAGE = 'projet2-cli-calcul'
    }

    stages {
        stage('Nettoyage et Préparation') {
            steps {
                echo 'Nettoyage des anciens builds...'
                sh 'ls -ala'
            }
        }

        stage('Build Images') {
            steps {
                // Build du Serveur (utilise le dossier SERVEUR)
                sh "docker build -t ${DOCKER_USER}/${SRV_IMAGE}:latest -f Dockerfile ./SERVEUR"
                
                // Build du Client (utilise le dossier CLIENT)
                sh "docker build -t ${DOCKER_USER}/${CLI_IMAGE}:latest -f Dockerfile ./CLIENT"
            }
        }

        stage('Push to Docker Hub') {
            steps {
                withCredentials([usernamePassword(credentialsId: 'docker-hub-creds', passwordVariable: 'DOCKER_HUB_PASSWORD', usernameVariable: 'DOCKER_HUB_USER')]) {
                    sh "echo \$DOCKER_HUB_PASSWORD | docker login -u \$DOCKER_HUB_USER --password-stdin"
                    
                    // Push des deux images
                    sh "docker push ${DOCKER_USER}/${SRV_IMAGE}:latest"
                    sh "docker push ${DOCKER_USER}/${CLI_IMAGE}:latest"
                }
            }
        }
    }
}
