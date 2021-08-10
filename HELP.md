Prérequis : Ubuntu, Java 7, Eclipse


I - leJOS

Télécharger leJOS (http://sourceforge.net/projects/ev3.lejos.p/files/0.9.0-beta/leJOS_EV3_0.9.0-beta.tar.gz/download) puis extraire l'archive à l'endroit désiré (de préférence dans le répertoire /opt).


II - Plugin Eclipse

Dans Eclipse, aller dans "Help" puis "Install New Software..." puis "Add...". Indiquer "LeJOS EV3" dans "Name" et "http://lejos.sourceforge.net/tools/eclipse/plugin/ev3" dans "Location". Valider en appuyant sur "OK" puis cocher "leJOS EV3 Support". Cliquer deux fois de suite sur "Next", accepter les conditions puis cliquer sur "Finish". Un message d'erreur va apparaître, cliquer sur "OK". Redémarrer en cliquant sur "Yes". Aller dans "Window" puis "Preferences" puis "leJOS EV3". Remplir le champs "EV3_HOME" avec "/opt/leJOS_EV3_0.9.0-beta" (ou bien le chemin choisi à la partie I). Cocher "Run Tools in separate JVM" et "Run program after upload". Valider en cliquant sur "Apply" puis sur "OK".


III - Git (1)

Installer Git :
$ sudo apt-get install git
Puis le configurer :
$ git config --global user.email johndoe@example.com
$ git config --global user.name "John Doe"


IV - GitHub

Générer une clé SSH sur chaque PC sur lequel on souhaite travailler :
$ ssh-keygen -t rsa
Appuyer sur la touche Entrée pour valider les paramètres par défaut. Saisir une passphrase qui sera demandée à chaque utilisation de la clé SSH. Copier l'intégralité du fichier ~/.ssh/id_rsa.pub (attention aux sauts de lignes). Se rendre sur GitHub (https://github.com/login/) et se connecter. Aller dans "Settings" puis "SSH and GPG keys" et cliquer sur "New SSH key". Afin, coller la clé dans le champs "Key" et donner un nom dans le champs "Title" (nom du PC par exemple).


V - Git (2)

Se rendre à l'endroit où l'on souhaite récupérer le projet puis le cloner :
$ git clone git@github.com:florianlallier/mindstormtrooper.git


VI - Eclipse

Dans Eclipse, aller dans "File", "New" puis "Project...". Dans "LeJOS EV3", sélectionner "LEJOS EV3 Project" et appuyer sur "Next". Décocher la case "Use default location" et insérer le chemin vers le répertoire du projet dans "Location". Terminer en cliquant sur "Finish".