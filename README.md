🧬 ADNSeq – Genetic Algorithm DNA Evolution Simulator
📌 Overview

ADNSeq este o aplicație Java care simulează evoluția unei populații de secvențe ADN către o secvență țintă, folosind un algoritm genetic.

Proiectul combină concepte din:

algoritmi și structuri de date
inteligență artificială (algoritmi evolutivi)
bioinformatică (modelare ADN)
dezvoltare GUI (JavaFX)
⚙️ Features
🔹 Generare populație inițială aleatoare
🔹 Calcul fitness (similaritate cu secvența țintă)
🔹 Selecție, crossover și mutație
🔹 Criterii de oprire inteligente:
soluție perfectă
stagnare
număr maxim de generații
🔹 Vizualizare grafică a evoluției (best vs average fitness)
🔹 Interfață grafică interactivă (JavaFX)
🔹 Evidențiere vizuală a caracterelor corecte/greșite
🧠 How It Works
🧬 Concept biologic
ADN = șir de caractere {A, C, G, T}
Populație = set de secvențe
Evoluție = mutații + selecție
💻 Algoritm genetic
Se generează o populație random
Se calculează fitness-ul fiecărui individ
Se selectează părinții (cei mai buni)
Se creează o nouă generație:
crossover
mutație
Se repetă până la convergență
📊 Fitness Function

Fitness-ul este calculat ca:

procentul de caractere identice cu secvența țintă (pe aceeași poziție)

Valori:

1.0 → soluție perfectă
0.0 → complet diferit
🖥️ UI Features
Input pentru:
secvență țintă
dimensiune populație
rată mutație
generații maxime
Output:
cea mai bună secvență
fitness final
număr generații
📈 Grafic:
Best fitness
Average fitness
🎨 Highlight:
verde = corect
roșu = greșit
