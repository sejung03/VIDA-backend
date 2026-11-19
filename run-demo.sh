#!/usr/bin/env sh
set -eu
./gradlew bootRun --args='--spring.profiles.active=demo'
