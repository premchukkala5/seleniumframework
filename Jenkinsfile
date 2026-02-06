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
                ping -n 3 localhost >nul 2>&1 || exit /b 0
                for /f "tokens=*" %%i in ('docker ps -aq --filter "name=selenium"') do docker rm -f %%i 2>nul || exit /b 0
                ping -n 3 localhost >nul 2>&1 || exit /b 0
                echo Starting Selenium Grid...
                docker-compose up -d
                echo Waiting for Selenium Hub to be healthy...
                setlocal enabledelayedexpansion
                set retries=60
                set count=0
                :wait_loop
                set /a count=!count! + 1
                if !count! gtr !retries! (
                    echo Grid failed to become healthy after !retries! retries
                    docker logs selenium-hub
                    exit /b 1
                )
                docker ps -q -f name=selenium-hub >nul 2>&1
                if errorlevel 1 (
                    echo Attempt !count!/!retries!: Hub container not running yet...
                    ping -n 2 localhost >nul 2>&1
                    goto wait_loop
                )
                echo Attempt !count!/!retries!: Hub is running, checking connectivity...
                for /f "delims=" %%a in ('docker inspect -f "{{.State.Health.Status}}" selenium-hub 2^>nul') do set health=%%a
                if "!health!"=="healthy" (
                    echo Grid is healthy and ready!
                    goto grid_ready
                )
                echo Health status: !health!, waiting...
                ping -n 2 localhost >nul 2>&1
                goto wait_loop
                :grid_ready
                echo Grid startup complete
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
