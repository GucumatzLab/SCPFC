use gucumatz;

create table usuario (
  id serial primary key,
  nombre varchar(100) not null unique,
  correoElectronico varchar(100) not null unique,
  contrasena varchar(100) not null,
  codigoDeActivacion varchar(100), -- Para los correos de confirmación
  esAdministrador bool not null default false,
  confirmada bool not null default false,
  rutaImagen varchar(100)
);

create table puesto (
	 id serial primary key,
	 nombre varchar(100) not null,
	 tipoComida varchar(200) not null,
	referencias varChar(100) NOT NULL,
 	 latitud float not null,
 	 longitud float not null
);

create table comentario (
  id serial primary key,
  comentario varchar(1024) not null,
  fecha date not null,
  puesto_id integer not null references Puesto(id),
  usuario_id integer not null references Usuario(id)
);

create table calificacion (
  id serial primary key,
  calificacion float not null,
  puesto_id integer not null references Puesto(id),
  usuario_id integer not null references Usuario(id)
);

CREATE TABLE fotosPuesto (
	idPuesto Integer REFERENCES Puesto(id),
	url varChar(100) NOT NULL,
	PRIMARY KEY (idPuesto, url)
);
