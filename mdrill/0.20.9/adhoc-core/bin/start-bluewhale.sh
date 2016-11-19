#!/bin/bash
#
echo "start bluewhale"
nohup ./bluewhale nimbus >nimbus.log &
nohup ./bluewhale supervisor >supervisor.log &
nohup ./bluewhale mdrillui 1107 ../lib/adhoc-web-0.18-beta.jar ./ui >ui.log &
echo "started"