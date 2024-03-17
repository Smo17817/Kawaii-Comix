<p align="center">
  <img src="https://github.com/Smo17817/Kawaii-Comix/blob/main/src/main/webapp/icons/kclogo.png?raw=true" alt="Testo alternativo" style="display:block; margin:auto;" />
</p>

# 🌸 Kawaii-Comix 🌸
→ e-commerce per appassionati di manga  

Collaboratori: [Simone DAssisi](//github.com/Smo17817), [Davide Del Franco Natale](https://github.com/ddfn03) & [Giovanni Sicilia](https://github.com/giogiosici)  

## Struttura Progetto  
📁 Deliverables: Questa directory contiene la documentazione completa per il progetto;    
📁 Kawaii-Bot🌸: Questa directory contiene il modulo di IA che si occupa dei suggerimenti;  
📁 Semilavorati: Questa directory contiene le componenti dei documenti finali;  
📁	db: Questa directory contiene lo schema del database relazionale;  
📁 src: Questa directory contiene il codice sorgente del progetto;  
📄 pom.xml: file che si occupa della gestione delle dipendenze.

## Tavola dei Contenuti 📋
* [Informazioni Generali](#informazioni-generali-ℹ)
  + [Cos'è?](#cosè-)
  + [Obiettivi](#obiettivi-)
* [Tecnologie Utilizzate](#Tecnologie-Utilizzate-)
- [Getting Started](#getting-started-)
  - [Prerequisiti](#prerequisiti)
  - [Setting](#setting)
* [Project Status](#project-status-)
## Informazioni Generali ℹ
### Cos'è ❓
_Kawaii-Comix_ è un e-commerce progettato per migliorare l’esperienza di shopping degli appassionati di manga.  
### Obiettivi 🎯
Lo scopo del progetto consiste nell'applicare un modello B2C in cui è possibile arricchire la propria collezione di manga a proprio piacimento con le serie che più si desidera semplicemente con un click e comodamente da casa, senza bisogno di doversi recare fisicamente in libreria o in fumetteria.
## Tecnologie Utilizzate 📊
+ Java
+ HTML
+ CSS
+ JavaScript (JS)
+ MySQL
+ Python

## Getting Started ▶️
### Prerequisiti❗
Per poter avviare il progetto Kawaii-Comix sulla vostra macchina è richiesta l'installazione di un Web Server a vostro piacimento (noi abbiamo utilizzato Apache Tomcat 9.0) e di MySQL per poter utilizzare il nostro Database. Per eseguire il progetto è necessario scaricare sulla vostra macchina (se non già presente) un IDE; in particolare consigliamo IntelliJ IDEA. Infine scaricate lo zip della repository e decomprimetelo.

### Setting⚙️
Dopo aver scaricato tutti i prerequisiti si può passare alla fase di setting: 
1. Accendete MySQL altrimenti non vedrete i prodotti. Per l'installazione basta andare sul [sito ufficiale](https://dev.mysql.com/doc/mysql-installation-excerpt/5.7/en/);
2. Eseguite IntelliJ IDEA e aprite il progetto Kawaii-Comix;
3. Settate come Run Configuration Apache Tomcat 9.0 (come da immagini nella sottocartella Istruzioni);
4. Modificare il campo username e password in 📁 src → 📁 webapp → 📁 META-INF → 📄context.xml con quelli scelti in fase di registrazione su MySQL;
5. Modificare il campo username e password in 📁 src → 📁 java → 📁 view → 📁 site → 📄DbManager.java con quelli scelti in fase di registrazione su MySQL
6. Runnare la configurazione: vi si aprirà automaticamente la schermata del browser che avete come predefinito con la Homepage del sito.

Una volta che è stato tutto configurato il progetto dovrebbe andare tutto senza problemi =)
   
## Project Status 👍
Il Progetto è: _Completo_ ✅
