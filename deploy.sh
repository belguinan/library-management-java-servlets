#!/bin/bash
RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
NC='\033[0m'
WILDFLY_HOME="/opt/homebrew/opt/wildfly-as/libexec"
DEBOUNCE_DELAY=10

build_and_deploy() {
    echo -e "${YELLOW}starting build and deploy project ${NC}"
    
    if [ ! -f "pom.xml" ]; then
        echo -e "${RED}pom.xml is not found${NC}"
        return 1
    fi

    PROJECT_NAME=$(mvn help:evaluate -Dexpression=project.artifactId -q -DforceStdout)
    if [ -z "$PROJECT_NAME" ]; then
        echo -e "${RED}failed to get project name${NC}"
        return 1
    fi

    echo -e "${YELLOW}Building $PROJECT_NAME...${NC}"
    mvn clean package
    if [ $? -ne 0 ]; then
        echo -e "${RED}failed to run mvn clean package${NC}"
        return 1
    fi

    if ! pgrep -f "wildfly" > /dev/null; then
        echo -e "${YELLOW}wildfly is not running or not found${NC}"
        $WILDFLY_HOME/bin/standalone.sh &
        echo -e "${YELLOW}starting wildfly...${NC}"
        sleep 10
    fi

    WAR_PATH="target/$PROJECT_NAME.war"
    if [ ! -f "$WAR_PATH" ]; then
        echo -e "${RED}WAR is not found $WAR_PATH${NC}"
        return 1
    fi

    echo -e "${YELLOW}Cleaning all deployments...${NC}"
    rm -f "$WILDFLY_HOME/standalone/deployments/"*.{war,ear,jar,deployed,failed,dodeploy}

    echo -e "${YELLOW}Deployment is started...${NC}"
    cp "$WAR_PATH" "$WILDFLY_HOME/standalone/deployments/"
    echo -e "${YELLOW}Hold on...${NC}"
    sleep 5

    if [ -f "$WILDFLY_HOME/standalone/deployments/$PROJECT_NAME.war.deployed" ]; then
        echo -e "${GREEN}Done!${NC}"
        echo -e "${GREEN}Wildfly dashboard: http://localhost:9990/${NC}"
        echo -e "${GREEN}Server logs: $WILDFLY_HOME/standalone/log/server.log${NC}"
        echo -e "${GREEN}App access: http://localhost:8081/$PROJECT_NAME${NC}"
    else
        echo -e "${RED}Failed to deploy please check${NC}"
        echo -e "${YELLOW}$WILDFLY_HOME/standalone/log/*${NC}"
        echo -e "${YELLOW}$WILDFLY_HOME/standalone/deployments/*${NC}"
        return 1
    fi
}

if ! command -v fswatch &> /dev/null; then
    echo -e "${RED}fswatch is not installed. Installing...${NC}"
    brew install fswatch
fi

build_and_deploy

LAST_BUILD=0
echo -e "${YELLOW}Watching for changes...${NC}"
fswatch -e ".*" -i "\\.java$" -i "\\.xml$" -i "\\.css$" -i "\\.js$" -i "\\.jsp$" -i "\\.html$" --event Updated ./src ./pom.xml | while read f; do
    CURRENT_TIME=$(date +%s)
    if (( CURRENT_TIME - LAST_BUILD > DEBOUNCE_DELAY )); then
        echo -e "${YELLOW}File saved: $f${NC}"
        build_and_deploy
        LAST_BUILD=$CURRENT_TIME
    fi
done
