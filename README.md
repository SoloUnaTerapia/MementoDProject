# 📌 Memento - Cognitive RPG

**Memento** è un gioco di ruolo (RPG) a turni procedurale sviluppato in Java.
Il progetto implementa un sistema di combattimento basato su affinità elementali, gestione delle risorse (Sanity e Focus) e doppia persistenza dei dati (JSON e Database Relazionale tramite Hibernate), ponendo il focus sulla corretta applicazione del pattern MVC e dei principi SOLID.

---

## 🚀 Come eseguire il progetto

### Prerequisiti
- Java 25 o più recente
- Gradle 9.1.0 o più recente
- Git

### Istruzioni per il Download

```bash
git clone https://github.com/SoloUnaTerapia/MementoDProject.git
cd MementoDProject
```

### Build del progetto
```bash
./gradlew build
```

### Esecuzione
```bash
./gradlew run
```

---

## 🤖 Uso di strumenti di AI

Durante lo sviluppo di questo progetto, strumenti di intelligenza artificiale sono stati utilizzati esclusivamente come supporto tecnico e prettamente estetico, mantenendo la totale paternità manuale sull'architettura e sulla logica del software.

Nello specifico, l'AI è stata utilizzata per:

 *   Generazione totale del codice CSS: Il foglio di stile visivo (style.css), inclusi i colori e gli effetti dei bottoni di JavaFX, è stato generato tramite IA partendo da direttive cromatiche testuali.

 *   Troubleshooting su librerie esterne: Supporto per la risoluzione di un errore di compatibilità tra Gradle Configuration Cache e JavaFX, e supporto alla sintassi corretta per configurare il file persistence.xml (Hibernate con dialetto SQLite)

 *  Boilerplate e Autocompletamento: Generazione meccanica di codice ripetitivo come firme dei metodi, costruttori e conversione delle classi DTO in record.

La progettazione del Dominio,la suddivisione nei layer dell'architettura MVC e la logica dei Service sono stati ideati, scritti e testati in totale autonomia.



📌 Per una documentazione tecnica dettagliata su Architettura, Responsabilità delle Classi, Pattern SOLID e Persistenza, fare riferimento alla Wiki del repository.



