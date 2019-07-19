# Pull base image  
FROM centos:7  
  
# Install GraphicsMagick  

RUN mkdir pkg
ADD ./pkg/GraphicsMagick-1.3.32.tar.gz /pkg/GraphicsMagick-1.3.32.tar.gz
RUN cd /pkg/GraphicsMagick-1.3.32.tar.gz && mv /pkg/GraphicsMagick-1.3.32.tar.gz /pkg/GraphicsMagick-1.3.32 && cd /pkg/GraphicsMagick-1.3.32

#install image libs
RUN yum install -y libpng-devel libpng libjpeg libjpeg-devel libpng libpng-devel libtiff-devel libtiff \
libwmf-devel libwmf libxml2-devel libxml2 zlib-devel zlib gd-devel gd bzip2 bzip2-devel libzip-devel libzip gcc automake autoconf libtool make

RUN yum install -y java-1.8.0-openjdk
RUN cd /pkg/GraphicsMagick-1.3.32/GraphicsMagick-1.3.32 && ls -l
RUN cd /pkg/GraphicsMagick-1.3.32/GraphicsMagick-1.3.32 && ./configure --prefix=/opt/freeware/GraphicsMagick && make && make install


RUN mkdir /oriImg
RUN mkdir /destImg
RUN mkdir /uploads
# Install tomcat8 
ADD ./pkg/apache-tomcat-8.5.43.tar.gz /pkg/apache-tomcat-8.5.43
RUN  mv /pkg/apache-tomcat-8.5.43/apache-tomcat-8.5.43 /opt/apache-tomcat-8.5.43 && ls /opt/apache-tomcat-8.5.43
#change config parameter

RUN sed -i '/\# OS/i JAVA_OPTS="$JAVA_OPTS -server -Xms512M -Xmx512M -XX:PermSize=64M -XX:MaxPermSize=128M  -XX:+UseConcMarkSweepGC -XX:ParallelGCThreads=8 -XX:+PrintCommandLineFlags -XX:+PrintGCDetails -XX:+UseCompressedOops -XX:-UseLargePagesIndividualAllocation -XX:+HeapDumpOnOutOfMemoryError" \n if [[ "$JAVA_OPTS" != *-Djava.security.egd=* ]]; then \n   JAVA_OPTS="$JAVA_OPTS -Djava.security.egd=file:/dev/./urandom" \n  fi'  /opt/apache-tomcat-8.5.43/bin/catalina.sh
RUN rm -fr /opt/apache-tomcat-8.5.43/webapps/*

COPY ./build/libs/iPaaS-IDPS.war /opt/apache-tomcat-8.5.43/webapps/iPaas-IDPS.war

#设置时区
RUN /bin/cp /usr/share/zoneinfo/Asia/Shanghai /etc/localtime \
    && echo 'Asia/Shanghai' > /etc/timezone

ENV CATALINA_HOME /opt/apache-tomcat-8.5.43  
ENV PATH $PATH:$CATALINA_HOME/bin:/opt/freeware/GraphicsMagick/bin  
ENV PATH $CATALINA_HOME/bin:$PATH


ADD ./script/tomcat8.sh /etc/init.d/tomcat8  
RUN chmod 755 /etc/init.d/tomcat8  
ADD ./script/idps.sh /idps.sh
RUN chmod 755 /*.sh  
# Expose ports.  
EXPOSE 8080  

RUN rm -fr /pkg
  
# Define default command.  
CMD ["/idps.sh"]