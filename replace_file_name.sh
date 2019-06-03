#!/bin/sh

dir='/run/media/javad/2E48FBFC48FBC11F/DATA-Partition/Video Coursers/Python/Skillhshare - Build Library Management System_Python & PyQt5/'

for file in "$dir"/*; do
    if [ -f "$file" ]; then
        newName=$(echo "$file" | sed 's/-git.ir//g')
        mv "$file" "$newName"
    fi
done