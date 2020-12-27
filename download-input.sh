#!/usr/bin/env bash

DAY=$(date +%d)

curl 'https://adventofcode.com/2020/day/16/input' \
  -H 'authority: adventofcode.com' \
  -H 'cache-control: max-age=0' \
  -H 'upgrade-insecure-requests: 1' \
  -H 'user-agent: Mozilla/5.0 (Macintosh; Intel Mac OS X 11_0_1) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/87.0.4280.88 Safari/537.36' \
  -H 'accept: text/html,application/xhtml+xml,application/xml;q=0.9,image/avif,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3;q=0.9' \
  -H 'sec-fetch-site: same-origin' \
  -H 'sec-fetch-mode: navigate' \
  -H 'sec-fetch-user: ?1' \
  -H 'sec-fetch-dest: document' \
  -H 'referer: https://adventofcode.com/2020/day/16' \
  -H 'accept-language: en-US,en;q=0.9,de-CH;q=0.8,de;q=0.7,ro-RO;q=0.6,ro;q=0.5,fr;q=0.4' \
  -H 'cookie: _ga=GA1.2.122760289.1606725519; session=53616c7465645f5faa692030aff7360cd77123aaed69035323789a63f986ffb731e46290f11ecba724d0f03c49812442; _gid=GA1.2.1153416207.1607944866' \
  --compressed \
  -o "input$DAY.txt"
