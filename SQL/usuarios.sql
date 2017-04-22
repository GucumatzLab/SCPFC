use gucumatz;

insert into usuario (nombre, correoElectronico, contrasena, esAdministrador, confirmada)
values ('admin', 'admin-scpfc@', '1234', true, true),
       ('user', 'user-scpfc@', '1234', false, true);
