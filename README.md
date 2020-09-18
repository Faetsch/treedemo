# Aufgabe 2: Java-Backend

## Autor
- Fatih Kesikli
- Junior Java / Jakarta EE Developer Bewerber
- github.com/Faetsch/treedemo

## Tech Stack für dieses Projekt
- WildFly Application Server
- JAX-RS
- JPA
- Postgres DB
- Docker
- Maven

## Gedankenprozess & Feedback
Allgemein würde ich mich sehr über Feedback oder evtl. eine Musterlösung freuen, besonders wenn meine Abgabe nicht den Anforderungen entsprechen sollte. Hierzu im Folgenden eine Dokumentation von meinem Gedankenprozess während der Entwicklung.

### Zum UML
Es schien offensichtlich, dass man sich bei der Bearbeitung der Aufgabe zu den Schlüsseln der Entities selbst Gedanken machen sollte. Hierzu entschied ich mich für künstliche Schlüssel und entsprechenden Tabellenconstraints, da composite keys in JPA - nach meinem aktuellen Verständnis von JPA - wesentlich schlechter zu warten sind.
Die Semantik des Attributes *nummer:long* in der Entity *Baum* war mir nicht direkt ersichtlich. Der Typ long schien zu indizieren, dass es sich um einen Primärschlüssel halten sollte, aber nach einer kurzen Recherche ergab sich, dass Baumnummern nur innerhalb einer Straße eindeutig sind. Daher wurde ein constraint über Straße & Nummer gesetzt.
Komposition/Aggregation wurde so implementiert, sodass die dependency zum constraint hinzugefügt wurde bzw. nicht.

