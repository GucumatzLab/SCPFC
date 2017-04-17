<html>
  <head>
    <meta charset="ISO-8859-1" />
  </head>
  <body>
    <h1>Crear cuenta</h1>

    <div style="margin: auto; width: 60%">
      <form action="bienvenida.jsp">
        <div>
          <div style="float: left; width: 50%">

            <div>
              <label>
                Nombre de usuario
                <input type="text" value="usuario" />
              </label>
            </div>

            <div>
              <label>
                Correo electrónico
                <input type="email" value="usuario@ciencias.unam.mx" />
              </label>
            </div>

            <div>
              <label>
                Contraseña
                <input type="password" value="1234" />
              </label>
            </div>

            <div>
              <label>
                Confirma tu contraseña
                <input type="password" value="1234" />
              </label>
            </div>

          </div>

          <div style="float: right; width: 50%">
            <div>
              <img src="" alt="Aquí va la foto del usuario"
                   width="200" height="200" />
            </div>
            <div>
              <label>
                Subir foto
                <input type="file" value="Subir foto" />
              </label>
            </div>
          </div>
        </div>

        <div>
          <input type="submit" value="Registrar" />
        </div>
      </form>
    </div>

  </body>
</html>
