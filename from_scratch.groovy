node { 
properties([
    // Below line sets "Discard build more than 5"
    buildDiscarder(logRotator(artifactDaysToKeepStr: '', artifactNumToKeepStr: '', daysToKeepStr: '', numToKeepStr: '5')),
    // Below line triggers this job every minute 
     pipelineTriggers([pollSCM('* * * * *')])
        ])
       
stage("Pull repo"){ 
    git 'https://github.com/farrukh90/cool_website.git'
} 
stage("Install prerequisites"){ 
sudo """
sudo yum install httpd -y 
sudo cp -r * /var/www/html/
sudo systemctl start httpd


"""
} 
stage("Stage3"){ 
echo "hello" 
} 
stage("Stage4"){ 
echo "hello" 
} 
stage("Stage5"){ 
echo "hello" 
}
} 

