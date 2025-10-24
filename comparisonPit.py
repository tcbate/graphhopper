import csv
import os
import sys

def get_pit_score(test_report_liste):
    #Initialization des types résultats
    killed = 0
    survived = 0
    timed_out = 0
    non_viable = 0
    memory_error = 0
    not_started = 0
    started = 0
    run_error = 0
    no_coverage = 0
    autre = 0
    #Parcourir le rapport pour compter les types
    for test in test_report_liste:
        result = test[-2]
        match result:
            case "KILLED":
                killed += 1
            case "SURVIVED":
                survived += 1
            case "TIMED_OUT":
                timed_out += 1
            case "NON_VIABLE":
                non_viable += 1
            case "MEMORY_ERROR":
                memory_error += 1
            case "NOT_STARTED":
                not_started += 1
            case "STARTED":
                started += 1
            case "RUN_ERROR":
                run_error += 1
            case "NO_COVERAGE":
                no_coverage += 1
            case _:
                autre += 1

    total_killed = killed + timed_out + memory_error

    total = (killed + survived + timed_out + non_viable +
    memory_error +  not_started + started + run_error +
                 no_coverage + autre)

    return total_killed/total


if __name__ == "__main__":
    #On écrit du code ici
    score1 = -1
    score2 = -1
    with open("core/target/pit-reports/mutations.csv",newline="") as report:
        report_parsed = csv.reader(report)
        score1 = get_pit_score(report_parsed)
    print("Score 1 : {:.2f}".format(score1))
    resultat = os.system("mvn -pl core test-compile org.pitest:pitest-maven:mutationCoverage")

    with open("core/target/pit-reports/mutations.csv",newline="") as report2:
        report2_parsed = csv.reader(report2)
        score2 = get_pit_score(report2_parsed)
    print("Score 2 : {:.2f}".format(score2))
    if score1 < score2:
        print("Le score de mutataion à baissé... Il faut échouer")
        sys.exit(1)
    else:
        sys.exit(0)


