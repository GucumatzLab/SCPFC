use gucumatz;

drop table if exists Comentario;
drop table if exists Calificacion;
drop table if exists FotosPuesto;
drop table if exists Usuario;
drop table if exists Puesto;

create table Usuario (
  id serial primary key,
  nombre varchar(100) not null unique,
  correoElectronico varchar(100) not null unique,
  contrasena varchar(100) not null,
  codigoDeActivacion varchar(100), -- Para los correos de confirmación
  esAdministrador bool not null default false,
  confirmada bool not null default false,
  rutaImagen varchar(100)
);

create table Puesto (
	 id serial primary key,
	 nombre varchar(100) not null,
	 tipoComida varchar(200) not null,
	referencias varChar(100) NOT NULL,
 	 latitud float not null,
 	 longitud float not null
);

create table Comentario (
  id serial primary key,
  comentario varchar(1024) not null,
  fecha date not null,
  puesto_id bigint unsigned not null,
  usuario_id bigint unsigned not null,
  foreign key (puesto_id) references Puesto(id),
  foreign key (usuario_id) references Usuario(id)
);

create table Calificacion (
  id serial primary key,
  calificacion float not null,
  puesto_id bigint unsigned not null,
  usuario_id bigint unsigned not null,
  foreign key (puesto_id) references Puesto(id),
  foreign key (usuario_id) references Usuario(id)
);

CREATE TABLE FotosPuesto (
  idPuesto bigint unsigned,
  url varChar(100) NOT NULL,
  PRIMARY KEY (idPuesto, url),
  foreign key (idPuesto) references Puesto(id)
);
