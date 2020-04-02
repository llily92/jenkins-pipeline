node { 
properties([
    // Below line sets "Discard build more than 5"
    buildDiscarder(logRotator(artifactDaysToKeepStr: '', artifactNumToKeepStr: '', daysToKeepStr: '', numToKeepStr: '5')),
    // Below line triggers this job every minute 
     pipelineTriggers([pollSCM('* * * * *')])
     parameters([choice(choices: [
			'dev1.acirrustech.com', 
			'qa1.acirrustech.com', 
			'stage1.acirrustech.com', 
			'prod1.acirrustech.com'], 
			description: 'Please choose an environment', 
			name: 'ENVIR')]), 
        ])
       
stage("Pull repo"){ 
    git 'https://github.com/farrukh90/cool_website.git'
} 
stage("Install prerequisites"){ 
    sh """
    ssh centos@3.81.121.8                 sudo yum install httpd -y
    """
} 
stage("Copy artifacts"){ 
   sh """
   scp -r *  centos@3.81.121.8:/tmp
   ssh centos@3.81.121.8                 sudo cp -r /tmp/index.html /var/www/html/
   ssh centos@3.81.121.8                 sudo cp -r /tmp/style.css /var/www/html/
   ssh centos@3.81.121.8				   sudo chown centos:centos /var/www/html/
   ssh centos@3.81.121.8				   sudo chmod 777 /var/www/html/*
   """
} 
stage("Restart web server"){ 
    sh "ssh centos@3.81.121.8                 sudo systemctl restart httpd"
} 
stage("Slack"){ 
    slackSend color: '#BADA55', message: 'Hello, World!' 
}
} 

