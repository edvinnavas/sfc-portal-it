# SFC-PORTAL-IT

docker run -d --name SFC-MYSQL -p 3306:3306 --restart=always --env MYSQL_ROOT_PASSWORD=S3rv1c10s --env TZ=America/Guatemala --volume "/VolumenMysqlPortalIT":/var/lib/mysql mysql:latest
	mysql> CREATE DATABASE db_jasperserver;
	mysql> CREATE DATABASE db_portal_it;
	mysql> CREATE USER 'user_jasperserver'@'%' IDENTIFIED BY 'sfctadmin';
	mysql> CREATE USER 'user_portal_it'@'%' IDENTIFIED BY 'sfctadmin';
	mysql> GRANT ALL ON db_jasperserver.* TO 'user_jasperserver'@'%';
	mysql> GRANT ALL ON db_portal_it.* TO 'user_portal_it'@'%';

docker run -d --name SFC-PAYARA -p 4848:4848 -p 8016:8080 --restart=always --env TZ=America/Guatemala -v "/SFC_PORTAL_IT":/SFC_PORTAL_IT payara/server-full:6.2023.6

docker run -d --name SFC-GLASSFISH4-1 -p 4949:4848 -p 8019:8080 --restart=always --env TZ=America/Guatemala -v "/SFC_PORTAL_IT":/SFC_PORTAL_IT glassfish:4.1
	sh asadmin change-admin-password "admin"
	sh asadmin enable-secure-admin

docker build -t sfcterra/api-sfc-portal-it:1.0.0 .
docker run -t -i --name PORTAL-IT-API -p 8015:8015 --restart=always --env TZ=America/Guatemala -v "/SFC_PORTAL_IT":/SFC_PORTAL_IT sfcterra/api-sfc-portal-it:1.0.0

Enlaces Pos-Manager.
https://posmanager.ete.mx/WebFactElect/
https://posmanager.endtoend.com.mx/WebFactElect/