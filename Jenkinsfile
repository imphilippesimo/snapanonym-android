def CONTAINER_NAME = "snapanonym-android"
def CONTAINER_TAG = "latest"
def DOCKER_HUB_USER = "imzerofiltre"
def APP_NAME = 'Snap\'anonym'

node {
    try {

        stage('Initialize') {
            def dockerHome = tool 'myDocker'
            env.PATH = "${dockerHome}/bin:${env.PATH}"
        }

        // Stage, is to tell the Jenkins that this is the new process/step that needs to be executed
        stage('Checkout') {
            // Pull the code from the repo
            checkout scm
        }

        stage('Image Build') {
            imageBuild(CONTAINER_NAME, CONTAINER_TAG)
        }

        stage('Run application test') {
            runTestInContainer(PROJECT)
        }

        stage('Push to Docker Registry') {
            withCredentials([usernamePassword(credentialsId: 'DockerhubCredentials', usernameVariable: 'USERNAME', passwordVariable: 'PASSWORD')]) {
                pushToImage(CONTAINER_NAME, CONTAINER_TAG, USERNAME, PASSWORD)
            }
        }

        stage('Push app to the play store') {
            switch (env.BRANCH_NAME) {
                case "master":
                    pushToPlay()
                    break
                case "develop":
                    pushToPlay()
                    break
            }
        }

        stage("Clean garbage") {
            cleanGarbage()
        }
    } catch (e) {
        currentBuild.result = "FAILED"
        throw e
    }
}


def imageBuild(containerName, tag) {
    sh "docker build -t $containerName:$tag  -t $containerName --pull --no-cache ."
    echo "Image build complete"
}

def runTestInContainer(containerName) {
    // If you need environmental variables in your image. Why not load it attach it to the image, and delete it afterward
    sh("env >> .env")
    sh("docker run --env-file .env --rm ${containerName} ./gradlew test")
    sh("rm -rf .env")
}

def pushToImage(containerName, tag, dockerUser, dockerPassword) {
    sh "docker login -u $dockerUser -p $dockerPassword"
    sh "docker tag $containerName:$tag $dockerUser/$containerName:$tag"
    sh "docker push $dockerUser/$containerName:$tag"
    echo "Image push complete"
}

def pushToPlay(containerName) {
    sh("env >> .env")
    sh("docker run --env-file .env --rm ${containerName} ./gradlew clean build assemble publishApkRelease")
    sh("rm -rf .env")

}

def cleanGarbage() {
    try {
        sh "docker image prune -f"
        sh "docker stop $containerName"
        sh "docker container prune -f"
        sh "docker volume prune -f"
    } catch (error) {
    }

}


//def notifyBuild(String buildStatus = 'STARTED') {
//    buildStatus = buildStatus ?: 'SUCCESSFUL'
//
//    def color = 'RED'
//    def colorCode = '#FF0000'
//    def subject = "${buildStatus}: Job '${env.JOB_NAME} [${env.BUILD_NUMBER}]'"
//    def summary = "${subject} (${env.BUILD_URL})"
//    def details = """<p>STARTED: Job '${env.JOB_NAME} [${env.BUILD_NUMBER}]':</p>
//    <p>Check console output at &QUOT;<a href='${env.BUILD_URL}'>${env.JOB_NAME} [${
//        env.BUILD_NUMBER
//    }]</a>&QUOT;</p>"""
//
//    if (buildStatus == 'STARTED') {
//        color = 'YELLOW'
//        colorCode = '#FFCC00'
//    } else if (buildStatus == 'SUCCESSFUL') {
//        color = 'GREEN'
//        colorCode = '#228B22'
//    } else {
//        color = 'RED'
//        colorCode = '#FF0000'
//    }
//
//    slackSend(color: colorCode, message: summary)
//}