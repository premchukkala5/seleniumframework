pipeline {
    agent any

    tools {
        maven 'Maven'
    }

    parameters {
        choice(name: 'BROWSER', choices: ['chrome', 'firefox', 'edge'])
        choice(name: 'EXECUTION_ENV', choices: ['grid', 'local'])
        booleanParam(name: 'HEADLESS', defaultValue: true)
    }

    stages {

        stage('Checkout') {
            steps {
                checkout scm
            }
        }

        stage('Start Grid') {
            when {
                expression { params.EXECUTION_ENV == 'grid' }
            }
            steps {
                bat """
                docker-compose down -v
                docker-compose up -d
                """
            }
        }

        stage('Run Tests') {
            steps {
                bat """
                mvn clean test ^
                -Dbrowser=${params.BROWSER} ^
                -Dheadless=${params.HEADLESS} ^
                -Dgrid=${params.EXECUTION_ENV == 'grid'}
                """
            }
        }

        stage('Archive Results') {
            steps {
                archiveArtifacts 'videos/**'
                archiveArtifacts 'target/surefire-reports/**'
            }
        }
    }

    post {
        always {
            script {
                if (params.EXECUTION_ENV == 'grid') {
                    bat 'docker-compose down'
                }
            }
        }
    }
}
