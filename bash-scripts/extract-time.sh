#!/bin/sh
for dir in ./*; do
    if [ -d "$dir" ]; then
        for file in "$dir"/*; do
            if [ -f "$file" ]; then
                if [ ${file: -4} == ".mp4" ]; then
                    echo Testing \"$file\" ...
                    time_d=$(ffmpeg -i "$file" 2>&1 | grep Duration | cut -d ' ' -f 4 | sed s/,//)                    
                    echo $time_d
                fi;
            fi;
        done;
    fi
done
