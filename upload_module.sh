#!/bin/sh 

pom_file="$1"
version="$2"
endpoint="$3"

display_name=$(xmllint --xpath 'string(/*[name()="project"]/*[name()="name"]/text())' "$pom_file")
artifact_id=$(xmllint --xpath 'string(/*[name()="project"]/*[name()="artifactId"]/text())' "$pom_file")
group_id="com.staf"
module_group=$(xmllint --xpath 'string(/*[name()="project"]/*[name()="properties"]/*[name()="taf-group"]/text())' "$pom_file")
description=$(xmllint --xpath 'string(/*[name()="project"]/*[name()="description"]/text())' "$pom_file")

jsonbody="{\"groupId\":\"${group_id}\", \"artifactId\": \"${artifact_id}\", \"displayName\": \"${display_name}\", \"description\": \"${description}\", \"moduleGroup\": 
\"${module_group}\", \"version\" : \"${version}\"}"

echo "Sending request to Wizard service with body"
echo $jsonbody
curl -X POST "$endpoint" -H "Content-Type: application/json" -H 'accept: */*' -d "$jsonbody"