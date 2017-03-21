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
};

CREATE TABLE FotosPuesto {
	idPuesto Integer REFERENCES Puesto(id),
	url varChar(100) NOT NULL,
	PRIMARY KEY (idPuesto, url)
}

CREATE TABLE PuestoTipoComida {
	idPuesto Integer NOT NULL REFERENCES Puesto(id),
	idComida Integer NOT NULL REFERENCES TipoComida(id),
	PRIMARY KEY (idPuesto, idComida)
};