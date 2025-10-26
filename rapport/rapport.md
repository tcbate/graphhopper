# Rapport pour la tâche 3

## Github action qui n’accepte pas une baisse du score PiTest

Principalement, cette action est gérée par un script python qui prend l’ancien rapport
pitest, calcul le score pitest à partir ce rapport, lance pitest, fait le calcul
à nouveau, et enfin, compare les deux scores. Si le score baisse, il retourne 1 pour
provoquer une erreur dans l’action Github. Sinon, il retourne 0.

Mais pendant les tests de ce script, on a remarqué que, des fois, quand on enlève un test,
le score pitest a élevé, puis en faisant rien que de lancer pitest encore une fois, le
score revient au niveau précédent. Donc, si le score est à 53 %, puis on enlève un test
et relance pitest, le score s’élève à 55 %. Si pitest est relancé encore une fois,
sans aucun changement dans le code, le score revient à 53 %. On croit que ce changement
est causé par l’utilisation d’historique. Évidemment, l’historique ne traite que les classes.
Donc, si on modifie un test, l’historique va empêcher la reprise des tests modifiés, et,
par la suite, le recalcul du score pitest. 


La solution implémentée pour le résoudre est de chercher à savoir quels fichiers sont
modifiés. On compte le nombre des fichiers avec
`git diff HEAD~1 HEAD --name-only | grep -c Test`. S’il y a un changement dans les tests,
l’historique ne sera pas téléchargé. Cela est pour assurer que le résultat pitest représente
la réalité du code. Une fois que les téléchargements sont terminés, le script Python est
chargé. Ce script lit et refait le calcul de l’ancien score de pitest. Par la suite,
Pitest est lancé, et le score est calculé encore une fois par le script. Si le niveau est
plus bas, le script retourne 1 et on met un flag pour indiquer l’échec. On continue pour
enregistrer l’historique et le rapport dans les artefacts. Finalement, on cherche encore
une fois le flag d’échec. Si cela a échoué, on exécute `exit 1` pour échouer l’action. 


