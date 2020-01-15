#!/bin/bash
mvn clean package dockerfile:build
mvn verify
mvn dockerfile:push