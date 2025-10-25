# Rapport pour la tâche 3

## Github action qui n'accept pas une baisse du score PiTest

Principalement, cette action est géré par un script python qui prend l'ancien rapport
pitest, calcul le score pitest à partir ce ce rapport, lance pitest, fait le calcul 
à nouveau, et enfin, compare les deux scores. Si le score baisse, il retourne 1 pour
provoquer un erreur dans l'action Github. Sinon, il retourne 0.

Mais pendant le tests de ce script, on a remarqué que des fois, quand on enlève un test
le score pitest a élevé, puis en faisant rien que de lancer pitest encore une fois, le
score revient au niveau précédent. Donc, si le score est à 53%, puis on enleve un test
et relance pitest, le score s'eléve à 55%. Si pitest est relancé encore une fois,
sans aucune changement dans le code, le score reviens à 53%. On croit que ce changement
est causé par l'utilisation d'historique. La solution implementé pour le resourdre est
de chercher à savoir quels fichiers sont modifiés. On compte le nombre des fichiers
avec `git diff --name-only | grep -c Test`.  
