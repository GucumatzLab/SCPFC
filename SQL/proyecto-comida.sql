use gucumatz;

CREATE TABLE TipoComida {
	id Serial PRIMARY KEY,
	nombre varChar(100) NOT NULL,
	productos varChar(200) NOT NULL
};

CREATE TABLE Puesto {
	id Serial PRIMARY KEY,
	nombre varChar(100) NOT NULL,
	referencias varChar(100) NOT NULL,
	longitud Float NOT NULL,
	latitud Float NOT NULL,
	urlFoto varChar(100)
};

CREATE TABLE PuestoTipoComida {
	idPuesto Integer NOT NULL,
	idComida Integer NOT NULL,
	PRIMARY KEY (idPuesto, idComida)
};