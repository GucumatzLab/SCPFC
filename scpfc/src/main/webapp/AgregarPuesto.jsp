 <!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html" charset="UTF-8">
        <title>Agregar Puesto</title>
        <style type="text/css">
            body {
                position: relative; 
                font-family: 'Open Sans', sans-serif;
                background: #ffffff;
                color: #000000;
                margin: 0;
                padding: 0;
            }

            
             h1 {
                font-family: 'Open Sans', sans-serif;
                margin-top: 10;
                 margin-bottom: 10;
             }

             #loc{
                background-color: blue;
                color : white;
             }
        </style>

    </head>
    <body>
        <center>
        	<h1>Nuevo Puesto de Comida</h1>
 			<form action="" method="post">
 			<table>
 			<tr>
 				<td><label for = "nombre">Nombre de Puesto : </label></td>
 				<td><input type="text" name="nombre2" value="Ninguno"></td>
 				<td></td>
 			</tr>
 			<tr>
 			 	<td><label for = "tipo">Tipo de Comida y Productos : </label></td>
 				<td><input type = "text" nombre="tipo" value="Ninguno"></td>
 				<td><input type = "text" nombre="producto" value="Ningun Producto"></td>
 			</tr>
 			<tr>
                <td><a href = "Localizar.jsp"><button for = "localizacion" type = "button" id = "loc">Localizacion</button></a></td>
                <td><input type="text" name="localizacion" value="Ninguna"></td>
                <td></td>
 			</tr>
 			<tr>
 				<td></td>
 				<td><input type="submit" value = "Agregar"></td>
 				<td></td>
 			</tr>
 			</table>
 			</form>
        </center>
    </body>
</html>

