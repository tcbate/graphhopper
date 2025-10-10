# Rapport Tache 2 — IFT3913


# Pitest avant les tests
<img width="1455" height="541" alt="image" src="https://github.com/user-attachments/assets/8f164901-586d-4a5e-b4b2-cfb6a7fa2e1b" />

# Pitest après les tests
<img width="1792" height="562" alt="image" src="https://github.com/user-attachments/assets/cbbc57c4-b486-4159-b5ad-4aa09b787078" />


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

   Mutants tués : getCommonNode : negatedConditional -> MemoryError

2. Nom : FindCommonNodeDisconnected

   Comportement testé : Lancer un exception si les deux noeuds ne sont
pas connectés.

   Motivation des données des tests choisi : On utilise une partie non-connexe
du graphe pour provoquer l'exception

   Explication de l'oracle : Un oracle direct — InvalidArgumentException est lancé
quand il ne trouve pas de lien entre les deux noeuds

   Mutants tués : Aucune

3. Nom : FindCommonNodeCycle

   Comportement Testé "Lancer un exception s'il y a un circle entre deux noeuds

   Motivation des données : Une partie du graph avec deux noeuds et deux arrêts
   entre les deux. Un qui point dand un direction et un qui point dans l'autre.
   Cela crée une cycle entre les deux noeuds.

   Explication de l'oracle : Un oracle direct - `InvalidArgumentException` est
   lancé quand il trouve le cycle entre les deux noeuds

   Mutents tués : Negated Conditional -> Memory Error (4x)
4. Nom : FindCommonNodeChain
   
   Comportement testé : Trouvé le noeud commun dans une chaîne des noeuds/arrêts
   
   Motivation des données : Une sous-graphe avec trois noeuds et deux arrêts qui point
   tous dans la même sens. (On peut donc passer par les deux arrêts pour atteindre le 
   dernier noeud).

   Explication de l'oracle : Un oracle direct - Le noeud touché par les deux arrêts dans
   la chaîne 

   Mutants tués : Negated conditional -> Memory Error (2x)

5. Nom : MockedEdgeLoop1

   Comportement testé : Lance une exception s'il y a une cycle entre deux noeuds

   Motivation des données : Les données sont mocké pour ce test. IL y a un garde
   lors de l'ajoute d'arrêt qui plant si on essaie de l'ajouter. On voulait tester
   qu'est-ce qui arrivera si jamais ce genre de données rentre dans le graph. 

   Explication de l'oracle : Un oracle direct — `InvalidArgumentException` si le
   noeud sur les deux côtés sont le même noeud

   Mutents tués : Negated Conditional -> Memory Error

6. Nom : MockedEdgeLoop2
   
   Comportement testé : Idem pour le `MockedEdgeLoop1` mais on a simulé l'autre côté 
   de l'arrêt par rapport à `MockedEdgeLoop1`

   Explication de l'oracle : Un oracle direct – `InvalidArgumentException` si le
   noeud sur les deux côtés sont le même noeud. 

   Mutants tués : Negated Conditional -> Memory Error

### DirectionResolverResult.java
pretty()

### InstructionOutgoingEdges.java
mergedOrSplitWay()


7. Test : testComparePathsWithDifferentWeightsShouldFail

   Intention : Vérifier qu'une AssertionError est levée si les poids des chemins sont trop différents.
   
    Données : Un chemin avec un poids de 100.0 et un autre de 101.1. La différence est volontairement grande pour garantir l'échec.
   
    Oracle : Le test s'attend à ce qu'une exception de type AssertionError soit lancée.
   
8. Test : testComparePathsWithDifferentDistanceShouldReturnViolation
   
    Intention : S'assurer qu'une différence de distance est signalée, même si les poids sont identiques.
   
    Données : Deux chemins identiques en tout point, sauf la distance (50.0 vs 50.2).
   
    Oracle : Le test vérifie que la méthode retourne une liste d'erreurs contenant le message "wrong distance".
   
9. Test : testComparePathsWithEquivalentDetourShouldReturnNoViolations
 
    Intention : Valider que la méthode considère deux chemins comme identiques si leur seule différence est un détour attendu (via).
    
    Données : Un chemin direct A->C et un chemin A->B->C. Le nœud B est spécifié comme un détour valide.
    
    Oracle : Le test s'attend à ce que la liste des erreurs retournée soit vide, confirmant que le détour a été accepté.
    
10. Test : testComparePathsWithFakerGeneratedWeights

     Intention : Confirmer que des poids très similaires (différence inférieure à la tolérance) ne causent pas d'erreur.
    
    Données : Faker génère un poids de base et une différence minime, garantie d'être dans la marge de tolérance de la méthode.
    
    Oracle : Le test vérifie que la liste des erreurs est vide, prouvant que la tolérance de la méthode fonctionne comme prévu.
