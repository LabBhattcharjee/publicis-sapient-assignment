node {
  stage('SC checkout'){
    git 'https://github.com/LabBhattcharjee/coreJavaPublic'
  } 
  
  stage('build install'){
    sh 'mvn clean install -DskipTests'
  }
}
