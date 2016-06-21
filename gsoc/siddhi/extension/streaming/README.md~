#Streaming Queries

##1. Streaming Linear Regression with SGD

###1.1 Template Query

from LinRegInputStream#streaming:streaminglr((learnType),(windowShift), (batchSize/timeFrame), (numIterations), (stepSize), (miniBatchFraction), (ci), salary, rbi, walks, strikeouts, errors)
select * 
insert into regResults; 

####1.1.1 Learn Type (learnType-int)
Select the required Learnign Method
0-Batch processing
1-Moving Window Processing
2-Time based Processing

####1.1.2 Batch Size (batchSize-int)
select the batch size required to periodically retrain the models

####1.1.3Number Of Iterations (numIterations-int)
Number of times that the SGD algorithms should be run

####1.1.4 Step Size (stepSize-int)
This related to step size on the Stochastic Gradient Descent Algorithms

####1.1.5 Mini Batch Fraction (miniBatchFraction-double * 0-1)
Percentage of data fron the current batch that algorithm shoud proceed at each iteration

####1.1.6 Confident Interval (ci-double *0-1)
In our case this should not be matter.But still did not finalize

####1.1.7 Variable List- (Dependent Variable, Independent Variables)
Please follow the JavaDocs in spark example
###1.2 Example Query

@Import('LinRegInputStream:1.0.0')
define stream LinRegInputStream (salary double, rbi double, walks double, strikeouts double, errors double);

@Export('LinRegOutputStream:1.0.0')
define stream LinRegOutputStream (regResults double);

from LinRegInputStream#streaming:streaminglr(0,1, 2, 100, 0.00000001, 1.0, 0.95, salary, rbi, walks, strikeouts, errors)
select *
insert into regResults;

##2. Streaming KMeans Clustering
###2.1 Template Query

from LinRegInputStream#streaming:streamingkm((learnType), (windowShift), (batchSize/timeFrame), (numClusters), (numIterations),(alpha), (ci), salary, rbi, walks, strikeouts, errors)
select *
insert into regResults;


####2.1.1 Learn Type (learnType-int)
Select the required Learnign Method
0-Batch processing\n
1-Moving Window Processing\n
2-Time based Processing\n

####2.1.2 Batch Size (batchSize-int)
select the batch size required to periodically retrain the models

####2.1.3 Number of Clusters (numClusters-int)
Clusters youo need

####2.1.4 Number Of Iterations (numIterations-int)
Number of times that the SGD algorithms should be run

####2.1.5 Alpha (alpha-double *0-1)
This is to control data horizon and the obsolences of data points when periodically retarin the model

####2.1.6 Confident Interval (ci-double *0-1)
In our case this should not be matter.But still did not finalize

####2.1.7 Variable List-(Dependent Variable, Independent Variables)

###2.2 Example Query
@Import('KMeansInputStream:1.0.0')
define stream KMeansInputStream (salary double, rbi double, walks double, strikeouts double, errors double);

@Export('KMeansOutputStream:1.0.0')
define stream KMeansOutputStream (regResults double);

from KMeansInputStream#streaming:streamingkm(0,1,3,0.95,2,10,1,salary,rbi,walks,strikeouts,errors)
select *
insert into regResults

