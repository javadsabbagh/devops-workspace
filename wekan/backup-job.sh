#!/bin/sh
cd /home/user/wekan
docker cp backup-db-inside-docker.sh  wekan-db:/data
docker exec -it wekan-db sh /data/backup-db-inside-docker.sh
docker cp wekan-db:/data/dump .
tar -cjvf wekan-db-"$(date +%Y%m%d_%H%M)".tar.bz2 dump/
rm -rf ./dump

