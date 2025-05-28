def call(Map config = [:]) {
    pipeline {
        agent any

        environment {
            SERVICE_NAME = config.serviceName ?: 'default-service'
        }

        stages {
            stage('Checkout') {
                steps {
                    checkout scm
                }
            }

            stage('Restore Dependencies') {
                steps {
                    sh 'dotnet restore'
                }
            }

            stage('Build') {
                steps {
                    sh 'dotnet build --configuration Release --no-restore'
                }
            }

            stage('Test') {
                steps {
                    sh 'dotnet test --no-build --verbosity normal'
                }
            }

            stage('Publish Output') {
                steps {
                    sh 'dotnet publish -c Release -o publish_output'
                    archiveArtifacts artifacts: 'publish_output/**', fingerprint: true
                }
            }
        }
    }
}
