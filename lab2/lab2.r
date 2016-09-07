# Andreza Raquel
library(tm)
library(FastKNN)

path = "C:/Users/Andreza/Documents/RI/OUTPUT/"

file.names <- dir(path)
doc.list = c()

for(i in 1:length(file.names)){
  filename <- paste(path, file.names[i], sep="")
  doc <- readChar(filename,file.info(filename)$size)
  doc.list[i] <- doc
}

names(doc.list) <- file.names
filenametest <- "Ace Ventura Pet Detective.DVDRip.BugBunny.br.srt"
#filenametest <- "Poltergeist(1982).br.srt"
#filenametest <- "Lord of the Rings The Two Towers The.DVDRip.SecretMyth.br.srt"
#filenametest <- "Fantastic Four.DVDRip.br.srt"
#filenametest <- "Frozen.720p.BlueRay.YIFY.br.srt"

fileTest <- paste(path, filenametest, sep="")
doctest <- readChar(fileTest,file.info(fileTest)$size)

my.docs <- VectorSource(c(doc.list, doctest))
my.docs$Names <- c(names(doc.list), "test")

my.corpus <- Corpus(my.docs)
my.corpus <- tm_map(my.corpus, removePunctuation)
my.corpus <- tm_map(my.corpus, stemDocument)
my.corpus <- tm_map(my.corpus, removeNumbers)
my.corpus <- tm_map(my.corpus, stripWhitespace)

term.doc.matrix.stm <- TermDocumentMatrix(my.corpus)

term.doc.matrix <- as.matrix(term.doc.matrix.stm)

tfidf.matrix = weightTfIdf(term.doc.matrix.stm)
colnames(tfidf.matrix) <- colnames(term.doc.matrix)

ncol(tfidf.matrix[1:646,])

distance_matrix <-  Distance_for_KNN_test(tfidf.matrix[647,], tfidf.matrix[1:646, ])

nearest <- k.nearest.neighbors(1,distance_matrix = distance_matrix, k=5)
nearest