#!/bin/bash

if [ -z "$1" ]; then
    echo "entrez un message de commit"
else
    git add .
    git commit -m "$1"
    git push 
fi
