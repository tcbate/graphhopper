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
                non-viable += 1
            #Etc. 

    pass


if __name__ == "__main__":
    #On écrit du code ici
    with open("core/target/pit-reports/pit-report.csv",newline="") as report:
        report_parsed = csv.reader(report)
        score1 = get_pit_score(test_report_list)

