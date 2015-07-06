FROM        dockerfile/ubuntu
ENV TOMCAT_VERSION 7.0.57
ENV DEPLOY_DIR /deploy

VOLUME      ["/var/log/tomcat7"]
RUN apt-get install -y software-properties-common
RUN add-apt-repository -y ppa:webupd8team/java
RUN apt-get update
RUN echo debconf shared/accepted-oracle-license-v1-1 select true | debconf-set-selections
RUN echo debconf shared/accepted-oracle-license-v1-1 seen true | debconf-set-selections
RUN apt-get install -y oracle-java7-installer

# Get and Unpack Tomcat
RUN wget http://archive.apache.org/dist/tomcat/tomcat-7/v${TOMCAT_VERSION}/bin/apache-tomcat-${TOMCAT_VERSION}.tar.gz -O /tmp/catalina.tar.gz && tar xzf /tmp/catalina.tar.gz -C /opt && ln -s /opt/apache-tomcat-${TOMCAT_VERSION} /opt/tomcat && rm /tmp/catalina.tar.gz

ADD tomcat-users.xml /opt/tomcat/conf/
CMD JAVA_HOME=/usr/lib/jvm/java-7-oracle CATALINA_BASE=/opt/tomcat CATALINA_HOME=/opt/tomcat /opt/tomcat/bin/catalina.sh run
EXPOSE 8080
