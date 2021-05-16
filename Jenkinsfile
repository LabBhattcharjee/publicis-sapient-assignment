pipeline {
    environment {
        registry = "labbdocker/lab82jenkinsrepo"
        registryCredential = 'dockerhub_id'
        dockerImage = ''
    }

    agent any // { dockerfile true }

    tools {
        // Install the Maven version configured as "M3" and add it to the path.
        maven "M3"
    }

    stages {

        stage('SC checkout') {
            steps {
                echo 'checking out'
                git changelog: false, poll: false, url: 'https://github.com/labb1982/publicis-sapient-assignment.git', branch: 'main'
            }
        }

        stage('build install')
                {
                    steps {
                        echo 'h2'
                        // withEnv( ["PATH+MAVEN=${tool 'M3'}/bin"] ) {
                        sh 'mvn clean package -Dmaven.test.skip'
                        //}
                    }
                }


        stage('Building our image') {
            steps {
                script {
                    dockerImage = docker.build registry + ":$BUILD_NUMBER"
                }
            }
        }
        stage('Deploy our image') {
            steps {
                script {
                    docker.withRegistry('', registryCredential) {
                        dockerImage.push()
                    }
                }
            }
        }
        stage('Cleaning up') {
            steps {
                sh "docker rmi $registry:$BUILD_NUMBER"
            }
        }

    }
}