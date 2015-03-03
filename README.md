pnlb
=========

Una vez bajado el repositorio para evitar que se suban cambios en lib hacer

git update-index --assume-unchanged /lib

Luego si se cambia algun JAR hacer 

git update-index --no-assume-unchanged /lib
