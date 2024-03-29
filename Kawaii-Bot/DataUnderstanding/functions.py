import pandas as pd
import matplotlib.pyplot as plt
import numpy as np
import missingno as msno
from collections import Counter
from scipy import stats
import re
import string
import seaborn as sns


def rimuovi_spazi(match):
    return match.group(0).replace(" ", "")

def clean_text(text):
    # Rimuovi gli spazi extra
    text = ' '.join(text.split())
    # Rimuovi la punteggiatura
    text = text.translate(str.maketrans('', '', string.punctuation))
    # Rimuovi i numeri
    text = re.sub(r'\d+', '', text)
    # Rimuovi i caratteri speciali e simboli
    text = re.sub(r'[^a-zA-Z\s]', '', text)

    return text

def tagDistribution(all_tags_list, top_n=35):
    # Conta le ripetizioni di ogni tag
    tag_counts = Counter(all_tags_list)

    # Estrai i top N tag più comuni e i loro conteggi
    top_tags = tag_counts.most_common(top_n)
    other_count = sum(tag_counts.values()) - sum(count for tag, count in top_tags)

    # Seleziona i tag e i conteggi per il grafico
    tags = [tag for tag, count in top_tags] + ['Altro']
    counts = [count for tag, count in top_tags] + [other_count]

    # Crea il grafico a barre della distribuzione dei tag
    plt.figure(figsize=(20, 15))
    plt.bar(tags, counts, color='skyblue')
    plt.xlabel('Tag')
    plt.ylabel('Numero di occorrenze')
    plt.title('Distribuzione dei tag')
    plt.xticks(rotation=45)  # Ruota le etichette sull'asse x per una migliore leggibilità
    plt.show()


def nanValues(dataset):
    #Conta e stampa i valori null per ogni attributo
    nan_mask = dataset.isna()
    nan_count = nan_mask.sum()
    print(nan_count)

    #Crea il grafico a griglia
    msno.matrix(dataset, color=(0.53, 0.81, 0.98))
    plt.show()


def duplicatedValues(dataset):
    duplicated_values = dataset.duplicated().sum()
    not_duplicated_values = (~dataset.duplicated()).sum()
    print("Valori duplicati: ", duplicated_values)
    print("Valori non duplicati: ", not_duplicated_values)

    # Creazione grafico a barre
    fig = plt.figure(figsize=(3, 2))
    plt.bar(["Duplicati", "Non Duplicati"], [duplicated_values, not_duplicated_values],
            color='skyblue', width=0.5, align='center')
    plt.subplots_adjust(left=0.2)
    plt.show()


def frequenze_osservate_rating(mangadataset):
    # Calcola le statistiche descrittive del rating
    rating_stats = mangadataset['rating'].describe()

    # Stampa le statistiche descrittive del rating
    print("Statistiche descrittive del rating:")
    print(rating_stats)

    # Visualizza un istogramma del rating
    plt.figure(figsize=(8, 6))
    plt.hist(mangadataset['rating'], bins=20, color='skyblue', edgecolor='white')
    plt.title('Distribuzione del rating dei manga')
    plt.xlabel('Rating')
    plt.ylabel('Frequenza')
    plt.grid(True)
    plt.show()


def frequenze_teoriche_rating(mangadataset):

    # Calcola le statistiche descrittive del rating
    rating_stats = mangadataset['rating'].describe()

    # Stampa le statistiche descrittive del rating
    print("Statistiche descrittive del rating:")
    print(rating_stats)

     # Calcola la media e la deviazione standard del rating
    mean_rating = rating_stats['mean']
    std_rating = rating_stats['std']

    # Genera una sequenza di valori per le frequenze teoriche
    min_rating = mangadataset['rating'].min()
    max_rating = mangadataset['rating'].max()
    x = np.linspace(min_rating, max_rating, 100)
    y = stats.norm.pdf(x, mean_rating, std_rating) * len(mangadataset['rating'])

    # Visualizza un istogramma del rating con frequenze osservate e teoriche
    plt.figure(figsize=(8, 6))
    plt.hist(mangadataset['rating'], bins=20, color='skyblue', edgecolor='black', label='Frequenze osservate')
    plt.plot(x, y, color='red', label='Frequenze teoriche')
    plt.title('Distribuzione del rating dei manga')
    plt.xlabel('Rating')
    plt.ylabel('Frequenza')
    plt.grid(True)
    plt.legend()
    plt.show()


def box_plot_rating(mangadataset):
    # Creazione del box plot del rating con colore skyblue
    plt.figure(figsize=(8, 6))
    bp = plt.boxplot(mangadataset['rating'], patch_artist=True)

    # Imposta il colore delle caselle del boxplot a skyblue
    for box in bp['boxes']:
        box.set(color='skyblue')

    plt.title('Box Plot del Rating dei Manga')
    plt.ylabel('Rating')
    plt.show()


def trova_outliers_iqr(mangadataset):
    rating = mangadataset['rating']

    # Calcola il primo e il terzo quartile
    Q1 = rating.quantile(0.25)
    Q3 = rating.quantile(0.75)

    # Calcola l'intervallo interquartile (IQR)
    IQR = Q3 - Q1

    # Calcola i limiti per identificare gli outlier
    lower_bound = Q1 - 1.5 * IQR
    upper_bound = Q3 + 1.5 * IQR

    # Trova gli outlier
    outlier_indices = rating[(rating < lower_bound) | (rating > upper_bound)].index.tolist()

    outlier_ratings = mangadataset.loc[outlier_indices, 'rating']
    print(outlier_ratings)