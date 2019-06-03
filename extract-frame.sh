#!/bin/sh
# for dir in /run/media/javad/2E48FBFC48FBC11F/DATA-Partition/'Video Coursers'/DevOps/'Learn DevOps The Complete Kubernetes Course'/*; do
#     if [ -d "$dir" ]; then
#         for file in "$dir"/*; do
#             if [ -f "$file" ]; then
#                 file_name="$(basename -- "$file")" # file name and extension
#                 y=${file_name%.mp4}
#                 echo ${y##*/};   # file name without extension
#                 out="$dir/${y##*/}"
#                 mkdir -p "$out"; #make a dir same as filename without extension
#                 ffmpeg -i "$file" -vf fps=0.3 "$out/thumb%04d.jpg" -hide_banner
#             fi
#         done
#     fi
# dones

process_dir() {
    #echo $(ls "$1")
    for file in "$1"/*; do
        if [ -d "$file" ]; then
            process_dir $file
            elif [ -f "$file" ]; then
            extract_file_frames  "$file"
        fi
    done
}

extract_file_frames() {
    if [ ${1: -4} == ".mp4" ]; then
        file_name="$(basename -- "$1")" # file name and extension
        y=${file_name%.mp4}
        echo ${y##*/};   # file name without extension
        out="$dir/${y##*/}"
        mkdir -p "$out"; #make a dir same as filename without extension
        ffmpeg -i "$1" -vf fps=0.15 "$out/thumb%04d.jpg" -hide_banner
    fi
}

dir='/run/media/javad/2E48FBFC48FBC11F/DATA-Partition/Video Coursers/Test/Edureka Testing/'
process_dir "$dir"
