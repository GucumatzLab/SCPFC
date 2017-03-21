use gucumatz;

create table TipoComida(
	id serial primary key,
	nombre varchar(100) not null,
	productos varchar(500) not null
);
create table Puesto (
	 id serial primary key,
	 nombre varchar(100) not null,
	 tipoComida integer not null references TipoComida(id),
 	 latitud float not null,
 	 longitud float not null,
 	 rutaImagen varchar(200)
);
