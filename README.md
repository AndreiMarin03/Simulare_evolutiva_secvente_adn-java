🧬 ADNSeq – Genetic Algorithm DNA Simulator
📌 Overview

ADNSeq este o aplicație Java care simulează evoluția unei populații de secvențe ADN către o secvență țintă, folosind un algoritm genetic.

⚙️ Features
generare populație aleatoare
fitness bazat pe similaritate cu ținta
selecție, crossover și mutație
criterii de oprire (soluție, stagnare, limită generații)
interfață grafică (JavaFX)
grafic evoluție (best vs average fitness)
highlight vizual (corect vs greșit)
🧠 How it works

Algoritmul:

generează populație random
calculează fitness
selectează părinți
aplică crossover + mutație
repetă până la convergență
🏗️ Structure
GeneticEngine   # logică algoritm
GeneticService  # API
MainController  # UI logic
MainApp         # entry point
🚀 Tech

Java • JavaFX • Genetic Algorithms • OOP

👤 Author

Marin Laurențiu-Andrei
