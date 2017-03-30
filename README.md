# Instrucciones

## Configuración del servidor de base de datos

Debe existir una base de datos llamada `gucumatz`. Si no existe se
puede crear con `create database gucumatz;`.

Para crear las tablas de la base de datos se utiliza el script
`SQL/proyecto-comida.sql`. Se puede correr desde `mysql` con el
comando `source <ruta del proyecto>/SQL/proyecto-comida.sql;`.

La configuración de la conexión de la aplicación a la base de datos
está en `scpfc/src/main/resources/META-INF/persistence.xml`. La
configuración actual requiere lo siguiente:

* El servidor de MySQL debe ser accesible desde el puerto 3306 de
  localhost.

* Debe existir un usuario `gucumatz` con contraseña `1234` con
  privilegios en la base de datos `gucumatz`. Lo mas sencillo es que
  tenga todos los privilegios aunque posiblemente no sea
  necesario. Esto se puede hacer desde `mysql` con:

  ```sql
  create user 'gucumatz'@'localhost' identified by '1234';
  grant all privileges on gucumatz.* to 'gucumatz'@'localhost';
  ```

  Posiblemente se necesite usar al usuario `root` para lo anterior.

La alternativa a lo anterior es modificar `persistence.xml` para la
configuración existente de MySQL.

## Ejecutando la aplicación

Desde el directorio `scpfc` hacer `mvn clean install tomcat7:run`. La
aplicación se puede ver desde `localhost:8080/scpfc`. De momento no
hay una página principal, así que se necesita ir directamente a la
página que se desea ver, por ejemplo
`localhost:8080/scpfc/ver-mapa.jsp`
