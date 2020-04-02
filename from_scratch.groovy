node { 
properties([
    // Below line sets "Discard build more than 5"
    buildDiscarder(logRotator(artifactDaysToKeepStr: '', artifactNumToKeepStr: '', daysToKeepStr: '', numToKeepStr: '5')),
    // Below line triggers this job every minute 
     pipelineTriggers([pollSCM('* * * * *')]),
     parameters([choice(choices: [
			'dev1.kuzyshynliliia.com', 
			'qa1.kuzyshynliliia.com', 
			'stage1.kuzyshynliliia.com', 
			'prod1.kuzyshynliliia.com'], 
			description: 'Please choose an environment', 
			name: 'ENVIR')]), 
        ])
       
stage("Pull repo"){ 
    git 'https://github.com/farrukh90/cool_website.git'
} 
stage("Install prerequisites"){ 
    sh """
    ssh centos@${ENVIR}               sudo yum install httpd -y
    """
} 
stage("Copy artifacts"){ 
   sh """
   scp -r *  centos@${ENVIR}:/tmp
   ssh centos@${ENVIR}                  sudo cp -r /tmp/index.html /var/www/html/
   ssh centos@${ENVIR}                  sudo cp -r /tmp/style.css /var/www/html/
   ssh centos@${ENVIR} 				   sudo chown centos:centos /var/www/html/
   ssh centos@${ENVIR} 				   sudo chmod 777 /var/www/html/*
   """
} 
stage("Restart web server"){ 
    sh "ssh centos@${ENVIR}                 sudo systemctl restart httpd"
} 
stage("Slack"){ 
    slackSend color: '#BADA55', message: 'Hello, World!' 
}
} 

