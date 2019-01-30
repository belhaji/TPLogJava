# Home
## Nom du projet 
 TP1Log
 
## Description 
 Cette application a pour but d'établir des indicateurs de participation en ligne des apprenants inscrits à un cours à partir d'un fichier de logs "data.csv" d'une plateforme web d'apprentissage. Les indicateurs sont les suivants :
 
 - Le nombre d'actions par apprenant sur une période donnée
 - Le nombre moyen d'action par apprenant sur une période donnée
 - La durée (approximative) d'utilisation de la plate-forme par apprenant sur une période
 - La durée moyenne d'utilisation par apprenant sur une période
 L'application  est réalisée sous la forme d'une interface graphique interactive conçue avec Java FX.

## Auteur
 Belhaji Adil
 
# Install
## A partir de bindist 
On récupère le dossier bindist, et on lance l'application à partir du fichier exécutable nommé"app".

## A partir de src dist 
 On ouvre "run configurations" à partir du projet, puis on lance l'application avec comme argument "maven package".
 
# Manuel d'utilisation

Une fois que l'on a lancé l'application, une interface scene builder apparaît. Il faut d'abord choisir un fichier avec l'onglet "file", puis sélectionner le fichier "data.csv" (correspondant au fichier de logs) situé dans le dossier "target". Ensuite, il faut saisir une date de début de période "Start date" et une date de fin de période "End Date". Nous obtenons ainsi 4 graphiques : 

 - Le 1er graphique représente le nombre d'actions par apprenant sur la période donnée ;
 - Le 2ème graphique représente le nombre moyen d'actions par apprenant sur la période donnée ; 
 - Le 3ème graphique représente la durée d'utilisation de la plateforme par apprenant sur la période donnée ; 
 - Le 4ème graphique représente la durée moyenne d'utilisation par apprenant sur la période donnée.
 
 Exemple :
 On entre comme période : du 19/02/2017 au 28/02/2017
 On obtient les résultats suivants :
 
 ![graphique1](img/graphique1.png)
 ![graphique2](img/graphique2.png)
 ![graphique3](img/graphique3.png)
 ![graphique4](img/graphique4.png)
# Manuel technique

## Fichier de logs
Le fichier de logs est au format csv. Chaque ligne correspond à une action sur la plate-forme. 
Les champs se présentent comme suit :
 - Le 1er correspond à l'Id : identificateur de l'apprenant
 - Le 2ème correspond à la date : timestamp de l'action au format : dd MMM yy, HH:mm
 

## Calculs des indicateurs
Chaque graphique représente les résultats pour chacun des apprenants. On va donc lire le fichier et regrouper les valeurs par utilisateur et par session.
Le premier indicateur est calculé en additionnant les actions réalisées par l'apprenant sur la période donnée.
Le deuxième indicateur est calculé en divisant le nombre total d'actions réalisées par l'apprenant sur le nombre total de sessions de cet apprenant sur la période donnée.
Le troisième indicateur est calculé en calculant le nombre de jours écoulés entre la 1ère et la dernière action de l'utilisateur sur la période donnée.
Le quatrième indicateur est calculé en divisant ce nombre par le nombre total de sessions de cet apprenant sur la période donnée.

# Classes
 
 * *LogEntry* : représente une ligne du fichier de logs 
 	- *id* : id de l'utilisateur dans le fichier de logs
 	- *timestamp*: la date dans le fichier de logs
 * *Action* : représente une action de l'utilisateur
 	- *actionDate*: la date de l'action
 * *Session* : représente une session de l'utilisateur (par défault une durée de 30 mins)
 	- *id*: l'id de la session (nombre calculé à partir des actions)
 	- *actions*: la liste des actions dans la session
 	- *startDate*: la date de début de la session
 	- *endDate*: la date de fin de la session
 * *User* : 
 	- *id*: l'id de l'utilisateur
 	- *sessions*: la list des sessions 

 * *MainUIController* : représente le controlleur de l'interface graphique. Le fichier main est le point d'entrée de l'application.

# Licence
Cette application appartient à la licence MIT.

# Dépendances
Java 1.8
