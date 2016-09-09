# Andreza Raquel
library(tm)
library(FastKNN)

path = "C:/Users/Andreza/Desktop/output_movies/"

file.names <- dir(path)
doc.list = c()

for(i in 1:length(file.names)){
  filename <- paste(path, file.names[i], sep="")
  doc <- readChar(filename,file.info(filename)$size)
  doc.list[i] <- doc
}

N.docs <- length(doc.list)
names(doc.list) <- paste0("doc", c(1:N.docs))

my.docs <- VectorSource(doc.list)
my.docs$Names <- names(doc.list)

my.corpus <- Corpus(my.docs)
my.corpus <- tm_map(my.corpus, removePunctuation)
my.corpus <- tm_map(my.corpus, stemDocument)
my.corpus <- tm_map(my.corpus, removeNumbers)
my.corpus <- tm_map(my.corpus, stripWhitespace)

term.doc.matrix.stm <- DocumentTermMatrix(my.corpus)

term.doc.matrix <- as.matrix(term.doc.matrix.stm)

tfidf.matrix = weightTfIdf(term.doc.matrix.stm)
colnames(tfidf.matrix) <- colnames(term.doc.matrix)

ncol(term.doc.matrix.stm)
nrow(term.doc.matrix.stm)
nrow(tfidf.matrix)

aceVenture <- 32
poltergeist <- 445
lordOfTheRings <- 373
fantasticFour <- 228
frozen <- 249

testIndex <- frozen
incTestIndex <- testIndex+1
matrixTest <- tfidf.matrix[testIndex,]

matrixTrain <- rbind2(tfidf.matrix[1:testIndex-1, ], tfidf.matrix[incTestIndex:N.docs, ])
nrow(matrixTrain)

distance_matrix <-  Distance_for_KNN_test(matrixTest, matrixTrain)

nearest <- k.nearest.neighbors(1,distance_matrix = distance_matrix, k=5)
nearest