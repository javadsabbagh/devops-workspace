process_dir() {
    #echo $(ls "$1")
    for file in "$1"/*; do
        if [ -d "$file" ]; then
            echo Entering directory "$file"
            process_dir "$file"   "$1"
        elif [ -f "$file" ]; then
            echo Processing file "$file"
            extract_file_frames  "$file"  "$1"
        fi        
    done
    echo Exiting directory "$file"
}

extract_file_frames() {
    if [ ${1: -4} == ".mp4" ]; then
        file_name="$(basename -- "$1")" # file name and extension
        y=${file_name%.mp4}
        echo ${y##*/};   # file name without extension
        out="$2/${y##*/}"
        mkdir -p "$out"; #make a dir same as filename without extension
        ffmpeg -i "$1" -vf fps=0.1 "$out/thumb%04d.jpg" -hide_banner
    fi
}

dir='/run/media/javad/2E48FBFC48FBC11F/DATA-Partition/Video Coursers/Python/Learn Cryptography Basics in Python'
process_dir "$dir"
