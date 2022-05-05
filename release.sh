#!/usr/bin/env bash

##### Bamboo variables
export JAVA_NOME=$bamboo_capability_system_jdk_OpenJDK_17_0_1

export MVN_HOME=$bamboo_capability_system_builder_mvn3_Maven_3
MVN_EXEC=$MVN_HOME/bin/mvn
MVN_CACHE="$(find /srv/bamboo/bamboo-agent-home/.m2/AGENT-* -name repository -maxdepth 1 -print)"
MAVEN="$MVN_EXEC -Dmaven.repo.local=$MVN_CACHE -B"

export GIT_SSH=/srv/repositories/bin/swm/mvngitwrapper/git-ssh-wrapper
GIT=$bamboo_capability_system_git_executable

##### Helper functions
failIfReturncodeNot0() {
  if [ $1 -ne 0 ]; then
    echo -n "returncode war nicht 0 sondern $1" >&2
    exit 1
  fi
}

readVersion() {
  $MAVEN org.apache.maven.plugins:maven-help-plugin:3.1.1:evaluate -Dexpression=project.version -q -DforceStdout
  failIfReturncodeNot0 $?
}

##### Release process
"$JAVA_HOME"/bin/java -version
failIfReturncodeNot0 $?

$MAVEN -v
failIfReturncodeNot0 $?

CURRENT_VERSION=$(readVersion)
echo Current version is $CURRENT_VERSION

echo Removing SNAPSHOT suffix
$MAVEN versions:set -DremoveSnapshot
failIfReturncodeNot0 $?

RELEASE_VERSION=$(readVersion)
echo Releasing version $RELEASE_VERSION

echo Building
$MAVEN -U clean deploy -DdeployAtEnd=true -Prelease,bamboo
failIfReturncodeNot0 $?

echo Committing release
$GIT add pom.xml
failIfReturncodeNot0 $?
$GIT commit -m "[release] set version to $RELEASE_VERSION"
failIfReturncodeNot0 $?

echo Tagging release
$GIT tag -a "relesae/$RELEASE_VERSION" -m "Release version $RELEASE_VERSION"
failIfReturncodeNot0 $?

echo Incrementing snapshot version
$MAVEN build-helper:parse-version versions:set -DnewVersion=\${parsedVersion.majorVersion}.\${parsedVersion.nextMinorVersion}.0-SNAPSHOT
failIfReturncodeNot0 $?

cat <<EOF >CHANGELOG.md
# test-repl $NEXT_SNAPSHOT_VERSION

$(cat CHANGELOG.md)
EOF

echo Committing snapshot version update
NEXT_SNAPSHOT_VERSION=$(readVersion)
$GIT add CHANGELOG.md pom.xml
failIfReturncodeNot0 $?
$GIT commit -m "[release] set next snapshot version to $NEXT_SNAPSHOT_VERSION"
failIfReturncodeNot0 $?

echo Pushing
$GIT push origin
failIfReturncodeNot0 $?
$GIT push origin --tags
failIfReturncodeNot0 $?
