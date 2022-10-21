#!/usr/bin/env bash

docker run -itd -e APPLICATION_PROFILE=local -p 8080:8080 \
                --name tui_task \
                tui_task:0.0.1