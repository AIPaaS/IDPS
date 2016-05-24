# Pull base image  
FROM centos:7  
  
# Install GraphicsMagick  

RUN mkdir pkg
ADD ./pkg/GraphicsMagick-1.3.23.tar.gz /pkg/GraphicsMagick-1.3.23.tar.gz
RUN cd /pkg/GraphicsMagick-1.3.23

#install image libs
RUN yum install -y libpng-devel libpng libjpeg libjpeg-devel libpng libpng-devel libtiff-devel libtiff \
libwmf-devel libwmf libxml2-devel libxml2 zlib-devel zlib gd-devel gd bzip2 bzip2-devel libzip-devel libzip gcc automake autoconf libtool make

RUN yum install -y java-1.8.0-openjdk

RUN ./pkg/GraphicsMagick-1.3.23/configure --prefix=/opt/freeware/GraphicsMagick-1.3.23 && make && make install


RUN mkdir /ori
RUN mkdir /dest
RUN mkdir /uploads
# Install tomcat7 
ADD ./pkg/apache-tomcat-8.0.35.tar.gz /pkg/ 
RUN  tar -xvf apache-tomcat-8.0.35.tar.gz  && mv /pkg/apache-tomcat-8.0.35/ /opt/apache-tomcat-8.0.35
#change config parameter

COPY ./build/libs/iPaaS-IDPS.war /opt/apache-tomcat-8.0.35/webapps/idps.war

ENV CATALINA_HOME /opt/apache-tomcat-8.0.35  
ENV PATH $PATH:$CATALINA_HOME/bin  


ADD ./tomcat8.sh /etc/init.d/tomcat8  
RUN chmod 755 /etc/init.d/tomcat8  
ADD idps.sh /idps.sh
RUN chmod 755 /*.sh  
# Expose ports.  
EXPOSE 8080  
  
# Define default command.  
CMD ["/idps.sh"]