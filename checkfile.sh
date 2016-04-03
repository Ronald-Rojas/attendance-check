#!/bin/bash
if [ $(ls -1A /home/ubuntu/input/ | wc -l) -gt 0 ]; then
    echo "cronjob" >> /home/cron.txt



fi
