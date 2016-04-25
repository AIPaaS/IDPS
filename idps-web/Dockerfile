# Pull base image  
FROM centos:7  
  
MAINTAINER jianhua.ma "majh5@asiainfo.com"  

# Install GraphicsMagick  
RUN yum -y install wget
RUN mkdir docker && cd docker && wget http://sourceforge.net/projects/graphicsmagick/files/graphicsmagick/1.3.19/GraphicsMagick-1.3.19.tar.gz && tar -xvf GraphicsMagic
k-1.3.19.tar.gz && cd /docker/GraphicsMagick-1.3.19
RUN yum install -y libpng-devel libpng libjpeg libjpeg-devel libpng libpng-devel libtiff-devel libtiff \
libwmf-devel libwmf libxml2-devel libxml2 zlib-devel zlib gd-devel gd bzip2 bzip2-devel libzip-devel libzip gcc automake autoconf libtool make
RUN ./docker/GraphicsMagick-1.3.19/configure --prefix=/opt/freeware/GraphicsMagick-1.3.19 && make && make install
RUN mkdir /originimage
RUN mkdir /processedimage
RUN mkdir /file
# Install tomcat7 
ADD ./apache-tomcat-7.0.67.tar /docker/ 
RUN mv /docker/apache-tomcat-7.0.67/ /opt/tomcat7/
ADD ./jre.tar /opt/java/  
COPY ./iPaas-IDPS /opt/tomcat7/webapps/ 
ADD ./jdbc.properties /opt/tomcat7/webapps/iPaas-IDPS/WEB-INF/classes/context/ 
ENV CATALINA_HOME /opt/tomcat7  
ENV PATH $PATH:$CATALINA_HOME/bin  
ENV JAVA_HOME /opt/java/jre
ADD ./tomcat7.sh /etc/init.d/tomcat7  
RUN chmod 755 /etc/init.d/tomcat7  
ADD idps_gm_tomcat_run.sh /idps_gm_tomcat_run.sh
RUN chmod 755 /*.sh  
# Expose ports.  
EXPOSE 8080  
  
# Define default command.  
CMD ["/idps_gm_tomcat_run.sh"]