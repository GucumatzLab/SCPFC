use gucumatz;

create table Usuario (
  id serial primary key,
  nombre varchar(100) not null,
  correoElectronico varchar(100) not null,
  contrasena varchar(100) not null,
  codigoDeActivacion varchar(100), -- Para los correos de confirmación
  esAdministrador bool not null default false,
  confirmada bool not null default false,
  rutaImagen varchar(100)
);

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
