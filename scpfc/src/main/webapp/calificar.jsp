<html>
<head>
    <style type="text/css">
    @import url(http://fonts.googleapis.com/css?family=Open+Sans);

	html, body {
    	margin: 0;
  	 	min-height: 100%;
	}

	body {
   		position: relative; 
    	font-family: 'Open Sans', sans-serif;
    	background: #ffffff;
    	color: #000000;
      	margin: 0;
		padding: 0;
	}

	.overlay {
 		position: fixed;
  		top: 0;
  		left: 0;
  		height: 100%;
  		width: 100%;
  		z-index: 10;
  		background-color: rgba(0,0,0,0.5);
	}

	.modal {
   		width: 70%;
    	height: 25%;
    	line-height: 5px;
    	position: fixed;
    	top: 15%; 
    	left: 15%;
   		margin-top: 0px;
    	margin-left: 0px;
    	background-color: #ffffff;
    	border-radius: 5px;
    	text-align: center;
    	z-index: 11;
	}

	.content {
    	margin: 30px;
	}

	h1 {
    	font-family: 'Open Sans', sans-serif;
      	margin: 0;
	}
      
    .rating {
  		unicode-bidi: bidi-override;
  		direction: rtl;
	}

	.rating > span {
  		display: inline-block;
  		position: relative;
  		width: 1em;
	}

	.rating > span:hover:before,
	.rating > span:hover ~ span:before {
   		content: "\2605";
   		position: absolute;
	}
	
    </style>

</head>
<body>
    <div class="overlay"></div>
  <div class="modal">
    <br><br><br>
    <font size="+2"> <b>Calificación</b> </font>
	<br> <br><br><br><br> <br><br><br><br><br><br>
    <font size="+7">
    	<div class="rating">
			<span>☆</span><span>☆</span><span>☆</span><span>☆</span><span>☆</span>
		</div>
    </font>
    <br><br><br><br><br><br><br><br><br><br><br>
    <button type="button">Calificar</button>
  </div>
  <div class="content">
      <h1>Aquí se vería la información del puesto</h1>
      Lorem ipsum dolor sit amet, consectetur adipisicing elit. Sint blanditiis perspiciatis nesciunt possimus minus molestiae culpa necessitatibus atque ut eveniet id magnam delectus reprehenderit! Ad atque aperiam rerum quas vitae!Lorem ipsum dolor sit amet, consectetur adipisicing elit. Velit esse nihil iusto ea natus aliquam enim ducimus deleniti vitae quibusdam aperiam voluptatibus necessitatibus nulla id non consectetur commodi! Laborum nam.Lorem ipsum dolor sit amet, consectetur adipisicing elit. Dolorem non quo laudantium doloremque necessitatibus deserunt sed vero officiis iste asperiores quasi quae nisi ab debitis dignissimos delectus cum repellat quidem.Lorem ipsum dolor sit amet, consectetur adipisicing elit. Modi a deserunt nostrum eos magni porro vel error dolore cupiditate iure sint fugiat rerum accusamus voluptates tenetur doloremque debitis ad reprehenderit?Lorem ipsum dolor sit amet, consectetur adipisicing elit. Repellendus aut dolorum officia eos commodi sit cumque nulla impedit perspiciatis ipsum odit expedita harum praesentium ducimus id labore deserunt quo repellat.Lorem ipsum dolor sit amet, consectetur adipisicing elit. Odit in voluptas excepturi perspiciatis iste vel laborum sunt nostrum quaerat laboriosam enim amet molestiae fugiat quos illum recusandae cum asperiores. Doloribus.Lorem ipsum dolor sit amet, consectetur adipisicing elit. Consectetur blanditiis repudiandae voluptatum provident quaerat fugiat tenetur. Voluptates soluta unde saepe necessitatibus mollitia culpa itaque tenetur alias commodi omnis fugit corrupti!Lorem ipsum dolor sit amet, consectetur adipisicing elit. Tempora expedita reiciendis facilis eius ea pariatur at dolore aperiam reprehenderit perferendis non ipsa quidem inventore unde architecto veniam similique amet commodi.Lorem ipsum dolor sit amet, consectetur adipisicing elit. Obcaecati amet veritatis architecto a eos corrupti rerum cumque nemo distinctio ut necessitatibus doloribus tempore aliquam vero non facilis illum optio qui.Lorem ipsum dolor sit amet, consectetur adipisicing elit. Alias necessitatibus tempore error eaque quidem id nulla veritatis quas iste eveniet aliquid ut cumque suscipit sapiente totam voluptatem cum eum adipisci.Lorem ipsum dolor sit amet, consectetur adipisicing elit. Sunt dolores iste maxime qui adipisci eius placeat. Unde debitis veniam doloremque quae magni inventore temporibus dignissimos culpa earum ipsa possimus repudiandae!
  </div>
</body>

</html>