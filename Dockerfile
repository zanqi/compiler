FROM mcr.microsoft.com/devcontainers/base:jammy
ENV DEBIAN_FRONTEND=noninteractive
RUN apt-get update
RUN apt-get install -y flex 
RUN apt-get install -y bison 
RUN apt-get install -y build-essential
RUN apt-get install -y csh
RUN apt-get install -y libxaw7-dev vim 

WORKDIR /usr/class/cs143/cool
COPY --chown=root:root . .

WORKDIR /root/cs143

ENTRYPOINT ["/bin/bash"]