pnlb
=========

Una vez bajado el repositorio para evitar que se suban cambios en lib hacer

git update-index --assume-unchanged lib/

Luego si se cambia algun JAR hacer 

git update-index --no-assume-unchanged lib/

====
Para hacer add de las clases modificadas sin subir los cambios al JAR hacer "git add [path]/\*.java
