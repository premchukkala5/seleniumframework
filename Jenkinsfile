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
                ping -n 3 localhost >nul 2>&1
                for /f "tokens=*" %%i in ('docker ps -aq --filter "name=selenium"') do docker rm -f %%i 2>nul || exit /b 0
                ping -n 3 localhost >nul 2>&1
                echo Starting Selenium Grid...
                docker-compose up -d
                echo Waiting for Selenium Hub to be ready...
                setlocal enabledelayedexpansion
                set retries=60
                set count=0
                :wait_loop
                set /a count=!count! + 1
                if !count! gtr !retries! (
                    echo Grid failed to start after !retries! retries
                    docker logs selenium-hub
                    exit /b 1
                )
                docker ps --filter "name=selenium-hub" --filter "status=running" | find "selenium-hub" >nul 2>&1
                if errorlevel 1 (
                    echo Attempt !count!/!retries!: Waiting for Hub container to be running...
                    ping -n 2 localhost >nul 2>&1
                    goto wait_loop
                )
                echo Attempt !count!/!retries!: Hub container is running!
                ping -n 3 localhost >nul 2>&1
                goto grid_ready
                :grid_ready
                echo Grid is ready, proceeding with tests
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
                archiveArtifacts 'target/surefire-reports/**'
                archiveArtifacts 'test-output/**'
            }
        }
        stage('Publish Extent Report') {
            steps {
                publishHTML([
                    allowMissing: false,
                    alwaysLinkToLastBuild: true,
                    keepAll: true,
                    reportDir: 'test-output',
                    reportFiles: 'ExtentReport.html',
                    reportName: 'Extent Automation Report'
                ])
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
         failed
         {
         emailext(
                    subject: "Automation Test Report - Build #${BUILD_NUMBER}",
                    body: """
                        Hi Team,<br/><br/>
                        Please find the TestNG emailable report below:<br/>
                        <a href="${BUILD_URL}Extent_20Automation_20Report/">View Report</a><br/><br/>
                        Thanks
                    """,
                    mimeType: 'text/html',
                    to: 'chukkalatheertham@gmail.com'
                )
         }
    }
}
