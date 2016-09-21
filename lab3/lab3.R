# Andreza
# Run it in interactive mode
library(tm)
library(FastKNN)

path = "C:/Users/Andreza/Desktop/output_movies/"
# Number of movies that will be shown for the user evaluation
NUM_MOVIES_TEST = 2

file.names <- dir(path) # Documents from input

# ========= Getting content of the files that the user liked and putting it into a unique document ========

movies.offer <- sample(1:length(file.names), NUM_MOVIES_TEST, replace=F) # Generating random list of documents for the user evaluation
movies.liked = c() # List of documents that the user liked. It will be populated in the loop below
for(i in 1: NUM_MOVIES_TEST) {
  number.movie <- movies.offer[i]
  question <- paste0("Did you like: ",file.names[number.movie], " ? [s or n]")
  answer <- readline(prompt=question)
  if (answer == "s"){
    movies.liked[i] <- number.movie
  }
}

N.movies.liked <- length(movies.liked)

content.docs.liked <- "" # Generating document that will be composed for all contents of the films that the user likes
for(i in 1:N.movies.liked){
  index = movies.liked[i] # Index of the film that the user likes
  filename <- paste(path, file.names[index], sep="")
  doc <- readChar(filename,file.info(filename)$size)
  content.docs.liked <- paste(content.docs.liked,doc) 
}
# ========= The variable content.docs.liked has the content of all movies that the user liked =============


doc.list = c() # List of contents from each document

indexlist <- 1
for(i in 1:length(file.names)){
  if (!(i %in% movies.liked)) { # Adding to documents just the films that is not in the list movies.liked
    filename <- paste(path, file.names[i], sep="")
    doc <- readChar(filename,file.info(filename)$size)
    doc.list[indexlist] <- doc
    indexlist <- indexlist + 1
  } 
}

N.docs <- length(doc.list) # Number of documents
doc.list[N.docs+1] <- content.docs.liked # Adding the variable content.docs.liked into the list of all other docs

N.docs <- length(doc.list) # Number of documents
names(doc.list) <- paste0("doc", c(1:N.docs))

my.docs <- VectorSource(doc.list)
my.docs$Names <- names(doc.list)

my.corpus <- Corpus(my.docs)
my.corpus <- tm_map(my.corpus, removePunctuation)
my.corpus <- tm_map(my.corpus, stemDocument)
my.corpus <- tm_map(my.corpus, removeNumbers)
my.corpus <- tm_map(my.corpus, stripWhitespace)

term.doc.matrix.stm <- DocumentTermMatrix(my.corpus)
term.doc.matrix <- as.matrix(term.doc.matrix.stm)  # Matrix of document-term with all documents

tfidf.matrix = weightTfIdf(term.doc.matrix.stm) # Matrix of tfidf with all documents
colnames(tfidf.matrix) <- colnames(term.doc.matrix)


# Generating matrixTest.
matrixTest = as.matrix(tfidf.matrix[N.docs,])
nrow(matrixTest)
ncol(matrixTest)

# Generating matrixTrain. 
matrixTrain <- tfidf.matrix[1:N.docs-1,]
nrow(matrixTrain)
ncol(matrixTrain)


# Generating distance matrix
distance_matrix <-  Distance_for_KNN_test(matrixTest, matrixTrain)

# More similar movies
nearest <- k.nearest.neighbors(1,distance_matrix = distance_matrix, k=5)
nearest