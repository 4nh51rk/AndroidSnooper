#!/bin/bash
if [ "${TRAVIS_PULL_REQUEST}" != "false" ]; then
    echo "Pull request: Skipping test artifact deployment:"
else
    zip -r reports.zip Snooper/build/spoon
    curl -X POST https://content.dropboxapi.com/2/files/upload \
        --header "Authorization: Bearer ${DROPBOX_ACCESS_TOKEN}" \
        --header "Dropbox-API-Arg: {\"path\": \"/builds/circle-ci-reports_${CIRCLE_BUILD_NUM}.zip\",\"mode\": \"overwrite\",\"autorename\": true,\"mute\": false}" \
        --header "Content-Type: application/octet-stream" \
        --data-binary @reports.zip
fi