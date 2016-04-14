# Pull base image  
FROM centos:7  
  

# Install tomcat7 
ADD ./apache-tomcat-7.0.67.tar /docker/ 
RUN mv /docker/apache-tomcat-7.0.67/ /opt/tomcat7/
ADD ./jre.tar /opt/java/  
COPY ./iPaas-me /opt/tomcat7/webapps/ 

ADD ./conf.properties /opt/tomcat7/webapps/iPaas-me/WEB-INF/classes/context/ 

ENV CATALINA_HOME /opt/tomcat7  
ENV PATH $PATH:$CATALINA_HOME/bin  
ENV JAVA_HOME /opt/java/jre

ADD ./tomcat7.sh /etc/init.d/tomcat7  
RUN chmod 755 /etc/init.d/tomcat7  
ADD tomcat_run.sh /tomcat_run.sh
RUN chmod 755 /*.sh
  
# Expose ports.  
EXPOSE 8080  
  
# Define default command.  
CMD ["/tomcat_run.sh"]