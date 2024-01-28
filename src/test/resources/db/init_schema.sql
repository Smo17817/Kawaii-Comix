

DROP TABLE IF EXISTS site_user;
CREATE TABLE site_user (
  `id` int PRIMARY KEY NOT NULL AUTO_INCREMENT,
  `email_address` varchar(70) NOT NULL,
  `password` varchar(100) NOT NULL,
  `nome` varchar(45) NOT NULL,
  `cognome` varchar(45) NOT NULL,
  `indirizzo` varchar(70) NOT NULL,
  `citta` varchar(70) NOT NULL,
  `codice_postale` char(5) NOT NULL,
  `provincia` char(2) NOT NULL,
  `nazione` varchar(45) NOT NULL
);

DROP TABLE IF EXISTS `categoria`;
CREATE TABLE `categoria` (
  `nome` varchar(30) PRIMARY KEY NOT NULL
);

DROP TABLE IF EXISTS `genere`;
CREATE TABLE `genere` (
  `nome` varchar(20) PRIMARY KEY NOT NULL
);

DROP TABLE IF EXISTS `prodotti`;
CREATE TABLE `prodotti` (
  `isbn` char(17) PRIMARY KEY NOT NULL,
  `nome` varchar(100) DEFAULT NULL,
  `autore` varchar(100) DEFAULT NULL,
  `descrizione` varchar(400) DEFAULT NULL,
  `immagine_prod` varchar(150) DEFAULT NULL,
  `prezzo` double DEFAULT NULL,
  `quantita` int DEFAULT NULL,
  `genere_nome` varchar(20) NOT NULL,
  `categoria_nome` varchar(30) NOT NULL,
  `copie_vendute` int NOT NULL--,
  --FOREIGN KEY (`categoria_nome`) REFERENCES `categoria` (`nome`),
  --FOREIGN KEY (`genere_nome`) REFERENCES `genere` (`nome`)
);

DROP TABLE IF EXISTS `carrello`;
CREATE TABLE `carrello` (
  `id` int PRIMARY KEY NOT NULL AUTO_INCREMENT,
  `user_id` int NOT NULL--,
  --FOREIGN KEY (`user_id`) REFERENCES `site_user` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
);

DROP TABLE IF EXISTS `carrello_prodotto`;
CREATE TABLE `carrello_prodotto` (
  `carrello_id` int NOT NULL,
  `prodotto_isbn` char(17) NOT NULL,
  PRIMARY KEY (`carrello_id`,`prodotto_isbn`)--,
  --FOREIGN KEY (`carrello_id`) REFERENCES `carrello` (`id`),
  --FOREIGN KEY (`prodotto_isbn`) REFERENCES `prodotti` (`isbn`)
);




DROP TABLE IF EXISTS `gestore_catalogo`;
CREATE TABLE `gestore_catalogo` (
  `email_address` varchar(70) PRIMARY KEY NOT NULL,
  `nome` varchar(45) NOT NULL,
  `cognome` varchar(45) NOT NULL,
  `password` varchar(45) NOT NULL
);

DROP TABLE IF EXISTS `gestore_ordini`;
CREATE TABLE `gestore_ordini` (
  `email_address` varchar(70) PRIMARY KEY NOT NULL,
  `nome` varchar(45) NOT NULL,
  `cognome` varchar(45) NOT NULL,
  `password` varchar(50) NOT NULL 
);

DROP TABLE IF EXISTS `metodo_spedizione`;
CREATE TABLE `metodo_spedizione` (
  `id` int PRIMARY KEY NOT NULL AUTO_INCREMENT,
  `nome` varchar(100) NOT NULL,
  `prezzo` double NOT NULL
);

DROP TABLE IF EXISTS `stato_ordine`;
CREATE TABLE `stato_ordine` (
  `id` int PRIMARY KEY NOT NULL AUTO_INCREMENT,
  `stato` varchar(50) DEFAULT NULL
);

DROP TABLE IF EXISTS `ordini`;
CREATE TABLE `ordini` (
  `id` int PRIMARY KEY NOT NULL AUTO_INCREMENT,
  `data` date NOT NULL,
  `totale` double NOT NULL,
  `site_user_id` int NOT NULL,
  `stato_ordine_id` int NOT NULL,
  `metodo_spedizione_id` int NOT NULL--,
  --FOREIGN KEY (`metodo_spedizione_id`) REFERENCES `metodo_spedizione` (`id`),
  --FOREIGN KEY (`site_user_id`) REFERENCES `site_user` (`id`),
  --FOREIGN KEY (`stato_ordine_id`) REFERENCES `stato_ordine` (`id`)
);



