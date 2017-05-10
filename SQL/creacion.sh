#!/bin/bash

mysql -ugucumatz -p1234 < proyecto-comida.sql
mysql -ugucumatz -p1234 < usuarios.sql
mysql -ugucumatz -p1234 < puestos.sql
mkdir -p /tmp/scpfc
rm -r /tmp/scpfc/*
cp -r imagenes /tmp/scpfc
