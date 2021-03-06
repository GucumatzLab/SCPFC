# Instrucciones

## Configuración del servidor de base de datos

Debe existir una base de datos llamada `gucumatz`. Si no existe se
puede crear con `create database gucumatz;`.

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

## Base de datos

Para crear las tablas de la base de datos se utiliza el script
`SQL/proyecto-comida.sql`.

Se pueden crear usuarios de prueba con el script `SQL/usuarios.sql`.

La información de los puestos de la facultad se puede crear con el
script `SQL/puestos.sql`. Es necesario que no se haya insertado ningún
puesto en la tabla `puesto` antes de usar este script. Las fotografías
de los puestos se encuentran en el directorio `SQL/imagenes` y se
deben mover o copiar manualmente a la ubicación donde las busca la
aplicación. En este momento, esta es `/tmp/scpfc/`.

Todos los scripts se pueden ejecutar desde la línea de comandos de
`mysql` con el comando `source /ruta/al/archivo/script.sql` o desde una
terminal con `mysql < /ruta/al/archivo/script.sql` incluyendo los argumentos de
autenticación necesarios

La creación y población de las tablas se puede hacer con el script de
Bash `SQL/creacion.sh`. Este script utiliza al usuario `gucumatz` con
contraseña `1234` y debe ser ejecutado desde la carpeta SQL.

## Ejecutando la aplicación

Desde el directorio `scpfc` hacer `mvn clean install tomcat7:run`. La
aplicación se puede ver desde `localhost:8080/scpfc`.
