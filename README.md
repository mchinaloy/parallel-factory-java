# ParallelCarFactory
===================

This CarFactory implementation makes extensive use of Akka Actors to facilitate the passing of car components throughout each stage of the production line.

1. The CarComponentFactory periodically creates components with the use of the Akka scheduler
2. Each CarComponent knows whether it is faulty upon creation
3. The FilterActor checks if a component is faulty and filters out those that are
4. Once we have enough components a car is assembled by the AssembleActor
5. Assembled cars are then sent to one of three paint actors by way of the Akka RoundRobinRouter
6. Once the car is fully assembled and painted the results end up at the Merge actor that keeps track of the number of cars we are producing every 1 minute

The technology stack is as follows -

Akka | Java 8 | Spring Boot | Lombok | Gradle


Example Output
===================

`2015-07-12 18:28:44.576  INFO 7778 --- [lt-dispatcher-6] carfactory.application.merge.MergeActor  : New Car assembled: Car(serialNumber=86cd24af-48bf-4d89-aa10-b1fb0877f489, color=BLUE, engine=CarComponent(type=engine, serialNumber=6bf73312-c76a-43b2-bf03-72df838875ae, faulty=false), coachwork=CarComponent(type=coachwork, serialNumber=300e4b9f-1cfc-4d8e-a38a-b3a414591378, faulty=false), wheels=[CarComponent(type=wheel, serialNumber=b6e82741-326b-492c-8c2a-4921bf8f83a9, faulty=false), CarComponent(type=wheel, serialNumber=11aefa68-31d9-4b89-a17b-c0250ce470e3, faulty=false), CarComponent(type=wheel, serialNumber=3e125b3e-635c-4f11-a913-78b9a2a07485, faulty=false), CarComponent(type=wheel, serialNumber=9d873373-a6e6-4bf1-9279-76e886e8272e, faulty=false)])`

===================
Performance Metrics
===================

With a 20% fault rate -

`2015-07-12 18:14:09.648  INFO 7649 --- [lt-dispatcher-2] carfactory.application.merge.MergeActor  : Performance Check, number of cars produced in the last 1 minute: 1065`

With a 0% fault rate -

`2015-07-12 18:16:11.118  INFO 7663 --- [t-dispatcher-11] carfactory.application.merge.MergeActor  : Performance Check, number of cars produced in the last 1 minute: 1515`
