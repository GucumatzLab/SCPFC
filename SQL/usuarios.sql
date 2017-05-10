use gucumatz;

insert into usuario (nombre, correoElectronico, contrasena, esAdministrador, confirmado)
values ('admin', 'admin-scpfc@', '1234', true, true),
       ('user', 'user-scpfc@', '1234', false, true);
