pipeline{
    agent any
    environment {
        DOCKERHUB_CREDENTIALS = credentials ('sunnyramagiri')
    }
    stages {
        stage('SCM checkout') {
            steps{
               git 'https://github.com/sunnyramagiri/youtubetab.git'
            }
        }
    }
    stage('Build docker image') {
        steps {
            sh 'docker build -t name of the sunnramagiri/nodeapp:$BUILD_NUMBER .'
        }
    }
    stage ('login to dockerhub') {
        steps{
            sh 'echo $DOCKERHUB_CREDENTIALS_PWS | docker login -u $DOCKERHUB_CREDENTIALS_USR --password-stdin'
        }
    }
    stage('push image') {
        stages{
            sh 'docker push sunnyramagiri/nodeapp:$BUILD_NUMBER'
        }
    }
}
post {
       always {
           sh 'docker logout'}
        }
    }
}
