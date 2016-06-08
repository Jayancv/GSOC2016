#Streaming Queries

##Streaming Linear Regression with SGD

###Template Query

from LinRegInputStream#streaming:streaminglr(<learnType>, <batchSize/timeFrame>, <numIterations>, <stepSize>, <miniBatchFraction>, <ci>, salary, rbi, walks, strikeouts, errors)
select *
insert into regResults;

####Learn Type <learnType>
Select the required Learnign Method
0-Batch processing
1-Moving Window Processing
2-Time based Processing

####Batch Size <batchSize>
select the batch size required to periodically retrain the models

####Number Of Iterations <numIterations>
Number of times that the SGD algorithms should be run

####Step Size <stepSize>
This related to step size on the Stochastic Gradient Descent Algorithms

####Mini Batch Fraction
Percentage of data fron the current batch that algorithm shoud proceed at each iteration

####Confident Interval <ci>
In our case this should not be matter.But still did not finalize

####Variable List-<Dependent Variable, Independent Variables>
Please follow the JavaDocs in spark example
###Example Query

@Import('LinRegInputStream:1.0.0')
define stream LinRegInputStream (salary double, rbi double, walks double, strikeouts double, errors double);

@Export('LinRegOutputStream:1.0.0')
define stream LinRegOutputStream (regResults double);

from LinRegInputStream#streaming:streaminglr(0, 2, 100, 0.00000001, 1, 0.95, salary, rbi, walks, strikeouts, errors)
select *
insert into regResults;

##Streaming KMeans Clustering
###Template Query

from LinRegInputStream#streaming:streamingkm(<learnType>, <batchSize/timeFrame>, <numClusters>, <numIterations>,<alpha>, <ci>, salary, rbi, walks, strikeouts, errors)
select *
insert into regResults;


####Learn Type <learnType>
Select the required Learnign Method
0-Batch processing
1-Moving Window Processing
2-Time based Processing

####Batch Size <batchSize>
select the batch size required to periodically retrain the models

####Number of Clusters <numClusters>
Clusters youo need

####Number Of Iterations <numIterations>
Number of times that the SGD algorithms should be run

####Alpha <alpha>
This is to control data horizon and the obsolences of data points when periodically retarin the model

####Confident Interval <ci>
In our case this should not be matter.But still did not finalize

####Variable List-<Dependent Variable, Independent Variables>

###Example Query
@Import('KMeansInputStream:1.0.0')
define stream KMeansInputStream (salary double, rbi double, walks double, strikeouts double, errors double);

@Export('KMeansOutputStream:1.0.0')
define stream KMeansOutputStream (regResults double);

from KMeansInputStream#streaming:streamingkm(0,3,0.95,2,10,1,salary,rbi,walks,strikeouts,errors)
select *
insert into regResults

