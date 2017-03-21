<html>
	<head>
		<title>SCPFC | Mapa</title>
		<style>
				#map {
					height: 400px;
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
	    <h2>Mapa de puestos</h2>
	    <div id="map"></div>
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
	    src="https://maps.googleapis.com/maps/api/js?key=AIzaSyD2A2vIybClb6npOfNcdDt_zA6J7HEiFYk&callback=initMap">
	    </script>
	</body>
</html>