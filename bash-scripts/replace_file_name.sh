#!/bin/sh

dir='/run/media/javad/2E48FBFC48FBC11F/DATA-Partition/Video Coursers/DevOps/Edureka DevOps/'

for file in "$dir"/*; do
    if [ -f "$file" ]; then
        newName=$(echo "$file" | sed 's/ _ Edureka//g')
        mv "$file" "$newName"
    fi
done