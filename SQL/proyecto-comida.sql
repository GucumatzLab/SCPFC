use gucumatz;

drop table if exists comentario;
drop table if exists calificacion;
drop table if exists foto_puesto;
drop table if exists usuario;
drop table if exists puesto;

create table usuario (
  id serial primary key,
  nombre varchar(100) not null unique,
  correoElectronico varchar(100) not null unique,
  contrasena varchar(100) not null,
  codigoDeActivacion varchar(100), -- Para los correos de confirmación
  esAdministrador bool not null default false,
  confirmado bool not null default false,
  eliminado bool not null default false,
  rutaImagen varchar(100)
);

create table puesto (
  id serial primary key,
  nombre varchar(100) not null,
  tipoComida varchar(200) not null,
  horario varchar(100),
  ubicacion varchar(100),
  latitud double not null,
  longitud double not null
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

create table foto_puesto (
  id serial primary key,
  puesto_id bigint unsigned not null,
  url varchar(100) not null,

  foreign key (puesto_id) references puesto(id)
);
