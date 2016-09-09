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

bourneIdentity <- 145
bruceAlmighty <- 157
fastFoodNation <- 233
freeWilly <- 246
theGodfather <- 260
ironMan <- 326
platoon <- 443
prettyWoman <- 450
pussInBoots <- 456
scarface <- 489


list.test.index <- list(bourneIdentity, bruceAlmighty, fastFoodNation, freeWilly, theGodfather, ironMan, platoon, prettyWoman, pussInBoots, scarface )

mBourne <- tfidf.matrix[bourneIdentity,]
mBruce <- tfidf.matrix[bruceAlmighty,]
mFree <- tfidf.matrix[freeWilly,]
mGodfather <- tfidf.matrix[theGodfather,]
mIronMan <- tfidf.matrix[ironMan,]
mPlatoon <- tfidf.matrix[platoon,]
mPuss <- tfidf.matrix[pussInBoots,]
mScarface <- tfidf.matrix[scarface,]
mPrettyWoman <- tfidf.matrix[prettyWoman,]
mFastFood <- tfidf.matrix[fastFoodNation,]

list.test <- list(mBourne, mBruce, mFree, mGodfather, mIronMan, mPlatoon, mPuss, mScarface, mPrettyWoman, mFastFood)
matrixTest <- do.call(rbind, list.test)
nrow(matrixTest)
ncol(matrixTest)

matrixTrain <- tfidf.matrix
matrixTrain <- matrixTrain[-scarface,]
matrixTrain <- matrixTrain[-pussInBoots,]
matrixTrain <- matrixTrain[-prettyWoman,]
matrixTrain <- matrixTrain[-platoon,]
matrixTrain <- matrixTrain[-ironMan,]
matrixTrain <- matrixTrain[-theGodfather,]
matrixTrain <- matrixTrain[-freeWilly,]
matrixTrain <- matrixTrain[-fastFoodNation,]
matrixTrain <- matrixTrain[-bruceAlmighty,]
matrixTrain <- matrixTrain[-bourneIdentity,]
nrow(matrixTrain)
ncol(matrixTrain)

distance_matrix <-  Distance_for_KNN_test(matrixTest, matrixTrain)

labels <- read.csv("C:/Users/Andreza/Desktop/entregaveisRI/labels.csv", header=TRUE, sep=",") 

labels <- labels[-scarface,]
labels <- labels[-pussInBoots,]
labels <- labels[-prettyWoman,]
labels <- labels[-platoon,]
labels <- labels[-ironMan,]
labels <- labels[-theGodfather,]
labels <- labels[-freeWilly,]
labels <- labels[-fastFoodNation,]
labels <- labels[-bruceAlmighty,]
labels <- labels[-bourneIdentity,]

matrixTest <- as.matrix(matrixTest) 

knn <- knn_test_function(matrixTrain, matrixTest, distance_matrix, labels[,2], k=20)

knn