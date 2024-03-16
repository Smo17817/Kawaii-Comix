import random

import joblib
import re
import pymysql
from sqlalchemy import create_engine
import  pandas as pd
from flask import Flask, request, jsonify
from sklearn.feature_extraction.text import CountVectorizer
from flask_cors import CORS


def clean_text2(text):
    # Converti il testo in minuscolo
    text = text.lower()

    # Rimuovi i numeri
    text = re.sub(r'\d+', '', text)

    # Rimuovi i caratteri speciali e simboli (mantieni la virgola e rimuovi il singolo apice)
    text = re.sub(r"[^a-zA-Z\s,]", '', text)

    # Rimuovi gli spazi extra
    text = ' '.join(text.split())

    return text


def rimuovi_spazi(match):
    return match.group(0).replace(" ", "")


# Funzione per pulire il testo
def clean_text(text):
    # Rimuove gli spazi all'interno di ciascun valore
    text = text.replace(" ", "")
    # Implementa altre operazioni di pulizia del testo come preferisci
    return text.strip()

app = Flask(__name__)
CORS(app)

# Carica il modello di KMeans
model = joblib.load('kmeans_best_model.sav')

# Definisci il vettore CountVectorizer
vectorizer = CountVectorizer()

@app.route('/')
def index():
    return 'Benvenuto sulla home page del tuo e-commerce'

from flask import jsonify

@app.route('/recommendations', methods=['POST'])
def get_recommendations():
    # Ricevi il prodotto dal frontend
    product_tags = request.form['tags']

    # Creazione di un engine SQLAlchemy per la connessione al database MySQL
    engine = create_engine('mysql+pymysql://root:S&ttembre3*@localhost/kawaiiComix')

    # Utilizzo di pandas per caricare i dati della tabella in un DataFrame
    df = pd.read_sql_query('SELECT * FROM prodotti', engine)

    # Pulizia delle colonne 'genere_nome' e 'categoria_nome'
    df['genere_nome'] = df['genere_nome'].apply(clean_text)
    df['categoria_nome'] = df['categoria_nome'].apply(clean_text)
    df['genere_nome'] = df['genere_nome'].apply(clean_text2)
    df['categoria_nome'] = df['categoria_nome'].apply(clean_text2)

    # Concatenazione delle colonne 'genere_nome' e 'categoria_nome' senza spazi aggiuntivi
    df['tags'] = df['genere_nome'] + ',' + df['categoria_nome']

    vectorizer = CountVectorizer()
    labels = vectorizer.fit_transform(df['tags'])

    X = pd.DataFrame(labels.toarray(), columns=vectorizer.get_feature_names_out())

    # Utilizza il modello di KMeans per ottenere le raccomandazioni
    recommendations = model.predict(X)

    # Ottieni il cluster del prodotto
    product_cluster = model.predict(vectorizer.transform([product_tags]))[0]

    print('cluster' + str(product_cluster))

    # Filtra i prodotti appartenenti allo stesso cluster del prodotto
    recommended_products = df[recommendations == product_cluster]

    # Se ci sono meno di 5 prodotti raccomandati, prendine tutti, altrimenti prendine 5 a caso
    random_recommended_products = recommended_products.sample(n=min(5, len(recommended_products)))

    # Restituisci gli oggetti prodotto completi come una lista JSON
    return jsonify(random_recommended_products.to_dict(orient='records'))


if __name__ == '__main__':
    app.run()
