name := "Final"

version := "0.1"

scalaVersion := "2.12.4"

libraryDependencies ++= Seq(
  "com.typesafe.akka" %% "akka-actor" % "2.5.6",
  "com.typesafe.akka" %% "akka-testkit" % "2.5.6" % Test
)
libraryDependencies ++= Seq(
  "com.typesafe.akka" %% "akka-stream" % "2.5.6",
  "com.typesafe.akka" %% "akka-stream-testkit" % "2.5.6" % Test
)
libraryDependencies ++= Seq(
  "com.typesafe.akka" %% "akka-http" % "10.0.10",
  "com.typesafe.akka" %% "akka-http-testkit" % "10.0.10" % Test
)
libraryDependencies +=
  "com.typesafe.akka" %% "akka-cluster" % "2.5.6"
libraryDependencies +=
  "com.typesafe.akka" %% "akka-cluster-sharding" %  "2.5.6"

libraryDependencies +=
  "com.typesafe.akka" %% "akka-distributed-data" % "2.5.6"

libraryDependencies +=
  "com.typesafe.akka" %% "akka-persistence" % "2.5.6"

libraryDependencies +=
  "com.typesafe.akka" %% "akka-http-spray-json" % "10.0.10"

libraryDependencies += "com.lihaoyi" %% "scalatags" % "0.6.7"




libraryDependencies ++= Seq(
  "com.typesafe.slick" %% "slick" % "3.2.1",
  "org.slf4j" % "slf4j-nop" % "1.6.4",
  "com.h2database" % "h2" % "1.4.196"
)

libraryDependencies += "ch.megard" %% "akka-http-cors" % "0.2.2"
libraryDependencies +=
  "com.oracle" % "ojdbc6" % "11.2.0.1.0"

libraryDependencies ++= Seq(
  "com.typesafe.akka" %% "akka-actor" % "2.5.6",
  "com.typesafe.akka" %% "akka-testkit" % "2.5.6" % Test
)
libraryDependencies ++= Seq(
  "com.typesafe.akka" %% "akka-stream" % "2.5.6",
  "com.typesafe.akka" %% "akka-stream-testkit" % "2.5.6" % Test
)
libraryDependencies ++= Seq(
  "com.typesafe.akka" %% "akka-http" % "10.0.10",
  "com.typesafe.akka" %% "akka-http-testkit" % "10.0.10" % Test
)
libraryDependencies +=
  "com.typesafe.akka" %% "akka-cluster" % "2.5.6"
libraryDependencies +=
  "com.typesafe.akka" %% "akka-cluster-sharding" %  "2.5.6"

libraryDependencies +=
  "com.typesafe.akka" %% "akka-distributed-data" % "2.5.6"

libraryDependencies +=
  "com.typesafe.akka" %% "akka-persistence" % "2.5.6"

libraryDependencies +=
  "com.typesafe.akka" %% "akka-http-spray-json" % "10.0.10"

libraryDependencies += "ch.megard" %% "akka-http-cors" % "0.2.2"

libraryDependencies += "org.scalatest" %% "scalatest" % "3.0.1" % "test"

libraryDependencies ++= Seq(
  "org.scalikejdbc" %% "scalikejdbc"       % "3.1.0",
  "org.scalikejdbc"   %%  "scalikejdbc-config" % "3.1.0",
  "com.h2database"  %  "h2"                % "1.4.196",
  "ch.qos.logback"  %  "logback-classic"   % "1.2.3"
)

libraryDependencies += "ch.qos.logback" % "logback-classic" % "1.1.3"

libraryDependencies += "com.typesafe.scala-logging" %% "scala-logging" % "3.7.2"