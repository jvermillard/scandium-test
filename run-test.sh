#!/bin/sh
sudo docker run -v /home/jvermillar/sandbox/scandium-test/scandium:/build/californium.scandium -v /home/jvermillar/sandbox/scandium-test/scandium-run-test:/build/scandium-run-test  -i -t --rm jvermillard/test-container
