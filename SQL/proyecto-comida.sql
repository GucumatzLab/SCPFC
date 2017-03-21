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
