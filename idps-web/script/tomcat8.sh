export JAVA_HOME=/usr/lib/jvm/java-1.8.0
export TOMCAT_HOME=/opt/apache-tomcat-8.0.35

case $1 in
start)
  sh $TOMCAT_HOME/bin/startup.sh
;;
stop)
  sh $TOMCAT_HOME/bin/shutdown.sh
;;
restart)
  sh $TOMCAT_HOME/bin/shutdown.sh
  sh $TOMCAT_HOME/bin/startup.sh
;;
esac
exit 0