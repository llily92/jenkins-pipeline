node { 
properties([
    // Below line sets "Discard build more than 5"
    buildDiscarder(logRotator(artifactDaysToKeepStr: '', artifactNumToKeepStr: '', daysToKeepStr: '', numToKeepStr: '5')),
    disableConcurrentBuilds(),
    // Below line triggers this job every minute 
     pipelineTriggers([pollSCM('* * * * *')]),
     parameters([
         // Asks for env to build
         choice(choices: [
			'dev1.kuzyshynliliia.com', 
			'qa1.kuzyshynliliia.com', 
			'stage1.kuzyshynliliia.com', 
			'prod1.kuzyshynliliia.com'], 
			description: 'Please choose an environment', 
			name: 'ENVIR'), 
             
             // Ask for version
            choice(choices:  [
            'v0.1', 
            'v0.2',
            'v0.3',
            'v0.4',
             'v0.5'
             ],  
            description: 'Which version should we deploy?',  
            name: 'Version'),

             // Asks for the input
            string(defaultValue: 'v1',
            description: 'Please enter version number',
            name: 'APP_VERSION',
            trim: true)

        ])
        ])
       
stage("Pull repo"){ 
    checkout([$class: 'GitSCM', branches: [[name: '*/FarrukH']], doGenerateSubmoduleConfigurations: false, extensions: [], submoduleCfg: [], userRemoteConfigs: [[url: 'https://github.com/farrukh90/cool_website.git']]])
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
ws ("tmp/") {
    sh "ssh centos@${ENVIR}                 sudo systemctl restart httpd"

}

} 
stage("Slack"){ 
    ws ("mnt/") {
        slackSend color: '#BADA55', message: 'Hello, World!' 
    }
}
} 

