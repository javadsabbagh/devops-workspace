
## Note
Images are pulled from *quay.io* instead of *dockerhub*.

## Backups 
Add crontab job:

```bash
crontab -e
```

# m h  dom mon dow   command
0 10,16  *  *   *     /home/user/wekan/backup-job.sh


