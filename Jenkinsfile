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
                echo Cleaning up existing containers...
                docker-compose down -v --remove-orphans || exit /b 0
                waitfor /t 5 /c || exit /b 0
                for /f "tokens=*" %%i in ('docker ps -aq --filter "name=selenium"') do docker rm -f %%i 2>nul || exit /b 0
                ping -n 3 localhost >nul 2>&1 || exit /b 0
                echo Starting Selenium Grid...
                docker-compose up -d
                echo Waiting for containers to be ready...
                ping -n 11 localhost >nul 2>&1
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
