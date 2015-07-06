#!/bin/bash

docker_output_dir="${DOCKER_OUTPUT_DIR}"
docker_image_name="${DOCKER_IMAGE_NAME}"

# the directory where the Tomcat container will output it's log files
docker_tomcat_log_dir=${docker_output_dir}/log

docker_container_property_file=${docker_output_dir}/container.properties

## pull image from repository if not already localy stored
docker images | grep $docker_image_name
if [[ $? != 0 ]]; then
   docker pull $docker_image_name
fi

mkdir -p ${docker_output_dir}
mkdir -p ${docker_tomcat_log_dir}

## start a container from the image with the log files from tomcat mapped to the host
container_id=$(docker run ${docker_run_args} -v ${docker_tomcat_log_dir}:/opt/tomcat/logs -d ${docker_image_name})
container_ip=$(docker inspect --format '{{.NetworkSettings.IPAddress}}' ${container_id})

# writing the container properties in java properties format to be read and exposed to next maven phase
echo "CONTAINER_ID=${container_id}"$'\r' > ${docker_container_property_file}
echo "CONTAINER_IP=${container_ip}" >> ${docker_container_property_file}

tomcat_log="${docker_tomcat_log_dir}/catalina.$(date +%Y-%m-%d).log"
