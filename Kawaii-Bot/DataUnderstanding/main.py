from DataUnderstanding.functions import *

manga_dataset = pd.read_csv('../manga.csv')

print("\nVALORI NULL")
nanValues(manga_dataset)

print("\nVALORI DUPLICATI")
#duplicatedValues(manga_dataset)

# Applica la rimozione degli spazi a ogni riga della colonna 'tags'
manga_dataset['tags'] = manga_dataset['tags'].apply(lambda x: re.sub(r"'([^']*)'", rimuovi_spazi, x))
# Pulisce il dataset dei tag
manga_dataset['tags'] = manga_dataset['tags'].apply(clean_text)
# Estrai tutti i tag in un'unica stringa
all_tags_string = manga_dataset['tags'].str.cat(sep=' ')
# Dividi la stringa in una lista di singoli tag
all_tags_list = all_tags_string.split()

# Rimuovi i duplicati convertendo la lista in un set e poi di nuovo in una lista
unique_tags = list(set(all_tags_list))
unique_tags.sort()

# Calcola la distribuzione dei tag
print("\nDISTRIBUZIONE VARIABILI")
#tagDistribution(all_tags_list)

print("\nBOX PLOT")
#boxPlot(all_tags_list)