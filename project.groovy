pipeline {
    agent any

    environment {
        CREDENTIALS_ID = "sunnyramagiri"
    }

    stages {
        stage('Checkout Code') {
            steps {
                git 'https://github.com/sunnyramagiri/youtubetab.git'
            }
        }

        stage('Build Docker Image') {
            steps {
                sh "docker build -t ${wordpress}:${wordpress_TAG} ."
            }
        }

        stage('Login to Docker Hub') {
            steps {
                echo "Logging into Docker Hub"
                withCredentials([usernamePassword(credentialsId: CREDENTIALS_ID, usernameVariable: 'USER', passwordVariable: 'PASS')]) {
                    sh "echo $PASS | docker login -u $USER --password-stdin"
                }
            }
        }

        stage('Push Docker Image') {
            steps {
                sh "docker push ${wordpress}:${wordpress}"
            }
        }

        stage('Cleanup') {
            steps {
                sh "docker rmi ${wordpress}:${wordpress}"
            }
        }
    }

    post {
        success {
            echo "✅ WordPress Docker image pushed successfully!"
        }
        failure {
            echo "❌ Pipeline failed! Check the logs."
        }
    }
}
