#!/usr/bin/env bash

cd "$(dirname "$0")" || exit 131

docker-compose down

docker run --rm -v "$HOME/volumes/conf2-api-debug/:/data" \
       busybox:1.28 \
       find /data -mindepth 1 -maxdepth 1 -exec \
       rm -rf {} \;

docker-compose up -d
