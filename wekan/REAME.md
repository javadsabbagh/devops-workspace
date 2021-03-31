
## Note
Images are pulled from *quay.io* instead of *dockerhub*.

## Backups 
Add crontab job:

```bash
crontab -e
```

# m h  dom mon dow   command
0 10,16  *  *   *     /home/javad/Projects/devops/wekan/backup-job.sh


