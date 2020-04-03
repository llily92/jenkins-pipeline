node { 
    // Keeps only 5 builds
properties([
    buildDiscarder(logRotator(artifactDaysToKeepStr: '', artifactNumToKeepStr: '', daysToKeepStr: '5', numToKeepStr: '')),
   pipelineTriggers([
       // Runs with "Input" job
        upstream('Input'),
        // Executes every minute
        cron('* * * * *')])
        ])
stage("Stage1"){ 
git 'https://github.com/farrukh90/packer.git' 
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
node { 
// some block 
}  
} 
} 