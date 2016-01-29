#!/bin/bash
docker-compose kill
docker-compose rm -vf
docker-compose up -d
