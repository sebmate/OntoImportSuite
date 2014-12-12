#!/bin/bash

export JENAROOT=./Jena-2.6.4/

rm Input.N3

echo "\nConverting Triples to UTF-8 ..."

iconv --from-code=ISO-8859-1 --to-code=UTF-8 ONTOLOGY_N3.txt > ONTOLOGY_N3_utf.txt

echo "Merging Head with Triples ..."

cat HeadEr.N3 > Input.N3

cat ONTOLOGY_N3_utf.txt | sed 's/""/#/g' | sed 's/"LINE"//g;s/"//g' | sed 's/#/"/g' | sed 's/""/"/g' >> Input.N3

echo "Converting N3 to RDF/XML ..."

./Jena-2.6.4/bin/rdfcopy Input.N3 N3 RDF/XML > Soarian.owl

echo "Done!\n"
