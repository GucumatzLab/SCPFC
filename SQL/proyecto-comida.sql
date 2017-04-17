use gucumatz;

drop table if exists comentario;
drop table if exists calificacion;
drop table if exists fotospuesto;
drop table if exists usuario;
drop table if exists puesto;

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
  puesto_id bigint unsigned not null,
  usuario_id bigint unsigned not null,
  foreign key (puesto_id) references puesto(id),
  foreign key (usuario_id) references usuario(id)
);

create table calificacion (
  id serial primary key,
  calificacion float not null,
  puesto_id bigint unsigned not null,
  usuario_id bigint unsigned not null,
  foreign key (puesto_id) references puesto(id),
  foreign key (usuario_id) references usuario(id)
);

CREATE TABLE fotospuesto (
  idPuesto bigint unsigned,
  url varChar(100) NOT NULL,
  PRIMARY KEY (idPuesto, url),
  foreign key (idPuesto) references puesto(id)
);
