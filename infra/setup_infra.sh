teamcity_tests_directory=$(pwd)
workdir="teamcity_tests_infrastructure"
teamcity_server_workdir="teamcity_server"
teamcity_agent_workdir="teamcity_agent"
selenoid_workdir="selenoid"
teamcity_server_container_name="teamcity_server_instance"
teamcity_agent_container_name="teamcity_agent_instance"
selenoid_container_name="selenoid_instance"
selenoid_ui_container_name="selenoid_ui_instance"
container_names=($teamcity_server_container_name $teamcity_agent_container_name $selenoid_container_name $selenoid_ui_container_name)
workdir_names=($teamcity_server_workdir $teamcity_agent_workdir $selenoid_workdir)

#########################################

echo "Request IP"
export ip=$(ipconfig getifaddr en0)
echo "Current IP: $ip"

#########################################
echo "Delete previous run data"
cd infra
rm -rf $workdir
mkdir $workdir
cd $workdir

for dir in "${workdir_names[@]}"; do
  mkdir $dir
done

for container in "${container_names[@]}"; do
  docker stop $container
  docker rm $container
done
#########################################
echo "Start teamcity server"
cd $teamcity_server_workdir

docker run -d --name $teamcity_server_container_name \
-v $(pwd)/logs:/opt/teamcity/logs \
-p 8111:8111 \
jetbrains/teamcity-server

echo "Teamcity Server is running...."

#########################################

echo "Start teamcity agent"
cd .. && cd $teamcity_agent_workdir

docker run -d -e SERVER_URL="http://$ip:8111"  \
    -v $(pwd)/conf:/data/teamcity_agent/conf  \
    jetbrains/teamcity-agent

echo "Teamcity agent is started...."
#########################################
echo "Start selenoid"

cd .. && cd $selenoid_workdir
mkdir config
cp $teamcity_tests_directory/infra/browsers.json config/

docker run -d                                   \
--name $selenoid_container_name                                 \
-p 4444:4444                                    \
-v /var/run/docker.sock:/var/run/docker.sock    \
-v $(pwd)/config/:/etc/selenoid/:ro              \
aerokube/selenoid:latest-release

image_names=($(awk -F'"' '/"image": "/{print $4}' "$(pwd)/config/browsers.json"))

echo "Pull all browsers images: $image_names"

for image in "${image_names[@]}"; do
  docker pull $image
done

#########################################
echo "Start selenoid-ui"

docker run -d --name $selenoid_ui_container_name -p 8080:8080 aerokube/selenoid-ui --selenoid-uri http://$ip:4444

#########################################
echo "Setup teamcity server"
cd .. && cd .. && cd ..
printf "host=$ip:8111\nremote=http://$ip:4444/wd/hub\nbrowser=firefox" > $teamcity_tests_directory/src/main/resources/config.properties
mvn test -Dtest="SetupTest#setupTeamcityTest"
#########################################
echo "Parse superuser token"

superuser_token=$(grep -o 'Super user authentication token: [0-9]*' $teamcity_tests_directory/infra/$workdir/$teamcity_server_workdir/logs/teamcity-server.log | awk '{print $NF}')
echo "Super user token: $superuser_token"

#########################################
echo "Authorize teamcity agent"

printf "host=$ip:8111\nsuperUserToken=$superuser_token\nremote=http://$ip:4444/wd/hub\nbrowser=firefox" > $teamcity_tests_directory/src/main/resources/config.properties

cat $teamcity_tests_directory/src/main/resources/config.properties

mvn test -Dtest="AddAgentTest#authorizeAgentTest"
#########################################
echo "Run system tests"

mvn test -DsuiteXmlFile=teamcity-suites/api-suite.xml
mvn test -DsuiteXmlFile=teamcity-suites/ui-suite.xml