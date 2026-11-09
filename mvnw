#!/bin/sh
# ----------------------------------------------------------------------------
# Maven Wrapper Script
# ----------------------------------------------------------------------------

MAVEN_HOME="/Users/sarahgamal/.m2/wrapper/dists/apache-maven-3.6.3-bin/1iopthnavndlasol9gbrbg6bf2/apache-maven-3.6.3"

if [ -f "$MAVEN_HOME/bin/mvn" ]; then
    exec "$MAVEN_HOME/bin/mvn" "$@"
fi

# Fallback: check if brew or system maven is available
if command -v mvn >/dev/null 2>&1; then
    exec mvn "$@"
fi

echo "Error: Maven not found. Please install Maven or check your PATH." >&2
exit 1
