# LunchMan

[![Build Status](https://travis-ci.org/MollieS/LunchMan.svg?branch=master)](https://travis-ci.org/MollieS/LunchMan) [![Coverage Status](https://coveralls.io/repos/github/MollieS/LunchMan/badge.svg?branch=master)](https://coveralls.io/github/MollieS/LunchMan?branch=master)

### Clone

```shell
$ cd <folder where you want to store the project>

$ git clone https://github.com/MollieS/LunchMan.git

$ git clone git@github.com:MollieS/LunchMan.git

$ cd LunchMan/
```

### Build Tool

This project uses Activator or SBT, Activator is a wrapper for sbt with some
extra features, but isn't really needed to run/test/build the project.

#### OSX

```shell
$ brew install sbt
```

#### Linux

```shell
echo "deb https://dl.bintray.com/sbt/debian /" | sudo tee -a /etc/apt/sources.list.d/sbt.list
sudo apt-key adv --keyserver hkp://keyserver.ubuntu.com:80 --recv 642AC823
sudo apt-get update
sudo apt-get install sbt
```

### Run

This will start an autoloader that will recompile the project when changes are
introduced. However, new LunchManCore JAR files will require a complete
restart.

```shell
$ sbt run
```

### Test Program

```shell
$ sbt clean coverage test
```

### Generate Coverage

This assumes you have run the test command above first. The output from this
command will show the absolute path to the coverage report.

```shell
$ sbt coverageReport
```

