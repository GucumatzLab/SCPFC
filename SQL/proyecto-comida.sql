use gucumatz;

<<<<<<< HEAD
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
=======
create table Usuario (
  id serial primary key,
  nombre varchar(100) not null
);

create table Puesto (
  id serial primary key
);

create table Comentario (
  id serial primary key,
  comentario varchar(1024) not null,
  fecha date not null,
  puesto_id integer not null references Puesto(id),
  usuario_id integer not null references Usuario(id)
);

create table Calificacion (
  id serial primary key,
  calificacion float not null,
  puesto_id integer not null references Puesto(id),
  usuario_id integer not null references Usuario(id)
);
>>>>>>> master
