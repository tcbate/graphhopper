# Rapport Tache 2 — IFT3913


# Pitest avant les test 
<img width="1455" height="541" alt="image" src="https://github.com/user-attachments/assets/8f164901-586d-4a5e-b4b2-cfb6a7fa2e1b" />


Pour chaque test : 
1. Nom du test
2. Comportement testé
3. Motivation des données des tests choisi
4. Explication de l'oracle 

### Tests sur `GHUtility.findCommonNode()`

Pour chaqu'un de ces tests on utilise un graph qui est divisé en trois
parites. (Ou un mockito à la place où la graph ne fonctionne pas pour
ce qu'on veut tester)

1. Nom : FindCommonNodeNormal

   Comportement testé : Trouver le noeud commun entre deux noeud
   connecté en forme d'étoille. 

   Motivation des données des tests choisi: Cette test utilise une partie
d'une graphe non-connexe en forme d'étoille. On test deux arrêts connecté
au noeud du centre pour vérifier si le fonctionne rétrouve le bon noeud

   Explication de l'oracle : On utilise un oracle direct qui est le noeud
commun entre les deux dans le graph déjà construit

2. Nom : FindCommonNodeDisconnected

   Comportement testé : Lancer un exception si les deux noeuds ne sont
pas connectés.

   Motivation des données des tests choisi : On utilise une partie non-connexe
du graphe pour provoquer l'exception

   Explication de l'oracle : Un oracle direct — InvalidArgumentException est lancé
quand il ne trouve pas de lien entre les deux noeuds

3. Nom : FindCommonNodeCycle

   Comportement Testé "Lancer un exception s'il y a un circle entre deux noeuds

   Motivation des données : Une partie du graph avec deux noeuds et deux arrêts
   entre les deux. Un qui point dand un direction et un qui point dans l'autre.
   Cela crée une cycle entre les deux noeuds.

   Explication de l'oracle : Un oracle direct - `InvalidArgumentException` est
   lancé quand il trouve le cycle entre les deux noeuds

4. Nom : FindCommonNodeChain
   
   Comportement testé : Trouvé le noeud commun dans une chaîne des noeuds/arrêts
   
   Motivation des données : Une sous-graphe avec trois noeuds et deux arrêts qui point
   tous dans la même sens. (On peut donc passer par les deux arrêts pour atteindre le 
   dernier noeud).

   Explication de l'oracle : Un oracle direct - Le noeud touché par les deux arrêts dans
   la chaîne

5. Nom : MockedEdgeLoop1

   Comportement testé : Lance une exception s'il y a une cycle entre deux noeuds

   Motivation des données : Les données sont mocké pour ce test. IL y a un garde
   lors de l'ajoute d'arrêt qui plant si on essaie de l'ajouter. On voulait tester
   qu'est-ce qui arrivera si jamais ce genre de données rentre dans le graph. 

   Explication de l'oracle : Un oracle direct — `InvalidArgumentException` si le
   noeud sur les deux côtés sont le même noeud

6. Nom : MockedEdgeLoop2
   
   Comportement testé : Idem pour le `MockedEdgeLoop1` mais on a simulé l'autre côté 
   de l'arrêt par rapport à `MockedEdgeLoop1`

   Explication de l'oracle : Un oracle direct – `InvalidArgumentException` si le
   noeud sur les deux côtés sont le même noeud. 

### DirectionResolverResult.java
pretty()

### InstructionOutgoingEdges.java
mergedOrSplitWay()


7.Nom: testComparePathsWithDifferentWeightsShouldFail
    Intention : Vérifier que la méthode lève une AssertionError lorsque les poids de deux chemins diffèrent au-delà de la tolérance permise.
    Motivation des données : Nous avons choisi des poids de 100.0 et 101.1. La différence (1.1) est suffisamment grande pour ne laisser aucune ambiguïté et doit absolument       déclencher l'erreur. Les autres propriétés des chemins sont non pertinentes car cette vérification est prioritaire.
    Oracle : Le comportement attendu, spécifié par une logique de comparaison stricte, est une exception. Le test réussit si un AssertionError.class est bien levé, ce que        nous validons avec assertThrows.
    
8.Nom : testComparePathsWithDifferentDistanceShouldReturnViolation
    Intention : Valider que la méthode détecte une différence de distance, même si les poids et les séquences de nœuds sont identiques.
    Motivation des données : Les poids et les nœuds sont identiques, mais les distances (50.0 vs 50.2) diffèrent d'une valeur supérieure à la tolérance habituelle. Ce test     cible la précision des comparaisons de nombres à virgule flottante.
    Oracle : Similaire au test précédent, l'oracle attend une liste de violations non vide contenant le message spécifique "wrong distance".

9.Nom : testComparePathsWithEquivalentDetourShouldReturnNoViolations
    Intention : Vérifier que la méthode gère correctement le cas d'un détour "attendu". Deux chemins doivent être considérés comme équivalents si leur seule différence est     le passage par un nœud intermédiaire spécifié dans le paramètre via.
    Motivation des données : Nous avons créé un chemin direct A->C (nœuds [0,2]) et un chemin alternatif A->B->C (nœuds [0,1,2]). Toutes les autres valeurs (poids,             distance) sont identiques pour isoler la logique de comparaison des nœuds. La clé du test est de passer le nœud 1 comme paramètre via, signalant au système que ce détour     est valide.
    Oracle : L'oracle est basé sur la spécification de cette fonctionnalité avancée. Quand un détour valide est fourni, la méthode doit retourner une liste de violations        vide. Le test réussit donc si l'assertion assertTrue(violations.isEmpty()) est validée.
10. Nom : testComparePathsWithFakerGeneratedWeights
    Intention : Valider que la méthode comparePaths ne signale aucune violation lorsque les poids de deux chemins sont très similaires (c'est-à-dire que leur différence est     inférieure à la tolérance de 1.e-2), en utilisant des données variées générées par Faker.
    Motivation des données : Plutôt que d'utiliser des valeurs fixes, nous utilisons Faker pour générer un poids de base réaliste et une très petite différence (garantie         d'être inférieure à la tolérance). Cela permet de vérifier que la logique de tolérance fonctionne correctement pour une large plage de valeurs et pas seulement pour         des nombres simples. La nature aléatoire (mais contrôlée) des données rend le test plus robuste.
    Oracle : L'oracle stipule que, puisque la différence de poids est volontairement créée pour être dans la marge de tolérance de la méthode, aucun AssertionError ne doit        être levé, et comme les autres propriétés sont identiques, la liste des violations doit être vide. Le test réussit si assertTrue(violations.isEmpty()) est vrai.




