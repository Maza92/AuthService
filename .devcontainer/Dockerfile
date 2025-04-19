FROM mcr.microsoft.com/devcontainers/java:21-bookworm

# Install required packages including native libraries
RUN apt-get update && export DEBIAN_FRONTEND=noninteractive \
    && apt-get -y install --no-install-recommends \
    build-essential \
    gcc \
    libc6-dev \
    libstdc++6 \
    postgresql-client \
    && apt-get clean -y \
    && rm -rf /var/lib/apt/lists/*

ARG GRADLE_VERSION=8.10.2
RUN su vscode -c "umask 0002 && . /usr/local/sdkman/bin/sdkman-init.sh && sdk install gradle ${GRADLE_VERSION}" \
    && mkdir -p /home/vscode/.gradle \
    && chown -R vscode:vscode /home/vscode/.gradle

# Set workspace permissions
RUN mkdir -p /workspace && chown -R vscode:vscode /workspace

USER vscode