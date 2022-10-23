#!/usr/bin/env bash

docker run -itd -p 8080:8080 \
                -e APPLICATION_PROFILE=local \
                -e GITHUB_TOKEN=your_token \
                --name tui_task \
                tui_task_0.0.1