# docker-containers
This repo holds some docker-compose files for running common docker containers, e.g MySQL, PostgreSQL, etc.

## Running containers

```shell
docker-compose up -f ./<docker-compose-file.yml>
```

You can use -d (detach mode) to run containers in background. You can also mention the intended service name in docker-compose file, such as:
```shell
docker-compose up -d -f ./<docker-compose-file.yml> <service-name>
```

## H2 Database
Create H2 database with h2-old (i.e H2 2014), then stop it and connect to the db with h2-new (i.e H2 2019):
```shell
docker-compose up -f ./h2-old.yml
docker-compose up -f ./h2-new.yml
```

*Connection URL* for H2 Old:
 - jdbc:h2:tcp://localhost:1521/testdb

*Connection URL* on H2 New:
 - jdbc:h2:tcp://localhost:1521/data/testdb
 - Note: You should also specify 'data' directory.

#### Removing H2 Databases
CD to the mounted volum directory, e.g /home/javad/DockerVolumes/h2 and run:
```sh
sudo rm -r *
```
