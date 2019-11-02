#!/bin/bash

release=$(ls runelite-client/build/libs/*shaded.jar | sort --version-sort | tail -n1)
java -ea -jar $release --developer-mode
