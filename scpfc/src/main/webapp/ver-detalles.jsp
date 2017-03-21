<html>
        <head>
                <title>SCPFC | Mapa</title>
                <style>
                                #map {
                                        height: 400px;
                                        width: 50%;
                                        float: left;
                                }
                                #info {
                                        width: 50%;
                                        float: left;
                                }
                                #fotos {
                                        width: 100%;
                                }
                </style>
        </head>
        <body>
              <header>
                <h1>Sistema de Calificacion de puestos de Facultad de Ciencias</h1>
                <button type="button">Iniciar sesion</button>
                <button type="button">Registrarse</button>
              </header>
            <div>            
              <div id="map"></div>
              <div id="info">
                <div id="fotos">
                  <img style="width: 32%" src="./images/img(1).jpg">
                  <img style="width: 32%" src="./images/img(2).jpg">
                  <img style="width: 32%" src="./images/img(3).jpg">
                </div>
                <div>
                  <h1>Nombre del puesto</h1>
                  <h2>Ubicación exacta</h2>
                  <h3>(Referencias)</h3>
                  <div>
                    <img style="width: 5%" src="./images/star.png">
                    <img style="width: 5%" src="./images/star.png">
                    <img style="width: 5%" src="./images/star.png">
                    <img style="width: 5%" src="./images/star.png">
                    <img style="width: 5%" src="./images/star.png">
                  </div>
                </div>
                <div>
                  <p>Horario</p>
                  <div>
                    <h4>Menú</h4>
                    <ul>
                      <li>Comida 1</li>
                      <li>Comida 2</li>
                    </ul>
                  </div>
                </div>
                <div>
                  <button type="button">Comentar</button>
                  <button type="button">Calificar</button>
                </div>
              </div>
            </div>  
            <script>
              function initMap() {
                var uluru = {lat: -25.363, lng: 131.044};
                var map = new google.maps.Map(document.getElementById('map'), {
                  zoom: 4,
                  center: uluru
                });
                var marker = new google.maps.Marker({
                  position: uluru,
                  map: map
                });
              }
            </script>
            <script async defer
            src="https://maps.googleapis.com/maps/api/js?key=YOUR_API_KEY&callback=initMap">
            </script>
        </body>
</html>