### Benennung der REST Endpoints
Da in den Anforderungen keine Anfragen auf Städte gemacht werden sollte, wurde hier die logische Struktur der Ressourcen teilweise ignoriert, aus Benutzerfreundlichkeitsgründen. Dadurch sind z.B. Straßen Städten nicht untergeordnet. Es könnten noch weitere Endpoints erzeugt werden, wie z.B. der Zugriff auf die Spezies eines Baumes könnte auch unter */streets/trees/1/species/* erreichbar sein, anstatt nur */trees/1/species/*.

### Erzeugung der Dummy-Objekte
Die Erzeugung der Dummy-Objekte in Java schien mir nicht elegant, aber hilfreich, um die Referenzen zwischen den Entitäten in der Datenbank darzustellen. Ein weiteres Problem wäre der heap space der nicht-garbage-collecteten Objekten bei der Erzeugung von zu vielen dummies. Eventuell wäre ein SQL-Script zur Initialisierung der DB mit Dummy-Objekten schöner, aber aus meiner Sicht aufwändiger, um die nötigen Referenzen korrekt darzustellen.

### JSON Darstellung der Entities
An wenigen Stellen musste bei der JSON Repräsentation der Entities die back reference der children beachtet werden, da bidirektionale Beziehungen vorhanden sind. Zu diesem Zweck wurde die Jackson Annotation @JsonIgnore auf ein paar backreferences eingesetzt, damit es nicht zu Endlosrekursionen kommt.

# Dokumentation DEMO

## Erzeugen der Postgres DB und Dummy-Objekte
Falls das Standard Postgres Image nicht vorhanden ist:
>docker pull postgres

Der Container basiert auf dem Standard Postgres Docker Image (etwa 300MB groß) und wurde so erzeugt:
>docker run --name testdb -e POSTGRES_PASSWORD=testpw -e POSTGRES_USER=testuser -d -p 5432:5432 postgres

Anschließend Projekt starten und wie unten dokumentiert Dummy-Objekte erzeugen.

Alternativ lokal in Postgres Datenbank *testuser* mit User *testuser* und Passwort *testpw* erstellen. Tabellen werden beim Startup aus den Entities erzeugt. Die Datenbank sollte lokal sein.


## REST Endpoints


### Dummy-Objekte erzeugen

-  /enowademo/restapi/mockobjects
 - POST
 - Erzeugt Dummy-Objekte in der DB mit jeweiligen Referenzen zueinandner
 - nicht idempotent
 - Anfrage kann evtl. etwa eine Minute dauern, falls Docker auf Windows über WSL2 läuft
 
Beispielanfrage in cURL:
 
 > curl -X GET http://localhost:8080/enowademo/restapi/mockobjects

### Geolocation aller Bäume einer Straße anfragen

-  /enowademo/restapi/streets/{streetId}/geolocationsoftrees/
 - GET
 - Gibt alle geolocations der Straße mit der ID streetId in der DB als JSON zurück. Falls keine solche ID existiert, wird [] zurückgegeben. Die jeweiligen geolocations werden ohne backreference zu den jeweils asoziierten Bäumen zurückgegeben (, die aber in der DB eingetragen sind),
 
Beispielanfrage in cURL:
 
 > curl -X GET http://localhost:8080/enowademo/restapi/streets/1/trees/geolocations

Beispielantwort:

>[{"id":1,"longitude":0,"latitude":0},{"id":2,"longitude":1,"latitude":1},{"id":3,"longitude":2,"latitude":2},{"id":4,"longitude":3,"latitude":3},{"id":5,"longitude":4,"latitude":4},{"id":6,"longitude":5,"latitude":5},{"id":7,"longitude":6,"latitude":6},{"id":8,"longitude":7,"latitude":7},{"id":9,"longitude":8,"latitude":8},{"id":10,"longitude":9,"latitude":9},{"id":11,"longitude":10,"latitude":10},{"id":12,"longitude":11,"latitude":11},{"id":13,"longitude":12,"latitude":12},{"id":14,"longitude":13,"latitude":13}]

### Bäume einer Straße anfragen

-  /enowademo/restapi/streets/{streetId}/trees
 - GET
 - Gibt alle Bäume der Straße mit der ID streetId in der DB als JSON zurück. Falls keine solche ID existiert, wird [] zurückgegeben. Die jeweiligen geolocations der Bäume werden ohne backreference zu den jeweils asoziierten Bäumen zurückgegeben (, die aber in der DB eingetragen sind),
 
Beispielanfrage in cURL:
 
 > curl -X GET http://localhost:8080/enowademo/restapi/streets/1/trees
 
Beispielantwort:

>[{"id":1,"nummer":0,"pflanzdatum":"15/09/2020","strasse":{"id":1,"name":"Strasse0","verwaltungskuerzel":"Verwaltungskuerzel0","stadt":{"id":1,"plz":"Plz0","name":"Stadt0"}},"spezies":{"id":249,"botanischerName":"BotanischerName248","trivialName":"TrivialName248"},"geoLocation":{"id":1,"longitude":0,"latitude":0}},{"id":2,"nummer":1,"pflanzdatum":"15/09/2020","strasse":{"id":1,"name":"Strasse0","verwaltungskuerzel":"Verwaltungskuerzel0","stadt":{"id":1,"plz":"Plz0","name":"Stadt0"}},"spezies":{"id":218,"botanischerName":"BotanischerName217","trivialName":"TrivialName217"},"geoLocation":{"id":2,"longitude":1,"latitude":1}},{"id":3,"nummer":2,"pflanzdatum":"15/09/2020","strasse":{"id":1,"name":"Strasse0","verwaltungskuerzel":"Verwaltungskuerzel0","stadt":{"id":1,"plz":"Plz0","name":"Stadt0"}},"spezies":{"id":41,"botanischerName":"BotanischerName40","trivialName":"TrivialName40"},"geoLocation":{"id":3,"longitude":2,"latitude":2}},{"id":4,"nummer":3,"pflanzdatum":"15/09/2020","strasse":{"id":1,"name":"Strasse0","verwaltungskuerzel":"Verwaltungskuerzel0","stadt":{"id":1,"plz":"Plz0","name":"Stadt0"}},"spezies":{"id":37,"botanischerName":"BotanischerName36","trivialName":"TrivialName36"},"geoLocation":{"id":4,"longitude":3,"latitude":3}},{"id":5,"nummer":4,"pflanzdatum":"15/09/2020","strasse":{"id":1,"name":"Strasse0","verwaltungskuerzel":"Verwaltungskuerzel0","stadt":{"id":1,"plz":"Plz0","name":"Stadt0"}},"spezies":{"id":70,"botanischerName":"BotanischerName69","trivialName":"TrivialName69"},"geoLocation":{"id":5,"longitude":4,"latitude":4}},{"id":6,"nummer":5,"pflanzdatum":"15/09/2020","strasse":{"id":1,"name":"Strasse0","verwaltungskuerzel":"Verwaltungskuerzel0","stadt":{"id":1,"plz":"Plz0","name":"Stadt0"}},"spezies":{"id":167,"botanischerName":"BotanischerName166","trivialName":"TrivialName166"},"geoLocation":{"id":6,"longitude":5,"latitude":5}},{"id":7,"nummer":6,"pflanzdatum":"15/09/2020","strasse":{"id":1,"name":"Strasse0","verwaltungskuerzel":"Verwaltungskuerzel0","stadt":{"id":1,"plz":"Plz0","name":"Stadt0"}},"spezies":{"id":222,"botanischerName":"BotanischerName221","trivialName":"TrivialName221"},"geoLocation":{"id":7,"longitude":6,"latitude":6}},{"id":8,"nummer":7,"pflanzdatum":"15/09/2020","strasse":{"id":1,"name":"Strasse0","verwaltungskuerzel":"Verwaltungskuerzel0","stadt":{"id":1,"plz":"Plz0","name":"Stadt0"}},"spezies":{"id":158,"botanischerName":"BotanischerName157","trivialName":"TrivialName157"},"geoLocation":{"id":8,"longitude":7,"latitude":7}},{"id":9,"nummer":8,"pflanzdatum":"15/09/2020","strasse":{"id":1,"name":"Strasse0","verwaltungskuerzel":"Verwaltungskuerzel0","stadt":{"id":1,"plz":"Plz0","name":"Stadt0"}},"spezies":{"id":62,"botanischerName":"BotanischerName61","trivialName":"TrivialName61"},"geoLocation":{"id":9,"longitude":8,"latitude":8}},{"id":10,"nummer":9,"pflanzdatum":"15/09/2020","strasse":{"id":1,"name":"Strasse0","verwaltungskuerzel":"Verwaltungskuerzel0","stadt":{"id":1,"plz":"Plz0","name":"Stadt0"}},"spezies":{"id":124,"botanischerName":"BotanischerName123","trivialName":"TrivialName123"},"geoLocation":{"id":10,"longitude":9,"latitude":9}},{"id":11,"nummer":10,"pflanzdatum":"15/09/2020","strasse":{"id":1,"name":"Strasse0","verwaltungskuerzel":"Verwaltungskuerzel0","stadt":{"id":1,"plz":"Plz0","name":"Stadt0"}},"spezies":{"id":151,"botanischerName":"BotanischerName150","trivialName":"TrivialName150"},"geoLocation":{"id":11,"longitude":10,"latitude":10}},{"id":12,"nummer":11,"pflanzdatum":"15/09/2020","strasse":{"id":1,"name":"Strasse0","verwaltungskuerzel":"Verwaltungskuerzel0","stadt":{"id":1,"plz":"Plz0","name":"Stadt0"}},"spezies":{"id":133,"botanischerName":"BotanischerName132","trivialName":"TrivialName132"},"geoLocation":{"id":12,"longitude":11,"latitude":11}},{"id":13,"nummer":12,"pflanzdatum":"15/09/2020","strasse":{"id":1,"name":"Strasse0","verwaltungskuerzel":"Verwaltungskuerzel0","stadt":{"id":1,"plz":"Plz0","name":"Stadt0"}},"spezies":{"id":230,"botanischerName":"BotanischerName229","trivialName":"TrivialName229"},"geoLocation":{"id":13,"longitude":12,"latitude":12}},{"id":14,"nummer":13,"pflanzdatum":"15/09/2020","strasse":{"id":1,"name":"Strasse0","verwaltungskuerzel":"Verwaltungskuerzel0","stadt":{"id":1,"plz":"Plz0","name":"Stadt0"}},"spezies":{"id":172,"botanischerName":"BotanischerName171","trivialName":"TrivialName171"},"geoLocation":{"id":14,"longitude":13,"latitude":13}}]

### Alle Befunde eines Baumes anfragen

-  /enowademo/restapi/trees/{treeId}/
 - GET
 - Gibt alle Befunde eines Baums mit der ID treeId in der Datenbank als JSON zurück.
 - Falls ID nicht in der DB gefunden, wird [] zurückgegeben
 
Beispielanfrage in cURL:
 
 > curl -X GET http://localhost:8080/enowademo/restapi/trees/2/

Beispielantwort:

>[{"id":2,"erhobenAm":"15/09/2020","beschreibung":"Beschreibung0"},{"id":3,"erhobenAm":"15/09/2020","beschreibung":"Beschreibung1"},{"id":4,"erhobenAm":"15/09/2020","beschreibung":"Beschreibung2"}]


### Alter eines Baumes anfragen

- /enowademo/restapi/trees/{treeId}/age/
- GET
- Gibt das Alter eines Baumes mit der ID treeId als plain text zurück.
- Falls Baum in der Datenbank nicht existiert, wird -1 zurückgegeben

Beispielanfrage in cURL:
> curl -X GET http://localhost:8080/enowademo/restapi/trees/1/age/

Beispielantwort:
>0

### Spezies eines Baumes anfragen

- /enowademo/restapi/trees/{treeId}/species
- GET
- Gibt die Spezie eines Baums mit der ID treeId in der Datenbank als JSON zurück.
- Falls Baum in der Datenbank nicht existiert, wird eine leere Spezies zurückgegeben (emptystring).


Beispielanfrage in cURL:
>curl -X GET http://localhost:8080/enowademo/restapi/trees/1/species

Beispielantwort:
>{"id":249,"botanischerName":"BotanischerName248","trivialName":"TrivialName248"}

## TODOs / Verbesserungen

- Projekt ist aktuell nur MVP
- JUnit tests hinzufügen und damit evtl. zu Java 11 wechseln, da besserer HTTPClient zum Testen. Aus Zeitgründen und anderen Bewerbungsprojekten leider nicht dazu gekommen. Aktuell hauptsächlich über cURL getestet.
- Besseres Error-Handling. Aktuell wird nur NoResultException gefangen, ansonsten Standard JPA error handling
- Datenbankkonfiguration sanitieren. Aktuell user/pw in web.xml / persistence.xml
- Bessere Erzeugung der Dummy-Objekt
- Evtl. bessere Endpoints, z.B. Befunde sowohl über */streets/1/trees/1/befunde/* als auch */trees/1/befunde/* sichtbar machen



