ADNSeq – Genetic Algorithm DNA Evolution Simulator
📌 Overview

ADNSeq este o aplicație Java care simulează evoluția unei populații de secvențe ADN către o secvență țintă, folosind un algoritm genetic. Proiectul combină concepte din algoritmi, inteligență artificială și bioinformatică, oferind și o interfață grafică pentru vizualizarea procesului.

⚙️ Features
generare populație inițială aleatoare
calcul fitness (similaritate cu secvența țintă)
selecție, crossover și mutație
criterii de oprire (soluție perfectă, stagnare, limită generații)
interfață grafică interactivă (JavaFX)
grafic evoluție (best vs average fitness)
evidențiere vizuală a caracterelor corecte/greșite
🧠 How it works
se generează o populație random de secvențe ADN
se calculează fitness-ul fiecărui individ
sunt selectați cei mai buni pentru reproducere
se aplică crossover și mutație
procesul se repetă până la convergență
📊 Fitness

Fitness-ul reprezintă procentul de caractere identice cu secvența țintă (pe aceeași poziție), cu valori între 0 și 1.

🏗️ Structure
GeneticEngine   # motorul algoritmului
GeneticService  # API (separă logica de UI)
MainController  # logică interfață
MainApp         # entry point JavaFX
🚀 Technologies

Java • JavaFX • OOP • Genetic Algorithms

🎯 Purpose

Proiect educațional care demonstrează cum pot fi modelate concepte inspirate din biologie (evoluție, mutație, selecție) în informatică, folosind algoritmi euristici.

👤 Author

Marin Laurențiu-Andrei
