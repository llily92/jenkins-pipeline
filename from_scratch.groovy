node { 
properties([buildDiscarder(logRotator(artifactDaysToKeepStr: '', artifactNumToKeepStr: '', daysToKeepStr: '', numToKeepStr: '5')), pipelineTriggers([cron('* * * * *')])])



stage("Stage1"){ 
echo "hello" 
} 
stage("Stage2"){ 
echo "hello" 
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

