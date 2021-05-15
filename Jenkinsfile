node {
  stage('SC checkout'){
    git 'https://github.com/LabBhattcharjee/publicis-sapient-assignment'
  } 
  
  stage('build install'){
    sh 'mvn clean install -DskipTests'
  }
}
