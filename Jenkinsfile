#!/usr/bin/env groovy

import com.coremedia.cm.DockerAgent
import com.coremedia.cm.Jenkins
import org.jenkinsci.plugins.workflow.libs.Library

@Library(['coremedia-internal']) _

String GIT_URL = 'https://github.com/CoreMedia/jangaroo-tools.git'

pipeline {
  agent {
    label Jenkins.nodeLabelDefault
  }

  options {
    timestamps()
    timeout(time: 1, unit: 'HOURS')
    buildDiscarder(logRotator(numToKeepStr: '30', artifactNumToKeepStr: '30'))
    durabilityHint('PERFORMANCE_OPTIMIZED')
  }

  parameters {
    string(name: 'BRANCH', defaultValue: 'main', description: 'The branch to build.')
    booleanParam(name: 'RELEASE', defaultValue: false, description: 'Create a new release from the given branch.')
  }

  environment {
    // global npm folder
    PREFIX = "${env.WORKSPACE}/.global"
    NPM_CONFIG_CACHE = "${env.WORKSPACE}/.cache/npm"
    NPM_CONFIG_STRICT_SSL = "false"
  }

  stages {
    stage('Snapshot Build') {
      when {
        expression { !params.RELEASE }
        beforeAgent true
      }
      agent {
        docker {
          image 'maven:3.6.3-amazoncorretto-8'
          args DockerAgent.defaultMavenArgs
          reuseNode true
        }
      }
      steps {
        cmMaven(cmd: 'deploy', scanMvnLog: true)
      }
    }

    stage('Release') {
      when {
        expression { params.RELEASE }
      }
      steps {
        script {
          cmMavenRelease(mvnImage: 'maven:3.6.3-amazoncorretto-8',
                  branch: params.BRANCH,
                  gitUrl: GIT_URL,
                  nexusStagingProfileName: 'coremedia-internal-releases')
        }
      }
    }
  }

  post {
    always {
      cmCleanup()
    }
  }
}
