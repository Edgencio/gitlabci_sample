#!/usr/bin/env bash

chmod -R 775 ./*
mkdir -p build

# prepare deployment
APP_NAME="gitlabci_sample"
BUILD_FILE_NAME='gitlabci_sample-1.0-SNAPSHOT.war'
DEPLOY_FILE_NAME="$APP_NAME.war"
CONTEXT_ROOT="gitlabci_sample"
GLASSFISH_HOME="/opt/glassfish5/glassfish/bin"

# To increase safety, you could define this variables as environmental variables on gitlab

SSH_USER="user"
SSH_SERVER="your_production_server_domain" # eg: sample.co.mz
PASSWORDS_FILE="passwords.txt" # So that you can be able to execute operations remotely on your glassfish instance
GLASSFISH_ADMIN="admin"# Your glassfish admin user

# setup ssh private key
echo "$SSH_KEY_FILE" > "/tmp/id_rsa"
SSH_KEY_FILE="/tmp/id_rsa"
chmod 600 ${SSH_KEY_FILE}

mkdir -p ~/.ssh/
touch ~/.ssh/known_hosts
echo -e "Host *\n\tStrictHostKeyChecking no\n\n" > ~/.ssh/config


echo -e "copying application $BUILD_FILE_NAME to $SSH_SERVER with new name $DEPLOY_FILE_NAME"
scp -i ${SSH_KEY_FILE} target/${BUILD_FILE_NAME} ${SSH_USER}@${SSH_SERVER}:/tmp/${DEPLOY_FILE_NAME}

echo -e "Undeploying application from glassfish"
ssh -i ${SSH_KEY_FILE} ${SSH_USER}@${SSH_SERVER} "${GLASSFISH_HOME}/asadmin --user ${GLASSFISH_ADMIN} --passwordfile ${GLASSFISH_HOME}/${PASSWORDS_FILE} undeploy ${APP_NAME}"

echo -e "Deploying application on glassfish"
ssh -i ${SSH_KEY_FILE} ${SSH_USER}@${SSH_SERVER} "${GLASSFISH_HOME}/asadmin --user ${GLASSFISH_ADMIN} --passwordfile ${GLASSFISH_HOME}/${PASSWORDS_FILE} deploy --force=true --contextroot ${CONTEXT_ROOT} /tmp/${DEPLOY_FILE_NAME}"